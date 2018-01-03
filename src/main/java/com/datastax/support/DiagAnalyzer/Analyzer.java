/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.DiagAnalyzer;

import com.datastax.support.Parser.DsetoolRingFileParser;
import com.datastax.support.Parser.NodetoolInfoFileParser;
import com.datastax.support.Parser.NodetoolStatusFileParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

/**
 * Created by Chun Gao on 3/01/2018
 */

public class Analyzer {
    private final static Logger logger = LogManager.getLogger("Analyzer.class");

    protected ArrayList<File> allFiles;
    protected NodetoolStatusFileParser nodetoolStatusFileParser;
    protected JSONObject nodetoolStatus;
    protected DsetoolRingFileParser dsetoolRingFileParser;
    protected JSONObject dsetoolRing;
    protected NodetoolInfoFileParser nodetoolInfoFileParser;
    protected ArrayList<Properties> nodetoolInfoProperties;

    public Analyzer(ArrayList<File> files) {
        this.allFiles = files;
        nodetoolStatusFileParser = new NodetoolStatusFileParser(allFiles);
        this.nodetoolStatus = nodetoolStatusFileParser.getNodetoolStatusJSON();
        dsetoolRingFileParser = new DsetoolRingFileParser(allFiles);
        this.dsetoolRing = dsetoolRingFileParser.getDsetoolRingJSON();
        nodetoolInfoFileParser = new NodetoolInfoFileParser(allFiles);
        nodetoolInfoProperties = nodetoolInfoFileParser.getNodetoolInfoProperties();
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
                    logger.debug(jsone);
                }
                return valueA.compareTo(valueB);
            }
        });
        sortedJsonArray.addAll(jsonObjects);
        return sortedJsonArray;
    }
}
