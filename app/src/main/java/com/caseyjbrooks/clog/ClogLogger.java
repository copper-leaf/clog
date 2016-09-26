package com.caseyjbrooks.clog;

public interface ClogLogger {
    boolean isActive();
    int log(String tag, String message);
    int log(String tag, String message, Throwable throwable);
}
