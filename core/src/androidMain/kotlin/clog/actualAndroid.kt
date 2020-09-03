package clog

import android.util.Log
import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider
import clog.impl.AndroidLogger
import clog.impl.DefaultFilter
import clog.impl.DefaultLogger
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.util.InferredTagFinder

actual fun inferCurrentTag(): String? = InferredTagFinder(
    InferredTagFinder::class.java.name,
    "clog.ActualAndroidKt",
    Clog::class.java.name,
    ClogProfile::class.java.name,
    DefaultTagProvider::class.java.name
).findCallerClassName()

internal val isRunningInUnitTest: Boolean get() {
    // if an exception was thrown while logging, we must be in a unit test with a mocked android.jar
    return runCatching { Log.d("", "") }.isFailure
}

actual fun createDefaultLogger(): ClogLogger = if (isRunningInUnitTest) DefaultLogger() else AndroidLogger()
actual fun createDefaultTagProvider(): ClogTagProvider = DefaultTagProvider()
actual fun createDefaultFilter(): ClogFilter = DefaultFilter()
actual fun createDefaultMessageFormatter(): ClogMessageFormatter = DefaultMessageFormatter()
