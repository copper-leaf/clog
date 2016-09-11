package com.caseyjbrooks.clog;

import android.support.v4.util.Pair;
import android.util.Log;

import com.caseyjbrooks.clog.loggers.ClogD;
import com.caseyjbrooks.clog.loggers.ClogE;
import com.caseyjbrooks.clog.loggers.ClogI;
import com.caseyjbrooks.clog.loggers.ClogV;
import com.caseyjbrooks.clog.loggers.ClogW;
import com.caseyjbrooks.clog.loggers.ClogWTF;
import com.caseyjbrooks.clog.parsers.ClogStringFormatter;

import java.util.ArrayList;
import java.util.HashMap;

public class Clog {
    private static final String CLASS_NAME = Clog.class.getName();

    private static HashMap<String, ClogLogger> loggers = new HashMap<>();
    private static ClogParser parser = new ClogStringFormatter();
    private static String defaultTag = null;
    private static String lastTag;
    private static String lastLog;

    static {
        // initialize default loggers
        loggers = new HashMap<>();
        loggers.put("d", new ClogD());
        loggers.put("e", new ClogE());
        loggers.put("i", new ClogI());
        loggers.put("v", new ClogV());
        loggers.put("w", new ClogW());
        loggers.put("wtf", new ClogWTF());
    }

    private Clog() {
    }

// Log messages with Clog
//--------------------------------------------------------------------------------------------------

    public static int log(String formatString, Object... args)                                    { return logger(null,       formatString,            args); }
    public static int   d(String formatString, Object... args)                                    { return logger("d",        formatString,            args); }
    public static int   e(String formatString, Object... args)                                    { return logger("e",        formatString,            args); }
    public static int   i(String formatString, Object... args)                                    { return logger("i",        formatString,            args); }
    public static int   v(String formatString, Object... args)                                    { return logger("v",        formatString,            args); }
    public static int   w(String formatString, Object... args)                                    { return logger("w",        formatString,            args); }
    public static int wtf(String formatString, Object... args)                                    { return logger("wtf",      formatString,            args); }

    public static int log(String formatString, Throwable throwable, Object... args)               { return logger(null,       formatString, throwable, args); }
    public static int   d(String formatString, Throwable throwable, Object... args)               { return logger("d",        formatString, throwable, args); }
    public static int   e(String formatString, Throwable throwable, Object... args)               { return logger("e",        formatString, throwable, args); }
    public static int   i(String formatString, Throwable throwable, Object... args)               { return logger("i",        formatString, throwable, args); }
    public static int   v(String formatString, Throwable throwable, Object... args)               { return logger("v",        formatString, throwable, args); }
    public static int   w(String formatString, Throwable throwable, Object... args)               { return logger("w",        formatString, throwable, args); }
    public static int wtf(String formatString, Throwable throwable, Object... args)               { return logger("wtf",      formatString, throwable, args); }

    public static int log(String tag, String formatString, Object... args)                        { return logger(null,  tag, formatString,            args); }
    public static int   d(String tag, String formatString, Object... args)                        { return logger("d",   tag, formatString,            args); }
    public static int   e(String tag, String formatString, Object... args)                        { return logger("e",   tag, formatString,            args); }
    public static int   i(String tag, String formatString, Object... args)                        { return logger("i",   tag, formatString,            args); }
    public static int   v(String tag, String formatString, Object... args)                        { return logger("v",   tag, formatString,            args); }
    public static int   w(String tag, String formatString, Object... args)                        { return logger("w",   tag, formatString,            args); }
    public static int wtf(String tag, String formatString, Object... args)                        { return logger("wtf", tag, formatString,            args); }

    public static int log(String tag, String formatString, Throwable throwable, Object... args)   { return logger(null,  tag, formatString, throwable, args); }
    public static int   d(String tag, String formatString, Throwable throwable, Object... args)   { return logger("d",   tag, formatString, throwable, args); }
    public static int   e(String tag, String formatString, Throwable throwable, Object... args)   { return logger("e",   tag, formatString, throwable, args); }
    public static int   i(String tag, String formatString, Throwable throwable, Object... args)   { return logger("i",   tag, formatString, throwable, args); }
    public static int   v(String tag, String formatString, Throwable throwable, Object... args)   { return logger("v",   tag, formatString, throwable, args); }
    public static int   w(String tag, String formatString, Throwable throwable, Object... args)   { return logger("w",   tag, formatString, throwable, args); }
    public static int wtf(String tag, String formatString, Throwable throwable, Object... args)   { return logger("wtf", tag, formatString, throwable, args); }

