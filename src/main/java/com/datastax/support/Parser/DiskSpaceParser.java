/*
 * Copyright (c)
 *
 * Date: 28/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mike Zhang on 28/12/2017.
 */

public class DiskSpaceParser {

    private static final Logger logger = LogManager.getLogger(DiskSpaceParser.class);

    private JSONObject disk_space_obj;

    private ArrayList<JSONObject> disk_space_obj_list;
    private JSONArray diskJSONArray;
    public ArrayList<JSONObject> parse(ArrayList<File> files)  {

        /*
        * {"disk_space_usage" :
        * [
        *  {"disk_name":"/dev/xvda1" , "total_space": "800", "used_space": "100"},
        *  {"disk_name":"/dev/xvdf" , "total_space": "700", "used_space": "200"},
        * ],
        * "file_id":"10.0.0.1"}
        *
        *
        * */
        disk_space_obj_list = new ArrayList<JSONObject>();


        for (File file : files) {
            if (file.getName().equals(ValFactory.DISK_SPACE_JSON))
            {
                diskJSONArray = new JSONArray();
                try {
                FileReader reader = new FileReader(file.getAbsolutePath());
                JSONParser jsonParser = new JSONParser();
                disk_space_obj = (JSONObject) jsonParser.parse(reader);

                JSONObject disk_info_obj_bynode =  new JSONObject();
                String free_space_str = disk_space_obj.get("free").toString();
                String[] free_space_with_diskname_and_space = Inspector.splitByComma(free_space_str);

                String used_space_str = disk_space_obj.get("used").toString();
                String[] used_space_with_diskname_and_space = Inspector.splitByComma(used_space_str);
                String disk_name_used = new String();
                double free_disk_space;
                double used_disk_space;
                double total_disk_space;
                for(String str : used_space_with_diskname_and_space)
                {
                    JSONObject disk_info_obj =  new JSONObject();
                    disk_name_used = Inspector.splitByColon(str)[0].trim().replaceAll("[{|}]", "")
                            .replaceAll("\"","")
                    ;
                    used_disk_space = Double.valueOf(Inspector.splitByColon(str)[1].trim()
                            .replaceAll("[{|}]", "").replaceAll("\"",""));

                    for(String str1 : free_space_with_diskname_and_space)
                    {
                        String disk_name_free = Inspector.splitByColon(str1)[0].trim().replaceAll("[{|}]", "")
                                .replaceAll("\"","");
                        if(disk_name_free.equals(disk_name_used)){

                            free_disk_space = Double.valueOf(Inspector.splitByColon(str1)[1].trim().
                                    replaceAll("[{|}]", "").replaceAll("\"",""));
                            total_disk_space =  used_disk_space + free_disk_space;
                            disk_info_obj.put(ValFactory.TOTAL_SPACE,String.format("%.2f",total_disk_space));
                            disk_info_obj.put(ValFactory.USED_SPACE,String.format("%.2f",used_disk_space));
                            disk_info_obj.put(ValFactory.DISK_NAME,disk_name_used.replaceAll("\\\\",""));
                            //disk_space_obj.put(StrFactory.FILE_ID,setIP(file.getAbsolutePath()));
                        }

                    }
                    diskJSONArray.add(disk_info_obj);
                }
                disk_info_obj_bynode.put(ValFactory.DISK_SPACE_USAGE,diskJSONArray);
                disk_info_obj_bynode.put(ValFactory.FILE_ID,setIP(file.getAbsolutePath()));
                logger.info("disk space info: " + disk_info_obj_bynode.toString());
                disk_space_obj_list.add(disk_info_obj_bynode);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        return disk_space_obj_list;
    }

    private String setIP (String filepath) {
        if (Inspector.foundIPAddress(filepath)) {
            return Inspector.getIPAddress(filepath);
        }
        else {
            return "no_valid_ip_address";
        }
    }

    public ArrayList<JSONObject> getDisk_space_obj_list() {
        return disk_space_obj_list;
    }
}
