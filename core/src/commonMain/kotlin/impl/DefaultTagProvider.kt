package clog.impl

import clog.api.ClogTagProvider
import clog.inferCurrentTag

/**
 * If a [tag] is not null, use that as the message tag. Otherwise, attempt to infer the tag on supported platforms.
 */
class DefaultTagProvider(private val tag: String? = null) : ClogTagProvider {
    override fun get(): String? {
        return tag ?: inferCurrentTag()
    }
}
