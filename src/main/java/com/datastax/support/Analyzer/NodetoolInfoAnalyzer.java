/*
 * Copyright (c)
 *
 * Date: 4/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Parser.NodetoolInfoParser;
import com.datastax.support.Parser.NodetoolStatusFileParser;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mike Zhang on 19/12/2017.
 */

public class NodetoolInfoAnalyzer {


    private static final Logger logger = LogManager.getLogger(NodetoolInfoAnalyzer.class);
    private boolean is_diff_heap_size = false;

    public TextArea generateNodetoolInfoOutput(FileFactory ff) {
        TextArea t = new TextArea();

        NodetoolInfoParser nip = new NodetoolInfoParser(ff.getAllFiles());
        ArrayList<JSONObject> info_obj_list = nip.getNodetoolInfoJSONList();
        logger.info("info obj list size is: " + info_obj_list.size());
        String nodetool_info_warning_text = new String("#### WARNING: ####\n");
        String nodetool_info_text =  new String("");
        JSONObject nodetoolStatusJSON;
        NodetoolStatusFileParser nodetoolStatusParser = new NodetoolStatusFileParser(ff.getAllFiles());
        nodetoolStatusJSON = nodetoolStatusParser.getNodetoolStatusJSON();
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        //logger.debug("JSONArray Size: " + dcArray.size());
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
//            logger.debug("DC: " + tmpdcvar.get(ValFactory.DATACENTER));
            Set<String> heap_size_set = new HashSet<String>();
            /////group by DC name///
            String dotlinestr = Inspector.generateDotline(19+ tmpdcvar.get(ValFactory.DATACENTER).toString().length())+"\n";
            nodetool_info_text+=dotlinestr +
                    ">>>Datacenter: "+ tmpdcvar.get(ValFactory.DATACENTER).toString()+"<<<<\n"+
                    dotlinestr;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;

                String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();

                for (JSONObject nodetool_info_obj: info_obj_list) {
                    //// node ip//////
                    if(file_id.equals(nodetool_info_obj.get(ValFactory.FILE_ID).toString())) {
//                        logger.debug("node: " + file_id);
                        nodetool_info_text+= "====== " + file_id + " =======\n";
                        //nodetool_info_text+= "Uptime: " + Inspector.secToTime(Integer.valueOf(nodetool_info_obj.get(ValFactory.INFO_UPTIME).toString()))+ "\n";
                        nodetool_info_text+= "Uptime: " + nodetool_info_obj.get(ValFactory.INFO_UPTIME).toString()+ "\n";
                        nodetool_info_text+= "Total Heap: " + nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP).toString()+ "mb\n";
                        nodetool_info_text+= "Used Heap: " + nodetool_info_obj.get(ValFactory.INFO_USEDHEAP).toString()+ "mb\n";
                        nodetool_info_text+= "Off Heap: " + nodetool_info_obj.get(ValFactory.INFO_OFFHEAP).toString()+ "mb\n";
                        nodetool_info_text+= "Gossip Generation: " + nodetool_info_obj.get(ValFactory.INFO_GENERATION).toString()+ "\n\n";
                        heap_size_set.add(nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP).toString().trim()+"mb");
                    }
                }
            }
            if(heap_size_set.size()>1) {
                is_diff_heap_size = true;
                String heap_str = new String();
                for(String str : heap_size_set)
                {
                    heap_str+=str+",";
                }
                nodetool_info_warning_text += "Different heap sizes in DC: "+
                        tmpdcvar.get(ValFactory.DATACENTER).toString() +"("+heap_str.substring(0,heap_str.length()-1)+")"+ "!!!!\n";
            }
        }

        if(is_diff_heap_size) {

            t.setStyle("-fx-font-size: 11pt; -fx-font-family:monospace");
            nodetool_info_warning_text +="\n";
            t.setText(nodetool_info_warning_text + nodetool_info_text);
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            double screen_height =  visualBounds.getHeight() ;
            //double screen_width = visualBounds.getWidth();
           // t.setMinHeight(screen_height *3*ValFactory.SCREEN_HEIGHT_FACTOR/5);
            t.setMinHeight(Inspector.getDynamicTextAreaHeight(nodetool_info_warning_text + nodetool_info_text));
            t.setEditable(false);
          //  t.setPrefWidth(1024);
          //  t.setMinHeight(450);
        } else {

            t.setStyle("-fx-font-size: 11pt; -fx-font-family:monospace");
            t.setText(nodetool_info_text);
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            double screen_height =  visualBounds.getHeight() ;
           // double screen_width = visualBounds.getWidth();
           // t.setMinHeight(screen_height *3*ValFactory.SCREEN_HEIGHT_FACTOR/5);
            t.setMinHeight(Inspector.getDynamicTextAreaHeight(nodetool_info_text));
            t.setEditable(false);
           // t.setPrefWidth(1024);
          //  t.setMinHeight(450);
        }

        return t;
    }
}
