package clog.util

actual class MutableVariable<T>
actual constructor(
    initialValue: T
) {
    private var atom = initialValue

    actual fun get(): T {
        return atom
    }

    actual fun set(value: T) {
        atom = value
    }
}
