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
import java.util.HashMap;

/**
 * Created by Chun Gao on 26/11/2017
 */

public class ValFactory {

    // values
    public static final int PAD = 2;
    public static final int CFSTATS_NO_OF_RECORDS = 10;
    public static final int PRINT_ALL_RECORDS = 0;
    String testme = "";

    // common
    public static final String NONE_EXIST = "none_exist";
    public static final String NODE = "node";
    public static final String NODEC = "Node";
    public static final String NODES = "nodes";
    public static final String FILE_ID = "file_id";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_PATH = "file_path";
    public static final String OPSCENTERD = "opscenterd";
    public static final String KEYSPACE = "Keyspace";
    public static final String KEYSPACES = "Keyspaces";
    public static final String TABLE = "Table";
    public static final String TABLES = "Tables";
    public static final String PADDING = "Padding";

    public static final String ISCLUSTER_INFOEXIST = "iscluster_infoexist";
    public static final String ISNODE_INFOEXIST = "isnode_infoexist";
    public static final String ISCPUEXIST = "iscpuexist";
    public static final String ISJAVA_SYSTEM_PROPERTIESEXIST = "isjava_system_propertiesexist";
    public static final String ISMACHINE_INFOEXIST = "ismachine_infoexist";
    public static final String NTPTIME_CONTENT = "ntptime_content";
    public static final String NTPTIME_STAUS = "ntptime_status";
    public static final String NTPTIME_OFFSET = "ntptime_offset";
    public static final String INFO_GENERATION = "info_generation";
    public static final String INFO_DATACENTER = "Data Center";
    public static final String INFO_UPTIME = "info_uptime";
    public static final String UPTIME = "Uptime";
    public static final String UPTIME_SECONDS = "Uptime (seconds)";
    public static final String ID = "ID";
    public static final String INFO_TOTALHEAP = "info_totalheap";
    public static final String INFO_USEDHEAP = "info_usedheap";
    public static final String INFO_OFFHEAP = "info_offheap";
    public static final String DISK_NAME = "disk_name";
    public static final String TOTAL_SPACE = "total_space";
    public static final String USED_SPACE = "used_space";
    public static final String DISK_SPACE_USAGE = "disk_space_usage";
    public static final String PERCENT_REPAIRED = "percent_repaired";
    // file names
    // conf file names;
    public static final String CLUSTER_INFO_JSON = "cluster_info.json";
    public static final String NODE_INFO_JSON = "node_info.json";
    public static final String CPU_JSON = "cpu.json";
    public static final String MEMORY_JSON = "memory.json";
    public static final String OS_INFO_JSON = "os-info.json";
    public static final String JAVA_SYSTEM_PROPERTIES_JSON = "java_system_properties.json";
    public static final String MACHINE_INFO_JSON = "machine-info.json";
    public static final String DISK_SPACE_JSON = "disk_space.json";
    public static final String NTPTIME = "ntptime";
    public static final String CASSANDRA_YAML = "cassandra.yaml";
    public static final String ADDRESS_YAML = "address.yaml";
    public static final String DSE_YAML = "dse.yaml";
    public static final String CONF_SURFFIX = ".conf";

    // cassandra.yaml configurations
    public static final String SEEDS = "seeds";
    public static final String CLUSTER_NAME = "cluster_name";
    public static final String NUM_TOKENS = "num_tokens";
    public static final String SNITCH = "endpoint_snitch";

    // agentaddress.yaml configurations

    // nodetool output
    public static final String NODETOOL = "nodetool";
    public static final String CFSTATS = "cfstats";
    public static final String LARGE_PARTITION = "Compacted large partition";
    public static final String MAX_TOMBSTONE = "Max tombstone per slice (last 5 minutes)";
    public static final String MAX_LIVE_CELL = "Max live cells per slice (last 5 minutes)";
    public static final String NUMBER_OF_KEYS = "Number of keys (estimate)";
    public static final String TABLE_SPACE_USED = "Current space used (total)";
    public static final String PARTITION_SIZE = "Partition Size";
    public static final String COMPACTIONHISTORY = "compactionhistory";
    public static final String COMPACTIONSTATS = "compactionstats";
    public static final String GETCOMPACTIONTHROUGHPUT = "getcompactionthroughput";
    public static final String GETSTREAMTHROUGHPUT = "getstreamthroughput";
    public static final String GOSSIPINFO = "gossipinfo";
    public static final String INFO = "info";
    public static final String NETSTATS = "netstats";

