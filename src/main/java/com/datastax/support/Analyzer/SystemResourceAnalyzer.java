/*
 * Copyright (c)
 *
 * Date: 24/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Chun Gao on 24/1/18
 */

public class SystemResourceAnalyzer extends Analyzer {
    private final static Logger logger = LogManager.getLogger(SystemResourceAnalyzer.class);

    private ArrayList<JSONObject> cpuJSONList;
    private ArrayList<JSONObject> memoryJSONList;
    private ArrayList<Properties> nodetoolInfoPropertiesList;
    HashMap<String, Integer> cpuPadding;
    HashMap<String, Integer> memoryPadding;

    private String output = "";

    public SystemResourceAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
        try {
            nodetoolInfoPropertiesList = fileFactory.getNodetoolInfoPropertiesList();
            cpuJSONList = fileFactory.getCPUJSONList();
            memoryJSONList = fileFactory.getMemoryJSONList();
            cpuPadding = new HashMap<String, Integer>();
            memoryPadding = new HashMap<String, Integer>();
            analyze();
        } catch (Exception e) {
            logUncheckedException(logger, e);
        }
    }

    private void analyze() {
        ArrayList<String> cpuKeys = new ArrayList<String>();
        ArrayList<String> memoryKeys = new ArrayList<String>();
        String cpuOutput = "";
        String memoryOutput = "";
        String cpuTitle = "CPU Usage (Highest to Lowest)";
        String memoryTitle = "OS and Heap Memory Usage (Used OS Memory Highest to Lowest)";

        cpuOutput += cpuTitle + "\n" + printDividingLine(cpuTitle.length()) + "\n";
        memoryOutput += memoryTitle + "\n" + printDividingLine(memoryTitle.length()) + "\n";

        cpuKeys.add(ValFactory.NODEC);
        cpuKeys.add(ValFactory.DC);
        for (String key : ValFactory.CPUKEYLIST) {
            cpuKeys.add(key);
        }
        cpuPadding = initiatePadding(cpuKeys);

        memoryKeys.add(ValFactory.NODEC);
        memoryKeys.add(ValFactory.DC);
        memoryKeys.add(ValFactory.TOTAL);
        for (String key : ValFactory.MEMORYKEYLIST) {
            memoryKeys.add(key);
        }
        for (String key : ValFactory.HEAPKEYLIST) {
            memoryKeys.add(key);
        }
        memoryPadding = initiatePadding(memoryKeys);

        JSONArray unsortedCPUArray = new JSONArray();
        JSONArray sortedCPUArray;

        for (Properties nodetoolInfoProperties : nodetoolInfoPropertiesList) {
            ArrayList<String> cpuValueList = new ArrayList<String>();
            JSONObject cpuInfoJSON = new JSONObject();
            cpuInfoJSON.put(ValFactory.NODEC, nodetoolInfoProperties.getProperty(ValFactory.FILE_ID));
            cpuValueList.add(nodetoolInfoProperties.getProperty(ValFactory.FILE_ID));
            cpuInfoJSON.put(ValFactory.DC, nodetoolInfoProperties.getProperty(ValFactory.DATA_CENTER));
            cpuValueList.add(nodetoolInfoProperties.getProperty(ValFactory.DATA_CENTER));
            for (Object JSON : cpuJSONList) {
                JSONObject cpuJSON = (JSONObject) JSON;
                if (nodetoolInfoProperties.getProperty(ValFactory.FILE_ID).equals(cpuJSON.get(ValFactory.FILE_ID))) {
                    for (String key : ValFactory.CPUKEYLIST) {
                        cpuInfoJSON.put(key, cpuJSON.get(key));
                        cpuValueList.add(cpuJSON.get(key).toString());
                    }
                }
            }
            cpuPadding = calculatePadding(cpuPadding, cpuKeys, cpuValueList);
            unsortedCPUArray.add(cpuInfoJSON);
        }

        sortedCPUArray = sortJSONArray(unsortedCPUArray, "%idle", false, "double");

        for (String key : cpuKeys) {
            cpuOutput += String.format("%1$-" + cpuPadding.get(key) + "s", key);
        }

        cpuOutput += "\n";

        for (Object JSON : sortedCPUArray) {
            JSONObject cpuJSON = (JSONObject) JSON;
            for (String key : cpuKeys) {
                cpuOutput += String.format("%1$-" + cpuPadding.get(key) + "s", cpuJSON.get(key));
            }
            cpuOutput += "\n";
        }

        JSONArray unsortedMemoryArray = new JSONArray();
        JSONArray sortedMemoryArray;

        for (Properties nodetoolInfoProperties : nodetoolInfoPropertiesList) {
            ArrayList<String> memoryValueList = new ArrayList<String>();
            JSONObject memoryInfoJSON = new JSONObject();
            memoryInfoJSON.put(ValFactory.NODEC, nodetoolInfoProperties.getProperty(ValFactory.FILE_ID));
            memoryValueList.add(nodetoolInfoProperties.getProperty(ValFactory.FILE_ID));
            memoryInfoJSON.put(ValFactory.DC, nodetoolInfoProperties.getProperty(ValFactory.DATA_CENTER));
            memoryValueList.add(nodetoolInfoProperties.getProperty(ValFactory.DATA_CENTER));

            for (Object JSON : memoryJSONList) {
                JSONObject memoryJSON = (JSONObject) JSON;
                if (nodetoolInfoProperties.getProperty(ValFactory.FILE_ID).equals(memoryJSON.get(ValFactory.FILE_ID))) {
                    long totalMemory = 0;
                    for (String key : ValFactory.MEMORYKEYLIST) {
                        totalMemory += (Long)memoryJSON.get(key);
                    }
                    memoryInfoJSON.put(ValFactory.TOTAL, totalMemory);
                    memoryValueList.add(Long.toString(totalMemory));
                    for (String key : ValFactory.MEMORYKEYLIST) {
                        memoryInfoJSON.put(key, memoryJSON.get(key));
                        memoryValueList.add(memoryJSON.get(key).toString());
                    }
                }
            }

            memoryInfoJSON.put(ValFactory.HEAPKEYLIST.get(0), nodetoolInfoProperties.getProperty(ValFactory.USEDHEAPVALUE));
            memoryInfoJSON.put(ValFactory.HEAPKEYLIST.get(1), nodetoolInfoProperties.getProperty(ValFactory.MAXHEAPVALUE));
            memoryInfoJSON.put(ValFactory.HEAPKEYLIST.get(2), nodetoolInfoProperties.getProperty(ValFactory.OFFHEAPVALUE));

            memoryValueList.add(nodetoolInfoProperties.getProperty(ValFactory.USEDHEAPVALUE));
            memoryValueList.add(nodetoolInfoProperties.getProperty(ValFactory.MAXHEAPVALUE));
            memoryValueList.add(nodetoolInfoProperties.getProperty(ValFactory.OFFHEAPVALUE));

            memoryPadding = calculatePadding(memoryPadding, memoryKeys, memoryValueList);
            unsortedMemoryArray.add(memoryInfoJSON);
        }

        sortedMemoryArray = sortJSONArray(unsortedMemoryArray, "used", true, "long");

        for (String key : memoryKeys) {
            memoryOutput += String.format("%1$-" + memoryPadding.get(key) + "s", key);
        }

        memoryOutput += "\n";

        for (Object JSON : sortedMemoryArray) {
            JSONObject memoryJSON = (JSONObject) JSON;
            for (String key : memoryKeys) {
                memoryOutput += String.format("%1$-" + memoryPadding.get(key) + "s", memoryJSON.get(key));
            }
            memoryOutput += "\n";
        }

        output += cpuOutput + "\n" + memoryOutput;
        logger.debug("\n" + output);
    }

    public String getOutput() {
        return output;
    }
}
