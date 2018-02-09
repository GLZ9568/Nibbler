/*
 * Copyright (c)
 *
 * Date: 4/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Parser.ClusterInfoParser;
import com.datastax.support.Parser.ConfFileParser;
import com.datastax.support.Parser.DiskSpaceParser;
import com.datastax.support.Parser.NodetoolStatusFileParser;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by Mike Zhang on 2/12/2017.
 */

public class ClusterInfoAnalyzer extends Analyzer {

    private static final Logger logger = LogManager.getLogger(ClusterInfoAnalyzer.class);
    private boolean is_mul_cluster_name = false;
    private boolean is_mul_snitch_name = false;
    private boolean is_mul_seeds_list = false;
    private boolean is_ntp_down = false;
    private boolean is_diff_java_version = false;
    boolean is_diff_dse_version = false;
    boolean is_unsupported_os = false;
    boolean is_commitlog_dir_same_with_datadir = false;
    private JSONObject cluster_info_obj;
    private JSONObject node_info_obj;
    private ArrayList<JSONObject> cpu_obj_list;
    private ArrayList<JSONObject> java_system_properties_obj_list;
    private ArrayList<JSONObject> machine_info_obj_list;
    private ArrayList<JSONObject> ntptime_obj_list;
    private ArrayList<JSONObject> ntpstat_obj_list;
    private ArrayList<JSONObject> os_info_obj_list;
    private ArrayList<JSONObject> disk_space_obj_list;
    private ArrayList<String> clustername;
    private ArrayList<String> snitch;
    private JSONObject nodetoolStatusJSON;
    private Set<String> ntp_downnode_set = new HashSet<String>();

    public ClusterInfoAnalyzer(FileFactory fileFactory) {
        super(fileFactory);
        this.cluster_info_obj = fileFactory.getCluster_info_obj();
        this.node_info_obj = fileFactory.getNode_info_obj();
        this.cpu_obj_list = fileFactory.getCpu_obj_list();
        this.java_system_properties_obj_list = fileFactory.getJava_system_properties_obj_list();
        this.machine_info_obj_list = fileFactory.getMachine_info_obj_list();
        this.ntptime_obj_list = fileFactory.getNtptime_obj_list();
        this.os_info_obj_list = fileFactory.getOs_info_obj_list();
        this.disk_space_obj_list = fileFactory.getDisk_space_obj_list();
        this.clustername = fileFactory.getClusterName();
        this.snitch = fileFactory.getSnitch_list();
        this.nodetoolStatusJSON = fileFactory.getNodetoolStatusJSON();
        this.ntpstat_obj_list = fileFactory.getNtpstat_obj_list();
    }

