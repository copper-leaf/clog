package clog

import clog.impl.DefaultFilter
import clog.impl.DefaultLogger
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.Slf4jMessageFormatter
import clog.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals

class JvmClogSingletonTest {
    @Test
    fun testDefaultConfiguration() {
        with(Clog.getInstance()) {
            assertEquals(DefaultLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(Slf4jMessageFormatter::class, messageFormatter::class)
            assertEquals(DefaultMessageFormatter::class, (messageFormatter as Slf4jMessageFormatter).delegate::class)
        }
    }

    @Test
    fun testBasicLog() {
        clogTest { logger ->
            Clog.v("m1")

            assertEquals(Clog.Priority.VERBOSE, logger.lastPriority)
            assertEquals("JvmClogSingletonTest", logger.lastTag)
            assertEquals("m1", logger.lastMessage)
        }
    }

    @Test
    fun testLogInAnonymousObject() {
        clogTest { logger ->
            val o = object {
                fun doLog() {
                    Clog.v("m1")
                }
            }

            o.doLog()

            assertEquals(Clog.Priority.VERBOSE, logger.lastPriority)
            assertEquals("JvmClogSingletonTest", logger.lastTag)
            assertEquals("m1", logger.lastMessage)
        }
    }

    @Test
    fun testLogInLambda() {
        clogTest { logger ->
            val doLog = {
                Clog.v("m1")
            }

            doLog()

            assertEquals(Clog.Priority.VERBOSE, logger.lastPriority)
            assertEquals("JvmClogSingletonTest", logger.lastTag)
            assertEquals("m1", logger.lastMessage)
        }
    }
}
