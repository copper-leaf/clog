package com.caseyjbrooks.clog.log;

public interface ICommonClog {

    int loggerInternal(String logger, String message, Throwable throwable, Object... args);

    /**
     * Log a Throwable to a generic logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param logger the key of the logger to use
     * @param throwable the throwable to log
     * @return int
     */
    default int logger(String logger, Throwable throwable) {
        return loggerInternal(logger, null, null, throwable);
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
    default int logger(String logger, String message, Object... args) {
        return loggerInternal(logger, message, null, args);
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
    default int logger(String logger, String message, Throwable throwable, Object... args) {
        return loggerInternal(logger, message, throwable, args);
    }

}
