/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by Chun Gao on 3/01/2018
 */

public class Analyzer {
    private final static Logger logger = LogManager.getLogger("Analyzer.class");

    protected FileFactory fileFactory;
    
    public Analyzer(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    protected JSONArray sortJSONArray (JSONArray unsortedJSONArray, final String sortKey) {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

        for (Object jsonObject : unsortedJSONArray) {
            jsonObjects.add((JSONObject) jsonObject);
        }

        Collections.sort(jsonObjects, new Comparator<JSONObject>() {
            public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                String valueA = new String();
                String valueB = new String();
                try {
                    valueA = (String) jsonObjectA.get(sortKey);
                    valueB = (String) jsonObjectB.get(sortKey);
                } catch (JSONException jsone) {
                    logException(logger, jsone);
                }
                return valueA.compareTo(valueB);
            }
        });
        sortedJsonArray.addAll(jsonObjects);
        return sortedJsonArray;
    }

    protected void logException (Logger logger, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        logger.error("Exception: " + e);
        logger.error("StackTrace: " + stackTrace.toString());
    }
}
