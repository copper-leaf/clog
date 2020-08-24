package clog.impl

import clog.Clog
import clog.api.ClogLogger

/**
 * Passes a log message to multiple other loggers
 */
data class DelegatingLogger(
    val delegates: List<ClogLogger> = emptyList()
) : ClogLogger {

    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        delegates.forEach { it.log(priority, tag, message) }
    }

    override fun logException(priority: Clog.Priority, tag: String?, throwable: Throwable) {
        delegates.forEach { it.logException(priority, tag, throwable) }
    }

    /**
     * Returns a copy of this [DelegatingLogger] with [logger] appended to its list of [delegates].
     */
    operator fun plus(logger: ClogLogger): DelegatingLogger {
        return DelegatingLogger(
            delegates + logger
        )
    }
}
