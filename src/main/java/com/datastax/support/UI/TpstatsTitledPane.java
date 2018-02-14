/*
 * Copyright (c)
 *
 * Date: 20/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Analyzer.TpstatsAnalyzer;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import javafx.scene.control.TitledPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chun Gao on 20/01/2018
 */

public class TpstatsTitledPane extends ComponentTitledPane {
    private static final Logger logger = LogManager.getLogger(TpstatsTitledPane.class);

    private TpstatsAnalyzer tpstatsAnalyzer;
    private static final String title = "Thread Pool Statistics";
    private String output;

    public TpstatsTitledPane (FileFactory fileFactory) {
        super(title);
        tpstatsAnalyzer = new TpstatsAnalyzer(fileFactory);
        output = tpstatsAnalyzer.getOutput();
        titledPane.setContent(generateTextArea(output));
    }

    public TitledPane getTpstatsTitledPane() {
        return titledPane;
    }
    public String save_thread_pool_stats_report()
    {
        return Inspector.saveReportFile(output,"thread_pool_stats.out");
    }
}
