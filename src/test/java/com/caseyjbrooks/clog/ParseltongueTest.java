package com.caseyjbrooks.clog;

import com.caseyjbrooks.clog.parseltongue.Parseltongue;

import com.caseyjbrooks.clog.parseltongue.Spell;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParseltongueTest {
    public static class ParselTongueTestClass {

        private String ha = "Harry";
        private String r = "Ron";
        private String he = "Hermione";
        private String fg = "Fred and George";

        public String pha = "Harry";
        public String pr = "Ron";
        public String phe = "Hermione";
        public String pfg = "Fred and George";

        public Object get(String key) {
            switch (key) {
                case "ha": return ha;
                case "r": return r;
                case "he": return he;
                case "fg": return fg;
                case "pha": return pha;
                case "pr": return pr;
                case "phe": return phe;
                case "pfg": return pfg;
                default: return null;
            }
        }
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

        input = "#{ $1 | repeat('#{$1}, ') | lowercase }";
        expectedOutput = "harry, ron, hermione, fred and george, ";
        output = parseltongue.format(input, names);
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

        // test that if we leave out anything inside the braces, we print objects in order
        input = "1: #{}, 2: #{}, 3: #{}, 4: #{}.";
        expectedOutput = "1: 10, 2: 20, 3: 30, 4: 40.";
        output = parseltongue.format(input, 10, 20, 30, 40);
        assertEquals(expectedOutput, output);

        // test that if we leave out anything inside the braces, we print objects in order, using SLF4J-style formatters
        input = "1: {}, 2: {}, 3: {}, 4: {}.";
        expectedOutput = "1: 10, 2: 20, 3: 30, 4: 40.";
        output = parseltongue.format(input, 10, 20, 30, 40);
        assertEquals(expectedOutput, output);

        // test that SLF4J-style formatters don't allow spells, instead just printing the item out directly
        input = "{ repeat(2) }";
        expectedOutput = "{ repeat(2) }";
        output = parseltongue.format(input, 10, 20, 30, 40);
        assertEquals(expectedOutput, output);

        input = "{ | repeat(2) }";
        expectedOutput = "{ | repeat(2) }";
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

// Test that lists and arrays can be indexed by position, and collections and other classes that can
// get an object by a string index can be found, and any generic object's property can be found
//--------------------------------------------------------------------------------------------------

    @Test
    public void testIndexers() {
        Parseltongue parseltongue = new Parseltongue();
        parseltongue.findSpells(ParseltongueTest.class);

        List<String> namesList = new ArrayList<>();
        namesList.add("Harry");
        namesList.add("Ron");
        namesList.add("Hermione");
        namesList.add("Fred and George");

        String[] namesArray = new String[] {
            "Harry",
            "Ron",
            "Hermione",
            "Fred and George"
        };

        Map<String, String> namesMap = new HashMap<>();
        namesMap.put("ha", "Harry");
        namesMap.put("r", "Ron");
        namesMap.put("he", "Hermione");
        namesMap.put("fg", "Fred and George");

        String input, expectedOutput, output;

        // Get the item at index 2 of the List
        input = "Enter Pipeline --> #{ $1[2] }";
        expectedOutput = "Enter Pipeline --> Hermione";
        output = parseltongue.format(input, namesList);
        assertEquals(expectedOutput, output);

        // Get the item at index 2 of the array. We need to add an extra param to force the String
        // array as an object and not as varargs
        input = "Enter Pipeline --> #{ $1[2] }";
        expectedOutput = "Enter Pipeline --> Hermione";
        output = parseltongue.format(input, namesArray, 0);
        assertEquals(expectedOutput, output);

        // Get the value from the map at the key 'he'
        input = "Enter Pipeline --> #{ $1['he'] }";
        expectedOutput = "Enter Pipeline --> Hermione";
        output = parseltongue.format(input, namesMap);
        assertEquals(expectedOutput, output);

        // The field 'he' is private and will not be displayed
        input = "Enter Pipeline --> #{ $1[he] }";
        expectedOutput = "Enter Pipeline --> ";
        output = parseltongue.format(input, new ParselTongueTestClass());
        assertEquals(expectedOutput, output);

        // The field 'phe' is public and can be viewed
        input = "Enter Pipeline --> #{ $1[phe] }";
        expectedOutput = "Enter Pipeline --> Hermione";
        output = parseltongue.format(input, new ParselTongueTestClass());
        assertEquals(expectedOutput, output);

        // The field 'he' is private, but by setting fields to be accessible, it can be accessed
        parseltongue.setPrivateFieldsAccessible(true);
        input = "Enter Pipeline --> #{ $1[he] }";
        expectedOutput = "Enter Pipeline --> Hermione";
        output = parseltongue.format(input, new ParselTongueTestClass());
        assertEquals(expectedOutput, output);

        // The ParselTongueTestClass has a 'get(String)' method, so it can be called with a key
        input = "Enter Pipeline --> #{ $1['he'] }";
        expectedOutput = "Enter Pipeline --> Hermione";
        output = parseltongue.format(input, new ParselTongueTestClass());
        assertEquals(expectedOutput, output);

        // test that spells can also have indexers applied. Spells using indexers must use empty
        // parentheses as params
        input = "Enter Pipeline --> #{ $1 | uppercaseAll()[2] }";
        expectedOutput = "Enter Pipeline --> HERMIONE";
        output = parseltongue.format(input, new Object[]{ namesArray });
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

    @Spell
    public static String[] uppercaseAll(String[] array) {

        String[] outputArray = new String[array.length];

        for(int i = 0; i < array.length; i++) {
            outputArray[i] = array[i].toUpperCase();
        }

        return outputArray;
    }

    @Spell
    public static String repeat(Object data, int times) {
        String output = "";
        for(int i = 0; i < times; i++) {
            if(data != null) {
                output += data.toString();
            }
        }

        return output;
    }
}
