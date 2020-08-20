# Clog

Zero-config Kotlin multiplatform logging utility

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/clog)

## Supported Platforms/Features

| Platform | Logging Target   | ANSI Colors | Tag Inference | Message Formatting | SLF4J Integration | SLF4J MDC Support |
| -------- | ---------------- | ----------- | ------------- | ------------------ | ----------------- | ----------------- |
| JVM      | System.out       | ✅          | ✅             | ✅                 | ✅                | ✅                 |
| Android  | android.util.Log | ❌          | ✅             | ✅                 | ✅                | ❌                 |
| JS       | console.log      | ❌          | ❌             | ✅                 | ❌                | ❌                 |
| iOS      | NSLog            | ❌          | ❌             | ✅                 | ❌                | ❌                 |
{.table}

## Installation

```kotlin
repositories {
    jcenter()
    maven(url = "https://maven.pkg.github.com/copper-leaf/clog") {
        credentials {
            username = project.properties["github_username"]?.toString() ?: ""
            password = project.properties["githubToken"]?.toString() ?: ""
        }
    }
}

// for plain JVM or Android projects
dependencies {
    implementation("clog:core:{version}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("clog:core:{version}")
            }
        }
    }
}
```

## Usage

### Normal log messages

Tag will be inferred on supported platforms, based on the calling class

```kotlin
Clog.v("message")
Clog.d("message")
Clog.i("message")
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

### Message Formatting

SLF4j-style formatting is supported, replacing `{}` with params passed to the logging call. This is supported on all
platforms and all log levels.

```kotlin
val foo = "bar"
Clog.i("message {}", foo) // logs 'message bar'
```

### Priority Filter

Messages can be filtered out by priority.

```kotlin
Clog.getInstance().setMinPriority(Clog.Priority.ERROR)
```

### Tag whitelisting/blacklisting

Messages can be filtered out by tags.

```kotlin
Clog.getInstance().addTagToWhitelist("tag1")
Clog.getInstance().addTagToBlacklist("tag2")
```

## Lambda DSL

Using the Clog DSL, simple strings can be logged lazily. The lambda is only evaluated if the logging level and tag is 
enabled. By default, messages logged with the lambda DSL are not formatted, but it can be re-enabled by using `format()`
inside the lambda.

### Normal log messages

```kotlin
import clog.dsl.*

v { "message" }
d { "message" }
i { "message" }
w { "message" }
e { "message" }
wtf { "message" }
```

### Logs with custom tags

```kotlin
import clog.dsl.*

v("tag") { "message" }
d("tag") { "message" }
i("tag") { "message" }
w("tag") { "message" }
e("tag") { "message" }
wtf("tag") { "message" }
```

### Message formatting

```kotlin
import clog.dsl.*

val foo = "bar" 
v {   format("message {}", foo) } // logs 'message bar'
d {   format("message {}", foo) } // logs 'message bar'
i {   format("message {}", foo) } // logs 'message bar'
w {   format("message {}", foo) } // logs 'message bar'
e {   format("message {}", foo) } // logs 'message bar'
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

