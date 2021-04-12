---
---

## Clog

Zero-config Kotlin multiplatform logging utility, strongly inspired by the [SLF4J](http://www.slf4j.org/) and 
[Timber](https://github.com/JakeWharton/timber) APIs.

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/clog)

Clog is designed with the following goals in mind:

- **Zero setup required**: just add the dependency and start logging
- **Support natural logging for each platform**: use `actual/expect` declarations to provide natural logging targets for 
    each platform, rather than printing everything to stdout
- **Be a central logger**: Unlike many other Kotlin/Multiplatform loggers which simply delegate to SLF4j, Clog is designed
    to be the central logger instead of a wrapper around another logger. Clog _is_ an SLF4J implementation, so logs from 
    other libraries will be passed through to Clog and can be configured and filtered using Clog's APIs.
- **Support existing standards for logging**: with the API of Timber and semantics compatible with SLF4J, Clog is a 
    natural way to transition your JVM-specific logging into the Kotlin/Multiplatform world
- **Customization is there if you need it**: the Clog singleton can be easily configured with custom classes to apply 
    your own formatting, filtering, and logging targets
- **Immutable loggers**: The ClogProfile and all classes implementing the logger are immutable, for multithreaded 
    performance and safety. The global Clog instance is updated only by swapping out the backing instance, and you're 
    free to bypass the global instance entirely and inject loggers instead.

### Supported Platforms/Features

| Platform | Logging Target   | ANSI Colors | Tag Inference | Message Formatting | SLF4J Integration | SLF4J MDC Support |
| -------- | ---------------- | ----------- | ------------- | ------------------ | ----------------- | ----------------- |
| JVM      | System.out       | ✅          | ✅             | ✅                 | ✅                | ✅                 |
| Android  | android.util.Log | ❌          | ✅             | ✅                 | ✅                | ❌                 |
| JS       | console.log      | ❌          | ❌             | ✅                 | ❌                | ❌                 |
| iOS      | NSLog            | ❌          | ❌             | ✅                 | ❌                | ❌                 |
{.table}

### Installation

```kotlin
repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/cjbrooks12/p/cjbrooks12/oss")
}

// for plain JVM or Android projects
dependencies {
    implementation("io.copper-leaf:clog-core:{{site.version}}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.copper-leaf:clog-core:{{site.version}}")
            }
        }
    }
}
```

## Usage

Clog's logging levels generally follow the SLF4J logging levels, and the API follows a similar API as 
[android.util.log](https://developer.android.com/reference/android/util/Log) or 
[Timber](https://github.com/JakeWharton/timber).

| Clog Level | Clog Method  | SLF4J Level      |
| ---------- | ------------ | ---------------- |
| Verbose    | `Clog.v()`   | `logger.trace()` |
| Debug      | `Clog.d()`   | `logger.debug()` |
| Info       | `Clog.i()`   | `logger.info()` |
| Default    | `Clog.log()` | N/A |
| Warning    | `Clog.w()`   | `logger.warn()` |
| Error      | `Clog.e()`   | `logger.error()` |
| Fatal      | `Clog.wtf()` | N/A |
{.table}

In general, a log consists of a _message_ (which may be formatted with params in SLF4J-style), a _tag_, and a 
_log level_. Below is a description of the API

### Normal log messages

Tag will be inferred on supported platforms, based on the calling class

```kotlin
Clog.v("message")
Clog.d("message")
Clog.i("message")
Clog.log("message")
Clog.w("message")
Clog.e("message")
Clog.wtf("message")
```

### Logs with custom tags

```kotlin
Clog.tag("tag").v("message")
Clog.tag("tag").d("message")
Clog.tag("tag").i("message")
Clog.tag("tag").w("message")
Clog.tag("tag").e("message")
Clog.tag("tag").wtf("message")
```

### Log exceptions

```kotlin
val e = RuntimeException()
Clog.v(e)
Clog.d(e)
Clog.i(e)
Clog.log(e)
Clog.w(e)
Clog.e(e)
Clog.wtf(e)
```

### Message Formatting

SLF4j-style formatting is supported, replacing `{}` with params passed to the logging call. This is supported on all
platforms and all log levels.

```kotlin
val foo = "bar"
Clog.i("message {}", foo) // logs 'message bar'
```

### Priority Filter

Messages and exceptions can be filtered out by priority.

```kotlin
Clog.setMinPriority(Clog.Priority.ERROR)
```

### Tag whitelisting/blacklisting

Messages can be filtered out by tags.

```kotlin
Clog.addTagToWhitelist("tag1")
Clog.addTagToBlacklist("tag2")
```

## Lambda DSL

Using the Clog DSL, simple strings can be logged lazily. The lambda is only evaluated if the logging level and tag is 
enabled. By default, messages logged with the lambda DSL are not formatted, but it can be re-enabled by using `format()`
inside the lambda.

### Normal log messages

```kotlin
import clog.dsl.*

v   { "message" }
d   { "message" }
i   { "message" }
w   { "message" }
e   { "message" }
wtf { "message" }
```

### Logs with custom tags

```kotlin
import clog.dsl.*

v("tag")   { "message" }
d("tag")   { "message" }
i("tag")   { "message" }
w("tag")   { "message" }
e("tag")   { "message" }
wtf("tag") { "message" }
```

### Message formatting

```kotlin
import clog.dsl.*

val foo = "bar" 
v   { format("message {}", foo) } // logs 'message bar'
d   { format("message {}", foo) } // logs 'message bar'
i   { format("message {}", foo) } // logs 'message bar'
w   { format("message {}", foo) } // logs 'message bar'
e   { format("message {}", foo) } // logs 'message bar'
wtf { format("message {}", foo) } // logs 'message bar'
```

## SLF4J

On plain JVM and Android platforms, Clog is set up as an SLF4J binding; that is, SLF4J will pass log messages through to 
Clog. Other libraries and frameworks logging to SLF4J will be formatted as normal Clog logs for uniform log output, and 
so Clog can be used as a simple SLF4J binding when you don't want to configure Logback. Additionally, SLF Mapped 
Diagnostic Context (MDC) is supported, and context data can be added to log messages with the standard format of 
`%X{mdcKey}`.

```kotlin
val slf4j: Logger = LoggerFactory.getLogger(JvmClogSlf4jTest::class.java)

MDC.put("akey", "avalue")
slf4j.trace("message %X{akey}") // logs 'message avalue' to the Clog logger
```

## More Customization

Clog is designed to work out-of-the-box with absolutely zero config required to start logging with it. However, you can
customize all components of Clog to your needs. Clog is comprised of several components wrapped in a `ClogProfile`, 
which is the global instance of `Clog.getInstance()`. You can customize your Clog by creating a new Profile with your 
custom components:

```kotlin
val newProfile = ClogProfile(...)
Clog.setProfile(newProfile)
```

You can also use the `Clog.updateProfile` helper to create a profile based on the current global instance:

```kotlin
Clog.updateProfile { it.copy(logger = newLogger) }
```

The table below describes the classes that can be customized in the `ClogProfile`, along with their default 
implementation for each supported platform:

| Interface              | Description                                                                 | JVM                                                | Android                     | JS                          | iOS                         |
| ---------------------- | --------------------------------------------------------------------------- | -------------------------------------------------- | --------------------------- | --------------------------- | --------------------------- |
| `ClogTagProvider`      | Infers a tag if one is not provided to the logging call                     | `DefaultTagProvider()`                             | `DefaultTagProvider()`      | `DefaultTagProvider()`      | `DefaultTagProvider()`      |
| `ClogMessageFormatter` | Formats a message string to pass to the `ClogLogger`                        | `Slf4jMessageFormatter(DefaultMessageFormatter())` | `DefaultMessageFormatter()` | `DefaultMessageFormatter()` | `DefaultMessageFormatter()` |
| `ClogFilter`           | Determines whether to format and log a message                              | `DefaultFilter()`                                  | `DefaultFilter()`           | `DefaultFilter()`           | `DefaultFilter()`           |
| `ClogLogger`           | Prints a formatted log to a lower-level platform-specific logger or console | `DefaultLogger()`                                  | `AndroidLogger()`           | `JsConsoleLogger()`         | `NsLogger()`                |
{.table}

## Recipes

### Turn off logging in production

```kotlin
val isDebug = ...
Clog.configureLoggingInProduction(isDebug)
```

### Use a custom logger

Replaces the current logging target with a custom one.

```kotlin
val customLogger = object : ClogLogger {
    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        ...
    }
    override fun logException(priority: Clog.Priority, tag: String?, throwable: Throwable) {
        ...
    }
}
Clog.updateProfile { it.copy(logger = customLogger) }
```

### Using multiple logging targets

Add an additional logger to the current instance. Calling `addLogger` multiple times will continue adding loggers, and
messages will be delegated to all loggers.

```kotlin
val customLogger = object : ClogLogger {
    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        ...
    }
    override fun logException(priority: Clog.Priority, tag: String?, throwable: Throwable) {
        ...
    }
}
Clog.addLogger(customLogger)
```

### Using Clog with dependency injection

```kotlin
// Given some classes that depend on a logger
class Controller(val logger: ClogProfile) 

// just declare a ClogProfile singleton with any configurations you need 
val module = module { 
  single { ClogProfile() } 
  single { Controller(get()) } 
} 
```
