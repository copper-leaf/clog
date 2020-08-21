package clog

import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider

/**
 * Infer a tag for a log message, on supported platforms. Typically, this inspects stackframes to determine the calling
 * class.
 *
 * Returns null if the platform does not support a mechanism for inferring a tag, or if there is an error attempting to
 * determine the tag.
 */
expect fun inferCurrentTag(): String?

/**
 * Create a new instance of the platform-specific [ClogLogger].
 */
expect fun createDefaultLogger(): ClogLogger

/**
 * Create a new instance of the platform-specific [ClogTagProvider].
 */
expect fun createDefaultTagProvider(): ClogTagProvider

/**
 * Create a new instance of the platform-specific [ClogFilter].
 */
expect fun createDefaultFilter(): ClogFilter

/**
 * Create a new instance of the platform-specific [ClogMessageFormatter].
 */
expect fun createDefaultMessageFormatter(): ClogMessageFormatter
