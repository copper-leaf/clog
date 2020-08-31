package clog

import clog.impl.AndroidLogger
import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.test.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidClogSingletonTest {
    @Test
    fun testDefaultConfiguration() {
        with(Clog.getInstance()) {
            assertEquals(AndroidLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(DefaultMessageFormatter::class, messageFormatter::class)
        }
    }

    @Test
    fun testBasicLog() {
        clogTest { logger ->
            Clog.v("m1")

            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("AndroidClogSingletonTest", logger.lastMessageTag)
            assertEquals("m1", logger.lastMessage)
        }
    }
}
