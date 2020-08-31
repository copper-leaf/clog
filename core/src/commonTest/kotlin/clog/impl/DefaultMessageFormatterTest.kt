package clog.impl

import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultMessageFormatterTest {



    @Test
    fun testDefaultFormatterFormat() {
        val formatted = DefaultMessageFormatter().format("message 1={}, '2'={}, true={}", 1, "2", true)
        assertEquals("message 1=1, '2'=2, true=true", formatted)
    }
}
