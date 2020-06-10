package clog.impl

import clog.api.ClogTagProvider
import clog.inferCurrentTag

class DefaultTagProvider(private val tag: String? = null) : ClogTagProvider {
    override fun get(): String? {
        return tag ?: inferCurrentTag()
    }
}
