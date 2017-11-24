/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.FileFactory;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mike Zhang on 24/11/2017.
 *
 * Parse and analyze nodetool status output
 */

public class statusParser {

    public TextArea generateNodeStatusOutput(FileFactory ff)

    {
        TextArea t = new TextArea();
        File statusfile;
        ArrayList<File> filelist = ff.getFiles();
        for (int i =0; i < filelist.size();++i)
        {
            statusfile = filelist.get(i);
            if(statusfile.getName().contains("status"))
            {
                try {
                FileInputStream  fis = new FileInputStream(statusfile);
                byte[] data = new byte[(int) statusfile.length()];

                    fis.read(data);
                    fis.close();
                    String str = new String(data, "UTF-8");
                    t.setText(str);
                    //t.setPrefSize(1024,768);
                    if (str.contains("Datacenter:"))
                        break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        return t;

    }
}
