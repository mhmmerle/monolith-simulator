import org.gradle.api.internal.tasks.TaskExecutionOutcome
import org.gradle.tooling.events.task.TaskExecutionResult
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import java.io.ByteArrayOutputStream

plugins {
    `maven-publish`
    id("com.jfrog.artifactory")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

configure<ArtifactoryPluginConvention> {
    setContextUrl(project.property("artifactoryBaseUrl") as String)
    publish {
        repository {
            repoKey = project.property("artifactoryRepo") as String
            if (project.hasProperty("artifactoryUsername")) {
                username = project.property("artifactoryUsername") as String
            }
            if (project.hasProperty("artifactoryPassword")) {
                password = project.property("artifactoryPassword") as String
            }
        }
        defaults {
            publications("mavenJava")
            setPublishArtifacts(true)
            setPublishPom(true)
        }
    }
    clientConfig.info.buildName = project.property("artifactoryBuildName").toString()
    clientConfig.info.buildNumber = project.property("buildNumber").toString()
}

if (rootProject.tasks.findByName("promote") == null) {
    logger.lifecycle("Registering promote task on ${rootProject.name}")

    rootProject.tasks.register("promote") {

        rootProject.subprojects.forEach { subproject ->
            subproject.plugins.withType<ArtifactoryPlugin>() {
                val artifactoryPublishTask = subproject.tasks.findByName("artifactoryPublish")
                if (artifactoryPublishTask != null) {
                    dependsOn(artifactoryPublishTask)
                    dependsOn("artifactoryDeploy")
                }
            }
        }

        val artifactoryBaseUrl = project.property("artifactoryBaseUrl")
        val artifactoryBuildName = project.property("artifactoryBuildName")
        val username = project.property("artifactoryUsername")
        val password = project.property("artifactoryPassword")
        val buildNumber = project.property("buildNumber")

        val url = "$artifactoryBaseUrl/api/build/promote/$artifactoryBuildName/$buildNumber"
        logger.lifecycle("Promoting build to $url")

        doLast {
            val output = ByteArrayOutputStream()
            exec {
                commandLine(
                    "curl",
                    "-u",
                    "$username:$password",
                    url,
                    "--request",
                    "POST",
                    "--header",
                    "Content-Type: application/json",
                    "--data",
                    """{"status": "promoted", "dry-run": "false"}""",
                    "-iv"
                )
                standardOutput = output
            }

            if (!output.toString().contains("HTTP/1.1 200 OK")) {
                logger.error(output.toString())
                throw RuntimeException("Promotion failed")
            }
        }
    }
}


