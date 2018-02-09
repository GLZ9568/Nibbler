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

public class NodetoolInfoAnalyzer extends Analyzer{


    private static final Logger logger = LogManager.getLogger(NodetoolInfoAnalyzer.class);
    private boolean is_diff_heap_size = false;
    private ArrayList<String> keyList = new ArrayList<String>();
    private JSONObject padding = new JSONObject();
    private JSONObject nodetoolStatusJSON;
    private ArrayList<JSONObject> info_obj_list = new ArrayList<JSONObject>();
    private JSONObject node_info_obj;
    public NodetoolInfoAnalyzer(FileFactory fileFactory) {
        super(fileFactory);
        this.nodetoolStatusJSON = fileFactory.getNodetoolStatusJSON();
        this.info_obj_list = fileFactory.getInfo_obj_list();
        this.node_info_obj = fileFactory.getNode_info_obj();
    }
    public TextArea generateNodetoolInfoOutput() {
        TextArea t = new TextArea();

        //NodetoolInfoParser nip = new NodetoolInfoParser(ff.getAllFiles());
       // info_obj_list = nip.getNodetoolInfoJSONList();
        //logger.info("info obj list size is: " + info_obj_list.size());
        String nodetool_info_warning_text = new String("#### WARNING: ####\n");
        String nodetool_info_text =  new String("");
       // JSONObject nodetoolStatusJSON;
       // NodetoolStatusFileParser nodetoolStatusParser = new NodetoolStatusFileParser(ff.getAllFiles());
      //  nodetoolStatusJSON = nodetoolStatusParser.getNodetoolStatusJSON();
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
       // cip = new ClusterInfoParser(ff);
        keyList.add(0, ValFactory.ADDRESS);
        keyList.add(1,ValFactory.HOSTNAME);
        keyList.add(2, ValFactory.UPTIME_SECONDS);
        keyList.add(3, "Max Heap(mb)");
        keyList.add(4, "Used Heap(mb)");
        keyList.add(5, "Off Heap(mb)");
        keyList.add(6, "Gossip Generation");
        keyList.add(7, "Percent Repaired");
        //padding = initiatePadding(keyList);
        //logger.debug("JSONArray Size: " + dcArray.size());
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
//            logger.debug("DC: " + tmpdcvar.get(ValFactory.DATACENTER));
            Set<String> heap_size_set = new HashSet<String>();
            String dc_name = tmpdcvar.get(ValFactory.DATACENTER).toString();
            /////group by DC name///

            nodetool_info_text += "Datacenter: " + dc_name + "\n";
            nodetool_info_text += Inspector.generateEqualline(12 + dc_name.length()) + "\n";
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);

            JSONObject dcpadding = getPaddingbyDC(dc_name, keyList);
            for (String key : keyList) {
                nodetool_info_text += String.format("%1$-" + dcpadding.get(key) + "s", key);
            }

