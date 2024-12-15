package com.verve.assignment.uniquerequests.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UniqueCountLoggerUtil {

    private static final Logger LOG = LoggerFactory.getLogger("CountLogger");

    private UniqueCountLoggerUtil() {}

    public static void logCount(long count) {
       LOG.info("{}", count);
    }
}