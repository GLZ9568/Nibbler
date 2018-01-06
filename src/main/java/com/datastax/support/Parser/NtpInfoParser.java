/*
 * Copyright (c)
 *
 * Date: 7/12/2017
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

/**
 * Created by Mike Zhang on 7/12/2017.
 */

public class NtpInfoParser {

    private static final Logger logger = LogManager.getLogger(NtpInfoParser.class);

    private JSONObject ntpInfoJSON;

    /**
     * NTP ERROR EXAMPLE:
     * ntp_gettime() returns code 5 (ERROR)
     * time ddbcc84a.cd33e000  Mon, Nov 20 2017  3:19:38.801, (.801573),
     * maximum error 16000000 us, estimated error 16 us, TAI offset 0
     * ntp_adjtime() returns code 5 (ERROR)
     * modes 0x0 (),
     * offset 0.000 us, frequency 0.000 ppm, interval 1 s,
     * maximum error 16000000 us, estimated error 16 us,
     * status 0x41 (PLL,UNSYNC),
     * time constant 7, precision 1.000 us, tolerance 500 ppm,
     * <p>
     * NTP WORKING EXAMPLE:
     * ntp_gettime() returns code 0 (OK)
     * time ddbf2db3.43f487e4  Tue, Nov 21 2017 22:56:51.265, (.265450835),
     * maximum error 389700 us, estimated error 1277 us, TAI offset 0
     * ntp_adjtime() returns code 0 (OK)
     * modes 0x0 (),
     * offset 2017.787 us, frequency -45.165 ppm, interval 1 s,
     * maximum error 389700 us, estimated error 1277 us,
     * status 0x2001 (PLL,NANO),
     * time constant 10, precision 0.001 us, tolerance 500 ppm,
     **/
    public JSONObject parse(File file) {
        ntpInfoJSON = new JSONObject();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                if (currentLine.toLowerCase().contains("ntp_gettime") && currentLine.contains("ERROR")) {
                    ntpInfoJSON.put(ValFactory.NTPTIME_STAUS, "ntp is down");
                    break;
                } else if (currentLine.toLowerCase().contains("ntp_gettime") && currentLine.contains("OK")) {

                    ntpInfoJSON.put(ValFactory.NTPTIME_STAUS, "ntp is running");
                   // logger.info("NTP status current line: " + currentLine);
                    //logger.info("NTP status:"+ ntpInfoJSON.get(StrFactory.NTPTIME_STAUS).toString());

                }
               else if (currentLine.toLowerCase().contains("offset")&&currentLine.toLowerCase().contains("frequency")) {
                    //logger.info("Offset current line: " + currentLine);
                    String[] splitLine = Inspector.splitByComma(currentLine);
                    Arrays.sort(splitLine);
                    ntpInfoJSON.put(ValFactory.NTPTIME_OFFSET, splitLine[0]);
                   // logger.info("NTP offset: " + ntpInfoJSON.get(StrFactory.NTPTIME_OFFSET).toString());
                }
                else if(currentLine.toLowerCase().contains("exception"))
                {
                    String[] splitLine = Inspector.splitByColon(currentLine);
                    ntpInfoJSON.put(ValFactory.NTPTIME_STAUS, splitLine[0] + ": " + splitLine[3]);
                }


            }
            logger.info(Inspector.getIPAddress(file.getAbsolutePath())+" NTP Summary: "+ ntpInfoJSON.toString());
        } catch (FileNotFoundException fnfe) {
            logger.error(Inspector.getStackTrace(fnfe));
        }

        return ntpInfoJSON;

    }


    public JSONObject getNodetoolStatusJSON() {
        return this.ntpInfoJSON;
    }
}
