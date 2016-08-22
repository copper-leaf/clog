package com.caseyjbrooks.clog.loggers;

import android.util.Log;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;

public class ClogW implements ClogLogger, ClogFormatter {

    @Override
    public int log(String tag, String message) {
        return Log.w(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.w(tag, message, throwable);
    }

    @Override
    public Object format(Object data, Object[] params) {
        Log.w("ClogW", data.toString());
        return data;
    }
}
