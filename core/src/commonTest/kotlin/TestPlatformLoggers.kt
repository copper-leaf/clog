package clog

import kotlin.test.Test

class TestPlatformLoggers {
    @Test
    fun testBasicLog() {
        Clog.v("m1")
        Clog.d("m1")
        Clog.i("m1")
        Clog.log("m1")
        Clog.w("m1")
        Clog.e("m1")
        Clog.wtf("m1")
    }

    @Test
    fun testLogWithTag() {
        Clog.tag("t1").v("m1")
        Clog.tag("t1").d("m1")
        Clog.tag("t1").i("m1")
        Clog.tag("t1").log("m1")
        Clog.tag("t1").w("m1")
        Clog.tag("t1").e("m1")
        Clog.tag("t1").wtf("m1")
    }
}
