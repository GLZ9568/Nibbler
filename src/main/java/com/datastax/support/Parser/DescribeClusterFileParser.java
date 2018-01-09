/*
 * Copyright (c)
 *
 * Date: 28/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Chun Gao on 26/12/2017
 */

public class DescribeClusterFileParser extends FileParser {

    private static final Logger logger = LogManager.getLogger(DescribeClusterFileParser.class);

    private ArrayList<JSONObject> describeclusterJSONList;
    private JSONObject describeclusterJSON;
    private JSONObject valueJSON;

    public DescribeClusterFileParser(ArrayList<File> files) {
        super(files);
        parse();
    }

    private void parse() {
        describeclusterJSONList = new ArrayList<JSONObject>();

        /**
         {
         "file_id":"13.57.154.111","file_name":"describecluster","file_path":"./nodes/13.57.154.111/nodetool/describecluster",
         "Name":"nes_staging", "Snitch":"org.apache.cassandra.locator.DynamicEndpointSnitch","Partitioner":"org.apache.cassandra.dht.Murmur3Partitioner",
         "Schema versions":
            {
            "87420b21-b4ba-3d48-a14c-3c4c1576d8d8":"[34.195.101.2, 34.239.74.78, 13.57.154.111, 34.238.217.214, 52.9.233.65, 13.57.164.148]",
            "UNREACHABLE":"[54.183.65.53, 52.53.82.230, 52.8.223.108, 54.152.227.113, 54.174.83.112, 54.235.227.84]"
            }
         }
         **/

        for (File file : files) {
            if (file.getAbsolutePath().contains(ValFactory.DESCRIBECLUSTER)) {
                describeclusterJSON = new JSONObject();
                valueJSON = new JSONObject();

                describeclusterJSON.put(ValFactory.FILE_PATH, file.getAbsolutePath());
                describeclusterJSON.put(ValFactory.FILE_NAME, file.getName());
                describeclusterJSON.put(ValFactory.FILE_ID, Inspector.getFileID(file));

                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        ArrayList<String> values = new ArrayList<String>(Arrays.asList(Inspector.splitByColon(scanner.nextLine())));
                        if (values.size() == 2) {
                            String key = values.get(0).trim();
                            String value = values.get(1).trim();
                            if (Inspector.foundIPAddress(value)) {
                                valueJSON.put(key, value);
                            } else {
                                describeclusterJSON.put(key, value);
                            }
                        }
                    }
                    describeclusterJSON.put(ValFactory.SCHEMA_VERSIONS, valueJSON);
                } catch (FileNotFoundException fnfe) {
                    logCheckedException(logger, fnfe);
                }
                describeclusterJSONList.add(describeclusterJSON);
            }
        }
    }

    public ArrayList<JSONObject> getDescribeClusterJSONList() {
        return describeclusterJSONList;
    }
}
