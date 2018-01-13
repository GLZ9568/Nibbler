/*
 * Copyright (c)
 *
 * Date: 25/11/2017
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

import java.util.*;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class NodeStatusAnalyzer extends Analyzer {
    private final static Logger logger = LogManager.getLogger("NodeStatusAnalyzer.class");

    private JSONObject nodetoolStatusJSON;
    private JSONObject dsetoolRingJSON;
    private ArrayList<Properties> nodetoolInfoPropertiesList;
    private String output = "";

    public NodeStatusAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
        this.nodetoolStatusJSON = fileFactory.getNodetoolStatusJSON();
        this.dsetoolRingJSON = fileFactory.getDsetoolRingJSON();
        this.nodetoolInfoPropertiesList = fileFactory.getNodetoolInfoPropertiesList();
        analyze();
    }

    private void analyze() {
        String dcInfo = "|";
        String nodeInfo = "";
        int totalNumofNodes = 0;
        JSONArray statusJSONArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        JSONArray ringJSONArray = (JSONArray) dsetoolRingJSON.get(ValFactory.RING);

        output += "Number of DCs: [" + statusJSONArray.size() + "]";

        for (Object dc : statusJSONArray) {
            JSONObject datacenterJSON = (JSONObject) dc;
            dcInfo += " " + datacenterJSON.get(ValFactory.DATACENTER) + " |";
            JSONObject statusPadding = (JSONObject) datacenterJSON.get(ValFactory.PADDING);
            JSONArray statusNodes = (JSONArray) datacenterJSON.get(ValFactory.NODES);
            JSONArray sortedStatusNodes = sortJSONArray(statusNodes, ValFactory.RACK);
            JSONArray ringNodes = new JSONArray();

            nodeInfo += String.format("Datacenter: [" + datacenterJSON.get(ValFactory.DATACENTER) + "] | Number of Nodes: [" + ((JSONArray) datacenterJSON.get(ValFactory.NODES)).size() + "]\n");
            totalNumofNodes += ((JSONArray) datacenterJSON.get(ValFactory.NODES)).size();

            HashMap<String, Integer> ringPadding = new HashMap<String, Integer>();
            ringPadding.put(ValFactory.DC, ValFactory.DC.length() + ValFactory.PAD);
            ringPadding.put(ValFactory.WORKLOAD, ValFactory.WORKLOAD.length() + ValFactory.PAD);
            ringPadding.put(ValFactory.OWNS, ValFactory.OWNS.length() + ValFactory.PAD);
            boolean foundGraph = false;
            boolean foundHealth = false;

            HashMap<String, Integer> nodetoolInfoPadding = new HashMap<String, Integer>();
            nodetoolInfoPadding.put(ValFactory.UPTIME, ValFactory.UPTIME.length() + ValFactory.PAD);

            for (Object ringNd : ringJSONArray) {
                JSONObject ringNode = (JSONObject) ringNd;
                if (ringNode.get(ValFactory.DC).toString().equals(datacenterJSON.get(ValFactory.DATACENTER).toString())) {
                    ringNodes.add(ringNode);
                    ringPadding.put(ValFactory.DC, ringPadding.get(ValFactory.DC) > ringNode.get(ValFactory.DC).toString().length() + ValFactory.PAD
                            ? ringPadding.get(ValFactory.DC) : ringNode.get(ValFactory.DC).toString().length() + ValFactory.PAD);
                    ringPadding.put(ValFactory.WORKLOAD, ringPadding.get(ValFactory.WORKLOAD) > ringNode.get(ValFactory.WORKLOAD).toString().length() + ValFactory.PAD
                            ? ringPadding.get(ValFactory.WORKLOAD) : ringNode.get(ValFactory.WORKLOAD).toString().length() + ValFactory.PAD);
                    ringPadding.put(ValFactory.OWNS, ringPadding.get(ValFactory.OWNS) > ringNode.get(ValFactory.OWNS).toString().length() + ValFactory.PAD
                            ? ringPadding.get(ValFactory.OWNS) : ringNode.get(ValFactory.OWNS).toString().length() + ValFactory.PAD);

                    if (ringNode.entrySet().toString().contains(ValFactory.GRAPH)) {
                        if (!foundGraph) {
                            foundGraph = true;
                            ringPadding.put(ValFactory.GRAPH, ValFactory.GRAPH.length() + ValFactory.PAD);
                        } else {
                            ringPadding.put(ValFactory.GRAPH, ringPadding.get(ValFactory.GRAPH) > ringNode.get(ValFactory.GRAPH).toString().length() + ValFactory.PAD
                                    ? ringPadding.get(ValFactory.GRAPH) : ringNode.get(ValFactory.GRAPH).toString().length() + ValFactory.PAD);
                        }
                    }

                    if (ringNode.entrySet().toString().contains(ValFactory.HEALTH)) {
                        if (!foundHealth) {
                            foundHealth = true;
                            ringPadding.put(ValFactory.HEALTH, ValFactory.HEALTH.length() + ValFactory.PAD);
                        } else {
                            ringPadding.put(ValFactory.HEALTH, ringPadding.get(ValFactory.HEALTH) > ringNode.get(ValFactory.HEALTH).toString().length() + ValFactory.PAD
                                    ? ringPadding.get(ValFactory.HEALTH) : ringNode.get(ValFactory.HEALTH).toString().length() + ValFactory.PAD);
                        }
                    }
                }
            }

            for (Properties properties : nodetoolInfoPropertiesList) {
                if (properties.get(ValFactory.DATA_CENTER).toString().equals(datacenterJSON.get(ValFactory.DATACENTER).toString())) {
                    nodetoolInfoPadding.put(ValFactory.UPTIME, nodetoolInfoPadding.get(ValFactory.UPTIME) > Inspector.secToTime(Integer.parseInt(properties.get(ValFactory.UPTIME_SECONDS).toString()), false).length() + ValFactory.PAD
                            ? nodetoolInfoPadding.get(ValFactory.UPTIME) : Inspector.secToTime(Integer.parseInt(properties.get(ValFactory.UPTIME_SECONDS).toString()), false).length() + ValFactory.PAD);
                }
            }

            for (String key : ValFactory.NODETOOLTATUS) {
                if(key.equals(ValFactory.UD)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(ValFactory.UPTIME) + "s", ValFactory.UPTIME);
                    if(foundHealth) {
                        nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.HEALTH) + "s", ValFactory.HEALTH);
                    }
                } else if(key.equals(ValFactory.ADDRESS)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.DC) + "s", ValFactory.DC);
                } else if (key.equals(ValFactory.TOKENS)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.WORKLOAD) + "s", ValFactory.WORKLOAD);
                    if(foundGraph) {
                        nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.GRAPH) + "s", ValFactory.GRAPH);
                    }
                } else if (key.equals(ValFactory.OWNS)) {
                    nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.OWNS) + "s", ValFactory.OWNS);
                } else {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                }
            }
            nodeInfo += "\n";

            for (Object statusNd : sortedStatusNodes) {
                JSONObject statusNode = (JSONObject) statusNd;

                for (String key : ValFactory.NODETOOLTATUS) {
                    if (key.equals(ValFactory.UD)) {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                        boolean foundNodeProperty = false;
                        for (Properties properties : nodetoolInfoPropertiesList) {
                            if (properties.get(ValFactory.ID).toString().equals(statusNode.get(ValFactory.HOST_ID))) {
                                foundNodeProperty = true;
                                nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(ValFactory.UPTIME) + "s",
                                        Inspector.secToTime(Integer.parseInt(properties.get(ValFactory.UPTIME_SECONDS).toString()), false).toString());
                                if (foundHealth) {
                                    for (Object ringNd : ringNodes) {
                                        JSONObject ringNode = (JSONObject) ringNd;
                                        if (statusNode.get(ValFactory.ADDRESS).toString().equals(ringNode.get(ValFactory.ADDRESS))) {
                                            nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.HEALTH) + "s", ringNode.get(ValFactory.HEALTH));
                                        }
                                    }
                                }
                            }
                        }
                        if (!foundNodeProperty) {
                            nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(ValFactory.UPTIME) + "s", "--");
                        }
                    } else if (key.equals(ValFactory.ADDRESS)) {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                        for (Object ringNd : ringNodes) {
                            JSONObject ringNode = (JSONObject) ringNd;
                            if (statusNode.get(ValFactory.ADDRESS).toString().equals(ringNode.get(ValFactory.ADDRESS))) {
                                nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.DC) + "s", ringNode.get(ValFactory.DC));
                            }
                        }
                    } else if (key.equals(ValFactory.TOKENS)) {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                        for (Object ringNd : ringNodes) {
                            JSONObject ringNode = (JSONObject) ringNd;
                            if (statusNode.get(ValFactory.ADDRESS).toString().equals(ringNode.get(ValFactory.ADDRESS))) {
                                nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.WORKLOAD) + "s", ringNode.get(ValFactory.WORKLOAD));
                                if (foundGraph) {
                                    nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.GRAPH) + "s", ringNode.get(ValFactory.GRAPH));
                                }
                            }
                        }
                    } else if (key.equals(ValFactory.OWNS)) {
                        for (Object ringNd : ringNodes) {
                            JSONObject ringNode = (JSONObject) ringNd;
                            if (statusNode.get(ValFactory.ADDRESS).toString().equals(ringNode.get(ValFactory.ADDRESS))) {
                                nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.OWNS) + "s", ringNode.get(ValFactory.OWNS));
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

    public String getOutput () {
        return output;
    }
}
