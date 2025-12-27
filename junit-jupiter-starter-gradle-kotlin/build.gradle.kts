import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "2.3.0"
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:6.0.1"))
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
	options.release = 17
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions.jvmTarget = JVM_17
}
