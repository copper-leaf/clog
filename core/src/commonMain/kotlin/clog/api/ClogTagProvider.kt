package clog.api

/**
 * Provides a tag to log with the message. This should be a globally-configured tag stored in the [ClogProfile] or a tag
 * inferred by the platform, if available.
 */
interface ClogTagProvider {

    /**
     * Return the tag that should be logged with the current message.
     */
    fun get(): String?
}
