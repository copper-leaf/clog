package com.caseyjbrooks.clog;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ClogProfileTest {

    private ClogProfile underTest;
    private ClogFormatter mockFormatter;
    private Map<String, ClogLogger> mockLoggers;
    private ClogLogger mockLoggerAtNull;
    private ClogLogger mockActiveLogger;
    private ClogLogger mockInactiveLogger;
    private String mockActiveLoggerKey;
    private String mockInactiveLoggerKey;

    private String tag;
    private String message;
    private Throwable throwable;
    private Object[] args;

    @Before
    public void setup() {
        mockActiveLoggerKey = "active";
        mockInactiveLoggerKey = "inactive";
        tag = "tag";
        message = "message";
        throwable = new Throwable("throwable");
        args = new Object[] {"one", 2, "3"};

        mockFormatter = mock(ClogFormatter.class);
        mockLoggerAtNull = mock(ClogLogger.class);
        mockActiveLogger = mock(ClogLogger.class);
        mockInactiveLogger = mock(ClogLogger.class);
        mockLoggers = new HashMap<>();
        mockLoggers.put(null, mockLoggerAtNull);
        mockLoggers.put(mockActiveLoggerKey, mockActiveLogger);
        mockLoggers.put(mockInactiveLoggerKey, mockInactiveLogger);
        underTest = spy(new ClogProfile(mockLoggers, mockFormatter));

        when(mockLoggerAtNull.isActive()).thenReturn(true);
        when(mockActiveLogger.isActive()).thenReturn(true);
        when(mockInactiveLogger.isActive()).thenReturn(false);
        when(mockFormatter.format(message)).thenReturn(message);
        when(mockFormatter.format(message, args)).thenReturn(message);
    }

    @Test
    public void testFormatter() throws Exception {
        underTest.format(message, args);
        verify(mockFormatter, times(1)).format(message, args);
    }

    @Test
    public void testGetters() throws Exception {
        assertThat(underTest.getLoggers(), is(sameInstance(mockLoggers)));
        assertThat(underTest.getFormatter(), is(sameInstance(mockFormatter)));
        assertThat(underTest.getLoggerKeys(), containsInAnyOrder(null, mockActiveLoggerKey, mockInactiveLoggerKey));
    }

    @Test
    public void testSetters() throws Exception {
        underTest.setLoggers(null);
        assertThat(underTest.getLoggers(), is(nullValue()));
        underTest.setLoggers(mockLoggers);
        assertThat(underTest.getLoggers(), is(sameInstance(mockLoggers)));

        underTest.setFormatter(null);
        assertThat(underTest.getFormatter(), is(nullValue()));
        underTest.setFormatter(mockFormatter);
        assertThat(underTest.getFormatter(), is(sameInstance(mockFormatter)));
    }

    @Test
    public void testAddRemoveLoggers() throws Exception {
        assertThat(underTest.getLoggers().values(), containsInAnyOrder(mockLoggerAtNull, mockActiveLogger, mockInactiveLogger));

        underTest.removeLogger(null);
        assertThat(underTest.getLoggers().values(), containsInAnyOrder(mockActiveLogger, mockInactiveLogger));
        underTest.addLogger(null, mockLoggerAtNull);
        assertThat(underTest.getLoggers().values(), containsInAnyOrder(mockLoggerAtNull, mockActiveLogger, mockInactiveLogger));

        underTest.removeLogger(mockActiveLoggerKey);
        assertThat(underTest.getLoggers().values(), containsInAnyOrder(mockLoggerAtNull, mockInactiveLogger));
        underTest.addLogger(mockActiveLoggerKey, mockActiveLogger);
        assertThat(underTest.getLoggers().values(), containsInAnyOrder(mockLoggerAtNull, mockActiveLogger, mockInactiveLogger));

        underTest.removeLogger(mockInactiveLoggerKey);
        assertThat(underTest.getLoggers().values(), containsInAnyOrder(mockLoggerAtNull, mockActiveLogger));
        underTest.addLogger(mockInactiveLoggerKey, mockInactiveLogger);
        assertThat(underTest.getLoggers().values(), containsInAnyOrder(mockLoggerAtNull, mockActiveLogger, mockInactiveLogger));
    }

    @Test
    public void testLoggingNullNoThrowable() throws Exception {
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());
        verify(mockLoggerAtNull, never()).log(anyString(), anyString(), (Throwable) any());
        verify(mockActiveLogger,  never()).log(anyString(), anyString());
        verify(mockActiveLogger,  never()).log(anyString(), anyString(), (Throwable) any());
    }

    @Test
    public void testLoggingNullThrowable() throws Exception {
        underTest.loggerInternal(null, message, throwable, args);
        verify(mockLoggerAtNull, never()).log(anyString(), anyString());
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString(), (Throwable) any());
        verify(mockActiveLogger,  never()).log(anyString(), anyString());
        verify(mockActiveLogger,  never()).log(anyString(), anyString(), (Throwable) any());
    }

    @Test
    public void testLoggingActiveNoThrowable() throws Exception {
        underTest.loggerInternal(mockActiveLoggerKey, message, null, args);
        verify(mockLoggerAtNull, never()).log(anyString(), anyString());
        verify(mockLoggerAtNull, never()).log(anyString(), anyString(), (Throwable) any());
        verify(mockActiveLogger,  times(1)).log(anyString(), anyString());
        verify(mockActiveLogger,  never()).log(anyString(), anyString(), (Throwable) any());
    }

    @Test
    public void testLoggingActiveThrowable() throws Exception {
        underTest.loggerInternal(mockActiveLoggerKey, message, throwable, args);
        verify(mockLoggerAtNull, never()).log(anyString(), anyString());
        verify(mockLoggerAtNull, never()).log(anyString(), anyString(), (Throwable) any());
        verify(mockActiveLogger,  never()).log(anyString(), anyString());
        verify(mockActiveLogger,  times(1)).log(anyString(), anyString(), (Throwable) any());
    }

    @Test
    public void testLoggingInactiveNoThrowable() throws Exception {
        underTest.loggerInternal(mockInactiveLoggerKey, message, null, args);
        verify(mockInactiveLogger, never()).log(anyString(), anyString());
        verify(mockInactiveLogger, never()).log(anyString(), anyString(), (Throwable) any());
    }

    @Test
    public void testLoggingInactiveThrowable() throws Exception {
        underTest.loggerInternal(mockActiveLoggerKey, message, throwable, args);
        verify(mockInactiveLogger, never()).log(anyString(), anyString());
        verify(mockInactiveLogger, never()).log(anyString(), anyString(), (Throwable) any());
    }

    @Test
    public void testMinPriority() throws Exception {
        when(mockLoggerAtNull.priority()).thenReturn(Clog.Priority.ERROR);

        underTest.setMinPriority(Clog.Priority.FATAL);
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, never()).log(anyString(), anyString());

        underTest.setMinPriority(Clog.Priority.WARNING);
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());
    }

    @Test
    public void testMaxPriority() throws Exception {
        when(mockLoggerAtNull.priority()).thenReturn(Clog.Priority.ERROR);

        underTest.setMaxPriority(Clog.Priority.WARNING);
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, never()).log(anyString(), anyString());

        underTest.setMaxPriority(Clog.Priority.FATAL);
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());
    }

    @Test
    public void testWhitelistedLogger() throws Exception {
        underTest.addLoggerToWhitelist(mockActiveLoggerKey);
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, never()).log(anyString(), anyString());

        underTest.addLoggerToWhitelist(null);
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());

        when(mockInactiveLogger.isActive()).thenReturn(true);

        underTest.loggerInternal(mockInactiveLoggerKey, message, null, args);
        verify(mockInactiveLogger, never()).log(anyString(), anyString());

        underTest.clearLoggerWhitelist();
        underTest.loggerInternal(mockInactiveLoggerKey, message, null, args);
        verify(mockInactiveLogger, times(1)).log(anyString(), anyString());
    }

    @Test
    public void testBlacklistedLogger() throws Exception {
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());

        underTest.addLoggerToBlacklist(null);
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());

        underTest.clearLoggerBlacklist();
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(2)).log(anyString(), anyString());
    }

    @Test
    public void testWhitelistedTag() throws Exception {
        underTest.addTagToWhitelist("one");
        underTest.pushTag("two");

        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, never()).log(anyString(), anyString());

        underTest.addTagToWhitelist("two");
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());

        underTest.pushTag("three");
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());

        underTest.clearTagWhitelist();
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(2)).log(anyString(), anyString());
    }

    @Test
    public void testBlacklistedTag() throws Exception {
        underTest.addTagToBlacklist("one");

        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());

        underTest.pushTag("two");
        underTest.addTagToBlacklist("two");
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(1)).log(anyString(), anyString());

        underTest.clearTagBlacklist();
        underTest.loggerInternal(null, message, null, args);
        verify(mockLoggerAtNull, times(2)).log(anyString(), anyString());
    }

    @Test
    public void testDebugTools() throws Exception {
        underTest.pushTag(tag);
        underTest.loggerInternal(null, message, null, args);
        assertThat(underTest.getLastTag(), is(equalTo(tag)));
        assertThat(underTest.getLastLog(), is(equalTo(message)));
        underTest.flush();
        assertThat(underTest.getLastTag(), is(nullValue()));
        assertThat(underTest.getLastLog(), is(nullValue()));
    }

    @Test
    public void testTags() throws Exception {
        underTest.pushTag("one");
        assertThat(underTest.getCurrentTag(), is(equalTo("one")));
        underTest.loggerInternal(null, message, null, args);
        assertThat(underTest.getLastTag(), is(equalTo("one")));

        underTest.pushTag("two");
        assertThat(underTest.getCurrentTag(), is(equalTo("two")));
        underTest.loggerInternal(null, message, null, args);
        assertThat(underTest.getLastTag(), is(equalTo("two")));

        underTest.popTag();
        assertThat(underTest.getCurrentTag(), is(equalTo("one")));
        underTest.loggerInternal(null, message, null, args);
        assertThat(underTest.getLastTag(), is(equalTo("one")));

        underTest.tag("three");
        assertThat(underTest.getCurrentTag(), is(equalTo("three")));
        underTest.loggerInternal(null, message, null, args);
        assertThat(underTest.getLastTag(), is(equalTo("three")));

        assertThat(underTest.getCurrentTag(), is(equalTo("one")));
        underTest.loggerInternal(null, message, null, args);
        assertThat(underTest.getLastTag(), is(equalTo("one")));
    }


}
