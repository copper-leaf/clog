package com.caseyjbrooks.androidclog;

import android.util.Log;

import com.caseyjbrooks.clog.ClogLogger;

public class ClogV implements ClogLogger {

    @Override
    public int log(String tag, String message) {
        return Log.v(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.v(tag, message, throwable);
    }
}
