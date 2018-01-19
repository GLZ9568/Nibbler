/*
 * Copyright (c)
 *
 * Date: 13/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Util.ValFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
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
    protected String fontSizeS = "-fx-font-size:9pt";
    protected String fontSizeM = "-fx-font-size:11pt";
    protected String fontSizeL = "-fx-font-size:13pt";
    protected String fontFamilyDefault = "-fx-font-family:Courier New";
    protected String fontFamilyAlign = "-fx-font-family:monospace";

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

        initiateTitledPane(title);
    }

    private void initiateTitledPane(String title) {
        titledPane.setExpanded(false);
        titledPane.setMinWidth(screen_width * ValFactory.SCREEN_WIDTH_FACTOR);
        titledPane.setStyle(fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
        titledPane.setText(title);
    }

    protected TextArea generateTextArea(String input) {
        TextArea textArea = new TextArea();
        textArea.setMinHeight(screen_height * (3*ValFactory.SCREEN_HEIGHT_FACTOR/5));
        textArea.setMinWidth(screen_width * ValFactory.SCREEN_WIDTH_FACTOR);
        textArea.setStyle(fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
        textArea.setText(input);
        textArea.setEditable(false);
        return textArea;
    }
}
