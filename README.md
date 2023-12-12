# Monolith Simulator

## Getting Started

### Prerequisites

Following tools are required to build and run the project.

- Java21 (JDK)
- Curl

### Functionality

The project contains a task `promote` which:

- Creates based on `moduleCount` submodules in the `submodules` directory.
- Builds all modules.
- Publishes all modules to the artifactory. Artifactory setup is required (see `gradle.properties`).
- Sets the build status of the artifactory to promoted.

### Usage

1. Check the values in the `gradle.properties` file and adapt them to your system.

2. Start the test with following command.

 ```bash
 ./gradlew promote \
        -PmoduleCount=<MODULE_COUNT> \
        -PbuildNumber=<BUILD_NUMBER> \
        -PartifactoryUsername=<ARTIFACTORY_USERNAME> \
        -PartifactoryPassword=<ARTIFACTORY_PASSWORD>
 ```

| Parameter            | Description                            | default    |
|----------------------|----------------------------------------|------------|
| MODULE_COUNT         | Number of modules to build and publish | 5          |
| BUILD_NUMBER         | Build number which is used             | 1          |
| ARTIFACTORY_USERNAME | Username to use for Artifactory        | no default |
| ARTIFACTORY_PASSWORD | Password to use for Artifactory        | no default |

### Clean

```bash
git clean -fdx
```