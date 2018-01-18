/*
 * Copyright (c)
 *
 * Date: 16/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Analyzer.CfstatsAnalyzer;
import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TitledPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CfstatsTitledPane extends ComponentTitledPane {
    private static final Logger logger = LogManager.getLogger(CfstatsTitledPane.class);

    private CfstatsAnalyzer cfstatsAnalyzer;
    private static final String title = "Cfstats";
    private String output;

    public CfstatsTitledPane(FileFactory fileFactory) {
        super(title);
        cfstatsAnalyzer = new CfstatsAnalyzer(fileFactory);
        output = cfstatsAnalyzer.getOutput();
        titledPane.setContent(generateTextArea(output));
    }

    public TitledPane getCfstatsTitledPane() {
        return titledPane;
    }

}
