# Clog

Zero-config Kotlin multiplatform logging utility

## Supported Platforms/Features

| Platform | Logging Target   | ANSI Colors | Tag Inference | SLF4J Integration |
| -------- | ---------------- | ----------- | ------------- | ----------------- |
| JVM      | Console/stdout   | ✅          | ✅             | ✅                |
| Android  | android.util.Log | ❌          | ✅             | ❌                |
| JS       | console.log      | ❌          | ❌             | ❌                |
| iOS      | NSLog            | ❌          | ❌             | ❌                |
{.table}

## Usage

```kotlin

```

## SLF4J

On plain JVM platform, Clog is set up as an SLF4J binding; that is, SLF4J will pass log messages through to Clog. Other 
libraries and frameworks logging to SLF4J will be formatted as normal Clog logs for uniform log output, and so Clog can
be used as a simple SLF4J binding when you don't want to configure Logback.
