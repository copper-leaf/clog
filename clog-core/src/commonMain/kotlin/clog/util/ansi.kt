package clog.util

import clog.Clog

private const val ansiControlCode = '\u001B'
private const val ansiRed = "[31m"
private const val ansiGreen = "[32m"
private const val ansiYellow = "[33m"
private const val ansiBlue = "[34m"
private const val ansiMagenta = "[35m"
private const val ansiCyan = "[36m"
private const val ansiReset = "[0m"

internal val Clog.Priority.ansiColorStart: String
    get() {
        return when (this) {
            Clog.Priority.VERBOSE -> "$ansiControlCode$ansiGreen"
            Clog.Priority.DEBUG -> "$ansiControlCode$ansiBlue"
            Clog.Priority.INFO -> "$ansiControlCode$ansiCyan"
            Clog.Priority.DEFAULT -> ""
            Clog.Priority.WARNING -> "$ansiControlCode$ansiYellow"
            Clog.Priority.ERROR -> "$ansiControlCode$ansiRed"
            Clog.Priority.FATAL -> "$ansiControlCode$ansiMagenta"
        }
    }

internal val Clog.Priority.ansiColorEnd: String
    get() {
        return when (this) {
            Clog.Priority.DEFAULT -> ""
            else -> "$ansiControlCode$ansiReset"
        }
    }
