package com.caseyjbrooks.clog.spells;

import com.caseyjbrooks.clog.ClogFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClogDate implements ClogFormatter {
    @Override
    public Object format(Object data, Object[] params) {
        Calendar now = Calendar.getInstance();
        String formatString = "yyyy/MM/dd HH:mm:ss";

        if(data != null && data instanceof Calendar) {
            now = (Calendar) data;
        }

        if(params != null && params.length > 0 && params[0] instanceof String) {
            formatString = (String) params[0];
        }

        final DateFormat dateFormat = new SimpleDateFormat(formatString);

        return dateFormat.format(now.getTime());
    }
}
