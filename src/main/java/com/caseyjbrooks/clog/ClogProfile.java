package com.caseyjbrooks.clog;

import com.caseyjbrooks.clog.log.IClog;
import com.caseyjbrooks.clog.log.ICommonClog;
import com.caseyjbrooks.clog.log.IDebugClog;
import com.caseyjbrooks.clog.log.IDefaultClog;
import com.caseyjbrooks.clog.log.IErrorClog;
import com.caseyjbrooks.clog.log.IFatalClog;
import com.caseyjbrooks.clog.log.IInfoClog;
import com.caseyjbrooks.clog.log.IVerboseClog;
import com.caseyjbrooks.clog.log.IWarningClog;
import com.caseyjbrooks.clog.parseltongue.Parseltongue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ClogProfile implements IClog {

    private Map<String, ClogLogger> loggers;
    private ClogFormatter formatter;
    private String lastTag;
    private String lastLog;

    private String temporaryTag;
    private Stack<String> tagStack;

    private List<String> tagWhitelist;
    private List<String> tagBlacklist;

    private List<String> loggerWhitelist;
    private List<String> loggerBlacklist;

    private Clog.Priority minPriority;
    private Clog.Priority maxPriority;

    /**
     * Initialize Clog with the default configuration, using a simple logger and the Parseltongue formatter
     */
    public ClogProfile() {
        temporaryTag = null;
        tagStack = new Stack<>();
        loggers = new HashMap<>();
        loggers.put(null,  new DefaultLogger(  Clog.Priority.DEFAULT));
        loggers.put(Clog.KEY_V, new DefaultLogger(  Clog.Priority.VERBOSE));
        loggers.put(Clog.KEY_D, new DefaultLogger(  Clog.Priority.DEBUG));
        loggers.put(Clog.KEY_I, new DefaultLogger(  Clog.Priority.INFO));
        loggers.put(Clog.KEY_W, new DefaultLogger(  Clog.Priority.WARNING));
        loggers.put(Clog.KEY_E, new DefaultLogger(  Clog.Priority.ERROR));
        loggers.put(Clog.KEY_WTF, new DefaultLogger(Clog.Priority.FATAL));
        formatter = new Parseltongue();

        tagWhitelist = new ArrayList<>();
        tagBlacklist = new ArrayList<>();

        loggerWhitelist = new ArrayList<>();
        loggerBlacklist = new ArrayList<>();

        minPriority = null;
        maxPriority = null;
    }

    /**
     * Initialize a custom Clog instance to be used as a custom profile
     *
     * @param loggers
     * @param formatter
     */
    public ClogProfile(Map<String, ClogLogger> loggers, ClogFormatter formatter) {
        this.tagStack = new Stack<>();
        this.loggers = loggers;
        this.formatter = formatter;

        tagWhitelist = new ArrayList<>();
        tagBlacklist = new ArrayList<>();

        loggerWhitelist = new ArrayList<>();
        loggerBlacklist = new ArrayList<>();

        minPriority = null;
        maxPriority = null;
    }

// Log messages with Clog
//--------------------------------------------------------------------------------------------------

    /**
     * Handy function to get a loggable stack trace from a Throwable. Copied from the Android
     * Open Source Project Log.java class.
     *
     * @param tr An exception to log
     */
    public String getStackTraceString(Throwable tr) {
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

    public String format(String message, Object... args) {
        return formatter.format(message, args);
    }

    public int loggerInternal(String logger, String message, Throwable throwable, Object... args) {
        ClogLogger currentLogger = null;
        String currentTag = getTag();
        this.temporaryTag = null;
        String currentMessage;

        // check tag against the whitelist and blacklist
        boolean inWhitelist = false;
        for(String whiteListedTag : tagWhitelist) {
            if(currentTag.equals(whiteListedTag)) {
                inWhitelist = true;
                break;
            }
        }

        boolean inBlacklist = false;
        for(String blackListedTag : tagBlacklist) {
            if(currentTag.equals(blackListedTag)) {
                inBlacklist = true;
                break;
            }
        }

        if((tagWhitelist.size() > 0 && !inWhitelist) || (tagBlacklist.size() > 0 && inBlacklist)) {
            return 0;
        }

        // get a logger to log to
        if (loggers != null) {
            if (loggers.containsKey(logger)) {
                currentLogger = loggers.get(logger);
            }

            if (currentLogger == null) {
                if (loggers.containsKey(null)) {
                    currentLogger = loggers.get(null);
                }
            }

            if (currentLogger == null) {
                currentLogger = new DefaultLogger();
            }
        } else {
            currentLogger = new DefaultLogger();
        }

        // check logger against the whitelist, blacklist, and priority levels
        inWhitelist = false;
        for(String whiteListedLogger : loggerWhitelist) {
            if(logger == null) {
                if(whiteListedLogger == null) {
                    inWhitelist = true;
                    break;
                }
            }
            else if(logger.equals(whiteListedLogger)) {
                inWhitelist = true;
                break;
            }
        }

        inBlacklist = false;
        for(String blackListedLogger : loggerBlacklist) {
            if(logger == null) {
                if(blackListedLogger == null) {
                    inBlacklist = true;
                    break;
                }
            }
            else if(logger.equals(blackListedLogger)) {
                inBlacklist = true;
                break;
            }
        }

        if((loggerWhitelist.size() > 0 && !inWhitelist) || (loggerBlacklist.size() > 0 && inBlacklist)) {
            return 0;
        }

        if(minPriority != null && currentLogger.priority().getPriority() < minPriority.getPriority()) {
            return 0;
        }

        if(maxPriority != null && currentLogger.priority().getPriority() > maxPriority.getPriority()) {
            return 0;
        }

        if (currentLogger.isActive()) {
            if (message != null) {
                currentMessage = formatter.format(message, args);
            } else {
                if (throwable != null) {
                    currentMessage = getStackTraceString(throwable);
                } else {
                    currentMessage = "";
                }
            }

            if (throwable == null) {
                lastTag = currentTag;
                lastLog = currentMessage;
                return currentLogger.log(currentTag, currentMessage);
            } else {
                lastTag = currentTag;
                lastLog = currentMessage;
                return currentLogger.log(currentTag, currentMessage, throwable);
            }
        }

        return 0;
    }

// Configure Clog
//--------------------------------------------------------------------------------------------------

    /**
     * Get the loggers for the current profile
     *
     * @return the map of loggers
     */
    public Map<String, ClogLogger> getLoggers() {
        return this.loggers;
    }

    /**
     * Get the keys for all loggers in the current profile
     *
     * @return a list of String keys which map to loggers
     */
    public List<String> getLoggerKeys() {
        return new ArrayList<>(this.loggers.keySet());
    }

    /**
     * Replace all loggers in the current profile with a new set
     *
     * @param loggers the new map of loggers
     */
    public void setLoggers(Map<String, ClogLogger> loggers) {
        this.loggers = loggers;
    }

    /**
     * Add a new logger to the current profile, replacing any existing logger already set with the key
     *
     * @param key    the logger key
     * @param logger the logger to add or replace
     */
    public void addLogger(String key, ClogLogger logger) {
        this.loggers.put(key, logger);
    }

    /**
     * Remove the logger at the specified key from the current profile
     *
     * @param key the key mapping to the logger to be removed
     */
    public void removeLogger(String key) {
        this.loggers.remove(key);
    }

    /**
     * Set the Clog formatter implementation to be used in the current profile
     *
     * @param formatter the formatter implementation to use
     */
    public void setFormatter(ClogFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Get the Clog formatter implementation used in the current profile
     *
     * @return the current formatter
     */
    public ClogFormatter getFormatter() {
        return this.formatter;
    }

    /**
     * Get the tag from the most recently logged message in the current profile. Mostly used for testing.
     *
     * @return the tag on last message logged
     */
    public String getLastTag() {
        return this.lastTag;
    }

    /**
     * Get the most recently logged message in the current profile. Mostly used for testing.
     *
     * @return the last message logged
     */
    public String getLastLog() {
        return this.lastLog;
    }

    /**
     * Flushes the last tag and last log from the current profile. Mostly used for testing.
     */
    public void flush() {
        this.lastTag = null;
        this.lastLog = null;
    }

    /**
     * Adds the tag to a whitelist. Only logging messages whose tag is in the whitelist will be logged. If the whitelist
     * is empty, no tag whitelist filtering will be done.
     *
     * @param tag  the tag to whitelist
     */
    public void addTagToWhitelist(String tag) {
        this.tagWhitelist.add(tag);
    }

    /**
     * Adds the tag to a blacklist. Only logging messages whose tag is not in the blacklist will be logged. If the
     * blacklist is empty, no tag blacklist filtering will be done.
     *
     * @param tag  the tag to blacklist
     */
    public void addTagToBlacklist(String tag) {
        this.tagBlacklist.add(tag);
    }

    /**
     * Clears the tag whitelist of all entries.
     */
    public void clearTagWhitelist() {
        this.tagWhitelist.clear();
    }

    /**
     * Clears the tag blacklist of all entries.
     */
    public void clearTagBlacklist() {
        this.tagBlacklist.clear();
    }

    /**
     * Adds the logger to a whitelist. Only logging messages whose logger is in the whitelist will be logged. If the
     * whitelist is empty, no logger whitelist filtering will be done.
     *
     * @param tag  the tag to whitelist
     */
    public void addLoggerToWhitelist(String tag) {
        this.loggerWhitelist.add(tag);
    }

    /**
     * Adds the tag to a blacklist. Only logging messages whose logger is not in the blacklist will be logged. If the
     * blacklist is empty, no logger blacklist filtering will be done.
     *
     * @param tag  the tag to blacklist
     */
    public void addLoggerToBlacklist(String tag) {
        this.loggerBlacklist.add(tag);
    }

    /**
     * Clears the logger whitelist of all entries.
     */
    public void clearLoggerWhitelist() {
        this.loggerWhitelist.clear();
    }

    /**
     * Clears the logger blacklist of all entries.
     */
    public void clearLoggerBlacklist() {
        this.loggerBlacklist.clear();
    }

    /**
     * Sets the minimum priority of loggers to log messages from
     *
     * @param minPriority  the minimum priority to log
     */
    public void setMinPriority(Clog.Priority minPriority) {
        this.minPriority = minPriority;
    }

    /**
     * Sets the maximum priority of loggers to log messages from
     *
     * @param maxPriority  the maximum priority to log
     */
    public void setMaxPriority(Clog.Priority maxPriority) {
        this.maxPriority = maxPriority;
    }

// Set and get the default logger tag.
//--------------------------------------------------------------------------------------------------

    public ClogProfile tag(String tag) {
        this.temporaryTag = tag;

        return this;
    }

    /**
     * Set the tag to use for subsequent logging calls. Be sure to push the tag as close the logging call as possible,
     * and to pop this tag off the stack when finished.
     *
     * @param tag the tag to use
     */
    public void pushTag(String tag) {
        this.tagStack.push(tag);
    }

    /**
     * Remove the latest tag from the tag stack.
     */
    public void popTag() {
        this.tagStack.pop();
    }

    /**
     * Remove the current tag from the tag stack.
     */
    public String getCurrentTag() {
        return getTag();
    }

    /**
     * Get the default tag in the current profile. If the default tag is defined, use that,
     * otherwise attempt to find the caller simple class name and use that as the tag.
     *
     * @return the default tag
     */
    private String getTag() {
        if (this.temporaryTag != null) {
            return this.temporaryTag;
        }
        else if (this.tagStack.size() > 0) {
            return this.tagStack.peek();
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
     * the class) of the external class that called a CLog method, or "Unknown" if
     * the stack trace is empty or only contains CLog class names.
     */
    private String findCallerClassName() {
        return findCallerClassName(null);
    }

    /**
     * Finds the external class name that directly called a CLog method. Copied from the Android
     * Open Source Project LogUtil.java class.
     *
     * @param t (Optional) the stack trace to search within, exposed for unit testing
     * @return The simple class name (or full-qualified if an error occurs getting a ref to
     * the class) of the external class that called a CLog method, or "Unknown" if
     * the stack trace is empty or only contains CLog class names.
     */
    private String findCallerClassName(Throwable t) {
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
            if (!isClassnameInternal(className)) {
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
    private String parseClassName(String fullName) {
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

    protected boolean isClassnameInternal(String className) {
        String[] classNames = new String[] {
                Clog.class.getName(),
                this.getClass().getName(),
                ClogProfile.class.getName(),
                IClog.class.getName(),
                ICommonClog.class.getName(),
                IDebugClog.class.getName(),
                IDefaultClog.class.getName(),
                IErrorClog.class.getName(),
                IFatalClog.class.getName(),
                IInfoClog.class.getName(),
                IVerboseClog.class.getName(),
                IWarningClog.class.getName()
        };

        for(String s : classNames) {
            if(className.equals(s)) {
                return true;
            }
        }

        return false;
    }
}
