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
import java.util.HashMap;

/**
 * Created by Chun Gao on 20/1/18
 */

public class TpstatsAnalyzer extends Analyzer {

    private final static Logger logger = LogManager.getLogger(TpstatsAnalyzer.class);

    private ArrayList<JSONObject> tpstatsJSONList;

    private String output = "";

    private JSONArray droppedArray;
    private HashMap<String, Integer> dropTablePadding;

    private JSONArray blockTableMutationStageArray;
    private HashMap<String, Integer> blockTableMutationStagePadding;
    private JSONArray blockTableViewMutationStageArray;
    private HashMap<String, Integer> blockTableViewMutationStagePadding;
    private JSONArray blockTableReadStageArray;
    private HashMap<String, Integer> blockTableReadStagePadding;
    private JSONArray blockTableRequestResponseStageArray;
    private HashMap<String, Integer> blockTableRequestResponseStagePadding;
    private JSONArray blockTableReadRepairStageArray;
    private HashMap<String, Integer> blockTableReadRepairStagePadding;
    private JSONArray blockTableCounterMutationStageArray;
    private HashMap<String, Integer> blockTableCounterMutationStagePadding;
    private JSONArray blockTableMiscStageArray;
    private HashMap<String, Integer> blockTableMiscStagePadding;
    private JSONArray blockTableCompactionExecutorArray;
    private HashMap<String, Integer> blockTableCompactionExecutorPadding;
    private JSONArray blockTableMemtableReclaimMemoryArray;
    private HashMap<String, Integer> blockTableMemtableReclaimMemoryPadding;
    private JSONArray blockTablePendingRangeCalculatorArray;
    private HashMap<String, Integer> blockTablePendingRangeCalculatorPadding;
    private JSONArray blockTableAntiCompactionExecutorArray;
    private HashMap<String, Integer> blockTableAntiCompactionExecutorPadding;
    private JSONArray blockTableGossipStageArray;
    private HashMap<String, Integer> blockTableGossipStagePadding;
    private JSONArray blockTableSecondaryIndexManagementArray;
    private HashMap<String, Integer> blockTableSecondaryIndexManagementPadding;
    private JSONArray blockTableHintsDispatcherArray;
    private HashMap<String, Integer> blockTableHintsDispatcherPadding;
    private JSONArray blockTableMigrationStageArray;
    private HashMap<String, Integer> blockTableMigrationStagePadding;
    private JSONArray blockTableMemtablePostFlushArray;
    private HashMap<String, Integer> blockTableMemtablePostFlushPadding;
    private JSONArray blockTableValidationExecutorArray;
    private HashMap<String, Integer> blockTableValidationExecutorPadding;
    private JSONArray blockTableSamplerArray;
    private HashMap<String, Integer> blockTableSamplerPadding;
    private JSONArray blockTableMemtableFlushWriterArray;
    private HashMap<String, Integer> blockTableMemtableFlushWriterPadding;
    private JSONArray blockTableInternalResponseStageArray;
    private HashMap<String, Integer> blockTableInternalResponseStagePadding;
    private JSONArray blockTableAntiEntropyStageArray;
    private HashMap<String, Integer> blockTableAntiEntropyStagePadding;
    private JSONArray blockTableCacheCleanupExecutorArray;
    private HashMap<String, Integer> blockTableCacheCleanupExecutorPadding;
    private JSONArray blockTableNTRArray;
    private HashMap<String, Integer> blockTableNTRPadding;

