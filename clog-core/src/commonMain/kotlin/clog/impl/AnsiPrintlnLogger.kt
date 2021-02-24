package clog.impl

import clog.Clog
import clog.api.ClogLogger
import clog.util.ansiColorEnd
import clog.util.ansiColorStart

/**
 * Logs a message to stdout using [println]. The priority is displayed with ANSI colors and the tag is prepended to the
 * message if one is provided.
 */
class AnsiPrintlnLogger : ClogLogger {
    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        getDefaultLogMessage(priority, tag, message).also { println(it) }
    }

    companion object {

        /**
         * Build a message string with the priority displayed with ANSI colors (for terminal consoles) and a tag
         * prepended to the message if one is provided.
         */
        fun getDefaultLogMessage(priority: Clog.Priority, tag: String?, message: String): String {
            return buildString {
                if (priority != Clog.Priority.DEFAULT) {
                    append("${priority.ansiColorStart}[${priority.name}]${priority.ansiColorEnd} ")
                }

                if (tag != null) {
                    append("$tag: ")
                }

                append(message)
            }
        }
    }
}
