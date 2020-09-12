package clog

import android.util.Log
import clog.dsl.setMinPriority
import clog.impl.AndroidLogger
import clog.impl.AnsiPrintlnLogger
import clog.impl.DefaultFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.impl.PrintlnLogger
import clog.test.impl.clogTest
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
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
    fun testDefaultConfiguration_notInUnitTest() {
        Clog.setMinPriority(Clog.Priority.WARNING)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        val underTest = ClogProfile()
        Clog.setMinPriority(null)

        with(underTest) {
            assertEquals(AndroidLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(DefaultMessageFormatter::class, messageFormatter::class)
        }

        Clog.setMinPriority(Clog.Priority.WARNING)
        unmockkStatic(Log::class)
        Clog.setMinPriority(null)
    }

    @Test
    fun testDefaultConfiguration_inUnitTest_windows() {
        Clog.setMinPriority(Clog.Priority.WARNING)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } throws RuntimeException("")
        mockkObject(ClogPlatform)
        every { ClogPlatform.getProperty("isWindows") } returns true
        val underTest = ClogProfile()
        Clog.setMinPriority(null)

        with(underTest) {
            assertEquals(PrintlnLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(DefaultMessageFormatter::class, messageFormatter::class)
        }

        Clog.setMinPriority(Clog.Priority.WARNING)
        unmockkStatic(Log::class)
        unmockkObject(ClogPlatform)
        Clog.setMinPriority(null)
    }

    @Test
    fun testDefaultConfiguration_inUnitTest_supportsAnsiCodes() {
        Clog.setMinPriority(Clog.Priority.WARNING)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } throws RuntimeException("")
        mockkObject(ClogPlatform)
        every { ClogPlatform.getProperty("isWindows") } returns false
        val underTest = ClogProfile()
        Clog.setMinPriority(null)

        with(underTest) {
            assertEquals(AnsiPrintlnLogger::class, logger::class)
            assertEquals(DefaultTagProvider::class, tagProvider::class)
            assertEquals(DefaultFilter::class, filter::class)
            assertEquals(DefaultMessageFormatter::class, messageFormatter::class)
        }

        Clog.setMinPriority(Clog.Priority.WARNING)
        unmockkStatic(Log::class)
        unmockkObject(ClogPlatform)
        Clog.setMinPriority(null)
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
