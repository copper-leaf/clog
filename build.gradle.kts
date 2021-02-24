
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
    }
}

plugins {
    kotlin("multiplatform") version "1.4.30" apply false
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1" apply false
}
