rootProject.name = "monolith-simulator"

includeBuild("build-logic")

val moduleCount: String? by settings

(1..(moduleCount?.toIntOrNull() ?: 5)).forEach { index ->
    logger.lifecycle("Creating module$index")

    val module = file("${rootDir}/submodules/module-$index").apply {
        mkdirs()
    }

    module.resolve("build.gradle.kts").writeText("""
        plugins {
            id("com.adcubum.demo.kotlin")
            id("com.adcubum.demo.artifactory")
        }
    """.trimIndent())
    module.resolve("src/main/kotlin/com/adcubum/demo/Module.kt").apply {
        parentFile.mkdirs()
        writeText("""
        package com.adcubum.demo
        
        class Module
    """.trimIndent())
    }

    include("submodules:module-$index")
}

/**
 * Comment out the following lines to enable Gradle Enterprise.
 */
//pluginManagement {
//    repositories {
//        gradlePluginPortal()
//    }
//}
//
//plugins {
//    id("com.gradle.enterprise") version "3.15.1"
//}
//
//gradleEnterprise {
//    server = "<tbd>"
//    allowUntrustedServer = true
//    buildScan {
//        publishAlways()
//    }
//}