    public static final String PROXYHISTOGRAMS = "proxyhistograms";
    public static final String MIN = "Min";
    public static final String MAX = "Max";
    public static final String PERCENTILE = "PCT";
    public static final String READ_LATENCY = "RLAT_\u03BCs";
    public static final String WRITE_LATENCY = "WLAT_\u03BCs";
    public static final String RANGE_LATENCY = "RGLAT_\u03BCs";
    public static final String CAS_READ_LATENCY = "CASRLAT_\u03BCs";
    public static final String CAS_WRITE_LATENCY = "CASWLAT_\u03BCs";
    public static final String VIEW_WRITE_LATENCY = "VWWLAT_\u03BCs";

    public static final String AUTHENTICATOR = "Authenticator";
    public static final String AUTHORIZER = "Authorizer";
    public static final String role_manager = "role_manager";
    public static final String roles_validity_in_ms = "roles_validity(ms)";
    public static final String permissions_validity_in_ms = "perm_valid(ms)";
    public static final String concurrent_compactors = "concur_compact";
    public static final String concurrent_reads = "concur_read";
    public static final String concurrent_writes = "concur_write";
    public static final String read_request_timeout_in_ms = "read_timeout(ms)";
    public static final String write_request_timeout_in_ms = "write_timeout(ms)";
    public static final String streaming_socket_timeout_in_ms = "stream_timeout(ms)";
    public static final String authentication_options = "authent_option";
    public static final String role_management_options = "role_man_option";



    public static final String DSETOOL = "dsetool";
    public static final String RING = "ring";
    public static final String DC = "DC";
    public static final String WORKLOAD = "Workload";
    public static final String GRAPH = "Graph";
    public static final String HEALTH = "Health";

    public static final String STATUS = "status";
    public static final String UD = "U/D";
    public static final String STATa = "Stat";
    public static final String DATACENTER = "Datacenter";
    public static final String DATA_CENTER = "Data Center";
    public static final String ADDRESS = "Address";
    public static final String HOSTNAME = "Hostname";
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
    public static final ArrayList<String> NODETOOLTATUS = new ArrayList<String>(Arrays.asList(UD, ADDRESS, RACK, TOKENS, LOAD, OWNS, HOST_ID));

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

    public static final double SCREEN_WIDTH_FACTOR = 0.7;
    public static final double SCREEN_HEIGHT_FACTOR = 0.8;

    public static final String supported_platform_url = "http://docs.datastax.com/en/landing_page/doc/landing_page/supportedPlatforms.html";

