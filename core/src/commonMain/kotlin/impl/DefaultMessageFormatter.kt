package clog.impl

import clog.api.ClogMessageFormatter

/**
 * Replaces `{}` placeholders in the message with positional args.
 */
class DefaultMessageFormatter : ClogMessageFormatter {
    override fun format(message: String, vararg args: Any?): String {
        var index = 0
        return message
            .replace(REGEX) {
                val replacement = args.getOrNull(index)
                (replacement?.toString() ?: "").also {
                    index++
                }
            }
    }

    companion object {
        val REGEX = """\{\}""".toRegex()
    }
}
