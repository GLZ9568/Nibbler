/*
 * Copyright (c)
 *
 * Date: 6/1/2018
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
 * Created by Chun Gao on 6/01/2018
 */

public class ProxyHistogramsFileParserTest extends Test {
    private static final Logger logger = LogManager.getLogger(ProxyHistogramsFileParserTest.class);

    public void parseFiles() {
        ArrayList<JSONObject> proxyHistogramsJSONList = fileFactory.getProxyHistogramsJSONList();
        logger.debug("ProxyHistogramsJSONList Size: " + proxyHistogramsJSONList.size());
        JSONObject proxyHistogramsJSON = proxyHistogramsJSONList.get(0);
        logger.debug("ProxyHistogramsJSON Info: " + proxyHistogramsJSON.get(ValFactory.FILE_ID) + " - " + proxyHistogramsJSON.get(ValFactory.FILE_NAME) + " - " + proxyHistogramsJSON.get(ValFactory.FILE_PATH));
        logger.debug("ProxyHistogramsJSON Content: " + proxyHistogramsJSON);
    }

    public static void main (String[] args) {
        ProxyHistogramsFileParserTest proxyHistogramsFileParserTest = new ProxyHistogramsFileParserTest();
        proxyHistogramsFileParserTest.initiate();
        proxyHistogramsFileParserTest.parseFiles();
    }
}
