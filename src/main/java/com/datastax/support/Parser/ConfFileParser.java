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

    private ArrayList<File> addressYamlFiles;
    private ArrayList<NibProperties> addressYamlProperties;

    private ArrayList<File> dseYamlFiles;
    private ArrayList<NibProperties> dseYamlProperties;

    public void parse(ArrayList<File> files) {

        cassandraYamlFiles = new ArrayList<File>();
        cassandraYamlProperties = new ArrayList<NibProperties>();

        addressYamlFiles = new ArrayList<File>();
        addressYamlProperties = new ArrayList<NibProperties>();

        dseYamlFiles = new ArrayList<File>();
        dseYamlProperties = new ArrayList<NibProperties>();

        for (File file : files) {
            if (isCassandraYaml(file)) {
                cassandraYamlFiles.add(file);
            } else if (isAgentAddressYaml(file)) {
                addressYamlFiles.add(file);
            } else if (isDSEYaml(file)) {
                dseYamlFiles.add(file);
            }
        }

        if (!cassandraYamlFiles.isEmpty()) {
            cassandraYamlProperties = extractProperties(cassandraYamlFiles);
        } else {
            logger.error("Did not find any " + StrFactory.cassandra_yaml + " files.");
        }

        if (!addressYamlFiles.isEmpty()) {
            addressYamlProperties = extractProperties(addressYamlFiles);
        } else {
            logger.error("Did not find any " + StrFactory.address_yaml + " files.");
        }

        if (!dseYamlFiles.isEmpty()) {
            dseYamlProperties = extractProperties(dseYamlFiles);
        } else {
            logger.error("Did not find any " + StrFactory.dse_yaml + " files.");
        }
    }

    public boolean isCassandraYaml (File file) {
        return file.getPath().contains(StrFactory.cassandra_yaml);
    }

    public ArrayList<NibProperties> getCassandraYamlProperties() {
        return cassandraYamlProperties;
    }

    public boolean isAgentAddressYaml (File file) {
        return file.getPath().contains(StrFactory.address_yaml);
    }

    public ArrayList<NibProperties> getAddressYamlProperties() {
        return addressYamlProperties;
    }

    public boolean isDSEYaml (File file) {
        return file.getPath().contains(StrFactory.dse_yaml);
    }

    public ArrayList<NibProperties> getDSEYamlProperties() {
        return dseYamlProperties;
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
