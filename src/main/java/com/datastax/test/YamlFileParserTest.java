/*
 * Copyright (c)
 *
 * Date: 21/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mike Zhang on 21/01/2018.
 */

public class YamlFileParserTest  extends Test{

    private static final Logger logger = LogManager.getLogger(YamlFileParserTest.class);

    private ArrayList<File> cassandraYamlFiles;
    private ArrayList<Map<String,Object>> cassandraYamlPropertiesList;
    private ArrayList<File> dseYamlFiles;
    private ArrayList<Map<String,Object>> dseYamlPropertiesList;
    private Map<String,Object> cas_map =  new HashMap<String, Object>();
    private Map<String,Object> dse_map = new HashMap<String, Object>();

    public void parseFiles() {
        //confFileParser = new ConfFileParser(files);
        cassandraYamlPropertiesList = fileFactory.getCassandraYamlPropertyList();

        dseYamlPropertiesList = fileFactory.getDseYamlPropertyList();

        for(Map<String,Object> dse_map_tmp : dseYamlPropertiesList)
        {
            dse_map = dse_map_tmp;
            for (Map.Entry<String,Object> entry : dse_map.entrySet())
            {
                logger.info("ip is: " + entry.getKey());
                Map map = (Map) entry.getValue();

                logger.info("dse.yaml auth options is : " + map.get("authentication_options"));

            }
        }


        for(Map<String,Object> cas_map_tmp : cassandraYamlPropertiesList)
        {
            cas_map = cas_map_tmp;
            for (Map.Entry<String,Object> entry : cas_map.entrySet())
            {
                logger.info("ip is: " + entry.getKey());
                Map map = (Map) entry.getValue();

                logger.info("cassandra.yaml server encryption option is: " + map.get("server_encryption_options"));

                logger.info("cassandra.yaml client encryption option is: " + map.get("client_encryption_options"));
            }
        }

    }

    public static void main (String[] args) {
        YamlFileParserTest yfpt = new YamlFileParserTest();
        yfpt.initiate();
        yfpt.parseFiles();
        System.exit(0);
    }
}
