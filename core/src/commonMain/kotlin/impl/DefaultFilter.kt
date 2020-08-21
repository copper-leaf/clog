package clog.impl

import clog.Clog
import clog.api.ClogFilter
import co.touchlab.stately.collections.IsoMutableCollection
import co.touchlab.stately.collections.IsoMutableSet
import co.touchlab.stately.concurrency.AtomicReference

/**
 * Filters messages based on tag and priority.
 *
 * Messages will be logged if the tag passes both the blacklist and the whitelist, and has a priority greater than or
 * equal to the configured minimum.
 */
class DefaultFilter : ClogFilter {

    private val tagWhitelist: IsoMutableCollection<String> = IsoMutableSet()
    private val tagBlacklist: IsoMutableCollection<String> = IsoMutableSet()
    private var minPriority: AtomicReference<Clog.Priority?> = AtomicReference(null)

    override fun addTagToWhitelist(tag: String) {
        tagWhitelist.add(tag)
    }

    override fun addTagToBlacklist(tag: String) {
        tagBlacklist.add(tag)
    }

    override fun setMinPriority(priority: Clog.Priority) {
        minPriority.set(priority)
    }

    override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
        if (tagWhitelist.isNotEmpty() && tag !in tagWhitelist) return false
        if (tagBlacklist.isNotEmpty() && tag in tagBlacklist) return false
        minPriority.get()?.let { if (priority < it) return false }

        return true
    }
}
