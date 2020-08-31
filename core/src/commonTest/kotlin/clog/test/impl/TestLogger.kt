package clog.test.impl

import clog.Clog
import clog.api.ClogLogger
import clog.ClogProfile
import clog.dsl.updateProfile
import co.touchlab.stately.collections.IsoMutableList

class TestLogger : ClogLogger {

    data class LoggingEvent(
        val priority: Clog.Priority,
        val tag: String?,
        val message: String?,
        val throwable: Throwable?
    )

    private val loggingEvents: IsoMutableList<LoggingEvent> = IsoMutableList()

    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        loggingEvents.add(LoggingEvent(priority, tag, message, null))
    }

    override fun logException(priority: Clog.Priority, tag: String?, throwable: Throwable) {
        loggingEvents.add(LoggingEvent(priority, tag, null, throwable))
    }

    private val messageEvents: List<LoggingEvent> get() = loggingEvents.filter { it.message != null }
    val messageWasLogged: Boolean get() = messageEvents.isNotEmpty()
    val lastMessagePriority: Clog.Priority? get() = messageEvents.lastOrNull()?.priority
    val lastMessageTag: String? get() = messageEvents.lastOrNull()?.tag
    val lastMessage: String? get() = loggingEvents.lastOrNull()?.message

    private val throwableEvents: List<LoggingEvent> get() = loggingEvents.filter { it.throwable != null }
    val throwableWasLogged: Boolean get() = throwableEvents.isNotEmpty()
    val lastThrowablePriority: Clog.Priority? get() = throwableEvents.lastOrNull()?.priority
    val lastThrowableTag: String? get() = throwableEvents.lastOrNull()?.tag
    val lastThrowable: Throwable? get() = throwableEvents.lastOrNull()?.throwable
}

inline fun clogTest(block: (TestLogger) -> Unit) {
    val originalProfile = Clog.getInstance()
    val testLogger = TestLogger()
    Clog.updateProfile { originalProfile.copy(logger = testLogger) }
    block(testLogger)

    Clog.setInstance(originalProfile)
}

inline fun clogProfileTest(testProfile: ClogProfile, block: (TestLogger) -> Unit) {
    val originalProfile = Clog.getInstance()
    val testLogger = TestLogger()
    Clog.updateProfile { testProfile.copy(logger = testLogger) }
    block(testLogger)

    Clog.setInstance(originalProfile)
}
