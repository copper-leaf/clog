package clog

import clog.dsl.setMinPriority
import clog.impl.AnsiPrintlnLogger
import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.PrintlnLogger
import clog.impl.Slf4jMessageFormatter
import clog.test.impl.clogTest
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlin.test.Test
import kotlin.test.assertEquals

class JvmClogSingletonTest {
    @Test
    fun testDefaultConfiguration() {
        with(Clog.getInstance()) {
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(Slf4jMessageFormatter::class, messageFormatter::class)
            assertEquals(DefaultMessageFormatter::class, (messageFormatter as Slf4jMessageFormatter).delegate::class)
        }
    }

    @Test
    fun testDefaultConfiguration_windows() {
        Clog.setMinPriority(Clog.Priority.WARNING)
        mockkObject(ClogPlatform)
        every { ClogPlatform.getProperty("isWindows") } returns true
        val underTest = ClogProfile()
        Clog.setMinPriority(null)

        with(underTest) {
            assertEquals(PrintlnLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(Slf4jMessageFormatter::class, messageFormatter::class)
            assertEquals(DefaultMessageFormatter::class, (messageFormatter as Slf4jMessageFormatter).delegate::class)
        }

        Clog.setMinPriority(Clog.Priority.WARNING)
        unmockkObject(ClogPlatform)
        Clog.setMinPriority(null)
    }

    @Test
    fun testDefaultConfiguration_supportsAnsiCodes() {
        Clog.setMinPriority(Clog.Priority.WARNING)
        mockkObject(ClogPlatform)
        every { ClogPlatform.getProperty("isWindows") } returns false
        val underTest = ClogProfile()
        Clog.setMinPriority(null)

        with(underTest) {
            assertEquals(AnsiPrintlnLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(Slf4jMessageFormatter::class, messageFormatter::class)
            assertEquals(DefaultMessageFormatter::class, (messageFormatter as Slf4jMessageFormatter).delegate::class)
        }

        Clog.setMinPriority(Clog.Priority.WARNING)
        unmockkObject(ClogPlatform)
        Clog.setMinPriority(null)
    }

    @Test
    fun testBasicLog() {
        clogTest { logger ->
            Clog.v("m1")

            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("JvmClogSingletonTest", logger.lastMessageTag)
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

            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("JvmClogSingletonTest", logger.lastMessageTag)
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

            assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
            assertEquals("JvmClogSingletonTest", logger.lastMessageTag)
            assertEquals("m1", logger.lastMessage)
        }
    }
}
