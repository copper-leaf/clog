package clog

import clog.api.ClogFilter
import clog.impl.DefaultMessageFormatter
import clog.impl.DefaultTagProvider
import clog.test.impl.TestLogger
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClogProfileTest {

    @Test
    fun testLogMethod_passesFilter() {
        var tagProvidedToFilter: String? = null
        var priorityProvidedToFilter: Clog.Priority? = null

        val logger = TestLogger()
        val tagProfile = DefaultTagProvider("test tag")
        val filter = object : ClogFilter {
            override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
                priorityProvidedToFilter = priority
                tagProvidedToFilter = tag

                return true
            }
        }
        val messageFormatter = DefaultMessageFormatter()
        val profile = ClogProfile(
            logger,
            tagProfile,
            filter,
            messageFormatter
        )

        profile.log(Clog.Priority.VERBOSE) { "message" }

        assertTrue(logger.messageWasLogged)
        assertEquals("test tag", tagProvidedToFilter)
        assertEquals("test tag", logger.lastMessageTag)
        assertEquals(Clog.Priority.VERBOSE, priorityProvidedToFilter)
        assertEquals(Clog.Priority.VERBOSE, logger.lastMessagePriority)
        assertEquals("message", logger.lastMessage)
    }

    @Test
    fun testLogMethod_doesNotPassFilter() {
        var tagProvidedToFilter: String? = null
        var priorityProvidedToFilter: Clog.Priority? = null

        val logger = TestLogger()
        val tagProfile = DefaultTagProvider("test tag")
        val filter = object : ClogFilter {
            override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
                priorityProvidedToFilter = priority
                tagProvidedToFilter = tag

                return false
            }
        }
        val messageFormatter = DefaultMessageFormatter()
        val profile = ClogProfile(
            logger,
            tagProfile,
            filter,
            messageFormatter
        )

        profile.log(Clog.Priority.VERBOSE) { "message" }

        assertFalse(logger.messageWasLogged)
        assertEquals("test tag", tagProvidedToFilter)
        assertNull(logger.lastMessageTag)
        assertEquals(Clog.Priority.VERBOSE, priorityProvidedToFilter)
        assertNull(logger.lastMessagePriority)
        assertNull(logger.lastMessage)
    }

    @Test
    fun testLogExceptionMethod_passesFilter() {
        var tagProvidedToFilter: String? = null
        var priorityProvidedToFilter: Clog.Priority? = null

        val logger = TestLogger()
        val tagProfile = DefaultTagProvider("test tag")
        val filter = object : ClogFilter {
            override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
                priorityProvidedToFilter = priority
                tagProvidedToFilter = tag

                return true
            }
        }
        val messageFormatter = DefaultMessageFormatter()
        val profile = ClogProfile(
            logger,
            tagProfile,
            filter,
            messageFormatter
        )

        profile.logException(Clog.Priority.VERBOSE) { RuntimeException("message") }

        assertTrue(logger.throwableWasLogged)
        assertEquals("test tag", tagProvidedToFilter)
        assertEquals(Clog.Priority.VERBOSE, priorityProvidedToFilter)

        assertEquals("test tag", logger.lastThrowableTag)
        assertEquals(Clog.Priority.VERBOSE, logger.lastThrowablePriority)
        assertEquals("message", logger.lastThrowable?.message)
    }

    @Test
    fun testLogExceptionMethod_doesNotPassFilter() {
        var tagProvidedToFilter: String? = null
        var priorityProvidedToFilter: Clog.Priority? = null

        val logger = TestLogger()
        val tagProfile = DefaultTagProvider("test tag")
        val filter = object : ClogFilter {
            override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
                priorityProvidedToFilter = priority
                tagProvidedToFilter = tag

                return false
            }
        }
        val messageFormatter = DefaultMessageFormatter()
        val profile = ClogProfile(
            logger,
            tagProfile,
            filter,
            messageFormatter
        )

        profile.logException(Clog.Priority.VERBOSE) { RuntimeException("message") }

        assertFalse(logger.throwableWasLogged)
        assertEquals("test tag", tagProvidedToFilter)
        assertEquals(Clog.Priority.VERBOSE, priorityProvidedToFilter)
        assertNull(logger.lastThrowableTag)
        assertNull(logger.lastThrowablePriority)
        assertNull(logger.lastThrowable)
    }
}
