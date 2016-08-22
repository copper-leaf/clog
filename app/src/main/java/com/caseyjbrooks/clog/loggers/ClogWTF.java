package com.caseyjbrooks.clog.loggers;

import android.util.Log;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;

public class ClogWTF implements ClogLogger, ClogFormatter {

    @Override
    public int log(String tag, String message) {
        return Log.wtf(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.wtf(tag, message, throwable);
    }

    @Override
    public Object format(Object data, Object[] params) {
        Log.wtf("ClogWTF", data.toString());
        return data;
    }
}
