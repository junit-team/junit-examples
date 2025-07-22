import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "2.2.0"
}

repositories {
	mavenCentral()
	maven(url = "https://central.sonatype.com/repository/maven-snapshots") {
		mavenContent {
			snapshotsOnly()
			includeGroupByRegex("org\\.junit.*")
		}
	}
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:6.0.0-M2"))
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
	options.release.set(17)
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions.jvmTarget = JVM_17
}
