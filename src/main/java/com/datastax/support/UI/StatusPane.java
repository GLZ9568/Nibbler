/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Util.FileFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import com.datastax.support.Parser.statusParser;
import javafx.stage.Screen;

import java.awt.*;

/**
 * Created by Mike Zhang on 24/11/2017.
 *
 * create status pane based on the result from statusParser
 */

public class StatusPane {

    private String status_report;

    TitledPane createstatusPane(FileFactory ff)
    {
       TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screen_height =  visualBounds.getHeight() ;
        double screen_width = visualBounds.getWidth();
      //  tp.setPrefWidth(screen_width*0.5);
        tp.setMinWidth(screen_width*0.7);
       // tp.setPrefWidth(1024);
        //tp.setMinHeight(768);
        tp.setStyle( "-fx-font-family: Courier New");
        //tp.setPrefSize(1024, 10);
        tp.setText("nodetool status");
        TextArea status_area = new statusParser().generateNodeStatusOutput(ff);
        tp.setContent(status_area);
        status_report = status_area.getText();
        return tp;

    }

    public String getStatus_report() {
        return status_report;
    }
}
