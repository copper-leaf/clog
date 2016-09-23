package com.caseyjbrooks.clog;

import com.caseyjbrooks.clog.parseltongue.Parseltongue;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ParseltongueTest {
    public static class ParselTongueTestClass {

    }

// Spot-test language features for correctness when syntax is valid
//--------------------------------------------------------------------------------------------------

    @Test
    public void testStringFormatting() throws Exception {
        ArrayList<String> names = new ArrayList<>();
        names.add("Harry");
        names.add("Ron");
        names.add("Hermione");
        names.add("Fred and George");

        Parseltongue parseltongue = new Parseltongue();
        parseltongue.findSpells(ParseltongueTest.class);

        String input, expectedOutput, output;

        input = "#{ $1 | repeat(3) | uppercase }";
        expectedOutput = "HARRYHARRYHARRY";
        output = parseltongue.format(input, "Harry");
        assertEquals(expectedOutput, output);

        input = "#{ $1 | join(', ') | lowercase }";
        expectedOutput = "harry, ron, hermione, fred and george";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | join(', ') | lowercase } references back to #{ @1 | uppercase }";
        expectedOutput = "harry, ron, hermione, fred and george references back to HARRY, RON, HERMIONE, FRED AND GEORGE";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // the hello method expects a boolean parameter, so this clog will fail and output nothing
        input = "#{ 1 | hello }";
        expectedOutput = "";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // this hello is passed a boolean, so will print correctly
        input = "#{ true | hello }";
        expectedOutput = "Hello, world!";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | className }";
        expectedOutput = "ParselTongueTestClass";
        output = parseltongue.format(input, new ParselTongueTestClass());
        assertEquals(expectedOutput, output);
    }

// Brute-force test all possible parameter types are working
//--------------------------------------------------------------------------------------------------

    @Test
    public void testValidParamTypes() {
        byte _byte = 3;
        short _short = 5;
        int _int = 7;
        long _long = 9;
        float _float = 3.3f;
        double _double = 5.5;
        boolean _booleanTrue = true;
        boolean _booleanFalse = false;

        Parseltongue parseltongue = new Parseltongue();
        parseltongue.findSpells(ParseltongueTest.class);

        String input, expectedOutput, output;

        input = "#{ $1 | printByte }";
        expectedOutput = "3";
        output = parseltongue.format(input, _byte);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | printShort }";
        expectedOutput = "5";
        output = parseltongue.format(input, _short);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | printInt }";
        expectedOutput = "7";
        output = parseltongue.format(input, _int);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | printLong }";
        expectedOutput = "9";
        output = parseltongue.format(input, _long);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | printFloat }";
        expectedOutput = "3.3";
        output = parseltongue.format(input, _float);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | printDouble }";
        expectedOutput = "5.5";
        output = parseltongue.format(input, _double);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | printBoolean }";
        expectedOutput = "true";
        output = parseltongue.format(input, _booleanTrue);
        assertEquals(expectedOutput, output);

        input = "#{ $1 | printBoolean }";
        expectedOutput = "false";
        output = parseltongue.format(input, _booleanFalse);
        assertEquals(expectedOutput, output);
    }


// Brute-force test all possible parsed parameters and literals are working
//--------------------------------------------------------------------------------------------------

    @Test
    public void testValidParsedParamTypes() {
        Parseltongue parseltongue = new Parseltongue();
        parseltongue.findSpells(ParseltongueTest.class);

        String input, expectedOutput, output;

        input = "#{ 3 | printLiteralInt }";
        expectedOutput = "3";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);

        input = "#{ 'hello world' | printLiteralString }";
        expectedOutput = "hello world";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);

        input = "#{ 3.3 | printLiteralDouble }";
        expectedOutput = "3.3";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);

        input = "#{ true | printLiteralBoolean }";
        expectedOutput = "true";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);

        input = "#{ false | printLiteralBoolean }";
        expectedOutput = "false";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);
    }


// Test that any clogs with invalid syntax simply do not display
//--------------------------------------------------------------------------------------------------

    @Test
    public void testInvalidSyntax() {
        Parseltongue parseltongue = new Parseltongue();
        parseltongue.findSpells(ParseltongueTest.class);

        ArrayList<String> names = new ArrayList<>();
        names.add("Harry");
        names.add("Ron");
        names.add("Hermione");
        names.add("Fred and George");

        String input, expectedOutput, output;

        // no input is valid syntax
        input = "";
        expectedOutput = "";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);

        // valid syntax
        input = "Harry Potter Names: #{ $1 | join('') | lowercase }!";
        expectedOutput = "Harry Potter Names: harryronhermionefred and george!";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // a pipe not followed by a spell (easily ignored, only clog disappears)
        input = "Harry Potter Names: #{ $1 | join('') |  }!";
        expectedOutput = "Harry Potter Names: !";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // a pipe not followed by a spell in the middle of the spell list (easily ignored, only clog disappears)
        input = "Harry Potter Names: #{ $1 |  | lowercase }!";
        expectedOutput = "Harry Potter Names: !";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // a pipe followed by nothing (easily ignored, only clog disappears)
        input = "Harry Potter Names: #{ $1 |  }!";
        expectedOutput = "Harry Potter Names: !";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // spell with params is missing a parentheses (easily ignored, only clog disappears)
        input = "Harry Potter Names: #{ $1 | join('' |  lowercase }!";
        expectedOutput = "Harry Potter Names: !";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // literal string quote never closed (no recovery, everything until next quote is ignored, possibly the rest of the string)
        input = "Harry Potter Names: #{ $1 | join(') | lowercase }!";
        expectedOutput = "Harry Potter Names: ";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        // No ending curly brace (everything is lost until it finds the next curly brace)
        input = "Harry Potter Names: #{ $1 | join('') | lowercase ! #{ $1 } is so cool!";
        expectedOutput = "Harry Potter Names:  is so cool!";
        output = parseltongue.format(input, names);
        assertEquals(expectedOutput, output);

        //test that for clogs that are ignored, except those missing ending braces, the ordering of
        //results is preserved
        input = "1: #{$1}, 2: #{$2}, 3: #{$e}, 4: #{$4}. a1: #{@1}, a2: #{@2}, a3: #{@3}, a4: #{@4}.";
        expectedOutput = "1: 10, 2: 20, 3: , 4: 40. a1: 10, a2: 20, a3: , a4: 40.";
        output = parseltongue.format(input, 10, 20, 30, 40);
        assertEquals(expectedOutput, output);
    }

