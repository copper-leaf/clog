package com.caseyjbrooks.clog;

/**
 * Denotes a Class as one that can take an input String with formatting markup, a list of objects, and transforms it
 * to an output String.
 */
public interface ClogFormatter {

    /**
     * Transform a String containing markup using the optional provided params
     *
     * @param message  a String containing markup
     * @param params  params to pass to the String to be rendered
     * @return  the rendered output String
     */
    String format(String message, Object... params);

}
