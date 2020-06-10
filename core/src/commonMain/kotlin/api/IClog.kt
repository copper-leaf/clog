package clog.api

interface IClog {
    fun v(message: String, vararg args: Any?)
    fun d(message: String, vararg args: Any?)
    fun i(message: String, vararg args: Any?)
    fun log(message: String, vararg args: Any?)
    fun w(message: String, vararg args: Any?)
    fun e(message: String, vararg args: Any?)
    fun wtf(message: String, vararg args: Any?)
}
