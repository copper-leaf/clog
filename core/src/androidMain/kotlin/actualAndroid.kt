package clog

import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogProfile
import clog.api.ClogTagProvider
import clog.impl.AndroidLogger
import clog.impl.DefaultFilter
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

actual fun createDefaultLogger(): ClogLogger = AndroidLogger()
actual fun createDefaultTagProvider(): ClogTagProvider = DefaultTagProvider()
actual fun createDefaultFilter(): ClogFilter = DefaultFilter()
actual fun createDefaultMessageFormatter(): ClogMessageFormatter = DefaultMessageFormatter()