    public String generateClusterInfoOutput() {

        //TextArea t = new TextArea();
        // TextFlow flow = new TextFlow();
        //ClusterInfoParser cip = new ClusterInfoParser(ff);
        // DiskSpaceParser dsp = new DiskSpaceParser();
        // dsp.parse(ff.getAllFiles());
        //JSONObject nodetoolStatusJSON = new JSONObject();

        // NodetoolStatusFileParser nodetoolStatusFileParser = new NodetoolStatusFileParser(ff.getAllFiles());
        //  nodetoolStatusJSON = nodetoolStatusFileParser.getNodetoolStatusJSON();

        String clusterinfotext = new String();

        String clusterinfo_warning_header = new String("#### WARNING: ####\n");

        String cluster_name_dff_msg_warning = "Multiple Clusternames detected in cassandra.yamls!!! \n";
        String snitch_name_dff_msg_warning = "Multiple Snitch types detected in cassandra.yamls!!! \n";
        String seeds_list_dff_msg_warning = "Multiple seeds lists detected in cassandra.yamls!!! \n";
        String java_version_dff_msg_warning = "Different Java versions in DC: ";
        String dse_version_dff_msg_warning = "Different DSE versions in DC:  ";
        String ntp_down_msg_warning = "NTP is down on nodes: \n";

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

        //ConfFileParser cfp = new ConfFileParser(ff.getAllFiles());

        // ArrayList<String> clustername = cfp.getClusterName();
        // ArrayList<String> snitch = cfp.getSnitch_list();
        //Set<String> seeds = cfp.getSeeds_list();

        clusterinfotext += "** Cluster Configuration Overview **\n" +
                Inspector.generateEqualline(new String("** Cluster Configuration Overview **").length()) + "\n";
        ////1. get cluster name
        if (clustername.size() > 1) {
            clusterinfo_warning_header += cluster_name_dff_msg_warning;
            is_mul_cluster_name = true;
            String tmp = new String();
            for (int i = 0; i < clustername.size(); ++i) {
                tmp += clustername.get(i) + ",";

            }

            clusterinfotext += "Cluster name: " + tmp + "\n";
        }

        if (clustername.size() == 1) {

            clusterinfotext += "Cluster Name: " + clustername.get(0) + "\n";
        }

        ////2. get number of DCs
        if (cluster_info_obj.get(ValFactory.ISCLUSTER_INFOEXIST).equals("true")) {
            clusterinfotext += "Number of Data Centers: " + cluster_info_obj.get("dc_count") + "\n";


            ///3. get number of nodes: ////

            clusterinfotext += "Number of Nodes: " + cluster_info_obj.get("node_count") + "\n";
        }
        /// get snitch ////

        if (snitch.size() > 1) {
            clusterinfo_warning_header += snitch_name_dff_msg_warning;
            is_mul_snitch_name = true;
            String tmp = new String();
            for (int i = 0; i < snitch.size(); ++i) {
                tmp += snitch.get(i) + ",";

            }

            clusterinfotext += "Snitch: " + tmp + "\n";
        }

        if (snitch.size() == 1) {

            clusterinfotext += "Snitch: " + snitch.get(0) + "\n";
        }

        if (cluster_info_obj.get(ValFactory.ISCLUSTER_INFOEXIST).equals("true")) {
            ///4. get instance types

            JSONArray instance_types = (JSONArray) cluster_info_obj.get("cluster_instance_types");

            /// If it is not AWS instances, instance_types will be null
            if (instance_types != null) {
                clusterinfotext += "Instance Types: \n";
                for (int i = 0; i < instance_types.size(); ++i) {
                    if (instance_types.get(i) != null) {
                        clusterinfotext += "  - " + instance_types.get(i).toString()
                                + "(" + ValFactory.aws_instance.get(instance_types.get(i).toString()) + ")" + "\n";
                        clusterinfotext += "     " + get_aws_dc(/*cip,*/nodetoolStatusJSON, instance_types.get(i).toString()) + "\n";
                    } else {
                        clusterinfotext += "  - Not AWS instance \n";
                        clusterinfotext += "     " + get_non_aws_dc(/*cip,*/nodetoolStatusJSON) + "\n";
                    }
                }
            }

            JSONArray cluster_os = (JSONArray) cluster_info_obj.get("cluster_os");
            clusterinfotext += "Cluster OS Version: \n";

            for (int i = 0; i < cluster_os.size(); ++i) {
                String[] cluster_os_str_split = Inspector.splitByComma(cluster_os.get(i).toString());
                if(!cluster_os_str_split[0].contains("null"))
                clusterinfotext += "  - " + cluster_os.get(i).toString() + "\n";
            }
            clusterinfotext += "\n";

            clusterinfotext += "** Cluster Rack Topology ** \n" +
                    Inspector.generateEqualline(new String("** Cluster Rack Topology **").length()) + "\n";

            String[] rack_map_arrary = Inspector.splitByComma(cluster_info_obj.get("rack_map").toString().replaceAll("[{|}]", ""));
            Arrays.sort(rack_map_arrary);

            for (int i = 0; i < rack_map_arrary.length; ++i) {
                clusterinfotext += rack_map_arrary[i] + "\n";
            }
            clusterinfotext += "\n";
        } else {
            clusterinfotext += "\n";
        }



        /*
        *   "linux",
            null,
            "NAME=\"Red",
            "amd64"

            "linux",
            "CentOS Linux",
            "7.2.1511",
            "amd64"

            "linux",
            null,
            "3.10.0-514.16.1.el7.x86_64",
            "amd64"

            "linux",
            "Red Hat Enterprise Linux Server",
            "6.9",
            "amd64"

        *
        * */


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


        clusterinfotext += "** Node Configuration Details(group by datacenters) **\n" +
                Inspector.generateEqualline(new String("** Node Configuration Details(group by datacenters) **").length()) + "\n";
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        //logger.debug("JSONArray Size: " + dcArray.size());
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            //logger.debug("DC: " + tmpdcvar.get(StrFactory.DATACENTER));
            String dotlinestr = Inspector.generateDotline(19 + tmpdcvar.get(ValFactory.DATACENTER).toString().length()) + "\n";
            clusterinfotext += dotlinestr +
                    ">>>Datacenter: " + tmpdcvar.get(ValFactory.DATACENTER) + "<<<<\n" + dotlinestr;

            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);