    public TpstatsAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
        tpstatsJSONList = fileFactory.getTpstatsJSONList();
        analyze();
    }

    private void analyze() {
        droppedArray = new JSONArray();
        dropTablePadding = initiatePadding(ValFactory.TPSTATS_DROP_TABLE);

        blockTableMutationStageArray = new JSONArray();
        blockTableViewMutationStageArray = new JSONArray();
        blockTableReadStageArray = new JSONArray();
        blockTableRequestResponseStageArray = new JSONArray();
        blockTableReadRepairStageArray = new JSONArray();
        blockTableCounterMutationStageArray = new JSONArray();
        blockTableMiscStageArray = new JSONArray();
        blockTableCompactionExecutorArray = new JSONArray();
        blockTableMemtableReclaimMemoryArray = new JSONArray();
        blockTablePendingRangeCalculatorArray = new JSONArray();
        blockTableAntiCompactionExecutorArray = new JSONArray();
        blockTableGossipStageArray = new JSONArray();
        blockTableSecondaryIndexManagementArray = new JSONArray();
        blockTableHintsDispatcherArray = new JSONArray();
        blockTableMigrationStageArray = new JSONArray();
        blockTableMemtablePostFlushArray = new JSONArray();
        blockTableValidationExecutorArray = new JSONArray();
        blockTableSamplerArray = new JSONArray();
        blockTableMemtableFlushWriterArray = new JSONArray();
        blockTableInternalResponseStageArray = new JSONArray();
        blockTableAntiEntropyStageArray = new JSONArray();
        blockTableCacheCleanupExecutorArray = new JSONArray();
        blockTableNTRArray = new JSONArray();

        blockTableMutationStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableViewMutationStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableReadStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableRequestResponseStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableReadRepairStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableCounterMutationStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableMiscStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableCompactionExecutorPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableMemtableReclaimMemoryPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTablePendingRangeCalculatorPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableAntiCompactionExecutorPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableGossipStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableSecondaryIndexManagementPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableHintsDispatcherPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableMigrationStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableMemtablePostFlushPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableValidationExecutorPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableSamplerPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableMemtableFlushWriterPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableInternalResponseStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableAntiEntropyStagePadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableCacheCleanupExecutorPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);
        blockTableNTRPadding = initiatePadding(ValFactory.TPSTATS_BLOCK_TABLE);

        for (Object tpstats : tpstatsJSONList) {
            if (tpstats instanceof JSONObject) {
                JSONObject tpstatsJSON = (JSONObject) tpstats;

                JSONArray tpstatsJSONArray = (JSONArray) tpstatsJSON.get(ValFactory.TPSTATS);
                JSONObject droppedValues = new JSONObject();

                for (Object values : tpstatsJSONArray) {
                    if (values instanceof JSONObject) {
                        droppedValues.put(ValFactory.ADDRESS, tpstatsJSON.get(ValFactory.FILE_ID));
                        JSONObject tpstatsValues = (JSONObject) values;

                        if (tpstatsValues.get(ValFactory.MESSAGE_TYPE)!=null) {
                            String msgType = (String)tpstatsValues.get(ValFactory.MESSAGE_TYPE);
                            String dropped = "--";
                            if (tpstatsValues.get(ValFactory.DROPPED)!=null) {
                                dropped = (String)tpstatsValues.get(ValFactory.DROPPED);
                            }
                            droppedValues.put(msgType, dropped);
                        }

                        if (tpstatsValues.get(ValFactory.POOL_NAME)!=null) {
                            tpstatsValues.put(ValFactory.ADDRESS, tpstatsJSON.get(ValFactory.FILE_ID));
                            String poolName = (String)tpstatsValues.get(ValFactory.POOL_NAME);
                            if (poolName.equals("MutationStage")) {
                                blockTableMutationStageArray = addToBlockJSONArray(blockTableMutationStageArray, tpstatsValues);
                                blockTableMutationStagePadding = getPadding(blockTableMutationStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("ViewMutationStage")) {
                                blockTableViewMutationStageArray = addToBlockJSONArray(blockTableViewMutationStageArray, tpstatsValues);
                                blockTableViewMutationStagePadding = getPadding(blockTableViewMutationStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("ReadStage")) {
                                blockTableReadStageArray = addToBlockJSONArray(blockTableReadStageArray, tpstatsValues);
                                blockTableReadStagePadding = getPadding(blockTableReadStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("RequestResponseStage")) {
                                blockTableRequestResponseStageArray = addToBlockJSONArray(blockTableRequestResponseStageArray, tpstatsValues);
                                blockTableRequestResponseStagePadding = getPadding(blockTableRequestResponseStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("ReadRepairStage")) {
                                blockTableReadRepairStageArray = addToBlockJSONArray(blockTableReadRepairStageArray, tpstatsValues);
                                blockTableReadRepairStagePadding = getPadding(blockTableReadRepairStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("CounterMutationStage")) {
                                blockTableCounterMutationStageArray = addToBlockJSONArray(blockTableCounterMutationStageArray, tpstatsValues);
                                blockTableCounterMutationStagePadding = getPadding(blockTableCounterMutationStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("MiscStage")) {
                                blockTableMiscStageArray = addToBlockJSONArray(blockTableMiscStageArray, tpstatsValues);
                                blockTableMiscStagePadding = getPadding(blockTableMiscStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("CompactionExecutor")) {
                                blockTableCompactionExecutorArray = addToBlockJSONArray(blockTableCompactionExecutorArray, tpstatsValues);
                                blockTableCompactionExecutorPadding = getPadding(blockTableCompactionExecutorPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("MemtableReclaimMemory")) {
                                blockTableMemtableReclaimMemoryArray = addToBlockJSONArray(blockTableMemtableReclaimMemoryArray, tpstatsValues);
                                blockTableMemtableReclaimMemoryPadding = getPadding(blockTableMemtableReclaimMemoryPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("PendingRangeCalculator")) {
                                blockTablePendingRangeCalculatorArray = addToBlockJSONArray(blockTablePendingRangeCalculatorArray, tpstatsValues);
                                blockTablePendingRangeCalculatorPadding = getPadding(blockTablePendingRangeCalculatorPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("AntiCompactionExecutor")) {
                                blockTableAntiCompactionExecutorArray = addToBlockJSONArray(blockTableAntiCompactionExecutorArray, tpstatsValues);
                                blockTableAntiCompactionExecutorPadding = getPadding(blockTableAntiCompactionExecutorPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("GossipStage")) {
                                blockTableGossipStageArray = addToBlockJSONArray(blockTableGossipStageArray, tpstatsValues);
                                blockTableGossipStagePadding = getPadding(blockTableGossipStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("SecondaryIndexManagement")) {
                                blockTableSecondaryIndexManagementArray = addToBlockJSONArray(blockTableSecondaryIndexManagementArray, tpstatsValues);
                                blockTableSecondaryIndexManagementPadding = getPadding(blockTableSecondaryIndexManagementPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("HintsDispatcher")) {
                                blockTableHintsDispatcherArray = addToBlockJSONArray(blockTableHintsDispatcherArray, tpstatsValues);
                                blockTableHintsDispatcherPadding = getPadding(blockTableHintsDispatcherPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("MigrationStage")) {
                                blockTableMigrationStageArray = addToBlockJSONArray(blockTableMigrationStageArray, tpstatsValues);
                                blockTableMigrationStagePadding = getPadding(blockTableMigrationStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("MemtablePostFlush")) {
                                blockTableMemtablePostFlushArray = addToBlockJSONArray(blockTableMemtablePostFlushArray, tpstatsValues);
                                blockTableMemtablePostFlushPadding = getPadding(blockTableMemtablePostFlushPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("ValidationExecutor")) {
                                blockTableValidationExecutorArray = addToBlockJSONArray(blockTableValidationExecutorArray, tpstatsValues);
                                blockTableValidationExecutorPadding = getPadding(blockTableValidationExecutorPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("Sampler")) {
                                blockTableSamplerArray = addToBlockJSONArray(blockTableSamplerArray, tpstatsValues);
                                blockTableSamplerPadding = getPadding(blockTableSamplerPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("MemtableFlushWriter")) {
                                blockTableMemtableFlushWriterArray = addToBlockJSONArray(blockTableMemtableFlushWriterArray, tpstatsValues);
                                blockTableMemtableFlushWriterPadding = getPadding(blockTableMemtableFlushWriterPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("InternalResponseStage")) {
                                blockTableInternalResponseStageArray = addToBlockJSONArray(blockTableInternalResponseStageArray, tpstatsValues);
                                blockTableInternalResponseStagePadding = getPadding(blockTableInternalResponseStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("AntiEntropyStage")) {
                                blockTableAntiEntropyStageArray = addToBlockJSONArray(blockTableAntiEntropyStageArray, tpstatsValues);
                                blockTableAntiEntropyStagePadding = getPadding(blockTableAntiEntropyStagePadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("CacheCleanupExecutor")) {
                                blockTableCacheCleanupExecutorArray = addToBlockJSONArray(blockTableCacheCleanupExecutorArray, tpstatsValues);
                                blockTableCacheCleanupExecutorPadding = getPadding(blockTableCacheCleanupExecutorPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            } else if (poolName.equals("Native-Transport-Requests")) {
                                blockTableNTRArray = addToBlockJSONArray(blockTableNTRArray, tpstatsValues);
                                blockTableNTRPadding = getPadding(blockTableNTRPadding, ValFactory.TPSTATS_BLOCK_TABLE, tpstatsValues);
                            }
                        }
                    }
                }

                boolean foundDroppedValue = false;
                for(int i=1; i<ValFactory.TPSTATS_DROP_TABLE.size(); i++) {
                    if (!droppedValues.get(ValFactory.TPSTATS_DROP_TABLE.get(i)).equals("0")) {
                        foundDroppedValue = true;
                    }
                }
                if (foundDroppedValue) {
                    droppedArray.add(droppedValues);
                    logger.debug("Drop Table Padding: " + dropTablePadding);
                    logger.debug("Dropped Values: " + droppedValues);
                    dropTablePadding = getPadding(dropTablePadding, ValFactory.TPSTATS_DROP_TABLE, droppedValues);
                }
            }
        }

        if (!droppedArray.isEmpty()) {
            droppedArray = sortJSONArray(droppedArray, ValFactory.MUTATION, true, "Long");
        }
        printArrayContent("Dropped Messages (Ordered by Dropped \"MUTATION\")", droppedArray, ValFactory.TPSTATS_DROP_TABLE, dropTablePadding);

        logger.debug("\n" + output);

        if(!blockTableNTRArray.isEmpty()) {
            blockTableNTRArray = sortJSONArray(blockTableNTRArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("Native-Transport-Requests (Ordered by \"All time blocked\")", blockTableNTRArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableNTRPadding);
        }

        if(!blockTableMutationStageArray.isEmpty()) {
            blockTableMutationStageArray = sortJSONArray(blockTableMutationStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("MutationStage (Ordered by \"All time blocked\")", blockTableMutationStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableMutationStagePadding);
        }

        if(!blockTableCompactionExecutorArray.isEmpty()) {
            blockTableCompactionExecutorArray = sortJSONArray(blockTableCompactionExecutorArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("CompactionExecutor (Ordered by \"All time blocked\")", blockTableCompactionExecutorArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableCompactionExecutorPadding);
        }

        if(!blockTableMemtableFlushWriterArray.isEmpty()) {
            blockTableMemtableFlushWriterArray = sortJSONArray(blockTableMemtableFlushWriterArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("MemtableFlushWriter (Ordered by \"All time blocked\")", blockTableMemtableFlushWriterArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableMemtableFlushWriterPadding);
        }

        if(!blockTableMemtablePostFlushArray.isEmpty()) {
            blockTableMemtablePostFlushArray = sortJSONArray(blockTableMemtablePostFlushArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("MemtablePostFlush (Ordered by \"All time blocked\")", blockTableMemtablePostFlushArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableMemtablePostFlushPadding);
        }

        if(!blockTableGossipStageArray.isEmpty()) {
            blockTableGossipStageArray = sortJSONArray(blockTableGossipStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("GossipStage (Ordered by \"All time blocked\")", blockTableGossipStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableGossipStagePadding);
        }

        if(!blockTableViewMutationStageArray.isEmpty()) {
            blockTableViewMutationStageArray = sortJSONArray(blockTableViewMutationStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("ViewMutationStage (Ordered by \"All time blocked\")", blockTableViewMutationStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableViewMutationStagePadding);
        }

        if(!blockTableReadStageArray.isEmpty()) {
            blockTableReadStageArray = sortJSONArray(blockTableReadStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("ReadStage (Ordered by \"All time blocked\")", blockTableReadStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableReadStagePadding);
        }

        if(!blockTableRequestResponseStageArray.isEmpty()) {
            blockTableRequestResponseStageArray = sortJSONArray(blockTableRequestResponseStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("RequestResponseStage (Ordered by \"All time blocked\")", blockTableRequestResponseStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableRequestResponseStagePadding);
        }

        if(!blockTableReadRepairStageArray.isEmpty()) {
            blockTableReadRepairStageArray = sortJSONArray(blockTableReadRepairStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("ReadRepairStage (Ordered by \"All time blocked\")", blockTableReadRepairStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableReadRepairStagePadding);
        }

        if(!blockTableCounterMutationStageArray.isEmpty()) {
            blockTableCounterMutationStageArray = sortJSONArray(blockTableCounterMutationStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("CounterMutationStage (Ordered by \"All time blocked\")", blockTableCounterMutationStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableCounterMutationStagePadding);
        }

        if(!blockTableMiscStageArray.isEmpty()) {
            blockTableMiscStageArray = sortJSONArray(blockTableMiscStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("MiscStage (Ordered by \"All time blocked\")", blockTableMiscStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableMiscStagePadding);
        }

        if(!blockTableMemtableReclaimMemoryArray.isEmpty()) {
            blockTableMemtableReclaimMemoryArray = sortJSONArray(blockTableMemtableReclaimMemoryArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("MemtableReclaimMemory (Ordered by \"All time blocked\")", blockTableMemtableReclaimMemoryArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableMemtableReclaimMemoryPadding);
        }

        if(!blockTablePendingRangeCalculatorArray.isEmpty()) {
            blockTablePendingRangeCalculatorArray = sortJSONArray(blockTablePendingRangeCalculatorArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("PendingRangeCalculator (Ordered by \"All time blocked\")", blockTablePendingRangeCalculatorArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTablePendingRangeCalculatorPadding);
        }

        if(!blockTableAntiCompactionExecutorArray.isEmpty()) {
            blockTableAntiCompactionExecutorArray = sortJSONArray(blockTableAntiCompactionExecutorArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("AntiCompactionExecutor (Ordered by \"All time blocked\")", blockTableAntiCompactionExecutorArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableAntiCompactionExecutorPadding);
        }

        if(!blockTableSecondaryIndexManagementArray.isEmpty()) {
            blockTableSecondaryIndexManagementArray = sortJSONArray(blockTableSecondaryIndexManagementArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("SecondaryIndexManagement (Ordered by \"All time blocked\")", blockTableSecondaryIndexManagementArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableSecondaryIndexManagementPadding);
        }

        if(!blockTableHintsDispatcherArray.isEmpty()) {
            blockTableHintsDispatcherArray = sortJSONArray(blockTableHintsDispatcherArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("HintsDispatcher (Ordered by \"All time blocked\")", blockTableHintsDispatcherArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableHintsDispatcherPadding);
        }

        if(!blockTableMigrationStageArray.isEmpty()) {
            blockTableMigrationStageArray = sortJSONArray(blockTableMigrationStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("MigrationStage (Ordered by \"All time blocked\")", blockTableMigrationStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableMigrationStagePadding);
        }

        if(!blockTableValidationExecutorArray.isEmpty()) {
            blockTableValidationExecutorArray = sortJSONArray(blockTableValidationExecutorArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("ValidationExecutor (Ordered by \"All time blocked\")", blockTableValidationExecutorArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableValidationExecutorPadding);
        }

        if(!blockTableSamplerArray.isEmpty()) {
            blockTableSamplerArray = sortJSONArray(blockTableSamplerArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("Sampler (Ordered by \"All time blocked\")", blockTableSamplerArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableSamplerPadding);
        }

        if(!blockTableInternalResponseStageArray.isEmpty()) {
            blockTableInternalResponseStageArray = sortJSONArray(blockTableInternalResponseStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("InternalResponseStage (Ordered by \"All time blocked\")", blockTableInternalResponseStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableInternalResponseStagePadding);
        }

        if(!blockTableAntiEntropyStageArray.isEmpty()) {
            blockTableAntiEntropyStageArray = sortJSONArray(blockTableAntiEntropyStageArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("AntiEntropyStage (Ordered by \"All time blocked\")", blockTableAntiEntropyStageArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableAntiEntropyStagePadding);
        }

        if(!blockTableCacheCleanupExecutorArray.isEmpty()) {
            blockTableCacheCleanupExecutorArray = sortJSONArray(blockTableCacheCleanupExecutorArray, ValFactory.ALL_TIME_BLOCKED, true, "String");
            printArrayContent("CacheCleanupExecutor (Ordered by \"All time blocked\")", blockTableCacheCleanupExecutorArray, ValFactory.TPSTATS_BLOCK_TABLE, blockTableCacheCleanupExecutorPadding);
        }

        logger.debug("\n" + output);
    }

    private JSONArray addToBlockJSONArray (JSONArray targetArray, JSONObject sourceObject) {
        for (String key : ValFactory.TPSTATS_BLOCK_TABLE) {
            if (sourceObject.get(key).equals(null)) {
                sourceObject.put(key, "--");
            }
        }

        if (!(sourceObject.get(ValFactory.ACTIVE).equals(null) && sourceObject.get(ValFactory.PENDING).equals(null)
                && sourceObject.get(ValFactory.COMPLETED).equals(null) && sourceObject.get(ValFactory.BLOCKED).equals(null)
                && sourceObject.get(ValFactory.ALL_TIME_BLOCKED).equals(null))
                && !(sourceObject.get(ValFactory.ACTIVE).equals("0") && sourceObject.get(ValFactory.PENDING).equals("0")
                && sourceObject.get(ValFactory.ALL_TIME_BLOCKED).equals("0"))) {
                for (String key : ValFactory.TPSTATS_BLOCK_TABLE) {
                    if (sourceObject.get(key).equals(null)) {
                        sourceObject.put(key, "--");
                    }
                }
            targetArray.add(sourceObject);
        }
        return targetArray;
    }

    private HashMap<String, Integer> getPadding(HashMap<String, Integer> padding, ArrayList<String> keyList, JSONObject jsonObject) {
        ArrayList <String> values = new ArrayList<String>();
        for (String key : keyList) {
            if (jsonObject.get(key) != null) {
                values.add((String)jsonObject.get(key));
            } else {
                values.add("--");
            }
        }
        return calculatePadding(padding, keyList, values);
    }

    private void printArrayContent(String title, JSONArray blockArray, ArrayList<String> keyList, HashMap<String, Integer> padding) {
        output += title + "\n" + printDividingLine(title.length()) + "\n";

        if (!blockArray.isEmpty()) {
            for (String key : keyList) {
                output += String.format("%1$-" + padding.get(key) + "s", key);
            }
            output += "\n";
            for (Object jsonObject : blockArray) {
                JSONObject values = (JSONObject) jsonObject;
                for (String key : keyList) {
                    if (values.get(key) != null) {
                        output += String.format("%1$-" + padding.get(key) + "s", values.get(key));
                    } else {
                        output += String.format("%1$-" + padding.get(key) + "s", "--");
                    }
                }
                output += "\n";
            }
            output += "\n";
        } else {
            output += "There has values to display\n\n";
        }
    }

    public String getOutput() {
        return output;
    }
}
