/*
 * Copyright (c)
 *
 * Date: 25/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.ConfAnalyzer.ConfAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class AnalyzeTest extends Test{

    private static final Logger logger = LogManager.getLogger(AnalyzeTest.class);

    private ConfAnalyzer confAnalyzer;

    public void analyzeConfFiles() {
        confAnalyzer = new ConfAnalyzer();
        confAnalyzer.analyze(files);
    }

    public static void main (String[] args) {
        AnalyzeTest at = new AnalyzeTest();
        at.initiate();
        at.analyzeConfFiles();
    }
}
