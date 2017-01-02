package com.caseyjbrooks.clog;

/**
 * Denotes a class that can handle messages from Clog.
 */
public interface ClogLogger {

    /**
     * Whether this logger actually does anything. For optimization, messages are not formatted if the selected Logger
     * is inactive.
     *
     * @return  whether this logger is active
     */
    boolean isActive();

    /**
     * Log a message with a given tag.
     *
     * @param tag  the tag, used to filter which messages should be logged
     * @param message  the message logged
     * @return  ignored, exists for caompatibility with Android Log class
     */
    int log(String tag, String message);

    /**
     * Log an exception with a message and a given tag.
     *
     * @param tag  the tag, used to filter which messages should be logged
     * @param message  the message logged
     * @param throwable  the Exception to log
     * @return  ignored, exists for compatibility with Android Log class
     */
    int log(String tag, String message, Throwable throwable);

    /**
     * The priority of this Logger. Priorities are used to filter loggers by only showing specific levels of messages, for
     * example only messages of priority ERROR or higher.
     *
     * @return  the priority of this Logger
     */
    int priority();
}
