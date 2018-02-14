/*
 * Copyright (c)
 *
 * Date: 21/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.ValFactory;
import com.esotericsoftware.yamlbeans.tokenizer.Tokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * Created by Mike Zhang on 21/01/2018.
 */

public class YamlFileParser extends FileParser {

    private static final Logger logger = LogManager.getLogger(YamlFileParser.class);

    public YamlFileParser(ArrayList<File> files) {
        super(files);
        parse();

    }

    private ArrayList<File> cassandraYamlFiles;
    private ArrayList<Map<String, Object>> cassandraYamlPropertiesList;
    private ArrayList<File> dseYamlFiles;
    private ArrayList<Map<String, Object>> dseYamlPropertiesList;

    private void parse() {

        cassandraYamlFiles = new ArrayList<File>();
        cassandraYamlPropertiesList = new ArrayList<Map<String, Object>>();

        dseYamlFiles = new ArrayList<File>();
        dseYamlPropertiesList = new ArrayList<Map<String, Object>>();
        for (File file : files) {
            if (isCassandraYaml(file)) {
                cassandraYamlFiles.add(file);
            } else if (isDSEYaml(file)) {
                dseYamlFiles.add(file);
            }
        }
        if (!cassandraYamlFiles.isEmpty()) {
            cassandraYamlPropertiesList = extractProperties(cassandraYamlFiles);
        } else {
            logger.error("Did not find any " + ValFactory.CASSANDRA_YAML + " files.");
        }


        if (!dseYamlFiles.isEmpty()) {
            dseYamlPropertiesList = extractProperties(dseYamlFiles);
        } else {
            logger.error("Did not find any " + ValFactory.DSE_YAML + " files.");
        }

    }


    private boolean isCassandraYaml(File file) {
        return file.getName().contains(ValFactory.CASSANDRA_YAML);
    }

    public ArrayList<Map<String, Object>> getCassandraYamlPropertiesList() {
        return cassandraYamlPropertiesList;
    }


    private boolean isDSEYaml(File file) {
        return file.getAbsolutePath().contains(ValFactory.DSE_YAML);
    }

    public ArrayList<Map<String, Object>> getDSEYamlPropertiesList() {
        return dseYamlPropertiesList;
    }

    public ArrayList<Map<String, Object>> extractProperties(ArrayList<File> files) {
        ArrayList<Map<String, Object>> propertiesArrayList = new ArrayList<Map<String, Object>>();
        for (File file : files) {
            String id;
            try {
                YamlReader reader = new YamlReader(new FileReader(file));
                Map<String, Object> yaml_property = new HashMap<String, Object>();
                Object object = reader.read();
                id = Inspector.getFileID(file);
                // ip <-> yaml map object
                yaml_property.put(id, object);
                propertiesArrayList.add(yaml_property);
            } catch (FileNotFoundException fnfe) {
                logCheckedException(logger, fnfe);
            } catch (IOException ioe) {
                logCheckedException(logger, ioe);
            } catch(Tokenizer.TokenizerException toe){
                logCheckedException(logger, toe);
            }
            catch (NullPointerException npe) {
                logCheckedException(logger, npe);
            }
        }
        return propertiesArrayList;
    }
}
