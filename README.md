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

In addition, Clog gets rid of the annoying Android Log tags. By default, the Tag passed to the Android logger is the simple classname of the calling class:

```
Clog.d(String message) -> Log.d(callerClassName, message)
```

If, for some reason, you need to set the Tag to something other than the caller class's name, you can push a Tag into Clog:

```
Clog.pushTag(tag);
```

Just make sure to pop it back off when you're done, otherwise other places will be logged with that tag as well.

```
Clog.popTag();
```

Getting Android Clog integrated into your project is easy. Add the following to your Activity's or Application's `onCreate()`:

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
[![JitPack Javadoc](https://img.shields.io/github/tag/JavaEden/Clog.svg?maxAge=2592000&label=javadoc)](https://jitpack.io/com/github/JavaEden/Clog/v1.6.1/javadoc/)

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
    api 'com.github.JavaEden:Android-Clog:v1.6.1'
}
```
