package com.caseyjbrooks.androidclog;

import android.util.Log;

import com.caseyjbrooks.clog.ClogLogger;

public class ClogW implements ClogLogger {

    @Override
    public int log(String tag, String message) {
        return Log.w(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.w(tag, message, throwable);
    }
}
