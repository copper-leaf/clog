package clog.dsl

import clog.Clog
import clog.api.ClogMessageFormatter
import clog.test.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ClogMessageLoggerDslTest {

// DSL message logs
// ---------------------------------------------------------------------------------------------------------------------

    private class ClogDslMessageTestCase(
        val doLog: (String?, ClogMessageFormatter.() -> String) -> Unit,
        val expectedPriority: Clog.Priority,
        val expectedTag: String? = null
    )

    @Test
    fun testDslLoggingMessage() {
        val tests = listOf(
            ClogDslMessageTestCase(::v, Clog.Priority.VERBOSE),
            ClogDslMessageTestCase(::d, Clog.Priority.DEBUG),
            ClogDslMessageTestCase(::i, Clog.Priority.INFO),
            ClogDslMessageTestCase(::log, Clog.Priority.DEFAULT),
            ClogDslMessageTestCase(::w, Clog.Priority.WARNING),
            ClogDslMessageTestCase(::e, Clog.Priority.ERROR),
            ClogDslMessageTestCase(::wtf, Clog.Priority.FATAL)
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            testCase.doLog(null) { "message $index" }
        }

        // unit-test each message
        clogTest { logger ->
            tests.forEachIndexed { index, testCase ->
                testCase.doLog(null) { "message $index" }
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
            }
        }
    }

    @Test
    fun testDslLoggingMessageWithTag() {
        val tests = listOf(
            ClogDslMessageTestCase(::v, Clog.Priority.VERBOSE, "tag v"),
            ClogDslMessageTestCase(::d, Clog.Priority.DEBUG, "tag d"),
            ClogDslMessageTestCase(::i, Clog.Priority.INFO, "tag i"),
            ClogDslMessageTestCase(::log, Clog.Priority.DEFAULT, "tag log"),
            ClogDslMessageTestCase(::w, Clog.Priority.WARNING, "tag w"),
            ClogDslMessageTestCase(::e, Clog.Priority.ERROR, "tag e"),
            ClogDslMessageTestCase(::wtf, Clog.Priority.FATAL, "tag wtf")
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            testCase.doLog(testCase.expectedTag) { "message $index" }
        }

        // unit-test each message
        clogTest { logger ->
            tests.forEachIndexed { index, testCase ->
                testCase.doLog(testCase.expectedTag) { "message $index" }
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
                assertEquals(testCase.expectedTag, logger.lastMessageTag)
            }
        }
    }

    @Test
    fun testDslLoggingMessageWithOneArg() {
        val tests = listOf(
            ClogDslMessageTestCase(::v, Clog.Priority.VERBOSE),
            ClogDslMessageTestCase(::d, Clog.Priority.DEBUG),
            ClogDslMessageTestCase(::i, Clog.Priority.INFO),
            ClogDslMessageTestCase(::log, Clog.Priority.DEFAULT),
            ClogDslMessageTestCase(::w, Clog.Priority.WARNING),
            ClogDslMessageTestCase(::e, Clog.Priority.ERROR),
            ClogDslMessageTestCase(::wtf, Clog.Priority.FATAL)
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            testCase.doLog(null) { format("message {}", index) }
        }

        // unit-test each message
        clogTest { logger ->
            tests.forEachIndexed { index, testCase ->
                testCase.doLog(null) { format("message {}", index) }
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
            }
        }
    }

    @Test
    fun testDslLoggingMessageWithMultipleArgs() {
        val tests = listOf(
            ClogDslMessageTestCase(::v, Clog.Priority.VERBOSE),
            ClogDslMessageTestCase(::d, Clog.Priority.DEBUG),
            ClogDslMessageTestCase(::i, Clog.Priority.INFO),
            ClogDslMessageTestCase(::log, Clog.Priority.DEFAULT),
            ClogDslMessageTestCase(::w, Clog.Priority.WARNING),
            ClogDslMessageTestCase(::e, Clog.Priority.ERROR),
            ClogDslMessageTestCase(::wtf, Clog.Priority.FATAL)
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            testCase.doLog(null) { format("message index={}, 1={}, '2'={}, true={}", index, 1, "2", true) }
        }

        // unit-test each message
        clogTest { logger ->
            tests.forEachIndexed { index, testCase ->
                testCase.doLog(null) { format("message index={}, 1={}, '2'={}, true={}", index, 1, "2", true) }
                assertEquals("message index=$index, 1=1, '2'=2, true=true", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
            }
        }
    }
}
