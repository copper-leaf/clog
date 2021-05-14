# Clog

> Zero-config Kotlin multiplatform logging utility, strongly inspired by the [SLF4J](http://www.slf4j.org/) and 
> [Timber](https://github.com/JakeWharton/timber) APIs.

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/clog)
![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/clog-core)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.4.32-orange)

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

# Supported Platforms/Features

| Platform | Logging Target   | ANSI Colors | Tag Inference | Message Formatting | SLF4J Integration | SLF4J MDC Support |
| -------- | ---------------- | ----------- | ------------- | ------------------ | ----------------- | ----------------- |
| JVM      | System.out       | ✅          | ✅             | ✅                 | ✅                | ✅                 |
| Android  | android.util.Log | ❌          | ✅             | ✅                 | ✅                | ❌                 |
| JS       | console.log      | ❌          | ❌             | ✅                 | ❌                | ❌                 |
| iOS      | NSLog            | ❌          | ❌             | ✅                 | ❌                | ❌                 |

# Installation

```kotlin
repositories {
    mavenCentral()
}

// for plain JVM or Android projects
dependencies {
    implementation("io.github.copper-leaf:clog-core:{{site.version}}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.copper-leaf:clog-core:{{site.version}}")
            }
        }
    }
}
```

# Documentation

See the [website](https://copper-leaf.github.io/clog/) for detailed documentation and usage instructions.

# License

Clog is licensed under the BSD 3-Clause License, see [LICENSE.md](https://github.com/copper-leaf/clog/tree/master/LICENSE.md).

# References

- [SLF4J](http://www.slf4j.org/)
- [Timber](https://github.com/JakeWharton/timber)
