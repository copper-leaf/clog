package clog

import clog.api.ClogFilter
import clog.api.ClogLogger
import clog.api.ClogMessageFormatter
import clog.api.ClogTagProvider

expect fun inferCurrentTag(): String?

expect fun createDefaultLogger(): ClogLogger
expect fun createDefaultTagProvider(): ClogTagProvider
expect fun createDefaultFilter(): ClogFilter
expect fun createDefaultMessageFormatter(): ClogMessageFormatter