    public static final HashMap<String, String> aws_instance = new HashMap<String, String>(){
        {
            put("t2.nano","vCPU: 1, Mem(GB): 0.5, Storage: EBS Only, Enhanced Networking: No");
            put("t2.micro","vCPU: 1, Mem(GB): 1, Storage: EBS Only, Enhanced Networking: No");
            put("t2.small","vCPU: 1, Mem(GB): 2, Storage: EBS Only, Enhanced Networking: No");
            put("t2.medium","vCPU: 2, Mem(GB): 4, Storage: EBS Only, Enhanced Networking: No");
            put("t2.large","vCPU: 2, Mem(GB): 8, Storage: EBS Only, Enhanced Networking: No");
            put("t2.xlarge","vCPU: 4, Mem(GB): 16, Storage: EBS Only, Enhanced Networking: No");
            put("t2.2xlarge","vCPU: 8, Mem(GB): 32, Storage: EBS Only, Enhanced Networking: No");
            put("m5.large","vCPU: 2, Mem(GB): 8, Storage: EBS Only, Enhanced Networking: Yes");
            put("m5.xlarge","vCPU: 4, Mem(GB): 16, Storage: EBS Only, Enhanced Networking: Yes");
            put("m5.2xlarge","vCPU: 8, Mem(GB): 32, Storage: EBS Only, Enhanced Networking: Yes");
            put("m5.4xlarge","vCPU: 16, Mem(GB): 64, Storage: EBS Only, Enhanced Networking: Yes");
            put("m5.12xlarge","vCPU: 48, Mem(GB): 192, Storage: EBS Only, Enhanced Networking: Yes");
            put("m5.24xlarge","vCPU: 96, Mem(GB): 384, Storage: EBS Only, Enhanced Networking: Yes");
            put("m4.large","vCPU: 2, Mem(GB): 8, Storage: EBS Only, Enhanced Networking: Yes");
            put("m4.xlarge","vCPU: 4, Mem(GB): 16, Storage: EBS Only, Enhanced Networking: Yes");
            put("m4.2xlarge","vCPU: 8, Mem(GB): 32, Storage: EBS Only, Enhanced Networking: Yes");
            put("m4.4xlarge","vCPU: 16, Mem(GB): 64, Storage: EBS Only, Enhanced Networking: Yes");
            put("m4.10xlarge","vCPU: 40, Mem(GB): 160, Storage: EBS Only, Enhanced Networking: Yes");
            put("m4.16xlarge","vCPU: 64, Mem(GB): 256, Storage: EBS Only, Enhanced Networking: Yes");
            put("m3.medium","vCPU: 1, Mem(GB): 3.75, Storage: 1 x 4 SSD, Enhanced Networking: No");
            put("m3.large","vCPU: 2, Mem(GB): 7.5, Storage: 1 x 32 SSD, Enhanced Networking: No");
            put("m3.xlarge","vCPU: 4, Mem(GB): 15, Storage: 2 x 40 SSD, Enhanced Networking: No");
            put("m3.2xlarge","vCPU: 8, Mem(GB): 30, Storage: 2 x 80 SSD, Enhanced Networking: No");
            put("c5.large","vCPU: 2, Mem(GB): 4, Storage: EBS Only, Enhanced Networking: Yes");
            put("c5.xlarge","vCPU: 4, Mem(GB): 8, Storage: EBS Only, Enhanced Networking: Yes");
            put("c5.2xlarge","vCPU: 8, Mem(GB): 16, Storage: EBS Only, Enhanced Networking: Yes");
            put("c5.4xlarge","vCPU: 16, Mem(GB): 32, Storage: EBS Only, Enhanced Networking: Yes");
            put("c5.9xlarge","vCPU: 36, Mem(GB): 72, Storage: EBS Only, Enhanced Networking: Yes");
            put("c5.18xlarge","vCPU: 72, Mem(GB): 144, Storage: EBS Only, Enhanced Networking: Yes");
            put("c4.large","vCPU: 2, Mem(GB): 3.75, Storage: EBS Only, Enhanced Networking: Yes");
            put("c4.xlarge","vCPU: 4, Mem(GB): 7.5, Storage: EBS Only, Enhanced Networking: Yes");
            put("c4.2xlarge","vCPU: 8, Mem(GB): 15, Storage: EBS Only, Enhanced Networking: Yes");
            put("c4.4xlarge","vCPU: 16, Mem(GB): 30, Storage: EBS Only, Enhanced Networking: Yes");
            put("c4.8xlarge","vCPU: 36, Mem(GB): 60, Storage: EBS Only, Enhanced Networking: Yes");
            put("c3.large","vCPU: 2, Mem(GB): 3.75, Storage: 2 x 16 SSD, Enhanced Networking: Yes");
            put("c3.xlarge","vCPU: 4, Mem(GB): 7.5, Storage: 2 x 40 SSD, Enhanced Networking: Yes");
            put("c3.2xlarge","vCPU: 8, Mem(GB): 15, Storage: 2 x 80 SSD, Enhanced Networking: Yes");
            put("c3.4xlarge","vCPU: 16, Mem(GB): 30, Storage: 2 x 160 SSD, Enhanced Networking: Yes");
            put("c3.8xlarge","vCPU: 32, Mem(GB): 60, Storage: 2 x 320 SSD, Enhanced Networking: Yes");
            put("x1.16large","vCPU: 64, Mem(GB): 976, Storage: 1 x 1,920 SSD, Enhanced Networking: Yes");
            put("x1.32xlarge","vCPU: 128, Mem(GB): 1952, Storage: 2 x 1,920 SSD, Enhanced Networking: Yes");
            put("x1e.xlarge","vCPU: 4, Mem(GB): 122, Storage: 1 x 120 SSD, Enhanced Networking: Yes");
            put("x1e.2xlarge","vCPU: 8, Mem(GB): 244, Storage: 1 x 240 SSD, Enhanced Networking: Yes");
            put("x1e.4xlarge","vCPU: 16, Mem(GB): 488, Storage: 1 x 480 SSD, Enhanced Networking: Yes");
            put("x1e.8xlarge","vCPU: 32, Mem(GB): 976, Storage: 1 x 960 SSD, Enhanced Networking: Yes");
            put("x1e.16xlarge","vCPU: 64, Mem(GB): 1952, Storage: 1 x 1,920 SSD, Enhanced Networking: Yes");
            put("x1e.32xlarge","vCPU: 128, Mem(GB): 3904, Storage: 2 x 1,920 SSD, Enhanced Networking: Yes");
            put("r4.large","vCPU: 2, Mem(GB): 15.25, Storage: EBS Only, Enhanced Networking: Yes");
            put("r4.xlarge","vCPU: 4, Mem(GB): 30.5, Storage: EBS Only, Enhanced Networking: Yes");
            put("r4.2xlarge","vCPU: 8, Mem(GB): 61, Storage: EBS Only, Enhanced Networking: Yes");
            put("r4.4xlarge","vCPU: 16, Mem(GB): 122, Storage: EBS Only, Enhanced Networking: Yes");
            put("r4.8xlarge","vCPU: 32, Mem(GB): 244, Storage: EBS Only, Enhanced Networking: Yes");
            put("r4.16xlarge","vCPU: 64, Mem(GB): 488, Storage: EBS Only, Enhanced Networking: Yes");
            put("r3.large","vCPU: 2, Mem(GB): 15.25, Storage: 1 x 32 SSD, Enhanced Networking: Yes");
            put("r3.xlarge","vCPU: 4, Mem(GB): 30.5, Storage: 1 x 80 SSD, Enhanced Networking: Yes");
            put("r3.2xlarge","vCPU: 8, Mem(GB): 61, Storage: 1 x 160 SSD, Enhanced Networking: Yes");
            put("r3.4xlarge","vCPU: 16, Mem(GB): 122, Storage: 1 x 320 SSD, Enhanced Networking: Yes");
            put("r3.8xlarge","vCPU: 32, Mem(GB): 244, Storage: 2 x 320 SSD, Enhanced Networking: Yes");

            put("p3.2xlarge","vCPU: 8, Mem(GB): 61, Storage: EBS Only, Enhanced Networking: Yes");
            put("p3.8xlarge","vCPU: 32, Mem(GB): 244, Storage: EBS Only, Enhanced Networking: Yes");
            put("p3.16xlarge","vCPU: 64, Mem(GB): 488, Storage: EBS Only, Enhanced Networking: Yes");

            put("p2.xlarge","vCPU: 4, Mem(GB): 61, Storage: EBS Only, Enhanced Networking: Yes");
            put("p2.8xlarge","vCPU: 32, Mem(GB): 488, Storage: EBS Only, Enhanced Networking: Yes");
            put("p2.16xlarge","vCPU: 64, Mem(GB): 732, Storage: EBS Only, Enhanced Networking: Yes");
            put("g3.4xlarge","vCPU: 16, Mem(GB): 122, Storage: EBS Only, Enhanced Networking: Yes");
            put("g3.8xlarge","vCPU: 32, Mem(GB): 244, Storage: EBS Only, Enhanced Networking: Yes");
            put("g3.16xlarge","vCPU: 64, Mem(GB): 488, Storage: EBS Only, Enhanced Networking: Yes");
            put("f1.2xlarge","vCPU: 8, Mem(GB): 122, Storage: 480 SSD, Enhanced Networking: Yes");
            put("f1.16xlarge","vCPU: 64, Mem(GB): 976, Storage: 4 x 960 SSD, Enhanced Networking: Yes");


            put("h1.2xlarge","vCPU: 8, Mem(GB): 32, Storage: 1 x 2,000 HDD, Enhanced Networking: Yes");
            put("h1.4xlarge","vCPU: 16, Mem(GB): 64, Storage: 2 x 2,000 HDD, Enhanced Networking: Yes");
            put("h1.8xlarge","vCPU: 32, Mem(GB): 128, Storage: 4 x 2,000 HDD, Enhanced Networking: Yes");
            put("h1.16xlarge","vCPU: 64, Mem(GB): 256, Storage: 8 x 2,000 HDD, Enhanced Networking: Yes");

            put("i3.large","vCPU: 2, Mem(GB): 15.25, Storage: 1 x 475 NVMe SSD, Enhanced Networking: Yes");
            put("i3.xlarge","vCPU: 4, Mem(GB): 30.5, Storage: 1 x 950 NVMe SSD, Enhanced Networking: Yes");
            put("i3.2xlarge","vCPU: 8, Mem(GB): 61, Storage: 1 x 1,900 NVMe SSD, Enhanced Networking: Yes");
            put("i3.4xlarge","vCPU: 16, Mem(GB): 122, Storage: 2 x 1,900 NVMe SSD, Enhanced Networking: Yes");
            put("i3.8xlarge","vCPU: 32, Mem(GB): 244, Storage: 4 x 1,900 NVMe SSD, Enhanced Networking: Yes");
            put("i3.16xlarge","vCPU: 64, Mem(GB): 488, Storage: 8 x 1,900 NVMe SSD, Enhanced Networking: Yes");

            put("d2.xlarge","vCPU: 4, Mem(GB): 30.5, Storage: 3 x 2000 HDD, Enhanced Networking: Yes");
            put("d2.2xlarge","vCPU: 8, Mem(GB): 61, Storage: 6 x 2000 HDD, Enhanced Networking: Yes");
            put("d2.4xlarge","vCPU: 16, Mem(GB): 122, Storage: 12 x 2000 HDD, Enhanced Networking: Yes");
            put("d2.8xlarge","vCPU: 36, Mem(GB): 244, Storage: 24 x 2000 HDD, Enhanced Networking: Yes");
        }

    };

