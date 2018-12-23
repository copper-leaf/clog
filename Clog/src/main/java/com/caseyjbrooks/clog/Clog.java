package com.caseyjbrooks.clog;

import java.util.HashMap;
import java.util.Map;

public class Clog {

    public static final String KEY_V = "v";
    public static final String KEY_D = "d";
    public static final String KEY_I = "i";
    public static final String KEY_W = "w";
    public static final String KEY_E = "e";
    public static final String KEY_WTF = "wtf";

    public enum Priority {
        VERBOSE(KEY_V,   1),
        DEBUG(  KEY_D,   2),
        INFO(   KEY_I,   3),
        DEFAULT(null,    4),
        WARNING(KEY_E,   5),
        ERROR(  KEY_E,   6),
        FATAL(  KEY_WTF, 7);

        private final int priority;
        private final String key;

        Priority(String key, int priority) {
            this.key = key;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public String getKey() {
            return key;
        }

        public static Priority getByKey(String key) {
            for(Priority priority : Priority.values()) {
                if(priority.toString().equalsIgnoreCase(key) || (priority.getKey() == null && key == null) || (priority.getKey() != null && priority.getKey().equalsIgnoreCase(key))) {
                    return priority;
                }
            }

            return Priority.DEFAULT;
        }
    }

    private static Map<String, ProfileSupplier> profileSuppliers;
    private static Map<String, ClogProfile> profiles;
    private static String currentProfileKey;
    private static ClogProfile currentProfile;

    /**
     * Get the instance of Clog to log to. If the instance is defined, use that profile, otherwise
     * create a new default profile.
     *
     * @return the current Clog profile instance
     */
    public static ClogProfile getInstance() {
        if (currentProfile == null) {
            if (profiles == null || profiles.get(currentProfileKey) == null) {
                if (profileSuppliers != null && profileSuppliers.get(currentProfileKey) != null) {
                    currentProfile = profileSuppliers.get(currentProfileKey).get();
                    profiles.put(currentProfileKey, currentProfile);
                }
                else {
                    currentProfile = new ClogProfile();
                }
            } else {
                currentProfile = profiles.get(null);
            }
        }

        return currentProfile;
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
        return getInstance().getStackTraceString(tr);
    }

    public static String format(String message, Object... args) {
        return getInstance().format(message, args);
    }

    public static ClogProfile tag(String tag) {
        return getInstance().tag(tag);
    }

    public static ClogProfile noTag() {
        return getInstance().tag("");
    }

// Generic key-valued logging calls
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to a generic logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param logger the key of the logger to use
     * @param throwable the throwable to log
     * @return int
     */
    public static int logger(String logger, Throwable throwable) {
        return getInstance().logger(logger, throwable);
    }