            Set<String> dse_version_set = new HashSet<String>();
            Set<String> java_version_set = new HashSet<String>();
            Set<String> un_supported_os_msg_set = new HashSet<String>();
            Set<String> commitlog_data_dir_set = new HashSet<String>();

            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;

                //logger.debug("node: " + tempnodevar.get(StrFactory.ADDRESS));

                String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();

                clusterinfotext += "====== " + tempnodevar.get(ValFactory.ADDRESS).toString() + " ======" + "\n";
                JSONObject node_obj = null;

                if (node_info_obj.get(ValFactory.ISNODE_INFOEXIST).equals("true"))
                    node_obj = (JSONObject) node_info_obj.get(file_id);

                if (node_obj != null) {

                    //// get dse version///

                    String[] node_version_array = Inspector.splitByComma(node_obj.get("node_version").toString());
                    String dse_version = new String();
                    clusterinfotext += "DSE(Cassandra) Version: ";
                    for (int i = 0; i < node_version_array.length; ++i) {
                        if (node_version_array[i].contains("dse")) {
                            clusterinfotext += node_version_array[i].replaceAll("[{|}]", "") + ",";

                            /// add and filter duplicate dse versions
                            dse_version_set.add(node_version_array[i].replaceAll("[{|}]", ""));

                            ////assign dse_version for later supported OS version check

                            String dse_version_tmp = node_version_array[i].replaceAll("[{|}]", "");
                            String[] split_str = Inspector.splitByColon(dse_version_tmp);
                            dse_version = split_str[1].replaceAll("\"", "");

                        }
                    }
                    for (int i = 0; i < node_version_array.length; ++i) {
                        if (node_version_array[i].contains("cassandra"))
                            clusterinfotext += node_version_array[i].replaceAll("[{|}]", "");
                    }
                    clusterinfotext += "\n";

                    ///get hostname
                    clusterinfotext += "Hostname: " + node_obj.get("hostname") + "\n";

                    /// get AWS instance type:

                    JSONObject ec2_obj = (JSONObject) node_obj.get("ec2");
                    if (ec2_obj.get("instance-type") != null) {
                        clusterinfotext += "AWS Instance Type: " + ec2_obj.get("instance-type")
                                .toString().trim().replaceAll("\"", "").
                                        replaceAll(",", "") + "\n";

                    } else {
                        clusterinfotext += "AWS Instance Type: " + "Non AWS Instance" + "\n";

                    }

                    //get vCPU
                    clusterinfotext += "Number of vCPUs: " + node_obj.get("num_procs") + "\n";


                    ///get machine memory

                    for (Object sysmem_obj : machine_info_obj_list) {
                        JSONObject sysmem_obj_tmp = (JSONObject) sysmem_obj;

                        if (sysmem_obj_tmp.get("file_id").equals(file_id)) {
                            clusterinfotext += "Machine Memory: " + sysmem_obj_tmp.get("memory").toString() + "mb" + "\n";

                        }

                    }

                    /////get and check OS version if supported////

                    for (Object os_info_obj : os_info_obj_list) {
                    /*
                      "sub_os" : "CentOS Linux",
                      "os_version" : "7.2.1511"
                    * */
                        JSONObject os_info_obj_tmp = (JSONObject) os_info_obj;
                        if (os_info_obj_tmp.get("file_id").equals(file_id)) {
                            if (os_info_obj_tmp.get("sub_os") != null) {
                                String os_name = os_info_obj_tmp.get("sub_os").toString().replaceAll("\"", "");
                                String os_version = os_info_obj_tmp.get("os_version").toString().replaceAll("\"", "");
                        /*
                        String dse_subversion = dse_version.substring(0,2);
                        String supported_os_str = StrFactory.supported_os.get(dse_subversion);
                        String[] supported_os_array = Inspector.
                                splitByComma(StrFactory.supported_os.get(dse_subversion).toLowerCase());
                        */
                                // clusterinfotext += "OS version: " + os_name + " "+ os_version + "\n";
                                if (dse_version != null && dse_version.length() != 0) {
                                    String os_check_msg = check_os(os_name, os_version, dse_version, file_id);

                                    if (os_check_msg.length() != 0) {
                                        String unsupported_msg_str =
                                                "Unsupported OS " + "in DC: " +
                                                        tmpdcvar.get(ValFactory.DATACENTER).toString() + "("
                                                        + os_name + " " + os_version + " for DSE " + dse_version + ")" + "!!"
                                                        + " Please check: "
                                                        + ValFactory.supported_platform_url + "\n";
                                        clusterinfotext += "OS Version: " + os_name + " " + os_version +
                                                "(unsupported os: " + os_name + " " + os_version + " for DSE " + dse_version + ")" + "\n";
                                        un_supported_os_msg_set.add(unsupported_msg_str);
                                    } else {
                                        clusterinfotext += "OS Version: " + os_name + " " + os_version + "\n";
                                    }
                                } else {
                                    os_version = os_info_obj_tmp.get("os_version").toString().replaceAll("\"", "");
                                    clusterinfotext += "OS Version: " + os_version + "\n";
                                }
                            }

                        }
                    }
                } else {

                    for (Object sysmem_obj : machine_info_obj_list) {
                        JSONObject sysmem_obj_tmp = (JSONObject) sysmem_obj;

                        if (sysmem_obj_tmp.get("file_id").equals(file_id)) {
                            clusterinfotext += "Machine Memory: " + sysmem_obj_tmp.get("memory").toString() + "mb" + "\n";

                        }

                    }
                }
                ///get java version and node timezone

