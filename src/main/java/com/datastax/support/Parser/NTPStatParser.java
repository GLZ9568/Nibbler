/*
 * Copyright (c)
 *
 * Date: 27/1/2018
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
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mike Zhang on 27/01/2018.
 */

public class NTPStatParser {

    private static final Logger logger = LogManager.getLogger(NTPStatParser.class);

    private JSONObject ntpStatJSON;
    /*
    *
    * ntpstat
    * exit status: 1
    * stdout:
    * unsynchronised
    * time server re-starting
    * polling server every 8 s
    *
    *
    *
    * synchronised to NTP server (10.180.194.72) at stratum 2
     * time correct to within 45 ms
     * polling server every 1024 s
     *
     *
     * Exception running ntpstat: java.io.IOException: Cannot run program "ntpstat": error=2, No such file or directory
     *
    * */
    public JSONObject parse(File file) {
        ntpStatJSON = new JSONObject();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                if (currentLine.toLowerCase().contains("unsynchronised")) {
                    ntpStatJSON.put(ValFactory.NTPTIME_STAUS, "ntp is down");
                    break;
                } else if (currentLine.toLowerCase().contains("time correct")) {

                    ntpStatJSON.put(ValFactory.NTPTIME_STAUS, "ntp is running");

                    Pattern p = Pattern.compile("-?\\d+");
                    Matcher m = p.matcher(currentLine);
                    String offset ="";
                   while (m.find()) {
                        offset = m.group(0);
                   }
                   // String[] splitLine = Inspector.splitBySpace(currentLine);
                   // Arrays.sort(splitLine);
                    ntpStatJSON.put(ValFactory.NTPTIME_OFFSET, offset + " ms");
                   // logger.info("NTP status current line: " + currentLine);
                  //  logger.info("NTP status:"+ ntpStatJSON.get(ValFactory.NTPTIME_OFFSET).toString());

                }
                else if(currentLine.toLowerCase().contains("exception"))
                {
                    String[] splitLine = Inspector.splitByColon(currentLine);
                    ntpStatJSON.put(ValFactory.NTPTIME_STAUS, splitLine[0] + ": " + splitLine[3]);
                }


            }
           //logger.info(Inspector.getIPAddress(file.getAbsolutePath())+" NTP Stat Summary: "+ ntpStatJSON.toString());
        } catch (FileNotFoundException fnfe) {
            logger.error(Inspector.getStackTrace(fnfe));
        }

        return ntpStatJSON;

    }

}
