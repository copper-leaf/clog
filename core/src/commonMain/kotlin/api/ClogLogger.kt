package clog.api

import clog.Clog

interface ClogLogger {
    fun log(priority: Clog.Priority, tag: String?, message: String)
}
