package com.caseyjbrooks.clog.parsers;

import com.caseyjbrooks.clog.ClogParser;

public class JavaStringFormatter implements ClogParser {
    @Override
    public String format(String formatString, Object... params) {
        return String.format(formatString, params);
    }
}
