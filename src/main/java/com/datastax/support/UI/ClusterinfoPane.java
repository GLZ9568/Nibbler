/*
 * Copyright (c)
 *
 * Date: 2/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.ConfAnalyzer.*;
import com.datastax.support.Util.FileFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.stage.Screen;

import java.awt.*;

/**
 * Created by Mike Zhang on 2/12/2017.
 */

public class ClusterinfoPane {

    private String cluster_info_report;
    TitledPane createClusterInfoPane(FileFactory ff)
    {
        TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screen_height =  visualBounds.getHeight() ;
        double screen_width = visualBounds.getWidth();
        //tp.setPrefWidth(screen_width*0.5);
        tp.setMinWidth(screen_width*0.7);
        //tp.setMinHeight(768);
        tp.setStyle( "-fx-font-family: Courier New");
        //tp.setPrefSize(1024, 10);
        tp.setText("Cluster Configuration Summary");
        try {
        TextArea cluster_info_area = new ClusterInfoAnalyzer().generateNodeStatusOutput(ff);
        tp.setContent(cluster_info_area);
        cluster_info_report = cluster_info_area.getText();
        }catch(Exception e)
        {
            TextArea cluster_info_area = new TextArea();
            cluster_info_area.setText("Exception happened when retrieving cluster configuration information!!!");
            tp.setContent(cluster_info_area);
            cluster_info_report = cluster_info_area.getText();
            e.printStackTrace();
            return tp;
        }
        return tp;

    }

    public String getCluster_info_report() {
        return cluster_info_report;
    }
}
