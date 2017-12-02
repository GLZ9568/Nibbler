/*
 * Copyright (c)
 *
 * Date: 23/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Nibbler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 23/11/2017
 */

public class ReadTest extends Test{

    private static final Logger logger = LogManager.getLogger(ReadTest.class);

    public void printFilesPath() {
        for (File file : files) {
            logger.debug(file.getAbsolutePath());
        }
    }

    public void printFilesName() {
        for (File file : files) {
            logger.debug(file.getName());
        }
    }

    public void printNumberofFiles() {
        logger.debug("Number of Files Read: " + files.size());
    }

    public void printFilesIP() {
        for (File file : files) {
            if (Inspector.foundIPAddress(file.getAbsolutePath())) {
                logger.debug("Found IP in: " + file.getAbsolutePath());
            } else {
                logger.debug("NOT Found IP in: " + file.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        ReadTest nt = new ReadTest();
        nt.initiate();
        nt.printNumberofFiles();
        nt.printFilesPath();
        nt.printFilesName();
        nt.printFilesIP();

        //launch(args);
    }
}
