import java.io.ByteArrayOutputStream

plugins {
    id("base")
}

repositories {
    jcenter()
    google()
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

// Automatic Versioning
//----------------------------------------------------------------------------------------------------------------------

data class ProjectVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val snapshot: Boolean,
    val snapshotSuffix: String,
    val sha: String,
    val commits: List<String>
) {
    val shortVersion: String = "$major.$minor"
    val releaseVersion: String = "$major.$minor.$patch"
    val fullVersion: String = "$releaseVersion${if (snapshot) snapshotSuffix else ""}"

    override fun toString(): String = fullVersion

    fun debug(): String {
        return """
            |ProjectVersion(
            |    major=$major
            |    minor=$minor
            |    patch=$patch
            |    snapshot=$snapshot
            |    snapshotSuffix=$snapshotSuffix
            |    sha=$sha
            |    commits=$commits
            |    shortVersion=$shortVersion
            |    releaseVersion=$releaseVersion
            |    fullVersion=$fullVersion
            |)
        """.trimMargin()
    }
}

fun runCommand(command: String): String {
    return runCatching {
        val stdout = ByteArrayOutputStream()

        exec {
            commandLine(*command.split(' ').toTypedArray())
            standardOutput = stdout
        }

        stdout.toString().trim()
    }.getOrElse { "" }
}

fun getCurrentSha(): String = runCommand("git rev-parse HEAD")
fun getLatestTagSha(): String = runCommand("git rev-list --tags --max-count=1")
fun getLatestTagName(): String = runCommand("git describe --abbrev=0 --tags")
fun getCommitsSinceLastTag(latestTagName: String): List<String> = runCommand("git log ${latestTagName}..HEAD --oneline --pretty=format:%s").lines().reversed()
fun hasUncommittedChanges(): Boolean = runCommand("git status --porcelain").isBlank()

fun String.parseVersion() : Triple<Int, Int, Int> {
    return this
        .split('.')
        .map { it.trim().toIntOrNull() ?: 0 }
        .let { Triple(it.component1(), it.component2(), it.component3()) }
}

fun getProjectVersion(
    logChanges: Boolean = false,
    failWithUncommittedChanges: Boolean = false,
    failIfNotRelease: Boolean = false,
    snapshotSuffix: String = "-SNAPSHOT",
    initialVersion: String = "0.1.0",
    majorVersionBumpCommitPrefix: String = "[major]",
    minorVersionBumpCommitPrefix: String = "[minor]"
): ProjectVersion {
    val latestTagName = getLatestTagName()
    val latestTagSha = getLatestTagSha()
    val currentSha = getCurrentSha()
    val commitsSinceLastTag = getCommitsSinceLastTag(latestTagName)
    val isRelease = hasProperty("release")
    val hasUncommittedChanges = hasUncommittedChanges()

    val (major, minor, patch) = when {
        latestTagName.isBlank() -> {
            initialVersion.parseVersion()
        }
        else -> {
            var (_major, _minor, _patch) = latestTagName.parseVersion()

            if (currentSha != latestTagSha || hasUncommittedChanges) {
                _patch++
            }

            for (commit in commitsSinceLastTag) {
                if (commit.startsWith(minorVersionBumpCommitPrefix)) {
                    _patch = 0
                    _minor++
                } else if (commit.startsWith(majorVersionBumpCommitPrefix)) {
                    _patch = 0
                    _minor = 0
                    _major++
                }
            }

            Triple(_major, _minor, _patch)
        }
    }

    // make checks on version
    if (failWithUncommittedChanges) {
        check(!hasUncommittedChanges) { "There are uncommitted changes!" }
    }
    if (failIfNotRelease) {
        check(isRelease) { "This is not a release build!" }
    }

    return ProjectVersion(
        major = major,
        minor = minor,
        patch = patch,
        snapshot = !isRelease,
        snapshotSuffix = snapshotSuffix,
        sha = currentSha,
        commits = commitsSinceLastTag
    ).also {
        if (logChanges) {
            println("$latestTagName ($latestTagSha) -> $it ($currentSha) \n  * " + commitsSinceLastTag.joinToString(separator = "\n  * "))
        }
    }
}

// Setup default project values
//----------------------------------------------------------------------------------------------------------------------

var ghUser: String by project.extra
var ghToken: String by project.extra
var bintrayUser: String by project.extra
var bintrayToken: String by project.extra

var projectVersion: ProjectVersion by project.extra
var shortVersion: String by project.extra
var releaseVersion: String by project.extra
var fullVersion: String by project.extra

ghUser       = (System.getenv("GITHUB_ACTOR")?.toString() ?: project.properties["github_username"]?.toString()) ?: ""
ghToken      = (System.getenv("GITHUB_TOKEN")?.toString() ?: project.properties["githubToken"]?.toString()) ?: ""
bintrayUser  = (System.getenv("BINTRAY_USER")?.toString() ?: project.properties["bintrayUser"]?.toString()) ?: ""
bintrayToken = (System.getenv("BINTRAY_TOKEN")?.toString() ?: project.properties["bintrayToken"]?.toString()) ?: ""

projectVersion = getProjectVersion(logChanges = true)
shortVersion = projectVersion.shortVersion
releaseVersion = projectVersion.releaseVersion
fullVersion = projectVersion.fullVersion

version = projectVersion
group = "io.copper-leaf"
