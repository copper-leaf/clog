package com.caseyjbrooks.clog.formatters;

import com.caseyjbrooks.clog.ClogFormatter;

public class ClogLowercase implements ClogFormatter {
    @Override
    public Object format(Object data, Object[] params) {
        return data.toString().toLowerCase();
    }
}
