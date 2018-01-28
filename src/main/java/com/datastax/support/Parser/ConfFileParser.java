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
 * Created by Mike Zhang on 17/11/17
 *
 */
public class ConfFileParser extends FileParser {
    private static final Logger logger = LogManager.getLogger(ConfFileParser.class);

    private ArrayList<File> cassandraYamlFiles;
    private ArrayList<NibProperties> cassandraYamlPropertiesList;
    private ArrayList<File> addressYamlFiles;
    private ArrayList<NibProperties> addressYamlPropertiesList;
    private ArrayList<File> dseYamlFiles;
    private ArrayList<NibProperties> dseYamlPropertiesList;
    private ArrayList<File> confFiles;
    private ArrayList<File> clusterConfFiles;
    private ArrayList<NibProperties> clusterConfPropertiesList;
    private ArrayList<String> clusterName;
    private ArrayList<String> snitch_list;

    public ConfFileParser(ArrayList<File> files) {
        super(files);
        parse();
    }

    private void parse() {
        cassandraYamlFiles = new ArrayList<File>();
        cassandraYamlPropertiesList = new ArrayList<NibProperties>();
        addressYamlFiles = new ArrayList<File>();
        addressYamlPropertiesList = new ArrayList<NibProperties>();
        dseYamlFiles = new ArrayList<File>();
        dseYamlPropertiesList = new ArrayList<NibProperties>();
        confFiles = new ArrayList<File>();
        clusterConfFiles = new ArrayList<File>();
        clusterConfPropertiesList = new ArrayList<NibProperties>();
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
            cassandraYamlPropertiesList = extractProperties(cassandraYamlFiles);
            for (NibProperties properties : cassandraYamlPropertiesList) {
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
            addressYamlPropertiesList = extractProperties(addressYamlFiles);
        } else {
            logger.error("Did not find any " + ValFactory.ADDRESS_YAML + " files.");
        }

        if (!dseYamlFiles.isEmpty()) {
            dseYamlPropertiesList = extractProperties(dseYamlFiles);
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
            clusterConfPropertiesList = extractProperties(clusterConfFiles);
        }
    }

    private boolean isCassandraYaml (File file) {
        return file.getName().contains(ValFactory.CASSANDRA_YAML);
    }

    public ArrayList<NibProperties> getCassandraYamlPropertiesList() {
        return cassandraYamlPropertiesList;
    }

    private boolean isAgentAddressYaml (File file) {
        return file.getName().contains(ValFactory.ADDRESS_YAML);
    }

    public ArrayList<NibProperties> getAddressYamlPropertiesList() {
        return addressYamlPropertiesList;
    }

    private boolean isDSEYaml (File file) {
        return file.getAbsolutePath().contains(ValFactory.DSE_YAML);
    }

    public ArrayList<NibProperties> getDSEYamlPropertiesList() {
        return dseYamlPropertiesList;
    }

    private boolean isConfFile (File file) {
        return file.getName().endsWith(ValFactory.CONF_SURFFIX);
    }

    private boolean isClusterConfFile (File file, ArrayList<String> cluster_name) {
        for (String cn : cluster_name) {
            if (file.getName().replaceAll("[^A-Za-z0-9]+", "").contains(cn.replaceAll("[^A-Za-z0-9]+", ""))) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<NibProperties> getClusterConfPropertiesList() {
        return clusterConfPropertiesList;
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
                logCheckedException(logger, fnfe);
            } catch (IOException ioe) {
                logCheckedException(logger, ioe);
            } catch (NullPointerException npe) {
                logCheckedException(logger, npe);
            }
        }
        return propertiesArrayList;
    }
}