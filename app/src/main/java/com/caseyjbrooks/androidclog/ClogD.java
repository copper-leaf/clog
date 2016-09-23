package com.caseyjbrooks.androidclog;

import android.util.Log;

import com.caseyjbrooks.clog.ClogLogger;

public class ClogD implements ClogLogger {

    @Override
    public int log(String tag, String message) {
        return Log.d(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.d(tag, message, throwable);
    }
}
