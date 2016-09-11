package com.caseyjbrooks.clog.parsers;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogParser;
import com.caseyjbrooks.clog.formatters.ClogJoin;
import com.caseyjbrooks.clog.formatters.ClogLowercase;
import com.caseyjbrooks.clog.formatters.ClogRepeat;
import com.caseyjbrooks.clog.formatters.ClogUppercase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClogStringFormatter implements ClogParser {
    private HashMap<String, ClogFormatter> formatters;
    private ArrayList<Object> params;
    private ArrayList<Object> results;

//Public API
//--------------------------------------------------------------------------------------------------
    public ClogStringFormatter() {
        formatters = new HashMap<>();
        formatters.put("lowercase", new ClogLowercase());
        formatters.put("uppercase", new ClogUppercase());
        formatters.put("join", new ClogJoin());
        formatters.put("repeat", new ClogRepeat());
    }

    public ClogStringFormatter(HashMap<String, ClogFormatter> formatters) {
        this.formatters = formatters;
    }

    @Override
    public String format(String formatString, Object... params) {
        if(params != null && params.length > 0) {
            this.params = new ArrayList<>(Arrays.asList(params));
            this.results = new ArrayList<>();
            return format(formatString);
        }
        else {
            return formatString;
        }
    }

// Format the entire input string
//--------------------------------------------------------------------------------------------------
    private String format(String formatString) {
        String replacementRegex = "#" + "\\{" + "([^\\{}]*)" + "\\}";
        Pattern pattern = Pattern.compile(replacementRegex);
        Matcher matcher = pattern.matcher(formatString);

        int lastIndex = 0;
        String output = "";
        while(matcher.find()) {
            // Add all text that isn't part of the formatter pieces
            String formatBody = formatString.substring(lastIndex, matcher.start());
            output += formatBody;

            // Split inner string on '|'. The first piece should indicate which object from the
            // params we should start with, and the other pieces should create a pipeline of
            // ClogFormatters which continually format the object.
            String token = matcher.group(1).trim();
            String[] bodyPieces = token.split("\\|");


            Object objectToPrint = findFromIndexer(bodyPieces[0].trim());


            if(bodyPieces.length > 1) {
                Object result = unclog(objectToPrint, bodyPieces);
                results.add(result);

                output += result.toString();
            }
            else {
                output += objectToPrint.toString();
            }

            lastIndex = matcher.end();
        }

        output += formatString.substring(lastIndex, formatString.length());

        return output;
    }

// Format a single 'clog'
//--------------------------------------------------------------------------------------------------
    //TODO: parse params such that literal strings to not get split
    private Object unclog(Object objectToPrint, String[] formatterPieces) {
        String[] formatterKeys = Arrays.copyOfRange(formatterPieces, 1, formatterPieces.length);

        Object formattedObject = objectToPrint;
        for(String formatterKey : formatterKeys) {
            formatterKey = formatterKey.trim();
            String[] paramsArray = null;
            Object[] parsedParamsArray = null;

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

                parsedParamsArray = new Object[paramsArray.length];

                for(int i = 0; i < paramsArray.length; i++) {
                    parsedParamsArray[i] = getParameter(paramsArray[i]);
                }

                formatterKey = formatterKey.replaceAll("\\((.*)\\)", "").trim();
            }

            if(formatters.containsKey(formatterKey)) {
                formattedObject = formatters.get(formatterKey).format(formattedObject, parsedParamsArray);
            }
            else {
                throw new IllegalArgumentException("Cannot find the formatter with key '" + formatterKey + "'");
            }
        }

        return formattedObject;
    }

// Reference an object from either the passed in params or from the clog results
//--------------------------------------------------------------------------------------------------
    private Object findFromIndexer(String indexer) {
        if(indexer.matches("^\\$\\d+$")) {
            return findParamsObject(Integer.parseInt(indexer.substring(1)) - 1);
        }
        else if(indexer.matches("^@\\d+$")) {
            return findResultsObject(Integer.parseInt(indexer.substring(1)) - 1);
        }
        else {
            return null;
        }
    }

    private Object findParamsObject(int index) {
        if(index >= 0 && index < params.size()) {
            return params.get(index);
        }
        else {
            return null;
        }
    }

    private Object findResultsObject(int index) {
        if(index >= 0 && index < results.size()) {
            return results.get(index);
        }
        else {
            return null;
        }
    }


// Parse the parameters of a single formatter
//--------------------------------------------------------------------------------------------------
    private Object getParameter(String token) {
        // match a Param Indexer
        if(token.matches("^\\$\\d+$")) {
            return findParamsObject(Integer.parseInt(token.substring(1)) - 1);
        }

        // match a Result Indexer
        else if(token.matches("^@\\d+$")) {
            return findResultsObject(Integer.parseInt(token.substring(1)) - 1);
        }

        // match a literal string
        else if(token.matches("^'.*'$")) {
            return token.substring(1, token.length() - 1);
        }

        // match a literal boolean true
        else if(token.toLowerCase().equals("true")) {
            return true;
        }

        // match a literal boolean false
        else if(token.toLowerCase().equals("false")) {
            return false;
        }

        // match a literal integer
        else if(token.matches("\\d+")) {
            return Integer.parseInt(token);
        }

        // match a literal double
        else if(token.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")) {
            return Double.parseDouble(token);
        }

        // parameter is of an unknown format
        else {
            return null;
        }
    }

}
