plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

group = "clog"

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
