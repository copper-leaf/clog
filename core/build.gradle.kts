plugins {
    id("com.android.library")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    kotlin("multiplatform") version "1.3.72"
    `clog-base`
}

val ghUser: String by extra
val ghToken: String by extra
val bintrayUser: String by extra
val bintrayToken: String by extra

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = project.version.toString()
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
        }
    }
    sourceSets {
        val main by getting {
            setRoot("src/androidMain")
        }
        val androidTest by getting {
            setRoot("src/androidTest")
        }
    }
    testOptions {
        unitTests(delegateClosureOf<com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions> {
            setIncludeAndroidResources(true)
            setReturnDefaultValues(true)
        })
    }
    lintOptions {
        disable("GradleDependency")
    }
}

kotlin {
    jvm { }
    android {
        publishAllLibraryVariants()
    }
    js {
        browser { }
    }
    ios { }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.Experimental")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                implementation(Kotlin.stdlib.common)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Kotlin.test.common)
                implementation(Kotlin.test.annotationsCommon)
            }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                implementation(Kotlin.stdlib.jdk7)
                implementation("org.slf4j:slf4j-api:_")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Kotlin.test.junit)
                implementation("io.mockk:mockk:1.10.0")
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                implementation(Kotlin.stdlib.jdk7)
                implementation("org.slf4j:slf4j-api:_")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Kotlin.test.junit)
                implementation("io.mockk:mockk:1.10.0")
            }
        }

        // JS Sourcesets
        val jsMain by getting {
            dependencies {
                implementation(Kotlin.stdlib.js)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(Kotlin.test.js)
            }
        }

        // iOS Sourcesets
        val iosMain by getting {
            dependencies { }
        }
        val iosTest by getting {
            dependencies { }
        }
    }
}

publishing {
    repositories {
        // publish to the project buildDir to make sure things are getting published correctly
        maven(url = "${project.buildDir}/.m2/repository") {
            name = "project"
        }
        maven(url = "https://api.bintray.com/maven/copper-leaf/oss/clog/;publish=1;override=1;") {
            name = "Bintray"
            credentials {
                username = bintrayUser
                password = bintrayToken
            }
        }
    }
}

ktlint {
    debug.set(false)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    additionalEditorconfigFile.set(file("$rootDir/.editorconfig"))
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
    }
}
