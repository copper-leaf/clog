package clog.impl

import clog.inferCurrentTag
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultTagProviderTest {

    @Test
    fun testDefaultTagProvider_noTag() {
        assertEquals(inferCurrentTag(), DefaultTagProvider().get())
    }

    @Test
    fun testDefaultTagProvider_withTag() {
        assertEquals("a tag", DefaultTagProvider("a tag").get())
    }
}
