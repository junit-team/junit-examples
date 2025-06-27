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

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.spi.*;
import java.util.stream.*;

record Project() {
  static Project ofCurrentWorkingDirectory() {
    var currentWorkingDirectory = Path.of("").toAbsolutePath();
    var expected = "junit-modular-world";
    if (!expected.equals(currentWorkingDirectory.getFileName().toString())) {
      throw new AssertionError(
          """
          Expected current working directory to end with: %s
          but got: %s
          """
              .formatted(expected, currentWorkingDirectory));
    }
    return new Project();
  }

  void clean() throws Exception {
    del("bin");
    del("lib");
    Files.deleteIfExists(Path.of(".jqwik-database"));
  }

  void compile() throws Exception {
    //
    // download main and test dependencies
    //
    String junitVersion = "6.0.0-SNAPSHOT";
    get("lib", "org.junit.platform", "junit-platform-commons", junitVersion);
    get("lib", "org.junit.platform", "junit-platform-console", junitVersion);
    get("lib", "org.junit.platform", "junit-platform-engine", junitVersion);
    get("lib", "org.junit.platform", "junit-platform-launcher", junitVersion);
    get("lib", "org.junit.platform", "junit-platform-reporting", junitVersion);
    get("lib", "org.junit.jupiter", "junit-jupiter-api", junitVersion);
    get("lib", "org.junit.jupiter", "junit-jupiter-params", junitVersion);
    get("lib", "org.junit.jupiter", "junit-jupiter-engine", junitVersion);
    get("lib", "org.junit.vintage", "junit-vintage-engine", junitVersion);
    get("lib", "junit", "junit", "4.13.2");
    get("lib", "org.hamcrest", "hamcrest-core", "1.3");
    get("lib", "org.apiguardian", "apiguardian-api", "1.1.2");
    get("lib", "org.jspecify", "jspecify", "1.0.0");
    get("lib", "org.opentest4j", "opentest4j", "1.3.0");
    get("lib", "org.opentest4j.reporting", "open-test-reporting-tooling-spi", "0.2.3");
    get("lib", "net.jqwik", "jqwik-api", "1.9.3");
    get("lib", "net.jqwik", "jqwik-engine", "1.9.3");

    //
    // compile and package tool module
    //
    run(
        "javac",
        "-d",
        "bin/main",
        "--module-source-path",
        "src/main",
        "--module",
        "com.example.tool");
    run(
        "jar",
        "--create",
        "--file",
        "bin/main-jars/com.example.tool.jar",
        "-C",
        "bin/main/com.example.tool",
        ".");
    run("jar", "--describe-module", "--file", "bin/main-jars/com.example.tool.jar");

    //
    // compile and package application module
    //
    run(
        "javac",
        "-d",
        "bin/main",
        "--module-source-path",
        "src/main",
        "--module",
        "com.example.application");
    run(
        "jar",
        "--create",
        "--file",
        "bin/main-jars/com.example.application.jar",
        "--main-class",
        "com.example.application.Main",
        "-C",
        "bin/main/com.example.application",
        ".");
    run("jar", "--describe-module", "--file", "bin/main-jars/com.example.application.jar");

    //
    // compile and package "ice.cream" module
    //
    run(
        "javac",
        "-d",
        "bin/main",
        "--module-path",
        "lib",
        "--module-source-path",
        "src/main",
        "--module",
        "ice.cream");
    run(
        "jar",
        "--create",
        "--file",
        "bin/main-jars/ice.cream.jar",
        "--module-version",
        "47.11",
        "-C",
        "bin/main/ice.cream",
        ".");
    run("jar", "--describe-module", "--file", "bin/main-jars/ice.cream.jar");

    //
    // run application
    //
    exe("java", "--module-path", "bin/main-jars", "--module", "com.example.application", "3", "4");
  }

  void test() throws Exception {
    testClasspath();
    testPatchCompile();
    testPatchRuntime();
  }

  void testClasspath() throws Exception {

    //
    // [test-classpath] compile and run tests on the classpath
    //
    Arguments args = new Arguments();
    args.add("-d").add("bin/test-classpath");
    args.add("--class-path")
        .addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib");
    args.addAllFiles(
        "src/test",
        path -> {
          String name = path.getFileName().toString();
          return name.endsWith(".java") && !name.contains("module-info");
        });
    run("javac", args.toArray());

    args = new Arguments();
    args.add("--class-path").addPath("bin/test-classpath", "bin/main-jars/*", "lib/*");
    args.add("org.junit.platform.console.ConsoleLauncher");
    args.add("execute");
    args.add("--reports-dir").add("bin/test-classpath-results/junit-platform");
    args.add("--scan-class-path");
    exe("java", args.toArray());
  }

