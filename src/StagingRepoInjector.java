/*
 * Copyright 2015-2026 the original author or authors.
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
import java.util.function.Consumer;

public class StagingRepoInjector {

    public static void main(String[] args) throws Exception {
        new StagingRepoInjector(args[0]).inject();
    }

    private final String stagingRepoUrl;

    private StagingRepoInjector(String stagingRepoUrl) {
        this.stagingRepoUrl = stagingRepoUrl;
    }

    private void inject() throws Exception {

        var gradleGroovyDslSnippet = """
                
                maven {
                    url = '%s'
                    credentials(HttpHeaderCredentials) {
                        name = 'Authorization'
                        value = "Bearer ${System.getenv('MAVEN_CENTRAL_USER_TOKEN')}"
                    }
                    authentication {
                        header(HttpHeaderAuthentication)
                    }
                }
                """
                .formatted(stagingRepoUrl);

        var gradleKotlinDslSnippet = """
                
                maven {
                    url = uri("%s")
                    credentials(HttpHeaderCredentials::class) {
                        name = "Authorization"
                        value = "Bearer ${System.getenv("MAVEN_CENTRAL_USER_TOKEN")}"
                    }
                    authentication {
                        create<HttpHeaderAuthentication>("header")
                    }
                }
                """
                .formatted(stagingRepoUrl);

        appendAfter("junit-jupiter-extensions/build.gradle", "mavenCentral()",
                gradleGroovyDslSnippet);

        replace("junit-jupiter-starter-ant/build.sh", "\"https://repo1.maven.org/maven2",
                "--header \"Authorization: Bearer $MAVEN_CENTRAL_USER_TOKEN\" \"%s".formatted(stagingRepoUrl));

        appendAfter("junit-jupiter-starter-gradle/build.gradle", "mavenCentral()",
                gradleGroovyDslSnippet);

        appendAfter("junit-jupiter-starter-gradle-groovy/build.gradle", "mavenCentral()",
                gradleGroovyDslSnippet);

        appendAfter("junit-jupiter-starter-gradle-kotlin/build.gradle.kts", "mavenCentral()",
                gradleKotlinDslSnippet);

        appendAfter("junit-migration-gradle/build.gradle", "mavenCentral()",
                gradleGroovyDslSnippet);

        replace("junit-modular-world/src/build/Project.java", "\"https://repo1.maven.org/maven2\"",
                "group.startsWith(\"org.junit\") ? \"%s\" : \"https://repo1.maven.org/maven2\"".formatted(stagingRepoUrl));

        appendAfter("junit-multiple-engines/build.gradle.kts", "mavenCentral()",
                gradleKotlinDslSnippet);

        replace("junit-source-launcher/lib/DownloadRequiredModules.java",
                "final String repository = \"https://repo.maven.apache.org/maven2\"",
                "final String repository = \"%s\"".formatted(stagingRepoUrl));
    }

    void appendAfter(String path, String token, String addedContent) throws IOException {
        process(path, content -> {
            if (content.indexOf(token) == -1) {
                throw new IllegalStateException("Token not found in " + path);
            }
            content.insert(content.indexOf(token) + token.length(), addedContent);
        });
    }

    void replace(String path, String token, String replacement) throws IOException {
        process(path, content -> {
            if (content.indexOf(token) == -1) {
                throw new IllegalStateException("Token not found in " + path);
            }
            content.replace(content.indexOf(token), content.indexOf(token) + token.length(), replacement);
        });
    }

    void process(String path, Consumer<StringBuilder> modification) throws IOException {
        System.out.printf("Processing %s...", path);
        System.out.flush();
        var file = Path.of(path);
        var content = new StringBuilder(Files.readString(file));
        modification.accept(content);
        Files.writeString(file, content);
        System.out.println(" OK");
    }
}
