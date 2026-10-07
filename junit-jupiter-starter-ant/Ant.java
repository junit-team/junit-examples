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
  String version = "1.10.18";
  String title = "apache-ant-" + System.getProperty("version", version);
  String archive = title + "-bin.zip";
  Path into = Path.of(title);
  Path antHome = into.resolve(title);
  // Get Ant
  if (!Files.isDirectory(antHome)) {
    Path target = into.resolve(archive);
    if (!Files.exists(target)) {
      String source = "https://dlcdn.apache.org/ant/binaries/" + archive;
      Path parent = target.getParent();
      if (!Files.isDirectory(parent)) Files.createDirectories(parent);
      try (InputStream stream = URI.create(source).toURL().openStream()) {
        IO.println(target.getFileName() + " <- " + source + "...");
        Files.copy(stream, target);
      }
    }
    ToolProvider jar = ToolProvider.findFirst("jar").orElseThrow(() -> new Error("jar not found"));
    int code = jar.run(System.out, System.err, "--extract", "--file", target.toString(), "--dir", title);
    if (code != 0) throw new Error("Extracting archive failed with error code: " + code);
  }
  // Run Ant
  Path javaLauncher = Path.of(System.getProperty("java.home", "."), "bin", "java");
  ProcessBuilder processBuilder = new ProcessBuilder(javaLauncher.toString());
  processBuilder.command().add("-jar");
  processBuilder.command().add(antHome.resolve("lib", "ant-launcher.jar").toString());
  Arrays.stream(args).forEach(processBuilder.command()::add);
  processBuilder.inheritIO();

  Process process = processBuilder.start();
  System.exit(process.waitFor());
}
