@file:Suppress("NOTHING_TO_INLINE")
package clog.dsl

import clog.Clog
import clog.api.ClogMessageFormatter
import clog.api.ClogProfile
import clog.impl.DisableInProductionFilter

// Logging DSL
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Log an unformatted message at the [Clog.Priority.VERBOSE] level. The global [ClogMessageFormatter] is provided to the
 * lambda to allow the `format()` method to be called if message formatting is needed.
 */
inline fun v(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.VERBOSE, message)
}

/**
 * Log an unformatted message at the [Clog.Priority.DEBUG] level. The global [ClogMessageFormatter] is provided to the
 * lambda to allow the `format()` method to be called if message formatting is needed.
 */
inline fun d(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.DEBUG, message)
}

/**
 * Log an unformatted message at the [Clog.Priority.INFO] level. The global [ClogMessageFormatter] is provided to the
 * lambda to allow the `format()` method to be called if message formatting is needed.
 */
inline fun i(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.INFO, message)
}

/**
 * Log an unformatted message at the [Clog.Priority.DEFAULT] level. The global [ClogMessageFormatter] is provided to the
 * lambda to allow the `format()` method to be called if message formatting is needed.
 */
inline fun log(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.DEFAULT, message)
}

/**
 * Log an unformatted message at the [Clog.Priority.WARNING] level. The global [ClogMessageFormatter] is provided to the
 * lambda to allow the `format()` method to be called if message formatting is needed.
 */
inline fun w(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.WARNING, message)
}

/**
 * Log an unformatted message at the [Clog.Priority.ERROR] level. The global [ClogMessageFormatter] is provided to the
 * lambda to allow the `format()` method to be called if message formatting is needed.
 */
inline fun e(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.ERROR, message)
}

/**
 * Log an unformatted message at the [Clog.Priority.FATAL] level. The global [ClogMessageFormatter] is provided to the
 * lambda to allow the `format()` method to be called if message formatting is needed.
 */
inline fun wtf(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.FATAL, message)
}

// Configuration DSL
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Update the current [ClogProfile] instance, returning the new profile to set as the global profile.
 */
inline fun Clog.updateProfile(block: (ClogProfile) -> ClogProfile) {
    setProfile(
        block(getInstance())
    )
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
