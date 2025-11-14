/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

// default package

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Platform-agnostic builder
 */
@SuppressWarnings({ "WeakerAccess", "SameParameterValue" })
class Builder {

	private static final String TARGET_OPTION = "--target=";
	private static final String EXCLUDE_OPTION = "--exclude=";

	public static void main(String[] args) {
		var target = determineTarget(args);
		var excludedProjects = determineExcludedProjects(args);
		var status = new Builder().build(target, excludedProjects);
		if (status != 0) {
			throw new AssertionError("Expected exit status of zero, but got: " + status);
		}
	}

	int status = 0;

	int build(Target target, Set<String> excludedProjects) {
		System.out.printf("|%n| Building all samples (%s)...%n|%n", target);
		run(".", "java", "--version");
		if (target == Target.TEST) {
			checkLicense("src/eclipse-public-license-2.0.java", ".java", ".kt", ".scala", ".groovy");
		}

		var antTarget = target == Target.TEST ? "test" : "compile";
		var gradleTask = target == Target.TEST ? "test" : "testClasses";
		var mavenLifecycle = target == Target.TEST ? "test" : "test-compile";
		var bazelTarget = target == Target.TEST ? "test" : "build";
		var sbtTask = target == Target.TEST ? "test" : "Test / compile";
		var modularAction = target == Target.TEST ? "src/build/Build.java" : "src/build/Compile.java";

		// jupiter-starter
		if (!isWindows()) { // TODO https://github.com/junit-team/junit-examples/issues/66
			runProject(excludedProjects, "junit-jupiter-starter-ant", "build.sh", "clean", antTarget);
		}

		runProject(excludedProjects, "junit-jupiter-starter-gradle", "gradlew", gradleTask);
		runProject(excludedProjects, "junit-jupiter-starter-gradle-groovy", "gradlew", gradleTask);
		runProject(excludedProjects, "junit-jupiter-starter-gradle-kotlin", "gradlew", gradleTask);
		runProject(excludedProjects, "junit-jupiter-starter-maven", "mvnw", "--batch-mode", "clean", mavenLifecycle);
		runProject(excludedProjects, "junit-jupiter-starter-maven-kotlin", "mvnw", "--batch-mode", "clean", mavenLifecycle);
		runProject(excludedProjects, "junit-jupiter-starter-bazel", "bazel", bazelTarget, "//...");
		runProject(excludedProjects, "junit-jupiter-starter-sbt", "sbt", sbtTask);

		// jupiter-extensions
		runProject(excludedProjects, "junit-jupiter-extensions", "gradlew", gradleTask);

		// migration
		runProject(excludedProjects, "junit-migration-gradle", "gradlew", gradleTask);
		runProject(excludedProjects, "junit-migration-maven", "mvnw", "--batch-mode", "clean", mavenLifecycle);
		runProject(excludedProjects, "junit-multiple-engines", "gradlew", gradleTask);

		// modular
		runProject(excludedProjects, "junit-modular-world", "java", modularAction);

		System.out.printf("%n%n%n|%n| Done. Build exits with status = %d.%n|%n", status);
		return status;
	}

	private static Target determineTarget(String[] args) {
		for (var arg : args) {
			if (arg.startsWith(TARGET_OPTION)) {
				return Target.valueOf(arg.substring(TARGET_OPTION.length()).toUpperCase(Locale.ROOT));
			}
		}
		return Target.TEST;
	}

	private static Set<String> determineExcludedProjects(String[] args) {
		Set<String> excludedProjects = new HashSet<>();
		for (var arg : args) {
			if (arg.startsWith(EXCLUDE_OPTION)) {
				excludedProjects.addAll(Set.of(arg.substring(EXCLUDE_OPTION.length()).split(",")));
			}
		}
		return Set.copyOf(excludedProjects);
	}

	void runProject(Set<String> excludedProjects, String project, String executable, String... args) {
		if (excludedProjects.contains(project)) {
			System.out.printf("%n%n%n|%n| %s is excluded.%n|%n", project);
			return;
		}
		run(project, executable, args);
	}

	void run(String directory, String executable, String... args) {
		if (status != 0) {
			return;
		}
		System.out.printf("%n%n%n|%n| %s%n|%n", directory);
		System.out.printf("| %s %s%n|%n", executable, String.join(" ", args));
		var path = Paths.get(directory);
		var isWindows = isWindows();
		if (!isWindows) {
			if (Files.isExecutable(path.resolve(executable))) {
				executable = "./" + executable;
			}
		}
		var processBuilder = new ProcessBuilder(isWindows ? "cmd.exe" : executable);
		if (isWindows) {
			processBuilder.command().add("/C");
			processBuilder.command().add(executable);
		}
		Arrays.stream(args).forEach(processBuilder.command()::add);
		processBuilder.directory(path.toFile());
		processBuilder.redirectErrorStream(true);
		try {
			var process = processBuilder.start();
			process.getInputStream().transferTo(System.out);
			status = process.waitFor();
		} catch (Exception exception) {
			System.out.printf("%n%n%n| %s failed to build!%n", directory);
			exception.printStackTrace(System.err);
			status = 1;
		}
	}

	boolean isWindows() {
		return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
	}

	void checkLicense(String blueprint, String... extensions) {
		if (status != 0) {
			return;
		}
		System.out.printf("%n%n%n|%n| check license: %s%n|%n", blueprint);
		try {
			var expected = Files.readAllLines(Paths.get(blueprint));
			var errors = 0;
			try (var paths = Files.walk(Paths.get("."))
					.filter(path -> Arrays.stream(extensions).anyMatch(extension -> path.getFileName().toString().endsWith(extension)))
					.filter(path -> !path.getFileName().toString().equals("MavenWrapperDownloader.java"))) {
				for (var path : paths.toList()) {
					if (checkLicense(path, expected)) {
						continue;
					}
					System.out.printf("| %s%n", path);
					errors++;
				}
			}
			if (errors > 0) {
				System.out.printf("| %d file(s) with no or false license.%n", errors);
				status = 1;
			}
		}
		catch (Exception exception) {
			System.out.printf("%n%n%n| License `%s` check failed!%n", blueprint);
			exception.printStackTrace(System.err);
			status = 1;
		}
	}

	boolean checkLicense(Path path, List<String> expected) throws Exception {
		var actual = Files.readAllLines(path);
		if (actual.size() >= expected.size()) {
			actual = actual.subList(0, expected.size());
			return actual.equals(expected);
		}
		return false;
	}

	enum Target {
		COMPILE, TEST
	}
}
