package clog

import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider
import clog.impl.AnsiPrintlnLogger
import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.InferredTagFinder
import clog.impl.PrintlnLogger
import clog.impl.Slf4jMessageFormatter

actual object ClogPlatform {
    actual fun inferCurrentTag(): String? = InferredTagFinder(
        InferredTagFinder::class.java.name,
        ClogPlatform::class.java.name,
        Clog::class.java.name,
        ClogProfile::class.java.name,
        DefaultTagProvider::class.java.name
    ).findCallerClassName()

    actual fun createDefaultLogger(): ClogLogger = when {
        isWindows -> PrintlnLogger()
        else -> AnsiPrintlnLogger()
    }
    actual fun createDefaultTagProvider(): ClogTagProvider = DefaultTagProvider()
    actual fun createDefaultFilter(): ClogFilter = DefaultFilter()
    actual fun createDefaultMessageFormatter(): ClogMessageFormatter = Slf4jMessageFormatter(DefaultMessageFormatter())

// Implementation helpers
// ---------------------------------------------------------------------------------------------------------------------

    private val isWindows: Boolean
        get() {
            return (System.getProperty("os.name") ?: "").contains("win", ignoreCase = true)
        }
}
