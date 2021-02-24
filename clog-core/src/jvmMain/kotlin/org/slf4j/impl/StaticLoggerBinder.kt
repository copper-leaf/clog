package org.slf4j.impl

import clog.Clog
import clog.ClogProfile
import clog.dsl.tag
import clog.impl.DefaultTagProvider
import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import org.slf4j.Marker
import org.slf4j.spi.LoggerFactoryBinder

object StaticLoggerBinder : LoggerFactoryBinder {

    @JvmField
    var REQUESTED_API_VERSION = "1.7.30"

    @JvmStatic
    fun getSingleton(): StaticLoggerBinder {
        return StaticLoggerBinder
    }

    override fun getLoggerFactory(): ILoggerFactory {
        return Clog4jLoggerFactory
    }

    override fun getLoggerFactoryClassStr(): String {
        return Clog4jLoggerFactory::class.java.name
    }
}

object Clog4jLoggerFactory : ILoggerFactory {
    override fun getLogger(name: String): Logger {
        return Clog4jLoggerImpl(name)
    }
}

class Clog4jLoggerImpl(private val loggerName: String) : Logger {

    private val internalClogLogger: ClogProfile get() = Clog.tag(loggerName)

    private fun withMarker(marker: Marker): ClogProfile {
        return internalClogLogger.copy(tagProvider = DefaultTagProvider(marker.name))
    }

    override fun getName(): String {
        return loggerName
    }

    // What is enabled
    override fun isTraceEnabled(): Boolean = true
    override fun isTraceEnabled(marker: Marker): Boolean = true
    override fun isDebugEnabled(): Boolean = true
    override fun isDebugEnabled(marker: Marker): Boolean = true
    override fun isInfoEnabled(): Boolean = true
    override fun isInfoEnabled(marker: Marker): Boolean = true
    override fun isWarnEnabled(): Boolean = true
    override fun isWarnEnabled(marker: Marker): Boolean = true
    override fun isErrorEnabled(): Boolean = true
    override fun isErrorEnabled(marker: Marker): Boolean = true

    // Logger implementations
    override fun trace(msg: String) {
        internalClogLogger.v(msg)
    }

    override fun trace(format: String, arg: Any) {
        internalClogLogger.v(format, arg)
    }

    override fun trace(format: String, arg1: Any, arg2: Any) {
        internalClogLogger.v(format, arg1, arg2)
    }

    override fun trace(format: String, vararg arguments: Any) {
        internalClogLogger.v(format, *arguments)
    }

    override fun trace(msg: String, t: Throwable) {
        internalClogLogger.v(msg, t)
    }

    override fun trace(marker: Marker, msg: String) {
        withMarker(marker).v(msg)
    }

    override fun trace(marker: Marker, format: String, arg: Any) {
        withMarker(marker).v(format, arg)
    }

    override fun trace(marker: Marker, format: String, arg1: Any, arg2: Any) {
        withMarker(marker).v(format, arg1, arg2)
    }

    override fun trace(marker: Marker, format: String, vararg argArray: Any) {
        withMarker(marker).v(format, *argArray)
    }

    override fun trace(marker: Marker, msg: String, t: Throwable) {
        withMarker(marker).v(msg, t)
    }

    override fun debug(msg: String) {
        internalClogLogger.d(msg)
    }

    override fun debug(format: String, arg: Any) {
        internalClogLogger.d(format, arg)
    }

    override fun debug(format: String, arg1: Any, arg2: Any) {
        internalClogLogger.d(format, arg1, arg2)
    }

    override fun debug(format: String, vararg arguments: Any) {
        internalClogLogger.d(format, *arguments)
    }

    override fun debug(msg: String, t: Throwable) {
        internalClogLogger.d(msg, t)
    }

    override fun debug(marker: Marker, msg: String) {
        withMarker(marker).d(msg)
    }

    override fun debug(marker: Marker, format: String, arg: Any) {
        withMarker(marker).d(format, arg)
    }

    override fun debug(marker: Marker, format: String, arg1: Any, arg2: Any) {
        withMarker(marker).d(format, arg1, arg2)
    }

    override fun debug(marker: Marker, format: String, vararg argArray: Any) {
        withMarker(marker).d(format, *argArray)
    }

    override fun debug(marker: Marker, msg: String, t: Throwable) {
        withMarker(marker).d(msg, t)
    }

    override fun info(msg: String) {
        internalClogLogger.i(msg)
    }

    override fun info(format: String, arg: Any) {
        internalClogLogger.i(format, arg)
    }

    override fun info(format: String, arg1: Any, arg2: Any) {
        internalClogLogger.i(format, arg1, arg2)
    }

    override fun info(format: String, vararg arguments: Any) {
        internalClogLogger.i(format, *arguments)
    }

    override fun info(msg: String, t: Throwable) {
        internalClogLogger.i(msg, t)
    }

    override fun info(marker: Marker, msg: String) {
        withMarker(marker).i(msg)
    }

    override fun info(marker: Marker, format: String, arg: Any) {
        withMarker(marker).i(format, arg)
    }

    override fun info(marker: Marker, format: String, arg1: Any, arg2: Any) {
        withMarker(marker).i(format, arg1, arg2)
    }

    override fun info(marker: Marker, format: String, vararg argArray: Any) {
        withMarker(marker).i(format, *argArray)
    }

    override fun info(marker: Marker, msg: String, t: Throwable) {
        withMarker(marker).i(msg, t)
    }

    override fun warn(msg: String) {
        internalClogLogger.w(msg)
    }

    override fun warn(format: String, arg: Any) {
        internalClogLogger.w(format, arg)
    }

    override fun warn(format: String, arg1: Any, arg2: Any) {
        internalClogLogger.w(format, arg1, arg2)
    }

    override fun warn(format: String, vararg arguments: Any) {
        internalClogLogger.w(format, *arguments)
    }

    override fun warn(msg: String, t: Throwable) {
        internalClogLogger.w(msg, t)
    }

    override fun warn(marker: Marker, msg: String) {
        withMarker(marker).w(msg)
    }

    override fun warn(marker: Marker, format: String, arg: Any) {
        withMarker(marker).w(format, arg)
    }

    override fun warn(marker: Marker, format: String, arg1: Any, arg2: Any) {
        withMarker(marker).w(format, arg1, arg2)
    }

    override fun warn(marker: Marker, format: String, vararg argArray: Any) {
        withMarker(marker).w(format, *argArray)
    }

    override fun warn(marker: Marker, msg: String, t: Throwable) {
        withMarker(marker).w(msg, t)
    }

    override fun error(msg: String) {
        internalClogLogger.e(msg)
    }

    override fun error(format: String, arg: Any) {
        internalClogLogger.e(format, arg)
    }

    override fun error(format: String, arg1: Any, arg2: Any) {
        internalClogLogger.e(format, arg1, arg2)
    }

    override fun error(format: String, vararg arguments: Any) {
        internalClogLogger.e(format, *arguments)
    }

    override fun error(msg: String, t: Throwable) {
        internalClogLogger.e(msg, t)
    }

    override fun error(marker: Marker, msg: String) {
        withMarker(marker).e(msg)
    }

    override fun error(marker: Marker, format: String, arg: Any) {
        withMarker(marker).e(format, arg)
    }

    override fun error(marker: Marker, format: String, arg1: Any, arg2: Any) {
        withMarker(marker).e(format, arg1, arg2)
    }

    override fun error(marker: Marker, format: String, vararg argArray: Any) {
        withMarker(marker).e(format, *argArray)
    }

    override fun error(marker: Marker, msg: String, t: Throwable) {
        withMarker(marker).e(msg, t)
    }
}
