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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 30/11/17
 */

public class NodeToolFileParseTest extends Test{
    private static final Logger logger = LogManager.getLogger(NodeToolFileParseTest.class);

    public void parseFiles() {
        NodeToolFileParser nodeToolFileParser = new NodeToolFileParser();
        nodeToolFileParser.parse(files);
    }

    public static void main (String[] args) {
        NodeToolFileParseTest ntfpt = new NodeToolFileParseTest();
        ntfpt.initiate();
        //ntfpt.();
    }

}
