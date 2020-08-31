package clog

import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider

/**
 * The standard entry-point to logging messages to Clog. Messages will be filtered by the [filter], and messages that
 * pass the filter will be formatted with [messageFormatter] and logged to [logger] with a tag provided by
 * [tagProvider].
 */
data class ClogProfile(
    val logger: ClogLogger = createDefaultLogger(),
    val tagProvider: ClogTagProvider = createDefaultTagProvider(),
    val filter: ClogFilter = createDefaultFilter(),
    val messageFormatter: ClogMessageFormatter = createDefaultMessageFormatter()
) : ClogMessageLogger, ClogThrowableLogger {

// ClogMessageLogger implementation
// ---------------------------------------------------------------------------------------------------------------------

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

    /**
     * Filters, formats, and logs a message. This is an internal method that should not be called directly. Use the
     * methods from [ClogProfile] or [clog.dsl] instead.
     */
    inline fun log(priority: Clog.Priority, message: ClogMessageFormatter.() -> String) {
        val tag = tagProvider.get()
        if (filter.shouldLog(priority, tag)) {
            logger.log(priority, tag, message(messageFormatter))
        }
    }

// ClogMessageLogger implementation
// ---------------------------------------------------------------------------------------------------------------------

    override fun v(throwable: Throwable) {
        logException(Clog.Priority.VERBOSE) { throwable }
    }

    override fun d(throwable: Throwable) {
        logException(Clog.Priority.DEBUG) { throwable }
    }

    override fun i(throwable: Throwable) {
        logException(Clog.Priority.INFO) { throwable }
    }

    override fun log(throwable: Throwable) {
        logException(Clog.Priority.DEFAULT) { throwable }
    }

    override fun w(throwable: Throwable) {
        logException(Clog.Priority.WARNING) { throwable }
    }

    override fun e(throwable: Throwable) {
        logException(Clog.Priority.ERROR) { throwable }
    }

    override fun wtf(throwable: Throwable) {
        logException(Clog.Priority.FATAL) { throwable }
    }

    /**
     * Filters and logs a [Throwable]. This is an internal method that should not be called directly. Use the
     * methods from [ClogProfile] or [clog.dsl] instead.
     */
    inline fun logException(priority: Clog.Priority, throwable: () -> Throwable) {
        val tag = tagProvider.get()
        if (filter.shouldLog(priority, tag)) {
            logger.logException(priority, tag, throwable())
        }
    }
}
