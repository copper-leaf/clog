---
title: Clog4j
---

# Using Clog with SLF4j
{ #overview .page-header }

> Clog4j is a fully-compatible SLF4j implementation, backed by Clog. It completely replaces Logback or Log4j, which are
> both unecessarily complex to set up for most projects just looking for debugging or user feedback. Clog4j lets your 
> libraries log to Clog using the SLF4j interface, but still gives you the freedom to use the full Clog library for 
> yourself, making it a once-size-fits-all Logger. 
  
### Features

- All features of the base Clog library
- Libraries that use SLF4j will be captured by Clog as well. No need to implement another logging system besides Clog, 
Clog4j does it all
- Use the same {}-placeholders as expected from SLF4j, or use the more advanced parameterization from Parseltongue. Both
can be use simultaneously in the same logging statement.

  
# Get It
{ #download .page-header }

### Include In Your Project

Clog4j can be added to your project from Jitpack through Gradle.

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
  api 'com.github.cjbrooks12:Clog4j:v{{site.version}}'
}
```

### Github

Full source can be viewed on [Github](https://github.com/cjbrooks12/Clog4j). Feel free to send me a PR.

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

Clog4j provides simple Log4j implementations for Clog. The mapping between Clog levels and Log4j levels is as follows:

`Clog.v(...) ->> logger.trace(...)`

`Clog.d(...) ->> logger.debug(...)`

`Clog.i(...) ->> logger.info(...)`

`Clog.w(...) ->> logger.warn(...)`

`Clog.e(...) ->> logger.error(...)`

`Clog.wtf(...) ->> logger.fatal(...)`

Clog tags are mapped to SLF4j markers like so:

`logger.debug(MarkerManager.getMarker(tag), message);`
