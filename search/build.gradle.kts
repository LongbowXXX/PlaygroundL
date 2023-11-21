plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `java-library`
}

group = "net.longbowxxx.search"
version = "${rootProject.property("package.version")}"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":openai"))
    implementation(project(":utils"))
    implementation("com.google.api-client:google-api-client:${property("google.api.client.version")}")
    implementation("com.google.apis:google-api-services-customsearch:${property("google.services.customsearch.version")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("kotlinx.coroutine.core.version")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx.serialization.version")}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${property("kotlinx.coroutine.core.version")}")
}

tasks.test {
    useJUnitPlatform()
}
