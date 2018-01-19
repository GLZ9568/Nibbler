/*
 * Copyright (c)
 *
 * Date: 10/1/2018
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
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Chun Gao on 10/1/18
 */

public class CfstatsAnalyzer extends Analyzer {
    private static final Logger logger = LogManager.getLogger(CfstatsAnalyzer.class);

    private final String largePartition = "Compacted partition maximum bytes";
    private final String spaceUsed = "Space used (total)";
    private final String maxTombStone = "Maximum tombstones per slice (last five minutes)";
    private final String maxLiveCell = "Maximum live cells per slice (last five minutes)";
    private final long largePartitionQualifier = 104857600; // 100M in byte size
    private ArrayList<String> largePartitionKeys;
    private ArrayList<String> maxTombstoneKeys;
    private ArrayList<String> maxLiveCellKeys;
    private HashMap<String, Integer> largePartitionPadding;
    private HashMap<String, Integer> maxTombstonePadding;
    private HashMap<String, Integer> maxLiveCellPadding;
    private HashMap<String, Object> largePartitionMap;
    private HashMap<String, Object> maxTombstoneMap;
    private HashMap<String, Object> maxLiveCellMap;
    private JSONArray unsortedLargePartitionArray = new JSONArray();
    private JSONArray unsortedMaxTombstonePerSliceArray = new JSONArray();
    private JSONArray unsortedMaxLiveCellPerSliceArray = new JSONArray();
    private boolean foundLargePartition = false;
    private boolean foundMaxTombstonePerSlice = false;
    private boolean foundMaxLiveCellPerSlice = false;
    private String output = "";


    ArrayList<JSONObject> cfstatsList = new ArrayList<JSONObject>();

