/*
 * Copyright (c)
 *
 * Date: 10/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.UI.NodeStatusTitledPane;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 10/1/18
 */

public class NodetoolStatusUITest extends Test {
    private static final Logger logger = LogManager.getLogger(NodetoolStatusUITest.class);

    public void start (Stage stage) {
        this.initiate();
        NodeStatusTitledPane nodeStatusPane = new NodeStatusTitledPane(fileFactory);
        TitledPane nodeStatusTitledPane = nodeStatusPane.getNodeStatusTitledPane();
        stage.setTitle("Nodetool Status UI Test");
        stage.setMinHeight(800);
        stage.setScene(new Scene(nodeStatusTitledPane));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
