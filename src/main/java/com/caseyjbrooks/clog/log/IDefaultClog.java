package com.caseyjbrooks.clog.log;

public interface IDefaultClog extends ICommonClog {

    /**
     * Log a Throwable to the 'debug' logger. Will print the stack trace of the throwable as the log
     * message, and use the default tag, which if not specified will be the calling class's simple name.
     *
     * @param throwable the throwable to log
     * @return int
     */
    default int log(Throwable throwable) {
        return logger(null, throwable);
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
    default int log(String message, Object... args) {
        return logger(null, message, args);
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
    default int log(String message, Throwable throwable, Object... args) {
        return logger(null, message, throwable, args);
    }

}
