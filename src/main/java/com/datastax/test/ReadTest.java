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

public class ReadTest extends Nibbler{

    protected static final Logger logger = LogManager.getLogger(ReadTest.class);

    private final String testDir = "39114";
    //private final String testDir = "test";
    private final File winDir = new File("D:\\Dropbox (HTG Projects)\\DSE\\02 Tickets\\2017_AP\\" + testDir);
    private final File linDir = new File ("/Users/cgao/Dropbox (HTG Projects)/DSE/02 Tickets/2017_AP/" + testDir);

    protected FileFactory ff;
    protected ArrayList<File> files;

    public void initiate() {
        ff = new FileFactory();
        files = new ArrayList<File>();
        if (Inspector.foundWindowsOS()) {
            logger.debug("Reading From: " + winDir + "\\");
            ff.readFiles(winDir);
        } else {
            logger.debug("Reading From: " + linDir + "/");
            ff.readFiles(linDir);
        }
        files = ff.getFiles();
    }

    public void printFilesPath() {
        for (File file : files) {
            logger.debug(file.getPath());
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
            if (Inspector.foundIPAddress(file.getPath())) {
                logger.debug("Found IP in: " + file.getPath());
            } else {
                logger.debug("NOT Found IP in: " + file.getPath());
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
