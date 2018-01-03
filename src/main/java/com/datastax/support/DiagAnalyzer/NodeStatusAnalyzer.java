/*
 * Copyright (c)
 *
 * Date: 25/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.DiagAnalyzer;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

/**
 * Created by Chun Gao on 25/11/2017
 */

public class NodeStatusAnalyzer extends Analyzer {
    private final static Logger logger = LogManager.getLogger("NodeStatusAnalyzer.class");

    public NodeStatusAnalyzer (ArrayList<File> files) {
        super(files);
    }

    public void analyze() {
        String output = "";
        String dcInfo = "|";
        String nodeInfo = "";
        int totalNumofNodes = 0;
        JSONArray status = (JSONArray) nodetoolStatus.get(ValFactory.STATUS);
        JSONArray ring = (JSONArray) dsetoolRing.get(ValFactory.RING);

        output += "Number of DCs: [" + status.size() + "]";

        for (Object dc : status) {
            JSONObject datacenter = (JSONObject) dc;
            dcInfo += " " + datacenter.get(ValFactory.DATACENTER) + " |";
            JSONObject statusPadding = (JSONObject) datacenter.get(ValFactory.PADDING);
            JSONArray statusNodes = (JSONArray) datacenter.get(ValFactory.NODES);
            JSONArray sortedNodes = sortJSONArray(statusNodes, ValFactory.RACK);
            JSONArray ringNodes = new JSONArray();

            nodeInfo += String.format("Datacenter: [" + datacenter.get(ValFactory.DATACENTER) + "] | Number of Nodes: [" + ((JSONArray) datacenter.get(ValFactory.NODES)).size() + "]\n");
            totalNumofNodes += ((JSONArray) datacenter.get(ValFactory.NODES)).size();

            HashMap<String, Integer> ringPadding = new HashMap<String, Integer>();
            ringPadding.put(ValFactory.DC, ValFactory.DC.length() + ValFactory.PAD);
            ringPadding.put(ValFactory.WORKLOAD, ValFactory.WORKLOAD.length() + ValFactory.PAD);
            ringPadding.put(ValFactory.OWNS, ValFactory.OWNS.length() + ValFactory.PAD);

            HashMap<String, Integer> nodetoolInfoPadding = new HashMap<String, Integer>();
            nodetoolInfoPadding.put(ValFactory.UPTIME, ValFactory.UPTIME.length() + ValFactory.PAD);

            for (Object ringNd : ring) {
                JSONObject ringNode = (JSONObject) ringNd;
                if (ringNode.get(ValFactory.DC).toString().equals(datacenter.get(ValFactory.DATACENTER).toString())) {
                    ringNodes.add(ringNode);
                    ringPadding.put(ValFactory.DC, ringPadding.get(ValFactory.DC) > ringNode.get(ValFactory.DC).toString().length() + ValFactory.PAD
                            ? ringPadding.get(ValFactory.DC) : ringNode.get(ValFactory.DC).toString().length() + ValFactory.PAD);
                    ringPadding.put(ValFactory.WORKLOAD, ringPadding.get(ValFactory.WORKLOAD) > ringNode.get(ValFactory.WORKLOAD).toString().length() + ValFactory.PAD
                            ? ringPadding.get(ValFactory.WORKLOAD) : ringNode.get(ValFactory.WORKLOAD).toString().length() + ValFactory.PAD);
                    ringPadding.put(ValFactory.OWNS, ringPadding.get(ValFactory.OWNS) > ringNode.get(ValFactory.OWNS).toString().length() + ValFactory.PAD
                            ? ringPadding.get(ValFactory.OWNS) : ringNode.get(ValFactory.OWNS).toString().length() + ValFactory.PAD);
                }
            }

            for (Properties properties : nodetoolInfoProperties) {
                if (properties.get(ValFactory.DATA_CENTER).toString().equals(datacenter.get(ValFactory.DATACENTER).toString())) {
                    nodetoolInfoPadding.put(ValFactory.UPTIME, nodetoolInfoPadding.get(ValFactory.UPTIME) > Inspector.secToTime(Integer.parseInt(properties.get(ValFactory.UPTIME_SECONDS).toString()),false).length() + ValFactory.PAD
                            ? nodetoolInfoPadding.get(ValFactory.UPTIME) : Inspector.secToTime(Integer.parseInt(properties.get(ValFactory.UPTIME_SECONDS).toString()),false).length() + ValFactory.PAD);
                }
            }

            for (String key : ValFactory.NODETOOLTATUS) {
                if(key.equals(ValFactory.UD)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(ValFactory.UPTIME) + "s", ValFactory.UPTIME);
                } else if(key.equals(ValFactory.ADDRESS)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.DC) + "s", ValFactory.DC);
                } else if (key.equals(ValFactory.TOKENS)) {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                    nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.WORKLOAD) + "s", ValFactory.WORKLOAD);
                } else if (key.equals(ValFactory.OWNS)) {
                    nodeInfo += String.format("%1$-" + ringPadding.get(ValFactory.OWNS) + "s", ValFactory.OWNS);
                } else {
                    nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", key);
                }
            }
            nodeInfo += "\n";

            for (Object statusNd : sortedNodes) {
                JSONObject statusNode = (JSONObject) statusNd;

                for (String key : ValFactory.NODETOOLTATUS) {
                    if (key.equals(ValFactory.UD)) {
                        nodeInfo += String.format("%1$-" + statusPadding.get(key) + "s", statusNode.get(key));
                        boolean found = false;
                        for (Properties properties : nodetoolInfoProperties) {
                            if (properties.get(ValFactory.ID).toString().equals(statusNode.get(ValFactory.HOST_ID))) {
                                found = true;
                                nodeInfo += String.format("%1$-" + nodetoolInfoPadding.get(ValFactory.UPTIME) + "s",
                                        Inspector.secToTime(Integer.parseInt(properties.get(ValFactory.UPTIME_SECONDS).toString()),false).toString());
                            }
                        }
                        if (!found) {
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
}
