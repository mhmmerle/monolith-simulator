plugins {
    kotlin("jvm")
}

group = "com.adcubum.demo"
version = "0.1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
