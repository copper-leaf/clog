package com.caseyjbrooks.clog.formatters;

import android.text.TextUtils;

import com.caseyjbrooks.clog.ClogFormatter;

public class ClogJoin implements ClogFormatter {
    @Override
    public Object format(Object data, Object[] params) {
        if(data instanceof Object[]) {
            return TextUtils.join(params[0].toString(), (Object[]) data);
        }
        else if(data instanceof Iterable) {
            return TextUtils.join(params[0].toString(), (Iterable) data);
        }
        else {
            return "";
        }
    }
}
