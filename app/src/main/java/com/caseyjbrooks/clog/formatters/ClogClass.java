package com.caseyjbrooks.clog.formatters;

import com.caseyjbrooks.clog.ClogFormatter;

public class ClogClass implements ClogFormatter {
    @Override
    public String format(Object data, Object[] params) {
        return data.getClass().getSimpleName();
    }
}
