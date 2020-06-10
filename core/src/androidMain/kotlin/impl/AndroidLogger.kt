package clog.impl

import android.util.Log
import clog.Clog
import clog.api.ClogLogger

class AndroidLogger : ClogLogger {
    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        when (priority) {
            Clog.Priority.VERBOSE -> Log.v(tag, message)
            Clog.Priority.DEBUG -> Log.d(tag, message)
            Clog.Priority.INFO -> Log.i(tag, message)
            Clog.Priority.DEFAULT -> Log.i(tag, message)
            Clog.Priority.WARNING -> Log.w(tag, message)
            Clog.Priority.ERROR -> Log.e(tag, message)
            Clog.Priority.FATAL -> Log.wtf(tag, message)
        }
    }
}
