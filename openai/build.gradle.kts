plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `java-library`
}

group = "net.longbowxxx.openai"
version = "${rootProject.property("package.version")}"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("kotlinx.coroutine.core.version")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx.serialization.version")}")
    implementation("com.squareup.okhttp3:okhttp:${property("okhttp3.version")}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${property("kotlinx.coroutine.core.version")}")
}

tasks.test {
    useJUnitPlatform()
}
