/*
 * Copyright (c)
 *
 * Date: 5/1/2018
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Chun Gao on 5/01/2018
 */

public class ProxyHistogramsFileParser extends FileParser {
    private static final Logger logger = LogManager.getLogger(ProxyHistogramsFileParser.class);

    private ArrayList<JSONObject> proxyHistogramsJSONList;
    private JSONObject proxyHistogramsJSON;
    private JSONArray latencyArray;
    private JSONObject latencyJSON;

    public ProxyHistogramsFileParser(ArrayList<File> files) {
        super(files);
        parse();
    }

    /**
     {
     "file_path":"..nodes/48.25.245.102/nodetool/proxyhistograms","file_name":"proxyhistograms","file_id":"48.25.245.102",
     "proxyhistograms":
        [
            {"PCT":"50%","VWWLAT_μs":"0.00","CASRLAT_μs":"0.00","RLAT_μs":"61.21","WLAT_μs":"126.93","RGLAT_μs":"0.00","CASWLAT_μs":"0.00"},
            {"PCT":"75%","VWWLAT_μs":"0.00","CASRLAT_μs":"0.00","RLAT_μs":"88.15","WLAT_μs":"152.32","RGLAT_μs":"0.00","CASWLAT_μs":"0.00"},
            {"PCT":"95%","VWWLAT_μs":"0.00","CASRLAT_μs":"0.00","RLAT_μs":"152.32","WLAT_μs":"379.02","RGLAT_μs":"0.00","CASWLAT_μs":"0.00"},
            {"PCT":"98%","VWWLAT_μs":"0.00","CASRLAT_μs":"0.00","RLAT_μs":"182.79","WLAT_μs":"379.02","RGLAT_μs":"0.00","CASWLAT_μs":"0.00"},
            {"PCT":"99%","VWWLAT_μs":"0.00","CASRLAT_μs":"0.00","RLAT_μs":"182.79","WLAT_μs":"379.02","RGLAT_μs":"0.00","CASWLAT_μs":"0.00"},
            {"PCT":"Min","VWWLAT_μs":"0.00","CASRLAT_μs":"0.00","RLAT_μs":"14.24","WLAT_μs":"73.46","RGLAT_μs":"0.00","CASWLAT_μs":"0.00"},
            {"PCT":"Max","VWWLAT_μs":"0.00","CASRLAT_μs":"0.00","RLAT_μs":"182.79","WLAT_μs":"379.02","RGLAT_μs":"0.00","CASWLAT_μs":"0.00"}
        ],
     "Padding":{"PCT":5,"VWWLAT_μs":11,"CASRLAT_μs":12,"RLAT_μs":9,"WLAT_μs":9,"RGLAT_μs":10,"CASWLAT_μs":12}
     }
     */
    private void parse () {
        proxyHistogramsJSONList = new ArrayList<JSONObject>();

        for (File file : files) {
            if (file.getName().equals(ValFactory.PROXYHISTOGRAMS)) {
                proxyHistogramsJSON = new JSONObject();
                latencyArray = new JSONArray();
                JSONObject padding = new JSONObject();

                proxyHistogramsJSON.put(ValFactory.FILE_PATH, file.getAbsolutePath());
                proxyHistogramsJSON.put(ValFactory.FILE_NAME, file.getName());
                proxyHistogramsJSON.put(ValFactory.FILE_ID, Inspector.getFileID(file));

                ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(ValFactory.PERCENTILE, ValFactory.READ_LATENCY, ValFactory.WRITE_LATENCY, ValFactory.RANGE_LATENCY));
                try {
                    Scanner scanner = new Scanner(file);
                    padding = initiatePadding(keyList);
                    boolean keyListModified = false;
                    while (scanner.hasNextLine()) {
                        String currentLine = scanner.nextLine();
                        if (currentLine.contains("%") || currentLine.contains(ValFactory.MIN) || currentLine.contains(ValFactory.MAX)) {
                            latencyJSON = new JSONObject();
                            String[] splitLine = Inspector.splitBySpace(currentLine);
                            if(splitLine.length > 4) {
                                if (!keyListModified) {
                                    keyList.add(ValFactory.CAS_READ_LATENCY);
                                    keyList.add(ValFactory.CAS_WRITE_LATENCY);
                                    keyList.add(ValFactory.VIEW_WRITE_LATENCY);
                                    padding = initiatePadding(keyList);
                                    keyListModified = true;
                                }
                            }
                            if (keyList.size() == splitLine.length) {
                                // if all keys and values both exists for a row, this is what suppose to happen
                                for (int i = 0; i < keyList.size(); i++) {
                                    latencyJSON.put(keyList.get(i), splitLine[i]);
                                }
                                padding = calculatePadding(padding, splitLine);
                            } else if (keyList.size() > splitLine.length){
                                // if all keys exists but some values are missing, we read in the available value and fill the rest with "--"
                                String[] newSplitLine = new String[keyList.size()];
                                for (int i = 0; i < keyList.size(); i++) {
                                    if(i<splitLine.length) {
                                        newSplitLine[i] = splitLine[i];
                                        latencyJSON.put(keyList.get(i), splitLine[i]);
                                    } else {
                                        newSplitLine[i] = "--";
                                        latencyJSON.put(keyList.get(i), "--");
                                    }
                                }
                                padding = calculatePadding(padding, newSplitLine);
                            } else {
                                // (this should not happen) if all values exists but keys missing, in this case, we return empty result set for now
                            }
                            latencyArray.add(latencyJSON);
                        }
                    }
                } catch (FileNotFoundException fnfe) {
                    logCheckedException(logger, fnfe);
                } catch (Exception e) {
                    logUncheckedException(logger, e);
                }
                proxyHistogramsJSON.put(ValFactory.PROXYHISTOGRAMS, latencyArray);
                proxyHistogramsJSON.put(ValFactory.PADDING, padding);
                proxyHistogramsJSONList.add(proxyHistogramsJSON);
            }
        }
    }

    public ArrayList<JSONObject> getProxyHistogramsJSONList() {
        return proxyHistogramsJSONList;
    }
}
