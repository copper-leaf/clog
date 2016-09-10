package com.caseyjbrooks.clog.parsers;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class ClogStringFormatter implements ClogParser {
    private HashMap<String, ClogFormatter> formatters;
    private Object[] params;

//Public API
//--------------------------------------------------------------------------------------------------
    public ClogStringFormatter() {
        formatters = new HashMap<>();
    }

    public ClogStringFormatter(HashMap<String, ClogFormatter> formatters) {
        this.formatters = formatters;
    }

    @Override
    public String format(String formatString, Object... params) {
        if(params != null && params.length > 0) {
            this.params = params;
            return format(formatString);
        }
        else {
            return formatString;
        }
    }


//Private implementation details
//--------------------------------------------------------------------------------------------------
    private String format(String formatString) {
        String replacementRegex = "\\{" + "\\{" + "([^\\{}]*)" + "\\}" + "\\}";
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
            Object objectToPrint = getObjectToFormat(bodyPieces[0].trim());
            if(bodyPieces.length > 1) {
                output += formatObject(objectToPrint, bodyPieces).toString();
            }
            else {
                output += objectToPrint.toString();
            }

            lastIndex = matcher.end();
        }

        output += formatString.substring(lastIndex, formatString.length());

        return output;
    }

    private Object getObjectToFormat(String indexPiece) {
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

    private Object getParameter(String token) {
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

    private Object formatObject(Object objectToPrint, String[] formatterPieces) {
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
                    parsedParamsArray[i] = getParameter(paramsArray[i]);
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
