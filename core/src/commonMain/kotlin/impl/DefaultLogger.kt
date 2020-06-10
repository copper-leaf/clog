package clog.impl

import clog.Clog
import clog.api.ClogLogger
import clog.util.ansiColorEnd
import clog.util.ansiColorStart

class DefaultLogger : ClogLogger {
    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        getDefaultLogMessageWithAnsiCodes(priority, tag, message).also { println(it) }
    }

    companion object {
        fun getDefaultLogMessageWithAnsiCodes(priority: Clog.Priority, tag: String?, message: String): String {
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

        fun getDefaultLogMessage(tag: String?, message: String): String {
            return getDefaultLogMessage(
                Clog.Priority.DEFAULT,
                tag,
                message
            )
        }

        fun getDefaultLogMessage(priority: Clog.Priority, tag: String?, message: String): String {
            return buildString {
                if (priority != Clog.Priority.DEFAULT) {
                    append("[${priority.name}] ")
                }

                if (tag != null) {
                    append("$tag: ")
                }

                append(message)
            }
        }
    }
}
