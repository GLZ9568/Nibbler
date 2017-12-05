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
import com.datastax.support.Util.StrFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.*;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mike Zhang on 30/11/2017.
 */

public class ClusterInfoParser {

    private static final Logger logger = LogManager.getLogger(FileFactory.class);

    private JSONObject cluster_info_obj;
    private JSONObject node_info_obj;
    private ArrayList<JSONObject> cpu_obj_list;
    private ArrayList<JSONObject> java_system_properties_obj_list;
    private ArrayList<JSONObject> machine_info_obj_list;
  /*  private boolean iscluster_infoexist = false;
    private boolean isnode_infoexist = false;
    private boolean iscpuexist = false;
    private boolean isjava_system_propertiesexist = false;
    private boolean ismachine_infoexist = false;
    */

    public ClusterInfoParser(FileFactory ff) {

        ArrayList<File> filelist = ff.getFiles();
        File filename;
        cluster_info_obj =  new JSONObject();
        cluster_info_obj.put(StrFactory.iscluster_infoexist,false);
        node_info_obj = new JSONObject();
        node_info_obj.put(StrFactory.isnode_infoexist,false);

        for (int i =0; i < filelist.size();++i)
        {
            filename = filelist.get(i);
            if(filename.getName().contains(StrFactory.cluster_info))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    cluster_info_obj = (JSONObject) jsonParser.parse(reader);
                    cluster_info_obj.put(StrFactory.iscluster_infoexist,true);
                    cluster_info_obj.put(StrFactory.file_path, filename.getAbsolutePath());
                    cluster_info_obj.put(StrFactory.file_name, filename.getName());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }
                //iscluster_infoexist = true;
            }
            if(filename.getName().contains(StrFactory.node_info))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    node_info_obj = (JSONObject) jsonParser.parse(reader);
                   // node_info_obj.put(StrFactory.file_id, setID(filename.getAbsolutePath()));
                    node_info_obj.put(StrFactory.isnode_infoexist,true);
                    node_info_obj.put(StrFactory.file_path, filename.getAbsolutePath());
                    node_info_obj.put(StrFactory.file_name, filename.getName());


                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }
                //isnode_infoexist = true;
            }
            if(filename.getName().contains(StrFactory.cpu))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    JSONObject cpu_info_obj = (JSONObject) jsonParser.parse(reader);
                    //cpu_info_obj.put("id","abc");
                   // String str = cpu_info_obj.get("id").toString();
                    cpu_info_obj.put(StrFactory.file_id, setIP(filename.getAbsolutePath()));
                    cpu_info_obj.put(StrFactory.file_path, filename.getAbsolutePath());
                    cpu_info_obj.put(StrFactory.file_name, filename.getName());
                    cpu_obj_list.add(cpu_info_obj);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }
                //isnode_infoexist = true;
            }

            if(filename.getName().contains(StrFactory.java_system_properties))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    JSONObject java_system_properties_obj = (JSONObject) jsonParser.parse(reader);
                    //cpu_info_obj.put("id","abc");
                    // String str = cpu_info_obj.get("id").toString();
                    java_system_properties_obj.put(StrFactory.file_id, setIP(filename.getAbsolutePath()));
                    java_system_properties_obj.put(StrFactory.file_path, filename.getAbsolutePath());
                    java_system_properties_obj.put(StrFactory.file_name, filename.getName());
                    java_system_properties_obj_list.add(java_system_properties_obj);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }
                //isnode_infoexist = true;
            }

            if(filename.getName().contains(StrFactory.machine_info))
            {
                try {
                    FileReader reader = new FileReader(filename.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    JSONObject machine_info_obj = (JSONObject) jsonParser.parse(reader);
                    //cpu_info_obj.put("id","abc");
                    // String str = cpu_info_obj.get("id").toString();
                    machine_info_obj.put(StrFactory.file_id, setIP(filename.getAbsolutePath()));
                    machine_info_obj.put(StrFactory.file_path, filename.getAbsolutePath());
                    machine_info_obj.put(StrFactory.file_name, filename.getName());
                    machine_info_obj_list.add(machine_info_obj);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ParseException p){
                    p.printStackTrace();
                }
                //isnode_infoexist = true;
            }

            if(filename.getName().contains(StrFactory.ntptime))
            {
                try {

                    FileInputStream  fis = new FileInputStream(filename);
                    byte[] data = new byte[(int) filename.length()];

                    fis.read(data);
                    fis.close();
                    String str = new String(data, "UTF-8");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //isnode_infoexist = true;
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

    public ArrayList<JSONObject> getCpu_obj() {
        return cpu_obj_list;
    }

    public ArrayList<JSONObject> getJava_system_properties_obj() {
        return java_system_properties_obj_list;
    }

    public ArrayList<JSONObject> getMachine_info_obj() {
        return machine_info_obj_list;
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
