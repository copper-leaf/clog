package com.caseyjbrooks.clog;

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
