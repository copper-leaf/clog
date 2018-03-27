package com.caseyjbrooks.clog;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.spy;

public class EmptyLoggerTest {

    private EmptyLogger underTest;
    private String tag;
    private String message;
    private Throwable throwable;

    @Before
    public void setup() {
        underTest = spy(new EmptyLogger());
        tag = "tag";
        message = "message";
        throwable = new Throwable("throwable");
    }

    @Test
    public void testBasicLogging() throws Exception {
        assertThat(underTest.isActive(), is(false));
        assertThat(underTest.log(tag, message), is(equalTo(0)));
        assertThat(underTest.log(tag, message, throwable), is(equalTo(0)));
        assertThat(underTest.priority(), is(Clog.Priority.DEFAULT));
    }

}
