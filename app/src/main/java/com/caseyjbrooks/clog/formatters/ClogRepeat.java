package com.caseyjbrooks.clog.formatters;

import com.caseyjbrooks.clog.ClogFormatter;

public class ClogRepeat implements ClogFormatter {
    @Override
    public Object format(Object data, Object[] params) {
        int times = (int) params[0];

        String output = "";
        for(int i = 0; i < times; i++) {
            output += data.toString();
        }

        return output;
    }
}
