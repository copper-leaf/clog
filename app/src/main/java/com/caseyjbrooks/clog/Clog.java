package com.caseyjbrooks.clog;

import com.caseyjbrooks.clog.parseltongue.Parseltongue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Clog {
    public static final String KEY_V = "v";
    public static final String KEY_D = "d";
    public static final String KEY_I = "i";
    public static final String KEY_W = "w";
    public static final String KEY_E = "e";
    public static final String KEY_WTF = "wtf";

    private static final String CLASS_NAME = Clog.class.getName();
    private static HashMap<String, Clog> profiles;
    private static Clog instance;

    private HashMap<String, ClogLogger> loggers;
    private ClogFormatter formatter;
    private String defaultTag;
    private String lastTag;
    private String lastLog;

    static {
        profiles = new HashMap<>();
        profiles.put(null, new Clog());
    }

    /**
     * Get the instance of Clog to log to. If the instance is defined, use that profile, otherwise
     * create a new default profile.
     *
     * @return the current Clog profile instance
     */
    private static Clog getInstance() {
        if(instance == null) {
            if(profiles == null || profiles.get(null) == null) {
                instance = new Clog();
            }
            else {
                instance = profiles.get(null);
            }
        }

        return instance;
    }

    /**
     * Initialize Clog with the default configuration, using a simple logger and the Parseltongue formatter
     */
    private Clog() {
        loggers = new HashMap<>();
        loggers.put(null, new DefaultLogger());
        formatter = new Parseltongue();
    }

    /**
     * Initialize a custom Clog instance to be used as a custom profile
     *
     * @param loggers
     * @param formatter
     */
    public Clog(HashMap<String, ClogLogger> loggers, ClogFormatter formatter) {
        this.loggers = loggers;
        this.formatter = formatter;
    }

// Log messages with Clog
//--------------------------------------------------------------------------------------------------

    /**
     * Handy function to get a loggable stack trace from a Throwable. Copied from the Android
     * Open Source Project Log.java class.
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, false);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static String format(String message, Object... args) {
        return getInstance().formatter.format(message, args);
    }

    public static int log(Throwable throwable)                                               { return logger(null,         getStackTraceString(throwable));   }
    public static int   d(Throwable throwable)                                               { return logger(KEY_D,        getStackTraceString(throwable));   }
    public static int   e(Throwable throwable)                                               { return logger(KEY_E,        getStackTraceString(throwable));   }
    public static int   i(Throwable throwable)                                               { return logger(KEY_I,        getStackTraceString(throwable));   }
    public static int   v(Throwable throwable)                                               { return logger(KEY_V,        getStackTraceString(throwable));   }
    public static int   w(Throwable throwable)                                               { return logger(KEY_W,        getStackTraceString(throwable));   }
    public static int wtf(Throwable throwable)                                               { return logger(KEY_WTF,      getStackTraceString(throwable));   }

    public static int log(String tag, Throwable throwable)                                   { return logger(null,    tag, getStackTraceString(throwable));   }
    public static int   d(String tag, Throwable throwable)                                   { return logger(KEY_D,   tag, getStackTraceString(throwable));   }
    public static int   e(String tag, Throwable throwable)                                   { return logger(KEY_E,   tag, getStackTraceString(throwable));   }
    public static int   i(String tag, Throwable throwable)                                   { return logger(KEY_I,   tag, getStackTraceString(throwable));   }
    public static int   v(String tag, Throwable throwable)                                   { return logger(KEY_V,   tag, getStackTraceString(throwable));   }
    public static int   w(String tag, Throwable throwable)                                   { return logger(KEY_W,   tag, getStackTraceString(throwable));   }
    public static int wtf(String tag, Throwable throwable)                                   { return logger(KEY_WTF, tag, getStackTraceString(throwable));   }

    public static int log(String message, Object... args)                                    { return logger(null,         message,                    args); }
    public static int   d(String message, Object... args)                                    { return logger(KEY_D,        message,                    args); }
    public static int   e(String message, Object... args)                                    { return logger(KEY_E,        message,                    args); }
    public static int   i(String message, Object... args)                                    { return logger(KEY_I,        message,                    args); }
    public static int   v(String message, Object... args)                                    { return logger(KEY_V,        message,                    args); }
    public static int   w(String message, Object... args)                                    { return logger(KEY_W,        message,                    args); }
    public static int wtf(String message, Object... args)                                    { return logger(KEY_WTF,      message,                    args); }

    public static int log(String tag, String message, Object... args)                        { return logger(null,    tag, message,                    args); }
    public static int   d(String tag, String message, Object... args)                        { return logger(KEY_D,   tag, message,                    args); }
    public static int   e(String tag, String message, Object... args)                        { return logger(KEY_E,   tag, message,                    args); }
    public static int   i(String tag, String message, Object... args)                        { return logger(KEY_I,   tag, message,                    args); }
    public static int   v(String tag, String message, Object... args)                        { return logger(KEY_V,   tag, message,                    args); }
    public static int   w(String tag, String message, Object... args)                        { return logger(KEY_W,   tag, message,                    args); }
    public static int wtf(String tag, String message, Object... args)                        { return logger(KEY_WTF, tag, message,                    args); }

    public static int log(String message, Throwable throwable, Object... args)               { return logger(null,         message, throwable,         args); }
    public static int   d(String message, Throwable throwable, Object... args)               { return logger(KEY_D,        message, throwable,         args); }
    public static int   e(String message, Throwable throwable, Object... args)               { return logger(KEY_E,        message, throwable,         args); }
    public static int   i(String message, Throwable throwable, Object... args)               { return logger(KEY_I,        message, throwable,         args); }
    public static int   v(String message, Throwable throwable, Object... args)               { return logger(KEY_V,        message, throwable,         args); }
    public static int   w(String message, Throwable throwable, Object... args)               { return logger(KEY_W,        message, throwable,         args); }
    public static int wtf(String message, Throwable throwable, Object... args)               { return logger(KEY_WTF,      message, throwable,         args); }

    public static int log(String tag, String message, Throwable throwable, Object... args)   { return logger(null,    tag, message, throwable,         args); }
    public static int   d(String tag, String message, Throwable throwable, Object... args)   { return logger(KEY_D,   tag, message, throwable,         args); }
    public static int   e(String tag, String message, Throwable throwable, Object... args)   { return logger(KEY_E,   tag, message, throwable,         args); }
    public static int   i(String tag, String message, Throwable throwable, Object... args)   { return logger(KEY_I,   tag, message, throwable,         args); }
    public static int   v(String tag, String message, Throwable throwable, Object... args)   { return logger(KEY_V,   tag, message, throwable,         args); }
    public static int   w(String tag, String message, Throwable throwable, Object... args)   { return logger(KEY_W,   tag, message, throwable,         args); }
    public static int wtf(String tag, String message, Throwable throwable, Object... args)   { return logger(KEY_WTF, tag, message, throwable,         args); }

    /**
     * Format and log a message with the default tag to the specified logger. Passing null as 'logger'
     * will use the default logger.
     *
     * @param logger the key of the specific logger
     * @param message the string to be formatted and logged
     * @param args the list of objects to format into the format string
     * @return int
     */
    public static int logger(String logger, String message, Object... args) {
        return logger(logger, getTag(), message, args);
    }

    /**
     * Format and log a message and tag to the specified logger
     *
     * @param logger the key of the specific logger
     * @param tag the string to be used as a tag
     * @param message the string to be formatted and logged
     * @param args the list of objects to format into the format string
     * @return int
     */
    public static int logger(String logger, String tag, String message, Object... args) {
        return logger(logger, tag, message, null, args);
    }

    /**
     * Format and log a message, tag, and throwable to the specified logger
     *
     * @param logger the key of the specific logger
     * @param tag the string to be used as a tag
     * @param message the string to be formatted and logged
     * @param args the list of objects to format into the format string
     * @return int
     */
    public static int logger(String logger, String tag, String message, Throwable throwable, Object... args) {
        return getInstance().loggerInternal(logger, tag, message, throwable, args);
    }

    /**
     * Format and log a message, tag, and throwable to the specified logger
     *
     * @param logger the key of the specific logger
     * @param tag the string to be used as a tag
     * @param message the string to be formatted and logged
     * @param args the list of objects to format into the format string
     * @return int
     */
    private int loggerInternal(String logger, String tag, String message, Throwable throwable, Object... args) {
        if(loggers.containsKey(logger) && (loggers.get(logger) != null)) {
            if(loggers.get(logger).isActive()) {
                String formattedMessage = formatter.format(message, args);
                lastLog = formattedMessage;
                lastTag = tag;
                if (throwable != null) {
                    return loggers.get(logger).log(tag, formattedMessage, throwable);
                } else {
                    return loggers.get(logger).log(tag, formattedMessage);
                }
            }
        }
        else {
            if(loggers.get(null).isActive()) {
                String formattedMessage = formatter.format(message, args);
                lastLog = formattedMessage;
                lastTag = tag;
                if (throwable != null) {
                    return loggers.get(null).log(tag, formattedMessage, throwable);
                } else {
                    return loggers.get(null).log(tag, formattedMessage);
                }
            }
        }

        return 0;
    }

// Configure Clog
//--------------------------------------------------------------------------------------------------

    /**
     * Add a new Clog profile named 'key'
     *
     * @param key the key of the profile
     * @param clog the profile
     */
    public static void addProfile(String key, Clog clog) {
        profiles.put(key, clog);
    }

    /**
     * Remove the profile at 'key'
     *
     * @param key the key of the profile to remove
     */
    public static void removeProfile(String key) {
        profiles.remove(key);
    }

    /**
     * Keys the current profile to the one named 'key'
     *
     * @param key the key of the profile to use
     */
    public static void setCurrentProfile(String key) {
        Clog.instance = profiles.get(key);
    }

    /**
     * Add a new Clog profile named 'key', and then switches to that profile
     *
     * @param key
     * @param clog
     */
    public static void setCurrentProfile(String key, Clog clog) {
        addProfile(key, clog);
        setCurrentProfile(key);
    }

    /**
     * Returns all currently configured profiles
     *
     * @return the map of profiles
     */
    public static HashMap<String, Clog> getProfiles() {
        return profiles;
    }

    /**
     * Set all profiles
     *
     * @param profiles the profile set to use
     */
    public static void setAllProfiles(HashMap<String, Clog> profiles) {
        Clog.profiles = profiles;
    }



    /**
     * Get the loggers for the current profile
     *
     * @return the map of loggers
     */
    public static HashMap<String, ClogLogger> getLoggers() {
        return getInstance().loggers;
    }

    /**
     * Get the keys for all loggers in the current profile
     *
     * @return a list of String keys which map to loggers
     */
    public static ArrayList<String> getLoggerKeys() {
        return new ArrayList<>(getInstance().loggers.keySet());
    }

    /**
     * Replace all loggers in the current profile with a new set
     *
     * @param loggers the new hashmap of loggers
     */
    public static void setLoggers(HashMap<String, ClogLogger> loggers) {
        getInstance().loggers = loggers;
    }

    /**
     * Add a new logger to the current profile, replacing any existing logger already set with the key
     *
     * @param key the logger key
     * @param logger the logger to add or replace
     */
    public static void addLogger(String key, ClogLogger logger) {
        getInstance().loggers.put(key, logger);
    }

    /**
     * Sets the default logger to use with the current profile, replacing any existing default logger.
     * This default logger will be used in calls to Clog.log(...) or Clog.logger(null, ...)
     *
     * @param logger the logger to add or replace
     */
    public static void setDefaultLogger(ClogLogger logger) {
        getInstance().loggers.put(null, logger);
    }

    /**
     * Remove the logger at the specified key from the current profile
     *
     * @param key the key mapping to the logger to be removed
     */
    public static void removeLogger(String key) {
        getInstance().loggers.remove(key);
    }

    /**
     * Set the Clog formatter implementation to be used in the current profile
     *
     * @param formatter the formatter implementation to use
     */
    public static void setFormatter(ClogFormatter formatter) {
        getInstance().formatter = formatter;
    }

    /**
     * Get the Clog formatter implementation used in the current profile
     *
     * @return the current formatter
     */
    public static ClogFormatter getFormatter() {
        return getInstance().formatter;
    }

    /**
     * Get the tag from the most recently logged message in the current profile. Mostly used for testing.
     *
     * @return the tag on last message logged
     */
    public static String getLastTag() {
        return getInstance().lastTag;
    }

    /**
     * Get the most recently logged message in the current profile. Mostly used for testing.
     *
     * @return the last message logged
     */
    public static String getLastLog() {
        return getInstance().lastLog;
    }

// Set and get the default logger tag.
//--------------------------------------------------------------------------------------------------

    /**
     * Get the default tag to be used with all logging messages in the current profile. A default tag
     * of 'null' indicates that all logged messages will use the caller's simple class name as the tag.
     *
     * @return the default tag, or null
     */
    public static String getDefaultTag() {
        return getInstance().defaultTag;
    }

    /**
     * Set the default tag to be used with all logging messages in the current profile. A default tag
     * of 'null' indicates that all logged messages will use the caller's simple class name as the tag.
     *
     * @param defaultTag the default tag, or null
     */
    public static void setDefaultTag(String defaultTag) {
        getInstance().defaultTag = defaultTag;
    }

    /**
     * Get the default tag in the current profile. If the default tag is defined, use that,
     * otherwise attempt to find the caller simple class name and use that as the tag.
     *
     * @return the default tag
     */
    private static String getTag() {
        if(getInstance().defaultTag != null) {
            return getInstance().defaultTag;
        }
        else {
            return findCallerClassName();
        }
    }

    /**
     * Finds the external class name that directly called a Clog method. Copied from the Android
     * Open Source Project LogUtil.java class.
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
