/*
 * Copyright (c)
 *
 * Date: 20/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chun Gao on 20/1/18
 */

public class TpstatsAnalyzer extends Analyzer {

    private final static Logger logger = LogManager.getLogger(TpstatsAnalyzer.class);

    private ArrayList<JSONObject> tpstatsJSONList;

    private String output = "";

    public TpstatsAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
        tpstatsJSONList = fileFactory.getTpstatsJSONList();
        analyze();
    }

    private void analyze() {
        for (Object tpstats : tpstatsJSONList) {
            if (tpstats instanceof JSONObject) {
                JSONObject tpstatsJSON = (JSONObject) tpstats;

                JSONObject nodeTpstatsJSON = new JSONObject();
                nodeTpstatsJSON.put(ValFactory.ADDRESS, tpstatsJSON.get(ValFactory.FILE_ID));

                JSONArray tpstatsJSONArray = (JSONArray) tpstatsJSON.get(ValFactory.TPSTATS);

                for (Object values : tpstatsJSONArray) {
                    if (values instanceof JSONObject) {
                        JSONObject tpstatsValues = (JSONObject) values;
                        tpstatsValues.keySet().contains(ValFactory.POOL_NAME);

                    }
                }
            }
        }
        logger.debug("NodeDCMap: " + nodeDCMap);
        logger.debug("Tpstats JSON: " + tpstatsJSONList.get(0));


    }

    public String getOutput() {
        return output;
    }
}