    public static final HashMap<String, String> supported_os = new HashMap<String, String>() {
        {
            put("5.1","Debian 8,Debian 7,CentOS 7.4,CentOS 7.3,CentOS 7.2,CentOS 7.1,CentOS 6.9,CentOS 6.8,CentOS 6.7," +
                    "Red Hat Enterprise Linux 7.4,Red Hat Enterprise Linux 7.3,Red Hat Enterprise Linux 7.2," +
                    "Red Hat Enterprise Linux 7.1,Red Hat Enterprise Linux 6.9,Red Hat Enterprise Linux 6.8," +
                    "Red Hat Enterprise Linux 6.7,SUSE Enterprise Linux 12,SUSE Enterprise Linux 11,Ubuntu 16.04," +
                    "Ubuntu 14.04,Ubuntu 12.04");

            put("5.0","Debian 8,Debian 7,CentOS 7.4,CentOS 7.3,CentOS 7.2,CentOS 7.1,CentOS 6.9,CentOS 6.8,CentOS 6.7," +
                    "Red Hat Enterprise Linux 7.4,Red Hat Enterprise Linux 7.3,Red Hat Enterprise Linux 7.2," +
                    "Red Hat Enterprise Linux 7.1,Red Hat Enterprise Linux 6.9,Red Hat Enterprise Linux 6.8," +
                    "Red Hat Enterprise Linux 6.7,SUSE Enterprise Linux 12,SUSE Enterprise Linux 11,Ubuntu 16.04," +
                    "Ubuntu 14.04,Ubuntu 12.04");

            put("5.0.1","Debian 8,Debian 7,CentOS 7.4,CentOS 7.3,CentOS 7.2,CentOS 7.1,CentOS 6.9," +
                    "Red Hat Enterprise Linux 7.4,Red Hat Enterprise Linux 7.3,Red Hat Enterprise Linux 7.2," +
                    "Red Hat Enterprise Linux 7.1,Red Hat Enterprise Linux 6.9,Red Hat Enterprise Linux 6.8," +
                    "SUSE Enterprise Linux 12,SUSE Enterprise Linux 11,Ubuntu LTS 16.04," +
                    "Ubuntu 14.04,Ubuntu 12.04");

            put("5.0.0","Debian 8,Debian 7,CentOS 7.4,CentOS 7.3,CentOS 7.2,CentOS 7.1,CentOS 6.9," +
                    "Red Hat Enterprise Linux 7.4,Red Hat Enterprise Linux 7.3,Red Hat Enterprise Linux 7.2," +
                    "Red Hat Enterprise Linux 7.1,Red Hat Enterprise Linux 6.9,Red Hat Enterprise Linux 6.8," +
                    "SUSE Enterprise Linux 12,SUSE Enterprise Linux 11,Ubuntu LTS 16.04," +
                    "Ubuntu 14.04,Ubuntu 12.04");

            put("4.7","Debian 7.1,Debian 6,CentOS 7.3,CentOS 7.2,CentOS 7.1,CentOS 7.0,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.3,Red Hat Enterprise Linux 7.2," +
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.6","Debian 7.1,Debian 6,CentOS 7.3,CentOS 7.2,CentOS 7.1,CentOS 7.0,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.3,Red Hat Enterprise Linux 7.2," +
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");

            put("4.8","Debian 7.1,Debian 6,CentOS 7.3,CentOS 7.2,CentOS 7.1,CentOS 7.0,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.3,Red Hat Enterprise Linux 7.2," +
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");

            put("4.8.0","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.1","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.2","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.3","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.4","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.5","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.6","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.7","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.8","Debian 7.1,Debian 6,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.9","Debian 7.1,Debian 6,CentOS 7.2,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.2,Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.10","Debian 7.1,Debian 6,CentOS 7.2,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.2,Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");
            put("4.8.11","Debian 7.1,Debian 6,CentOS 7.2,CentOS 7.0," +
                    "CentOS 7.1,CentOS 6.7,CentOS 6.5,CentOS 6.4,CentOS 6.3,CentOS 6.2,CentOS 6.1,CentOS 6.0" +
                    "CentOS 5.11,CentOS 5.10,CentOS 5.9,CentOS 5.8," +
                    "Oracle Linux 6,"+
                    "Red Hat Enterprise Linux 7.2,Red Hat Enterprise Linux 7.0,Red Hat Enterprise Linux 6.7,Red Hat Enterprise Linux 6.5," +
                    "Red Hat Enterprise Linux 6.4,Red Hat Enterprise Linux 6.3,Red Hat Enterprise Linux 6.2,Red Hat Enterprise Linux 6.1,Red Hat Enterprise Linux 6.0,"+
                    "Red Hat Enterprise Linux 5.8,Red Hat Enterprise Linux 5.9,Red Hat Enterprise Linux 5.10,Red Hat Enterprise Linux 5.11"+
                    "SUSE Enterprise Linux 11.2,SUSE Enterprise Linux 11.4,Ubuntu 13.04," +
                    "Ubuntu 14.04,Ubuntu 11");




        }
    };

    // dsetool output

    // ntp output

    // os output
}
