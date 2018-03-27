---

title: Android Clog

---

# Using Clog with Android
{ #overview .page-header }

> Clog is a replacement of the standard Android Log. Featuring multiple logging profiles, removal of annoying tags, and 
> the powerful Parselmouth formatting language, you'll wonder how you ever got by without it.
  
### Features

- All features of the base Clog library
- Android-specific logging profiles for both development and production

  
# Get It
{ #download .page-header }

### Include In Your Project

Android Clog can be added to your project from Jitpack through Gradle.

Add this to your project-level build.gradle:

```groovy
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```  

Add this to your module's dependencies:

```groovy
dependencies {
  ...
  api 'com.github.cjbrooks12:Android-Clog:v{{site.version}}'
}
```


### Github

Full source can be viewed on [Github](https://github.com/cjbrooks12/Android-Clog). Feel free to send me a PR.

### Jitpack

Download the most recent commits, or see instructions for installing with Maven, at Jitpack.

[![](https://jitpack.io/v/JavaEden/Clog.svg)](https://jitpack.io/#JavaEden/Clog)
  
### Javadoc

View javadoc, hosted by Jitpack.

[![](https://jitpack.io/v/JavaEden/Clog.svg)](https://jitpack.io/com/github/cjbrooks12/Clog/v{{site.version}}/javadoc/)

# Use In Your Apps
{ #download .page-header }

### Clog implementation

See [{{site.baseUrl}}]({{site.baseUrl}}) for information on Clog.

Android Clog provides simple Android Log implementations for Clog. The mapping between Clog levels and Android Clog 
levels is one-to-one, as the Clog API was designed to replace Log, but be more flexible and not require tags with the 
logging calls.

`Clog.v(...) ->> Log.v(...)`

`Clog.d(...) ->> Log.d(...)`

`Clog.i(...) ->> Log.i(...)`

`Clog.w(...) ->> Log.w(...)`

`Clog.e(...) ->> Log.e(...)`

`Clog.wtf(...) ->> Log.wtf(...)`

In addition, Clog gets rid of the annoying Android Log tags. By default, the Tag passed to the Android logger is the 
simple classname of the calling class:

```
Clog.d(String message) ->> Log.d(callerClassName, message)
```

If, for some reason, you need to set the Tag to something other than the caller class's name, you can push a Tag into 
Clog before logging:

```
Clog.pushTag(customTag);
Clog.d(message); ->> Log.d(customTag, message)
```

Just make sure to pop it back off when you're done, otherwise other places will be logged with that tag as well.

```
Clog.popTag();
```

Getting Android Clog integrated into your project is easy. Add the following to your Activity's or Application's 
`onCreate()` method:

```java
if(BuildConfig.DEBUG) {
  Clog.setCurrentProfile("dev", AndroidClog.getDevelopmentClog());
}
else {
  Clog.setCurrentProfile("prod", AndroidClog.getProductionClog());
}
```

In development, all logs will be directed to the standard Log implementations shown above, but in production, all logs 
will be discarded. You can replace the production log profile with implementations that write to file or send to 
Crashlytics if you need that instead.
