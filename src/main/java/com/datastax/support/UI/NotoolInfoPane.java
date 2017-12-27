/*
 * Copyright (c)
 *
 * Date: 24/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.ConfAnalyzer.NodetoolInfoAnalyzer;
import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TitledPane;

/**
 * Created by Mike Zhang on 24/12/2017.
 */

public class NotoolInfoPane {

    TitledPane createinfoPane(FileFactory ff)
    {
        TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        tp.setPrefWidth(1024);
        //tp.setMinHeight(768);
        tp.setStyle( "-fx-font-family: Courier New");
        //tp.setPrefSize(1024, 10);
        tp.setText("nodetool info(group by DC)");
        tp.setContent(new NodetoolInfoAnalyzer().generateNodeStatusOutput(ff));
        return tp;

    }
}
