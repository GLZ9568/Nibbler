/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.NibProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Chun Gao on 24/11/2017
 */

public class ConfFileParser {

    private static final Logger logger = LogManager.getLogger(ConfFileParser.class);

    private NibProperties properties = new NibProperties();

    public boolean parse(File file) {
        try {
            logger.debug("Loading " + file.getName());
            FileInputStream input = new FileInputStream(file);
            properties.load(input);
        } catch (FileNotFoundException fnfe) {
            logger.error(fnfe);
            return false;
        } catch (IOException ioe) {
            logger.error(ioe);
            return false;
        } catch (NullPointerException e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    public NibProperties getProperties() {
        return properties;
    }
}