  void testPatchCompile() throws Exception {

    //
    // [test] compile test modules and run all tests on the module-path
    //
    Arguments args = new Arguments();
    args.add("-d").add("bin/test-patch-compile");
    args.add("--module-path").addPath("bin/main-jars", "lib");
    args.add("--patch-module").add("com.example.application=src/main/com.example.application");
    args.add("--patch-module").add("com.example.tool=src/main/com.example.tool");
    args.add("--patch-module").add("ice.cream=src/main/ice.cream");
    args.add("--module-source-path").add("src/test");
    args.addAllFiles("src/test", ".java");
    run("javac", args.toArray());

    args = new Arguments();
    args.add("--module-path").addPath("bin/test-patch-compile", "bin/main-jars", "lib");
    args.add("--add-modules").add("ALL-MODULE-PATH,ALL-DEFAULT");
    args.add("--add-opens")
        .add("org.junit.platform.engine/org.junit.platform.engine.support.filter=net.jqwik.engine");
    args.add("--add-opens")
        .add("org.junit.platform.commons/org.junit.platform.commons.util=net.jqwik.engine");
    args.add("--module").add("org.junit.platform.console");
    args.add("execute");
    args.add("--reports-dir").add("bin/test-patch-compile-results/junit-platform");
    args.add("--scan-modules");
    exe("java", args.toArray());
  }

  void testPatchRuntime() throws Exception {
    //
    // [test-patch-runtime] compile and run tests on the patched module-path
    //
    Arguments args = new Arguments();
    args.add("-d").add("bin/test-patch-runtime/com.example.application");
    args.add("--class-path")
        .addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib");
    args.addAllFiles(
        "src/test/com.example.application",
        path -> {
          String name = path.getFileName().toString();
          return name.endsWith(".java") && !name.contains("module-info");
        });
    run("javac", args.toArray());
    args = new Arguments();
    args.add("-d").add("bin/test-patch-runtime/com.example.tool");
    args.add("--class-path")
        .addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib");
    args.addAllFiles(
        "src/test/com.example.tool",
        path -> {
          String name = path.getFileName().toString();
          return name.endsWith(".java") && !name.contains("module-info");
        });
    run("javac", args.toArray());
    args = new Arguments();
    args.add("-d").add("bin/test-patch-runtime/ice.cream");
    args.add("--class-path")
        .addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib");
    args.addAllFiles(
        "src/test/ice.cream",
        path -> {
          String name = path.getFileName().toString();
          return name.endsWith(".java") && !name.contains("module-info");
        });
    run("javac", args.toArray());

    args = new Arguments();
    args.add("--module-path")
        .addPath(
            "bin/main-jars",
            "lib",
            "bin/test-patch-compile/extra.modular"); // re-use compiled "extra.modular" module here
    args.add("--add-modules").add("ALL-MODULE-PATH,ALL-DEFAULT");
    args.add("--patch-module")
        .add("com.example.application=bin/test-patch-runtime/com.example.application");
    args.add("--patch-module").add("com.example.tool=bin/test-patch-runtime/com.example.tool");
    args.add("--patch-module").add("ice.cream=bin/test-patch-runtime/ice.cream");
    args.add("--add-opens")
        .add("com.example.application/com.example.application=org.junit.platform.commons");
    args.add("--add-opens").add("com.example.tool/com.example.tool=org.junit.platform.commons");
    args.add("--add-opens")
        .add("com.example.tool/com.example.tool.internal=org.junit.platform.commons");
    args.add("--add-opens").add("ice.cream/ice.cream=org.junit.platform.commons");
    args.add("--add-opens")
        .add("org.junit.platform.engine/org.junit.platform.engine.support.filter=net.jqwik.engine");
    args.add("--add-opens")
        .add("org.junit.platform.commons/org.junit.platform.commons.util=net.jqwik.engine");
    args.add("--add-reads").add("ice.cream=org.junit.jupiter.api");
    args.add("--add-reads").add("com.example.application=org.junit.jupiter.api");
    args.add("--add-reads").add("com.example.tool=org.junit.jupiter.api");
    args.add("--module").add("org.junit.platform.console");
    args.add("execute");
    args.add("--reports-dir").add("bin/test-patch-runtime-results/junit-platform");
    args.add("--scan-modules");
    exe("java", args.toArray());
  }

