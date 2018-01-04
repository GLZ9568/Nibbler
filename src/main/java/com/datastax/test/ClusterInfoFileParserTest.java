/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.ClusterInfoFileParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Created by Chun Gao on 3/01/2018
 */

public class ClusterInfoFileParserTest extends Test{
    private static final Logger logger = LogManager.getLogger(ClusterInfoFileParserTest.class);

    private ClusterInfoFileParser clusterInfoFileParser;
    private JSONObject clusterInfo;

    public void parseFiles() {
        initiate();
        clusterInfoFileParser = new ClusterInfoFileParser(files);
        clusterInfo = clusterInfoFileParser.getClusterInfoJSON();

        logger.debug("ClusterInfo: " + clusterInfo);
    }

    public static void main(String[] args) {
        ClusterInfoFileParserTest clusterInfoFileParserTest = new ClusterInfoFileParserTest();
        clusterInfoFileParserTest.parseFiles();
    }
}
