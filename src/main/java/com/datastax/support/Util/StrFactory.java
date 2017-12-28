/*
 * Copyright (c)
 *
 * Date: 26/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Chun Gao on 26/11/2017
 */

public class StrFactory {

    // common
    public static final String NONE_EXIST = "none_exist";
    public static final String NODE = "node";
    public static final String NODES = "nodes";
    public static final String FILE_ID = "file_id";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_PATH = "file_path";
    public static final String OPSCENTERD = "opscenterd";
    public static final String KEYSPACE = "Keyspace";
    public static final String KEYSPACES = "Keyspaces";
    public static final String TABLE = "Table";
    public static final String TABLES = "Tables";

    public static final String ISCLUSTER_INFOEXIST = "iscluster_infoexist";
    public static final String ISNODE_INFOEXIST = "isnode_infoexist";
    public static final String ISCPUEXIST = "iscpuexist";
    public static final String ISJAVA_SYSTEM_PROPERTIESEXIST = "isjava_system_propertiesexist";
    public static final String ISMACHINE_INFOEXIST = "ismachine_infoexist";
    public static final String NTPTIME_CONTENT = "ntptime_content";
    public static final String NTPTIME_STAUS = "ntptime_status";
    public static final String NTPTIME_OFFSET = "ntptime_offset";
    // file names
    // conf file names;
    public static final String CLUSTER_INFO = "cluster_info.json";
    public static final String NODE_INFO = "node_info.json";
    public static final String CPU = "cpu.json";
    public static final String JAVA_SYSTEM_PROPERTIES = "java_system_properties.json";
    public static final String MACHINE_INFO = "machine-info.json";
    public static final String NTPTIME = "ntptime";
    public static final String CASSANDRA_YAML = "cassandra.yaml";
    public static final String ADDRESS_YAML = "address.yaml";
    public static final String DSE_YAML = "dse.yaml";
    public static final String CONF_SURFFIX = ".conf";

    // cassandra.yaml configurations
    public static final String SEEDS = "seeds";
    public static final String CLUSTER_NAME = "cluster_name";
    public static final String NUM_TOKENS = "num_tokens";

    // agentaddress.yaml configurations

    // nodetool output
    public static final String NODETOOL = "nodetool";
    public static final String CFSTATS = "cfstats";
    public static final String COMPACTIONHISTORY = "compactionhistory";
    public static final String COMPACTIONSTATS = "compactionstats";
    public static final String GETCOMPACTIONTHROUGHPUT = "getcompactionthroughput";
    public static final String GETSTREAMTHROUGHPUT = "getstreamthroughput";
    public static final String GOSSIPINFO = "gossipinfo";
    public static final String INFO = "info";
    public static final String NETSTATS = "netstats";
    public static final String PROXYHISTOGRAMS = "proxyhistograms";

    public static final String DSETOOL = "dsetool";
    public static final String RING = "ring";

    public static final String STATUS = "status";
    public static final String STAT = "stat";
    public static final String DATACENTER = "Datacenter";
    public static final String ADDRESS = "Address";
    public static final String LOAD = "Load";
    public static final String TOKENS = "Tokens";
    public static final String OWNS = "Owns";
    public static final String HOST_ID = "Host ID";
    public static final String RACK = "Rack";
    public static final String UN = "UN";
    public static final String UL = "UL";
    public static final String UJ = "UJ";
    public static final String UM = "UM";
    public static final String DN = "DN";
    public static final String DL = "DL";
    public static final String DJ = "DJ";
    public static final String DM = "DM";
    public static final ArrayList<String> NODESTATUS = new ArrayList<String>(Arrays.asList(UN, UL, UJ, UM, DN, DL, DJ, DM));

    public static final String TPSTATS = "tpstats";
    public static final String POOL_NAME = "Pool Name";
    public static final String ACTIVE = "Active";
    public static final String PENDING = "Pending";
    public static final String COMPLETED = "Completed";
    public static final String BLOCKED = "Blocked";
    public static final String ALL_TIME_BLOCKED = "All time blocked";
    public static final ArrayList<String> TPSTATS_POOL = new ArrayList<String>(Arrays.asList(POOL_NAME, ACTIVE, PENDING, COMPLETED, BLOCKED, ALL_TIME_BLOCKED));
    public static final String MESSAGE_TYPE = "Message type";
    public static final String DROPPED = "Dropped";
    public static final ArrayList<String> TPSTATS_MSG = new ArrayList<String>(Arrays.asList(MESSAGE_TYPE, DROPPED));

    public static final String DESCRIBECLUSTER = "describecluster";
    public static final String CLUSTER_INFORMATION = "Cluster Inforamtion";
    public static final String SCHEMA_VERSIONS = "Schema versions";

    public static final String STATUSBINARY = "statusbinary";
    public static final String STATUSTHRIFT = "statusthrift";
    public static final String VERSION = "version";



    // dsetool output

    // ntp output

    // os output
}