  // ============

  static class Arguments {
    List<String> list = new ArrayList<>();

    Arguments add(Object o) {
      list.add(String.valueOf(o));
      return this;
    }

    Arguments addPath(Predicate<Path> predicate, String... roots) throws Exception {
      List<String> paths = new ArrayList<>();
      for (String root : roots) {
        try (var stream = Files.walk(Paths.get(root))) {
          stream.filter(predicate).map(Object::toString).forEach(paths::add);
        }
      }
      add(String.join(File.pathSeparator, paths));
      return this;
    }

    Arguments addPath(String... paths) {
      add(String.join(File.pathSeparator, paths));
      return this;
    }

    Arguments addAllFiles(String root, String extension) throws Exception {
      return addAllFiles(root, path -> path.getFileName().toString().endsWith(extension));
    }

    Arguments addAllFiles(String root, Predicate<Path> predicate) throws Exception {
      try (var stream = Files.walk(Paths.get(root))) {
        stream.filter(predicate).forEach(this::add);
      }
      return this;
    }

    String[] toArray() {
      return list.toArray(new String[0]);
    }
  }

  int run(String tool, String... args) {
    printCommandDetails("run", tool, args);
    return ToolProvider.findFirst(tool).get().run(System.out, System.err, args);
  }

  int exe(String executable, String... args) throws Exception {
    printCommandDetails("exe", executable, args);
    ProcessBuilder processBuilder = new ProcessBuilder(executable);
    Arrays.stream(args).forEach(processBuilder.command()::add);
    processBuilder.redirectErrorStream(true);
    Process process = processBuilder.start();
    process.getInputStream().transferTo(System.out);
    return process.waitFor();
  }

  void printCommandDetails(String context, String command, String... args) {
    if (args.length < 2) {
      System.out.printf("[%s] %s%s%n", context, command, (args.length == 1 ? " " + args[0] : ""));
      System.out.println();
      return;
    }
    System.out.printf("[%s] %s with %d arguments%n", context, command, args.length);
    boolean simple = true;
    int indent = 8;
    for (String arg : args) {
      indent = simple ? 8 : arg.startsWith("-") ? 8 : 10;
      simple = !arg.startsWith("-");
      if (arg.length() > 100) {
        if (arg.contains(File.pathSeparator)) {
          for (String path : arg.split(File.pathSeparator)) {
            System.out.printf("%-10s%s%n", "", path);
          }
          continue;
        }
        arg = arg.substring(0, 96) + "[...]";
      }
      System.out.printf("%-" + indent + "s%s%n", "", arg);
    }
    System.out.println();
  }

  void del(String directory) throws Exception {
    System.out.printf("[del] %s%n", directory);
    Path root = Paths.get(directory);
    if (Files.notExists(root)) {
      return;
    }
    try (Stream<Path> stream = Files.walk(root)) {
      Stream<Path> selected = stream.sorted((p, q) -> -p.compareTo(q));
      for (Path path : selected.toList()) {
        Files.deleteIfExists(path);
      }
    }
  }

  Path get(String directory, URI uri) throws Exception {
    String uriPath = uri.getPath();
    int begin = uriPath.lastIndexOf('/') + 1;
    String file = uriPath.substring(begin).split("\\?")[0].split("#")[0];
    Path folder = Paths.get(directory);
    Path target = folder.resolve(file);
    if (Files.exists(target)) {
      return target;
    }
    System.out.printf("[get] %s%n", target);
    Files.createDirectories(folder);
    var connection = (HttpURLConnection) uri.toURL().openConnection();
    if ("central.sonatype.com".equals(uri.getHost())) {
      var mavenCentralUserToken =
          Objects.requireNonNull(
              System.getenv("MAVEN_CENTRAL_USER_TOKEN"),
              "The MAVEN_CENTRAL_USER_TOKEN environment variable must be set");
      connection.setRequestProperty("Authorization", "Bearer " + mavenCentralUserToken);
    }
    try (InputStream sourceStream = connection.getInputStream();
        OutputStream targetStream = Files.newOutputStream(target)) {
      sourceStream.transferTo(targetStream);
    }
    return target;
  }

  Path get(String directory, String group, String artifact, String version) throws Exception {
    String repo = "https://repo1.maven.org/maven2";
    String file = artifact + "-" + version + ".jar";
    URI uri = URI.create(String.join("/", repo, group.replace('.', '/'), artifact, version, file));
    return get(directory, uri);
  }
}
