package clog.impl

import clog.Clog
import clog.api.ClogLogger
import platform.Foundation.NSLog

class NsLogger : ClogLogger {
    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        DefaultLogger.getDefaultLogMessage(priority, tag, message).also { NSLog(it) }
    }
}
