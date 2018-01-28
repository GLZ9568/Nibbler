/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Util;

import com.datastax.support.Parser.*;
import com.datastax.support.UI.ClusterinfoPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Chun Gao on 19/11/2017
 * <p>
 * Create objects for selected files in the given directory
 */

public class FileFactory {

    private static final Logger logger = LogManager.getLogger(FileFactory.class);

    private File inputDirectory;
    private boolean initiateSuccessCheck;

    private ArrayList<File> allFiles = new ArrayList<File>();

    private ArrayList<JSONObject> cfstatsList;
    private ConfFileParser confFileParser;
    private ClusterInfoParser cip;
    private ArrayList<NibProperties> cassandraYamlPropertiesList;
    private ArrayList<NibProperties> addressYamlPropertiesList;
    private ArrayList<NibProperties> dseYamlPropertiesList;
    private ArrayList<NibProperties> clusterConfPropertiesList;
    private ArrayList<Map<String,Object>> cassandraYamlPropertyList;
    private ArrayList<Map<String,Object>> dseYamlPropertyList;
    private ArrayList<JSONObject> describeClusterJSONList;
    private JSONObject dsetoolRingJSON;
    private JSONFileParser jsonFileParser;
    private JSONObject clusterInfoJSON;
    private JSONObject nodeInfoJSON;
    private ArrayList<JSONObject> osInfoJSONList;
    private ArrayList<JSONObject> machineInfoJSONList;
    private ArrayList<JSONObject> cpuJSONList;
    private ArrayList<JSONObject> memoryJSONList;
    private ArrayList<JSONObject> diskSpaceJSONList;
    private ArrayList<JSONObject> javaSystemPropertiesJSONList;
    private ArrayList<Properties> nodetoolInfoPropertiesList;
    private JSONObject nodetoolStatusJSON;
    private ArrayList<JSONObject> tpstatsJSONList;
    private ArrayList<JSONObject> proxyHistogramsJSONList;
    private ArrayList<JSONObject> info_obj_list = new ArrayList<JSONObject>();

    private JSONObject cluster_info_obj;
    private JSONObject node_info_obj;
    private ArrayList<JSONObject> cpu_obj_list;
    private ArrayList<JSONObject> java_system_properties_obj_list;
    private ArrayList<JSONObject> machine_info_obj_list;
    private ArrayList<JSONObject> ntptime_obj_list;
    private ArrayList<JSONObject> ntpstat_obj_list;
    private ArrayList<JSONObject> os_info_obj_list;
    private ArrayList<JSONObject> disk_space_obj_list;

    private ArrayList<String> clusterName;
    private ArrayList<String> snitch_list;

    public FileFactory(final File inputDirectory) {
        this.inputDirectory = inputDirectory;
        initiateSuccessCheck = initiate (inputDirectory);
        parse();
    }

    private boolean initiate(final File directory) {
        try {
            for (final File entry : directory.listFiles()) {
                if (entry.isDirectory()) {
                    initiate(entry);
                } else {
                    allFiles.add(entry);
                }
            }
        } catch (NullPointerException npe) {
            logger.error(Inspector.getStackTrace(npe));
            return false;
        }
        return true;
    }

    private void parse () {
        this.cfstatsList = new CfstatsFileParser(allFiles).getCfstatsList();

        this.confFileParser = new ConfFileParser(allFiles);
        this.cassandraYamlPropertiesList = confFileParser.getCassandraYamlPropertiesList();
        this.addressYamlPropertiesList = confFileParser.getAddressYamlPropertiesList();

        this.dseYamlPropertiesList = confFileParser.getDSEYamlPropertiesList();
        this.clusterConfPropertiesList = confFileParser.getClusterConfPropertiesList();
        this.describeClusterJSONList = new DescribeClusterFileParser(allFiles).getDescribeClusterJSONList();
        this.dsetoolRingJSON = new DsetoolRingFileParser(allFiles).getDsetoolRingJSON();
        this.jsonFileParser = new JSONFileParser(allFiles);
        this.clusterInfoJSON = jsonFileParser.getClusterInfoJSON();
        this.nodeInfoJSON = jsonFileParser.getNodeInfoJSON();
        this.osInfoJSONList = jsonFileParser.getOSInfoJSONList();
        this.machineInfoJSONList = jsonFileParser.getMachineInfoJSONList();
        this.cpuJSONList = jsonFileParser.getCPUJSONList();
        this.memoryJSONList = jsonFileParser.getMemoryJSONList();
        this.diskSpaceJSONList = jsonFileParser.getDiskSpaceJSONList();
        this.javaSystemPropertiesJSONList = jsonFileParser.getJavaSystemPropertiesJSONList();
        this.nodetoolInfoPropertiesList = new NodetoolInfoFileParser(allFiles).getNodetoolInfoPropertiesList();
        this.nodetoolStatusJSON = new NodetoolStatusFileParser(allFiles).getNodetoolStatusJSON();
        this.tpstatsJSONList = new TpstatsFileParser(allFiles).getTpstatsJSONList();
        this.proxyHistogramsJSONList = new ProxyHistogramsFileParser(allFiles).getProxyHistogramsJSONList();
        this.cassandraYamlPropertyList =  new YamlFileParser(allFiles).getCassandraYamlPropertiesList();
        this.dseYamlPropertyList = new YamlFileParser(allFiles).getDSEYamlPropertiesList();
        this.info_obj_list = new NodetoolInfoParser(allFiles).getNodetoolInfoJSONList();

        this.clusterName = this.confFileParser.getClusterName();
        this.snitch_list = this.confFileParser.getSnitch_list();
        cip = new ClusterInfoParser(allFiles);
        this.disk_space_obj_list = new DiskSpaceParser(allFiles).getDisk_space_obj_list();
        this.cluster_info_obj =  cip.getCluster_info_obj();
        this.node_info_obj = cip.getNode_info_obj();
        this.cpu_obj_list = cip.getCpu_obj_list();
        this.java_system_properties_obj_list = cip.getJava_system_properties_obj_list();
        this.machine_info_obj_list = cip.getMachine_info_obj_list();
        this.ntptime_obj_list = cip.getNtptime_list();
        this.os_info_obj_list = cip.getOs_info_obj_list();
        this.ntpstat_obj_list = cip.getNtpstat_obj_list();

    }

