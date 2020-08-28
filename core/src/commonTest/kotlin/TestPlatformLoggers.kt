package clog

import clog.api.ClogMessageFormatter
import clog.dsl.tag
import clog.dsl.*
import clog.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class ClogMessageTestCase(
    val doLog: (String, Array<Any?>)->Unit,
    val expectedPriority: Clog.Priority
)

private class ClogMessageTestCaseWithProfile(
    val doLog: ClogProfile.(String, Array<Any?>)->Unit,
    val expectedPriority: Clog.Priority,
    val expectedTag: String
)

private class ClogThrowableTestCase(
    val doLog: (Throwable)->Unit,
    val expectedPriority: Clog.Priority
)

private class ClogThrowableTestCaseWithProfile(
    val doLog: ClogProfile.(Throwable)->Unit,
    val expectedPriority: Clog.Priority,
    val expectedTag: String
)

private class ClogDslMessageTestCase(
    val doLog: (String?, ClogMessageFormatter.() -> String)->Unit,
    val expectedPriority: Clog.Priority,
    val expectedTag: String? = null
)

class TestPlatformLoggers {

// Singleton messages logs
//----------------------------------------------------------------------------------------------------------------------

    @Test
    fun testSingletonLoggingMessage() {
        val tests = listOf(
            ClogMessageTestCase(Clog::v, Clog.Priority.VERBOSE),
            ClogMessageTestCase(Clog::d, Clog.Priority.DEBUG),
            ClogMessageTestCase(Clog::i, Clog.Priority.INFO),
            ClogMessageTestCase(Clog::log, Clog.Priority.DEFAULT),
            ClogMessageTestCase(Clog::w, Clog.Priority.WARNING),
            ClogMessageTestCase(Clog::e, Clog.Priority.ERROR),
            ClogMessageTestCase(Clog::wtf, Clog.Priority.FATAL)
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            testCase.doLog("message $index", emptyArray())
        }

        // unit-test each message
        tests.forEachIndexed { index, testCase ->
            clogTest { logger ->
                testCase.doLog("message $index", emptyArray())
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
            }
        }
    }

    @Test
    fun testSingletonLoggingMessageWithTag() {
        val tests = listOf(
            ClogMessageTestCaseWithProfile(ClogProfile::v, Clog.Priority.VERBOSE, "tag v"),
            ClogMessageTestCaseWithProfile(ClogProfile::d, Clog.Priority.DEBUG, "tag d"),
            ClogMessageTestCaseWithProfile(ClogProfile::i, Clog.Priority.INFO, "tag i"),
            ClogMessageTestCaseWithProfile(ClogProfile::log, Clog.Priority.DEFAULT, "tag log"),
            ClogMessageTestCaseWithProfile(ClogProfile::w, Clog.Priority.WARNING, "tag w"),
            ClogMessageTestCaseWithProfile(ClogProfile::e, Clog.Priority.ERROR, "tag e"),
            ClogMessageTestCaseWithProfile(ClogProfile::wtf, Clog.Priority.FATAL, "tag wtf")
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            with(testCase) {
                Clog.tag(testCase.expectedTag).doLog("message $index", emptyArray())
            }
        }

        // unit-test each message
        tests.forEachIndexed { index, testCase ->
            clogTest(Clog.tag(testCase.expectedTag)) { logger ->
                with(testCase) {
                    Clog.getInstance().doLog("message $index", emptyArray())
                }
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
                assertEquals(testCase.expectedTag, logger.lastMessageTag)
            }
        }
    }

    @Test
    fun testSingletonLoggingMessageWithArgs() {
        val tests = listOf(
            ClogMessageTestCase(Clog::v, Clog.Priority.VERBOSE),
            ClogMessageTestCase(Clog::d, Clog.Priority.DEBUG),
            ClogMessageTestCase(Clog::i, Clog.Priority.INFO),
            ClogMessageTestCase(Clog::log, Clog.Priority.DEFAULT),
            ClogMessageTestCase(Clog::w, Clog.Priority.WARNING),
            ClogMessageTestCase(Clog::e, Clog.Priority.ERROR),
            ClogMessageTestCase(Clog::wtf, Clog.Priority.FATAL)
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            testCase.doLog("message {}", arrayOf(index))
        }

        // unit-test each message
        tests.forEachIndexed { index, testCase ->
            clogTest { logger ->
                testCase.doLog("message {}", arrayOf(index))
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
            }
        }
    }

// Singleton throwable logs
//----------------------------------------------------------------------------------------------------------------------

    @Test
    fun testSingletonLoggingThrowable() {
        val tests = listOf(
            ClogThrowableTestCase(Clog::v, Clog.Priority.VERBOSE),
            ClogThrowableTestCase(Clog::d, Clog.Priority.DEBUG),
            ClogThrowableTestCase(Clog::i, Clog.Priority.INFO),
            ClogThrowableTestCase(Clog::log, Clog.Priority.DEFAULT),
            ClogThrowableTestCase(Clog::w, Clog.Priority.WARNING),
            ClogThrowableTestCase(Clog::e, Clog.Priority.ERROR),
            ClogThrowableTestCase(Clog::wtf, Clog.Priority.FATAL)
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            testCase.doLog(RuntimeException("throwable $index"))
        }

        // unit-test each message
        tests.forEachIndexed { index, testCase ->
            clogTest { logger ->
                testCase.doLog(RuntimeException("throwable $index"))
                assertEquals("throwable $index", logger.lastThrowable?.message)
                assertEquals(testCase.expectedPriority, logger.lastThrowablePriority)
            }
        }
    }

    @Test
    fun testSingletonLoggingThrowableWithTag() {
        val tests = listOf(
            ClogThrowableTestCaseWithProfile(ClogProfile::v, Clog.Priority.VERBOSE, "tag v"),
            ClogThrowableTestCaseWithProfile(ClogProfile::d, Clog.Priority.DEBUG, "tag d"),
            ClogThrowableTestCaseWithProfile(ClogProfile::i, Clog.Priority.INFO, "tag i"),
            ClogThrowableTestCaseWithProfile(ClogProfile::log, Clog.Priority.DEFAULT, "tag log"),
            ClogThrowableTestCaseWithProfile(ClogProfile::w, Clog.Priority.WARNING, "tag w"),
            ClogThrowableTestCaseWithProfile(ClogProfile::e, Clog.Priority.ERROR, "tag e"),
            ClogThrowableTestCaseWithProfile(ClogProfile::wtf, Clog.Priority.FATAL, "tag wtf")
        )

        // test each message on the platform logger (without verification)
        tests.forEachIndexed { index, testCase ->
            with(testCase) {
                Clog.tag(testCase.expectedTag).doLog(RuntimeException("throwable $index"))
            }
        }

        // unit-test each message
        tests.forEachIndexed { index, testCase ->
            clogTest(Clog.tag(testCase.expectedTag)) { logger ->
                with(testCase) {
                    Clog.getInstance().doLog(RuntimeException("throwable $index"))
                }
                assertEquals("throwable $index", logger.lastThrowable?.message)
                assertEquals(testCase.expectedPriority, logger.lastThrowablePriority)
                assertEquals(testCase.expectedTag, logger.lastThrowableTag)
            }
        }
    }

// DSL messages logs
//----------------------------------------------------------------------------------------------------------------------

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
    fun testDslLoggingMessageWithArgs() {
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
                testCase.doLog(null) { "message $index" }
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
            }
        }
    }

}
