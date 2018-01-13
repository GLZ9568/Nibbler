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
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;

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
        ArrayList<File> filelist = ff.getAllFiles();
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
                    //t.setPrefWidth(1024);
                   // t.setMinHeight(450);
                    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
                    double screen_height =  visualBounds.getHeight() ;
                    double screen_width = visualBounds.getWidth();
                    t.setMinHeight(screen_height*0.4);
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
            t.setStyle("-fx-font-size: 11pt; -fx-font-family: Courier New");
            t.setText("No Valid dsetool ring collected!!");
        }
        t.setStyle("-fx-font-size: 11pt; -fx-font-family: Courier New");
        return t;

    }
}
