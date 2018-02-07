/*
 * Copyright (c)
 *
 * Date: 13/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Analyzer.NodeStatusAnalyzer;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 13/01/2018
 */

public class NodeStatusTitledPane extends ComponentTitledPane {
    private static final Logger logger = LogManager.getLogger(NodeStatusTitledPane.class);

    private NodeStatusAnalyzer nodeStatusAnalyzer;
    private static final String title = "Node Status";
    private String output;

    public NodeStatusTitledPane(FileFactory fileFactory) {
        super(title);
        nodeStatusAnalyzer = new NodeStatusAnalyzer(fileFactory);
        output = nodeStatusAnalyzer.getOutput();
        titledPane.setContent(generateTextArea(output));
    }

    private TextFlow generateTextFlow(String input) {
        TextFlow textFlow = new TextFlow();
        String[] lines = input.split("\n");

        for (String line : lines) {
            Text t = new Text();
            if (line.toLowerCase().contains("dcs")) {
                t.setStyle(fillInfo + ";" + fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
            } else if (line.toLowerCase().contains("total")) {
                t.setStyle(fillWarning + ";" + fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
            } else if (line.toLowerCase().contains("datacenter")) {
                t.setStyle(fillWarning + ";" + fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
            } else if (line.toLowerCase().contains("address")) {
                t.setStyle(fillTitle + ";" + fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
            } else {
                t.setStyle(fillValue + ";" + fontWeightBody + ";" + fontSizeM + ";" + fontFamilyAlign);
            }
            t.setText(line + "\n");
            textFlow.getChildren().add(t);
        }
        return textFlow;
    }

    public TitledPane getNodeStatusTitledPane() {
        return titledPane;
    }

    public String save_node_status_report()
    {
        return Inspector.saveReportFile(output,"node_status_info.out");
    }
}
