plugins {
    java
    groovy
    eclipse // optional (to generate Eclipse project files)
    idea // optional (to generate IntelliJ IDEA project files)
    kotlin("jvm") version "1.5.31"
}

repositories {
    mavenCentral()
}

dependencies {
    val junit4Version = "4.13.2"
    val junitBomVersion = "5.8.2"

    // Use junit-bom to align versions
    // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
    implementation(platform("org.junit:junit-bom:$junitBomVersion")) {
        because("Platform, Jupiter, and Vintage versions should match")
    }

    // JUnit Jupiter
    testImplementation("org.junit.jupiter:junit-jupiter")

    // JUnit Vintage
    testImplementation("junit:junit:$junit4Version")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine") {
        because("allows JUnit 3 and JUnit 4 tests to run")
    }

    // JUnit Suites
    testImplementation("org.junit.platform:junit-platform-suite")

    // JUnit Platform Launcher + Console
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
        because("allows tests to run from IDEs that bundle older version of launcher")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-console") {
        because("needed to launch the JUnit Platform Console program")
    }

    // Mainrunner
    testImplementation("de.sormuras.mainrunner:de.sormuras.mainrunner.engine:2.1.4") {
        because("executes Java programs as tests")
    }

    // jqwik
    testImplementation("net.jqwik:jqwik:1.6.0") {
        because("allows jqwik properties to run")
    }

    // Spek2
    val spekVersion = "2.0.17"
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")

    // Spock2
    testImplementation("org.spockframework:spock-core:2.1-M2-groovy-3.0") {
        because("allows Spock specifications to run")
    }

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:4.6.3")
    testRuntimeOnly("org.slf4j:slf4j-nop:1.7.32") {
        because("defaulting to no-operation (NOP) logger implementation")
    }

    // TestNG
    testImplementation("org.testng:testng:7.4.0") {
        because("allows writing TestNG tests")
    }
    testRuntimeOnly("org.junit.support:testng-engine:1.0.1") {
        because("allows running TestNG tests on the JUnit Platform")
    }
}

tasks {

    withType<JavaCompile>().configureEach {
        options.release.set(8)
    }

    val consoleLauncherTest by registering(JavaExec::class) {
        dependsOn(testClasses)
        val reportsDir = file("$buildDir/test-results")
        outputs.dir(reportsDir)
        classpath = sourceSets.test.get().runtimeClasspath
        main = "org.junit.platform.console.ConsoleLauncher"
        args("--scan-classpath")
        args("--include-classname", ".*((Tests?)|(Spec))$")
        args("--details", "tree")
        args("--reports-dir", reportsDir)
    }

    test {
        // useJUnitPlatform() ... https://github.com/gradle/gradle/issues/4912
        dependsOn(consoleLauncherTest)
        exclude("**/*")
    }
}
