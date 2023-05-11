plugins {
    kotlin("jvm")
    `java-library`
}

group = "net.longbowxxx.generativeai"
version = "${rootProject.property("package.version")}"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("kotlinx.coroutine.core.version")}")
    implementation("com.google.cloud:gapic-google-cloud-ai-generativelanguage-v1beta2-java:${property("google.cloud.ai.version")}")
    implementation("io.grpc:grpc-okhttp:${property("grpc.version")}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
