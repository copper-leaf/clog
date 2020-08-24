package clog

import clog.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC

class JvmClogSlf4jTest {
    @Test
    fun testBasicLog() {
        clogTest { logger ->
            val slf4j: Logger = LoggerFactory.getLogger(JvmClogSlf4jTest::class.java)

            slf4j.trace("m1")

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("clog.JvmClogSlf4jTest", logger.lastMessageTag)
            assertEquals("m1", logger.lastMessage)
        }
    }

    @Test
    fun testLogInAnonymousObject() {
        clogTest { logger ->
            val slf4j: Logger = LoggerFactory.getLogger(JvmClogSlf4jTest::class.java)

            val o = object {
                fun doLog() {
                    slf4j.trace("m1")
                }
            }

            o.doLog()

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("clog.JvmClogSlf4jTest", logger.lastMessageTag)
            assertEquals("m1", logger.lastMessage)
        }
    }

    @Test
    fun testLogInLambda() {
        clogTest { logger ->
            val slf4j: Logger = LoggerFactory.getLogger(JvmClogSlf4jTest::class.java)

            val doLog = {
                slf4j.trace("m1")
            }

            doLog()

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("clog.JvmClogSlf4jTest", logger.lastMessageTag)
            assertEquals("m1", logger.lastMessage)
        }
    }

    @Test
    fun testMdc() {
        clogTest { logger ->
            val slf4j: Logger = LoggerFactory.getLogger(JvmClogSlf4jTest::class.java)

            MDC.put("akey", "avalue")
            slf4j.trace("m1 %X{akey}")

            assertTrue(logger.messageWasLogged)
            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("clog.JvmClogSlf4jTest", logger.lastMessageTag)
            assertEquals("m1 avalue", logger.lastMessage)

            MDC.clear()
        }
    }
}
