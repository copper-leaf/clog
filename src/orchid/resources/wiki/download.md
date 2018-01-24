---
---

# Get It

### Include In your Project

Clog can be added to your project from Jitpack through Gradle.

Add this to your project-level build.gradle:

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }

Add this to your module's dependencies:

    dependencies {
        ...
        compile('com.github.cjbrooks12:Clog:{{options.v}}') {
            transitive = true
        }
    }

Alternatively, use the preconfigured Clog profiles for Android or Log4j:

    dependencies {
        ...
        compile('com.github.cjbrooks12:Android-Clog:{{options.v}}@aar') {
            transitive = true;
        }
    }

    dependencies {
        ...
        compile('com.github.cjbrooks12:Clog4j:{{options.v}}') {
            transitive = true;
        }
    }

### Github

Full source can be viewed on Github. Feel free to send me a PR.

Download the most recent commits, or see instructions for installing with Maven, at Jitpack. [![](https://jitpack.io/v/JavaEden/Clog.svg)](https://jitpack.io/#JavaEden/Clog)
