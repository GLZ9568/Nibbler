/*
 * Copyright (c)
 *
 * Date: 20/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 20/1/18
 */

public class TpstatsAnalyzer extends Analyzer {

    private final static Logger logger = LogManager.getLogger(SystemResourceAnalyzer.class);

    private String output = "";

    public TpstatsAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
    }

    public String getOutput() {
        return output;
    }
}
