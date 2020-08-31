package clog.api

import clog.Clog
import clog.ClogProfile

/**
 * Determines whether a message should be logged. Only logs passing the filter will continue to be formatted with the
 * [ClogMessageFormatter] and sent to the [ClogLogger].
 */
interface ClogFilter {

    /**
     * Declare a tag that should be added to the whitelist. Only messages with a tag in the whitelist will be logged.
     *
     * Returns a copy of this [ClogFilter] with this change to be set in the [ClogProfile].
     */
    fun addTagToWhitelist(tag: String): ClogFilter = apply {}

    /**
     * Declare a tag that should be added to the blacklist. Only messages with a tag not in the blacklist will be
     * logged.
     *
     * Returns a copy of this [ClogFilter] with this change to be set in the [ClogProfile].
     */
    fun addTagToBlacklist(tag: String): ClogFilter = apply {}

    /**
     * Set the minimum priority for log messages. Only messages with a level greater than or equal to [priority] will be
     * logged.
     *
     * Returns a copy of this [ClogFilter] with this change to be set in the [ClogProfile].
     */
    fun setMinPriority(priority: Clog.Priority): ClogFilter = apply {}

    /**
     * Given a [priority] and [tag], determine if a message should be logged. It should conform to the whitelist,
     * blacklist, and minimum priority, but additional logic may be present as well.
     *
     * Returns true if the message should be logged, and false if it should be dropped.
     */
    fun shouldLog(priority: Clog.Priority, tag: String?): Boolean
}
