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
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mike Zhang on 15/12/2017.
 */

public class NodetoolInfoParser {

    private static final Logger logger = LogManager.getLogger(NodetoolInfoParser.class);

    private JSONObject nodetoolInfoJSON;
    private ArrayList<JSONObject> info_obj_list;

    public NodetoolInfoParser(ArrayList<File> files) {
        parse(files);
    }

    /**
     Generation No          : 1513232422
     Uptime (seconds)       : 67018
     Heap Memory (MB)       : 16407.56 / 24576.00
     Off Heap Memory (MB)   : 59.93

     **/

    /**
     {"info":
        [
            {"nodes":
                [
                    {Generation No : 1513907549,
     Uptime (seconds)       : 18447,
     Heap Memory (MB)       : 5164.60 / 8192.00,
     Off Heap Memory (MB)   : 388.21,
     Data Center            : DC3
     file_id : ip_address
     }
     ],
     "Datacenter":"DC3"},
     {"nodes":
     [
     {
     {
     Generation No          : 1513907549,
     Uptime (seconds)       : 18447,
     Heap Memory (MB)       : 5164.60 / 8192.00,
     Off Heap Memory (MB)   : 388.21,
     Data Center            : DC3
     file_id : ip_address
     }
     }
     {
     ],
     "Datacenter":"DC5"}
     ]
     }

     {
     "info_uptime":" 17016(04 hours 43 mins 36 seconds)",
     "info_generation":" 1511385969(Thu Nov 23 08:26:09 AEDT 2017)",
     "file_id":"13.57.154.111",
     "info_totalheap":" 49152.00",
     "info_usedheap":" 142.27"
     }
     **/
    private void parse(ArrayList<File> files) {
        info_obj_list = new ArrayList<JSONObject>();
        for (File file : files) {
            if (file.getName().equals(ValFactory.INFO)) {
                //logger.info("nodetool info file_name: " + file.getName());
                nodetoolInfoJSON = new JSONObject();

                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String currentLine = scanner.nextLine();
                        if (currentLine.toLowerCase().contains("generation no")) {
                            String[] splitLine = Inspector.splitByColon(currentLine);
                            nodetoolInfoJSON.put(ValFactory.INFO_GENERATION, splitLine[1] + "(" + Inspector.epochToDate(splitLine[1].trim()) + ")");
                            logger.info("Generation No: " + splitLine[1] + "(" + Inspector.epochToDate(splitLine[1].trim()) + ")");
                        }

                        if (currentLine.toLowerCase().contains("uptime")) {
                            String[] splitLine = Inspector.splitByColon(currentLine);

                            nodetoolInfoJSON.put(ValFactory.INFO_UPTIME, splitLine[1] + "(" + Inspector.secToTime(Integer.valueOf(splitLine[1].trim()),true)+ ")");
                            //nodetoolInfoJSON.put(ValFactory.INFO_UPTIME, splitLine[1].trim());
                            // logger.info("NTP status current line: " + currentLine);
                            //logger.info("NTP status:"+ ntpInfoJSON.get(StrFactory.NTPTIME_STAUS).toString());
                            logger.info("Uptime: " + splitLine[1] + "(" + Inspector.secToTime(Integer.valueOf(splitLine[1].trim()), true) + ")");
                        }
                        if (currentLine.toLowerCase().contains("heap memory") && !currentLine.toLowerCase().contains("off heap")) {
                            //logger.info("Offset current line: " + currentLine);
                            String[] splitLine = Inspector.splitByColon(currentLine);

                            String[] splitLine1 = Inspector.splitBySlash(splitLine[1]);
                            //Arrays.sort(splitLine);
                            nodetoolInfoJSON.put(ValFactory.INFO_USEDHEAP, splitLine1[0]);
                            nodetoolInfoJSON.put(ValFactory.INFO_TOTALHEAP, splitLine1[1]);
                            logger.info("Heap Memory: " + splitLine1[0] + "/" + splitLine1[1]);
                        }
                        if (currentLine.toLowerCase().contains("off heap memory")) {
                            String[] splitLine = Inspector.splitByColon(currentLine);
                            nodetoolInfoJSON.put(ValFactory.INFO_OFFHEAP, splitLine[1]);
                            logger.info("Off Heap Memory: " + splitLine[1]);
                        }
                    }
                    logger.info("node ip: " + setIP(file.getAbsolutePath()));
                    nodetoolInfoJSON.put(ValFactory.FILE_ID, setIP(file.getAbsolutePath()));
                    info_obj_list.add(nodetoolInfoJSON);
                } catch (FileNotFoundException fnfe) {
                    logger.error(Inspector.getStackTrace(fnfe));
                }
            }
        }
    }

    public ArrayList<JSONObject> getNodetoolInfoJSONList() {
        return this.info_obj_list;
    }

    private String setIP(String filepath) {
        if (Inspector.foundIPAddress(filepath)) {
            return Inspector.getIPAddress(filepath);
        } else {
            return "no_valid_ip_address";
        }
    }
}
