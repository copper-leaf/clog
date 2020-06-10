package clog.api

import clog.Clog

interface ClogFilter {
    fun addTagToWhitelist(tag: String)
    fun addTagToBlacklist(tag: String)
    fun setMinPriority(priority: Clog.Priority)

    fun shouldLog(priority: Clog.Priority, tag: String?): Boolean
}
