package com.caseyjbrooks.clog;

public class DefaultLogger implements ClogLogger {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int log(String tag, String message) {
        System.out.println(tag + ": " + message);
        return 0;
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        System.out.println(tag + ": " + message + " (" + throwable.getMessage() + ")");
        return 0;
    }

    @Override
    public int priority() {
        return 100;
    }
}
