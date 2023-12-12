plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.21")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:5.1.11")
}