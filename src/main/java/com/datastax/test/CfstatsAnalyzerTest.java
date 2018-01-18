/*
 * Copyright (c)
 *
 * Date: 16/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Analyzer.CfstatsAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CfstatsAnalyzerTest extends Test {
    private static final Logger logger = LogManager.getLogger(CfstatsAnalyzerTest.class);

    private CfstatsAnalyzer cfstatsAnalyzer;

    public void analyze() {
        this.initiate();
        cfstatsAnalyzer = new CfstatsAnalyzer(fileFactory);
    }

    public static void main(String[] args) {
        CfstatsAnalyzerTest cfstatsAnalyzerTest = new CfstatsAnalyzerTest();
        cfstatsAnalyzerTest.analyze();
    }
}
