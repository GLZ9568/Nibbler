/*
 * Copyright (c)
 *
 * Date: 30/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.NibProperties;
import org.json.*;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Mike Zhang on 30/11/2017.
 */

public class clusterInfoParser {

    private JSONParser jsonParser = new JSONParser();

    private JSONObject cluster_info_obj;
    private JSONObject node_info_obj;
    private JSONObject cpu_obj;
    private JSONObject java_system_properties_obj;
    private JSONObject machine_info_obj;

    public String getClustername()
    {
        return null;
    }
    public JSONObject getCluster_info_obj(FileFactory ff) {
        //FileReader reader = new FileReader(ff.getFiles());


        return cluster_info_obj;
    }

    public JSONObject getNode_info_obj(FileFactory ff) {
        return node_info_obj;
    }

    public JSONObject getCpu_obj() {
        return cpu_obj;
    }

    public JSONObject getJava_system_properties_obj(FileFactory ff) {
        return java_system_properties_obj;
    }

    public JSONObject getMachine_info_obj(FileFactory ff) {
        return machine_info_obj;
    }


}
