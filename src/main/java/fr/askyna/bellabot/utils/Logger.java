package fr.askyna.bellabot.utils;

import org.slf4j.LoggerFactory;

public class Logger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("BellaBot");

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Throwable t){
        logger.info(message);
    }



    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Throwable t) {
        logger.error(message, t);
    }



    public static void warn(String message) {
        logger.warn(message);
    }

    public static void warn(String message, Throwable t) {
        logger.warn(message, t);
    }



    public static void debug(String message) {
        logger.debug(message);
    }

    public static void debug(String message, Throwable t) {
        logger.debug(message, t);
    }


    public static void fatal(String message) {
        logger.error("A CRITICAL ERROR AS OCCURRED: " + message);
    }

    public static void fatal(String message, Throwable t) {
        logger.error("A CRITICAL ERROR AS OCCURRED: " + message, t);
    }
}