import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "2.2.20"
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.14.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(8)
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions.jvmTarget = JVM_1_8
}
