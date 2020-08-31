@file:Suppress("NOTHING_TO_INLINE")

package clog.dsl

import clog.Clog
import clog.api.ClogLogger
import clog.ClogProfile
import clog.api.ClogFilter
import clog.impl.DefaultTagProvider
import clog.impl.DelegatingLogger
import clog.impl.DisableInProductionFilter
import kotlin.jvm.JvmStatic

// Helper methods
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Formats a message with positional args and any other contextual information.
 */
fun Clog.format(message: String, vararg args: Any?): String {
    return getInstance().messageFormatter.format(message, *args)
}

/**
 * Given a [priority] and [tag], determine if a message should be logged. It should conform to the whitelist,
 * blacklist, and minimum priority, but additional logic may be present as well.
 *
 * Returns true if the message should be logged, and false if it should be dropped.
 */
fun Clog.shouldLog(priority: Clog.Priority, tag: String?): Boolean {
    return getInstance().filter.shouldLog(priority, tag)
}
