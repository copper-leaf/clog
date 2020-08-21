package clog.impl

import clog.api.ClogMessageFormatter
import org.slf4j.MDC

class Slf4jMessageFormatter(
    val delegate: ClogMessageFormatter = DefaultMessageFormatter()
) : ClogMessageFormatter {

    override fun format(message: String, vararg args: Any?): String {
        return message
            .replace(REGEX) { matchResult ->
                val replacementKey = matchResult.groupValues[1]
                val replacement = MDC.get(replacementKey)
                replacement
            }
            .let {
                delegate.format(it, *args)
            }
    }

    companion object {
        val REGEX = """%X\{(.*?)\}""".toRegex()
    }
}
