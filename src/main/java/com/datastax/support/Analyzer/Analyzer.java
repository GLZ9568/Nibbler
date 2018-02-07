/*
 * Copyright (c)
 *
 * Date: 3/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Chun Gao on 3/01/2018
 */

public class Analyzer {
    private final static Logger logger = LogManager.getLogger("Analyzer.class");

    protected FileFactory fileFactory;
    
    public Analyzer(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    protected JSONArray sortJSONArray (JSONArray unsortedJSONArray, final String sortKey, final boolean desc, final String type) {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

        for (Object jsonObject : unsortedJSONArray) {
            jsonObjects.add((JSONObject) jsonObject);
        }
        Collections.sort(jsonObjects, new Comparator<JSONObject>() {
            public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                if (type.toLowerCase().equals("string")) {
                    String valueA = new String();
                    String valueB = new String();
                    try {
                        valueA = (String) jsonObjectA.get(sortKey);
                        valueB = (String) jsonObjectB.get(sortKey);
                    } catch (JSONException jsone) {
                        logException(logger, jsone);
                    }
                    if (!desc) {
                        return valueA.compareTo(valueB);
                    } else {
                        return valueB.compareTo(valueA);
                    }
                } else if (type.toLowerCase().equals("long")) {
                    Long valueA = 0L;
                    Long valueB = 0L;
                    try {
                        valueA = Long.parseLong(jsonObjectA.get(sortKey).toString());
                        valueB = Long.parseLong(jsonObjectB.get(sortKey).toString());
                    } catch (JSONException jsone) {
                        logException(logger, jsone);
                    }
                    if (!desc) {
                        return valueA.compareTo(valueB);
                    } else {
                        return valueB.compareTo(valueA);
                    }
                } else if (type.toLowerCase().equals("double")) {
                    Double valueA = 0.0;
                    Double valueB = 0.0;
                    try {
                        valueA = Double.parseDouble(jsonObjectA.get(sortKey).toString());
                        valueB = Double.parseDouble(jsonObjectB.get(sortKey).toString());
                    } catch (JSONException jsone) {
                        logException(logger, jsone);
                    }
                    if (!desc) {
                        return valueA.compareTo(valueB);
                    } else {
                        return valueB.compareTo(valueA);
                    }
                } else {
                    // this should not happen, only use the defined type to sort the JSONArray, currently there are String and Long
                    // can implement more types here later if needed
                    return 0;
                }
            }
        });
        sortedJsonArray.addAll(jsonObjects);
        return sortedJsonArray;
    }

    protected ArrayList<Properties> sortPropertiesArrayList(ArrayList<Properties> unsortedArrayList, final String sortKey, final boolean desc, final String type) {
        ArrayList<Properties> sortedArrayList = new ArrayList<Properties>();

        List<Properties> propertiesList = new ArrayList<Properties>();

        for (Properties properties : unsortedArrayList) {
            propertiesList.add(properties);
        }

        Collections.sort(propertiesList, new Comparator<Properties>() {
            public int compare(Properties propertiesA, Properties propertiesB) {
                if (type.toLowerCase().equals("string")) {
                    String valueA = new String();
                    String valueB = new String();
                    try {
                        valueA = (String) propertiesA.get(sortKey);
                        valueB = (String) propertiesB.get(sortKey);
                    } catch (JSONException jsone) {
                        logException(logger, jsone);
                    }
                    if (!desc) {
                        return valueA.compareTo(valueB);
                    } else {
                        return valueB.compareTo(valueA);
                    }
                } else if (type.toLowerCase().equals("long")) {
                    Long valueA = 0L;
                    Long valueB = 0L;
                    try {
                        valueA = Long.parseLong(propertiesA.get(sortKey).toString());
                        valueB = Long.parseLong(propertiesB.get(sortKey).toString());
                    } catch (JSONException jsone) {
                        logException(logger, jsone);
                    }
                    if (!desc) {
                        return valueA.compareTo(valueB);
                    } else {
                        return valueB.compareTo(valueA);
                    }
                } else if (type.toLowerCase().equals("double")) {
                    Double valueA = 0.0;
                    Double valueB = 0.0;
                    try {
                        valueA = Double.parseDouble(propertiesA.get(sortKey).toString());
                        valueB = Double.parseDouble(propertiesB.get(sortKey).toString());
                    } catch (JSONException jsone) {
                        logException(logger, jsone);
                    }
                    if (!desc) {
                        return valueA.compareTo(valueB);
                    } else {
                        return valueB.compareTo(valueA);
                    }
                } else {
                    // this should not happen, only use the defined type to sort the JSONArray, currently there are String and Long
                    // can implement more types here later if needed
                    return 0;
                }
            }
        });

        sortedArrayList.addAll(propertiesList);
        return sortedArrayList;
    }

