/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Util;

import com.datastax.support.Nibbler;
import javafx.geometry.Rectangle2D;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

import java.net.URISyntaxException;
import java.util.Date;
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

    public static boolean foundIPAddress(String str) {
        Pattern pattern = Pattern.compile(IP_PATTERN);
        Matcher matcher = pattern.matcher(str);
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

    public static String getFileID(File file) {
        if (foundIPAddress(file.getAbsolutePath())) {
            return getIPAddress(file.getAbsolutePath());
        } else if (foundOpsCenter(file.getAbsolutePath())) {
            return ValFactory.OPSCENTERD;
        } else {
            return "Cannot find file ID information";
        }
    }

    public static boolean foundOpsCenter(String path) {
        if (path.contains(ValFactory.OPSCENTERD)) {
            return true;
        }
        return false;
    }

    public static String getStackTrace(Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }

    public static String[] splitBySpace(String input) {
        return input.split("\\s+");
    }

    public static String[] splitByComma(String input) {
        return input.split(",");
    }

    public static String[] splitByColon(String input) {
        return input.split(":");
    }

    public static String[] splitBySlash(String input) {
        return input.split("/");
    }

    public static String epochToDate(String input) {
        long epoch = Long.parseLong(input);
        Date generation = new Date(epoch * 1000L);
        return generation.toString();
    }

    public static String secToTime(int sec, boolean fullformat) {
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if (hours >= 24) {
                int days = hours / 24;
                if (fullformat) {
                    return String.format("%d days %02d hours %02d minutes %02d seconds", days, hours % 24, minutes, seconds);
                } else {
                    return String.format("%dd %02dh %02dm %02ds", days, hours % 24, minutes, seconds);
                }
            }
            if (fullformat) {
                return String.format("%02d hours %02d minutes %02d seconds", hours, minutes, seconds);
            } else {
                return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
            }
        }
        if (fullformat) {
            return String.format("%02d minutes %02d seconds", minutes, seconds);
        } else {
            return String.format("%02dm %02ds", minutes, seconds);
        }
    }

    public static String generateDotline(int length) {
        String dotinestr = new String();

        for (int i = 0; i < length; ++i) {
            dotinestr += "-";
        }
        return dotinestr;
    }

    public static String generateEqualline(int length) {
        String dotinestr = new String();

        for (int i = 0; i < length; ++i) {
            dotinestr += "=";
        }
        return dotinestr;
    }

    public static String saveReportFile(String file_content, String file_name) {
        String save_file_path = "";
        File report_file;
        try {

            String current_file_path_tmp = Nibbler.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath().toString();

            String current_file_path = new String();

            if (System.getProperty("os.name").toString().toLowerCase().equals("windows"))
                current_file_path = current_file_path_tmp.substring(0, current_file_path_tmp.lastIndexOf("\\") + 1);
            else {
                current_file_path = current_file_path_tmp.substring(0, current_file_path_tmp.lastIndexOf("/") + 1);
            }
            logger.info("save status file report to: " + current_file_path + file_name);

            report_file = new File(current_file_path + file_name);

            FileWriter report_file_writer = new FileWriter(report_file);

            report_file_writer.write(file_content);
            report_file_writer.close();
            save_file_path = report_file.getAbsolutePath();

        } catch (IOException e) {
            save_file_path = "error";
            e.printStackTrace();

        } catch (URISyntaxException e1) {
            save_file_path = "error";
            e1.printStackTrace();
        }

        return save_file_path;
    }

    public static void logException (Logger logger, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        logger.error("Exception: " + e);
        logger.error("StackTrace: " + stackTrace.toString());
    }

    public static double getDynamicTextAreaHeight(String input){

        Text text = new Text();
        text.setText(input);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screen_height = visualBounds.getHeight();
        double actual_height = text.getLayoutBounds().getHeight() * 1.4;

        double pref_height = screen_height * (3*ValFactory.SCREEN_HEIGHT_FACTOR/5);
        logger.info( " Textarea actual height is: " + actual_height + "  Pref height is: " + pref_height);

        return actual_height > pref_height? pref_height:actual_height;


    }
}

