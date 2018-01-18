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
import com.datastax.support.UI.CfstatsTitledPane;
import com.datastax.support.UI.NodeStatusTitledPane;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 24/11/2017
 */

public class UITest extends Test{
    private static final Logger logger = LogManager.getLogger(UITest.class);

    public void start (Stage stage) {
        GridPane gridPane = new GridPane();
        this.initiate();
        NodeStatusTitledPane nodeStatusPane = new NodeStatusTitledPane(fileFactory);
        TitledPane nodeStatusTitledPane = nodeStatusPane.getNodeStatusTitledPane();
        CfstatsTitledPane cfstatsPane = new CfstatsTitledPane(fileFactory);
        TitledPane cfstatsTitledPane = cfstatsPane.getCfstatsTitledPane();

        stage.setTitle("Nibbler UI Test");
        stage.setMinHeight(800);
        gridPane.add(nodeStatusTitledPane, 0, 1);
        gridPane.add(cfstatsTitledPane, 0, 2);
        gridPane.setMinHeight(600);
        stage.setScene(new Scene(gridPane));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
