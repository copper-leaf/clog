package clog

import clog.api.ClogMessageFormatter
import clog.api.ClogProfile
import clog.api.IClog
import clog.impl.DefaultTagProvider
import co.touchlab.stately.concurrency.AtomicReference

/**
 * Holds a global instance of [ClogProfile] to process and handle logging message.
 */
object Clog : IClog {
    enum class Priority {
        VERBOSE,
        DEBUG,
        INFO,
        DEFAULT,
        WARNING,
        ERROR,
        FATAL,
    }

    private val INSTANCE: AtomicReference<ClogProfile> = AtomicReference(ClogProfile())

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

    /**
     * Get the current global instance of the ClogProfile.
     */
    fun getInstance(): ClogProfile {
        return INSTANCE.get()
    }

    /**
     * Sets the current global instance of the ClogProfile.
     */
    fun setProfile(profile: ClogProfile) {
        INSTANCE.set(profile)
    }

    /**
     * Returns a copy of the global ClogProfile instance with a specified tag.
     */
    fun tag(tag: String): ClogProfile {
        return INSTANCE.get().copy(tagProvider = DefaultTagProvider(tag))
    }
}
