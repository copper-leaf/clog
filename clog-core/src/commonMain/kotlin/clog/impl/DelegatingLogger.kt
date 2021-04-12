package clog.impl

import clog.Clog
import clog.api.ClogFilter
import clog.api.ClogLogger

/**
 * Passes a log message to multiple other loggers
 */
data class DelegatingLogger(
    val delegates: List<Pair<ClogFilter?, ClogLogger>> = emptyList()
) : ClogLogger {

    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        filteredLoggers(priority, tag).forEach { it.log(priority, tag, message) }
    }

    override fun logException(priority: Clog.Priority, tag: String?, throwable: Throwable) {
        filteredLoggers(priority, tag).forEach { it.logException(priority, tag, throwable) }
    }

    private fun filteredLoggers(priority: Clog.Priority, tag: String?): List<ClogLogger> {
        return delegates
            .filter { it.first == null || it.first!!.shouldLog(priority, tag) }
            .map { it.second }
    }

    /**
     * Returns a copy of this [DelegatingLogger] with [filteredLogger] appended to its list of [delegates].
     */
    operator fun plus(filteredLogger: Pair<ClogFilter?, ClogLogger>): DelegatingLogger {
        return DelegatingLogger(
            delegates + filteredLogger
        )
    }

    /**
     * Returns a copy of this [DelegatingLogger] with [logger] removed from its list of [delegates]. All delegates that
     * are the same instance as [logger] are removed, regardless of their associated [ClogFilter].
     */
    operator fun minus(logger: ClogLogger): DelegatingLogger {
        val newDelegatesList = delegates.filterNot { it.second === logger }

        return DelegatingLogger(
            newDelegatesList
        )
    }
}
