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
import javafx.scene.control.TitledPane;
import com.datastax.support.Parser.statusParser;

/**
 * Created by Mike Zhang on 24/11/2017.
 *
 * create status pane based on the result from statusParser
 */

public class statusPane {

    TitledPane createstatusPane(FileFactory ff)
    {
       TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        tp.setPrefSize(1024, 700);
        tp.setText("nodetool status");
        tp.setContent(new statusParser().generateNodeStatusOutput(ff));
        return tp;

    }

}
