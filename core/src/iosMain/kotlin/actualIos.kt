package clog

import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider
import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.NsLogger

actual fun inferCurrentTag(): String? = null

actual fun createDefaultLogger(): ClogLogger = NsLogger()
actual fun createDefaultTagProvider(): ClogTagProvider = DefaultTagProvider()
actual fun createDefaultFilter(): ClogFilter = DefaultFilter()
actual fun createDefaultMessageFormatter(): ClogMessageFormatter = DefaultMessageFormatter()
