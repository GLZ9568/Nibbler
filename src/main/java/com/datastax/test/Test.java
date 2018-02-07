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
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

public class Test extends Nibbler {

    private static final Logger logger = LogManager.getLogger(Test.class);

    //private final String testDir = "DSE4.8";
    //private final String testDir = "DSE5.0";
    private final String testDir = "test";
    private final File winDir = new File("C:\\Users\\CGao\\Dropbox (HTG Projects)\\DSE\\02 Tickets\\TestData\\" + testDir);
    //private final File winDir = new File("C:\\Users\\CGao\\Dropbox (HTG Projects)\\DSE\\02 Tickets\\2018_FTS\\40467\\xnetdse_prod1-diagnostics-2018_01_23_02_31_10_UTC");
    private final File linDir = new File ("/Users/cgao/Dropbox (HTG Projects)/DSE/02 Tickets/TestData/" + testDir);
    //private final File linDir = new File ("/Users/cgao/Dropbox (HTG Projects)/DSE/02 Tickets/TestData/largedata");
    //private final File linDir = new File ("/Users/tongjixianing/Downloads/order_prod_cluster-diagnostics-2017_11_24_23_44_42_UTC");

    protected FileFactory fileFactory;
    protected ArrayList<File> files;

    public void initiate() {
        files = new ArrayList<File>();

        if (Inspector.foundWindowsOS()) {
            logger.debug("Reading From: " + winDir + "\\");
            fileFactory = new FileFactory(winDir);
        } else {
            logger.debug("Reading From: " + linDir + "/");
            fileFactory = new FileFactory(linDir);
        }
        files = fileFactory.getAllFiles();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Nibbler Test");
        BorderPane border = new BorderPane();
        primaryStage.setScene(new Scene(border, 1024, 768));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
