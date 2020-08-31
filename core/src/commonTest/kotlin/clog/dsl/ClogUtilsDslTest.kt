package clog.dsl

import clog.Clog
import clog.ClogProfile
import clog.api.ClogFilter
import clog.test.impl.clogProfileTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClogUtilsDslTest {



    @Test
    fun testClogFormat() {
        val formatted = Clog.format("message 1={}, '2'={}, true={}", 1, "2", true)
        assertEquals("message 1=1, '2'=2, true=true", formatted)
    }

    @Test
    fun testClogShouldLog_false() {
        var tagProvidedToFilter: String? = null
        var priorityProvidedToFilter: Clog.Priority? = null

        val profile = ClogProfile(
            filter = object : ClogFilter {
                override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
                    priorityProvidedToFilter = priority
                    tagProvidedToFilter = tag

                    return false
                }
            }
        )

        clogProfileTest(profile) {
            val shouldLog = Clog.shouldLog(Clog.Priority.VERBOSE, "test tag")
            assertEquals("test tag", tagProvidedToFilter)
            assertEquals(Clog.Priority.VERBOSE, priorityProvidedToFilter)
            assertFalse(shouldLog)
        }
    }

    @Test
    fun testClogShouldLog_true() {
        var tagProvidedToFilter: String? = null
        var priorityProvidedToFilter: Clog.Priority? = null

        val profile = ClogProfile(
            filter = object : ClogFilter {
                override fun shouldLog(priority: Clog.Priority, tag: String?): Boolean {
                    priorityProvidedToFilter = priority
                    tagProvidedToFilter = tag

                    return true
                }
            }
        )

        clogProfileTest(profile) {
            val shouldLog = Clog.shouldLog(Clog.Priority.VERBOSE, "test tag")
            assertEquals("test tag", tagProvidedToFilter)
            assertEquals(Clog.Priority.VERBOSE, priorityProvidedToFilter)
            assertTrue(shouldLog)
        }
    }

}