    public CfstatsAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
        try {
            largePartitionKeys = new ArrayList<String>(Arrays.asList(ValFactory.NODEC, ValFactory.KEYSPACE, ValFactory.TABLE,
                    ValFactory.LARGE_PARTITION, ValFactory.NUMBER_OF_KEYS, ValFactory.TABLE_SPACE_USED));
            largePartitionPadding = initiatePadding(largePartitionKeys);

            maxTombstoneKeys = new ArrayList<String>(Arrays.asList(ValFactory.NODEC, ValFactory.KEYSPACE, ValFactory.TABLE,
                    ValFactory.MAX_TOMBSTONE, ValFactory.NUMBER_OF_KEYS, ValFactory.TABLE_SPACE_USED));
            maxTombstonePadding = initiatePadding(maxTombstoneKeys);
            maxLiveCellKeys = new ArrayList<String>(Arrays.asList(ValFactory.NODEC, ValFactory.KEYSPACE, ValFactory.TABLE,
                    ValFactory.MAX_LIVE_CELL, ValFactory.NUMBER_OF_KEYS, ValFactory.TABLE_SPACE_USED));
            maxLiveCellPadding = initiatePadding(maxLiveCellKeys);

            analyze();
        } catch (Exception e) {
            output = "Encoutntered Unchecked Exception";
            logUncheckedException(logger, e);
        }
    }

    private void analyze() {
        cfstatsList = fileFactory.getCfstatsList();
        String largePartitionTitle = "Compacted Large Partition (Partition Size > " + calByte(largePartitionQualifier) + ")";
        String maxTombstoneTitle = "Maximum Tombstones Per Slice (Last 5 Minutes)";
        String maxLiveCellTitle = "Maximum Live Cells Per Slice (Last 5 Minutes)";
        String localReadLatencyTitle = "Local Read Latency";

        output += largePartitionTitle + "\n" + printDividingLine(largePartitionTitle.length()) + "\n";

        largePartitionMap = new HashMap<String, Object>();
        maxTombstoneMap = new HashMap<String, Object>();
        maxLiveCellMap = new HashMap<String, Object>();

        for (JSONObject cfstats : cfstatsList) {
            String fileID = (String) cfstats.get(ValFactory.FILE_ID);
            JSONArray keyspaces = (JSONArray) cfstats.get(ValFactory.KEYSPACES);
            extractInfo(fileID, keyspaces);
        }

        if (foundLargePartition) {
            JSONArray sortedLargePartitionArray = sortJSONArray(unsortedLargePartitionArray, ValFactory.LARGE_PARTITION, true, "long");

            for (Object largePartition : sortedLargePartitionArray) {
                if (largePartition instanceof JSONObject) {
                    ((JSONObject)largePartition).put(ValFactory.LARGE_PARTITION,
                            ((JSONObject)largePartition).get(ValFactory.LARGE_PARTITION).toString() + " (" + calByte(Long.parseLong(((JSONObject)largePartition).get(ValFactory.LARGE_PARTITION).toString())) + ")");
                    ((JSONObject)largePartition).put(ValFactory.TABLE_SPACE_USED,
                            ((JSONObject)largePartition).get(ValFactory.TABLE_SPACE_USED).toString() + " (" + calByte(Long.parseLong(((JSONObject)largePartition).get(ValFactory.TABLE_SPACE_USED).toString())) + ")");
                }
            }
            output += printJSONArrayWithPadding(sortedLargePartitionArray, largePartitionKeys, largePartitionPadding, false, ValFactory.PRINT_ALL_RECORDS);
        } else {
            String exp = "No Partition Larger Than " + largePartitionQualifier /1024/1024 + " MB Found";
            output += exp + "\n" + printDividingLine(exp.length()) + "\n";
        }

        output += "\n" + maxTombstoneTitle + "\n" + printDividingLine(maxTombstoneTitle.length()) + "\n";

        if (foundMaxTombstonePerSlice) {
            JSONArray sortedMaxTombstonePerSliceArray = sortJSONArray(unsortedMaxTombstonePerSliceArray, ValFactory.MAX_TOMBSTONE, true, "double");

            for (Object maxTombstonePerSlice : sortedMaxTombstonePerSliceArray) {
                if (maxTombstonePerSlice instanceof JSONObject) {
                   ((JSONObject)maxTombstonePerSlice).put(ValFactory.TABLE_SPACE_USED,
                            ((JSONObject)maxTombstonePerSlice).get(ValFactory.TABLE_SPACE_USED).toString());
                }
            }
            output += printJSONArrayWithPadding(sortedMaxTombstonePerSliceArray, maxTombstoneKeys, maxTombstonePadding, false, ValFactory.CFSTATS_NO_OF_RECORDS);
        } else {
            String exp = "All Maximum Tombstones Per Slice (Last 5 Minutes) Are 0.0";
            output += exp + "\n";
            output += printDividingLine(exp.length());
        }

        output += "\n" + maxLiveCellTitle + "\n" + printDividingLine(maxLiveCellTitle.length()) + "\n";

        if (foundMaxLiveCellPerSlice) {
            JSONArray sortedMaxLiveCellPerSliceArray = sortJSONArray(unsortedMaxLiveCellPerSliceArray, ValFactory.MAX_LIVE_CELL, true, "double");

            for (Object maxLiveCellPerSlice : sortedMaxLiveCellPerSliceArray) {
                if (maxLiveCellPerSlice instanceof JSONObject) {
                    ((JSONObject)maxLiveCellPerSlice).put(ValFactory.TABLE_SPACE_USED,
                            ((JSONObject)maxLiveCellPerSlice).get(ValFactory.TABLE_SPACE_USED).toString());
                }
            }
            output += printJSONArrayWithPadding(sortedMaxLiveCellPerSliceArray, maxLiveCellKeys, maxLiveCellPadding, false, ValFactory.CFSTATS_NO_OF_RECORDS);
        } else {
            String exp = "Maximum Live Cells Per Slice (Last 5 Minutes) Are 0.0";
            output += exp + "\n";
            output += printDividingLine(exp.length());
        }

        logger.debug("Sorted Large Partition JSON Array: \n" + output);
    }

    private void extractInfo(String fileID, JSONArray keyspaces) {
        for (Object ks : keyspaces) {
            if(ks instanceof JSONObject) {
                JSONObject keyspace = (JSONObject) ks;
                JSONArray tables = (JSONArray) keyspace.get(ValFactory.TABLES);
                for (Object tb : tables) {
                    if(tb instanceof JSONObject) {
                        JSONObject table = (JSONObject) tb;

                        JSONObject largePartitionJSON = new JSONObject();
                        long partitionSize = Long.parseLong((String)table.get(largePartition));
                        if(partitionSize > largePartitionQualifier) {
                            foundLargePartition = true;
                            String largePartitionMapKey = keyspace.get(ValFactory.KEYSPACE).toString() + "." + table.get(ValFactory.TABLE).toString();
                            if (!largePartitionMap.containsKey(largePartitionMapKey) || ((Long) largePartitionMap.get(largePartitionMapKey)) < partitionSize) {
                                largePartitionMap.put(largePartitionMapKey, partitionSize);
                                ArrayList<String> largePartitionValueList = new ArrayList<String>(Arrays.asList(fileID, keyspace.get(ValFactory.KEYSPACE).toString(),
                                        table.get(ValFactory.TABLE).toString(), String.valueOf(partitionSize),
                                        table.get(ValFactory.NUMBER_OF_KEYS).toString(), table.get(spaceUsed).toString()));
                                ArrayList<String> largePartitionPaddingValueList = new ArrayList<String>(Arrays.asList(fileID, keyspace.get(ValFactory.KEYSPACE).toString(),
                                        table.get(ValFactory.TABLE).toString(), String.valueOf(partitionSize) + " (" + calByte(partitionSize) + ")",
                                        table.get(ValFactory.NUMBER_OF_KEYS).toString(), table.get(spaceUsed).toString() + " (" + calByte(Long.parseLong(table.get(spaceUsed).toString())) + ")"));
                                if (largePartitionKeys.size() == largePartitionValueList.size()) {
                                    for (int i=0; i<largePartitionKeys.size(); i++) {
                                        largePartitionJSON.put(largePartitionKeys.get(i), largePartitionValueList.get(i));
                                    }
                                    largePartitionPadding = calculatePadding(largePartitionPadding, largePartitionKeys, largePartitionPaddingValueList);
                                    unsortedLargePartitionArray.add(largePartitionJSON);
                                }
                            }
                        }

                        JSONObject maxTombstoneJSON = new JSONObject();
                        double maxTombstonePerSlice = Double.parseDouble((String)table.get(maxTombStone));
                        if(maxTombstonePerSlice > 0) {
                            foundMaxTombstonePerSlice = true;
                            String maxTombstoneMapKey = keyspace.get(ValFactory.KEYSPACE).toString() + "." + table.get(ValFactory.TABLE).toString();
                            if (!maxTombstoneMap.containsKey(maxTombstoneMapKey)) {
                                maxTombstoneMap.put(maxTombstoneMapKey, maxTombstonePerSlice);

                                ArrayList<String> maxTomeStonePerSliceValueList = new ArrayList<String>(Arrays.asList(fileID, keyspace.get(ValFactory.KEYSPACE).toString(),
                                        table.get(ValFactory.TABLE).toString(), String.valueOf(maxTombstonePerSlice), table.get(ValFactory.NUMBER_OF_KEYS).toString(),
                                        table.get(spaceUsed).toString() + " (" + calByte(Double.parseDouble(table.get(spaceUsed).toString())) + ")"));
                                if (maxTombstoneKeys.size() == maxTomeStonePerSliceValueList.size()) {
                                    for (int i = 0; i < maxTombstoneKeys.size(); i++) {
                                        maxTombstoneJSON.put(maxTombstoneKeys.get(i), maxTomeStonePerSliceValueList.get(i));
                                    }
                                    maxTombstonePadding = calculatePadding(maxTombstonePadding, maxTombstoneKeys, maxTomeStonePerSliceValueList);
                                    unsortedMaxTombstonePerSliceArray.add(maxTombstoneJSON);
                                }
                            }
                        }

                        JSONObject maxLiveCellJSON = new JSONObject();
                        double maxLiveCellPerSlice = Double.parseDouble((String)table.get(maxLiveCell));
                        if(maxLiveCellPerSlice > 0) {
                            foundMaxLiveCellPerSlice = true;
                            String maxLiveCellMapKey = keyspace.get(ValFactory.KEYSPACE).toString() + "." + table.get(ValFactory.TABLE).toString();
                            if (!maxLiveCellMap.containsKey(maxLiveCellMapKey)) {
                                maxLiveCellMap.put(maxLiveCellMapKey, maxLiveCellPerSlice);

                                ArrayList<String> maxLiveCellPerSliceValueList = new ArrayList<String>(Arrays.asList(fileID, keyspace.get(ValFactory.KEYSPACE).toString(),
                                        table.get(ValFactory.TABLE).toString(), String.valueOf(maxLiveCellPerSlice), table.get(ValFactory.NUMBER_OF_KEYS).toString(),
                                        table.get(maxLiveCell).toString() + " (" + calByte(Double.parseDouble(table.get(maxLiveCell).toString())) + ")"));
                                if (maxLiveCellKeys.size() == maxLiveCellPerSliceValueList.size()) {
                                    for (int i = 0; i < maxLiveCellKeys.size(); i++) {
                                        maxLiveCellJSON.put(maxLiveCellKeys.get(i), maxLiveCellPerSliceValueList.get(i));
                                    }
                                    maxLiveCellPadding = calculatePadding(maxLiveCellPadding, maxLiveCellKeys, maxLiveCellPerSliceValueList);
                                    unsortedMaxLiveCellPerSliceArray.add(maxLiveCellJSON);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getOutput() {
        return output;
    }
 }
