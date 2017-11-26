/*
 * Copyright (c)
 *
 * Date: 26/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.StrFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 17/11/17
 *
 */

public class ConfFileParser {

    private static final Logger logger = LogManager.getLogger(ConfFileParser.class);

    private ArrayList<File> cassandraYamlFiles;
    private ArrayList<NibProperties> cassandraYamlProperties;

    private ArrayList<File> agentaddressYamlFiles;
    private ArrayList<NibProperties> agentaddressYamlProperties;

    private ArrayList<File> dseYamlFiles;
    private ArrayList<NibProperties> dseYamlProperties;

    public void parse(ArrayList<File> files) {

        cassandraYamlFiles = new ArrayList<File>();
        cassandraYamlProperties = new ArrayList<NibProperties>();

        agentaddressYamlFiles = new ArrayList<File>();
        agentaddressYamlProperties = new ArrayList<NibProperties>();

        dseYamlFiles = new ArrayList<File>();
        dseYamlProperties = new ArrayList<NibProperties>();

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
            parseCassandraYamlProperties(cassandraYamlFiles);
        } else {
            logger.error("Did not find any " + StrFactory.cassandra_yaml + " files.");
        }

        if (!agentaddressYamlFiles.isEmpty()) {
            parseAgentAddressYaml(agentaddressYamlFiles);
        } else {
            logger.error("Did not find any " + StrFactory.address_yaml + " files.");
        }

        if (!dseYamlFiles.isEmpty()) {
            parseDSEYaml(dseYamlFiles);
        } else {
            logger.error("Did not find any " + StrFactory.dse_yaml + " files.");
        }
    }

    public void parseCassandraYamlProperties(ArrayList<File> files) {
        cassandraYamlProperties = extractProperties(files);
    }

    public ArrayList<NibProperties> getCassandraYamlProperties() {
        return cassandraYamlProperties;
    }

    public void parseAgentAddressYaml(ArrayList<File> files) {
        for (File file : files) {
            logger.debug("Found " + file.getName() + " at path: " + file.getPath());
        }
    }

    public void parseDSEYaml(ArrayList<File> files) {
        for (File file : files) {
            logger.debug("Found " + file.getName() + " at path: " + file.getPath());
        }
    }

    public boolean isCassandraYaml (File file) {
        return file.getPath().contains(StrFactory.cassandra_yaml);
    }

    public boolean isAgentAddressYaml (File file) {
        return file.getPath().contains(StrFactory.address_yaml);
    }

    public boolean isDSEYaml (File file) {
        return file.getPath().contains(StrFactory.dse_yaml);
    }

    public ArrayList<NibProperties> extractProperties(ArrayList<File> files) {
        ArrayList<NibProperties> propertiesArrayList = new ArrayList<NibProperties>();

        for (File file : files) {
            String id;
            NibProperties properties = new NibProperties();

            try {
                FileInputStream input = new FileInputStream(file);
                properties.load(input);
                id = setID(file);
                properties.put(StrFactory.file_id, id);
                properties.put(StrFactory.file_path, file.getPath());
                properties.put(StrFactory.file_name, file.getName());
                propertiesArrayList.add(properties);
            } catch (FileNotFoundException fnfe) {
                logger.error(fnfe);
            } catch (IOException ioe) {
                logger.error(ioe);
            } catch (NullPointerException e) {
                logger.error(e);
            }
        }
        return propertiesArrayList;
    }

    private String setID (File file) {
        if (Inspector.foundIPAddress(file.getPath())) {
            return Inspector.getIPAddress(file.getPath());
        } else {
            return "TODO: to be implemented";
        }
    }
}
