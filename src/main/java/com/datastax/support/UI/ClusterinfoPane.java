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
import javafx.scene.control.TitledPane;

/**
 * Created by Mike Zhang on 2/12/2017.
 */

public class ClusterinfoPane {


    TitledPane createClusterInfoPane(FileFactory ff)
    {
        TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        tp.setPrefWidth(1024);
        //tp.setMinHeight(768);
        tp.setStyle( "-fx-font-family: Courier New");
        //tp.setPrefSize(1024, 10);
        tp.setText("Cluster Configuration Summary");
        tp.setContent(new ClusterInfoAnalyzer().generateNodeStatusOutput(ff));
        return tp;

    }
}
