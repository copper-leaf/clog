plugins {
    id("com.android.library")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint")
    kotlin("multiplatform")
    `clog-base`
}

val ghUser: String by extra
val ghToken: String by extra
val jetbrainsSpaceUser: String by extra
val jetbrainsSpaceToken: String by extra

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
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
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
    js(BOTH) {
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
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:1.4.32")
            }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.30")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
                implementation("io.mockk:mockk:1.10.6")
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.30")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
                implementation("io.mockk:mockk:1.10.6")
            }
        }

        // JS Sourcesets
        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-js:1.4.32")
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
        maven(url = "https://maven.pkg.jetbrains.space/cjbrooks12/p/cjbrooks12/oss") {
            name = "JetbrainsSpace"
            credentials {
                username = jetbrainsSpaceUser
                password = jetbrainsSpaceToken
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
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.useIR = true
}
