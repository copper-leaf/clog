package clog.impl

import clog.Clog
import kotlin.test.Test

// all Loggers do is produce side-effects, there's no way to verify it
class DefaultLoggerTest {

    @Test
    fun testDefaultLogger_noTag() {
        DefaultLogger().log(Clog.Priority.VERBOSE, null, "a message")
    }

    @Test
    fun testDefaultLogger_withTag() {
        DefaultLogger().log(Clog.Priority.VERBOSE, "a tag", "a message")
    }
}
