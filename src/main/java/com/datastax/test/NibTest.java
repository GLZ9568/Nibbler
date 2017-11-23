/*
 * Copyright (c)
 *
 * Date: 23/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.FileFactory;
import com.datastax.support.OSChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 23/11/2017
 */

public class NibTest {

    private static final Logger logger = LogManager.getLogger(NibTest.class);

    private final String testDir = "39114";
    //private final String testDir = "test";
    private final File winDir = new File("D:\\Dropbox (HTG Projects)\\DSE\\02 Tickets\\2017_AP\\" + testDir);
    private final File linDir = new File ("/Users/cgao/Dropbox (HTG Projects)/DSE/02 Tickets/2017_AP/" + testDir);

    private FileFactory ff;
    private ArrayList<File> files;

    public void initiate() {
        ff = new FileFactory();
        files = new ArrayList<File>();
        if (OSChecker.isWindows()) {
            logger.debug("Reading From: " + winDir + "\\");
            ff.readFiles(winDir);
        } else {
            logger.debug("Reading From: " + linDir + "/");
            ff.readFiles(linDir);
        }
        files = ff.getFiles();
        logger.debug("Number of Files Read: " + files.size());
    }

    public void printFilesPath() {
        for (File file : files) {
            logger.debug(file.getPath());
        }
    }

    public static void main(String[] args) {
        NibTest nt = new NibTest();
        nt.initiate();
        nt.printFilesPath();
    }
}
