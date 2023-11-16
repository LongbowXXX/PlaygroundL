import org.jetbrains.dokka.gradle.AbstractDokkaTask
import java.net.URL

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
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
    apply(plugin = "org.jetbrains.dokka")
    tasks.withType<AbstractDokkaTask>().configureEach {
        val dokkaBaseConfiguration = """
        {
           "footerMessage": "Copyright (c) 2023 LongbowXXX"
        }
        """
        pluginsMapConfiguration.set(
            mapOf(
                "org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration
            )
        )
    }
}

tasks.dokkaHtmlMultiModule {
    includes.from("README.md", "HowToUse.md")

    val mv00 = file("images/00-Setup-API-Key.mp4").toString().replace("\\", "\\\\")
    val mv10 = file("images/10-OpenAI-Chat.mp4").toString().replace("\\", "\\\\")
    val mv11 = file("images/11_OpenAI-Chat-Function-1.mp4").toString().replace("\\", "\\\\")
    val mv12 = file("images/12_OpenAI-Chat-Function-2.mp4").toString().replace("\\", "\\\\")
    val mv15 = file("images/15_OpenAI-Chat-Restore-Old-Session.mp4").toString().replace("\\", "\\\\")
    val mv20 = file("images/20_PaLM2-Discuss-Service.mp4").toString().replace("\\", "\\\\")
    val mv30 = file("images/30_OpenAI-Image.mp4").toString().replace("\\", "\\\\")
    val dokkaBaseConfiguration = """
        {
            "customAssets": ["$mv00","$mv10","$mv11","$mv12","$mv15","$mv20","$mv30"]
        }
        """
    pluginsMapConfiguration.set(
        mapOf(
            "org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration
        )
    )
}

subprojects {
    val ktlint by configurations.creating
    dependencies {
        ktlint("com.pinterest.ktlint:ktlint-cli:${property("ktlint.version")}") {
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

tasks.register("downloadPalm2BetaSDK") {
    group = "build"
    description = "Since the SDK for PaLM2 is a Beta version, download the file and install it on mavenLocal"
    dependsOn(emptyArray<String>())
    doLast {
        val baseName = "google-cloud-ai-generativelanguage-v1beta3-java"
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
    destinationDirectory.set(file("$buildDir/release"))
    archiveFileName.set("${project.name}-${rootProject.property("package.version")}.zip")

    dependsOn("app:copyArtifacts")
    dependsOn("copyChatPrompt")
    dependsOn("copyChatMessage")
    dependsOn("copyChatFunction")
}
