/*
 * Copyright (c)
 *
 * Date: 17/11/2017.
 *
 * Author: Chun Gao & Mike Zhang
 */

package com.datastax.support;

import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Chun Gao on 17/11/17
 *
 * Read corrisponding files and then
 * 1. extract the information from the files
 * 2. analyze the extracted information
 * 3. output the extracted information and recommendations
 */

public interface Analyzer {

    // get value from valueparser
    public void getValues(ValueParser vp);

    // read a single file
    public void readFile(FileReader fr);

    // read a list of files
    public void readFiles(ArrayList<FileReader> fr);

    // analyze the data
    public void analyze();

    // return an int value
    public int getInt();

    // return a list of integers
    public ArrayList<Integer> getIntList();

    // return a String
    public String getString();

    // return a list of Strings
    public ArrayList<String> getStringList();
}
