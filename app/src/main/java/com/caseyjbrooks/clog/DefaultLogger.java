package com.caseyjbrooks.clog;

import com.caseyjbrooks.clog.ClogLogger;

public class DefaultLogger implements ClogLogger {

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
}
