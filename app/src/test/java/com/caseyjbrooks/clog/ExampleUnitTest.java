package com.caseyjbrooks.clog;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String output = Clog.formatString("{{ $1 }} {{ $2 }}", 1, 2);
        assertEquals("1 2", output);
    }
}
