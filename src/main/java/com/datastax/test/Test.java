/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Nibbler;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

public class Test extends Nibbler {

    private static final Logger logger = LogManager.getLogger(Test.class);

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

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        BorderPane border = new BorderPane();
        primaryStage.setScene(new Scene(border, 1024, 768));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
