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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Updates the versions of JUnit Framework artifacts in all example projects.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
class Updater {

    private static final String VERSION_REGEX = "([0-9.]+(?:-[A-Z]+[0-9]*)?)";

    public static void main(String[] args) throws Exception {
        new Updater(args[0]).update();
    }

    private final String newVersion;

    public Updater(String newVersion) {
        this.newVersion = newVersion;
    }

    void update() throws IOException {
        var gradleBomReplacement = Pattern.compile("org.junit:junit-bom:" + VERSION_REGEX);
        var mavenBomReplacement = Pattern.compile(
                """
                \\s*<groupId>org.junit</groupId>
                \\s*<artifactId>junit-bom</artifactId>
                \\s*<version>""" + VERSION_REGEX + "</version>",
                Pattern.MULTILINE
        );

        update(Path.of("junit-jupiter-extensions/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit-jupiter-starter-ant/build.sh"), List.of(
                Pattern.compile("junit_version='" + VERSION_REGEX + "'")
        ));
        update(Path.of("junit-jupiter-starter-bazel/MODULE.bazel"), List.of(
                Pattern.compile("JUNIT_VERSION = \"" + VERSION_REGEX + '"')
        ));
        update(Path.of("junit-jupiter-starter-gradle/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit-jupiter-starter-gradle-groovy/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit-jupiter-starter-gradle-kotlin/build.gradle.kts"), List.of(gradleBomReplacement));
        update(Path.of("junit-jupiter-starter-maven/pom.xml"), List.of(mavenBomReplacement));
        update(Path.of("junit-jupiter-starter-maven-kotlin/pom.xml"), List.of(mavenBomReplacement));
        update(Path.of("junit-jupiter-starter-sbt/build.sbt"), List.of(
                Pattern.compile("\"org.junit.jupiter\" % \"junit-jupiter\" % \"" + VERSION_REGEX + '"'),
                Pattern.compile("\"org.junit.platform\" % \"junit-platform-launcher\" % \"" + VERSION_REGEX + '"')
        ));
        update(Path.of("junit-migration-gradle/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit-migration-gradle/README.md"), List.of(
                Pattern.compile("org.junit.jupiter:junit-jupiter:" + VERSION_REGEX),
                Pattern.compile("org.junit.vintage:junit-vintage-engine:" + VERSION_REGEX)
        ));
        update(Path.of("junit-migration-maven/pom.xml"), List.of(mavenBomReplacement));
        update(Path.of("junit-modular-world/src/build/Project.java"), List.of(
                Pattern.compile("junitVersion = \"" + VERSION_REGEX + '"')
        ));
        update(Path.of("junit-multiple-engines/build.gradle.kts"), List.of(
                Pattern.compile("junitBomVersion = \"" + VERSION_REGEX + '"')
        ));
        update(Path.of("junit-source-launcher/lib/DownloadRequiredModules.java"), List.of(
                Pattern.compile("final String version = \"" + VERSION_REGEX + '\"')
        ));
    }

    void update(Path path, List<Pattern> patterns) throws IOException {
        System.out.printf("Updating %s...", path);
        System.out.flush();
        int matches = 0;
        var content = new StringBuilder(Files.readString(path));
        for (var pattern : patterns) {
            var minIndex = 0;
            var matcher = pattern.matcher(content);
            while (matcher.find(minIndex)) {
                matches++;
                int start = matcher.start(1);
                int end = matcher.end(1);
                int oldLength = end - start;
                content.replace(start, end, newVersion);
                minIndex = end + newVersion.length() - oldLength;
            }
        }
        Files.writeString(path, content);
        System.out.printf(" %d%n", matches);
        if (matches == 0) {
            throw new IllegalStateException("No matches found in " + path);
        }
    }
}
