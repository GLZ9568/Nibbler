/*
 * Copyright (c)
 *
 * Date: 24/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Analyzer.SystemResourceAnalyzer;
import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TitledPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 24/01/2018
 */

public class SystemResourceTitledPane extends ComponentTitledPane {
    private static final Logger logger = LogManager.getLogger(SystemResourceAnalyzer.class);

    private SystemResourceAnalyzer systemResourceAnalyzer;
    private static final String title = "Node Resource Usage Info";
    private String output;

    public SystemResourceTitledPane (FileFactory fileFactory) {
        super(title);
        systemResourceAnalyzer = new SystemResourceAnalyzer(fileFactory);
        output = systemResourceAnalyzer.getOutput();
        titledPane.setContent(generateTextArea(output));
    }

    public TitledPane getSystemResourceTitledPane() {
        return titledPane;
    }
}
