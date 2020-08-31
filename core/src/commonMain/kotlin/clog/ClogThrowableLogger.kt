package clog

/**
 * The entry-point for logging throwables to Clog.
 */
interface ClogThrowableLogger {

    /**
     * Logs a [Throwable] at the [Clog.Priority.VERBOSE] level.
     */
    fun v(throwable: Throwable)

    /**
     * Logs a [Throwable] at the [Clog.Priority.DEBUG] level.
     */
    fun d(throwable: Throwable)

    /**
     * Logs a [Throwable] at the [Clog.Priority.INFO] level.
     */
    fun i(throwable: Throwable)

    /**
     * Logs a [Throwable] at the [Clog.Priority.DEFAULT] level.
     */
    fun log(throwable: Throwable)

    /**
     * Logs a [Throwable] at the [Clog.Priority.WARNING] level.
     */
    fun w(throwable: Throwable)

    /**
     * Logs a [Throwable] at the [Clog.Priority.ERROR] level.
     */
    fun e(throwable: Throwable)

    /**
     * Logs a [Throwable] at the [Clog.Priority.FATAL] level.
     */
    fun wtf(throwable: Throwable)
}
