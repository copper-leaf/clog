package clog.api

import clog.Clog

/**
 * Log a message to a platform-specific logging target,
 */
interface ClogLogger {

    /**
     * Log a message at a given [priority] to a platform-specific logging target. The [tag] is already inferred and the
     * [message] already formatted at this point.
     */
    fun log(priority: Clog.Priority, tag: String?, message: String)
}
