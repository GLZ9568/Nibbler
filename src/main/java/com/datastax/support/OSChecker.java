/*
 * Copyright (c)
 *
 * Date: 23/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support;

import org.apache.commons.lang3.SystemUtils;

/**
 * Created by Chun Gao on 23/11/2017
 */

public final class OSChecker {

    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }
}