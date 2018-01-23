package com.caseyjbrooks.androidclog;

import android.util.Log;
import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogLogger;

public class AndroidLogger implements ClogLogger {

    private Clog.Priority priority;

    public AndroidLogger() {
        this(Clog.Priority.DEFAULT);
    }

    public AndroidLogger(Clog.Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int log(String tag, String message) {
        switch (priority) {
            case VERBOSE: return Log.v(tag,   message);
            case DEBUG:   return Log.d(tag,   message);
            case INFO:    return Log.i(tag,   message);
            case WARNING: return Log.w(tag,   message);
            case ERROR:   return Log.e(tag,   message);
            case FATAL:   return Log.wtf(tag, message);
            case DEFAULT:
            default:      return Log.i(tag,   message);
        }
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        switch (priority) {
            case VERBOSE: return Log.v(tag,   message, throwable);
            case DEBUG:   return Log.d(tag,   message, throwable);
            case INFO:    return Log.i(tag,   message, throwable);
            case WARNING: return Log.w(tag,   message, throwable);
            case ERROR:   return Log.e(tag,   message, throwable);
            case FATAL:   return Log.wtf(tag, message, throwable);
            case DEFAULT:
            default:      return Log.i(tag,   message, throwable);
        }
    }

    @Override
    public Clog.Priority priority() {
        return priority;
    }

}
