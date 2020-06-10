package clog.api

interface ClogTagProvider {
    fun get(): String?
}
