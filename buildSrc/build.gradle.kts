plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

group = "clog"

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
