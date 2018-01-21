/*
 * Copyright (c)
 *
 * Date: 6/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 6/01/2018
 */

public class FileParser {
    private static final Logger logger = LogManager.getLogger(FileParser.class);

    protected ArrayList<File> files;

    public FileParser (ArrayList<File> files) {
        this.files = files;
    }

    protected JSONObject initiatePadding(ArrayList<String> keys) {
        JSONObject padding = new JSONObject();
        for (String key : keys) {
            padding.put(key, key.length() + ValFactory.PAD);
        }
        return padding;
    }

    protected JSONObject calculatePadding(JSONObject padding, String[] splitLine) {
        for (int i=0; i<padding.size(); i++) {
            padding.put(padding.keySet().toArray()[i], (Integer) padding.get(padding.keySet().toArray()[i]) > splitLine[i].length() + ValFactory.PAD
                    ? (Integer) padding.get(padding.keySet().toArray()[i]) : splitLine[i].length() + ValFactory.PAD);
        }
        return padding;
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
        logger.error("Exception: " + e);
        logger.error("StackTrace: " + stackTrace.toString());
    }
}
