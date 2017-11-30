/*
 * Copyright (c)
 *
 * Date: 25/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Parser.ConfFileParser;
import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.StrFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class ParseTest extends Test{

    private static final Logger logger = LogManager.getLogger(ParseTest.class);

    private ConfFileParser fileParser;
    private ArrayList<NibProperties> cassandraYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> addressYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> dseYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> clusterConfProperties = new ArrayList<NibProperties>();

    public void parseFiles() {
        fileParser = new ConfFileParser();
        fileParser.parse(files);
        cassandraYamlProperties = fileParser.getCassandraYamlProperties();
        addressYamlProperties = fileParser.getAddressYamlProperties();
        dseYamlProperties = fileParser.getDSEYamlProperties();
        clusterConfProperties = fileParser.getClusterConfProperties();
    }

    public ArrayList<NibProperties> getCassandraYamlProperties () {
        return cassandraYamlProperties;
    }

    public ArrayList<NibProperties> getAddressYamlProperties () {
        return addressYamlProperties;
    }

    public ArrayList<NibProperties> getDSEYamlProperties () {
        return dseYamlProperties;
    }

    public ArrayList<NibProperties> getClusterConfProperties () {return clusterConfProperties; }

    public static void main (String[] args) {
        ParseTest pt = new ParseTest();
        pt.initiate();
        pt.parseFiles();

        NibProperties cassandraYamlProperties = pt.getCassandraYamlProperties().get(0);
        NibProperties addressYamlProperties = pt.getAddressYamlProperties().get(0);
        NibProperties dseYamlProperties = pt.getDSEYamlProperties().get(0);
        NibProperties clusterConfProperties = pt.getClusterConfProperties().get(0);

        for (Object key : addressYamlProperties.keySet()){
            for (NibProperties props : pt.getAddressYamlProperties()){
                logger.debug("node " + props.get(StrFactory.file_id) + " - " + props.get(StrFactory.file_name) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : cassandraYamlProperties.keySet()){
            for (NibProperties props : pt.getCassandraYamlProperties()){
                logger.debug("node " + props.get(StrFactory.file_id) + " - " + props.get(StrFactory.file_name) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : dseYamlProperties.keySet()){
            for (NibProperties props : pt.getDSEYamlProperties()){
                logger.debug("node " + props.get(StrFactory.file_id) + " - " + props.get(StrFactory.file_name) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : clusterConfProperties.keySet()) {
            for (NibProperties props : pt.getClusterConfProperties()) {
                logger.debug("opscenter " + props.get(StrFactory.file_id) + " - " + props.get(StrFactory.file_name) + " - " + key.toString() + ": " + props.get(key));
            }
        }
    }
}
