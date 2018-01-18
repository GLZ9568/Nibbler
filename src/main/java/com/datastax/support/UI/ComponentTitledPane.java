/*
 * Copyright (c)
 *
 * Date: 13/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 13/01/2018
 */

public class ComponentTitledPane {
    private static final Logger logger = LogManager.getLogger(ComponentTitledPane.class);

    protected String fillWarning = "-fx-fill:RED";
    protected String fillTitle = "-fx-fill:BLUE";
    protected String fillValue = "-fx-fill:BLACK";
    protected String fillInfo = "-fx-fill:PURPLE";
    protected String fontWeightTitle = "-fx-font-weight:bold";
    protected String fontWeightBody = "-fx-font-weight:normal";
    protected String fontSizeS = "-fx-font-size:8pt";
    protected String fontSizeM = "-fx-font-size:10pt";
    protected String fontSizeL = "-fx-font-size:12pt";
    protected String fontFamilyDefault = "-fx-font-family:Courier New";
    protected String fontFamilyAlign = "-fx-font-family:monospace";
    protected double screenWidthFactor = 0.7;
    protected double screenHeightFactor = 0.8;

    protected TitledPane titledPane;
    protected TextFlow textFlow;

    protected Rectangle2D visualBounds;
    protected double screen_height;
    protected double screen_width;

    public ComponentTitledPane(String title) {
        titledPane = new TitledPane();
        textFlow = new TextFlow();

        visualBounds = Screen.getPrimary().getVisualBounds();
        screen_height = visualBounds.getHeight() ;
        screen_width = visualBounds.getWidth();

        titledPane.setMinHeight(500);

        initiateTitledPane(title);
    }

    private void initiateTitledPane(String title) {
        titledPane.setExpanded(true);
        titledPane.setMinWidth(screen_width*screenWidthFactor);
        titledPane.setStyle(fontWeightBody + ";" + fontSizeM + ";" + fontFamilyDefault);
        titledPane.setText(title);
    }

    protected TextArea generateTextArea(String input) {
        TextArea textArea = new TextArea();
        textArea.setStyle(fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
        textArea.setText(input);
        textArea.setEditable(false);
        return textArea;
    }
}
