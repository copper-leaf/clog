package clog.api

import clog.Clog

/**
 * Determines whether a message should be logged. Only logs passing the filter will continue to be formatted with the
 * [ClogMessageFormatter] and sent to the [ClogLogger].
 */
interface ClogFilter {
    /**
     * Declare a tag that should be added to the whitelist. Only messages with a tag in the whitelist will be logged.
     */
    fun addTagToWhitelist(tag: String)

    /**
     * Declare a tag that should be added to the blacklist. Only messages with a tag not in the blacklist will be
     * logged.
     */
    fun addTagToBlacklist(tag: String)

    /**
     * Set the minimum priority for log messages. Only messages with a level greater than or equal to [priority] will be
     * logged.
     */
    fun setMinPriority(priority: Clog.Priority)

    /**
     * Given a [priority] and [tag], determine if a message should be logged. It should conform to the whitelist,
     * blacklist, and minimum priority, but additional logic may be present as well.
     *
     * Returns true if the message should be logged, and false if it should be dropped.
     */
    fun shouldLog(priority: Clog.Priority, tag: String?): Boolean
}
