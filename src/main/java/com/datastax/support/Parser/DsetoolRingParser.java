/*
 * Copyright (c)
 *
 * Date: 3/12/2017
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
 * Created by Mike Zhang on 3/12/2017.
 */

public class DsetoolRingParser {

    public TextArea generateDsetoolRingOutput(FileFactory ff)

    {
        TextArea t = new TextArea();
        File dsetoolringfile;
        ArrayList<File> filelist = ff.getFiles();
        boolean isvalidringoutput = false;
        for (int i =0; i < filelist.size();++i)
        {
            dsetoolringfile = filelist.get(i);
            if(dsetoolringfile.getName().contains("ring")&&dsetoolringfile.getAbsolutePath().contains("dsetool"))
            {
                try {
                    FileInputStream  fis = new FileInputStream(dsetoolringfile);
                    byte[] data = new byte[(int) dsetoolringfile.length()];

                    fis.read(data);
                    fis.close();
                    String str = new String(data, "UTF-8");
                    t.setText(str);
                    t.setPrefWidth(1024);
                    t.setMinHeight(450);

                    if (str.contains("Workload")){
                        isvalidringoutput = true;
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        if (!isvalidringoutput)
        {
            t.setText("No Valid dsetool ring collected!!");
        }

        return t;

    }
}
