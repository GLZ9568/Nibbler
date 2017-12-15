/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Util;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chun Gao on 23/11/2017
 */

public final class Inspector {

    private static final Logger logger = LogManager.getLogger(Inspector.class);

    private static final String IP_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

    public static boolean foundWindowsOS() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    public static boolean foundIPAddress(String path) {
        Pattern pattern = Pattern.compile(IP_PATTERN);
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    public static String getIPAddress(String path) {
        String ip = "";
        Pattern pattern = Pattern.compile(IP_PATTERN);
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            ip = matcher.group(0);
        }
        return ip;
    }

    public static String getFileID (File file) {
        if (foundIPAddress(file.getAbsolutePath())) {
            return getIPAddress(file.getAbsolutePath());
        } else if (foundOpsCenter(file.getAbsolutePath())) {
            return StrFactory.OPSCENTERD;
        } else {
            return "Cannot find file ID information";
        }
    }

    public static boolean foundOpsCenter (String path) {
        if (path.contains(StrFactory.OPSCENTERD)) {
            return true;
        }
        return false;
    }

    public static String[] splitBySpace (String input) {
        return input.split("\\s+");
    }

    public static String[] splitByComma (String input) {
        return input.split(",");
    }

    public static String[] splitByColon(String input){

        return input.split(":");
    }
}