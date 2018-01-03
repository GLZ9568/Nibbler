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
import com.datastax.support.Util.ValFactory;
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

    private ArrayList<File> confFiles;

    private ArrayList<File> clusterConfFiles;
    private ArrayList<NibProperties> clusterConfProperties;

    private ArrayList<String> clusterName;
    private ArrayList<String> snitch_list;

    public void parse(ArrayList<File> files) {

        cassandraYamlFiles = new ArrayList<File>();
        cassandraYamlProperties = new ArrayList<NibProperties>();

        addressYamlFiles = new ArrayList<File>();
        addressYamlProperties = new ArrayList<NibProperties>();

        dseYamlFiles = new ArrayList<File>();
        dseYamlProperties = new ArrayList<NibProperties>();

        confFiles = new ArrayList<File>();

        clusterConfFiles = new ArrayList<File>();
        clusterConfProperties = new ArrayList<NibProperties>();

        clusterName = new ArrayList<String>();
        snitch_list = new ArrayList<String>();

        for (File file : files) {
            if (isCassandraYaml(file)) {
                cassandraYamlFiles.add(file);
            } else if (isAgentAddressYaml(file)) {
                addressYamlFiles.add(file);
            } else if (isDSEYaml(file)) {
                dseYamlFiles.add(file);
            } else if (isConfFile(file)) {
                confFiles.add(file);
            }
        }

        if (!cassandraYamlFiles.isEmpty()) {
            cassandraYamlProperties = extractProperties(cassandraYamlFiles);
            for (NibProperties properties : cassandraYamlProperties) {
                String clasterName = properties.get(ValFactory.CLUSTER_NAME).toString();
                String snitch_str = properties.get(ValFactory.SNITCH).toString();
                if (!clusterName.contains(clasterName)) {
                    clusterName.add(clasterName);
                }
                if (!snitch_list.contains(snitch_str)) {
                    snitch_list.add(snitch_str);
                }
            }
        } else {
            logger.error("Did not find any " + ValFactory.CASSANDRA_YAML + " files.");
        }

        if (!addressYamlFiles.isEmpty()) {
            addressYamlProperties = extractProperties(addressYamlFiles);
        } else {
            logger.error("Did not find any " + ValFactory.ADDRESS_YAML + " files.");
        }

        if (!dseYamlFiles.isEmpty()) {
            dseYamlProperties = extractProperties(dseYamlFiles);
        } else {
            logger.error("Did not find any " + ValFactory.DSE_YAML + " files.");
        }

        if (!confFiles.isEmpty()) {
            for (File file : confFiles) {
                if (isClusterConfFile(file, clusterName)) {
                    clusterConfFiles.add(file);
                }
            }
        }

        if (!clusterConfFiles.isEmpty()) {
            clusterConfProperties = extractProperties(clusterConfFiles);
        }
    }

    public boolean isCassandraYaml (File file) {
        return file.getName().contains(ValFactory.CASSANDRA_YAML);
    }

    public ArrayList<NibProperties> getCassandraYamlProperties() {
        return cassandraYamlProperties;
    }

    public boolean isAgentAddressYaml (File file) {
        return file.getName().contains(ValFactory.ADDRESS_YAML);
    }

    public ArrayList<NibProperties> getAddressYamlProperties() {
        return addressYamlProperties;
    }

    public boolean isDSEYaml (File file) {
        return file.getAbsolutePath().contains(ValFactory.DSE_YAML);
    }

    public ArrayList<NibProperties> getDSEYamlProperties() {
        return dseYamlProperties;
    }

    public boolean isConfFile (File file) {
        return file.getName().endsWith(ValFactory.CONF_SURFFIX);
    }

    public boolean isClusterConfFile (File file, ArrayList<String> cluster_name) {
        for (String cn : cluster_name) {
            if (file.getName().replaceAll("[^A-Za-z0-9]+", "").contains(cn.replaceAll("[^A-Za-z0-9]+", ""))) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<NibProperties> getClusterConfProperties () {
        return clusterConfProperties;
    }

    public ArrayList<String> getClusterName() {return clusterName;}

    public ArrayList<String> getSnitch_list() {
        return snitch_list;
    }

    public ArrayList<NibProperties> extractProperties(ArrayList<File> files) {
        ArrayList<NibProperties> propertiesArrayList = new ArrayList<NibProperties>();

        for (File file : files) {
            String id;
            NibProperties properties = new NibProperties();

            try {
                FileInputStream input = new FileInputStream(file);
                properties.load(input);
                id = Inspector.getFileID(file);
                properties.put(ValFactory.FILE_ID, id);
                properties.put(ValFactory.FILE_PATH, file.getAbsolutePath());
                properties.put(ValFactory.FILE_NAME, file.getName());
                propertiesArrayList.add(properties);
            } catch (FileNotFoundException fnfe) {
                logger.error(fnfe);
            } catch (IOException ioe) {
                logger.error(ioe);
            } catch (NullPointerException npe) {
                logger.error(npe);
            }
        }
        return propertiesArrayList;
    }
}
