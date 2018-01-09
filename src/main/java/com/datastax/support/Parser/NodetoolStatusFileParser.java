/*
 * Copyright (c)
 *
 * Date: 24/11/2017
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


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

/**
 * Created by Chun Gao on 24/11/2017
 */

public class NodetoolStatusFileParser extends FileParser {

    private static final Logger logger = LogManager.getLogger(NodetoolStatusFileParser.class);

    private JSONObject nodetoolStatusJSON;
    private JSONArray nodeJSONArray;

    public NodetoolStatusFileParser(ArrayList<File> files) {
        super(files);
        parse();
    }

    /**
    {"file_id":"34.239.74.78","file_name":"status","file_path":"./nodes/34.239.74.78/nodetool/status",
     "status":
        [
            {"nodes":
                [
                    {"Rack":"1d","U/D":"UN","Load":"509.83 MB","Address":"34.239.74.78","Host ID":"d9703f05-6e22-46e2-979b-b72d9fba3a0a","Owns":"?","Tokens":"256"},
                    {"Rack":"1c","U/D":"UN","Load":"326.13 MB","Address":"34.195.101.2","Host ID":"3250f313-af2f-4499-a3ec-3952c8be8c73","Owns":"?","Tokens":"256"},
                    {"Rack":"1e","U/D":"UN","Load":"358.83 MB","Address":"34.238.217.214","Host ID":"97a3bce3-bacf-4277-a1b2-54312a0fed87","Owns":"?","Tokens":"256"}
                ],
            "Datacenter":"us-east-1",
            "Padding":{"Rack":6,"Load":11,"Address":16,"Host ID":38,"Owns":6,"U\/D":5,"Tokens":8}
            },
            {"nodes":
                [
                    {"Rack":"1c","U/D":"UN","Load":"795.63 MB","Address":"13.57.154.111","Host ID":"f82ff87f-f036-413b-b432-161f9da47e10","Owns":"?","Tokens":"256"},
                    {"Rack":"1a","U/D":"UN","Load":"1.01 GB","Address":"52.9.233.65","Host ID":"801ddb04-649c-440c-845d-247f672854e6","Owns":"?","Tokens":"256"},
                    {"Rack":"1a","U/D":"UN","Load":"629.17 MB","Address":"13.57.164.148","Host ID":"21d79763-cbcc-4a7b-a012-491a11e9422f","Owns":"?","Tokens":"256"}
                ],
            "Datacenter":"us-west-1"
            "Padding":{"Rack":6,"Load":11,"Address":15,"Host ID":38,"Owns":6,"U\/D":5,"Tokens":8}
            }
        ]
    }
    **/
    private void parse () {
        nodetoolStatusJSON = new JSONObject();
        nodeJSONArray = new JSONArray();

        boolean valid = false;

        for (File file : files) {
            if (file.getAbsolutePath().contains(ValFactory.NODETOOL) && file.getName().equals(ValFactory.STATUS) && !valid) {

                JSONArray nodeArray = new JSONArray();
                JSONObject padding = initiatePadding(ValFactory.NODETOOLTATUS);

                nodetoolStatusJSON.put(ValFactory.FILE_PATH, file.getAbsolutePath());
                nodetoolStatusJSON.put(ValFactory.FILE_NAME, file.getName());
                nodetoolStatusJSON.put(ValFactory.FILE_ID, Inspector.getFileID(file));

                try {
                    Scanner scanner = new Scanner(file);
                    JSONObject dcInfo = new JSONObject();

                    while (scanner.hasNextLine()) {
                        String currentLine = scanner.nextLine();
                        String[] splitLine = Inspector.splitBySpace(currentLine);
                        if (currentLine.toLowerCase().contains(ValFactory.DATACENTER.toLowerCase())) {
                            if(!nodeArray.isEmpty()) {
                                dcInfo.put(ValFactory.NODES, nodeArray);
                                dcInfo.put(ValFactory.PADDING, padding);
                                nodeJSONArray.add(dcInfo);
                                dcInfo = new JSONObject();
                            }
                            valid = true;

                            dcInfo.put(ValFactory.DATACENTER, splitLine[1]);

                            nodeArray = new JSONArray();
                            padding = initiatePadding(ValFactory.NODETOOLTATUS);
                        }
                        if (ValFactory.NODESTATUS.contains(splitLine[0])) {
                            JSONObject nodeInfo = new JSONObject();
                            nodeInfo.put(ValFactory.UD, splitLine[0]);
                            padding.put(ValFactory.UD, (Integer) padding.get(ValFactory.UD) > splitLine[0].length() + ValFactory.PAD ? padding.get(ValFactory.UD) : splitLine[0].length() + ValFactory.PAD);
                            nodeInfo.put(ValFactory.ADDRESS, splitLine[1]);
                            padding.put(ValFactory.ADDRESS, (Integer) padding.get(ValFactory.ADDRESS) > splitLine[1].length() + ValFactory.PAD ? padding.get(ValFactory.ADDRESS) : splitLine[1].length() + ValFactory.PAD);
                            nodeInfo.put(ValFactory.LOAD, splitLine[2] + " " + splitLine[3]);
                            padding.put(ValFactory.LOAD, (Integer) padding.get(ValFactory.LOAD) > splitLine[2].length() + 1 + splitLine[3].length() + ValFactory.PAD ? padding.get(ValFactory.LOAD) : splitLine[2].length() + 1 + splitLine[3].length() + ValFactory.PAD);
                            nodeInfo.put(ValFactory.TOKENS, splitLine[4]);
                            padding.put(ValFactory.TOKENS, (Integer) padding.get(ValFactory.TOKENS) > splitLine[4].length() + ValFactory.PAD ? padding.get(ValFactory.TOKENS) : splitLine[4].length() + ValFactory.PAD);
                            nodeInfo.put(ValFactory.OWNS, splitLine[5]);
                            padding.put(ValFactory.OWNS, (Integer) padding.get(ValFactory.OWNS) > splitLine[5].length() + ValFactory.PAD ? padding.get(ValFactory.OWNS) : splitLine[5].length() + ValFactory.PAD);
                            nodeInfo.put(ValFactory.HOST_ID, splitLine[6]);
                            padding.put(ValFactory.HOST_ID, (Integer) padding.get(ValFactory.HOST_ID) > splitLine[6].length() + ValFactory.PAD ? padding.get(ValFactory.HOST_ID) : splitLine[6].length() + ValFactory.PAD);
                            nodeInfo.put(ValFactory.RACK, splitLine[7]);
                            padding.put(ValFactory.RACK, (Integer) padding.get(ValFactory.RACK) > splitLine[7].length() + ValFactory.PAD ? padding.get(ValFactory.RACK) : splitLine[7].length() + ValFactory.PAD);

                            nodeArray.add(nodeInfo);
                        }
                    }
                    dcInfo.put(ValFactory.NODES, nodeArray);
                    dcInfo.put(ValFactory.PADDING, padding);
                    nodeJSONArray.add(dcInfo);
                    nodetoolStatusJSON.put(ValFactory.STATUS, nodeJSONArray);
                } catch (FileNotFoundException fnfe) {
                    logCheckedException(logger, fnfe);
                }
            }
        }
    }

    public JSONObject getNodetoolStatusJSON() {
        return this.nodetoolStatusJSON;
    }
}
