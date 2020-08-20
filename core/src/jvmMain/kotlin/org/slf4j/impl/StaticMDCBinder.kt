package org.slf4j.impl

import java.util.concurrent.ConcurrentHashMap
import org.slf4j.spi.MDCAdapter

// TODO: add context-data support to core Clog functionality, and have slf4j delegate to that
object StaticMDCBinder {

    @JvmStatic
    fun getSingleton(): StaticMDCBinder = this

    fun getMDCA(): MDCAdapter {
        return ClogMDCAdapter()
    }

    fun getMDCAdapterClassStr(): String {
        return ClogMDCAdapter::class.qualifiedName!!
    }
}

class ClogMDCAdapter : MDCAdapter {

    private val backingMap = ThreadLocal.withInitial { ConcurrentHashMap<String, String>() }

    override fun put(key: String, value: String) {
        backingMap.get()[key] = value
    }

    override fun remove(key: String) {
        backingMap.get().remove(key)
    }

    override fun clear() {
        backingMap.get().clear()
    }

    override fun get(key: String): String? {
        return backingMap.get()[key]
    }

    override fun getCopyOfContextMap(): Map<String, String> {
        return backingMap.get()
    }

    override fun setContextMap(contextMap: Map<String, String>) {
        backingMap.get().clear()
        backingMap.get().putAll(contextMap)
    }
}
