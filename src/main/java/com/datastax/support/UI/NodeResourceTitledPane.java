/*
 * Copyright (c)
 *
 * Date: 9/2/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Analyzer.NodeResourceAnalyzer;
import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TitledPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 07/02/2018
 */

public class NodeResourceTitledPane extends ComponentTitledPane {
    private static final Logger logger = LogManager.getLogger(NodeResourceTitledPane.class);

    private NodeResourceAnalyzer nodeResourceAnalyzer;
    private static final String title = "Node Resource Usage Info";
    private String output;

    public NodeResourceTitledPane(FileFactory fileFactory) {
        super(title);
        nodeResourceAnalyzer = new NodeResourceAnalyzer(fileFactory);
        output = nodeResourceAnalyzer.getOutput();
        titledPane.setContent(generateTextArea(output));
    }

    public TitledPane getNodeResourceTitledPane() {
        return titledPane;
    }
}
