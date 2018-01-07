/*
 * Copyright (c)
 *
 * Date: 30/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Analyzer.NodeStatusAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 30/12/2017
 */

public class NodeStatusAnalyzerTest extends Test{
    private static final Logger logger = LogManager.getLogger(NodeStatusAnalyzerTest.class);

    private NodeStatusAnalyzer nodeStatusAnalyzer;

    public void analyze() {
        this.initiate();
        nodeStatusAnalyzer = new NodeStatusAnalyzer(fileFactory);
        nodeStatusAnalyzer.analyze();
    }

    public static void main(String[] args) {
        NodeStatusAnalyzerTest nodeStatusAnalyzerTest = new NodeStatusAnalyzerTest();
        nodeStatusAnalyzerTest.analyze();
    }
}
