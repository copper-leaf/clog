package com.caseyjbrooks.androidclog;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;
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
        loggers.put(null, new EmptyClog());
        loggers.put("d", new EmptyClog());
        loggers.put("e", new EmptyClog());
        loggers.put("i", new EmptyClog());
        loggers.put("v", new EmptyClog());
        loggers.put("w", new EmptyClog());
        loggers.put("wtf", new EmptyClog());

        return new Clog(loggers, formatter);
    }
}
