package com.caseyjbrooks.clog.formatters;

import com.caseyjbrooks.clog.ClogFormatter;

public class ClogUppercase implements ClogFormatter {
    @Override
    public Object format(Object data, Object[] params) {
        return data.toString().toUpperCase();
    }
}
