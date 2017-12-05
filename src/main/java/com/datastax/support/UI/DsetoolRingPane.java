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
import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TitledPane;

/**
 * Created by Mike Zhang on 3/12/2017.
 */

public class DsetoolRingPane {


    TitledPane createDsetoolRingPane(FileFactory ff)
    {
        TitledPane tp = new TitledPane();

        tp.setExpanded(false);
        tp.setPrefWidth(1024);
        //tp.setMinHeight(768);
        //tp.setMaxHeight(768);
        tp.setStyle( "-fx-font-family: Courier New");
        //tp.setPrefSize(1024, 10);
        tp.setText("dsetool ring");
        tp.setContent(new DsetoolRingParser().generateDsetoolRingOutput(ff));
        return tp;

    }
}
