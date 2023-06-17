plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `java-library`
}

group = "net.longbowxxx.generativeai"
version = "${rootProject.property("package.version")}"

repositories {
    mavenCentral()
    // for Palm2 SDK (Beta)
    mavenLocal()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("kotlinx.coroutine.core.version")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx.serialization.version")}")
    implementation("com.google.cloud:gapic-google-cloud-ai-generativelanguage-v1beta2-java:${property("google.cloud.ai.version")}")
    implementation("io.grpc:grpc-okhttp:${property("grpc.version")}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
