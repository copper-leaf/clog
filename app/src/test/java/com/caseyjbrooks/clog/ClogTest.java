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
        profileOneLoggers.put(null, new DefaultLogger());
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
        assertEquals(lastTag, "ClogTest");
        assertEquals(lastLog, "message");

        // test a basic log with the tag manually set, which should override the default class name
        // as the tag
        Clog.i("tag", "message");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        assertEquals(lastTag, "tag");
        assertEquals(lastLog, "message");

        // test a basic log with the default tag set
        Clog.setDefaultTag("Clog");

        Clog.i("message");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        assertEquals(lastTag, "Clog");
        assertEquals(lastLog, "message");

        // test a basic log with the default tag set and the tag in the call manually set. The logging
        // tag should override the default tag
        Clog.i("tag", "message");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        assertEquals(lastTag, "tag");
        assertEquals(lastLog, "message");

        Clog.setDefaultTag(null);
    }

    @Test
    public void testFormattedLogging() throws Exception {
        String lastLog;
        String lastTag;

        ArrayList<String> names = new ArrayList<>();
        names.add("Harry");
        names.add("Ron");
        names.add("Hermione");
        names.add("Fred and George");

        Clog.i("#{ $1 | join(', ') | lowercase } references back to #{ @1 | uppercase }", names);
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        assertEquals(lastTag, "ClogTest");
        assertEquals(lastLog, "harry, ron, hermione, fred and george references back to HARRY, RON, HERMIONE, FRED AND GEORGE");
    }

    @Test
    public void testMultipleProfiles() throws Exception {
        String lastLog;
        String lastTag;
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
        Clog.i("Tag One", "Message One");

        Clog.setCurrentProfile("two");
        Clog.i("Tag Two", "Message Two");

        Clog.setCurrentProfile("three");
        Clog.i("Tag Three", "Message Three");

        Clog.setCurrentProfile("one");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        numberOfLoggers = Clog.getLoggers().size();
        assertEquals(lastTag, "Tag One");
        assertEquals(lastLog, "Message One");
        assertEquals(numberOfLoggers, 1);

        Clog.setCurrentProfile("two");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        numberOfLoggers = Clog.getLoggers().size();
        assertEquals(lastTag, "Tag Two");
        assertEquals(lastLog, "Message Two");
        assertEquals(numberOfLoggers, 4);

        Clog.setCurrentProfile("three");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        numberOfLoggers = Clog.getLoggers().size();
        assertEquals(lastTag, "Tag Three");
        assertEquals(lastLog, "Message Three");
        assertEquals(numberOfLoggers, 7);

        //go back to profile one and check again for good measure
        Clog.setCurrentProfile("one");
        lastLog = Clog.getLastLog();
        lastTag = Clog.getLastTag();
        numberOfLoggers = Clog.getLoggers().size();
        assertEquals(lastTag, "Tag One");
        assertEquals(lastLog, "Message One");
        assertEquals(numberOfLoggers, 1);
    }
}
