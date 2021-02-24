package clog.util

import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze

actual class MutableVariable<T>
actual constructor(
    initialValue: T
) {
    private var atom = AtomicReference<T>(initialValue.freeze())

    actual fun get(): T {
        return atom.value
    }

    actual fun set(value: T) {
        atom.value = value.freeze()
    }
}
