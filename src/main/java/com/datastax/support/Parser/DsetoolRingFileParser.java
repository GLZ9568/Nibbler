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
import com.datastax.support.Util.StrFactory;
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

    private static Logger logger = LogManager.getLogger(NodetoolStatusParser.class);

    private JSONObject dsetoolRingJSON;
    private JSONArray dsetoolRingJSONArray;
    private JSONObject nodeJSON;

    /**
     {"file_id":"34.239.74.78","file_name":"ring","file_path":"./nodes/34.239.74.78/dsetool/ring",
     "status":
        [
            {"nodes":
                [
                    {"Rack":"1d","stat":"UN","Load":"509.83 MB","Address":"34.239.74.78","Host ID":"d9703f05-6e22-46e2-979b-b72d9fba3a0a","Owns":"?","Tokens":"256"},
                    {"Rack":"1c","stat":"UN","Load":"326.13 MB","Address":"34.195.101.2","Host ID":"3250f313-af2f-4499-a3ec-3952c8be8c73","Owns":"?","Tokens":"256"},
                    {"Rack":"1e","stat":"UN","Load":"358.83 MB","Address":"34.238.217.214","Host ID":"97a3bce3-bacf-4277-a1b2-54312a0fed87","Owns":"?","Tokens":"256"}
                ],
            "Datacenter":"us-east-1"
            },
            {"nodes":
                [
                    {"Rack":"1c","stat":"UN","Load":"795.63 MB","Address":"13.57.154.111","Host ID":"f82ff87f-f036-413b-b432-161f9da47e10","Owns":"?","Tokens":"256"},
                    {"Rack":"1a","stat":"UN","Load":"1.01 GB","Address":"52.9.233.65","Host ID":"801ddb04-649c-440c-845d-247f672854e6","Owns":"?","Tokens":"256"},
                    {"Rack":"1a","stat":"UN","Load":"629.17 MB","Address":"13.57.164.148","Host ID":"21d79763-cbcc-4a7b-a012-491a11e9422f","Owns":"?","Tokens":"256"}
                ],
            "Datacenter":"us-west-1"
            }
        ]
     }
     **/
    public void parse (ArrayList<File> files) {
        dsetoolRingJSON = new JSONObject();

        boolean valid = false;

        for (File file : files) {
            if (file.getAbsolutePath().contains(StrFactory.DSETOOL) && file.getName().equals(StrFactory.RING) && !valid) {
                dsetoolRingJSONArray = new JSONArray();

                dsetoolRingJSON.put(StrFactory.FILE_PATH, file.getAbsolutePath());
                dsetoolRingJSON.put(StrFactory.FILE_NAME, file.getName());
                dsetoolRingJSON.put(StrFactory.FILE_ID, Inspector.getFileID(file));

                try {
                    Scanner scanner = new Scanner(file);
                    ArrayList<String> keys = new ArrayList<String>();
                    ArrayList<String> values;

                    while (scanner.hasNextLine()) {
                        ArrayList<String> splitLine = new ArrayList<String>(Arrays.asList(Inspector.splitBySpace(scanner.nextLine())));
                        if (splitLine.size() == 9) {
                            if (splitLine.get(0).toLowerCase().equals(StrFactory.ADDRESS.toLowerCase()) && !valid) {
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
                    dsetoolRingJSON.put(StrFactory.RING, dsetoolRingJSONArray);
                } catch (FileNotFoundException fnfe) {
                    logger.debug(fnfe);
                }
            }
        }
    }

    public JSONObject getDsetoolRingJSON() {
        return this.dsetoolRingJSON;
    }
}
