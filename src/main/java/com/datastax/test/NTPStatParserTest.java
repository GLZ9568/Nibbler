/*
 * Copyright (c)
 *
 * Date: 28/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.NTPStatParser;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mike Zhang on 28/01/2018.
 */

public class NTPStatParserTest extends Test{

    private static final Logger logger = LogManager.getLogger(NTPStatParserTest.class);

    private JSONObject ntpStatJSON;
    private NTPStatParser ntpStatParser = new NTPStatParser();
    public void parseFiles() {

        for(File ntpstatfile : files){

            if(ntpstatfile.getName().contains(ValFactory.NTPSTAT)){

                ntpStatJSON = ntpStatParser.parse(ntpstatfile);
                logger.info(Inspector.getIPAddress(ntpstatfile.getAbsolutePath())
                +": NTP status: " + ntpStatJSON.get(ValFactory.NTPTIME_STAUS) + " NTP offset: " + ntpStatJSON.get(ValFactory.NTPTIME_OFFSET));
            }
        }

    }

    public static void main (String[] args) {
        //YamlFileParserTest yfpt = new YamlFileParserTest();
        //yfpt.initiate();
       // yfpt.parseFiles();
        NTPStatParserTest ntpStatParserTest = new NTPStatParserTest();
        ntpStatParserTest.initiate();
        ntpStatParserTest.parseFiles();
        System.exit(0);
    }
}
