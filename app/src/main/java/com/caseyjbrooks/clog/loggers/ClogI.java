package com.caseyjbrooks.clog.loggers;

import android.util.Log;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;

public class ClogI implements ClogLogger, ClogFormatter {

    @Override
    public int log(String tag, String message) {
        return Log.i(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.i(tag, message, throwable);
    }

    @Override
    public Object format(Object data, Object[] params) {
        Log.i("ClogI", data.toString());
        return data;
    }
}
