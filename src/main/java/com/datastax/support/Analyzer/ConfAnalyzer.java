/*
 * Copyright (c)
 *
 * Date: 4/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.ValFactory;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.reactfx.value.Val;

import java.util.*;

/**
 * Created by Chun Gao on 26/11/2017
 */

public class ConfAnalyzer extends Analyzer {

    private final static Logger logger = LogManager.getLogger("ConfAnalyzer.class");
    private ArrayList<NibProperties> cassandraYamlPropertiesList;
    private ArrayList<NibProperties> dseYamlPropertiesList;
    private ArrayList<Map<String,Object>> cassandraYamlPropertyList;
    private ArrayList<Map<String,Object>> dseYamlPropertyList;
    private JSONObject nodetoolStatusJSON;
    private ArrayList<String> keyList = new ArrayList<String>();
    private ArrayList<JSONObject> conf_obj_list = new ArrayList<JSONObject>();
    private Map<String,Object> cas_map =  new HashMap<String, Object>();
    private Map<String,Object> dse_map = new HashMap<String, Object>();

    private JSONObject padding = new JSONObject();

    public ConfAnalyzer(FileFactory fileFactory) {
        super(fileFactory);
        this.cassandraYamlPropertiesList = fileFactory.getCassandraYamlPropertiesList();
        this.dseYamlPropertiesList = fileFactory.getDSEYamlPropertiesList();
        this.cassandraYamlPropertyList = fileFactory.getCassandraYamlPropertyList();
        this.dseYamlPropertyList = fileFactory.getDseYamlPropertyList();
        this.nodetoolStatusJSON = fileFactory.getNodetoolStatusJSON();
    }
    /*
    * authenticator: com.datastax.bdp.cassandra.auth.DseAuthenticator / AllowAllAuthenticator / PasswordAuthenticator
    * authorizer: com.datastax.bdp.cassandra.auth.DseAuthorizer / AllowAllAuthorizer / CassandraAuthorizer
    * role_manager: com.datastax.bdp.cassandra.auth.DseRoleManager /  CassandraRoleManager
    * roles_validity_in_ms: 2000
    * permissions_validity_in_ms: 2000
    * seeds: "10.143.99.101,10.143.99.102,10.143.97.192,10.143.97.193,10.143.97.195,10.143.97.196,10.161.99.93,10.161.99.94,10.161.97.192,10.161.97.193,10.161.97.195,10.161.97.196"
    * concurrent_compactors: 6
    * concurrent_reads: 64
    * concurrent_writes: 128
    * tombstone_warn_threshold: 2000
    * tombstone_failure_threshold: 200000
    * read_request_timeout_in_ms: 30000
    * write_request_timeout_in_ms: 10000
    * streaming_socket_timeout_in_ms: 72000000
    *
    * authentication_options:
    enabled: false
    default_scheme: internal


    role_management_options:
      mode: internal
    * **/

