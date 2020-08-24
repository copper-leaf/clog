package clog

import clog.dsl.tag
import kotlin.test.Test

class TestPlatformLoggers {
    @Test
    fun testLoggingMessage() {
        val message = "message 1"

        Clog.v(message)
        Clog.d(message)
        Clog.i(message)
        Clog.log(message)
        Clog.w(message)
        Clog.e(message)
        Clog.wtf(message)
    }

    @Test
    fun testLoggingMessageWithTag() {
        val tag = "tag 2"
        val message = "message 2"

        Clog.tag(tag).v(message)
        Clog.tag(tag).d(message)
        Clog.tag(tag).i(message)
        Clog.tag(tag).log(message)
        Clog.tag(tag).w(message)
        Clog.tag(tag).e(message)
        Clog.tag(tag).wtf(message)
    }

    @Test
    fun testLoggingThrowable() {
        val throwable = RuntimeException("throwable 3")

        Clog.v(throwable)
        Clog.d(throwable)
        Clog.i(throwable)
        Clog.log(throwable)
        Clog.w(throwable)
        Clog.e(throwable)
        Clog.wtf(throwable)
    }

    @Test
    fun testLoggingThrowableWithTag() {
        val tag = "tag 4"
        val throwable = RuntimeException("throwable 4")

        Clog.tag(tag).v(throwable)
        Clog.tag(tag).d(throwable)
        Clog.tag(tag).i(throwable)
        Clog.tag(tag).log(throwable)
        Clog.tag(tag).w(throwable)
        Clog.tag(tag).e(throwable)
        Clog.tag(tag).wtf(throwable)
    }
}
