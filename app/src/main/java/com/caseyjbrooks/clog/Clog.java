package com.caseyjbrooks.clog;

import android.util.Log;

import com.caseyjbrooks.clog.formatters.ClogClass;
import com.caseyjbrooks.clog.formatters.ClogDate;
import com.caseyjbrooks.clog.loggers.ClogD;
import com.caseyjbrooks.clog.loggers.ClogE;
import com.caseyjbrooks.clog.loggers.ClogI;
import com.caseyjbrooks.clog.loggers.ClogV;
import com.caseyjbrooks.clog.loggers.ClogW;
import com.caseyjbrooks.clog.loggers.ClogWTF;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Clog {
    private static HashMap<String, ClogFormatter> formatters = new HashMap<>();
    private static HashMap<String, ClogLogger> loggers = new HashMap<>();
    static {
        // initialize default formatters
        formatters = new HashMap<>();
        formatters.put("date", new ClogDate());
        formatters.put("class", new ClogClass());

        formatters.put("d", new ClogD());
        formatters.put("e", new ClogE());
        formatters.put("i", new ClogI());
        formatters.put("v", new ClogV());
        formatters.put("w", new ClogW());
        formatters.put("wtf", new ClogWTF());

        // initialize default loggers
        loggers = new HashMap<>();
        loggers.put("d", new ClogD());
        loggers.put("e", new ClogE());
        loggers.put("i", new ClogI());
        loggers.put("v", new ClogV());
        loggers.put("w", new ClogW());
        loggers.put("wtf", new ClogWTF());
    }

    private Clog() {
    }

    public static int d(String formatString, Object... args)                                    { return log("d", "Clog", formatString, args); }
    public static int e(String formatString, Object... args)                                    { return log("e", "Clog", formatString, args); }
    public static int i(String formatString, Object... args)                                    { return log("i", "Clog", formatString, args); }
    public static int v(String formatString, Object... args)                                    { return log("v", "Clog", formatString, args); }
    public static int w(String formatString, Object... args)                                    { return log("w", "Clog", formatString, args); }
    public static int wtf(String formatString, Object... args)                                  { return log("wtf", "Clog", formatString, args); }

    public static int w(String tag, Throwable throwable)                                        { return log("wtf", tag, getStackTraceString(throwable)); }
    public static int wtf(String tag, Throwable throwable)                                      { return log("wtf", tag, Log.getStackTraceString(throwable)); }

    public static int d(String tag, String formatString, Object... args)                        { return log("d", tag, formatString, args); }
    public static int e(String tag, String formatString, Object... args)                        { return log("e", tag, formatString, args); }
    public static int i(String tag, String formatString, Object... args)                        { return log("i", tag, formatString, args); }
    public static int v(String tag, String formatString, Object... args)                        { return log("v", tag, formatString, args); }
    public static int w(String tag, String formatString, Object... args)                        { return log("w", tag, formatString, args); }
    public static int wtf(String tag, String formatString, Object... args)                      { return log("wtf", tag, formatString, args); }

    public static int d(String tag, String formatString, Throwable throwable, Object... args)   { return log("d", tag, formatString, throwable, args); }
    public static int e(String tag, String formatString, Throwable throwable, Object... args)   { return log("e", tag, formatString, throwable, args); }
    public static int i(String tag, String formatString, Throwable throwable, Object... args)   { return log("i", tag, formatString, throwable, args); }
    public static int v(String tag, String formatString, Throwable throwable, Object... args)   { return log("v", tag, formatString, throwable, args); }
    public static int w(String tag, String formatString, Throwable throwable, Object... args)   { return log("w", tag, formatString, throwable, args); }
    public static int wtf(String tag, String formatString, Throwable throwable, Object... args) { return log("wtf", tag, formatString, throwable, args); }

    public static String getStackTraceString(Throwable throwable) { return Log.getStackTraceString(throwable); }
    public static boolean isLoggable(String tag, int level) { return Log.isLoggable(tag, level); }
    public static int println(int priority, String tag, String message) { return Log.println(priority, tag, message); }

    public static int log(String logger, String tag, String message, Object... args) {
        String formattedMessage = formatString(message, args);

        if(loggers.containsKey(logger)) {
            return loggers.get(logger).log(tag, formattedMessage);
        }
        else {
            return new ClogW().log(tag, "" + formattedMessage + " (cannot find default logger with tag '" + logger + "')");
        }
    }

    public static int log(String logger, String tag, String message, Throwable throwable, Object... args) {
        String formattedMessage = formatString(message, args);

        if(loggers.containsKey(logger)) {
            return loggers.get(logger).log(tag, formattedMessage, throwable);
        }
        else {
            return new ClogW().log(tag, "" + formattedMessage + " (cannot find logger with tag '" + logger + "')");
        }
    }







//String format parser
//--------------------------------------------------------------------------------------------------
    public static String formatString(String message, Object... params) {
        if(params != null && params.length > 0) {

            String replacementRegex = "\\{" + "\\{" + "([^\\{}]*)" + "\\}" + "\\}";
            Pattern pattern = Pattern.compile(replacementRegex);
            Matcher matcher = pattern.matcher(message);

            int lastIndex = 0;
            String output = "";
            while(matcher.find()) {
                // Add all text that isn't part of the formatter pieces
                String formatBody = message.substring(lastIndex, matcher.start());
                output += formatBody;

                // Split inner string on '|'. The first piece should indicate which object from the
                // params we should start with, and the other pieces should create a pipeline of
                // ClogFormatters which continually format the object.
                String token = matcher.group(1).trim();
                String[] bodyPieces = token.split("\\|");
                Object objectToPrint = getObjectToFormat(bodyPieces[0].trim(), params);
                if(bodyPieces.length > 1) {
                    output += formatObject(objectToPrint, bodyPieces, params).toString();
                }
                else {
                    output += objectToPrint.toString();
                }

                lastIndex = matcher.end();
            }

            output += message.substring(lastIndex, message.length());

            return output;
        }
        else {
            return message;
        }
    }

    private static Object getObjectToFormat(String indexPiece, Object[] params) {
        if(indexPiece.matches("^\\$\\d+$")) {
            int objectIndex = parseInt(indexPiece.substring(1)) - 1;

            if(objectIndex >= 0 && objectIndex < params.length) {
                return params[objectIndex];
            }
            else if(objectIndex == -1) {
                return null;
            }
            else {
                throw new IllegalArgumentException("Attempted to access an object not within the range of given params: Object index: " + objectIndex + ", number of params: " + params.length);
            }
        }
        else {
            throw new IllegalArgumentException("Formatters must specify an object to format using the format '$index': '" + indexPiece + "'");
        }
    }

    private static Object getParameter(String token, Object[] params) {
        if(token.matches("^\\$\\d+$")) {
            int objectLiteral = parseInt(token.substring(1)) - 1;

            if(objectLiteral >= 0 && objectLiteral < params.length) {
                return params[objectLiteral];
            }
            else {
                return null;
            }
        }
        else if(token.matches("^'.*'$")) {
            return token.substring(1, token.length() - 1);
        }
        else if(token.toLowerCase().equals("true")) {
            return true;
        }
        else if(token.toLowerCase().equals("false")) {
            return false;
        }

        return null;
    }

    private static Object formatObject(Object objectToPrint, String[] formatterPieces, Object[] params) {
        String[] formatterKeys = Arrays.copyOfRange(formatterPieces, 1, formatterPieces.length);

        Object formattedObject = objectToPrint;
        for(String formatterKey : formatterKeys) {
            formatterKey = formatterKey.trim();
            String[] paramsArray = null;

            // Get optional params for the formatter in they exist
            Pattern pattern = Pattern.compile("\\((.*)\\)");
            Matcher matcher = pattern.matcher(formatterKey);
            if(matcher.find()) {
                String paramsString = matcher.group(1);

                if(paramsString.contains(",")) {
                    paramsArray = paramsString.split("\\s*,\\s*");
                }
                else {
                    paramsArray = new String[] { paramsString };
                }

                Object[] parsedParamsArray = new Object[paramsArray.length];

                for(int i = 0; i < paramsArray.length; i++) {
                    parsedParamsArray[i] = getParameter(paramsArray[i], params);
                }

                formatterKey = formatterKey.replaceAll("\\((.*)\\)", "").trim();
            }

            if(formatters.containsKey(formatterKey)) {
                formattedObject = formatters.get(formatterKey).format(formattedObject, paramsArray);
            }
            else {
                throw new IllegalArgumentException("Cannot find the formatter with key '" + formatterKey + "'");
            }
        }

        return formattedObject;
    }
}
