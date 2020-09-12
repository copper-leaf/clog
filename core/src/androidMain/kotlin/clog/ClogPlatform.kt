package clog

import android.util.Log
import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider
import clog.impl.AndroidLogger
import clog.impl.AnsiPrintlnLogger
import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.PrintlnLogger
import clog.util.InferredTagFinder

actual object ClogPlatform {
    actual fun inferCurrentTag(): String? = InferredTagFinder(
        InferredTagFinder::class.java.name,
        ClogPlatform::class.java.name,
        Clog::class.java.name,
        ClogProfile::class.java.name,
        DefaultTagProvider::class.java.name
    ).findCallerClassName()

    actual fun createDefaultLogger(): ClogLogger = when {
        !isRunningInUnitTest -> AndroidLogger()
        isWindows -> PrintlnLogger()
        else -> AnsiPrintlnLogger()
    }
    actual fun createDefaultTagProvider(): ClogTagProvider = DefaultTagProvider()
    actual fun createDefaultFilter(): ClogFilter = DefaultFilter()
    actual fun createDefaultMessageFormatter(): ClogMessageFormatter = DefaultMessageFormatter()

// Implementation helpers
// ---------------------------------------------------------------------------------------------------------------------

    private val isWindows: Boolean
        get() {
            return (System.getProperty("os.name") ?: "").contains("win", ignoreCase = true)
        }
    private val isRunningInUnitTest: Boolean
        get() {
            // if an exception was thrown while logging, we must be in a unit test with a mocked android.jar
            return runCatching { Log.d("", "") }.isFailure
        }
}
