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
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class ConfFileParseTest extends Test{

    private static final Logger logger = LogManager.getLogger(ConfFileParseTest.class);

    private ArrayList<NibProperties> cassandraYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> addressYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> dseYamlProperties = new ArrayList<NibProperties>();
    private ArrayList<NibProperties> clusterConfProperties = new ArrayList<NibProperties>();

    public void parseFiles() {
        //confFileParser = new ConfFileParser(files);
        cassandraYamlProperties = fileFactory.getCassandraYamlPropertiesList();
        addressYamlProperties = fileFactory.getAddressYamlPropertiesList();
        dseYamlProperties = fileFactory.getDSEYamlPropertiesList();
        clusterConfProperties = fileFactory.getClusterConfPropertiesList();

        for (Object key : addressYamlProperties.get(0).keySet()){
            for (NibProperties props : addressYamlProperties){
                logger.debug("node " + props.get(ValFactory.FILE_ID) + " - " + props.get(ValFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : cassandraYamlProperties.get(0).keySet()){
            for (NibProperties props : cassandraYamlProperties){
                logger.debug("node " + props.get(ValFactory.FILE_ID) + " - " + props.get(ValFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : dseYamlProperties.get(0).keySet()){
            for (NibProperties props : dseYamlProperties){
                logger.debug("node " + props.get(ValFactory.FILE_ID) + " - " + props.get(ValFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }

        for (Object key : clusterConfProperties.get(0).keySet()) {
            for (NibProperties props : clusterConfProperties) {
                logger.debug("opscenter " + props.get(ValFactory.FILE_ID) + " - " + props.get(ValFactory.FILE_NAME) + " - " + key.toString() + ": " + props.get(key));
            }
        }

    }

    public static void main (String[] args) {
        ConfFileParseTest cfpt = new ConfFileParseTest();
        cfpt.initiate();
        cfpt.parseFiles();


    }
}
