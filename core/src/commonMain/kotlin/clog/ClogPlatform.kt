package clog

import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider

/**
 * Acutal/expect declarations for Clog
 */
expect object ClogPlatform {

    /**
     * Infer a tag for a log message, on supported platforms. Typically, this inspects stackframes to determine the calling
     * class.
     *
     * Returns null if the platform does not support a mechanism for inferring a tag, or if there is an error attempting to
     * determine the tag.
     */
    fun inferCurrentTag(): String?

    /**
     * Create a new instance of the platform-specific [ClogLogger]. This may be conditional, returning different
     * implementations based on conditions like OS and ANSI control code support.
     */
    fun createDefaultLogger(): ClogLogger

    /**
     * Create a new instance of the platform-specific [ClogTagProvider].
     */
    fun createDefaultTagProvider(): ClogTagProvider

    /**
     * Create a new instance of the platform-specific [ClogFilter].
     */
    fun createDefaultFilter(): ClogFilter

    /**
     * Create a new instance of the platform-specific [ClogMessageFormatter].
     */
    fun createDefaultMessageFormatter(): ClogMessageFormatter
}
