package clog.impl

import clog.Clog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultFilterTest {

    @Test
    fun testFilterWithoutConfiguration() {
        val underTest = DefaultFilter()

        assertTrue(underTest.shouldLog(Clog.Priority.VERBOSE, null))
        assertTrue(underTest.shouldLog(Clog.Priority.DEBUG, null))
        assertTrue(underTest.shouldLog(Clog.Priority.INFO, null))
        assertTrue(underTest.shouldLog(Clog.Priority.DEFAULT, null))
        assertTrue(underTest.shouldLog(Clog.Priority.WARNING, null))
        assertTrue(underTest.shouldLog(Clog.Priority.ERROR, null))
        assertTrue(underTest.shouldLog(Clog.Priority.FATAL, null))
    }

    @Test
    fun testMinPriority_warning() {
        val underTest = DefaultFilter(minPriority = Clog.Priority.WARNING)

        assertFalse(underTest.shouldLog(Clog.Priority.VERBOSE, null))
        assertFalse(underTest.shouldLog(Clog.Priority.DEBUG, null))
        assertFalse(underTest.shouldLog(Clog.Priority.INFO, null))
        assertFalse(underTest.shouldLog(Clog.Priority.DEFAULT, null))
        assertTrue(underTest.shouldLog(Clog.Priority.WARNING, null))
        assertTrue(underTest.shouldLog(Clog.Priority.ERROR, null))
        assertTrue(underTest.shouldLog(Clog.Priority.FATAL, null))
    }

    @Test
    fun testBlacklistTag() {
        val underTest = DefaultFilter(tagBlacklist = listOf("don't log me", "don't log me, either"))

        assertTrue(underTest.shouldLog(Clog.Priority.VERBOSE, null))
        assertTrue(underTest.shouldLog(Clog.Priority.VERBOSE, "please log me"))
        assertFalse(underTest.shouldLog(Clog.Priority.VERBOSE, "don't log me"))
        assertFalse(underTest.shouldLog(Clog.Priority.VERBOSE, "don't log me, either"))
    }

    @Test
    fun testWhitelistTag() {
        val underTest = DefaultFilter(tagWhitelist = listOf("only log me", "don't forget about me!"))

        assertFalse(underTest.shouldLog(Clog.Priority.VERBOSE, null))
        assertFalse(underTest.shouldLog(Clog.Priority.VERBOSE, "don't log me"))
        assertTrue(underTest.shouldLog(Clog.Priority.VERBOSE, "only log me"))
        assertTrue(underTest.shouldLog(Clog.Priority.VERBOSE, "don't forget about me!"))
    }

    @Test
    fun testAddTagToWhitelist() {
        val original = DefaultFilter(tagBlacklist = listOf("two"))
        val updated = original.addTagToWhitelist("one")

        assertEquals(DefaultFilter(tagWhitelist = listOf("one"), tagBlacklist = listOf("two")), updated)
    }

    @Test
    fun testAddTagToBlacklist() {
        val original = DefaultFilter(tagWhitelist = listOf("two"))
        val updated = original.addTagToBlacklist("one")

        assertEquals(DefaultFilter(tagWhitelist = listOf("two"), tagBlacklist = listOf("one")), updated)
    }

    @Test
    fun testSetMinPriority() {
        val original = DefaultFilter(tagWhitelist = listOf("two"))
        val updated = original.setMinPriority(Clog.Priority.WARNING)

        assertEquals(DefaultFilter(tagWhitelist = listOf("two"), minPriority = Clog.Priority.WARNING), updated)
    }
}
