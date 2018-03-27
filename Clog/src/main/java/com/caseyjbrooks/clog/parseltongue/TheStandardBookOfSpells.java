package com.caseyjbrooks.clog.parseltongue;

import com.caseyjbrooks.clog.Clog;
import org.fusesource.jansi.Ansi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

/**
 * A collection of all the Spells available by default within Parseltongue formatters.
 */
public class TheStandardBookOfSpells {

    /**
     * Calls '.toString()' on an object, then formats the output as uppercase
     *
     * Clog example: #{ $1 | uppercase }
     *
     * @param data  the object to format
     * @return  the uppercase String representation of that object
     */
    @Spell(name="uppercase")
    public static String toUpper(Object data) {
        return data.toString().toUpperCase();
    }

    /**
     * Calls '.toString()' on an object, then formats the output as lowercase
     *
     * Clog example: #{ $1 | lowercase }
     *
     * @param data  the object to format
     * @return  the trimmed String representation of that object
     */
    @Spell(name="lowercase")
    public static String toLower(Object data) {
        return data.toString().toLowerCase();
    }

    /**
     * Calls '.toString()' on an object, then trims whitespace from its output
     *
     * Clog example: #{ $1 | trim }
     *
     * @param data  the object to format
     * @return  the lowercase String representation of that object
     */
    @Spell(name="trim")
    public static String trim(Object data) {
        return data.toString().trim();
    }

    /**
     * Calls '.toString()' on an object, then splits it into an array of String based on a given delimiter
     *
     * Clog example: #{ $1 | split('.') }
     *
     * @param data  the object to format
     * @return  the split String representation of that object
     */
    @Spell(name="split")
    public static String[] split(Object data, String delimiter) {
        return data.toString().split(delimiter);
    }

