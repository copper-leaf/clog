package clog.impl

import clog.Clog
import clog.test.impl.TestLogger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DelegatingLoggerTest {

    @Test
    fun testDelegatingLogger_message() {
        val logger1 = TestLogger()
        val logger2 = TestLogger()
        val logger3 = TestLogger()
        val underTest = DelegatingLogger(listOf(
            null to logger1,
            DefaultFilter() to logger2,
            DefaultFilter(minPriority = Clog.Priority.WARNING) to logger3
        ))

        underTest.log(Clog.Priority.VERBOSE, null, "a message")

        // logged because there was no filter
        assertTrue(logger1.messageWasLogged)
        assertEquals("a message", logger1.lastMessage)

        // logged because it passed its filter
        assertTrue(logger2.messageWasLogged)
        assertEquals("a message", logger2.lastMessage)

        // did not log because it failed its filter
        assertFalse(logger3.messageWasLogged)
        assertNull(logger3.lastMessage)
    }

    @Test
    fun testDelegatingLogger_throwable() {
        val logger1 = TestLogger()
        val logger2 = TestLogger()
        val logger3 = TestLogger()
        val underTest = DelegatingLogger(listOf(
            null to logger1,
            DefaultFilter() to logger2,
            DefaultFilter(minPriority = Clog.Priority.WARNING) to logger3
        ))

        underTest.logException(Clog.Priority.VERBOSE, null, RuntimeException("a message"))

        // logged because there was no filter
        assertTrue(logger1.throwableWasLogged)
        assertEquals("a message", logger1.lastThrowable?.message)

        // logged because it passed its filter
        assertTrue(logger2.throwableWasLogged)
        assertEquals("a message", logger2.lastThrowable?.message)

        // did not log because it failed its filter
        assertFalse(logger3.throwableWasLogged)
        assertNull(logger3.lastThrowable?.message)
    }
}
