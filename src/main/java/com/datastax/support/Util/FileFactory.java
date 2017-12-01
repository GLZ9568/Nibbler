/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 19/11/2017
 * <p>
 * Extract values defined by nibbler.conf from files
 */

public class FileFactory {

    private static final Logger logger = LogManager.getLogger(FileFactory.class);
    private ArrayList<File> files = new ArrayList<File>();

    public boolean readFiles(final File directory) {
        try {
            for (final File entry : directory.listFiles()) {
                logger.debug("Processing File: " + entry.getAbsolutePath());
                if (entry.isDirectory()) {
                    readFiles(entry);
                } else {
                    files.add(entry);
                }
            }
        } catch (NullPointerException npe) {
            logger.error(npe);
            return false;
        }
        return true;
    }

    public ArrayList<File> getFiles() {
        return files;
    }
}
