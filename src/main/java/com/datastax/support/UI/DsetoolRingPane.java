/*
 * Copyright (c)
 *
 * Date: 3/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Parser.DsetoolRingParser;
import com.datastax.support.Parser.statusParser;
import com.datastax.support.Util.FileFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.stage.Screen;

import java.awt.*;

/**
 * Created by Mike Zhang on 3/12/2017.
 */

public class DsetoolRingPane {

    private String ring_report;
    TitledPane createDsetoolRingPane(FileFactory ff)
    {
        TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screen_height =  visualBounds.getHeight() ;
        double screen_width = visualBounds.getWidth();
        //tp.setPrefWidth(screen_width*0.5);
        tp.setMinWidth(screen_width*0.7);
       // tp.setPrefWidth(1024);
        //tp.setMinHeight(768);
        //tp.setMaxHeight(768);
        tp.setStyle( "-fx-font-family: Courier New");
        //tp.setPrefSize(1024, 10);
        tp.setText("dsetool ring");
        TextArea ring_area = new DsetoolRingParser().generateDsetoolRingOutput(ff);
        tp.setContent(ring_area);
        ring_report = ring_area.getText();
        return tp;

    }

    public String getRing_report() {
        return ring_report;
    }
}
