package com.caseyjbrooks.androidclog;

import android.content.Context;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;
import com.caseyjbrooks.clog.ClogProfile;
import com.caseyjbrooks.clog.EmptyLogger;
import com.caseyjbrooks.clog.ProfileSupplier;
import com.caseyjbrooks.clog.parseltongue.Parseltongue;

import java.util.HashMap;

public class AndroidClog {

    public static ClogProfile getDevelopmentClog() {
        ClogFormatter formatter = new Parseltongue();
        HashMap<String, ClogLogger> loggers = new HashMap<>();

        loggers.put(null,         new AndroidLogger(Clog.Priority.DEFAULT));
        loggers.put(Clog.KEY_V,   new AndroidLogger(Clog.Priority.VERBOSE));
        loggers.put(Clog.KEY_D,   new AndroidLogger(Clog.Priority.DEBUG));
        loggers.put(Clog.KEY_I,   new AndroidLogger(Clog.Priority.INFO));
        loggers.put(Clog.KEY_W,   new AndroidLogger(Clog.Priority.WARNING));
        loggers.put(Clog.KEY_E,   new AndroidLogger(Clog.Priority.ERROR));
        loggers.put(Clog.KEY_WTF, new AndroidLogger(Clog.Priority.FATAL));

        return new ClogProfile(loggers, formatter);
    }

    public static ClogProfile getProductionClog() {
        ClogFormatter formatter = new Parseltongue();
        HashMap<String, ClogLogger> loggers = new HashMap<>();

        loggers.put(null,         new EmptyLogger());
        loggers.put(Clog.KEY_V,   new EmptyLogger());
        loggers.put(Clog.KEY_D,   new EmptyLogger());
        loggers.put(Clog.KEY_I,   new EmptyLogger());
        loggers.put(Clog.KEY_W,   new EmptyLogger());
        loggers.put(Clog.KEY_E,   new EmptyLogger());
        loggers.put(Clog.KEY_WTF, new EmptyLogger());

        return new ClogProfile(loggers, formatter);
    }

    public static void init(Context context, boolean debug) {
        if(debug) {
            Clog.setCurrentProfile("debug", new ProfileSupplier() {
                @Override
                public ClogProfile get() {
                    return getDevelopmentClog();
                }
            });
        }
        else {
            Clog.setCurrentProfile("prod", new ProfileSupplier() {
                @Override
                public ClogProfile get() {
                    return getProductionClog();
                }
            });
        }
    }

}
