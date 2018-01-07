/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.JSONFileParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Created by Chun Gao on 3/01/2018
 */

public class JSONFileParserTest extends Test{
    private static final Logger logger = LogManager.getLogger(JSONFileParserTest.class);

    private JSONFileParser JSONFileParser;
    private JSONObject clusterInfo;

    public void parseFiles() {
        initiate();
        clusterInfo = fileFactory.getClusterInfoJSON();

        logger.debug("ClusterInfo: " + clusterInfo);
    }

    public static void main(String[] args) {
        JSONFileParserTest JSONFileParserTest = new JSONFileParserTest();
        JSONFileParserTest.parseFiles();
    }
}
