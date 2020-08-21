package clog.api

import clog.Clog

/**
 * The entry-point for logging messages to Clog.
 */
interface IClog {

    /**
     * Logs a message at the [Clog.Priority.VERBOSE] level with additional positional args.
     */
    fun v(message: String, vararg args: Any?)

    /**
     * Logs a message at the [Clog.Priority.DEBUG] level with additional positional args.
     */
    fun d(message: String, vararg args: Any?)

    /**
     * Logs a message at the [Clog.Priority.INFO] level with additional positional args.
     */
    fun i(message: String, vararg args: Any?)

    /**
     * Logs a message at the [Clog.Priority.DEFAULT] level with additional positional args.
     */
    fun log(message: String, vararg args: Any?)

    /**
     * Logs a message at the [Clog.Priority.WARNING] level with additional positional args.
     */
    fun w(message: String, vararg args: Any?)

    /**
     * Logs a message at the [Clog.Priority.ERROR] level with additional positional args.
     */
    fun e(message: String, vararg args: Any?)

    /**
     * Logs a message at the [Clog.Priority.FATAL] level with additional positional args.
     */
    fun wtf(message: String, vararg args: Any?)
}