    /**
     * Calls '.toString()' on an object, then capitalizes the output
     *
     * Clog example: #{ $1 | capitalize }
     *
     * @param data  the object to format
     * @return  the capitalized String representation of that object
     */
    @Spell(name="capitalize")
    public static String capitalize(Object data) {
        String input = data.toString();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Repeats the input data for each element of the array, applying the corresponding Clog format String.
     *
     * Clog example: #{ $1 | repeat('* #{ $1 }') }
     *
     * @param data  the data to repeat
     * @param template  the Clog template to render for each element
     * @return  the repeated output
     */
    @Spell(name="repeat")
    public static String repeat(Object[] data, String template) {
        StringBuilder sb = new StringBuilder();
        for (Object token: data) {
            sb.append(Clog.format(template, token));
        }
        return sb.toString();
    }

    /**
     * Repeats the input data for each element of the Iterable, applying the corresponding Clog format String.
     *
     * Clog example: #{ $1 | repeat('#{ $1 }') }
     *
     * @param data  the data to repeat
     * @param template  the Clog template to render for each element
     * @return  the repeated output
     */
    @Spell(name="repeat")
    public static String repeat(Iterable data, String template) {
        StringBuilder sb = new StringBuilder();
        for (Object token: data) {
            sb.append(Clog.format(template, token));
        }
        return sb.toString();
    }

    /**
     * Calls '.toString()' on every element in the array, then joins them with a specified separator String. Based on
     * the Android Open Source Project TestUtils.java class.
     *
     * Clog example: #{ $1 | join(', ') }
     *
     * @param data  an Array of any type to join
     * @param separator  a String to concatenate between every element's String representation
     * @return  the joined string
     */
    @Spell(name="join")
    public static String join(Object[] data, String separator) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token: data) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(separator);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * Calls '.toString()' on every element in the Iterable, then joins them with a specified separator String. Based on
     * the Android Open Source Project TestUtils.java class.
     *
     * Clog example: #{ $1 | join(', ') }
     *
     * @param data  an Iterable of any type to join
     * @param separator  a String to concatenate between every element's String representation
     * @return  the joined string
     */
    @Spell(name="join")
    public static String join(Iterable data, String separator) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token: data) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(separator);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * Converts any Object to the simple Class name String of that Object
     *
     * Clog example: #{ $1 | className }
     *
     * @param data  any object to display the simple Class name
     * @return  the simple Class name of the object
     */
    @Spell(name="className")
    public static String getClassName(Object data) {
        return data.getClass().getSimpleName();
    }

    /**
     * Get the number of characters in a String
     *
     * Clog example: #{ $1 | length }
     *
     * @param data  a String to find the length of
     * @return  the length of the input String
     */
    @Spell(name="length")
    public static int length(String data) {
        return data.length();
    }

    /**
     * Get the number of elements in an Array
     *
     * Clog example: #{ $1 | length }
     *
     * @param data  an Array of any type to find the length of
     * @return  the length of the input Array
     */
    @Spell(name="length")
    public static int length(Object[] data) {
        return data.length;
    }

    /**
     * Get the number of elements in a Collection
     *
     * Clog example: #{ $1 | length }
     *
     * @param data  a Collection of any type to find the length of
     * @return  the length of the input Collection
     */
    @Spell(name="length")
    public static int length(Collection data) {
        return data.size();
    }


    /**
     * Formats a Calendar object with to human-readable date of the form 'yyyy/MM/dd HH:mm:ss'
     *
     * Clog example: #{ $1 | date }
     *
     * @param data  a Calendar object to get the date of
     * @return  the human-readable date
     */
    @Spell(name="date")
    public static Object date(Calendar data) {
        return date(data, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * Formats a Calendar object with to human-readable date of the form specified
     *
     * Clog example: #{ $1 | date('yyyy-MM-dd') }
     *
     * @param data  a Calendar object to get the date of
     * @param formatString  the String used to format the Calendar object
     * @return  the human-readable date
     */
    @Spell(name="date")
    public static Object date(Calendar data, String formatString) {
        if(data == null) {
            data = Calendar.getInstance();
        }

        if(formatString == null || formatString.length() == 0) {
            formatString = "yyyy/MM/dd HH:mm:ss";
        }

        return new SimpleDateFormat(formatString).format(data.getTime());
    }

    /**
     * Inserts the appropriate ANSI codes to change the foreground color of the text. The foreground color will be used
     * until an ANSI 'reset' character is added with the 'reset' Spell. Should only be used when output is intended to
     * be displayed in a Terminal. Works on both Unix/Linux terminals and Windows command prompt.
     *
     * Available Colors (case insensitive):
     * BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE, DEFAULT
     *
     * Clog example: #{ $0 | fg('cyan') }
     *
     * @param data  ignored
     * @param color  the color to change the foreground to.
     * @return  the ANSI codes to color the foreground of the terminal text
     */
    @Spell(name="fg")
    public static Object ansiForegroundColorStart(Object data, String color) {
        Ansi.Color ansiColor = Ansi.Color.valueOf(color.toUpperCase());

        if(ansiColor != null) {
            return Ansi.ansi().fg(ansiColor);
        }
        else {
            return "";
        }
    }

    /**
     * Inserts the appropriate ANSI codes to change the background color of the text. The background color will be used
     * until an ANSI 'reset' character is added with the 'reset' Spell. Should only be used when output is intended to
     * be displayed in a Terminal. Works on both Unix/Linux terminals and Windows command prompt.
     *
     * Available Colors (case insensitive):
     * BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE, DEFAULT
     *
     * Clog example: #{ $0 | bg('yellow') }
     *
     * @param data  ignored
     * @param color  the color to change the background to.
     * @return  the ANSI codes to color the background of the terminal text
     */
    @Spell(name="bg")
    public static Object ansiBackgroundColorStart(Object data, String color) {
        Ansi.Color ansiColor = Ansi.Color.valueOf(color.toUpperCase());

        if(ansiColor != null) {
            return Ansi.ansi().bg(ansiColor);
        }
        else {
            return "";
        }
    }

    /**
     * Inserts the appropriate ANSI codes to reset the terminal text colors. Should only be used when output is intended
     * to be displayed in a Terminal. Works on both Unix/Linux terminals and Windows command prompt.
     *
     * @param data  ignored
     * @return  the ANSI codes to reset the terminal colors
     */
    @Spell(name="reset")
    public static Object ansiStop(Object data) {
        return Ansi.ansi().reset();
    }
}
