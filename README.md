# Clog

### About

Clog is a simple-to-use yet powerful and customizable logging utility for Java. It is based on the Android Log class interface, but improves it with features like custom loggers with multiple profiles and powerful String formatting with the Parseltongue formatting language. You'll wonder how you ever got by without it.

### Download
Clog is distrubuted through JitPack.io.

[![](https://jitpack.io/v/cjbrooks12/Clog.svg)](https://jitpack.io/#cjbrooks12/Clog)
[![JitPack Javadoc](https://img.shields.io/github/tag/cjbrooks12/Clog.svg?maxAge=2592000&label=javadoc)](https://jitpack.io/com/github/cjbrooks12/Clog/v0.3.0/javadoc/)
[![Github Releases](https://img.shields.io/github/downloads/cjbrooks12/Clog/latest/total.svg?maxAge=2592000)]()

In your project-level `build.gradle`:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

In your module's `build.gradle` (replace 'v0.3.0' with the most recent release):
```groovy
dependencies {
    ...
    compile 'com.github.cjbrooks12:Clog:v0.3.0'
}
```

[View Full Documentation](https://cjbrooks12.github.io/Clog/)