    /**
     * Log a formatted message to a generic logger. Will use the default tag, which if not specified
     * will be the calling class's simple name.
     *
     * @param logger the key of the logger to use
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int logger(String logger, String message, Object... args) {
        return getInstance().logger(logger, message, args);
    }

    /**
     * Log a formatted message and a Throwable to a generic logger. Will use the default tag, which
     * if not specified will be the calling class's simple name.
     *
     * @param logger the key of the logger to use
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int logger(String logger, String message, Throwable throwable, Object... args) {
        return getInstance().logger(logger, message, throwable, args);
    }

// Default logging calls. Calls to generic logger function with key 'null'
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to the default logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    public static int log(Throwable throwable) {
        return getInstance().log(throwable);
    }

    /**
     * Log a formatted message to the default logger. Will use the default tag, which if not specified
     * will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int log(String message, Object... args) {
        return getInstance().log(message, args);
    }

    /**
     * Log a formatted message and a Throwable to the default logger. Will use the default tag, which
     * if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int log(String message, Throwable throwable, Object... args) {
        return getInstance().log(message, throwable, args);
    }

// Verbose-level logging calls. Calls to generic logger function with key 'v'
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to the 'verbose' logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    public static int v(Throwable throwable) {
        return getInstance().v(throwable);
    }

    /**
     * Log a formatted message to the 'verbose' logger. Will use the default tag, which if not specified
     * will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int v(String message, Object... args) {
        return getInstance().v(message, args);
    }

    /**
     * Log a formatted message and a Throwable to the 'verbose' logger. Will use the default tag, which
     * if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int v(String message, Throwable throwable, Object... args) {
        return getInstance().v(message, throwable, args);
    }

// Debug-level logging calls. Calls to generic logger function with key 'd'
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to the 'debug' logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    public static int d(Throwable throwable) {
        return getInstance().d(throwable);
    }

    /**
     * Log a formatted message to the 'debug' logger. Will use the default tag, which if not specified
     * will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int d(String message, Object... args) {
        return getInstance().d(message, args);
    }

    /**
     * Log a formatted message and a Throwable to the 'debug' logger. Will use the default tag, which
     * if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int d(String message, Throwable throwable, Object... args) {
        return getInstance().d(message, throwable, args);
    }

// Info-level logging calls. Calls to generic logger function with key 'i'
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to the 'info' logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    public static int i(Throwable throwable) {
        return getInstance().i(throwable);
    }

    /**
     * Log a formatted message to the 'info' logger. Will use the default tag, which if not specified
     * will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int i(String message, Object... args) {
        return getInstance().i(message, args);
    }

    /**
     * Log a formatted message and a Throwable to the 'info' logger. Will use the default tag, which
     * if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int i(String message, Throwable throwable, Object... args) {
        return getInstance().i(message, throwable, args);
    }

// Warning-level logging calls. Calls to generic logger function with key 'w'
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to the 'warning' logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    public static int w(Throwable throwable) {
        return getInstance().w(throwable);
    }

    /**
     * Log a formatted message to the 'warning' logger. Will use the default tag, which if not specified
     * will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int w(String message, Object... args) {
        return getInstance().w(message, args);
    }

    /**
     * Log a formatted message and a Throwable to the 'warning' logger. Will use the default tag, which
     * if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int w(String message, Throwable throwable, Object... args) {
        return getInstance().w(message, throwable, args);
    }

// Error-level logging calls. Calls to generic logger function with key 'e'
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to the 'error' logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    public static int e(Throwable throwable) {
        return getInstance().e(throwable);
    }

    /**
     * Log a formatted message to the 'error' logger. Will use the default tag, which if not specified
     * will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int e(String message, Object... args) {
        return getInstance().e(message, args);
    }

    /**
     * Log a formatted message and a Throwable to the 'error' logger. Will use the default tag, which
     * if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int e(String message, Throwable throwable, Object... args) {
        return getInstance().e(message, throwable, args);
    }

// Fatal-level (What a Terrible Failure) logging calls. Calls to generic logger function with key 'wtf'
//--------------------------------------------------------------------------------------------------

    /**
     * Log a Throwable to the WTF (what a terrible failure!) logger. Will print the stack trace of
     * the throwable as the log message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    public static int wtf(Throwable throwable) {
        return getInstance().wtf(throwable);
    }

    /**
     * Log a formatted message to the WTF (what a terrible failure!) logger. Will use the default
     * tag, which if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param args (optional) the arguments to pass to the formatter
     * @return int
     */
    public static int wtf(String message, Object... args) {
        return getInstance().wtf(message, args);
    }

    /**
     * Log a formatted message and a Throwable to the WTF (what a terrible failure!) logger. Will
     * use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param message the message to log. Should be marked up with your chosen formatter's markup,
     *                with objects passed in as varargs
     * @param throwable the throwable to log
     * @param args (optional) the arguments to pass to the formatter
     */
    public static int wtf(String message, Throwable throwable, Object... args) {
        return getInstance().wtf(message, throwable, args);
    }

// Configure Clog
//--------------------------------------------------------------------------------------------------

    /**
     * Add a new Clog profile named 'key'
     *
     * @param key  the key of the profile
     * @param clog the profile
     */
    public static void addProfile(String key, ProfileSupplier clog) {
        if(profileSuppliers == null) profileSuppliers = new HashMap<>();
        profileSuppliers.put(key, clog);
    }

    /**
     * Remove the profile at 'key'
     *
     * @param key the key of the profile to remove
     */
    public static void removeProfile(String key) {
        if(profiles == null) profiles = new HashMap<>();
        profiles.remove(key);
    }

    /**
     * Keys the current profile to the one named 'key'
     *
     * @param key the key of the profile to use
     */
    public static ClogProfile setCurrentProfile(String key) {
        if(profiles == null) profiles = new HashMap<>();
        Clog.currentProfileKey = key;
        Clog.currentProfile = profiles.get(key);
        return getInstance();
    }

    /**
     * Add a new Clog profile named 'key', and then switches to that profile
     *
     * @param key
     * @param clog
     */
    public static ClogProfile setCurrentProfile(String key, ProfileSupplier clog) {
        addProfile(key, clog);
        return setCurrentProfile(key);
    }

    /**
     * Returns all currently configured profiles
     *
     * @return the map of profiles
     */
    public static Map<String, ClogProfile> getProfiles() {
        if(profiles == null) profiles = new HashMap<>();
        return profiles;
    }

    /**
     * Set all profiles
     *
     * @param profiles the profile set to use
     */
    public static void setAllProfiles(Map<String, ClogProfile> profiles) {
        if(profiles == null) throw new NullPointerException("profiles cannot be null");
        Clog.profiles = profiles;
    }
}
