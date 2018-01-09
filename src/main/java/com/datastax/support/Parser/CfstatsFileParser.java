/*
 * Copyright (c)
 *
 * Date: 15/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Chun Gao on 15/12/17
 */

public class CfstatsFileParser extends FileParser{

    private static final Logger logger = LogManager.getLogger(CfstatsFileParser.class);

    private ArrayList<JSONObject> cfstats;
    private JSONObject nodetoolStatusJSON;
    private JSONArray keyspaceArray;
    private JSONObject keyspaceJSON;
    private JSONArray tableArray;
    private JSONObject tableJSON;

    public CfstatsFileParser (ArrayList<File> files) {
        super(files);
        parse();
    }

    /**
     {
     "file_id":"13.57.154.111","file_name":"cfstats","file_path":"./nodes/13.57.154.111/nodetool/cfstats",
     "Keyspaces":
        [
            {"Keyspace":"registry","Read Count":"0",...
            "Tables":
                [
                    {"Table":"image_labels","SSTable count":"0","Space used (live)":"0",...},
                ],
            },
            {"Keyspace":"csr","Read Count":"0",...
            "Tables":
                [
                    {"Table":"configuration_registration","SSTable count":"3","Space used (live)":"27983",...},
                ],
            },
        ]
     }
     **/
    private void parse () {
        cfstats = new ArrayList<JSONObject>();

        for (File file : files) {
            if (file.getAbsolutePath().contains(ValFactory.NODETOOL) && file.getName().equals(ValFactory.CFSTATS)) {

                nodetoolStatusJSON = new JSONObject();
                keyspaceArray = new JSONArray();

                keyspaceJSON = new JSONObject();
                tableArray = new JSONArray();
                tableJSON = new JSONObject();

                boolean isKeyspaceData = false;
                boolean isTableData = false;

                nodetoolStatusJSON.put(ValFactory.FILE_PATH, file.getAbsolutePath());
                nodetoolStatusJSON.put(ValFactory.FILE_NAME, file.getName());
                nodetoolStatusJSON.put(ValFactory.FILE_ID, Inspector.getFileID(file));

                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String currentLine = scanner.nextLine();
                        String[] splitLine = Inspector.splitByColon(currentLine);

                        if(splitLine.length == 2) {
                            String key = splitLine[0];
                            String value = splitLine[1];

                            if(!isKeyspaceData && !isTableData) {
                                if(key.trim().toLowerCase().equals(ValFactory.KEYSPACE.toLowerCase())) {
                                    isKeyspaceData = true;
                                    isTableData = false;
                                    keyspaceJSON.put(key.trim(), value.trim());
                                } else {
                                    nodetoolStatusJSON.put(key.trim(), value.trim());
                                }
                            } else if (isKeyspaceData && !isTableData){
                                if(key.trim().toLowerCase().equals(ValFactory.TABLE.toLowerCase())) {
                                    isTableData = true;
                                    tableJSON.put(key.trim(), value.trim());
                                } else {
                                    keyspaceJSON.put(key.trim(), value.trim());
                                }
                            } else if (isKeyspaceData && isTableData) {
                                if (key.trim().toLowerCase().equals(ValFactory.TABLE.toLowerCase())) {
                                    if (!tableJSON.isEmpty()) {
                                        tableArray.add(tableJSON);
                                        tableJSON = new JSONObject();
                                    }
                                    tableJSON.put(key.trim(), value.trim());
                                } else if (key.trim().toLowerCase().equals(ValFactory.KEYSPACE.toLowerCase())) {
                                    isKeyspaceData = true;
                                    isTableData = false;
                                    tableArray.add(tableJSON);
                                    tableJSON = new JSONObject();
                                    keyspaceJSON.put(ValFactory.TABLES, tableArray);
                                    tableArray = new JSONArray();
                                    keyspaceArray.add(keyspaceJSON);
                                    keyspaceJSON = new JSONObject();
                                    keyspaceJSON.put(key.trim(), value.trim());
                                } else {
                                    tableJSON.put(key.trim(), value.trim());
                                }
                            }
                        }
                    }

                    tableArray.add(tableJSON);
                    keyspaceJSON.put(ValFactory.TABLES, tableArray);
                    keyspaceArray.add(keyspaceJSON);

                    nodetoolStatusJSON.put(ValFactory.KEYSPACES, keyspaceArray);
                    cfstats.add(nodetoolStatusJSON);

                } catch (FileNotFoundException fnfe) {
                    logCheckedException(logger, fnfe);
                }
            }
        }
    }

    public ArrayList<JSONObject> getCfstatsList() {
        return this.cfstats;
    }
}