    public String generateConfOutput() {
        String confinfotext = new String();

        Map<String,Map<String,String>> seed_map = new HashMap<String, Map<String,String>>();
        Map<String,String> sub_seed_map = new HashMap<String, String>();
        ArrayList<String> node_list =  new ArrayList<String>();
        String[] splitline = new String[15];
        String confinfo_warning_header = new String("#### WARNING: ####\n");

        /// get each node's config and padding for each property column

        /*
        *
                    ValFactory.ADDRESS,
                    ValFactory.DC,
                    ValFactory.AUTHENTICATOR,
                    ValFactory.authentication_options,
                    ValFactory.AUTHORIZER,
                    ValFactory.concurrent_compactors,
                    ValFactory.concurrent_writes,
                    ValFactory.concurrent_reads,
                    ValFactory.write_request_timeout_in_ms,
                    ValFactory.read_request_timeout_in_ms,
                    ValFactory.streaming_socket_timeout_in_ms,
                    ValFactory.permissions_validity_in_ms,
                    ValFactory.role_manager,
                    ValFactory.role_management_options,
                    ValFactory.roles_validity_in_ms)
        *
        * */
        keyList.add(0, ValFactory.ADDRESS);
        keyList.add(1, ValFactory.DC);
        keyList.add(2, ValFactory.AUTHENTICATOR);
        keyList.add(3, ValFactory.authentication_options);
        keyList.add(4, ValFactory.AUTHORIZER);
        keyList.add(5, ValFactory.concurrent_compactors);
        keyList.add(6, ValFactory.concurrent_writes);
        keyList.add(7, ValFactory.concurrent_reads);
        keyList.add(8, ValFactory.write_request_timeout_in_ms);
        keyList.add(9, ValFactory.read_request_timeout_in_ms);
        keyList.add(10, ValFactory.streaming_socket_timeout_in_ms);
        keyList.add(11, ValFactory.permissions_validity_in_ms);
        keyList.add(12, ValFactory.role_manager);
        keyList.add(13, ValFactory.role_management_options);
        keyList.add(14, ValFactory.roles_validity_in_ms);


        padding = initiatePadding(keyList);
        for (NibProperties np_casyaml : cassandraYamlPropertiesList) {
            for (NibProperties np_dseyaml : dseYamlPropertiesList) {
                JSONObject conf_obj = new JSONObject();
                if (np_dseyaml.getProperty(ValFactory.FILE_ID).toString().equals(np_casyaml.getProperty(ValFactory.FILE_ID).toString())) {

                    splitline[0] = np_dseyaml.getProperty(ValFactory.FILE_ID).toString();
                    splitline[1] = getNodeDCName(splitline[0]);
                    if (np_casyaml.getProperty(ValFactory.AUTHENTICATOR.toLowerCase()).toLowerCase().contains("dseauthenticator"))
                        splitline[2] = "DseAuthenticator";
                    else if (np_casyaml.getProperty(ValFactory.AUTHENTICATOR.toLowerCase()).toLowerCase().contains("allowallauthenticator"))
                        splitline[2] = "AllowAllAuthenticator";
                    else if (np_casyaml.getProperty(ValFactory.AUTHENTICATOR.toLowerCase()).toLowerCase().contains("passwordauthenticator"))
                        splitline[2] = "PasswordAuthenticator";
                    else
                        splitline[2] = np_casyaml.getProperty(ValFactory.AUTHENTICATOR.toLowerCase());
/*
                    if (np_dseyaml.getProperty("default_scheme") != null) {

                        splitline[3] = np_dseyaml.getProperty("default_scheme");

                        logger.info(splitline[0] + " authentication_options in dse.yaml is: " + splitline[3]);
                    } else
                        splitline[3] = "NaN";*/
                    ////// get auth option in dse.yaml///

                    if(splitline[2].equals("DseAuthenticator")) {
                        for (Map<String, Object> dse_map_tmp : dseYamlPropertyList) {
                            dse_map = dse_map_tmp;
                            for (Map.Entry<String, Object> entry : dse_map.entrySet()) {
                                if (splitline[0].equals(entry.getKey())) {
                                    Map map = (Map) entry.getValue();
                                    logger.info("ip is: " + entry.getKey());

                                    String auth_option_str = map.get("authentication_options").toString();
                                    logger.info("dse.yaml auth options is : " + auth_option_str);

                                    String[] auth_options = Inspector.splitByComma
                                            (auth_option_str.trim().replaceAll("[{|}]", ""));

                                    for (String str : auth_options) {
                                        if (str.contains("default_scheme")) {
                                            splitline[3] = Inspector.splitByEqual(str)[1];
                                        }
                                    }

                                }
                            }
                        }
                    }
                    else
                        splitline[3] = "NaN";

                    //////// end of get auth option in dse.yaml/////


                    if (np_casyaml.getProperty(ValFactory.AUTHORIZER.toLowerCase()).toLowerCase().contains("dseauthorizer"))
                        splitline[4] = "DseAuthorizer";
                    else if (np_casyaml.getProperty(ValFactory.AUTHORIZER.toLowerCase()).toLowerCase().contains("allowallauthorizer"))
                        splitline[4] = "AllowAllAuthorizer";
                    else if (np_casyaml.getProperty(ValFactory.AUTHORIZER.toLowerCase()).toLowerCase().contains("cassandraauthorizer"))
                        splitline[4] = "CassandraAuthorizer";
                    else
                        splitline[4] = np_casyaml.getProperty(ValFactory.AUTHORIZER);

                    if (np_casyaml.getProperty("concurrent_compactors") != null)
                        splitline[5] = np_casyaml.getProperty("concurrent_compactors");

                    else {

                        splitline[5] = "NaN";
                    }

                    if (np_casyaml.getProperty("concurrent_writes") != null)
                        splitline[6] = np_casyaml.getProperty("concurrent_writes");
                    else
                        splitline[6] = "NaN";

                    if (np_casyaml.getProperty("concurrent_reads") != null)
                        splitline[7] = np_casyaml.getProperty("concurrent_reads");
                    else
                        splitline[7] = "NaN";

                    if (np_casyaml.getProperty("write_request_timeout_in_ms") != null)
                        splitline[8] = np_casyaml.getProperty("write_request_timeout_in_ms");
                    else
                        splitline[8] = "NaN";

                    if (np_casyaml.getProperty("read_request_timeout_in_ms") != null)
                        splitline[9] = np_casyaml.getProperty("read_request_timeout_in_ms");
                    else
                        splitline[9] = "NaN";

                    if (np_casyaml.getProperty("streaming_socket_timeout_in_ms") == null) {
                        splitline[10] = "NaN";
                        logger.info(splitline[0] + " stream timeout(null) is : " +
                                np_casyaml.getProperty("streaming_socket_timeout_in_ms"));
                    } else {
                        String str = np_casyaml.getProperty("streaming_socket_timeout_in_ms");
                        splitline[10] = str + "("+Inspector.milsecToTime(Integer.valueOf(str),false)+")";
                        logger.info(splitline[0] + " stream timeout is(not null) : " +
                                np_casyaml.getProperty("streaming_socket_timeout_in_ms"));
                    }

                    splitline[11] = np_casyaml.getProperty("permissions_validity_in_ms");

                    if (np_casyaml.getProperty(ValFactory.role_manager) != null) {
                        if (np_casyaml.getProperty(ValFactory.role_manager).toLowerCase().contains("dserolemanager"))
                            splitline[12] = "DseRoleManager";
                        else if (np_casyaml.getProperty(ValFactory.role_manager).toLowerCase().contains("cassandrarolemanager"))
                            splitline[12] = "CassandraRoleManager";
                        else
                            splitline[12] = np_casyaml.getProperty(ValFactory.role_manager);
                    } else
                        splitline[12] = "NaN";

                   /*
                    if (np_dseyaml.getProperty("mode") != null) {

                        if (np_dseyaml.getProperty("mode").toLowerCase().equals("internal") ||
                                np_dseyaml.getProperty("mode").toLowerCase().equals("ldap")) {
                            splitline[13] = np_dseyaml.getProperty("mode");
                            logger.info(splitline[0] + " role_management_options in dse.yaml is: " + splitline[13]);
                        } else {
                            splitline[13] = np_dseyaml.getProperty("mode");
                            logger.info(splitline[0] + " role_management_options in dse.yaml is: " + splitline[13]);
                        }

                    } else
                        splitline[13] = "NaN";

                    */

                    ///// get role_management_options in dse.yaml/////

                    if(splitline[12].equals("DseRoleManager")) {
                        for (Map<String, Object> dse_map_tmp : dseYamlPropertyList) {
                            dse_map = dse_map_tmp;
                            for (Map.Entry<String, Object> entry : dse_map.entrySet()) {
                                if (splitline[0].equals(entry.getKey())) {
                                    Map map = (Map) entry.getValue();
                                    logger.info("ip is: " + entry.getKey());

                                    Object role_man_opt_obj = map.get("role_management_options");
                                    if(role_man_opt_obj != null) {
                                        String dse_role_option_str = role_man_opt_obj.toString();
                                        logger.info("dse.yaml auth options is : " + dse_role_option_str);

                                        String[] role_options = Inspector.splitByComma
                                                (dse_role_option_str.trim().replaceAll("[{|}]", ""));

                                        for (String str : role_options) {
                                            if (str.contains("mode")) {
                                                splitline[13] = Inspector.splitByEqual(str)[1];
                                            }
                                        }
                                    }
                                    else
                                        splitline[13] = "NaN";

                                }
                            }
                        }
                    }
                    else
                        splitline[13] = "NaN";
                    ///// end of role_management_options in dse.yaml////



                    if (np_casyaml.getProperty("roles_validity_in_ms") != null)
                        splitline[14] = np_casyaml.getProperty("roles_validity_in_ms");
                    else
                        splitline[14] = "NaN";

                    padding = calculateMaxPadding(padding, splitline, keyList);

                    /***
                     *  ValFactory.ADDRESS,
                     ValFactory.AUTHENTICATOR,
                     ValFactory.authentication_options,
                     ValFactory.AUTHORIZER,
                     ValFactory.concurrent_compactors,
                     ValFactory.concurrent_writes,
                     ValFactory.concurrent_reads,
                     ValFactory.write_request_timeout_in_ms,
                     ValFactory.read_request_timeout_in_ms,
                     ValFactory.streaming_socket_timeout_in_ms,
                     ValFactory.permissions_validity_in_ms,
                     ValFactory.role_manager,
                     ValFactory.role_management_options,
                     ValFactory.roles_validity_in_ms
                     *
                     */
                    conf_obj.put(ValFactory.ADDRESS, splitline[0]);
                    conf_obj.put(ValFactory.DC, splitline[1]);
                    conf_obj.put(ValFactory.AUTHENTICATOR, splitline[2]);
                    conf_obj.put(ValFactory.authentication_options, splitline[3]);
                    conf_obj.put(ValFactory.AUTHORIZER, splitline[4]);
                    conf_obj.put(ValFactory.concurrent_compactors, splitline[5]);
                    conf_obj.put(ValFactory.concurrent_writes, splitline[6]);
                    conf_obj.put(ValFactory.concurrent_reads, splitline[7]);
                    conf_obj.put(ValFactory.write_request_timeout_in_ms, splitline[8]);
                    conf_obj.put(ValFactory.read_request_timeout_in_ms, splitline[9]);
                    conf_obj.put(ValFactory.streaming_socket_timeout_in_ms, splitline[10]);
                    conf_obj.put(ValFactory.permissions_validity_in_ms, splitline[11]);
                    conf_obj.put(ValFactory.role_manager, splitline[12]);
                    conf_obj.put(ValFactory.role_management_options, splitline[13]);
                    conf_obj.put(ValFactory.roles_validity_in_ms, splitline[14]);
                    conf_obj.put(ValFactory.SEEDS, np_casyaml.getProperty("seeds"));
                    sub_seed_map.put(splitline[0],np_casyaml.getProperty("seeds").toString());
                    seed_map.put(np_casyaml.getProperty("seeds").toString(),sub_seed_map);


                }

                conf_obj_list.add(conf_obj);

            }


        }

        if(cassandraYamlPropertiesList.size()==0)
        {
            confinfotext +="Not found cassandra.yaml files!!";
            return confinfotext;
        }
        confinfotext += "**  Configuration Parameter Summary **\n"
        +Inspector.generateEqualline(new String("**  Configuration Parameter Summary ***").length())+"\n";

        for (String key : keyList) {
            confinfotext += String.format("%1$-" + padding.get(key) + "s", key);
        }
        confinfotext += "\n";

        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);

        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            String dc_name = tmpdcvar.get(ValFactory.DATACENTER).toString();
            //   confinfotext+="Datacenter: "+ dc_name +"\n";
            //   confinfotext+= Inspector.generateEqualline(12+dc_name.length())+"\n";

            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;
                String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                for (Object conf_obj_tmp : conf_obj_list) {
                    JSONObject conf_obj_tmp1 = (JSONObject) conf_obj_tmp;
                    if (file_id.equals(conf_obj_tmp1.get(ValFactory.ADDRESS))
                            && conf_obj_tmp1.get(ValFactory.DC).equals(dc_name)) {
                        logger.info("ip is: " + file_id + " dc is: " + dc_name);
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.ADDRESS) + "s", conf_obj_tmp1.get(ValFactory.ADDRESS));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.DC) + "s", conf_obj_tmp1.get(ValFactory.DC));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.AUTHENTICATOR) + "s", conf_obj_tmp1.get(ValFactory.AUTHENTICATOR));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.authentication_options) + "s", conf_obj_tmp1.get(ValFactory.authentication_options));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.AUTHORIZER) + "s", conf_obj_tmp1.get(ValFactory.AUTHORIZER));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.concurrent_compactors) + "s", conf_obj_tmp1.get(ValFactory.concurrent_compactors));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.concurrent_writes) + "s", conf_obj_tmp1.get(ValFactory.concurrent_writes));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.concurrent_reads) + "s", conf_obj_tmp1.get(ValFactory.concurrent_reads));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.write_request_timeout_in_ms) + "s", conf_obj_tmp1.get(ValFactory.write_request_timeout_in_ms));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.read_request_timeout_in_ms) + "s", conf_obj_tmp1.get(ValFactory.read_request_timeout_in_ms));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.streaming_socket_timeout_in_ms) + "s", conf_obj_tmp1.get(ValFactory.streaming_socket_timeout_in_ms));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.permissions_validity_in_ms) + "s", conf_obj_tmp1.get(ValFactory.permissions_validity_in_ms));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.role_manager) + "s", conf_obj_tmp1.get(ValFactory.role_manager));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.role_management_options) + "s", conf_obj_tmp1.get(ValFactory.role_management_options));
                        confinfotext += String.format("%1$-" +
                                padding.get(ValFactory.roles_validity_in_ms) + "s", conf_obj_tmp1.get(ValFactory.roles_validity_in_ms));
                        confinfotext += "\n";
                    }
                }
            }
        }

        confinfotext +="\n";
        confinfotext += "**  Seed List Configuration **\n\n"
              //  + Inspector.generateEqualline(new String("**  Seed List Configuration **").length())+"\n\n"
        ;
        ArrayList<String> seed_key_list = new ArrayList<String>();
        seed_key_list.add(0, ValFactory.ADDRESS);
        seed_key_list.add(1, ValFactory.SEEDS);
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            String dc_name = tmpdcvar.get(ValFactory.DATACENTER).toString();
            confinfotext += "Datacenter: " + dc_name + "\n";
            confinfotext += Inspector.generateEqualline(12 + dc_name.length()) + "\n";
            JSONObject dcpadding = getPaddingbyDC(dc_name, seed_key_list);
            for (String key : seed_key_list) {
                confinfotext += String.format("%1$-" + dcpadding.get(key) + "s", key);
            }
            confinfotext += "\n";
            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;
                String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();

                for (Object conf_obj_tmp : conf_obj_list) {
                    JSONObject conf_obj_tmp1 = (JSONObject) conf_obj_tmp;
                    if (file_id.equals(conf_obj_tmp1.get(ValFactory.ADDRESS))
                            && conf_obj_tmp1.get(ValFactory.DC).equals(dc_name)) {

                        confinfotext += String.format("%1$-" +
                                dcpadding.get(ValFactory.ADDRESS) + "s", conf_obj_tmp1.get(ValFactory.ADDRESS));
                        confinfotext += String.format("%1$-" +
                                dcpadding.get(ValFactory.SEEDS) + "s", conf_obj_tmp1.get(ValFactory.SEEDS));
                        confinfotext += "\n";
                    }

                }

            }
            confinfotext += "\n";
        }

        logger.info("\n" + confinfotext);
        if(seed_map.keySet().size()>1)
        {
            confinfo_warning_header+="seed list is not consistent across the cluster!!!\n";
            /**
             *
             * Map<String,Map<String,String>> seed_map = new HashMap<String, Map<String,String>>();
             Map<String,String> sub_seed_map = new HashMap<String, String>();
             *
             * **/
            for (Map.Entry<String,Map<String,String>> entry : seed_map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                // get node list for seed list

                Map<String,String> seed_node_sub_list = entry.getValue();

                String node_list_str_tmp = "[";

                for(Map.Entry<String,String> entry1 : seed_node_sub_list.entrySet())
                {
                    if(entry1.getValue().equals(entry.getKey()))
                        node_list_str_tmp+=entry1.getKey()+",";
                }

                String node_list_str = node_list_str_tmp.substring(0,node_list_str_tmp.length()-1)+"]";

                confinfo_warning_header+="  - seeds:" + entry.getKey() + "\n" +
                                         "      - nodes:"+node_list_str+"\n";
            }

            confinfo_warning_header+="\n";

            confinfotext = confinfo_warning_header + confinfotext;

        }

        return confinfotext;
    }

    protected JSONObject getPaddingbyDC(String dc_name, ArrayList<String> keylist) {
        JSONObject padding = initiatePadding(keylist);

        String[] splitline = new String[2];
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);

        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(ValFactory.NODES);
            String dc_name1 = tmpdcvar.get(ValFactory.DATACENTER).toString();

            if (dc_name.equals(dc_name1)) {
                for (Object node : nodesarrary) {
                    JSONObject tempnodevar = (JSONObject) node;
                    String file_id = tempnodevar.get(ValFactory.ADDRESS).toString();
                    for (Object conf_obj_tmp : conf_obj_list) {
                        JSONObject conf_obj_tmp1 = (JSONObject) conf_obj_tmp;
                        if (file_id.equals(conf_obj_tmp1.get(ValFactory.ADDRESS))
                                && conf_obj_tmp1.get(ValFactory.DC).equals(dc_name)) {
                            splitline[0] = file_id;
                            splitline[1] = conf_obj_tmp1.get("seeds").toString();
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

   /* protected   ArrayList<JSONObject> getMaxPaddingforEachColumnperDC(JSONObject nodetoolStatusJSON, ArrayList<JSONObject> conf_obj_list)
    {
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(ValFactory.STATUS);
        JSONObject dc_padding = new JSONObject();
        ArrayList<JSONObject> dc_padding_list = new ArrayList<JSONObject>();
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            JSONArray nodesarrary =  (JSONArray)tmpdcvar.get(ValFactory.NODES);
            String dc_name = tmpdcvar.get(ValFactory.DATACENTER).toString();
            dc_padding.put(ValFactory.DC,dc_name);
            for(Object node :nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;
                String file_id= tempnodevar.get(ValFactory.ADDRESS).toString();


            }
        }

        return null;
    }*/
}
