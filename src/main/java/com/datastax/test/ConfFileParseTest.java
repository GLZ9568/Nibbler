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
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class ConfFileParseTest extends Test{

    private static final Logger logger = LogManager.getLogger(ConfFileParseTest.class);

    private ConfFileParser confFileParser;
    private ArrayList<NibProperties> cassandraYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> addressYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> dseYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> clusterConfProperties = new ArrayList<NibProperties>();

    public void parseFiles() {
        confFileParser = new ConfFileParser();
        confFileParser.parse(files);
        cassandraYamlProperties = confFileParser.getCassandraYamlProperties();
        addressYamlProperties = confFileParser.getAddressYamlProperties();
        dseYamlProperties = confFileParser.getDSEYamlProperties();
        clusterConfProperties = confFileParser.getClusterConfProperties();

        for (Object key : addressYamlProperties.get(0).keySet()){
            for (NibProperties props : addressYamlProperties){
                logger.debug("node " + props.get(StrFactory.FILE_ID) + " - " + props.get(StrFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : cassandraYamlProperties.get(0).keySet()){
            for (NibProperties props : cassandraYamlProperties){
                logger.debug("node " + props.get(StrFactory.FILE_ID) + " - " + props.get(StrFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : dseYamlProperties.get(0).keySet()){
            for (NibProperties props : dseYamlProperties){
                logger.debug("node " + props.get(StrFactory.FILE_ID) + " - " + props.get(StrFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : clusterConfProperties.get(0).keySet()) {
            for (NibProperties props : clusterConfProperties) {
                logger.debug("opscenter " + props.get(StrFactory.FILE_ID) + " - " + props.get(StrFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
        }

    }

    public static void main (String[] args) {
        ConfFileParseTest cfpt = new ConfFileParseTest();
        cfpt.initiate();
        cfpt.parseFiles();


    }
}
