package com.caseyjbrooks.clog;

import android.support.test.runner.AndroidJUnit4;

import com.caseyjbrooks.clog.parsers.Parseltongue;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ParseltongueTest {
    @Test
    public void testStringFormatting() throws Exception {
        ArrayList<String> names = new ArrayList<>();
        names.add("Bob");
        names.add("Larry");
        names.add("Junior");
        names.add("French Peas");

        Parseltongue parseltongue = new Parseltongue();

        String input, expectedOutput, output;

        input = "#{ $1 | repeat(3) | uppercase }";
        expectedOutput = "BOBBOBBOB";
        output = parseltongue.format(input, "Bob");
        assertEquals(expectedOutput, output);

        input = "#{ $1 | join('; ') | lowercase }";
        expectedOutput = "bob; larry; junior; french peas";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | join('; ') | lowercase } references back to #{ @1 | uppercase }";
        expectedOutput = "bob; larry; junior; french peas references back to BOB; LARRY; JUNIOR; FRENCH PEAS";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

    }
}
