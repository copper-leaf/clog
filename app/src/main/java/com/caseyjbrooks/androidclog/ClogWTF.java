package com.caseyjbrooks.androidclog;

import android.util.Log;

import com.caseyjbrooks.clog.ClogLogger;

public class ClogWTF implements ClogLogger {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int log(String tag, String message) {
        return Log.wtf(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.wtf(tag, message, throwable);
    }
}
