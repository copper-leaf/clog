@file:Suppress("NOTHING_TO_INLINE")

package clog.dsl

import clog.Clog
import clog.ClogProfile
import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.impl.DefaultTagProvider
import clog.impl.DelegatingLogger
import clog.impl.DisableInProductionFilter

// Configuration DSL
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Update the current [ClogProfile] instance, returning the new profile to set as the global profile.
 */
inline fun Clog.updateProfile(block: (ClogProfile) -> ClogProfile) {
    setInstance(
        block(getInstance())
    )
}

/**
 * Returns a copy of the global ClogProfile instance with a specified tag.
 */
fun Clog.tag(tag: String): ClogProfile {
    return getInstance().copy(tagProvider = DefaultTagProvider(tag))
}

/**
 * If [tag] is not null, return a copy of the global [ClogProfile] with that tag specified. Otherwise, directly return
 * the global [ClogProfile].
 */
inline fun Clog.maybeTag(tag: String?): ClogProfile {
    return if (tag != null) tag(tag) else getInstance()
}

/**
 * Configures Clog to only log messages in debug builds. If [isDebug] is true, then logs will be filtered normally,
 * delegating to the previously-configured filter. If [isDebug] is false, then all logs will be ignored and no messages
 * will be logged.
 */
inline fun Clog.configureLoggingInProduction(isDebug: Boolean) {
    updateProfile {
        it.copy(filter = DisableInProductionFilter(isDebug, it.filter))
    }
}

/**
 * Adds an additional [ClogLogger] as a logging target.
 *
 * @see [DelegatingLogger.plus]
 */
inline fun Clog.addLogger(logger: ClogLogger, filter: ClogFilter? = null) {
    updateProfile {
        val delegatingLogger = it.logger as? DelegatingLogger ?: DelegatingLogger(listOf(null to it.logger))
        it.copy(logger = delegatingLogger + (filter to logger))
    }
}

/**
 * Removes the given [ClogLogger] as a logging target, if it was previously added. Loggers are removed if they are the
 * same instance.
 *
 * @see [DelegatingLogger.minus]
 */
inline fun Clog.removeLogger(logger: ClogLogger) {
    updateProfile {
        if (it.logger is DelegatingLogger) {
            it.copy(logger = it.logger - logger)
        } else {
            it
        }
    }
}

// Filter Configuration
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Declare a tag that should be added to the whitelist. Only messages with a tag in the whitelist will be logged.
 *
 * Returns a copy of this [ClogFilter] with this change to be set in the [ClogProfile].
 */
fun Clog.addTagToWhitelist(tag: String) {
    updateProfile {
        it.copy(filter = it.filter.addTagToWhitelist(tag))
    }
}

/**
 * Declare a tag that should be added to the blacklist. Only messages with a tag not in the blacklist will be
 * logged.
 *
 * Returns a copy of this [ClogFilter] with this change to be set in the [ClogProfile].
 */
fun Clog.addTagToBlacklist(tag: String) {
    updateProfile {
        it.copy(filter = it.filter.addTagToBlacklist(tag))
    }
}

/**
 * Set the minimum priority for log messages. Only messages with a level greater than or equal to [priority] will be
 * logged.
 *
 * Returns a copy of this [ClogFilter] with this change to be set in the [ClogProfile].
 */
fun Clog.setMinPriority(priority: Clog.Priority?) {
    updateProfile {
        it.copy(filter = it.filter.setMinPriority(priority))
    }
}
