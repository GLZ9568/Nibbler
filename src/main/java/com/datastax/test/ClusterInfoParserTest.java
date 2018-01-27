/*
 * Copyright (c)
 *
 * Date: 2/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Analyzer.ClusterInfoAnalyzer;
import com.datastax.support.Util.FileFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Mike Zhang on 2/12/2017.
 */

public class ClusterInfoParserTest {

    private static final Logger logger = LogManager.getLogger(FileFactory.class);
    public static void main (String[] args) {

        FileFactory ff = new FileFactory(new File("/Users/tongjixianing/Downloads/order_prod_cluster-diagnostics-2017_11_24_23_44_42_UTC"));
        //boolean b = ff.initiate(new File("/Users/tongjixianing/Downloads/order_prod_cluster-diagnostics-2017_11_24_23_44_42_UTC"));
        boolean b = ff.getInitiateSuccessCheck();

        if (b)
        {
            ClusterInfoAnalyzer cia =  new ClusterInfoAnalyzer(ff);

            cia.generateClusterInfoOutput();

            /*
            ClusterInfoParser cip = new ClusterInfoParser(ff);
            //logger.debug(cip.getCluster_info_obj().toString());
            //logger.debug(cip.getNode_info_obj().toString());
           // logger.debug(cip.getCpu_obj_list().toString());
            //logger.debug(cip.getJava_system_properties_obj_list().toString());
            //logger.debug(cip.getMachine_info_obj_list().toString());
            //logger.debug(cip.getNtptime_list().toString());
            for (int i =0; i < cip.getCpu_obj_list().size(); ++i)
            {
                JSONObject node = (JSONObject) cip.getNode_info_obj().get(cip.getCpu_obj_list().get(i).get("file_id"));

                logger.debug(cip.getCpu_obj_list().get(i).get("file_id")+":"
                        +cip.getCpu_obj_list().get(i).get("%idle") + ", "+
                        cip.getMachine_info_obj_list().get(i).get("file_id")+":"+ cip.getMachine_info_obj_list().get(i).get("memory") + ", "
                +cip.getJava_system_properties_obj_list().get(i).get("file_id")+":"
                        +cip.getJava_system_properties_obj_list().get(i).get("user.timezone")+ ", " +
                        cip.getJava_system_properties_obj_list().get(i).get("java.runtime.version") +", " + node.get("hostname")
                                + ", " + node.get("node_ip")

                        );


            }
            */
        }
    }
}
