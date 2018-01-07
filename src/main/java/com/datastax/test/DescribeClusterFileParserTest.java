/*
 * Copyright (c)
 *
 * Date: 28/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 28/12/2017
 */

public class DescribeClusterFileParserTest extends Test {

    private static final Logger logger = LogManager.getLogger(DescribeClusterFileParserTest.class);

    public void parseFiles() {
        ArrayList<JSONObject> describeclusterJSONs = fileFactory.getDescribeClusterJSONList();
        logger.debug("Number of Describecluster JSONs: " + describeclusterJSONs.size());
        for(JSONObject describeclusterJSON : describeclusterJSONs) {
            logger.debug("Describecluster JSON Detials: " + describeclusterJSON.get(ValFactory.FILE_ID) + " - " + describeclusterJSON.get(ValFactory.FILE_NAME) + " - " + describeclusterJSON.get(ValFactory.FILE_PATH));
            if(describeclusterJSONs.indexOf(describeclusterJSON) == 0) {
                logger.debug("First Describecluster Output: " + describeclusterJSON);
            }
        }
    }

    public static void main (String[] args) {
        DescribeClusterFileParserTest describeClusterFileParserTest = new DescribeClusterFileParserTest();
        describeClusterFileParserTest.initiate();
        describeClusterFileParserTest.parseFiles();
    }
}
