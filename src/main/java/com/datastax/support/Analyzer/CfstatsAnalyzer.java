/*
 * Copyright (c)
 *
 * Date: 10/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 10/1/18
 */

public class CfstatsAnalyzer extends Analyzer {
    private static final Logger logger = LogManager.getLogger(CfstatsAnalyzer.class);

    ArrayList<JSONObject> cfstatsList = new ArrayList<JSONObject>();

    public CfstatsAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
        analyze();
    }

    public void analyze() {
        cfstatsList = fileFactory.getCfstatsList();
        logger.debug("Number of Cfstats: " + cfstatsList.size());
    }
 }
