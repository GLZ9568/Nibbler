/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 3/01/2018
 */

public class ClusterInfoFileParser {
    private static final Logger logger = LogManager.getLogger(ClusterInfoFileParser.class);

    private JSONObject clusterInfoJSON;

    public ClusterInfoFileParser(ArrayList<File> files) {
        clusterInfoJSON = new JSONObject();
        parse(files);
    }

    public void parse(ArrayList<File> files) {
        for (File file : files) {
            if (file.getName().toLowerCase().contains(ValFactory.CLUSTER_INFO.toLowerCase())) {
                JSONParser parser = new JSONParser();
                try {
                    clusterInfoJSON = (JSONObject) parser.parse(new FileReader(file.getAbsolutePath()));
                } catch (FileNotFoundException fnfe) {
                    logger.error((fnfe));
                } catch (ParseException pe) {
                    logger.error(pe);
                } catch (IOException ioe) {
                    logger.error(ioe);
                }
            }
        }
    }

    public JSONObject getClusterInfoJSON() {
        return clusterInfoJSON;
    }
}
