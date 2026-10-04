# JUnit 5 Samples [![ci-badge]][ci-actions]

Welcome to _JUnit 5 Samples_, a collection of sample applications and extensions
using JUnit Jupiter, JUnit Vintage, and the JUnit Platform on various build systems.

CI builds for sample projects are performed by [GitHub Actions][ci-actions]. Using JDK 11+'s
`jshell` tool, you may build all samples via the `build-all-samples.jsh` script.

## Jupiter Starter Samples

_Basic setups showing how to get started with JUnit Jupiter._

### Jupiter on Ant ![badge-jdk-8] ![badge-tool-ant] ![badge-junit-jupiter]

The [junit5-jupiter-starter-ant] sample demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Ant build system.

### Jupiter on Gradle ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit5-jupiter-starter-gradle] sample demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Gradle build system.

### Jupiter on Gradle using Kotlin ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit5-jupiter-starter-gradle-kotlin] sample demonstrates the bare minimum
configuration for getting started with JUnit Jupiter using the Gradle build system and the
Kotlin programming language.

### Jupiter on Gradle using Groovy ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit5-jupiter-starter-gradle-groovy] sample demonstrates the bare minimum
configuration for getting started with JUnit Jupiter using the Gradle build system and the
Groovy programming language.

### Jupiter on Maven ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-jupiter]

The [junit5-jupiter-starter-maven] sample demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Maven build system.

### Jupiter on Maven using Kotlin ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-jupiter]

The [junit5-jupiter-starter-maven-kotlin] sample demonstrates the bare minimum configuration for
getting started with JUnit Jupiter project using Maven build system and Kotlin programming language.

### Jupiter on Bazel ![badge-jdk-8] ![badge-tool-bazel] ![badge-junit-jupiter]

The [junit5-jupiter-starter-bazel] sample demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Bazel build system.

## Jupiter Feature Samples

_Extending JUnit Jupiter using its `Extension` API._

### Sample Extensions ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit5-jupiter-extensions] sample demonstrates how one can implement custom
JUnit Jupiter extensions and use them in tests.


## Migration Samples

_More complex setups how to integrate various parts of "JUnit 5" including a
possible migration path for JUnit 3 or 4 based projects._

### Gradle Migration ![badge-jdk-8] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]

The [junit5-migration-gradle] sample demonstrates how to set up a Gradle project
using the JUnit Platform, JUnit Jupiter, and JUnit Vintage.

### Maven Migration ![badge-jdk-8] ![badge-tool-maven] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]

The [junit5-migration-maven] sample demonstrates how to set up a Maven project
using the JUnit Platform, JUnit Jupiter, and JUnit Vintage.


## Platform Samples
_Showing basic features of the JUnit Platform._

### Multiple Engines ![badge-jdk-11] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage] ...

The [junit5-multiple-engines] sample demonstrates how to set up a Gradle project
using the JUnit Platform with various [TestEngine][guide-custom-engine] implementations.

### Living in the Modular World ![badge-jdk-11] ![badge-tool-console] ![badge-junit-platform]

The [junit5-modular-world] sample demonstrates how to test code organized in modules.
This sample also demonstrates how to implement a custom [TestEngine][guide-custom-engine]
for the JUnit Platform using the Java Platform Module System.

[junit5-jupiter-extensions]: junit5-jupiter-extensions
[junit5-jupiter-starter-ant]: junit-jupiter-starter-ant
[junit5-jupiter-starter-gradle]: junit5-jupiter-starter-gradle
[junit5-jupiter-starter-gradle-groovy]: junit5-jupiter-starter-gradle-groovy
[junit5-jupiter-starter-gradle-kotlin]: junit5-jupiter-starter-gradle-kotlin
[junit5-jupiter-starter-maven]: junit5-jupiter-starter-maven
[junit5-jupiter-starter-maven-kotlin]: junit5-jupiter-starter-maven-kotlin
[junit5-jupiter-starter-bazel]: junit5-jupiter-starter-bazel
[junit5-migration-gradle]: junit5-migration-gradle
[junit5-migration-maven]: junit5-migration-maven
[junit5-multiple-engines]: junit5-multiple-engines
[junit5-modular-world]: junit5-modular-world

[badge-jdk-8]: https://img.shields.io/badge/jdk-8-lightgray.svg "JDK-8"
[badge-jdk-11]: https://img.shields.io/badge/jdk-11-red.svg "JDK-11 or higher"
[badge-tool-ant]: https://img.shields.io/badge/tool-ant-10f0f0.svg "Ant"
[badge-tool-gradle]: https://img.shields.io/badge/tool-gradle-blue.svg "Gradle wrapper included"
[badge-tool-maven]: https://img.shields.io/badge/tool-maven-0440af.svg "Maven wrapper included"
[badge-tool-bazel]: https://img.shields.io/badge/tool-bazel-43a047.svg "Bazel"
[badge-tool-console]: https://img.shields.io/badge/tool-console-022077.svg "Command line tools"
[badge-junit-platform]: https://img.shields.io/badge/junit-platform-brightgreen.svg "JUnit Platform"
[badge-junit-jupiter]: https://img.shields.io/badge/junit-jupiter-green.svg "JUnit Jupiter Engine"
[badge-junit-vintage]: https://img.shields.io/badge/junit-vintage-yellowgreen.svg "JUnit Vintage Engine"

[ci-badge]:https://github.com/junit-team/junit5-samples/workflows/Build%20all%20samples/badge.svg "CI build status"
[ci-actions]: https://github.com/junit-team/junit5-samples/actions

[guide-custom-engine]: http://junit.org/junit5/docs/current/user-guide/#launcher-api-engines-custom "Plugging in Your Own Test Engine"
