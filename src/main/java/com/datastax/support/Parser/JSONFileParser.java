/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 3/01/2018
 */

public class JSONFileParser extends FileParser {
    private static final Logger logger = LogManager.getLogger(JSONFileParser.class);

    private JSONObject clusterInfoJSON;
    private JSONObject nodeInfoJSON;
    private ArrayList<JSONObject> osInfoJSONList;
    private ArrayList<JSONObject> machineInfoJSONList;
    private ArrayList<JSONObject> cpuJSONList;
    private ArrayList<JSONObject> memoryJSONList;
    private ArrayList<JSONObject> diskSpaceJSONList;
    private ArrayList<JSONObject> javaSystemPropertiesJSONList;

    public JSONFileParser(ArrayList<File> files) {
        super(files);
        parse();
    }

    private void parse() {
        clusterInfoJSON = new JSONObject();
        nodeInfoJSON = new JSONObject();
        osInfoJSONList = new ArrayList<JSONObject>();
        machineInfoJSONList = new ArrayList<JSONObject>();
        cpuJSONList = new ArrayList<JSONObject>();
        memoryJSONList = new ArrayList<JSONObject>();
        diskSpaceJSONList = new ArrayList<JSONObject>();
        javaSystemPropertiesJSONList = new ArrayList<JSONObject>();
        for (File file : files) {
            try {
                JSONParser parser = new JSONParser();
                if (file.getName().toLowerCase().contains(ValFactory.CLUSTER_INFO_JSON.toLowerCase())) {
                    clusterInfoJSON = (JSONObject) parser.parse(new FileReader(file.getAbsolutePath()));
                } else if (file.getName().toLowerCase().contains(ValFactory.NODE_INFO_JSON.toLowerCase())) {
                    nodeInfoJSON = (JSONObject) parser.parse(new FileReader(file.getAbsolutePath()));
                } else if (file.getName().toLowerCase().contains(ValFactory.OS_INFO_JSON.toLowerCase())) {
                    osInfoJSONList.add((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())));
                } else if (file.getName().toLowerCase().contains(ValFactory.MACHINE_INFO_JSON.toLowerCase())) {
                    machineInfoJSONList.add((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())));
                } else if (file.getName().toLowerCase().contains(ValFactory.CPU_JSON.toLowerCase())) {
                    cpuJSONList.add((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())));
                } else if (file.getName().toLowerCase().contains(ValFactory.MEMORY_JSON.toLowerCase())) {
                    memoryJSONList.add((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())));
                } else if (file.getName().toLowerCase().contains(ValFactory.DISK_SPACE_JSON.toLowerCase())) {
                    diskSpaceJSONList.add((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())));
                } else if (file.getName().toLowerCase().contains(ValFactory.JAVA_SYSTEM_PROPERTIES_JSON.toLowerCase())) {
                    javaSystemPropertiesJSONList.add((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())));
                }
            } catch (FileNotFoundException fnfe) {
                logCheckedException(logger, fnfe);
            } catch (ParseException pe) {
                logCheckedException(logger, pe);
            } catch (IOException ioe) {
                logCheckedException(logger, ioe);
            }
        }
    }

    public JSONObject getClusterInfoJSON() {
        return clusterInfoJSON;
    }

    public JSONObject getNodeInfoJSON() {
        return nodeInfoJSON;
    }

    public ArrayList<JSONObject> getOSInfoJSONList() {
        return osInfoJSONList;
    }

    public ArrayList<JSONObject> getMachineInfoJSONList() {
        return machineInfoJSONList;
    }

    public ArrayList<JSONObject> getCPUJSONList() {
        return cpuJSONList;
    }

    public ArrayList<JSONObject> getMemoryJSONList() {
        return memoryJSONList;
    }

    public ArrayList<JSONObject> getDiskSpaceJSONList() {
        return diskSpaceJSONList;
    }

    public ArrayList<JSONObject> getJavaSystemPropertiesJSONList() {
        return javaSystemPropertiesJSONList;
    }
}
