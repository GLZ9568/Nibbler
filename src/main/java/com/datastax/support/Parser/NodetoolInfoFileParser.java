/*
 * Copyright (c)
 *
 * Date: 2/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Parser;

import com.datastax.support.Util.Inspector;
import com.datastax.support.Util.ValFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Chun Gao on 2/01/2018
 */

public class NodetoolInfoFileParser extends FileParser {
    private static final Logger logger = LogManager.getLogger(NodetoolInfoFileParser.class);

    private ArrayList<Properties> nodetoolInfoProperties;

    public NodetoolInfoFileParser (ArrayList<File> files) {
        super(files);
        parse();
    }

    private void parse () {
        nodetoolInfoProperties = new ArrayList<Properties>();

        for (File file : files) {
            if(file.getAbsolutePath().contains(ValFactory.NODETOOL) && file.getName().equals(ValFactory.INFO)) {
                Properties properties = new Properties();

                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String currentLine = scanner.nextLine();
                        String[] split = Inspector.splitByColon(currentLine);
                        if (split.length == 2) {
                            properties.put(split[0].trim(), split[1].trim());
                        }
                    }
                    String id = Inspector.getFileID(file);
                    properties.put(ValFactory.FILE_ID, id);
                    properties.put(ValFactory.FILE_PATH, file.getAbsolutePath());
                    properties.put(ValFactory.FILE_NAME, file.getName());
                } catch (FileNotFoundException fnfe) {
                    logCheckedException(logger, fnfe);
                }
                nodetoolInfoProperties.add(properties);
            }
        }
    }

    public ArrayList<Properties> getNodetoolInfoPropertiesList() {
        return nodetoolInfoProperties;
    }
}
