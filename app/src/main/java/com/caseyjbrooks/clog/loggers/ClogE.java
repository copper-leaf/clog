package com.caseyjbrooks.clog.loggers;

import android.util.Log;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;

public class ClogE implements ClogLogger, ClogFormatter {

    @Override
    public int log(String tag, String message) {
        return Log.e(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.e(tag, message, throwable);
    }

    @Override
    public Object format(Object data, Object[] params) {
        Log.e("ClogE", data.toString());
        return data;
    }
}
