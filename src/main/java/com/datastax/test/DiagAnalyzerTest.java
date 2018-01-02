/*
 * Copyright (c)
 *
 * Date: 30/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.DiagAnalyzer.DiagAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 30/12/2017
 */

public class DiagAnalyzerTest extends Test{
    private static final Logger logger = LogManager.getLogger(DiagAnalyzerTest.class);

    private DiagAnalyzer diagAnalyzer;

    public void analyze() {
        this.initiate();
        diagAnalyzer = new DiagAnalyzer(files);
        diagAnalyzer.analyzeClusterInfo();
    }

    public static void main(String[] args) {
        DiagAnalyzerTest diagAnalyzerTest = new DiagAnalyzerTest();
        diagAnalyzerTest.analyze();
    }
}
