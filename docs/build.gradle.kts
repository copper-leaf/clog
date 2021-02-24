plugins {
    id("com.eden.orchidPlugin") version "0.21.1"
    `clog-base`
}

repositories {
    jcenter()
}

dependencies {
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidDocs:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidBsDoc:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidGithub:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidKotlindoc:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidPluginDocs:0.21.1")
}

// Orchid setup
//----------------------------------------------------------------------------------------------------------------------

val ghUser: String by extra
val ghToken: String by extra
var releaseVersion: String by project.extra

orchid {
    githubToken = ghToken
    version = releaseVersion
}

val build by tasks
val check by tasks
val orchidBuild by tasks
val orchidDeploy by tasks

orchidBuild.mustRunAfter(check)
build.dependsOn(orchidBuild)

val publish by tasks.registering {
    dependsOn(orchidDeploy)
}
