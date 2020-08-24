package clog

import clog.api.ClogMessageFormatter
import co.touchlab.stately.concurrency.AtomicReference

/**
 * Holds a global instance of [ClogProfile] to process and handle logging message.
 */
object Clog : ClogMessageLogger, ClogThrowableLogger {
    enum class Priority {
        VERBOSE,
        DEBUG,
        INFO,
        DEFAULT,
        WARNING,
        ERROR,
        FATAL,
    }

// Configuration
// ---------------------------------------------------------------------------------------------------------------------

    private val INSTANCE: AtomicReference<ClogProfile> = AtomicReference(ClogProfile())

    /**
     * Get the current global instance of the ClogProfile.
     */
    fun getInstance(): ClogProfile {
        return INSTANCE.get()
    }

    /**
     * Sets the current global instance of the ClogProfile.
     */
    fun setInstance(profile: ClogProfile) {
        INSTANCE.set(profile)
    }

// ClogMessageLogger implementation
// ---------------------------------------------------------------------------------------------------------------------

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.VERBOSE] level.
     */
    override fun v(message: String, vararg args: Any?) {
        INSTANCE.get().v(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.DEBUG] level.
     */
    override fun d(message: String, vararg args: Any?) {
        INSTANCE.get().d(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.INFO] level.
     */
    override fun i(message: String, vararg args: Any?) {
        INSTANCE.get().i(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.DEFAULT] level.
     */
    override fun log(message: String, vararg args: Any?) {
        INSTANCE.get().log(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.WARNING] level.
     */
    override fun w(message: String, vararg args: Any?) {
        INSTANCE.get().w(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.ERROR] level.
     */
    override fun e(message: String, vararg args: Any?) {
        INSTANCE.get().e(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.FATAL] level.
     */
    override fun wtf(message: String, vararg args: Any?) {
        INSTANCE.get().wtf(message, *args)
    }

// ClogMessageLogger implementation
// ---------------------------------------------------------------------------------------------------------------------

    /**
     * Logs a [Throwable] at the [Clog.Priority.VERBOSE] level.
     */
    override fun v(throwable: Throwable) {
        INSTANCE.get().v(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.DEBUG] level.
     */
    override fun d(throwable: Throwable) {
        INSTANCE.get().d(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.INFO] level.
     */
    override fun i(throwable: Throwable) {
        INSTANCE.get().i(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.DEFAULT] level.
     */
    override fun log(throwable: Throwable) {
        INSTANCE.get().log(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.WARNING] level.
     */
    override fun w(throwable: Throwable) {
        INSTANCE.get().w(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.ERROR] level.
     */
    override fun e(throwable: Throwable) {
        INSTANCE.get().w(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.VERBOSE] level.
     */
    override fun wtf(throwable: Throwable) {
        INSTANCE.get().wtf(throwable)
    }
}
