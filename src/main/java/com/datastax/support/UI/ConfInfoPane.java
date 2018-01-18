/*
 * Copyright (c)
 *
 * Date: 18/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Analyzer.ConfAnalyzer;
import com.datastax.support.Analyzer.NodeStatusAnalyzer;
import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TitledPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mike Zhang on 18/01/2018.
 */

public class ConfInfoPane extends ComponentTitledPane {

    private static final Logger logger = LogManager.getLogger(ConfInfoPane.class);

    private ConfAnalyzer confAnalyzer;
    private static final String title = "Node Configuration Files Info";
    private String output = "";

    public ConfInfoPane(FileFactory fileFactory) {
        super(title);
        confAnalyzer = new ConfAnalyzer(fileFactory);
        output = confAnalyzer.generateConfOutput();
        titledPane.setContent(generateTextArea(output));
    }

    public TitledPane getConfInfoPane() {
        return titledPane;
    }
}
