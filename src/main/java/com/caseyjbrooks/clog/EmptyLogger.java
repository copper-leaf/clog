package com.caseyjbrooks.clog;

/**
 * A default inactive logger. For performance in a production environment, loggers should be replaced by EmptyLogger or
 * a custom inactive logger.
 */
public class EmptyLogger implements ClogLogger {

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public int log(String tag, String message) {
        return 0;
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return 0;
    }

    @Override
    public int priority() {
        return 100;
    }
}