    public static int   w(String tag, Throwable throwable)                                        { return logger("wtf", tag, getStackTraceString(throwable)); }
    public static int wtf(String tag, Throwable throwable)                                        { return logger("wtf", tag, Log.getStackTraceString(throwable)); }

    public static String getStackTraceString(Throwable throwable) { return Log.getStackTraceString(throwable); }
    public static boolean isLoggable(String tag, int level) { return Log.isLoggable(tag, level); }
    public static int println(int priority, String tag, String message) { return Log.println(priority, tag, message); }

    /**
     * Format and log a message with the default tag to the specified logger. Passing null as 'logger'
     * will use the default logger.
     *
     * @param logger the key of the specific logger
     * @param formatString the string to be formatted and logged
     * @param args the list of objects to format into the format string
     * @return int
     */
    public static int logger(String logger, String formatString, Object... args) {
        return logger(logger, getTag(), formatString, args);
    }

    /**
     * Format and log a message and tag to the specified logger
     *
     * @param logger the key of the specific logger
     * @param tag the string to be used as a tag
     * @param formatString the string to be formatted and logged
     * @param args the list of objects to format into the format string
     * @return int
     */
    public static int logger(String logger, String tag, String formatString, Object... args) {
        return logger(logger, tag, formatString, null, args);
    }

    /**
     * Format and log a message, tag, and throwable to the specified logger
     *
     * @param logger the key of the specific logger
     * @param tag the string to be used as a tag
     * @param formatString the string to be formatted and logged
     * @param args the list of objects to format into the format string
     * @return int
     */
    public static int logger(String logger, String tag, String formatString, Throwable throwable, Object... args) {
        String formattedMessage = parser.format(formatString, args);
        lastLog = formattedMessage;
        lastTag = tag;

        if(loggers.containsKey(logger) && (loggers.get(logger) != null)) {
            if(throwable != null) {
                return loggers.get(logger).log(tag, formattedMessage, throwable);
            }
            else {
                return loggers.get(logger).log(tag, formattedMessage);
            }
        }
        else {
            if(throwable != null) {
                return new ClogW().log(tag, formattedMessage + " (cannot find logger with tag '" + logger + "')", throwable);
            }
            else {
                return new ClogW().log(tag, formattedMessage + " (cannot find logger with tag '" + logger + "')");
            }
        }
    }

// Configure Clog
//--------------------------------------------------------------------------------------------------

    /**
     * Get the currently set loggers
     *
     * @return the map of loggers
     */
    public static HashMap<String, ClogLogger> getLoggers() {
        return loggers;
    }

    /**
     * Get the keys for all currently set loggers
     *
     * @return a list of String keys which map to loggers
     */
    public static ArrayList<String> getLoggerKeys() {
        return new ArrayList<>(loggers.keySet());
    }

    /**
     * Replace all currently set loggers with a new set
     *
     * @param loggers the new hashmap of loggers
     */
    public static void setLoggers(HashMap<String, ClogLogger> loggers) {
        Clog.loggers = loggers;
    }

    /**
     * Add a new logger, replacing any existing logger already set with the key
     *
     * @param key the logger key
     * @param logger the logger to add or replace
     */
    public static void addLogger(String key, ClogLogger logger) {
        Clog.loggers.put(key, logger);
    }

    /**
     * Sets the default logger, replacing any existing default logger. This default logger will be
     * used in calls to Clog.log(...) or Clog.logger(null, ...)
     *
     * @param logger the logger to add or replace
     */
    public static void setDefaultLogger(ClogLogger logger) {
        Clog.loggers.put(null, logger);
    }

    /**
     * Remove the logger at the specified key
     *
     * @param key the key mapping to the logger to be removed
     */
    public static void removeLogger(String key) {
        Clog.loggers.remove(key);
    }

