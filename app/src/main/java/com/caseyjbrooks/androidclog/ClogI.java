package com.caseyjbrooks.androidclog;

import android.util.Log;

import com.caseyjbrooks.clog.ClogLogger;

public class ClogI implements ClogLogger {

    @Override
    public int log(String tag, String message) {
        return Log.i(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.i(tag, message, throwable);
    }
}
