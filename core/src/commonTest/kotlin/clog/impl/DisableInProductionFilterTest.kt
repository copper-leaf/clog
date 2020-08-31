package clog.impl

import clog.Clog
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisableInProductionFilterTest {

    @Test
    fun testDisableInProductionFilter_notProduction() {
        val underTest = DisableInProductionFilter(true, DefaultFilter(minPriority = Clog.Priority.WARNING))

        assertFalse(underTest.shouldLog(Clog.Priority.VERBOSE, null))
        assertTrue(underTest.shouldLog(Clog.Priority.WARNING, null))
    }

    @Test
    fun testDisableInProductionFilter_isProduction() {
        val underTest = DisableInProductionFilter(false, DefaultFilter(minPriority = Clog.Priority.WARNING))

        assertFalse(underTest.shouldLog(Clog.Priority.VERBOSE, null))
        assertFalse(underTest.shouldLog(Clog.Priority.WARNING, null))
    }
}
