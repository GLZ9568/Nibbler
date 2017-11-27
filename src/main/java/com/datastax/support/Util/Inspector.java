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
}