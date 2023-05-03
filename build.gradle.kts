import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

group = "net.longbowxxx.playground"
version = "${rootProject.property("package.version")}"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
                implementation("org.jetbrains.compose.material3:material3-desktop:${property("compose.version")}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("kotlinx.coroutine.core.version")}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx.serialization.version")}")
            }
        }
        val jvmTest by getting
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
    }
}

tasks.register("generateBatch") {
    group = "release"
    doLast {
        // 生成するファイルの内容を定義
        val batchString = """
            set "PATH=%JAVA_HOME%\bin;%PATH%"
            java -jar ./PlaygroundL-windows-x64-${rootProject.property("package.version")}.jar
        """.trimIndent()

        // 生成するファイルのパスを指定
        val filePath = "$buildDir/tmp/release/run.bat"

        // ファイルを生成
        File(filePath).apply {
            parentFile.mkdirs()
            writeText(batchString)
        }
    }
}

tasks.register<Copy>("copyArtifacts") {
    group = "release"
    from("$buildDir/compose/jars")
    into("$buildDir/tmp/release")
    dependsOn("packageUberJarForCurrentOS")
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

tasks.register<Zip>("zipArtifacts") {
    group = "release"
    from("$buildDir/tmp/release")
    destinationDirectory.set(file("$buildDir/release"))
    archiveFileName.set("${project.name}-${rootProject.property("package.version")}.zip")

    dependsOn("generateBatch")
    dependsOn("copyArtifacts")
    dependsOn("copyDocuments")
    dependsOn("copyChatPrompt")
}
