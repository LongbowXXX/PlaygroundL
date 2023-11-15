import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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
                implementation(project(":search"))
                implementation("org.jetbrains.compose.material3:material3-desktop:${property("compose.version")}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("kotlinx.coroutine.core.version")}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx.serialization.version")}")
                implementation("io.realm.kotlin:library-base:${property("realm.version")}")
                implementation("org.jsoup:jsoup:${property("jsoup.version")}")
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
            windows {
                iconFile.set(File("src/jvmMain/resources/app-icon.ico"))
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from("proguard-rules.pro")
        }
    }
}

tasks.register<Copy>("copyArtifacts") {
    group = "release"
    from("$buildDir/compose/binaries/main-release/app/PlaygroundL")
    into("${rootProject.buildDir}/tmp/release")
    dependsOn("createReleaseDistributable")
}