    /**
     * Get the current Clog parser implementation
     *
     * @return the current parser
     */
    public static ClogParser getParser() {
        return parser;
    }

    /**
     * Set the current Clog parser implementation
     *
     * @param parser the parser implementation to use
     */
    public static void setParser(ClogParser parser) {
        Clog.parser = parser;
    }

    /**
     * Get the default tag to be used with all logging messages. A default tag of 'null' indicates
     * that all logged messages will use the caller's simple class name as the tag.
     *
     * @return the default tag, or null
     */
    public static String getDefaultTag() {
        return defaultTag;
    }

    /**
     * Set the default tag to be used with all logging messages. A default tag of 'null' indicates
     * that all logged messages will use the caller's simple class name as the tag.
     *
     * @param defaultTag the default tag, or null
     */
    public static void setDefaultTag(String defaultTag) {
        Clog.defaultTag = defaultTag;
    }

    /**
     * Get the most recently logged tag and message. The tag is the first item in the pair, and the
     * message is the second item in the pair.
     *
     * @return a pair of the last tag and log as 'Pair(lastTag, lastLog)'
     */
    public static Pair<String, String> getLastLog() {
        return new Pair<>(lastTag, lastLog);
    }

    // Set and get the default logger tag.
//--------------------------------------------------------------------------------------------------
    /**
     * Get the default tag to log. If the default tag is defined, use that, otherwise attempt to
     * find the caller simple class name and use that as the tag
     *
     * @return the default tag
     */
    private static String getTag() {
        if(defaultTag == null) {
            return findCallerClassName();
        }
        else {
            return defaultTag;
        }
    }

    /**
     * Finds the external class name that directly called a CLog method. Copied from the Android
     * Open Source Project LogUtil.java class.
     *
     * See {@link <a href="https://android.googlesource.com/platform/tools/tradefederation/+/master/src/com/android/tradefed/log/LogUtil.java#324">LogUtil.java#324</a>}.
     *
     * @return The simple class name (or full-qualified if an error occurs getting a ref to
     *         the class) of the external class that called a CLog method, or "Unknown" if
     *         the stack trace is empty or only contains CLog class names.
     */
    private static String findCallerClassName() {
        return findCallerClassName(null);
    }
    /**
     * Finds the external class name that directly called a CLog method. Copied from the Android
     * Open Source Project LogUtil.java class.
     *
     * See {@link <a href="https://android.googlesource.com/platform/tools/tradefederation/+/master/src/com/android/tradefed/log/LogUtil.java#336">LogUtil.java#336</a>}.
     *
     * @param t (Optional) the stack trace to search within, exposed for unit testing
     * @return The simple class name (or full-qualified if an error occurs getting a ref to
     *         the class) of the external class that called a CLog method, or "Unknown" if
     *         the stack trace is empty or only contains CLog class names.
     */
    private static String findCallerClassName(Throwable t) {
        String className = "Unknown";
        if (t == null) {
            t = new Throwable();
        }
        StackTraceElement[] frames = t.getStackTrace();
        if (frames.length == 0) {
            return className;
        }
        // starting with the first frame's class name (this CLog class)
        // keep iterating until a frame of a different class name is found
        int f;
        for (f = 0; f < frames.length; f++) {
            className = frames[f].getClassName();
            if (!className.equals(CLASS_NAME)) {
                break;
            }
        }
        return parseClassName(className);
    }

    /**
     * Parses the simple class name out of the full class name. If the formatting already
     * looks like a simple class name, then just returns that. Copied from the Android
     * Open Source Project LogUtil.java class.
     *
     * See {@link <a href="https://android.googlesource.com/platform/tools/tradefederation/+/master/src/com/android/tradefed/log/LogUtil.java#368">LogUtil.java#368</a>}.
     *
     * @param fullName the full class name to parse
     * @return The simple class name
     */
    private static String parseClassName(String fullName) {
        int lastdot = fullName.lastIndexOf('.');
        String simpleName = fullName;
        if (lastdot != -1) {
            simpleName = fullName.substring(lastdot + 1);
        }
        // handle inner class names
        int lastdollar = simpleName.lastIndexOf('$');
        if (lastdollar != -1) {
            simpleName = simpleName.substring(0, lastdollar);
        }
        return simpleName;
    }
}
