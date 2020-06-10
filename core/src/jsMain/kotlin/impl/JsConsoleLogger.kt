package clog.impl

import clog.Clog
import clog.api.ClogLogger

class JsConsoleLogger : ClogLogger {
    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        DefaultLogger
            .getDefaultLogMessage(tag, message)
            .also {
                when (priority) {
                    Clog.Priority.VERBOSE -> console.asDynamic().debug(it)
                    Clog.Priority.DEBUG -> console.asDynamic().debug(it)
                    Clog.Priority.INFO -> console.asDynamic().info(it)
                    Clog.Priority.DEFAULT -> console.log(it)
                    Clog.Priority.WARNING -> console.warn(it)
                    Clog.Priority.ERROR -> console.error(it)
                    Clog.Priority.FATAL -> console.error(it)
                }
            }
    }
}