            nodetool_info_text += "\n";
            ArrayList<String> missing_node_list = getMissingNodeinDC(info_obj_list,dc_name);
            for (/*Object node : nodesarrary*/JSONObject nodetool_info_obj: info_obj_list) {
                //JSONObject tempnodevar = (JSONObject) node;

               // String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                String file_id = nodetool_info_obj.get(ValFactory.FILE_ID).toString();
                boolean foundnode = false;
                for (/*JSONObject nodetool_info_obj: info_obj_list*/Object node : nodesarrary) {
                    //// node ip//////
                    JSONObject tempnodevar = (JSONObject) node;
                    if(/*file_id.equals(nodetool_info_obj.get(ValFactory.FILE_ID).toString())*/
                            file_id.equals(tempnodevar.get(ValFactory.ADDRESS).toString())) {
                    //logger.debug("node: " + file_id);
                      /*  nodetool_info_text+= "====== " + file_id + " =======\n";
                        //nodetool_info_text+= "Uptime: " + Inspector.secToTime(Integer.valueOf(nodetool_info_obj.get(ValFactory.INFO_UPTIME).toString()))+ "\n";
                        nodetool_info_text+= "Uptime: " + nodetool_info_obj.get(ValFactory.INFO_UPTIME).toString()+ "\n";
                        nodetool_info_text+= "Total Heap: " + nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP).toString()+ "mb\n";
                        nodetool_info_text+= "Used Heap: " + nodetool_info_obj.get(ValFactory.INFO_USEDHEAP).toString()+ "mb\n";
                        nodetool_info_text+= "Off Heap: " + nodetool_info_obj.get(ValFactory.INFO_OFFHEAP).toString()+ "mb\n";
                        nodetool_info_text+= "Gossip Generation: " + nodetool_info_obj.get(ValFactory.INFO_GENERATION).toString()+ "\n\n";
                        heap_size_set.add(nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP).toString().trim()+"mb");

                        */

                        nodetool_info_text += String.format("%1$-" +
                                dcpadding.get(ValFactory.ADDRESS) + "s", nodetool_info_obj.get(ValFactory.FILE_ID));
                        JSONObject node_obj = null;
                        if(node_info_obj.get(ValFactory.ISNODE_INFOEXIST).equals("true"))
                            node_obj =  (JSONObject) node_info_obj.get(file_id);

                        if(node_obj !=null)
                        {
                            nodetool_info_text += String.format("%1$-" +
                                    dcpadding.get(ValFactory.HOSTNAME) + "s", node_obj.get("hostname").toString());
                        }

                        nodetool_info_text += String.format("%1$-" +
                                dcpadding.get(ValFactory.UPTIME_SECONDS) + "s", nodetool_info_obj.get(ValFactory.INFO_UPTIME)==null? "NaN": nodetool_info_obj.get(ValFactory.INFO_UPTIME));
                        nodetool_info_text += String.format("%1$-" +
                                dcpadding.get("Max Heap(mb)") + "s", nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP)==null? "NaN":  nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP));
                        nodetool_info_text += String.format("%1$-" +
                                dcpadding.get("Used Heap(mb)") + "s", nodetool_info_obj.get(ValFactory.INFO_USEDHEAP)==null? "NaN": nodetool_info_obj.get(ValFactory.INFO_USEDHEAP) );
                        nodetool_info_text += String.format("%1$-" +
                                dcpadding.get("Off Heap(mb)") + "s", nodetool_info_obj.get(ValFactory.INFO_OFFHEAP)==null? "NaN":nodetool_info_obj.get(ValFactory.INFO_OFFHEAP));
                        nodetool_info_text += String.format("%1$-" +
                                dcpadding.get("Gossip Generation") + "s", nodetool_info_obj.get(ValFactory.INFO_GENERATION)==null? "NaN":nodetool_info_obj.get(ValFactory.INFO_GENERATION));
                        if(nodetool_info_obj.containsKey(ValFactory.PERCENT_REPAIRED))
                            nodetool_info_text += String.format("%1$-" +
                                dcpadding.get("Percent Repaired") + "s", nodetool_info_obj.get(ValFactory.PERCENT_REPAIRED));
                        else
                            nodetool_info_text += String.format("%1$-" +
                                    dcpadding.get("Percent Repaired") + "s","NaN");

                        if(nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP)!=null&&!nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP).equals("NaN"))
                        heap_size_set.add(nodetool_info_obj.get(ValFactory.INFO_TOTALHEAP).toString().trim()+"mb");
                        nodetool_info_text += "\n";

                        foundnode = true;
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

            //// if the nodetool info is missing but we do not want to miss that in our output////

            if(missing_node_list.size()!=0) {

                for (String ip : missing_node_list){
                    nodetool_info_text += String.format("%1$-" +
                            dcpadding.get(ValFactory.ADDRESS) + "s", ip);
                JSONObject node_obj = null;
                if (node_info_obj.get(ValFactory.ISNODE_INFOEXIST).equals("true"))
                    node_obj = (JSONObject) node_info_obj.get(ip);

                if (node_obj != null) {
                    nodetool_info_text += String.format("%1$-" +
                            dcpadding.get(ValFactory.HOSTNAME) + "s", node_obj.get("hostname").toString());
                }

                nodetool_info_text += String.format("%1$-" +
                        dcpadding.get(ValFactory.UPTIME_SECONDS) + "s", "NaN");
                nodetool_info_text += String.format("%1$-" +
                        dcpadding.get("Max Heap(mb)") + "s", "NaN");
                nodetool_info_text += String.format("%1$-" +
                        dcpadding.get("Used Heap(mb)") + "s", "NaN");
                nodetool_info_text += String.format("%1$-" +
                        dcpadding.get("Off Heap(mb)") + "s", "NaN");
                nodetool_info_text += String.format("%1$-" +
                        dcpadding.get("Gossip Generation") + "s", "NaN");
                nodetool_info_text += String.format("%1$-" +
                        dcpadding.get("Percent Repaired") + "s", "NaN");

                nodetool_info_text += "\n";
            }
            }
            nodetool_info_text += "\n";
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
    protected JSONObject getPaddingbyDC(String dc_name, ArrayList<String> keylist) {
        JSONObject padding = initiatePadding(keylist);

        String[] splitline = new String[8];
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);

        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            String dc_name1 = tmpdcvar.get(ValFactory.DATACENTER).toString();

            if (dc_name.equals(dc_name1)) {
                for (Object node : nodesarrary) {
                    JSONObject tempnodevar = (JSONObject) node;
                    String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                    for (Object info_obj_tmp : info_obj_list) {
                        JSONObject info_obj_tmp1 = (JSONObject) info_obj_tmp;
                        if (file_id.equals(info_obj_tmp1.get(ValFactory.FILE_ID))) {

                            /*
                            *  keyList.add(0, ValFactory.ADDRESS);
                            *  keyList.add(1, ValFactory.UPTIME);
                            *  keyList.add(2, "Total Heap");
                            *  keyList.add(3, "Used Heap");
                            *  keyList.add(4, "Off Heap");
                            *  keyList.add(5, "Gossip Generation");
                            *  keyList.add(6, "Percent Repaired");
                            * */
                            splitline[0] = file_id;
                            JSONObject node_obj = null;
                            if(node_info_obj.get(ValFactory.ISNODE_INFOEXIST).equals("true"))
                                node_obj =  (JSONObject) node_info_obj.get(file_id);

                            if(node_obj !=null)
                            {
                                splitline[1] =  node_obj.get("hostname").toString();
                            }

                            if(info_obj_tmp1.get(ValFactory.INFO_UPTIME)!=null) {
                                splitline[2] = info_obj_tmp1.get(ValFactory.INFO_UPTIME).toString();
                            }else
                                splitline[2] = "NaN";

                            if(info_obj_tmp1.get(ValFactory.INFO_TOTALHEAP)!=null) {
                                splitline[3] = info_obj_tmp1.get(ValFactory.INFO_TOTALHEAP).toString();
                            }else
                                splitline[3] = "NaN";


                            if(info_obj_tmp1.get(ValFactory.INFO_USEDHEAP)!=null) {
                                splitline[4] = info_obj_tmp1.get(ValFactory.INFO_USEDHEAP).toString();
                            }else
                                splitline[4] = "NaN";


                            if(info_obj_tmp1.get(ValFactory.INFO_OFFHEAP)!=null) {
                                splitline[5] = info_obj_tmp1.get(ValFactory.INFO_OFFHEAP).toString();
                            }else
                                splitline[5] = "NaN";

                            if(info_obj_tmp1.get(ValFactory.INFO_GENERATION)!=null) {
                                splitline[6] = info_obj_tmp1.get(ValFactory.INFO_GENERATION).toString();
                            }else
                                splitline[6] = "NaN";

                            if(info_obj_tmp1.get(ValFactory.PERCENT_REPAIRED)!=null) {
                                splitline[7] = info_obj_tmp1.get(ValFactory.PERCENT_REPAIRED).toString();
                            }else
                                splitline[7] = "NaN";
                            if(!info_obj_tmp1.containsKey(ValFactory.PERCENT_REPAIRED)){

                                splitline[7] = "NaN";
                            }

                            padding = calculateMaxPadding(padding, splitline, keylist);

                        }

                    }

                }
            }
        }
        return padding;
    }

    protected JSONObject initiatePadding(ArrayList<String> keys) {
        JSONObject padding = new JSONObject();
        for (String key : keys) {
            padding.put(key, key.length() + ValFactory.PAD);
        }
        return padding;
    }

    protected JSONObject calculateMaxPadding(JSONObject padding, String[] splitLine, ArrayList<String> keylist) {
        for (int i = 0; i < padding.size(); i++) {
            logger.info("padding key is: " + keylist.get(i) + " splitline is: " + splitLine[i]);
            padding.put(keylist.get(i), (Integer) padding.get(keylist.get(i)) > splitLine[i].length() + ValFactory.PAD
                    ? (Integer) padding.get(keylist.get(i)) : splitLine[i].length() + ValFactory.PAD);
        }
        return padding;
    }

    protected String getNodeDCName(String node_ip)

    {
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            String tmp_dc_name = tmpdcvar.get(ValFactory.DATACENTER).toString();
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);

            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;
                String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                if (file_id.equals(node_ip))
                    return tmp_dc_name;
            }
        }


        return null;
    }

    protected ArrayList<String> getMissingNodeinDC(ArrayList<JSONObject> info_obj_list,String dc_name)
    {
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        ArrayList<String> missing_node_ip_list = new ArrayList<String>();
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            String dc_name1 = tmpdcvar.get(ValFactory.DATACENTER).toString();
            if (dc_name.equals(dc_name1)) {
                boolean foundnode = false;
                for (Object node : nodesarrary) {
                    JSONObject tempnodevar = (JSONObject) node;
                    String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                    for (Object info_obj_tmp : info_obj_list) {
                        JSONObject info_obj_tmp1 = (JSONObject) info_obj_tmp;
                        if (file_id.equals(info_obj_tmp1.get(ValFactory.FILE_ID))) {
                            foundnode = true;
                        }
                    }
                    if(!foundnode)
                    {
                        missing_node_ip_list.add(file_id);
                    }
                }
            }
        }
        return missing_node_ip_list;
    }
}
