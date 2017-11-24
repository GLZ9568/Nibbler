/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;


/**
 * Created by Chun Gao on 24/11/2017
 */

public class NibProperties extends Properties {
    private static final Logger logger = LogManager.getLogger(NibProperties.class);

    public void load (FileInputStream input) throws IOException {
        Scanner scanner = new Scanner(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        while (scanner.hasNext()) {
            output.write(scanner.nextLine().replace(" ","").getBytes());
            output.write("\n".getBytes());
        }

        InputStream in = new ByteArrayInputStream(output.toByteArray());
        super.load(in);
    }
}
