/*
 * Copyright (c)
 *
 * Date: 12/2/2018
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
 * Created by Mike Zhang on 12/02/2018.
 */

public class DiskIOParser extends FileParser{

    private static final Logger logger = LogManager.getLogger(DiskIOParser.class);

    private JSONObject disk_io_obj;

    private ArrayList<JSONObject> disk_io_obj_list;
    private JSONArray diskIOJSONArray;


    public DiskIOParser (ArrayList<File> files) {
        super(files);
        parse();
    }
    public ArrayList<JSONObject> parse()  {

        /*
         * {"disk_io_usage" :
         * [
         *  {"disk_name":"/dev/dm-0" , "await": "0.0", "w_await": "3.5"},
         *  {"disk_name":"/dev/sda" , "await": "2.3", "w_await": "6.1"},
         * ],
         * "file_id":"10.0.0.1"}
         *
         *
         * */
        disk_io_obj_list = new ArrayList<JSONObject>();


        for (File file : files) {
            if (file.getName().equals(ValFactory.DISK_IO_JSON))
            {
                diskIOJSONArray = new JSONArray();
                try {
                    FileReader reader = new FileReader(file.getAbsolutePath());
                    JSONParser jsonParser = new JSONParser();
                    disk_io_obj = (JSONObject) jsonParser.parse(reader);
                    JSONObject disk_info_obj_bynode = new JSONObject();
                    JSONObject disk_info_obj_invalid = new JSONObject();
                    boolean isvald = true;
                    if(disk_io_obj.get("await")!=null
                            && disk_io_obj.get("w_await")!=null
                            && disk_io_obj.get("r_await")!=null) {

                        String await_str = disk_io_obj.get("await").toString();
                        String[] await_with_diskname_and_space = Inspector.splitByComma(await_str);
                        String w_await_str = disk_io_obj.get("w_await").toString();
                        String[] w_await_with_diskname_and_space = Inspector.splitByComma(w_await_str);
                        String r_await_str = disk_io_obj.get("r_await").toString();
                        String[] r_await_with_diskname_and_space = Inspector.splitByComma(r_await_str);

                        double await;
                        double w_await;
                        double r_await;
                        for (String str : w_await_with_diskname_and_space) {
                            JSONObject disk_info_obj = new JSONObject();
                            String disk_name_w_await = Inspector.splitByColon(str)[0].trim().replaceAll("[{|}]", "")
                                    .replaceAll("\"", "")
                            ;
                            w_await = Double.valueOf(Inspector.splitByColon(str)[1].trim()
                                    .replaceAll("[{|}]", "").replaceAll("\"", ""));

                            for (String str1 : await_with_diskname_and_space) {
                                String disk_name_await = Inspector.splitByColon(str1)[0].trim().replaceAll("[{|}]", "")
                                        .replaceAll("\"", "");
                                if (disk_name_await.equals(disk_name_w_await)) {

                                    await = Double.valueOf(Inspector.splitByColon(str1)[1].trim().
                                            replaceAll("[{|}]", "").replaceAll("\"", ""));
                                    for(String str2 : r_await_with_diskname_and_space){

                                        String disk_name_r_await =  Inspector.splitByColon(str2)[0].trim().replaceAll("[{|}]", "")
                                                .replaceAll("\"", "");
                                        if(disk_name_r_await.equals(disk_name_await)){
                                            r_await = Double.valueOf(Inspector.splitByColon(str2)[1].trim().
                                                    replaceAll("[{|}]", "").replaceAll("\"", ""));
                                            disk_info_obj.put(ValFactory.AWAIT, String.format("%.2f", await));
                                            disk_info_obj.put(ValFactory.W_AWAIT, String.format("%.2f", w_await));
                                            disk_info_obj.put(ValFactory.R_AWAIT, String.format("%.2f", r_await));
                                            disk_info_obj.put(ValFactory.DISK_NAME, disk_name_await.replaceAll("\\\\", ""));
                                            isvald =  true;
                                        }
                                    }

                                }

                            }
                            diskIOJSONArray.add(disk_info_obj);

                        }

                        }else{
                        disk_info_obj_invalid.put(ValFactory.AWAIT, "NaN");
                        disk_info_obj_invalid.put(ValFactory.W_AWAIT, "NaN");
                        disk_info_obj_invalid.put(ValFactory.R_AWAIT, "NaN");
                        disk_info_obj_invalid.put(ValFactory.DISK_NAME, "NaN");
                        isvald =  false;
                        diskIOJSONArray.add(disk_info_obj_invalid);
                    }
                        disk_info_obj_bynode.put(ValFactory.DISK_IO_USAGE, diskIOJSONArray);
                        disk_info_obj_bynode.put(ValFactory.FILE_ID, setIP(file.getAbsolutePath()));
                        disk_info_obj_bynode.put(ValFactory.IS_IOINFO_VALID,isvald);
                        logger.info("disk io info: " + disk_info_obj_bynode.toString());
                        disk_io_obj_list.add(disk_info_obj_bynode);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        return disk_io_obj_list;
    }

    private String setIP (String filepath) {
        if (Inspector.foundIPAddress(filepath)) {
            return Inspector.getIPAddress(filepath);
        }
        else {
            return "no_valid_ip_address";
        }
    }

    public ArrayList<JSONObject> getDisk_io_obj_list() {
        return disk_io_obj_list;
    }
}