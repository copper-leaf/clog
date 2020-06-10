package clog.api

import clog.Clog
import clog.createDefaultFilter
import clog.createDefaultLogger
import clog.createDefaultMessageFormatter
import clog.createDefaultTagProvider

data class ClogProfile(
    val logger: ClogLogger = createDefaultLogger(),
    val tagProvider: ClogTagProvider = createDefaultTagProvider(),
    val filter: ClogFilter = createDefaultFilter(),
    val messageFormatter: ClogMessageFormatter = createDefaultMessageFormatter()
) : IClog, ClogFilter by filter {

    override fun v(message: String, vararg args: Any?) {
        log(Clog.Priority.VERBOSE) { format(message, *args) }
    }

    override fun d(message: String, vararg args: Any?) {
        log(Clog.Priority.DEBUG) { format(message, *args) }
    }

    override fun i(message: String, vararg args: Any?) {
        log(Clog.Priority.INFO) { format(message, *args) }
    }

    override fun w(message: String, vararg args: Any?) {
        log(Clog.Priority.WARNING) { format(message, *args) }
    }

    override fun e(message: String, vararg args: Any?) {
        log(Clog.Priority.ERROR) { format(message, *args) }
    }

    override fun wtf(message: String, vararg args: Any?) {
        log(Clog.Priority.FATAL) { format(message, *args) }
    }

    override fun log(message: String, vararg args: Any?) {
        log(Clog.Priority.DEFAULT) { format(message, *args) }
    }

// Internal implementation
// ---------------------------------------------------------------------------------------------------------------------

    inline fun log(priority: Clog.Priority, message: ClogMessageFormatter.() -> String) {
        val tag = tagProvider.get()
        if (filter.shouldLog(priority, tag)) {
            logger.log(priority, tag, message(messageFormatter))
        }
    }
}
