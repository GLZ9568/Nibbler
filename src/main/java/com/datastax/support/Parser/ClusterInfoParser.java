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
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mike Zhang on 30/11/2017.
 */

public class ClusterInfoParser extends FileParser{

    private static final Logger logger = LogManager.getLogger(ClusterInfoParser.class);

    private JSONObject cluster_info_obj;
    private JSONObject node_info_obj;
    private ArrayList<JSONObject> cpu_obj_list;
    private ArrayList<JSONObject> java_system_properties_obj_list;
    private ArrayList<JSONObject> machine_info_obj_list;
    private ArrayList<NibProperties> dsetool_ring_obj_list;
    private ArrayList<JSONObject> ntptime_obj_list;
    private ArrayList<JSONObject> ntpstat_obj_list;
    private ArrayList<JSONObject> os_info_obj_list;

/*  private boolean iscluster_infoexist = false;
    private boolean isnode_infoexist = false;
    private boolean iscpuexist = false;
    private boolean isjava_system_propertiesexist = false;
    private boolean ismachine_infoexist = false;
    */

    public ClusterInfoParser(ArrayList<File> files) {

        super(files);

      //  ArrayList<File> filelist = ff.getAllFiles();
        File filename;
        cluster_info_obj =  new JSONObject();
        cluster_info_obj.put(ValFactory.ISCLUSTER_INFOEXIST,"false");
        node_info_obj = new JSONObject();
        node_info_obj.put(ValFactory.ISNODE_INFOEXIST,"false");
        cpu_obj_list = new ArrayList<JSONObject>();
        java_system_properties_obj_list =  new ArrayList<JSONObject>();
        machine_info_obj_list =  new ArrayList<JSONObject>();
        ntptime_obj_list = new ArrayList<JSONObject>();
        ntpstat_obj_list = new ArrayList<JSONObject>();
        os_info_obj_list = new ArrayList<JSONObject>();

        for (int i =0; i < files.size();++i)
        {
            filename = files.get(i);
            if(filename.getName().contains(ValFactory.CLUSTER_INFO_JSON))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    cluster_info_obj = (JSONObject) jsonParser.parse(reader);
                    cluster_info_obj.put(ValFactory.ISCLUSTER_INFOEXIST,"true");
                    cluster_info_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    cluster_info_obj.put(ValFactory.FILE_NAME, filename.getName());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }
                //iscluster_infoexist = true;
            }
            if(filename.getName().contains(ValFactory.NODE_INFO_JSON))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    node_info_obj = (JSONObject) jsonParser.parse(reader);
                   // node_info_obj.put(StrFactory.file_id, setID(filename.getAbsolutePath()));
                    node_info_obj.put(ValFactory.ISNODE_INFOEXIST,"true");
                    node_info_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    node_info_obj.put(ValFactory.FILE_NAME, filename.getName());


                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }
                //isnode_infoexist = true;
            }
            if(filename.getName().contains(ValFactory.CPU_JSON))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    JSONObject cpu_info_obj = (JSONObject) jsonParser.parse(reader);
                    //cpu_info_obj.put("id","abc");
                   // String str = cpu_info_obj.get("id").toString();

                    cpu_info_obj.put(ValFactory.ISCPUEXIST,true);
                    cpu_info_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    cpu_info_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    cpu_info_obj.put(ValFactory.FILE_NAME, filename.getName());
                    cpu_obj_list.add(cpu_info_obj);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }

            }

            if(filename.getName().contains(ValFactory.JAVA_SYSTEM_PROPERTIES_JSON))
            {
                JSONObject java_system_properties_obj =  new JSONObject();

                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    java_system_properties_obj = (JSONObject) jsonParser.parse(reader);
                    //cpu_info_obj.put("id","abc");
                    // String str = cpu_info_obj.get("id").toString();

                    java_system_properties_obj.put(ValFactory.ISJAVA_SYSTEM_PROPERTIESEXIST,"true");
                    java_system_properties_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    java_system_properties_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    java_system_properties_obj.put(ValFactory.FILE_NAME, filename.getName());
                    java_system_properties_obj_list.add(java_system_properties_obj);

                } catch (IOException ioe) {
                    java_system_properties_obj.put(ValFactory.ISJAVA_SYSTEM_PROPERTIESEXIST,"false");
                    java_system_properties_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    java_system_properties_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    java_system_properties_obj.put(ValFactory.FILE_NAME, filename.getName());
                    java_system_properties_obj_list.add(java_system_properties_obj);
                    //StringWriter sw = new StringWriter();
                    //PrintWriter pw = new PrintWriter(sw);
                    //ioe.printStackTrace(pw);
                    //logger.error(sw.toString());
                    logger.error("node: " + setIP(filename.getAbsolutePath())+"\n"+Inspector.getStackTrace(ioe));
                    //ioe.printStackTrace();
                }
                catch (ParseException pe){
                    java_system_properties_obj.put(ValFactory.ISJAVA_SYSTEM_PROPERTIESEXIST,"false");
                    java_system_properties_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    java_system_properties_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    java_system_properties_obj.put(ValFactory.FILE_NAME, filename.getName());
                    java_system_properties_obj_list.add(java_system_properties_obj);
                    //StringWriter sw = new StringWriter();
                    //PrintWriter pw = new PrintWriter(sw);
                    //pe.printStackTrace(pw);
                    //logger.error(sw.toString());
                    logger.error("node: " + setIP(filename.getAbsolutePath())+"\n"+ Inspector.getStackTrace(pe));
                    //pe.printStackTrace();
                }
                catch (Exception e)
                {
                    java_system_properties_obj.put(ValFactory.ISJAVA_SYSTEM_PROPERTIESEXIST,"false");
                    java_system_properties_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    java_system_properties_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    java_system_properties_obj.put(ValFactory.FILE_NAME, filename.getName());
                    java_system_properties_obj_list.add(java_system_properties_obj);
                    //StringWriter sw = new StringWriter();
                    //PrintWriter pw = new PrintWriter(sw);
                    //e.printStackTrace(pw);
                    //logger.error(sw.toString());
                    logger.error("node: " + setIP(filename.getAbsolutePath())+"\n"+Inspector.getStackTrace(e));
                    //e.printStackTrace();
                }

            }

            if(filename.getName().contains(ValFactory.MACHINE_INFO_JSON))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    JSONObject machine_info_obj = (JSONObject) jsonParser.parse(reader);
                    //cpu_info_obj.put("id","abc");
                    // String str = cpu_info_obj.get("id").toString();

                    machine_info_obj.put(ValFactory.ISMACHINE_INFOEXIST,true);
                    machine_info_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    machine_info_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    machine_info_obj.put(ValFactory.FILE_NAME, filename.getName());
                    machine_info_obj_list.add(machine_info_obj);

                } catch (IOException e) {
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }
                catch (ParseException p){
                    logger.info(p.getMessage());
                    p.printStackTrace();
                }

            }

            if(filename.getName().contains(ValFactory.OS_INFO_JSON))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    JSONObject os_info_obj = (JSONObject) jsonParser.parse(reader);

                    os_info_obj.put(ValFactory.ISMACHINE_INFOEXIST,true);
                    os_info_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    os_info_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    os_info_obj.put(ValFactory.FILE_NAME, filename.getName());
                    os_info_obj_list.add(os_info_obj);

                } catch (IOException e) {
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }
                catch (ParseException p){
                    logger.info(p.getMessage());
                    p.printStackTrace();
                }

            }

            if(filename.getName().contains(ValFactory.NTPTIME))
            {
                try {

                    NtpInfoParser ntpInfoParser =  new NtpInfoParser();

                    JSONObject ntptime_obj = ntpInfoParser.parse(filename);

                    ntptime_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    ntptime_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    ntptime_obj.put(ValFactory.FILE_NAME, filename.getName());
                    ntptime_obj_list.add(ntptime_obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if(filename.getName().contains(ValFactory.NTPSTAT))
            {
                try {

                    NTPStatParser ntpStatParser =  new NTPStatParser();

                    JSONObject ntpstat_obj = ntpStatParser.parse(filename);

                    ntpstat_obj.put(ValFactory.FILE_ID, setIP(filename.getAbsolutePath()));
                    ntpstat_obj.put(ValFactory.FILE_PATH, filename.getAbsolutePath());
                    ntpstat_obj.put(ValFactory.FILE_NAME, filename.getName());
                    ntpstat_obj_list.add(ntpstat_obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

    }

    public String getClustername()
    {
        return null;
    }

    public JSONObject getCluster_info_obj() {
        //FileReader reader = new FileReader(ff.getFiles());
       //
        return cluster_info_obj;
    }

    private String setIP (String filepath) {
        if (Inspector.foundIPAddress(filepath)) {
            return Inspector.getIPAddress(filepath);
        }
        else {
            return "no_valid_ip_address";
        }
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

    public ArrayList<JSONObject> getNtptime_list() {
        return ntptime_obj_list;
    }

    public ArrayList<JSONObject> getOs_info_obj_list() {
        return os_info_obj_list;
    }

    public ArrayList<JSONObject> getNtpstat_obj_list() {
        return ntpstat_obj_list;
    }

    /* public boolean isIscluster_infoexist() {
        return iscluster_infoexist;
    }

    public boolean isIsnode_infoexist() {
        return isnode_infoexist;
    }

    public boolean isIscpuexist() {
        return iscpuexist;
    }

    public boolean isIsjava_system_propertiesexist() {
        return isjava_system_propertiesexist;
    }

    public boolean isIsmachine_infoexist() {
        return ismachine_infoexist;
    }*/
}
