/*
 * Copyright (c)
 *
 * Date: 26/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Util;

/**
 * Created by Chun Gao on 26/11/2017
 */

public class StrFactory {

    // common
    public static final String none_exist = "none_exist";
    public static final String node = "node";
    public static final String file_id = "file_id";
    public static final String file_name= "file_name";
    public static final String file_path = "file_path";
    public static final String opscenterd = "opscenterd";

    // conf file names
    public static final String cassandra_yaml = "cassandra.yaml";
    public static final String address_yaml = "address.yaml";
    public static final String dse_yaml = "dse.yaml";
    public static final String conf_surffix = ".conf";

    // cassandra.yaml configurations
    public static final String seeds = "seeds";
    public static final String cluster_name = "cluster_name";
    public static final String num_tokens = "num_tokens";

    // agentaddress.yaml configurations

    // nodetool output
    public static final String nodetool = "nodetool";
    public static final String cfstats = "ctstats";
    public static final String compactionhistory = "compactionhistory";
    public static final String compactionstats = "compactionstats";
    public static final String describecluster = "describecluster";
    public static final String getcompactionthroughput = "getcompactionthroughput";
    public static final String getstreamthroughput = "getstreamthroughput";
    public static final String gossipinfo = "gossipinfo";
    public static final String info = "info";
    public static final String netstats = "netstats";
    public static final String proxyhistograms = "proxyhistograms";
    public static final String ring = "ring";
    public static final String status = "status";
    public static final String statusbinary = "statusbinary";
    public static final String statusthrift = "statusthrift";
    public static final String tpstats = "tpstats";
    public static final String version = "version";

    public static final String datacenter = "Datacenter";

    // dsetool output

    // ntp output

    // os output
}
