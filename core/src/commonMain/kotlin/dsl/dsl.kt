package clog.dsl

import clog.Clog
import clog.api.ClogMessageFormatter
import clog.maybeTag

inline fun v(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.VERBOSE, message)
}

inline fun d(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.DEBUG, message)
}

inline fun i(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.INFO, message)
}

inline fun log(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.DEFAULT, message)
}

inline fun w(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.WARNING, message)
}

inline fun e(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.ERROR, message)
}

inline fun wtf(tag: String? = null, message: ClogMessageFormatter.() -> String) {
    Clog.maybeTag(tag).log(Clog.Priority.FATAL, message)
}
