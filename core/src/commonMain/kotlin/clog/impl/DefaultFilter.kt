package clog.impl

import clog.Clog
import clog.api.ClogFilter

/**
 * Filters messages based on tag and priority.
 *
 * Messages will be logged if the tag passes both the blacklist and the whitelist, and has a priority greater than or
 * equal to the configured minimum.
 */
data class DefaultFilter(
    private val tagWhitelist: List<String> = emptyList(),
    private val tagBlacklist: List<String> = emptyList(),
    private val minPriority: Clog.Priority? = null
) : ClogFilter {

    override fun addTagToWhitelist(tag: String): DefaultFilter {
        return copy(tagWhitelist = tagWhitelist + tag)
    }

    override fun addTagToBlacklist(tag: String): DefaultFilter {
        return copy(tagBlacklist = tagBlacklist + tag)
    }

    override fun setMinPriority(priority: Clog.Priority): DefaultFilter {
        return copy(minPriority = priority)
    }

    override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
        if (tagWhitelist.isNotEmpty() && tag !in tagWhitelist) return false
        if (tagBlacklist.isNotEmpty() && tag in tagBlacklist) return false
        minPriority?.let { if (priority < it) return false }

        return true
    }
}