    protected void logException (Logger logger, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        logger.error("Exception: " + e);
        logger.error("StackTrace: " + stackTrace.toString());
    }

    protected HashMap<String, Integer> initiatePadding(ArrayList<String> keys) {
        HashMap<String, Integer> padding = new HashMap<String, Integer>();
        for (String key : keys) {
            padding.put(key, key.length() + ValFactory.PAD);
        }
        return padding;
    }

    protected HashMap<String, Integer> calculatePadding(HashMap<String, Integer> padding, ArrayList<String> keys, ArrayList<String> values) {
        for (int i=0; i<keys.size(); i++) {
            padding.put(keys.get(i), padding.get(keys.get(i)) > values.get(i).length() + ValFactory.PAD
                    ? padding.get(keys.get(i)) : values.get(i).length() + ValFactory.PAD);
        }
        return padding;
    }

    protected String calByte (double val) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (val < 1024) {
            return df.format(val) + " B";
        } else {
            val = val / 1024;
            if (val < 1024) {
                return df.format(val) + " KB";
            } else {
                val = val / 1024;
                if (val < 1024) {
                    return df.format(val) + " MB";
                } else {
                    val = val / 1024;
                    if (val < 1024) {
                        return df.format(val) + " GB";
                    } else {
                        return df.format(val / 1024) + " TB";
                    }
                }
            }
        }
    }

    protected String printJSONArrayWithPadding(JSONArray sortedJSONArray, ArrayList<String> keys, HashMap<String, Integer> padding, boolean printDividingLine, int numOfRecords) {
        String output = "";
        String key = "";

        for (int i = 0; i < keys.size(); i++) {
            key += String.format("%1$-" + padding.get(keys.get(i)) + "s", keys.get(i));
        }

        output += key + "\n";
        if (printDividingLine) {
            output += printDividingLine(key.length());
            output += "\n";
        }

        if (numOfRecords != 0 && sortedJSONArray.size() >= numOfRecords) {
            for (int i=0; i<numOfRecords; i++) {
                for (int j = 0; j < keys.size(); j++) {
                    output += String.format("%1$-" + padding.get(keys.get(j)) + "s", ((JSONObject) sortedJSONArray.get(i)).get(keys.get(j)));
                }
                output += "\n";
            }
        } else {
            for (Object sortedJSONObject : sortedJSONArray) {
                if (sortedJSONObject instanceof JSONObject) {
                    for (int i = 0; i < keys.size(); i++) {
                        output += String.format("%1$-" + padding.get(keys.get(i)) + "s", ((JSONObject) sortedJSONObject).get(keys.get(i)));
                    }
                    output += "\n";
                }
            }
        }
        return output;
    }

    protected String printDividingLine (int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<length; i++) {
            stringBuilder.append("=");
        }
        return stringBuilder.toString();
    }

    protected void logCheckedException(Logger logger, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        logger.error("Exception: " + e);
        logger.error("StackTrace: " + stackTrace.toString());
    }

    protected void logUncheckedException(Logger logger, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        logger.error("Encountered Unchecked Exception: " + e);
        logger.error("StackTrace: " + stackTrace.toString());
    }
}
