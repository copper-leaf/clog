package clog

import clog.api.ClogMessageFormatter
import co.touchlab.stately.concurrency.AtomicReference
import kotlin.jvm.JvmStatic

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

    private val atomicInstance: AtomicReference<ClogProfile> = AtomicReference(ClogProfile())

    /**
     * Get the current global instance of the ClogProfile.
     */
    @JvmStatic
    fun getInstance(): ClogProfile {
        return atomicInstance.get()
    }

    /**
     * Sets the current global instance of the ClogProfile.
     */
    @JvmStatic
    fun setInstance(profile: ClogProfile) {
        atomicInstance.set(profile)
    }

// ClogMessageLogger implementation
// ---------------------------------------------------------------------------------------------------------------------

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.VERBOSE] level.
     */
    // @JvmStatic
    override fun v(message: String, vararg args: Any?) {
        getInstance().v(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.DEBUG] level.
     */
    // @JvmStatic
    override fun d(message: String, vararg args: Any?) {
        getInstance().d(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.INFO] level.
     */
    // @JvmStatic
    override fun i(message: String, vararg args: Any?) {
        getInstance().i(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.DEFAULT] level.
     */
    // @JvmStatic
    override fun log(message: String, vararg args: Any?) {
        getInstance().log(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.WARNING] level.
     */
    // @JvmStatic
    override fun w(message: String, vararg args: Any?) {
        getInstance().w(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.ERROR] level.
     */
    // @JvmStatic
    override fun e(message: String, vararg args: Any?) {
        getInstance().e(message, *args)
    }

    /**
     * Log a message (formatted with the global [ClogMessageFormatter]) at the [Clog.Priority.FATAL] level.
     */
    // @JvmStatic
    override fun wtf(message: String, vararg args: Any?) {
        getInstance().wtf(message, *args)
    }

// ClogMessageLogger implementation
// ---------------------------------------------------------------------------------------------------------------------

    /**
     * Logs a [Throwable] at the [Clog.Priority.VERBOSE] level.
     */
    // @JvmStatic
    override fun v(throwable: Throwable) {
        getInstance().v(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.DEBUG] level.
     */
    // @JvmStatic
    override fun d(throwable: Throwable) {
        getInstance().d(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.INFO] level.
     */
    // @JvmStatic
    override fun i(throwable: Throwable) {
        getInstance().i(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.DEFAULT] level.
     */
    // @JvmStatic
    override fun log(throwable: Throwable) {
        getInstance().log(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.WARNING] level.
     */
    // @JvmStatic
    override fun w(throwable: Throwable) {
        getInstance().w(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.ERROR] level.
     */
    // @JvmStatic
    override fun e(throwable: Throwable) {
        getInstance().e(throwable)
    }

    /**
     * Logs a [Throwable] at the [Clog.Priority.VERBOSE] level.
     */
    // @JvmStatic
    override fun wtf(throwable: Throwable) {
        getInstance().wtf(throwable)
    }
}
