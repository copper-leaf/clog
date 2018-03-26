package com.caseyjbrooks.clog;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class DefaultLoggerTest {

    private DefaultLogger underTest;
    private String tag;
    private String message;
    private Throwable throwable;

    @Before
    public void setup() {
        underTest = spy(new DefaultLogger());
        tag = "tag";
        message = "message";
        throwable = new Throwable("throwable");
    }

    @Test
    public void testBasicLogging() throws Exception {
        assertThat(underTest.isActive(), is(true));
        assertThat(underTest.log(tag, message), is(equalTo(0)));
        verify(underTest, times(1)).getAnsiLevelString();
        assertThat(underTest.log(tag, message, throwable), is(equalTo(0)));
        verify(underTest, times(2)).getAnsiLevelString();
        assertThat(underTest.priority(), is(Clog.Priority.DEFAULT));
    }

}
