/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.UI.CfstatsTitledPane;
import com.datastax.support.UI.NodeStatusTitledPane;
import com.datastax.support.Util.ValFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 24/11/2017
 */

public class UITest extends Test{
    private static final Logger logger = LogManager.getLogger(UITest.class);

    protected Rectangle2D visualBounds;
    protected double screen_height;
    protected double screen_width;

    public void start (Stage stage) {
        visualBounds = Screen.getPrimary().getVisualBounds();
        screen_height = visualBounds.getHeight() ;
        screen_width = visualBounds.getWidth();

        GridPane gridPane = new GridPane();
        this.initiate();
        NodeStatusTitledPane nodeStatusPane = new NodeStatusTitledPane(fileFactory);
        final TitledPane nodeStatusTitledPane = nodeStatusPane.getNodeStatusTitledPane();
        CfstatsTitledPane cfstatsPane = new CfstatsTitledPane(fileFactory);
        final TitledPane cfstatsTitledPane = cfstatsPane.getCfstatsTitledPane();

        stage.setTitle("Nibbler UI Test");
        stage.setMinHeight(screen_height * ValFactory.SCREEN_HEIGHT_FACTOR);
        stage.setMinWidth(screen_width * ValFactory.SCREEN_WIDTH_FACTOR);
        gridPane.add(nodeStatusTitledPane, 0, 1);
        gridPane.add(cfstatsTitledPane, 0, 2);
        gridPane.setMinHeight(screen_height * ValFactory.SCREEN_HEIGHT_FACTOR);
        gridPane.setMinWidth(screen_width * ValFactory.SCREEN_WIDTH_FACTOR);
        gridPane.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                cfstatsTitledPane.setPrefWidth(arg2.doubleValue() - 15);
                nodeStatusTitledPane.setPrefWidth(arg2.doubleValue() - 15);
            }
        });
        stage.setScene(new Scene(gridPane));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
