package com.caseyjbrooks.clog;

import com.caseyjbrooks.clog.parseltongue.Parseltongue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ClogTest {
    @Test
    public void testBasicLogging() throws Exception {

        HashMap<String, ClogLogger> profileOneLoggers = new HashMap<>();
        profileOneLoggers.put(null, new DefaultLogger(Clog.Priority.DEFAULT));
        ClogFormatter profileOneFormatter = new Parseltongue();
        Clog profileOne = new Clog(profileOneLoggers, profileOneFormatter);
        Clog.addProfile(null, profileOne);
        Clog.setCurrentProfile(null);

        String lastLog;
        String lastTag;

        // test a basic log, which will use the caller class name as the tag
        Clog.i("message");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        assertEquals("ClogTest", lastTag);
        assertEquals("message", lastLog);

        // test a basic log with the default tag set
        Clog.pushTag("Clog");

        Clog.i("message");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        assertEquals(lastTag, "Clog");
        assertEquals(lastLog, "message");

        // test a basic log with the default tag set and the tag in the call manually set. The logging
        // tag should override the default tag
        Clog.pushTag("tag");
        Clog.i("message");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        assertEquals(lastTag, "tag");
        assertEquals(lastLog, "message");

        Clog.popTag();
        lastTag = Clog.getCurrentTag();
        assertEquals(lastTag, "Clog");

        Clog.popTag();
        lastTag = Clog.getCurrentTag();
        assertEquals(lastTag, "ClogTest");
    }

    @Test
    public void testFormattedLogging() throws Exception {
        String lastLog;

        ArrayList<String> names = new ArrayList<>();
        names.add("Harry");
        names.add("Ron");
        names.add("Hermione");
        names.add("Fred and George");

        Clog.i("#{ $1 | join(', ') | lowercase } references back to #{ @1 | uppercase }", names);
        lastLog = Clog.getLastLog();
        assertEquals(lastLog, "harry, ron, hermione, fred and george references back to HARRY, RON, HERMIONE, FRED AND GEORGE");
    }

    @Test
    public void testMultipleProfiles() throws Exception {
        String lastLog;
        int numberOfLoggers;

        HashMap<String, ClogLogger> profileOneLoggers = new HashMap<>();
        HashMap<String, ClogLogger> profileTwoLoggers = new HashMap<>();
        HashMap<String, ClogLogger> profileThreeLoggers = new HashMap<>();

        profileOneLoggers.put(null, new DefaultLogger());

        profileTwoLoggers.put(null, new DefaultLogger());
        profileTwoLoggers.put("i", new DefaultLogger());
        profileTwoLoggers.put("d", new DefaultLogger());
        profileTwoLoggers.put("e", new DefaultLogger());

        profileThreeLoggers.put(null, new DefaultLogger());
        profileThreeLoggers.put("i", new DefaultLogger());
        profileThreeLoggers.put("d", new DefaultLogger());
        profileThreeLoggers.put("e", new DefaultLogger());
        profileThreeLoggers.put("v", new DefaultLogger());
        profileThreeLoggers.put("w", new DefaultLogger());
        profileThreeLoggers.put("wtf", new DefaultLogger());

        ClogFormatter profileOneFormatter = new Parseltongue();
        ClogFormatter profileTwoFormatter = new Parseltongue();
        ClogFormatter profileThreeFormatter = new Parseltongue();

        Clog profileOne = new Clog(profileOneLoggers, profileOneFormatter);
        Clog profileTwo = new Clog(profileTwoLoggers, profileTwoFormatter);
        Clog profileThree = new Clog(profileThreeLoggers, profileThreeFormatter);

        Clog.addProfile("one", profileOne);
        Clog.addProfile("two", profileTwo);
        Clog.addProfile("three", profileThree);

        // as a proof of concept, I will log a message on each, then since each profile holds its
        // own last logged message, I will go back and check the messages of each profile, and also
        // check the number of loggers in each profile
        Clog.setCurrentProfile("one");
        Clog.i("Message One");

        Clog.setCurrentProfile("two");
        Clog.i("Message Two");

        Clog.setCurrentProfile("three");
        Clog.i("Message Three");

        Clog.setCurrentProfile("one");
        lastLog = Clog.getLastLog();

        numberOfLoggers = Clog.getLoggers().size();

        assertEquals(lastLog, "Message One");
        assertEquals(numberOfLoggers, 1);
        Clog.setCurrentProfile("two");
        lastLog = Clog.getLastLog();

        numberOfLoggers = Clog.getLoggers().size();
        assertEquals(lastLog, "Message Two");
        assertEquals(numberOfLoggers, 4);

        Clog.setCurrentProfile("three");
        lastLog = Clog.getLastLog();
        numberOfLoggers = Clog.getLoggers().size();
        assertEquals(lastLog, "Message Three");
        assertEquals(numberOfLoggers, 7);

        //go back to profile one and check again for good measure
        Clog.setCurrentProfile("one");
        lastLog = Clog.getLastLog();
        numberOfLoggers = Clog.getLoggers().size();
        assertEquals(lastLog, "Message One");
        assertEquals(numberOfLoggers, 1);
    }

    @Test
    public void testFiltering() {

        HashMap<String, ClogLogger> profileOneLoggers = new HashMap<>();
        profileOneLoggers.put(null, new DefaultLogger());
        ClogFormatter profileOneFormatter = new Parseltongue();
        Clog profileOne = new Clog(profileOneLoggers, profileOneFormatter);
        Clog.addProfile(null, profileOne);
        Clog.setCurrentProfile(null);

        String lastLog;

        // test a basic log, which will use the caller class name as the tag
        Clog.i("message");
        lastLog = Clog.getLastLog();
        assertEquals("message", lastLog);

        Clog.addTagToWhitelist("TestTag");
        Clog.flush();

        Clog.pushTag("TestTag");
        Clog.i("message");
        lastLog = Clog.getLastLog();
        assertEquals("message", lastLog);
        Clog.popTag();

        Clog.addTagToBlacklist("TestTag");
        Clog.flush();

        Clog.pushTag("TestTag");
        Clog.i("message");
        lastLog = Clog.getLastLog();
        assertEquals(null, lastLog);
        Clog.popTag();

        Clog.clearTagWhitelist();
        Clog.clearTagBlacklist();

        Clog.clearLoggerWhitelist();
        Clog.clearLoggerBlacklist();
    }

    @Test
    public void testAnsiCodes() {

        // Test that output from the default logger can show colors
        HashMap<String, ClogLogger> loggers = new HashMap<>();
        loggers.put(null,  new DefaultLogger());
        loggers.put(Clog.KEY_V, new DefaultLogger(Clog.Priority.VERBOSE));
        loggers.put(Clog.KEY_D, new DefaultLogger(Clog.Priority.DEBUG));
        loggers.put(Clog.KEY_I, new DefaultLogger(Clog.Priority.INFO));
        loggers.put(Clog.KEY_W, new DefaultLogger(Clog.Priority.WARNING));
        loggers.put(Clog.KEY_E, new DefaultLogger(Clog.Priority.ERROR));
        loggers.put(Clog.KEY_WTF, new DefaultLogger(Clog.Priority.FATAL));

        Clog clog = new Clog(loggers, new Parseltongue());

        Clog.setCurrentProfile("test2", clog);

        Clog.v("Log me, dog!");
        Clog.d("Log me, dog!");
        Clog.i("Log me, dog!");
        Clog.w("Log me, dog!");
        Clog.e("Log me, dog!");
        Clog.wtf("Log me, dog!");

        //test that the color spell works

        Clog.log("Log #{$0 | fg('BLUE') }me,#{$0 | reset } dog!");
    }

    @Test
    public void testPriorities() {

        // Test that output from the default logger can show colors
        HashMap<String, ClogLogger> loggers = new HashMap<>();
        loggers.put(null,  new DefaultLogger());
        loggers.put(Clog.KEY_V, new DefaultLogger(Clog.Priority.VERBOSE));
        loggers.put(Clog.KEY_D, new DefaultLogger(Clog.Priority.DEBUG));
        loggers.put(Clog.KEY_I, new DefaultLogger(Clog.Priority.INFO));
        loggers.put(Clog.KEY_W, new DefaultLogger(Clog.Priority.WARNING));
        loggers.put(Clog.KEY_E, new DefaultLogger(Clog.Priority.ERROR));
        loggers.put(Clog.KEY_WTF, new DefaultLogger(Clog.Priority.FATAL));

        Clog clog = new Clog(loggers, new Parseltongue());
        Clog.setCurrentProfile("test3", clog);


        Clog.pushTag("testPriorities");

        // Test using known priorities
        clog.setMinPriority(Clog.Priority.WARNING);
        Clog.i("Test using known priorities");
        assertEquals(null, Clog.getLastLog());

        clog.setMinPriority(Clog.Priority.DEBUG);
        Clog.i("Test using known priorities");
        assertEquals("Test using known priorities", Clog.getLastLog());
        Clog.flush();

        // Test by using Strings to find priorities by name
        clog.setMinPriority(Clog.Priority.getByKey("warning"));
        Clog.i("Test by using Strings to find priorities by name");
        assertEquals(null, Clog.getLastLog());

        clog.setMinPriority(Clog.Priority.getByKey("debug"));
        Clog.i("Test by using Strings to find priorities by name");
        assertEquals("Test by using Strings to find priorities by name", Clog.getLastLog());
        Clog.flush();

        // Test by using Strings to find priorities by key
        clog.setMinPriority(Clog.Priority.getByKey(Clog.KEY_W));
        Clog.i("Test by using Strings to find priorities by key");
        assertEquals(null, Clog.getLastLog());

        clog.setMinPriority(Clog.Priority.getByKey(Clog.KEY_D));
        Clog.i("Test by using Strings to find priorities by key");
        assertEquals("Test by using Strings to find priorities by key", Clog.getLastLog());
        Clog.flush();

        Clog.popTag();
    }
}
