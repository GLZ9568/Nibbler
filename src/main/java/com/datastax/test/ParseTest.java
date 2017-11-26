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

    public void parseFiles() {
        fileParser = new ConfFileParser();
        fileParser.parse(files);
        cassandraYamlProperties = fileParser.getCassandraYamlProperties();
    }

    public ArrayList<NibProperties> getCassandraYamlProperties () {
        return cassandraYamlProperties;
    }

    public static void main (String[] args) {
        ParseTest pt = new ParseTest();
        pt.initiate();
        pt.parseFiles();

        NibProperties properties = pt.getCassandraYamlProperties().get(0);

        for (Object key : properties.keySet()){
            for (NibProperties props : pt.getCassandraYamlProperties()){
                logger.debug("node " + props.get(StrFactory.file_id) + " - " + key.toString() + ": " + props.get(key));
            }
            logger.debug("");
        }
    }
}
