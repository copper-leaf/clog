package clog.test.impl

actual class CopyOnWriteList<T>(
    delegateList: MutableList<T>
) : MutableList<T> by delegateList {
    actual constructor() : this(mutableListOf())
}
