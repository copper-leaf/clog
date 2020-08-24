package clog

import clog.dsl.addLogger
import clog.dsl.addTagToBlacklist
import clog.dsl.addTagToWhitelist
import clog.dsl.configureLoggingInProduction
import clog.dsl.setMinPriority
import clog.dsl.tag
import clog.impl.TestLogger
import clog.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClogSingletonTest {
    @Test
    fun testMessageLog() {
        clogTest { logger ->
            Clog.v("m1")

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("m1", logger.lastMessage)
        }
    }

    @Test
    fun testMessageLogWithTag() {
        clogTest { logger ->
            Clog.tag("t1").v("m1")

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("t1", logger.lastMessageTag)
            assertEquals("m1", logger.lastMessage)
        }
    }

    @Test
    fun testThrowableLog() {
        clogTest { logger ->
            Clog.v(RuntimeException("throwable 1"))

            assertTrue(logger.throwableWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastThrowablePriority)
            assertEquals("throwable 1", logger.lastThrowable?.message)
        }
    }

    @Test
    fun testThrowableLogWithTag() {
        clogTest { logger ->
            Clog.tag("t1").v(RuntimeException("throwable 2"))

            assertTrue(logger.throwableWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastThrowablePriority)
            assertEquals("t1", logger.lastThrowableTag)
            assertEquals("throwable 2", logger.lastThrowable?.message)
        }
    }

    @Test
    fun testFormattedLog() {
        clogTest { logger ->
            Clog.v("m1: {}", "p1")

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("m1: p1", logger.lastMessage)
        }
    }

    @Test
    fun testFormattedLogMultipleParams() {
        clogTest { logger ->
            Clog.v("m1: {}, {}, {}, {}", "p1", 2, true, "p4" to "p5")

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("m1: p1, 2, true, (p4, p5)", logger.lastMessage)
        }
    }

    @Test
    fun testTagBlacklisting() {
        clogTest { logger ->
            Clog.addTagToBlacklist("t1")

            Clog.tag("t1").v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.tag("t2").v("m2")
            assertTrue(logger.messageWasLogged)
            assertEquals("t2", logger.lastMessageTag)
            assertEquals("m2", logger.lastMessage)
        }
    }

    @Test
    fun testTagWhitelisting() {
        clogTest { logger ->
            Clog.addTagToWhitelist("t2")

            Clog.tag("t1").v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.tag("t2").v("m2")
            assertTrue(logger.messageWasLogged)
            assertEquals("t2", logger.lastMessageTag)
            assertEquals("m2", logger.lastMessage)
        }
    }

    @Test
    fun testMinPriorityFilter() {
        clogTest { logger ->
            Clog.setMinPriority(Clog.Priority.ERROR)

            Clog.v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.e("m2")
            assertTrue(logger.messageWasLogged)
            assertEquals("m2", logger.lastMessage)
        }
    }

    @Test
    fun testLogInProductionFilter_notProd() {
        clogTest { logger ->
            val isDebug = true
            Clog.configureLoggingInProduction(isDebug)

            Clog.v("m1")
            assertTrue(logger.messageWasLogged)

            Clog.e("m2")
            assertTrue(logger.messageWasLogged)
        }
    }

    @Test
    fun testLogInProductionFilter_prod() {
        clogTest { logger ->
            val isDebug = false
            Clog.configureLoggingInProduction(isDebug)

            Clog.v("m1")
            assertFalse(logger.messageWasLogged)

            Clog.e("m2")
            assertFalse(logger.messageWasLogged)
        }
    }

    @Test
    fun testAddLogger() {
        clogTest { logger ->
            val logger1 = TestLogger()
            val logger2 = TestLogger()

            Clog.addLogger(logger1)
            Clog.addLogger(logger2)

            Clog.v("m1")
            assertTrue(logger.messageWasLogged)
            assertTrue(logger1.messageWasLogged)
            assertTrue(logger2.messageWasLogged)

            Clog.e("m2")
            assertTrue(logger.messageWasLogged)
            assertTrue(logger1.messageWasLogged)
            assertTrue(logger2.messageWasLogged)
        }
    }
}