                for (Object java_sys_obj : java_system_properties_obj_list) {
                    JSONObject java_sys_obj_tmp = (JSONObject) java_sys_obj;

                    if (java_sys_obj_tmp.get("file_id").equals(file_id)) {

                        if (java_sys_obj_tmp.get(ValFactory.ISJAVA_SYSTEM_PROPERTIESEXIST).toString().equals("true")) {
                            clusterinfotext += "Java Version: " + java_sys_obj_tmp.get("java.vendor").toString() + " "
                                    + java_sys_obj_tmp.get("java.version").toString() + "\n";

                            java_version_set.add(java_sys_obj_tmp.get("java.vendor").toString() + " "
                                    + java_sys_obj_tmp.get("java.version").toString());

                            TimeZone timeZone = TimeZone.getTimeZone(java_sys_obj_tmp.get("user.timezone").toString());

                            int timezone_offset = timeZone.getOffset(new Date().getTime()) / 1000 / 60 / 60;


                            if (timezone_offset >= 0)
                                clusterinfotext += "Timezone: " + java_sys_obj_tmp.get("user.timezone").toString() + "(GMT+" +
                                        Integer.valueOf(timeZone.getOffset(new Date().getTime()) / 1000 / 60 / 60) + ")" + "\n";
                            else
                                clusterinfotext += "Timezone: " + java_sys_obj_tmp.get("user.timezone").toString() + "(GMT" +
                                        Integer.valueOf(timeZone.getOffset(new Date().getTime()) / 1000 / 60 / 60) + ")" + "\n";

                        }
                    }

                }


