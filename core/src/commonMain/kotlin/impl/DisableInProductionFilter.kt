package clog.impl

import clog.Clog
import clog.api.ClogFilter

class DisableInProductionFilter(
    private val isDebug: Boolean,
    private val delegate: ClogFilter
) : ClogFilter by delegate {

    override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
        return if (isDebug) delegate.shouldLog(priority, tag) else false
    }
}
