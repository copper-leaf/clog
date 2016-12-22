# Android-Clog
Clog logger implementation for the standard Android Log priorities

See https://github.com/JavaEden/Clog for information on Clog.

### Android Clog Implementation

Android Clog provides simple Android Log implementations for Clog. The mapping between Clog levels and Android Clog levels is one-to-one, as the Clog API was designed to directly replace Log:

`Clog.d(...) --> Log.d(...)`

`Clog.e(...) --> Log.e(...)`

`Clog.i(...) --> Log.i(...)`

`Clog.v(...) --> Log.v(...)`

`Clog.w(...) --> Log.w(...)`

`Clog.wtf(...) --> Log.wtf(...)`

In addition, Clog tags work exactly like Android Log tags, like so:

```
d(String tag, String message) {
  Log.d(tag, message);
}
```

Getting Android Clog integrated into your project is easy. Add the following to your Activity's `onCreate()`:

```java
if(BuildConfig.DEBUG) {
    Clog.setCurrentProfile("dev", AndroidClog.getDevelopmentClog());
}
else {
    Clog.setCurrentProfile("prod", AndroidClog.getProductionClog());
}
```

In development, all logs will be directed to the standard Log implementations shown above, but in production, all logs will be discarded. You can replace the production log profile with implementations that write to file or send to Crashlytics if you need that instead.

### Download
Clog and Android Clog is distrubuted through JitPack.io.

[![](https://jitpack.io/v/JavaEden/Android-Clog.svg)](https://jitpack.io/#JavaEden/Android-Clog)
[![JitPack Javadoc](https://img.shields.io/github/tag/JavaEden/Clog.svg?maxAge=2592000&label=javadoc)](https://jitpack.io/com/github/JavaEden/Clog/v1.2.3/javadoc/)

In your project-level `build.gradle`:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

In your module's `build.gradle` (replace the version with the most recent release shown on badge above):
```groovy
dependencies {
    ...
    compile('com.github.JavaEden:Android-Clog:v1.2.3@aar') {
        transitive = true;
    }
}
```
