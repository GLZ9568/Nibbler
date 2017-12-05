/*
 * Copyright (c)
 *
 * Date: 30/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.NodeToolFileParser;
import com.datastax.support.Util.StrFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Chun Gao on 30/11/17
 */

public class NodeToolFileParseTest extends Test{
    private static final Logger logger = LogManager.getLogger(NodeToolFileParseTest.class);

    private JSONObject nodetoolStatusJSON = new JSONObject();

    public void parseFiles() {
        NodeToolFileParser nodeToolFileParser = new NodeToolFileParser();
        nodeToolFileParser.parse(files);
        nodetoolStatusJSON = nodeToolFileParser.getNodetoolStatusJSON();
        logger.debug(nodetoolStatusJSON);
    }

    public void readNodetoolStatusJSON() {
        logger.debug(nodetoolStatusJSON);
        logger.debug(nodetoolStatusJSON.get(StrFactory.STATUS));
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(StrFactory.STATUS);
        logger.debug("JSONArray Size: " + dcArray.size());
        for (Object ja : dcArray) {
            JSONObject tmp = (JSONObject) ja;
            logger.debug("DC: " + tmp.get(StrFactory.DATACENTER));
        }
    }

    public static void main (String[] args) {
        NodeToolFileParseTest ntfpt = new NodeToolFileParseTest();
        ntfpt.initiate();
        ntfpt.parseFiles();
        ntfpt.readNodetoolStatusJSON();
    }

}
