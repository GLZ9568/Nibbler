/*
 * Copyright (c)
 *
 * Date: 12/2/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.test;

import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mike Zhang on 12/02/2018.
 */

public class DiskIOParserTest extends Test{

    private static final Logger logger = LogManager.getLogger(DiskIOParserTest.class);

    private ArrayList<JSONObject> disk_io_obj_list;

    public void parseFiles() {
        //confFileParser = new ConfFileParser(files);
        disk_io_obj_list = fileFactory.getDisk_io_obj_list();
        for(JSONObject disk_io_obj: disk_io_obj_list)
        {
            logger.info(disk_io_obj.get(ValFactory.FILE_ID)+ " disk io usage: "+ disk_io_obj.get(ValFactory.DISK_IO_USAGE));
        }

    }

    public static void main (String[] args) {
        DiskIOParserTest dipt = new DiskIOParserTest();
        dipt.initiate();
        dipt.parseFiles();
        System.exit(1);

    }
}