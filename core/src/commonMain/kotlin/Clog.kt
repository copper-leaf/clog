package clog

import clog.api.ClogProfile
import clog.api.IClog
import clog.impl.DefaultTagProvider
import co.touchlab.stately.concurrency.AtomicReference

object Clog : IClog {
    enum class Priority {
        VERBOSE,
        DEBUG,
        INFO,
        DEFAULT,
        WARNING,
        ERROR,
        FATAL,
    }

    private val INSTANCE: AtomicReference<ClogProfile> = AtomicReference(ClogProfile())

    override fun v(message: String, vararg args: Any?) {
        INSTANCE.get().v(message, *args)
    }

    override fun d(message: String, vararg args: Any?) {
        INSTANCE.get().d(message, *args)
    }

    override fun i(message: String, vararg args: Any?) {
        INSTANCE.get().i(message, *args)
    }

    override fun log(message: String, vararg args: Any?) {
        INSTANCE.get().log(message, *args)
    }

    override fun w(message: String, vararg args: Any?) {
        INSTANCE.get().w(message, *args)
    }

    override fun e(message: String, vararg args: Any?) {
        INSTANCE.get().e(message, *args)
    }

    override fun wtf(message: String, vararg args: Any?) {
        INSTANCE.get().wtf(message, *args)
    }

    fun getInstance(): ClogProfile {
        return INSTANCE.get()
    }

    fun setProfile(profile: ClogProfile) {
        INSTANCE.set(profile)
    }

    fun tag(tag: String): ClogProfile {
        return INSTANCE.get().copy(tagProvider = DefaultTagProvider(tag))
    }
}

inline fun Clog.updateProfile(block: (ClogProfile) -> ClogProfile) {
    setProfile(
        block(getInstance())
    )
}

fun Clog.maybeTag(tag: String?): ClogProfile {
    return if (tag != null) tag(tag) else getInstance()
}
