package com.caseyjbrooks.clog;

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ClogTest {
    @Test
    public void testBasicLogging() throws Exception {

        Pair<String, String> lastLog;

        // test a basic log. It should log this class name as the tag, and the exact message as the message
        Clog.i("message");
        lastLog = Clog.getLastLog();
        assertEquals(lastLog.first, "ClogTest");
        assertEquals(lastLog.second, "message");

        // test a basic log. It should log the given tag as the tag, and the exact message as the message
        Clog.i("tag", "message");
        lastLog = Clog.getLastLog();
        assertEquals(lastLog.first, "tag");
        assertEquals(lastLog.second, "message");

        Clog.setDefaultTag("Clog");

        // test a basic log. It should log 'Clog' the tag, and the exact message as the message
        Clog.i("message");
        lastLog = Clog.getLastLog();
        assertEquals(lastLog.first, "Clog");
        assertEquals(lastLog.second, "message");

        // test a basic log. It should log the given tag as the tag, and the exact message as the message
        Clog.i("tag", "message");
        lastLog = Clog.getLastLog();
        assertEquals(lastLog.first, "tag");
        assertEquals(lastLog.second, "message");
    }

    @Test
    public void testFormattedLogging() throws Exception {
        Clog.setDefaultTag(null);

        Pair<String, String> lastLog;

        ArrayList<String> names = new ArrayList<>();
        names.add("Harry");
        names.add("Ron");
        names.add("Hermione");
        names.add("Fred and George");

        Clog.i("#{ $1 | join(', ') | lowercase } references back to #{ @1 | uppercase }", names);
        lastLog = Clog.getLastLog();
        assertEquals(lastLog.first, "ClogTest");
        assertEquals(lastLog.second, "harry, ron, hermione, fred and george references back to HARRY, RON, HERMIONE, FRED AND GEORGE");
    }


}