    // get variables and objects created from input files
    public String getInputDirectory() {
        return inputDirectory.getAbsolutePath();
    }

    public ArrayList<File> getAllFiles() {
        return allFiles;
    }

    public boolean getInitiateSuccessCheck() {
        return initiateSuccessCheck;
    }

    public ArrayList<JSONObject> getCfstatsList() {
        return cfstatsList;
    }

    public ArrayList<NibProperties> getCassandraYamlPropertiesList() {
        return cassandraYamlPropertiesList;
    }

    public ArrayList<NibProperties> getAddressYamlPropertiesList() {
        return addressYamlPropertiesList;
    }

    public ArrayList<NibProperties> getDSEYamlPropertiesList() {
        return dseYamlPropertiesList;
    }

    public ArrayList<JSONObject> getDescribeClusterJSONList() {
        return describeClusterJSONList;
    }

    public ArrayList<NibProperties> getClusterConfPropertiesList() {
        return clusterConfPropertiesList;
    }

    public JSONObject getDsetoolRingJSON() {
        return dsetoolRingJSON;
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

    public ArrayList<Properties> getNodetoolInfoPropertiesList() {
        return nodetoolInfoPropertiesList;
    }

    public JSONObject getNodetoolStatusJSON() {
        return nodetoolStatusJSON;
    }

    public ArrayList<JSONObject> getTpstatsJSONList() {
        return tpstatsJSONList;
    }

    public ArrayList<JSONObject> getProxyHistogramsJSONList() {
        return proxyHistogramsJSONList;
    }

    public ArrayList<Map<String, Object>> getCassandraYamlPropertyList() {
        return cassandraYamlPropertyList;
    }

    public ArrayList<Map<String, Object>> getDseYamlPropertyList() {
        return dseYamlPropertyList;
    }

    public ArrayList<JSONObject> getInfo_obj_list() {
        return info_obj_list;
    }

    public JSONObject getCluster_info_obj() {
        return cluster_info_obj;
    }

    public JSONObject getNode_info_obj() {
        return node_info_obj;
    }

    public ArrayList<JSONObject> getCpu_obj_list() {
        return cpu_obj_list;
    }

    public ArrayList<JSONObject> getJava_system_properties_obj_list() {
        return java_system_properties_obj_list;
    }

    public ArrayList<JSONObject> getMachine_info_obj_list() {
        return machine_info_obj_list;
    }

    public ArrayList<JSONObject> getNtptime_obj_list() {
        return ntptime_obj_list;
    }

    public ArrayList<JSONObject> getNtpstat_obj_list() {
        return ntpstat_obj_list;
    }

    public ArrayList<JSONObject> getOs_info_obj_list() {
        return os_info_obj_list;
    }

    public ArrayList<JSONObject> getDisk_space_obj_list() {
        return disk_space_obj_list;
    }

    public ArrayList<NibProperties> getDseYamlPropertiesList() {
        return dseYamlPropertiesList;
    }

    public JSONFileParser getJsonFileParser() {
        return jsonFileParser;
    }

    public ArrayList<JSONObject> getOsInfoJSONList() {
        return osInfoJSONList;
    }

    public ArrayList<JSONObject> getCpuJSONList() {
        return cpuJSONList;
    }

    public ArrayList<String> getClusterName() {
        return clusterName;
    }

    public ArrayList<String> getSnitch_list() {
        return snitch_list;
    }
}
