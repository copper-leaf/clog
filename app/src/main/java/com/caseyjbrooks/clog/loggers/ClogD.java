package com.caseyjbrooks.clog.loggers;

import android.util.Log;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;

public class ClogD implements ClogLogger, ClogFormatter {

    @Override
    public int log(String tag, String message) {
        return Log.d(tag, message);
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return Log.d(tag, message, throwable);
    }

    @Override
    public Object format(Object data, Object[] params) {
        Log.d("ClogD", data.toString());
        return data;
    }
}
