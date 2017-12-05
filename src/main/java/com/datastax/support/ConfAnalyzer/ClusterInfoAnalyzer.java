/*
 * Copyright (c)
 *
 * Date: 2/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.ConfAnalyzer;

import com.datastax.support.Parser.ClusterInfoParser;
import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TextArea;

/**
 * Created by Mike Zhang on 2/12/2017.
 */

public class ClusterInfoAnalyzer {

    public TextArea generateNodeStatusOutput(FileFactory ff)

    {
        TextArea t = new TextArea();

        ClusterInfoParser cip = new ClusterInfoParser(ff);


        return t;

    }
}
