/*
 * Copyright (c)
 *
 * Date: 25/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.ConfAnalyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class CassandraYamlAnalyzer {

    private static final Logger logger = LogManager.getLogger(CassandraYamlAnalyzer.class);

    private final String seeds = "-seeds";
    private final String cluster_name = "cluster_name";
    private final String num_tokens = "num_tokens";
    private final String none_exist = "none_exist";

    public void analyze(File file) {

    }
}
