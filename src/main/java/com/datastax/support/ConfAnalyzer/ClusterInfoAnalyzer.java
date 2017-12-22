/*
 * Copyright (c)
 *
 * Date: 2/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.ConfAnalyzer;

import com.datastax.support.Parser.ClusterInfoParser;
import com.datastax.support.Parser.ConfFileParser;
import com.datastax.support.Parser.NodetoolStatusParser;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.StrFactory;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mike Zhang on 2/12/2017.
 */

public class ClusterInfoAnalyzer {

    private static final Logger logger = LogManager.getLogger(FileFactory.class);

    public TextArea generateNodeStatusOutput(FileFactory ff)

    {

        TextArea t = new TextArea();

        ClusterInfoParser cip = new ClusterInfoParser(ff);
        JSONObject nodetoolStatusJSON = new JSONObject();

        NodetoolStatusParser nodetoolStatusParser = new NodetoolStatusParser();
        nodetoolStatusParser.parse(ff.getFiles());
        nodetoolStatusJSON = nodetoolStatusParser.getNodetoolStatusJSON();

        String clusterinfotext = new String();

        String clusterinfoheader =  new String();

        String cluster_name_dff_msg_warning = "Multiple Clusternames detected in cassandra.yamls!!! \n";

        String java_version_dff_msg_warning = "Different Java versions in single DC!!! \n";

        String dse_version_dff_msg_warning = "Different DSE versions in single DC!!!! \n";

        ///cluster general info///


        /****
         *
         * 1.1.Cluster name: Total number of nodes(cluster_info.json),  OS version(if supported, cluster_info.json), aws_instance_type(determine storage type,cluster_info.json)
         -> DC name(Workloadtype, number of tokens): number of nodes(nodetool_status), rack_map(cluster_info.json)
         -> each node(node_info.json):
         - ip address, hostname, dse version(cassandra version), "user.timezone" : "America/New_York"(java_system_properties.json),"java.version" : "1.8.0_77" (java_system_properties.json),
         workload type(dsetool_ring), ntp status(ntptime),machine memory(machine-info.json),num_procs, used cpu(cpu.json)

         *
         */

        ConfFileParser cfp = new ConfFileParser();

        cfp.parse(ff.getFiles());

        ArrayList<String> clustername = cfp.getClusterName();

        clusterinfotext +="####Cluster Configuration Overview#### \n";
        ////1. get cluster name
        if(clustername.size()>1)
        {
            clusterinfoheader +=cluster_name_dff_msg_warning;
            String tmp = new String();
            for(int i=0; i< clustername.size();++i)
            {
                 tmp +=clustername.get(i)+",";

            }

            clusterinfotext +="Cluster name: " + tmp +"\n";
        }

        if(clustername.size()==1)
        {

            clusterinfotext +="cluster name: " + clustername.get(0) + "\n";
        }

        ////2. get number of DCs

        clusterinfotext += "number of data centers: " + cip.getCluster_info_obj().get("dc_count")+ "\n";


        ///3. get number of nodes:

        clusterinfotext += "number of nodes: " + cip.getCluster_info_obj().get("node_count")+ "\n";

        ///4. get instance types

        JSONArray instance_types = (JSONArray) cip.getCluster_info_obj().get("cluster_instance_types");

        /// If it is not AWS instances, instance_types will be null
        if (instance_types !=null) {
            clusterinfotext += "instance types: ";
            for (int i = 0; i < instance_types.size(); ++i) {
                if(instance_types.get(i) !=null)
                    clusterinfotext +=  instance_types.get(i).toString() + ", ";
            }
            clusterinfotext += "\n";
        }

        JSONArray cluster_os = (JSONArray) cip.getCluster_info_obj().get("cluster_os");
        clusterinfotext +="cluster OS version: ";
        for (int i=0; i< cluster_os.size();++i)
        {
            clusterinfotext += cluster_os.get(i).toString() + ", ";
        }
        clusterinfotext += "\n";
        clusterinfotext += "\n";

        clusterinfotext += "####Cluster Rack Topology#### \n";

        String[] rack_map_arrary = Inspector.splitByComma(cip.getCluster_info_obj().get("rack_map").toString().replaceAll("[{|}]", ""));
        Arrays.sort(rack_map_arrary);

        for(int i=0; i< rack_map_arrary.length; ++i)
        {
            clusterinfotext +=rack_map_arrary[i]+"\n";
        }

        clusterinfotext += "\n";
        //clusterinfotext += cip.getCluster_info_obj().get("rack_map") + "\n";

       // logger.info(clusterinfotext);

        /**
         *
         * "cluster_instance_types": [
         null,
         "m4.4xlarge"
         ],
         "cluster_os": [
         [
         "linux",
         null,
         "3.10.0-514.16.1.el7.x86_64",
         "amd64"
         ],
         [
         "linux",
         "Red Hat Enterprise Linux Server",
         "6.9",
         "amd64"
         ]
         ],

         "dc_count": 3,
         "node_count": 144,

         "rack_map": {
         "LVDC.RAC1": 13,
         "LVDC.RAC2": 13,
         "LVDC.RAC3": 13,
         "QDC.RAC1": 13,
         "QDC.RAC2": 13,
         "QDC.RAC3": 13,
         "us-west-2.us-west-2a": 22,
         "us-west-2.us-west-2b": 22,
         "us-west-2.us-west-2c": 22
         },
         *
         *
         *
         *
         *
         *
         *
         *
         *
         *
         *
         * */
        ///get each node info////


        /*
        *  -> each node(node_info.json):
        * - ip address, hostname, dse version(cassandra version), "user.timezone" : "America/New_York"(java_system_properties.json)
        *,"java.version" : "1.8.0_77" (java_system_properties.json),workload type(dsetool_ring),
        * ntp status(ntptime),machine memory(machine-info.json),num_procs,
        * used cpu(cpu.json)
        *
        *
        *
        * */


        clusterinfotext += "####Node Configuration Details(group by datacenters)####\n";
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(StrFactory.STATUS);
        //logger.debug("JSONArray Size: " + dcArray.size());
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            //logger.debug("DC: " + tmpdcvar.get(StrFactory.DATACENTER));
            clusterinfotext += ">>>Datacenter: " + tmpdcvar.get(StrFactory.DATACENTER)+ "<<<<\n";

            JSONArray nodesarrary =  (JSONArray)tmpdcvar.get(StrFactory.NODES);
            for(Object node :nodesarrary)
            {
                JSONObject tempnodevar = (JSONObject) node;

                //logger.debug("node: " + tempnodevar.get(StrFactory.ADDRESS));

                String file_id= tempnodevar.get(StrFactory.ADDRESS).toString();

                clusterinfotext += "====== "+tempnodevar.get(StrFactory.ADDRESS).toString()+" ======"+"\n";
                JSONObject node_obj = (JSONObject) cip.getNode_info_obj().get(file_id);

                String[] node_version_array = Inspector.splitByComma(node_obj.get("node_version").toString());
                clusterinfotext += "DSE(Cassandra) version: ";
                for(int i=0; i< node_version_array.length;++i)
                {
                    if(node_version_array[i].contains("dse"))
                      clusterinfotext += node_version_array[i].replaceAll("[{|}]", "")+ ",";
                }
                for(int i=0; i< node_version_array.length;++i)
                {
                    if(node_version_array[i].contains("cassandra"))
                        clusterinfotext += node_version_array[i].replaceAll("[{|}]", "");
                }
                clusterinfotext += "\n";
                clusterinfotext += "hostname: " + node_obj.get("hostname") + "\n";

                clusterinfotext += "number of vCPUs: " + node_obj.get("num_procs") + "\n";

                ///get java version and node timezone

                for(Object java_sys_obj: cip.getJava_system_properties_obj_list())
                {
                    JSONObject java_sys_obj_tmp = (JSONObject) java_sys_obj;

                    if(java_sys_obj_tmp.get("file_id").equals(file_id))
                    {
                        clusterinfotext += "java version: " +java_sys_obj_tmp.get("java.vendor").toString()+ " "
                                +java_sys_obj_tmp.get("java.version").toString() + "\n";

                        clusterinfotext += "timezone: " +java_sys_obj_tmp.get("user.timezone").toString()+ "\n";
                    }

                }

                ///get machine memory

                for(Object sysmem_obj: cip.getMachine_info_obj_list())
                {
                    JSONObject sysmem_obj_tmp = (JSONObject) sysmem_obj;

                    if(sysmem_obj_tmp.get("file_id").equals(file_id))
                    {
                        clusterinfotext += "machine memory: " +sysmem_obj_tmp.get("memory").toString()+ "mb" + "\n";

                    }

                }

                ///get ntp status////

                for(Object ntp_obj: cip.getNtptime_list())
                {
                    JSONObject ntp_obj_tmp = (JSONObject) ntp_obj;

                    if(ntp_obj_tmp.get("file_id").equals(file_id))
                    {
                        if(ntp_obj_tmp.get(StrFactory.NTPTIME_STAUS)!=null) {
                            if (ntp_obj_tmp.get(StrFactory.NTPTIME_STAUS).toString().contains("down"))
                                clusterinfotext += "NTP status: " + ntp_obj_tmp.get(StrFactory.NTPTIME_STAUS).toString() + "\n";
                        }
                        if(ntp_obj_tmp.get(StrFactory.NTPTIME_STAUS)!=null) {
                            if (ntp_obj_tmp.get(StrFactory.NTPTIME_STAUS).toString().contains("running"))
                                if(ntp_obj_tmp.get(StrFactory.NTPTIME_OFFSET)!=null) {
                                    clusterinfotext += "NTP status: " + ntp_obj_tmp.get(StrFactory.NTPTIME_STAUS).toString() +
                                            ntp_obj_tmp.get(StrFactory.NTPTIME_OFFSET).toString() + "\n";
                                }
                        }
                    }

                }

                ////get cpu info////

                for(Object cpu_obj: cip.getCpu_obj_list())
                {
                    JSONObject cpu_obj_tmp = (JSONObject) cpu_obj;

                    if(cpu_obj_tmp.get("file_id").equals(file_id))
                    {
                       clusterinfotext += "cpu usage: " + "user: " + cpu_obj_tmp.get("%user")+ ", "
                               + "system: " + cpu_obj_tmp.get("%system")+", " + "iowait: "+
                               cpu_obj_tmp.get("%iowait")+", "+ "idle: " + cpu_obj_tmp.get("%idle") + "\n";
                    }

                }

                clusterinfotext +="\n";

            }

        }


