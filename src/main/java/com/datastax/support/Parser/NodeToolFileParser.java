/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.StrFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.io.File;

/**
 * Created by Chun Gao on 24/11/2017
 */

public class NodeToolFileParser {

    private static Logger logger = LogManager.getLogger(NodeToolFileParser.class);

    private ArrayList<File> nodetoolStatusFiles;
    private ArrayList<File> nodetoolCfstatusFiles;

    public void parse (ArrayList<File> files) {

        nodetoolStatusFiles = new ArrayList<File>();
        nodetoolCfstatusFiles = new ArrayList<File>();

        for (File file : files) {
            if (file.getAbsolutePath().contains(StrFactory.nodetool) && file.getName().equals(StrFactory.status) && Inspector.isValidNodetoolStatusFile(file)) {
                nodetoolStatusFiles.add(file);
                logger.debug("Added nodetool file: " + file.getAbsolutePath());
            }
        }


    }



}
