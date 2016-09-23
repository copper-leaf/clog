package com.caseyjbrooks.androidclog;

import com.caseyjbrooks.clog.ClogLogger;

/**
 * A logger implementation that does nothing. If you are not sending your logs anywhere in production,
 * you should use this class so your logs do not pollute the global log buffers.
 */
public class EmptyClog implements ClogLogger {
    @Override
    public int log(String tag, String message) {
        return 0;
    }

    @Override
    public int log(String tag, String message, Throwable throwable) {
        return 0;
    }
}
