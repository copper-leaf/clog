package clog.api

interface ClogMessageFormatter {
    fun format(message: String, vararg args: Any?): String
}
