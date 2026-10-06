/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

/**
 * Apache Ant is a Java library and command-line tool.
 *
 * @see <a href="https://ant.apache.org">https://ant.apache.org</a>
 */
void main(String... args) throws Exception {
  var version = System.getProperty("version", "1.10.18");
  var title = "apache-ant-" + version;
  var archive = title + "-bin.zip";
  var into = Path.of(title);
  var antHome = into.resolve(title);
  // Get Ant
  if (!Files.isDirectory(antHome)) {
    var target = into.resolve(archive);
    if (!Files.exists(target)) {
      var source = "https://dlcdn.apache.org/ant/binaries/" + archive;
      var parent = target.getParent();
      if (!Files.isDirectory(parent)) Files.createDirectories(parent);
      try (var stream = URI.create(source).toURL().openStream()) {
        IO.println(target.getFileName() + " <- " + source + "...");
        Files.copy(stream, target);
      }
    }
    var jar = ToolProvider.findFirst("jar").orElseThrow();
    jar.run(System.out, System.err, "--extract", "--file", target.toString(), "--dir", title);
  }
  // Run Ant
  ProcessBuilder processBuilder = new ProcessBuilder("java");
  processBuilder.command().add("--class-path");
  processBuilder.command().add(antHome.resolve("lib/ant-launcher.jar").toString());
  processBuilder.command().add("org.apache.tools.ant.launch.Launcher");
  Arrays.stream(args).forEach(processBuilder.command()::add);
  processBuilder.redirectErrorStream(true);
  Process process = processBuilder.start();
  process.getInputStream().transferTo(System.out);
  int result = process.waitFor();
  if (result == 0) return;
  throw new Error("Ant failed with error code: " + result);
}