        /****
         *
         *  - DC: DC1
         *  - node: 172.29.134.21
         *  - node: 172.29.135.21
         *  - node: 172.29.135.20
         *  - node: 172.29.134.20
         *  - node: 172.29.134.151
         *
         *
         *  - DC: DC2
         *  - node: 172.29.167.21
         *  - node: 172.29.166.21
         *  - node: 172.29.167.20
         *  - node: 172.29.166.20
         *  - node: 172.29.166.151
         *
         *
         *  - DC: DC3
         *  - node: 172.29.10.13
         *  - node: 172.29.10.12
         *  - node: 172.29.10.14
         *  - node: 172.29.10.11
         *
         */
            //logger.debug(cip.getCluster_info_obj().toString());
            //logger.debug(cip.getNode_info_obj().toString());
            // logger.debug(cip.getCpu_obj_list().toString());
            //logger.debug(cip.getJava_system_properties_obj_list().toString());
            //logger.debug(cip.getMachine_info_obj_list().toString());
            //logger.debug(cip.getNtptime_list().toString());


/*
         for (int i =0; i < cip.getCpu_obj_list().size(); ++i)
            {
                JSONObject node = (JSONObject) cip.getNode_info_obj().get(cip.getCpu_obj_list().get(i).get("file_id"));

                clusterinfotext = "====== "+cip.getCpu_obj_list().get(i).get("file_id")+" ======"+"\n";
                clusterinfotext += "";
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

        t.setText(clusterinfotext);
        t.setPrefWidth(1024);
        t.setMinHeight(450);
        return t;

    }
}
