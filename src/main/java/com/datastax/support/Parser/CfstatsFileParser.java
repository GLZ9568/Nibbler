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
import com.datastax.support.Util.StrFactory;
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

public class CfstatsFileParser {

    private static Logger logger = LogManager.getLogger(CfstatsFileParser.class);

    private ArrayList<JSONObject> nodetoolCfstats;
    private JSONObject nodetoolStatusJSON;
    private JSONArray keyspaceArray;
    private JSONObject keyspaceJSON;
    private JSONArray tableArray;
    private JSONObject tableJSON;

    /**
     {
     "Keyspaces":
        [
            {"Keyspace":"k1","Read Count":"0",...
            "Tables":
                [
                    {"Table":"t1","Local write latency":"NaN ms",...},
                    {"Table":"t2","Local write latency":"NaN ms",...}
                ],
            },
            {"Keyspace":"k2","Read Count":"0",...
            "Tables":
                [
                    {"Table":"t3","Local write latency":"NaN ms",...}
                    {"Table":"t4","Local write latency":"NaN ms",...}
                ],
            }
        ]
     }
     **/
    public void parse (ArrayList<File> files) {
        nodetoolCfstats = new ArrayList<JSONObject>();

        for (File file : files) {
            if (file.getAbsolutePath().contains(StrFactory.NODETOOL) && file.getName().equals(StrFactory.CFSTATS)) {

                nodetoolStatusJSON = new JSONObject();
                keyspaceArray = new JSONArray();

                keyspaceJSON = new JSONObject();
                tableArray = new JSONArray();
                tableJSON = new JSONObject();

                boolean isKeyspaceData = false;
                boolean isTableData = false;

                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String currentLine = scanner.nextLine();
                        String[] splitLine = Inspector.splitByColon(currentLine);

                        if(splitLine.length == 2) {
                            String key = splitLine[0];
                            String value = splitLine[1];

                            if(!isKeyspaceData && !isTableData) {
                                if(key.trim().toLowerCase().equals(StrFactory.KEYSPACE.toLowerCase())) {
                                    isKeyspaceData = true;
                                    isTableData = false;
                                    keyspaceJSON.put(key.trim(), value.trim());
                                } else {
                                    nodetoolStatusJSON.put(key.trim(), value.trim());
                                }
                            } else if (isKeyspaceData && !isTableData){
                                if(key.trim().toLowerCase().equals(StrFactory.TABLE.toLowerCase())) {
                                    isTableData = true;
                                    tableJSON.put(key.trim(), value.trim());
                                } else {
                                    keyspaceJSON.put(key.trim(), value.trim());
                                }
                            } else if (isKeyspaceData && isTableData) {
                                if (key.trim().toLowerCase().equals(StrFactory.TABLE.toLowerCase())) {
                                    if (!tableJSON.isEmpty()) {
                                        tableArray.add(tableJSON);
                                        tableJSON = new JSONObject();
                                    }
                                    tableJSON.put(key.trim(), value.trim());
                                } else if (key.trim().toLowerCase().equals(StrFactory.KEYSPACE.toLowerCase())) {
                                    isKeyspaceData = true;
                                    isTableData = false;
                                    tableArray.add(tableJSON);
                                    tableJSON = new JSONObject();
                                    keyspaceJSON.put(StrFactory.TABLES, tableArray);
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
                    keyspaceJSON.put(StrFactory.TABLES, tableArray);
                    keyspaceArray.add(keyspaceJSON);

                    nodetoolStatusJSON.put(StrFactory.KEYSPACES, keyspaceArray);
                    nodetoolCfstats.add(nodetoolStatusJSON);

                } catch (FileNotFoundException fnfe) {
                    logger.debug(fnfe);
                }
            }
        }
    }

    public ArrayList<JSONObject> getNodetoolCfstats() {
        return this.nodetoolCfstats;
    }
}
