package com.caseyjbrooks.clog;

public interface ClogFormatter {
    String format(String message, Object... params);
}
