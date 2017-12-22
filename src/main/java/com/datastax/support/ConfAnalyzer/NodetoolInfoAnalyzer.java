/*
 * Copyright (c)
 *
 * Date: 19/12/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.ConfAnalyzer;

import com.datastax.support.Parser.NodetoolInfoParser;
import com.datastax.support.Parser.NodetoolStatusParser;
import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.StrFactory;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Mike Zhang on 19/12/2017.
 */

public class NodetoolInfoAnalyzer {


    private static final Logger logger = LogManager.getLogger(FileFactory.class);

    public TextArea generateNodeStatusOutput(FileFactory ff) {
        TextArea t = new TextArea();

        NodetoolInfoParser nip = new NodetoolInfoParser();
        JSONObject nodetoolStatusJSON = new JSONObject();
        NodetoolStatusParser nodetoolStatusParser = new NodetoolStatusParser();
        nodetoolStatusParser.parse(ff.getFiles());
        nodetoolStatusJSON = nodetoolStatusParser.getNodetoolStatusJSON();
        JSONArray dcArray = (JSONArray) nodetoolStatusJSON.get(StrFactory.STATUS);
        //logger.debug("JSONArray Size: " + dcArray.size());
        for (Object dc : dcArray) {
            JSONObject tmpdcvar = (JSONObject) dc;
            //logger.debug("DC: " + tmpdcvar.get(StrFactory.DATACENTER));


            JSONArray nodesarrary = (JSONArray) tmpdcvar.get(StrFactory.NODES);
            for (Object node : nodesarrary) {
                JSONObject tempnodevar = (JSONObject) node;

                //logger.debug("node: " + tempnodevar.get(StrFactory.ADDRESS));

                String file_id = tempnodevar.get(StrFactory.ADDRESS).toString();
            }


        }
        return t;
    }

}
