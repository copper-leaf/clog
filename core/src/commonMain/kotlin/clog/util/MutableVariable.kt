package clog.util

expect class MutableVariable<T>(initialValue: T) {

    fun get(): T
    fun set(value: T)
}
