package clog

import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.NsLogger
import kotlin.test.Test
import kotlin.test.assertEquals

class IosClogSingletonTest {
    @Test
    fun testDefaultConfiguration() {
        with(Clog.getInstance()) {
            assertEquals(NsLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(DefaultMessageFormatter::class, messageFormatter::class)
        }
    }

    @Test
    fun testBasicLog() {
        clogTest { logger ->
            Clog.v("m1")

            assertEquals(Clog.Priority.VERBOSE, logger.lastPriority)
            assertEquals(null, logger.lastTag)
            assertEquals("m1", logger.lastMessage)
        }
    }
}
