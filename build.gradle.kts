import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.net.URL

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("io.realm.kotlin")
}

group = "net.longbowxxx.playground"
version = "${rootProject.property("package.version")}"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    // for Palm2 SDK (Beta)
    mavenLocal()
}

allprojects {
    val ktlint by configurations.creating
    dependencies {
        ktlint("com.pinterest:ktlint:${property("ktlint.version")}") {
            attributes {
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
            }
        }
    }
    val ktlintCheck by tasks.registering(JavaExec::class) {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        description = "Check Kotlin code style"
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
        args(
            "**/src/**/*.kt",
            "**.kts",
            "!**/build/**",
        )
    }

    afterEvaluate {
        tasks.check {
            dependsOn(ktlintCheck)
        }
    }

    tasks.register<JavaExec>("ktlintFormat") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        description = "Check Kotlin code style and format"
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
        // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
        args(
            "-F",
            "**/src/**/*.kt",
            "**.kts",
            "!**/build/**",
        )
    }
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":openai"))
                implementation(project(":generativeai"))
                implementation("org.jetbrains.compose.material3:material3-desktop:${property("compose.version")}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("kotlinx.coroutine.core.version")}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx.serialization.version")}")
                implementation("io.realm.kotlin:library-base:${property("realm.version")}")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit5")
                implementation("org.junit.jupiter:junit-jupiter")
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    if (name == "jvmTest") {
        useJUnitPlatform()
    }
}

compose.desktop {
    application {
        mainClass = "net.longbowxxx.playground.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "PlaygroundL"
            packageVersion = property("package.version") as String
        }

        buildTypes.release.proguard {
            configurationFiles.from("proguard-rules.pro")
        }
    }
}

tasks.register("downloadPalm2BetaSDK") {
    group = "build"
    description = "Since the SDK for PaLM2 is a Beta version, download the file and install it on mavenLocal"
    dependsOn(emptyArray<String>())
    doLast {
        val baseName = "google-cloud-ai-generativelanguage-v1beta2-java"
        val outFileName = "$baseName.tar.gz"
        val palm2WorkDir = "${rootProject.rootDir}/palm2"
        File(palm2WorkDir).mkdirs()
        val outFile = File("$palm2WorkDir/$outFileName")
        val url = URL("https://storage.googleapis.com/generativeai-downloads/clients/$outFileName")
        outFile.outputStream().use { out ->
            url.openStream().use {
                it.copyTo(out)
            }
        }
        copy {
            from(tarTree(resources.gzip(outFile)))
            into(palm2WorkDir)
        }
        exec {
            workingDir = File("$palm2WorkDir/$baseName")
            executable("./gradlew.bat")
            args("publishToMavenLocal")
        }
    }
}

tasks.register<Copy>("copyArtifacts") {
    group = "release"
    from("$buildDir/compose/binaries/main-release/app/PlaygroundL")
    into("$buildDir/tmp/release")
    dependsOn("createReleaseDistributable")
}

tasks.register<Copy>("copyDocuments") {
    group = "release"
    from("$rootDir/documents")
    into("$buildDir/tmp/release")
}

tasks.register<Copy>("copyChatPrompt") {
    group = "release"
    from("$rootDir/chatPrompt")
    into("$buildDir/tmp/release/chatPrompt")
}

tasks.register<Copy>("copyChatMessage") {
    group = "release"
    from("$rootDir/chatMessage")
    into("$buildDir/tmp/release/chatMessage")
}

tasks.register<Copy>("copyChatFunction") {
    group = "release"
    from("$rootDir/chatFunction")
    into("$buildDir/tmp/release/chatFunction")
}

tasks.register<Zip>("zipArtifacts") {
    group = "release"
    from("$buildDir/tmp/release")
    destinationDirectory.set(file("$rootDir/releaseArtifacts"))
    archiveFileName.set("${project.name}-${rootProject.property("package.version")}.zip")

    dependsOn("copyArtifacts")
    dependsOn("copyDocuments")
    dependsOn("copyChatPrompt")
    dependsOn("copyChatMessage")
    dependsOn("copyChatFunction")
}
