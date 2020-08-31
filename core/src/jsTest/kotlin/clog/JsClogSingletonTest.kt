package clog

import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.JsConsoleLogger
import kotlin.test.Test
import kotlin.test.assertEquals

class JsClogSingletonTest {
    @Test
    fun testDefaultConfiguration() {
        with(Clog.getInstance()) {
            assertEquals(JsConsoleLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(DefaultMessageFormatter::class, messageFormatter::class)
        }
    }
}