                ///get ntp status////

                /*
                for(Object ntp_obj: ntptime_obj_list)
                {
                    JSONObject ntp_obj_tmp = (JSONObject) ntp_obj;

                    if(ntp_obj_tmp.get("file_id").equals(file_id))
                    {
                        if(ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS)!=null) {
                            if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString().contains("down"))
                                clusterinfotext += "NTP Status: " + ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString() + "\n";
                        }
                        if(ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS)!=null) {
                            if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString().contains("running"))
                                if(ntp_obj_tmp.get(ValFactory.NTPTIME_OFFSET)!=null) {
                                    clusterinfotext += "NTP Status: " + ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString() +
                                            ntp_obj_tmp.get(ValFactory.NTPTIME_OFFSET).toString() + "\n";
                                }
                        }
                        if(ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS)!=null)
                        {
                            if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString().toLowerCase().contains("exception"))
                            {
                                clusterinfotext += "NTP Status: " + ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString() + "\n";
                            }
                        }
                    }
                    */

                for (Object ntpstat_obj : ntpstat_obj_list) {
                    JSONObject ntp_obj_tmp = (JSONObject) ntpstat_obj;

                    if (ntp_obj_tmp.get("file_id").equals(file_id)) {
                        if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS) != null) {
                            if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString().contains("down")) {
                                clusterinfotext += "NTP Status: " + ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString() + "\n";
                                ntp_downnode_set.add(file_id);
                                is_ntp_down = true;
                            }

                        }
                        if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS) != null) {
                            if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString().contains("running"))
                                if (ntp_obj_tmp.get(ValFactory.NTPTIME_OFFSET) != null) {
                                    clusterinfotext += "NTP Status: " + ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString() +
                                            ", time to correct: " +
                                            ntp_obj_tmp.get(ValFactory.NTPTIME_OFFSET).toString() + "\n";
                                }
                        }
                        if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS) != null) {
                            if (ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString().toLowerCase().contains("exception")) {
                                clusterinfotext += "NTP Status: " + ntp_obj_tmp.get(ValFactory.NTPTIME_STAUS).toString() + "\n";
                            }
                        }

                    }
                }

                ////get cpu info////

                for (Object cpu_obj : cpu_obj_list) {
                    JSONObject cpu_obj_tmp = (JSONObject) cpu_obj;

                    if (cpu_obj_tmp.get("file_id").equals(file_id)) {
                        clusterinfotext += "CPU Usage: " + "user: " +
                                String.format("%.2f", Double.parseDouble(cpu_obj_tmp.get("%user").toString()))
                                + ", "
                                + "system: " + String.format("%.2f", Double.parseDouble(cpu_obj_tmp.get("%system").toString()))

                                + ", " + "iowait: " +
                                String.format("%.2f", Double.parseDouble(cpu_obj_tmp.get("%iowait").toString()))

                                + ", " + "steal: " +
                                String.format("%.2f", Double.parseDouble(cpu_obj_tmp.get("%steal").toString()))

                                + ", " + "nice: " +
                                String.format("%.2f", Double.parseDouble(cpu_obj_tmp.get("%nice").toString()))

                                + ", " + "idle: " +
                                String.format("%.2f", Double.parseDouble(cpu_obj_tmp.get("%idle").toString())) + "\n";
                    }

                }


                /////get disk info ////
                if (node_obj != null) {
                    clusterinfotext += "Storage Configuration:\n";
                    String commitlog_dir = new String();
                    for (Object disk_obj_tmp : disk_space_obj_list) {
                        JSONObject disk_obj = (JSONObject) disk_obj_tmp;
                        if (disk_obj.get(ValFactory.FILE_ID).equals(file_id)) {
                            JSONArray disk_list = (JSONArray) disk_obj.get(ValFactory.DISK_SPACE_USAGE);
                            JSONObject node_info_obj_byip = (JSONObject) node_info_obj.get(file_id);
                            if (node_info_obj_byip != null) {
                                JSONObject partitions_info = (JSONObject) node_info_obj_byip.get("partitions");
                                if (partitions_info.get("commitlog") != null) {
                                    commitlog_dir = partitions_info.get("commitlog").toString().trim().
                                            replaceAll("[{|}]", "").replaceAll("\"", "")
                                            .replaceAll(",", "");

//                                logger.info("get commitlog dir from node_info is: " + commitlog_dir);

                                    ///get commitlog disk device////

                                    for (Object each_disk_tmp : disk_list) {
                                        JSONObject each_disk_obj = (JSONObject) each_disk_tmp;
                                        if (each_disk_obj.get(ValFactory.DISK_NAME).toString().equals(commitlog_dir)) {
                                            clusterinfotext += " - commitlog disk device: \n"
                                                    + "   - " + commitlog_dir
                                                    + " (total space(GB): " + each_disk_obj.get(ValFactory.TOTAL_SPACE).toString()
                                                    + ", used space(GB): " + each_disk_obj.get(ValFactory.USED_SPACE).toString() + ")\n";
                                        }
                                    }
                                }
                                ////get data disk devices///


                                JSONArray data_dirs = (JSONArray) partitions_info.get("data");
                                if (data_dirs != null) {
                                    clusterinfotext += " - data disk devices: \n";
                                    for (Object each_disk_tmp : disk_list) {
                                        JSONObject each_disk_obj = (JSONObject) each_disk_tmp;

                                        for (Object each_data_dir_tmp : data_dirs) {

                                            String each_data_dir = each_data_dir_tmp.toString();
                                            if (each_data_dir.trim().replaceAll("[{|}]", "")
                                                    .replaceAll("\"", "")
                                                    .replaceAll(",", "").equals(each_disk_obj.get(ValFactory.DISK_NAME))) {
                                                clusterinfotext += "   - " + each_disk_obj.get(ValFactory.DISK_NAME)
                                                        + " (total space(GB): " + each_disk_obj.get(ValFactory.TOTAL_SPACE).toString()
                                                        + ", used space(GB): " + each_disk_obj.get(ValFactory.USED_SPACE).toString() + ")\n";

                                                //check if commitlog dir is the same with data dir
                                                if (commitlog_dir.equals(each_disk_obj.get(ValFactory.DISK_NAME).toString())) {
                                                    commitlog_data_dir_set.add(commitlog_dir);
                                                    int start = clusterinfotext.lastIndexOf("Storage Configuration:\n");
                                                    String clusterinfotext_before_start = clusterinfotext.substring(0, start);
                                                    clusterinfotext_before_start += "Storage Configuration(!!!WARNING!!! Commitlog " +
                                                            "and data directory are on the same disk device - " + commitlog_dir + "):\n";
                                                    String clusterinfotext_after_start = clusterinfotext.
                                                            substring(start + new String("Storage Configuration:\n").length());
                                                    clusterinfotext = clusterinfotext_before_start + clusterinfotext_after_start;

                                                }
                                            }

                                        }
                                    }
                                }

                            }
                        }
                    }
                }

                /// add line afer each node info ///

                clusterinfotext += "\n";
            }

            ///add line after each DC info ///
            clusterinfotext += "\n";


            if (commitlog_data_dir_set.size() > 0) {
                clusterinfo_warning_header += "Commitlog directory is on the same disk device with data directory in DC: " +
                        tmpdcvar.get(ValFactory.DATACENTER).toString() +
                        "(Device name is: " + commitlog_data_dir_set.toArray()[0].toString() + ")" + "!!\n";
                is_commitlog_dir_same_with_datadir = true;
            }

            if (dse_version_set.size() > 1) {
                String dse_version_str = new String();
                for (String str : dse_version_set) {
                    dse_version_str += str + ",";
                }
                clusterinfo_warning_header += dse_version_dff_msg_warning +
                        tmpdcvar.get(ValFactory.DATACENTER).toString() + "(" + dse_version_str.substring(0, dse_version_str.length() - 1) + ")" + "!!\n";
                is_diff_dse_version = true;
            }

            if (java_version_set.size() > 1) {
                String java_version_str = new String();
                for (String str : java_version_set) {
                    java_version_str += str + ",";
                }
                clusterinfo_warning_header += java_version_dff_msg_warning +
                        tmpdcvar.get(ValFactory.DATACENTER).toString() + "(" + java_version_str.substring(0, java_version_str.length() - 1) + ")" +
                        "!!\n";
                is_diff_java_version = true;
            }

            if (un_supported_os_msg_set.size() > 0) {
                logger.info("un_supported_os_msg_set is not empty!!!");
                for (String str : un_supported_os_msg_set) {
                    clusterinfo_warning_header += str;
                }
                is_unsupported_os = true;
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

        if (is_diff_java_version || is_diff_dse_version || is_mul_cluster_name || is_unsupported_os || is_commitlog_dir_same_with_datadir || is_ntp_down) {
            //String ntp_down_msg_warning = "NTP is down on nodes: \n";
            if (ntp_downnode_set.size() > 0) {
                ntp_down_msg_warning += "  - [";
                for (String str : ntp_downnode_set) {

                    ntp_down_msg_warning += str + ",";
                }

                String tmp = ntp_down_msg_warning.substring(0, ntp_down_msg_warning.length() - 1) + "]\n";

                clusterinfo_warning_header += tmp + "\n";


            } else
                clusterinfo_warning_header += "\n";
            //t.setText(clusterinfo_warning_header);

            // t.setStyle("-fx-font-size: 11pt; -fx-font-family:monospace");
            //  t.setText(clusterinfo_warning_header+clusterinfotext);
            //  Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            //  double screen_height =  visualBounds.getHeight() ;
            //   double screen_width = visualBounds.getWidth();
            //t.setMinHeight(screen_height * 3*ValFactory.SCREEN_HEIGHT_FACTOR/5);
            //   t.setMinHeight(Inspector.getDynamicTextAreaHeight(clusterinfo_warning_header+clusterinfotext));
            //    t.setEditable(false);
            // t.setPrefWidth(1024);

            //t.setScrollTop(0);
            /// flow.setLineSpacing(0);

            //flow.getChildren().addAll(t1,t2);
            return clusterinfo_warning_header + clusterinfotext;
        } else {
            //  t.setStyle("-fx-font-size: 11pt; -fx-font-family:monospace");
            //    t.setText(clusterinfotext);
            //    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            //     double screen_height =  visualBounds.getHeight() ;
            //     double screen_width = visualBounds.getWidth();
            // t.setMinHeight(screen_height * 3*ValFactory.SCREEN_HEIGHT_FACTOR/5);
            //      t.setMinHeight(Inspector.getDynamicTextAreaHeight(clusterinfotext));
            //      t.setEditable(false);
            // t.setPrefWidth(1024);
            // t.setMinHeight(450);
            //flow.getChildren().addAll(t);

            return clusterinfotext;
        }
        // return t;

    }

    public String check_os(String os_name, String os_version, String dse_version, String file_id) {

        String clusterinfo_warning_header = new String("");
        String dse_subversion = new String();
        try {
            dse_subversion = dse_version.substring(0, 3);
        } catch (StringIndexOutOfBoundsException e) {
            logger.info("!!!dse_subversion exception!!! dse version is: " + dse_version + "ip is: " + file_id);
        }

        //String supported_os_str = StrFactory.supported_os.get(dse_subversion);

        if (dse_version.equals("5.0.0") ||
                dse_version.equals("5.0.1") || dse_version.equals("4.8.0") || dse_version.equals("4.8.1")
                || dse_version.equals("4.8.2") || dse_version.equals("4.8.3") || dse_version.equals("4.8.4")
                || dse_version.equals("4.8.5") || dse_version.equals("4.8.6") || dse_version.equals("4.8.7")
                || dse_version.equals("4.8.8") || dse_version.equals("4.8.9") || dse_version.equals("4.8.10")
                || dse_version.equals("4.8.11")) {

            String[] supported_os_array = Inspector.
                    splitByComma(ValFactory.supported_os.get(dse_version).toLowerCase());
            if (os_name != null) {
                if (os_name.toLowerCase().contains("centos")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("centos")) {
                            String version = Inspector.splitBySpace(str)[1];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("red hat")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("red hat")) {
                            String version = Inspector.splitBySpace(str)[4];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("ubuntu")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("ubuntu")) {
                            String version = Inspector.splitBySpace(str)[1];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("suse")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("suse")) {
                            String version = Inspector.splitBySpace(str)[3];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("debian")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("debian")) {
                            String version = Inspector.splitBySpace(str)[1];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else {
                    clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                            ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                }


            }
        } else //if(dse_subversion.equals("5.1"))
        {

//            logger.info("dse version is: " +  dse_version + " dse sub version is: " + dse_subversion);
            String[] supported_os_array = Inspector.
                    splitByComma(ValFactory.supported_os.get(dse_subversion).toLowerCase());

            if (os_name != null) {
                if (os_name.toLowerCase().contains("centos")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("centos")) {
                            String version = Inspector.splitBySpace(str)[1];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("oracle linux")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("oracle linux")) {
                            String version = Inspector.splitBySpace(str)[2];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("red hat")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("red hat")) {
                            String version = Inspector.splitBySpace(str)[4];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("ubuntu")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("ubuntu")) {
                            String version = Inspector.splitBySpace(str)[1];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("suse")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("suse")) {
                            String version = Inspector.splitBySpace(str)[3];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else if (os_name.toLowerCase().contains("debian")) {
                    int check = 0;
                    for (String str : supported_os_array) {
                        if (str.contains("debian")) {
                            String version = Inspector.splitBySpace(str)[1];
                            if (os_version.contains(version)) {
                                check = check + 1;
                            }
                        }
                    }
                    if (check == 0) {
                        clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                                ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                    }
                } else {
                    clusterinfo_warning_header += "Unsupported OS Version for DSE " + dse_version + " " +
                            ": " + os_name + " " + os_version + " on node: " + file_id + " !!\n";
                }


            }
        }

        return clusterinfo_warning_header;
    }

    public String get_aws_dc(/*ClusterInfoParser cip,*/ JSONObject nodetoolStatusJSON, String aws_instance_type) {
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        Set<String> aws_dc_set = new HashSet<String>();
        String aws_dc = new String("- DC: [");
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;

                JSONObject node_obj = null;
                String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                if (node_info_obj.get(ValFactory.ISNODE_INFOEXIST).equals("true"))
                    node_obj = (JSONObject) node_info_obj.get(file_id);

                if (node_obj != null) {

                    JSONObject ec2_obj = (JSONObject) node_obj.get("ec2");
                    if (ec2_obj.get("instance-type") != null) {
                        if (aws_instance_type.equals(ec2_obj.get("instance-type").toString()))
                            aws_dc_set.add(tmpdcvar.get(ValFactory.DATACENTER).toString());
                    }

                }

            }
        }
        for (String aws_dc_str : aws_dc_set) {
            aws_dc += aws_dc_str + "|";
        }

        return aws_dc.substring(0, aws_dc.length() - 1) + "]";
    }

    public String get_non_aws_dc(/*ClusterInfoParser cip,*/ JSONObject nodetoolStatusJSON) {
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        Set<String> non_aws_dc_set = new HashSet<String>();
        String non_aws_dc = new String("- DC: [");
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;

                JSONObject node_obj = null;
                String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                if (node_info_obj.get(ValFactory.ISNODE_INFOEXIST).equals("true"))
                    node_obj = (JSONObject) node_info_obj.get(file_id);

                if (node_obj != null) {

                    JSONObject ec2_obj = (JSONObject) node_obj.get("ec2");
                    if (ec2_obj.get("instance-type") == null) {
                        non_aws_dc_set.add(tmpdcvar.get(ValFactory.DATACENTER).toString());
                    }

                }

            }
        }
        for (String aws_dc_str : non_aws_dc_set) {
            non_aws_dc += aws_dc_str + "|";
        }

        return non_aws_dc.substring(0, non_aws_dc.length() - 1) + "]";

    }
}
