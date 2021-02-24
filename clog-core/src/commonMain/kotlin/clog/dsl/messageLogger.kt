package clog.dsl

import clog.Clog
import clog.api.ClogMessageFormatter

// ClogMessageLogger DSL
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
