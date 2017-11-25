/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.ConfAnalyzer;

import com.datastax.support.Parser.ConfFileParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 17/11/17
 *
 * Read corrisponding files and then
 * 1. extract the information from the files
 * 2. analyze the extracted information
 * 3. output the extracted information and recommendations
 */

public class ConfAnalyzer {

    private static final Logger logger = LogManager.getLogger(ConfAnalyzer.class);

    private final String cassandra_yaml = "cassandra.yaml";
    private ArrayList<File> cassandraYamlFiles;
    private final String agentaddress_yaml = "agentaddress.yaml";
    private ArrayList<File> agentaddressYamlFiles;
    private final String dse_yaml = "dse.yaml";
    private ArrayList<File> dseYamlFiles;

    public void analyze (ArrayList<File> files) {

        cassandraYamlFiles = new ArrayList<File>();
        agentaddressYamlFiles = new ArrayList<File>();
        dseYamlFiles = new ArrayList<File>();

        for (File file : files) {
            if (isCassandraYaml(file)) {
                cassandraYamlFiles.add(file);
            } else if (isAgentAddressYaml(file)) {
                agentaddressYamlFiles.add(file);
            } else if (isDSEYaml(file)) {
                dseYamlFiles.add(file);
            }
        }

        if (!cassandraYamlFiles.isEmpty()) {
            analyzeCassandraYaml(cassandraYamlFiles);
        } else {
            logger.debug("Did not find any " + cassandra_yaml + " files.");
        }

        if (!agentaddressYamlFiles.isEmpty()) {
            analyzeAgentAddressYaml(agentaddressYamlFiles);
        } else {
            logger.debug("Did not find any " + agentaddress_yaml + " files.");
        }

        if (!dseYamlFiles.isEmpty()) {
            analyzeDSEYaml(dseYamlFiles);
        } else {
            logger.debug("Did not find any " + dse_yaml + " files.");
        }
    }

    public void analyzeCassandraYaml(ArrayList<File> files) {
        for(File file : files) {
            logger.debug("Found " + file.getName() + " at path: " + file.getPath() + " To Do: Implement analyzeCassandraYaml");
        }

    }

    public void analyzeAgentAddressYaml(ArrayList<File> files) {
        for (File file : files) {
            logger.debug("Found " + file.getName() + " at path: " + file.getPath() + " To Do: Implement analyseAgentAddressYaml");
        }
    }

    public void analyzeDSEYaml (ArrayList<File> files) {
        for (File file : files) {
            logger.debug("Found " + file.getName() + " at path: " + file.getPath() + " To Do: Implement analyseDSEYaml");
        }
    }

    public boolean isCassandraYaml (File file) {
        return file.getPath().contains(cassandra_yaml);
    }

    public boolean isAgentAddressYaml (File file) {
        return file.getPath().contains(agentaddress_yaml);
    }

    public boolean isDSEYaml (File file) {
        return file.getPath().contains(dse_yaml);
    }
}
