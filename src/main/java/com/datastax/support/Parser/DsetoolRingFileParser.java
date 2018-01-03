/*
 * Copyright (c)
 *
 * Date: 26/12/2017
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
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Chun Gao on 26/12/2017
 */

public class DsetoolRingFileParser {

    private static Logger logger = LogManager.getLogger(DsetoolRingFileParser.class);

    private JSONObject dsetoolRingJSON;
    private JSONArray dsetoolRingJSONArray;
    private JSONObject nodeJSON;

    public DsetoolRingFileParser(ArrayList<File> files) {
        parse(files);
    }

    /**
     {"file_id":"34.239.74.78","file_name":"ring","file_path":"./nodes/34.239.74.78/dsetool/ring",
     "ring":
        [
            {"Status":"Up","Rack":"1c","Load":"795.63 MB","Address":"13.57.154.111","VNodes":"256","State":"Normal","Owns":"14.29%","DC":"us-west-1","Workload":"Cassandra"},
            {"Status":"Up","Rack":"1a","Load":"629.17 MB","Address":"13.57.164.148","VNodes":"256","State":"Normal","Owns":"17.02%","DC":"us-west-1","Workload":"Cassandra"},
            {"Status":"Up","Rack":"1c","Load":"326.13 MB","Address":"34.195.101.2","VNodes":"256","State":"Normal","Owns":"16.25%","DC":"us-east-1","Workload":"Cassandra"},
            {"Status":"Up","Rack":"1e","Load":"358.83 MB","Address":"34.238.217.214","VNodes":"256","State":"Normal","Owns":"17.82%","DC":"us-east-1","Workload":"Cassandra"},
            {"Status":"Up","Rack":"1d","Load":"509.83 MB","Address":"34.239.74.78","VNodes":"256","State":"Normal","Owns":"17.84%","DC":"us-east-1","Workload":"Cassandra"},
            {"Status":"Up","Rack":"1a","Load":"1.01 GB","Address":"52.9.233.65","VNodes":"256","State":"Normal","Owns":"16.77%","DC":"us-west-1","Workload":"Cassandra"}
        ],
     "file_name":"ring","file_id":"13.57.154.111"
     }
     **/
    public void parse (ArrayList<File> files) {
        dsetoolRingJSON = new JSONObject();

        boolean valid = false;

        for (File file : files) {
            if (file.getAbsolutePath().contains(ValFactory.DSETOOL) && file.getName().equals(ValFactory.RING) && !valid) {
                dsetoolRingJSONArray = new JSONArray();

                dsetoolRingJSON.put(ValFactory.FILE_PATH, file.getAbsolutePath());
                dsetoolRingJSON.put(ValFactory.FILE_NAME, file.getName());
                dsetoolRingJSON.put(ValFactory.FILE_ID, Inspector.getFileID(file));

                try {
                    Scanner scanner = new Scanner(file);
                    ArrayList<String> keys = new ArrayList<String>();
                    ArrayList<String> values;

                    while (scanner.hasNextLine()) {
                        ArrayList<String> splitLine = new ArrayList<String>(Arrays.asList(Inspector.splitBySpace(scanner.nextLine())));
                        if (splitLine.size() == 9) {
                            if (splitLine.get(0).toLowerCase().equals(ValFactory.ADDRESS.toLowerCase()) && !valid) {
                                valid = true;
                                keys = splitLine;
                            }
                        }else if (splitLine.size() == 10) {
                            values = new ArrayList<String>();
                            for (int i=0; i<keys.size(); i++) {
                                if(i<6) {
                                    values.add(splitLine.get(i));
                                } else if (i==6) {
                                    values.add(splitLine.get(i) + " " + splitLine.get(i+1));
                                } else {
                                    values.add(splitLine.get(i+1));
                                }
                            }

                            if(keys.size() == 9) {
                                nodeJSON = new JSONObject();
                                for (int i = 0; i < keys.size(); i++) {
                                    nodeJSON.put(keys.get(i), values.get(i));
                                }
                                dsetoolRingJSONArray.add(nodeJSON);
                            }
                        }
                    }
                    dsetoolRingJSON.put(ValFactory.RING, dsetoolRingJSONArray);
                } catch (FileNotFoundException fnfe) {
                    logger.debug(fnfe);
                }
            }
        }
    }

    public JSONObject initiatePadding(ArrayList<String> keys, int pad) {
        JSONObject padding = new JSONObject();

        for (String key : keys) {
            padding.put(key, key.length() + pad);
        }

        return padding;
    }

    public JSONObject getDsetoolRingJSON() {
        return this.dsetoolRingJSON;
    }
}
