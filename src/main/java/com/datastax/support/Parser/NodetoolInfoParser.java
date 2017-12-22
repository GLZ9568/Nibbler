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
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Mike Zhang on 15/12/2017.
 */

public class NodetoolInfoParser {

    private static Logger logger = LogManager.getLogger(NodetoolStatusParser.class);

    private JSONObject nodetoolInfoJSON;

    /**
     Generation No          : 1513232422
     Uptime (seconds)       : 67018
     Heap Memory (MB)       : 16407.56 / 24576.00
     Off Heap Memory (MB)   : 59.93

     **/
    public JSONObject parse(File file) {
        nodetoolInfoJSON = new JSONObject();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                if (currentLine.toLowerCase().contains("Generation No")) {
                    String[] splitLine = Inspector.splitByColon(currentLine);
                    nodetoolInfoJSON.put(StrFactory.INFO_GENERATION, Inspector.epochtoDate(splitLine[1]));

                }

                if (currentLine.toLowerCase().contains("Uptime")) {

                    String[] splitLine = Inspector.splitByColon(currentLine);

                    nodetoolInfoJSON.put(StrFactory.INFO_UPTIME, Inspector.secToTime(Integer.valueOf(splitLine[1])));
                    // logger.info("NTP status current line: " + currentLine);
                    //logger.info("NTP status:"+ ntpInfoJSON.get(StrFactory.NTPTIME_STAUS).toString());

                }
                if (currentLine.toLowerCase().contains("Heap Memory")&&!currentLine.toLowerCase().contains("Off Heap")) {
                    //logger.info("Offset current line: " + currentLine);
                    String[] splitLine = Inspector.splitByColon(currentLine);

                    String[] splitLine1 = Inspector.splitBySlash(splitLine[1]);
                    //Arrays.sort(splitLine);
                    nodetoolInfoJSON.put(StrFactory.INFO_USEDHEAP, splitLine1[0]);
                    nodetoolInfoJSON.put(StrFactory.INFO_TOTALHEAP, splitLine1[1]);
                    // logger.info("NTP offset: " + ntpInfoJSON.get(StrFactory.NTPTIME_OFFSET).toString());
                }
                if(currentLine.toLowerCase().contains("Off Heap Memory"))
                {
                    String[] splitLine = Inspector.splitByColon(currentLine);
                    nodetoolInfoJSON.put(StrFactory.INFO_OFFHEAP, splitLine[1]);
                }


            }
           // logger.info(Inspector.getIPAddress(file.getAbsolutePath())+" NTP Summary: "+ nodetoolInfoJSON.toString());
        } catch (FileNotFoundException fnfe) {
            logger.debug(fnfe);
        }

        return nodetoolInfoJSON;

    }


    public JSONObject getNodetoolStatusJSON() {
        return this.nodetoolInfoJSON;
    }
}
