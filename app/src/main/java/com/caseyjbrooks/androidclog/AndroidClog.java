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
        loggers.put(null, new ClogI());
        loggers.put("d", new ClogD());
        loggers.put("e", new ClogE());
        loggers.put("i", new ClogI());
        loggers.put("v", new ClogV());
        loggers.put("w", new ClogW());
        loggers.put("wtf", new ClogWTF());

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
