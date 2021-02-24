package clog.impl

import clog.ClogPlatform
import clog.api.ClogTagProvider

/**
 * If a [tag] is not null, use that as the message tag. Otherwise, attempt to infer the tag on supported platforms.
 */
data class DefaultTagProvider(private val tag: String? = null) : ClogTagProvider {
    override fun get(): String? {
        return tag ?: ClogPlatform.inferCurrentTag()
    }
}