// Test that data is moved through the pipeline correctly, accounting for errors
//--------------------------------------------------------------------------------------------------

    @Test
    public void testPipeline() {
        Parseltongue parseltongue = new Parseltongue();
        parseltongue.findSpells(ParseltongueTest.class);

        ArrayList<String> names = new ArrayList<>();
        names.add("Harry");
        names.add("Ron");
        names.add("Hermione");
        names.add("Fred and George");

        String input, expectedOutput, output;

        input = "";
        expectedOutput = "";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);

        input = "Enter Pipeline --> #{ $0 }";
        expectedOutput = "Enter Pipeline --> ";
        output = parseltongue.format(input);
        assertEquals(expectedOutput, output);

        // test normal pipeline flow, with input started by real object
        input = "Enter Pipeline --> #{ $1 | paramOrder(2, '3', 4.5) | repeat(2) | length }";
        expectedOutput = "Enter Pipeline --> 28";
        output = parseltongue.format(input, "1");
        assertEquals(expectedOutput, output);

        input = "Enter Pipeline --> #{ $0 | paramOrder(2, '3', 4.5) | repeat(2) }";
        expectedOutput = "Enter Pipeline --> [null, 2, 3, 4.5][null, 2, 3, 4.5]";
        output = parseltongue.format(input, "1", null, 3);
        assertEquals(expectedOutput, output);

        input = "Enter Pipeline --> #{ $1 | paramOrder(2, $2, 4.5) | repeat(2) }";
        expectedOutput = "Enter Pipeline --> [1, 2, null, 4.5][1, 2, null, 4.5]";
        output = parseltongue.format(input, "1", null, 3);
        assertEquals(expectedOutput, output);

        input = "Enter Pipeline --> #{ $1 | paramOrder(2, $0, 4.5) | repeat(2) }";
        expectedOutput = "Enter Pipeline --> [1, 2, null, 4.5][1, 2, null, 4.5]";
        output = parseltongue.format(input, "1", null, 3);
        assertEquals(expectedOutput, output);

        input = "Enter Pipeline --> #{ $1 | paramOrder(2, null, 4.5) | repeat(2) }";
        expectedOutput = "Enter Pipeline --> [1, 2, null, 4.5][1, 2, null, 4.5]";
        output = parseltongue.format(input, "1", null, 3);
        assertEquals(expectedOutput, output);

        input = "Enter Pipeline --> #{ $1 | paramOrder(2, $1, 4.5) | repeat(null) }";
        expectedOutput = "Enter Pipeline --> ";
        output = parseltongue.format(input, "1", null, 3);
        assertEquals(expectedOutput, output);

        input = "Enter Pipeline --> #{ $1 | returnNull | repeat(5) | length}";
        expectedOutput = "Enter Pipeline --> 0";
        output = parseltongue.format(input, "1", null, 3);
        assertEquals(expectedOutput, output);
    }

//test spells
//--------------------------------------------------------------------------------------------------

    @Spell
    public static String hello(boolean includeWorld) {
        return (includeWorld) ? "Hello, world!" : "Hello";
    }

    @Spell
    public static String printByte(byte val) {
        return Byte.toString(val);
    }

    @Spell
    public static String printShort(short val) {
        return Short.toString(val);
    }

    @Spell
    public static String printInt(int val) {
        return Integer.toString(val);
    }

    @Spell
    public static String printLong(long val) {
        return Long.toString(val);
    }

    @Spell
    public static String printFloat(float val) {
        return Float.toString(val);
    }

    @Spell
    public static String printDouble(double val) {
        return Double.toString(val);
    }

    @Spell
    public static String printBoolean(boolean val) {
        return Boolean.toString(val);
    }

    @Spell
    public static String printLiteralInt(int val) {
        return Integer.toString(val);
    }

    @Spell
    public static String printLiteralString(String val) {
        return val;
    }

    @Spell
    public static String printLiteralDouble(double val) {
        return Double.toString(val);
    }

    @Spell
    public static String printLiteralBoolean(boolean val) {
        return Boolean.toString(val);
    }

    @Spell
    public static String returnNull(Object object) {
        return null;
    }

    @Spell
    public static String paramOrder(String one, int two, String three, double four) {
        return "[" + one + ", " + Integer.toString(two) + ", " + three + ", " + Double.toString(four) + "]";
    }


}
