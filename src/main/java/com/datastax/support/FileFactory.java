/*
 * Copyright (c)
 *
 * Date: 19/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 19/11/2017
 * <p>
 * Extract values defined by nibller.conf from files
 */

public class FileFactory {

    private static final Logger logger = LogManager.getLogger(FileFactory.class);
    private ArrayList<File> files = new ArrayList<File>();

    public FileFactory() {
    }

    public boolean readFiles(final File directory) {
        try {
            for (final File entry : directory.listFiles()) {
                if (entry.isDirectory()) {
                    readFiles(entry);
                } else {
                    logger.debug(" file: " + entry.getName());
                    files.add(entry);
                }
            }
        } catch (NullPointerException npe) {
            logger.error(directory + " Does Not Exist");
            return false;
        }
        return true;
    }

    public ArrayList<File> getFiles() {
        return files;
    }
}
