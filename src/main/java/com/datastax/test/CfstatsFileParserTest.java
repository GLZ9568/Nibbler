/*
 * Copyright (c)
 *
 * Date: 15/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.CfstatsFileParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 15/12/17
 */

public class CfstatsFileParserTest extends Test {

    private static final Logger logger = LogManager.getLogger(CfstatsFileParserTest.class);

    private ArrayList<JSONObject> cfstatsList = new ArrayList<JSONObject>();

    public void parseFiles () {
        cfstatsList = fileFactory.getCfstatsList();

        JSONObject cfstats = cfstatsList.get(0);

        logger.debug("1st cfstats read in: " + cfstats);
    }

    public static void main (String[] args) {
        CfstatsFileParserTest cfpt = new CfstatsFileParserTest();
        cfpt.initiate();
        cfpt.parseFiles();
    }
}
