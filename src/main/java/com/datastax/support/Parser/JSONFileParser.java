/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
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
                    clusterInfoJSON = addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file);
                } else if (file.getName().toLowerCase().contains(ValFactory.NODE_INFO_JSON.toLowerCase())) {
                    nodeInfoJSON = addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file);
                } else if (file.getName().toLowerCase().contains(ValFactory.OS_INFO_JSON.toLowerCase())) {
                    osInfoJSONList.add(addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file));
                } else if (file.getName().toLowerCase().contains(ValFactory.MACHINE_INFO_JSON.toLowerCase())) {
                    machineInfoJSONList.add(addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file));
                } else if (file.getName().toLowerCase().contains(ValFactory.CPU_JSON.toLowerCase())) {
                    cpuJSONList.add(addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file));
                } else if (file.getName().toLowerCase().contains(ValFactory.MEMORY_JSON.toLowerCase())) {
                    memoryJSONList.add(addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file));
                } else if (file.getName().toLowerCase().contains(ValFactory.DISK_SPACE_JSON.toLowerCase())) {
                    diskSpaceJSONList.add(addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file));
                } else if (file.getName().toLowerCase().contains(ValFactory.JAVA_SYSTEM_PROPERTIES_JSON.toLowerCase())) {
                    javaSystemPropertiesJSONList.add(addFileInfo((JSONObject) parser.parse(new FileReader(file.getAbsolutePath())), file));
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

    private JSONObject addFileInfo (JSONObject jsonObject, File file) {
        String id = Inspector.getFileID(file);
        jsonObject.put(ValFactory.FILE_ID, id);
        jsonObject.put(ValFactory.FILE_PATH, file.getAbsolutePath());
        jsonObject.put(ValFactory.FILE_NAME, file.getName());
        return jsonObject;
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
