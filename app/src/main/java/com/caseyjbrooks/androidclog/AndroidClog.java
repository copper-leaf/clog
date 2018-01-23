package com.caseyjbrooks.androidclog;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;
import com.caseyjbrooks.clog.EmptyLogger;
import com.caseyjbrooks.clog.parseltongue.Parseltongue;

import java.util.HashMap;

public class AndroidClog {

    public static Clog getDevelopmentClog() {
        ClogFormatter formatter = new Parseltongue();
        HashMap<String, ClogLogger> loggers = new HashMap<>();

        loggers.put(null,         new AndroidLogger(Clog.Priority.DEFAULT));
        loggers.put(Clog.KEY_V,   new AndroidLogger(Clog.Priority.VERBOSE));
        loggers.put(Clog.KEY_D,   new AndroidLogger(Clog.Priority.DEBUG));
        loggers.put(Clog.KEY_I,   new AndroidLogger(Clog.Priority.INFO));
        loggers.put(Clog.KEY_W,   new AndroidLogger(Clog.Priority.WARNING));
        loggers.put(Clog.KEY_E,   new AndroidLogger(Clog.Priority.ERROR));
        loggers.put(Clog.KEY_WTF, new AndroidLogger(Clog.Priority.FATAL));

        return new Clog(loggers, formatter);
    }

    public static Clog getProductionClog() {
        ClogFormatter formatter = new Parseltongue();
        HashMap<String, ClogLogger> loggers = new HashMap<>();
        loggers.put(null, new EmptyLogger());
        loggers.put("d", new EmptyLogger());
        loggers.put("e", new EmptyLogger());
        loggers.put("i", new EmptyLogger());
        loggers.put("v", new EmptyLogger());
        loggers.put("w", new EmptyLogger());
        loggers.put("wtf", new EmptyLogger());

        return new Clog(loggers, formatter);
    }
}
