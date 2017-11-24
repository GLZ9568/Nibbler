/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.ConfFileParser;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Chun Gao on 24/11/2017
 */

public class ParseTest extends ReadTest {

    private final String cassandra_ymal = "cassandra.yaml";
    private final String seeds = "-seeds";
    private final String cluster_name = "cluster_name";
    private final String num_tokens = "num_tokens";
    private final String none_exist = "none_exist";

    //private ArrayList<ConfFileParser> confFiles = new ArrayList<ConfFileParser>();

    public void parse() {
        for (File file : files) {
            if (file.getName().contains(cassandra_ymal)) {
                logger.debug("Found cassandra.yaml: " + file.getPath());
                ConfFileParser cfp = new ConfFileParser();
                cfp.parse(file);
                logger.debug("cluster_name: " + cfp.getProperties().getProperty(cluster_name));
                logger.debug("seeds: " + cfp.getProperties().getProperty(seeds));
                logger.debug("num_tokens: " + cfp.getProperties().getProperty(num_tokens));
                logger.debug("none_exist: " + cfp.getProperties().getProperty(none_exist));
            }
        }
    }

    public static void main (String[] args) {
        ParseTest pt = new ParseTest();
        pt.initiate();
        pt.parse();

    }
}
