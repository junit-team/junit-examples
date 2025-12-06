import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JVM_17
    }
    compileJava {
        options.release = 17
    }
}
