package clog.impl

import clog.api.ClogMessageFormatter

class DefaultMessageFormatter : ClogMessageFormatter {
    companion object {
        val pattern = """\{}""".toRegex()
    }

    override fun format(message: String, vararg args: Any?): String {
        return buildString {
            var previousIndex = 0
            var index = 0

            // for each match in the input, replace it with the corresponding arg at that same position
            pattern.findAll(message).forEach { match ->
                append(message.substring(previousIndex, match.range.first))
                append(args.getOrNull(index)?.toString())

                previousIndex = match.range.last + 1
                index++
            }

            // append message tail
            append(message.substring(previousIndex))
        }
    }
}
