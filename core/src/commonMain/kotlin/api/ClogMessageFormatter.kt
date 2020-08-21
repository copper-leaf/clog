package clog.api

/**
 * Formats a log message with positional args and any other contextual information.
 */
interface ClogMessageFormatter {

    /**
     * Formats a log message with positional args and any other contextual information. Tags and logging levels are
     * handled by the [ClogLogger] and should not be included in this formatted message.
     */
    fun format(message: String, vararg args: Any?): String
}
