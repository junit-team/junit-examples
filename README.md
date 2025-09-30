# JUnit Examples [![ci-badge]][ci-actions]

Welcome to _JUnit Examples_, a collection of example applications and extensions
using JUnit Jupiter, JUnit Vintage, and the JUnit Platform on various build systems.

CI builds for example projects are performed by [GitHub Actions][ci-actions]. Using JDK 24+'s
`java` multi-file source-code launcher feature, you may build all examples by running
`java src/Builder.java` in the main directory of this project.

## Jupiter Starter Examples

_Basic setups showing how to get started with JUnit Jupiter._

### Jupiter on Ant ![badge-jdk-17] ![badge-tool-ant] ![badge-junit-jupiter]

The [junit-jupiter-starter-ant] example demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Ant build system.

### Jupiter on Gradle ![badge-jdk-17] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit-jupiter-starter-gradle] example demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Gradle build system.

### Jupiter on Gradle using Kotlin ![badge-jdk-17] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit-jupiter-starter-gradle-kotlin] example demonstrates the bare minimum
configuration for getting started with JUnit Jupiter using the Gradle build system and the
Kotlin programming language.

### Jupiter on Gradle using Groovy ![badge-jdk-17] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit-jupiter-starter-gradle-groovy] example demonstrates the bare minimum
configuration for getting started with JUnit Jupiter using the Gradle build system and the
Groovy programming language.

### Jupiter on Maven ![badge-jdk-17] ![badge-tool-maven] ![badge-junit-jupiter]

The [junit-jupiter-starter-maven] example demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Maven build system.

### Jupiter on Maven using Kotlin ![badge-jdk-17] ![badge-tool-maven] ![badge-junit-jupiter]

The [junit-jupiter-starter-maven-kotlin] example demonstrates the bare minimum configuration for
getting started with JUnit Jupiter project using Maven build system and Kotlin programming language.

### Jupiter on Bazel ![badge-jdk-17] ![badge-tool-bazel] ![badge-junit-jupiter]

The [junit-jupiter-starter-bazel] example demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using the Bazel build system.

### Jupiter on sbt ![badge-jdk-17] ![badge-tool-sbt] ![badge-junit-jupiter]

The [junit-jupiter-starter-sbt] example demonstrates the bare minimum configuration for
getting started with JUnit Jupiter using sbt and the Scala programming language.

## Jupiter Feature Examples

_Extending JUnit Jupiter using its `Extension` API._

### Sample Extensions ![badge-jdk-17] ![badge-tool-gradle] ![badge-junit-jupiter]

The [junit-jupiter-extensions] example demonstrates how one can implement custom
JUnit Jupiter extensions and use them in tests.


## Migration Examples

_More complex setups how to integrate various parts of "JUnit 5" including a
possible migration path for JUnit 3 or 4 based projects._

### Gradle Migration ![badge-jdk-17] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]

The [junit-migration-gradle] example demonstrates how to set up a Gradle project
using the JUnit Platform, JUnit Jupiter, and JUnit Vintage.

### Maven Migration ![badge-jdk-17] ![badge-tool-maven] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage]

The [junit-migration-maven] example demonstrates how to set up a Maven project
using the JUnit Platform, JUnit Jupiter, and JUnit Vintage.


## Platform Samples
_Showing basic features of the JUnit Platform._

### Multiple Engines ![badge-jdk-17] ![badge-tool-gradle] ![badge-junit-platform] ![badge-junit-jupiter] ![badge-junit-vintage] ...

The [junit-multiple-engines] example demonstrates how to set up a Gradle project
using the JUnit Platform with various [TestEngine][guide-custom-engine] implementations.

### Living in the Modular World ![badge-jdk-17] ![badge-tool-console] ![badge-junit-platform]

The [junit-modular-world] example demonstrates how to test code organized in modules.
This example also demonstrates how to implement a custom [TestEngine][guide-custom-engine]
for the JUnit Platform using the Java Platform Module System.

[junit-jupiter-extensions]: junit-jupiter-extensions
[junit-jupiter-starter-ant]: junit-jupiter-starter-ant
[junit-jupiter-starter-gradle]: junit-jupiter-starter-gradle
[junit-jupiter-starter-gradle-groovy]: junit-jupiter-starter-gradle-groovy
[junit-jupiter-starter-gradle-kotlin]: junit-jupiter-starter-gradle-kotlin
[junit-jupiter-starter-maven]: junit-jupiter-starter-maven
[junit-jupiter-starter-maven-kotlin]: junit-jupiter-starter-maven-kotlin
[junit-jupiter-starter-bazel]: junit-jupiter-starter-bazel
[junit-jupiter-starter-sbt]: junit-jupiter-starter-sbt
[junit-migration-gradle]: junit-migration-gradle
[junit-migration-maven]: junit-migration-maven
[junit-multiple-engines]: junit-multiple-engines
[junit-modular-world]: junit-modular-world

[badge-jdk-17]: https://img.shields.io/badge/jdk-17-orange.svg "JDK-17"
[badge-tool-ant]: https://img.shields.io/badge/tool-ant-aa167e.svg "Ant"
[badge-tool-gradle]: https://img.shields.io/badge/tool-gradle-209bc4.svg "Gradle wrapper included"
[badge-tool-maven]: https://img.shields.io/badge/tool-maven-0440af.svg "Maven wrapper included"
[badge-tool-bazel]: https://img.shields.io/badge/tool-bazel-43a047.svg "Bazel"
[badge-tool-sbt]: https://img.shields.io/badge/tool-sbt-4A34BC.svg "SBT"
[badge-tool-console]: https://img.shields.io/badge/tool-console-022077.svg "Command line tools"
[badge-junit-platform]: https://img.shields.io/badge/junit-platform-brightgreen.svg "JUnit Platform"
[badge-junit-jupiter]: https://img.shields.io/badge/junit-jupiter-green.svg "JUnit Jupiter Engine"
[badge-junit-vintage]: https://img.shields.io/badge/junit-vintage-yellowgreen.svg "JUnit Vintage Engine"

[ci-badge]:https://github.com/junit-team/junit-examples/workflows/Build%20all%20examples/badge.svg "CI build status"
[ci-actions]: https://github.com/junit-team/junit-examples/actions

[guide-custom-engine]: https://docs.junit.org/current/user-guide/#launcher-api-engines-custom "Plugging in Your Own Test Engine"
