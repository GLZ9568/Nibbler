/*
 * Copyright (c)
 *
 * Date: 28/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.DsetoolRingFileParser;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Created by Chun Gao on 27/12/2017
 */

public class DsetoolRingFileParseTest extends Test {

    private static final Logger logger = LogManager.getLogger(DsetoolRingFileParseTest.class);

    public void parseFiles() {
        JSONObject dsetoolRingJSON = fileFactory.getDsetoolRingJSON();
        logger.debug("Dsetool Ring Detials: " + dsetoolRingJSON.get(ValFactory.FILE_ID) + " - " + dsetoolRingJSON.get(ValFactory.FILE_NAME) + " - " + dsetoolRingJSON.get(ValFactory.FILE_PATH));
        logger.debug("Dsetool Ring Output: " + dsetoolRingJSON);
    }

    public static void main (String[] args) {
        DsetoolRingFileParseTest dsetoolRingFileParseTest = new DsetoolRingFileParseTest();
        dsetoolRingFileParseTest.initiate();
        dsetoolRingFileParseTest.parseFiles();
    }
}
