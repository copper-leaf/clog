package clog

import clog.dsl.tag
import clog.test.impl.clogProfileTest
import clog.test.impl.clogTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ClogTest {

// Singleton message logs with no tag
// ---------------------------------------------------------------------------------------------------------------------

    private class ClogMessageTestCase(
        val doLog: (String, Array<Any?>) -> Unit,
        val expectedPriority: Clog.Priority
    )

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
    fun testSingletonLoggingMessageWithOneArg() {
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

    @Test
    fun testSingletonLoggingMessageWithMultipleArgs() {
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
            testCase.doLog("message index={}, 1={}, '2'={}, true={}", arrayOf(index, 1, "2", true))
        }

        // unit-test each message
        tests.forEachIndexed { index, testCase ->
            clogTest { logger ->
                testCase.doLog("message index={}, 1={}, '2'={}, true={}", arrayOf(index, 1, "2", true))
                assertEquals("message index=$index, 1=1, '2'=2, true=true", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
            }
        }
    }

// Singleton message logs with tag
// ---------------------------------------------------------------------------------------------------------------------

    private class ClogMessageTestCaseWithProfile(
        val doLog: ClogProfile.(String, Array<Any?>) -> Unit,
        val expectedPriority: Clog.Priority,
        val expectedTag: String
    )

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
            clogProfileTest(Clog.tag(testCase.expectedTag)) { logger ->
                with(testCase) {
                    Clog.getInstance().doLog("message $index", emptyArray())
                }
                assertEquals("message $index", logger.lastMessage)
                assertEquals(testCase.expectedPriority, logger.lastMessagePriority)
                assertEquals(testCase.expectedTag, logger.lastMessageTag)
            }
        }
    }

// Singleton throwable logs with no tag
// ---------------------------------------------------------------------------------------------------------------------

    private class ClogThrowableTestCase(
        val doLog: (Throwable) -> Unit,
        val expectedPriority: Clog.Priority
    )

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

// Singleton throwable logs with tag
// ---------------------------------------------------------------------------------------------------------------------

    private class ClogThrowableTestCaseWithProfile(
        val doLog: ClogProfile.(Throwable) -> Unit,
        val expectedPriority: Clog.Priority,
        val expectedTag: String
    )

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
            clogProfileTest(Clog.tag(testCase.expectedTag)) { logger ->
                with(testCase) {
                    Clog.getInstance().doLog(RuntimeException("throwable $index"))
                }
                assertEquals("throwable $index", logger.lastThrowable?.message)
                assertEquals(testCase.expectedPriority, logger.lastThrowablePriority)
                assertEquals(testCase.expectedTag, logger.lastThrowableTag)
            }
        }
    }
}
