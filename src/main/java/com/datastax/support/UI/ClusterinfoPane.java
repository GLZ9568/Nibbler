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
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

/**
 * Created by Mike Zhang on 2/12/2017.
 */

public class ClusterinfoPane {

    private String cluster_info_report;
    TitledPane createClusterInfoPane(FileFactory ff)
    {
        TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        tp.setPrefWidth(1024);
        //tp.setMinHeight(768);
        tp.setStyle( "-fx-font-family: Courier New");
        //tp.setPrefSize(1024, 10);
        tp.setText("Cluster Configuration Summary");
        TextArea cluster_info_area = new ClusterInfoAnalyzer().generateNodeStatusOutput(ff);
        tp.setContent(cluster_info_area);
        cluster_info_report = cluster_info_area.getText();
        return tp;

    }

    public String getCluster_info_report() {
        return cluster_info_report;
    }
}
