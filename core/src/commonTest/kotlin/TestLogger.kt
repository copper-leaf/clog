package clog

import clog.api.ClogLogger
import clog.api.ClogProfile
import co.touchlab.stately.collections.IsoMutableList

class TestLogger : ClogLogger {

    data class LoggingEvent(
        val priority: Clog.Priority,
        val tag: String?,
        val message: String
    )

    private val loggingEvents: IsoMutableList<LoggingEvent> = IsoMutableList()

    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        loggingEvents.add(LoggingEvent(priority, tag, message))
    }

    val messageWasLogged: Boolean get() = loggingEvents.isNotEmpty()
    val lastPriority: Clog.Priority? get() = loggingEvents.lastOrNull()?.priority
    val lastTag: String? get() = loggingEvents.lastOrNull()?.tag
    val lastMessage: String? get() = loggingEvents.lastOrNull()?.message
}

inline fun clogTest(block: (TestLogger) -> Unit) {
    val testLogger = TestLogger()
    Clog.updateProfile { ClogProfile(logger = testLogger) }

    block(testLogger)

    Clog.setProfile(ClogProfile())
}
