/*
 * Copyright (c)
 *
 * Date: 25/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.DiagAnalyzer;

import com.datastax.support.Parser.DsetoolRingFileParser;
import com.datastax.support.Parser.NodetoolInfoFileParser;
import com.datastax.support.Parser.NodetoolStatusFileParser;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.StrFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class DiagAnalyzer {

    private final static Logger logger = LogManager.getLogger("DiagAnalyzer.class");

    private ArrayList<File> allFiles;
    private NodetoolStatusFileParser nodetoolStatusFileParser;
    private JSONObject nodetoolStatus;
    private DsetoolRingFileParser dsetoolRingFileParser;
    private JSONObject dsetoolRing;
    private NodetoolInfoFileParser nodetoolInfoFileParser;
    private ArrayList<Properties> nodetoolInfoProperties;

    public DiagAnalyzer(ArrayList<File> files) {
        this.allFiles = files;
        nodetoolStatusFileParser = new NodetoolStatusFileParser(allFiles);
        this.nodetoolStatus = nodetoolStatusFileParser.getNodetoolStatusJSON();
        dsetoolRingFileParser = new DsetoolRingFileParser(allFiles);
        this.dsetoolRing = dsetoolRingFileParser.getDsetoolRingJSON();
        nodetoolInfoFileParser = new NodetoolInfoFileParser(allFiles);
        nodetoolInfoProperties = nodetoolInfoFileParser.getNodetoolInfoProperties();
    }

    public void analyzeOSInfo() {

    }

    public void analyzeClusterInfo() {
        String output = "";
        String dcInfo = "|";
        String nodeInfo = "";
        int totalNumofNodes = 0;
        JSONArray status = (JSONArray) nodetoolStatus.get(StrFactory.STATUS);
        JSONArray ring = (JSONArray) dsetoolRing.get(StrFactory.RING);

        output += "Number of DCs: [" + status.size() + "]";

        for (Object dc : status) {
            JSONObject datacenter = (JSONObject) dc;
            dcInfo += " " + datacenter.get(StrFactory.DATACENTER) + " |";
            JSONObject statusPadding = (JSONObject) datacenter.get(StrFactory.PADDING);
            JSONArray statusNodes = (JSONArray) datacenter.get(StrFactory.NODES);
            JSONArray sortedNodes = sortJSONArray(statusNodes, StrFactory.RACK);
            JSONArray ringNodes = new JSONArray();

            nodeInfo += String.format("Datacenter: [" + datacenter.get(StrFactory.DATACENTER) + "] | Number of Nodes: [" + ((JSONArray) datacenter.get(StrFactory.NODES)).size() + "]\n");
            totalNumofNodes += ((JSONArray) datacenter.get(StrFactory.NODES)).size();

            HashMap<String, Integer> ringPadding = new HashMap<String, Integer>();
            ringPadding.put(StrFactory.DC, StrFactory.DC.length() + StrFactory.PAD);
            ringPadding.put(StrFactory.WORKLOAD, StrFactory.WORKLOAD.length() + StrFactory.PAD);
            ringPadding.put(StrFactory.OWNS, StrFactory.OWNS.length() + StrFactory.PAD);

            HashMap<String, Integer> nodetoolInfoPadding = new HashMap<String, Integer>();
            nodetoolInfoPadding.put(StrFactory.UPTIME, StrFactory.UPTIME.length() + StrFactory.PAD);

            for (Object ringNd : ring) {
                JSONObject ringNode = (JSONObject) ringNd;
                if (ringNode.get(StrFactory.DC).toString().equals(datacenter.get(StrFactory.DATACENTER).toString())) {
                    ringNodes.add(ringNode);
                    ringPadding.put(StrFactory.DC, ringPadding.get(StrFactory.DC) > ringNode.get(StrFactory.DC).toString().length() + StrFactory.PAD
                            ? ringPadding.get(StrFactory.DC) : ringNode.get(StrFactory.DC).toString().length() + StrFactory.PAD);
                    ringPadding.put(StrFactory.WORKLOAD, ringPadding.get(StrFactory.WORKLOAD) > ringNode.get(StrFactory.WORKLOAD).toString().length() + StrFactory.PAD
                            ? ringPadding.get(StrFactory.WORKLOAD) : ringNode.get(StrFactory.WORKLOAD).toString().length() + StrFactory.PAD);
                    ringPadding.put(StrFactory.OWNS, ringPadding.get(StrFactory.OWNS) > ringNode.get(StrFactory.OWNS).toString().length() + StrFactory.PAD
                            ? ringPadding.get(StrFactory.OWNS) : ringNode.get(StrFactory.OWNS).toString().length() + StrFactory.PAD);
                }
            }

            for (Properties properties : nodetoolInfoProperties) {
                if (properties.get(StrFactory.DATA_CENTER).toString().equals(datacenter.get(StrFactory.DATACENTER).toString())) {
                    nodetoolInfoPadding.put(StrFactory.UPTIME, nodetoolInfoPadding.get(StrFactory.UPTIME) > Inspector.secToTime(Integer.parseInt(properties.get(StrFactory.UPTIME_SECONDS).toString())).length() + StrFactory.PAD
                            ? nodetoolInfoPadding.get(StrFactory.UPTIME) : Inspector.secToTime(Integer.parseInt(properties.get(StrFactory.UPTIME_SECONDS).toString())).length() + StrFactory.PAD);
                }
            }

            for (String key : StrFactory.NODETOOLTATUS) {
                if(key.equals(StrFactory.UD)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(StrFactory.UPTIME) + "s", StrFactory.UPTIME);
                } else if(key.equals(StrFactory.ADDRESS)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + ringPadding.get(StrFactory.DC) + "s", StrFactory.DC);
                } else if (key.equals(StrFactory.TOKENS)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + ringPadding.get(StrFactory.WORKLOAD) + "s", StrFactory.WORKLOAD);
                } else if (key.equals(StrFactory.OWNS)) {
                    nodeInfo += String.format("%1$-" + ringPadding.get(StrFactory.OWNS) + "s", StrFactory.OWNS);
                } else {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                }
            }
            nodeInfo += "\n";

            for (Object statusNd : sortedNodes) {
                JSONObject statusNode = (JSONObject) statusNd;

                for (String key : StrFactory.NODETOOLTATUS) {
                    if (key.equals(StrFactory.UD)) {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                        boolean found = false;
                        for (Properties properties : nodetoolInfoProperties) {
                            if (properties.get(StrFactory.ID).toString().equals(statusNode.get(StrFactory.HOST_ID))) {
                                found = true;
                                nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(StrFactory.UPTIME) + "s", Inspector.secToTime(Integer.parseInt(properties.get(StrFactory.UPTIME_SECONDS).toString())).toString());
                            }
                        }
                        if (!found) {
                            nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(StrFactory.UPTIME) + "s", "--");
                        }
                    } else if (key.equals(StrFactory.ADDRESS)) {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                        for (Object ringNd : ringNodes) {
                            JSONObject ringNode = (JSONObject) ringNd;
                            if (statusNode.get(StrFactory.ADDRESS).toString().equals(ringNode.get(StrFactory.ADDRESS))) {
                                nodeInfo += String.format("%1$-" + ringPadding.get(StrFactory.DC) + "s", ringNode.get(StrFactory.DC));
                            }
                        }
                    } else if (key.equals(StrFactory.TOKENS)) {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                        for (Object ringNd : ringNodes) {
                            JSONObject ringNode = (JSONObject) ringNd;
                            if (statusNode.get(StrFactory.ADDRESS).toString().equals(ringNode.get(StrFactory.ADDRESS))) {
                                nodeInfo += String.format("%1$-" + ringPadding.get(StrFactory.WORKLOAD) + "s", ringNode.get(StrFactory.WORKLOAD));
                            }
                        }
                    } else if (key.equals(StrFactory.OWNS)) {
                        for (Object ringNd : ringNodes) {
                            JSONObject ringNode = (JSONObject) ringNd;
                            if (statusNode.get(StrFactory.ADDRESS).toString().equals(ringNode.get(StrFactory.ADDRESS))) {
                                nodeInfo += String.format("%1$-" + ringPadding.get(StrFactory.OWNS) + "s", ringNode.get(StrFactory.OWNS));
                            }
                        }
                    } else {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                    }
                }
                nodeInfo += "\n";
            }
            nodeInfo += "\n";
        }
        dcInfo += "\nTotal Number of Nodes: [" + totalNumofNodes + "]";
        output += " " + dcInfo + "\n\n" + nodeInfo;
        logger.debug("\n" + output);
    }

    public JSONArray sortJSONArray (JSONArray unsortedJSONArray, final String sortKey) {
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
