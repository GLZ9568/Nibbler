/*
 * Copyright (c)
 *
 * Date: 18/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.TpstatsFileParser;

import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 18/12/2017
 */

public class TpstatsFileParseTest extends Test {

    private static final Logger logger = LogManager.getLogger(TpstatsFileParseTest.class);

    public void parseFiles() {
        TpstatsFileParser tpstatsFileParser = new TpstatsFileParser();
        tpstatsFileParser.parse(files);
        ArrayList<JSONObject> tpstatsJSONs = tpstatsFileParser.getTpstatsJSONs();
        logger.debug("Number of Tpstats JSONs: " + tpstatsJSONs.size());
        for(JSONObject tpstatsJSON : tpstatsJSONs) {
            logger.debug("Tpstats JSON Detials: " + tpstatsJSON.get(ValFactory.NODE) + " - " + tpstatsJSON.get(ValFactory.FILE_NAME) + " - " + tpstatsJSON.get(ValFactory.FILE_PATH));
            if(tpstatsJSONs.indexOf(tpstatsJSON) == 0) {
                logger.debug("First Tpstats Output: " + tpstatsJSON);
            }
        }
    }

    public static void main (String[] args) {
        TpstatsFileParseTest tpstatsFileParseTest = new TpstatsFileParseTest();
        tpstatsFileParseTest.initiate();
        tpstatsFileParseTest.parseFiles();
    }
}
