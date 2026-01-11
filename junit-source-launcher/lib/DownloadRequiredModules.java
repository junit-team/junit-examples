/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

final Path lib = Path.of("lib"); // local directory to be used in module path
final Set<String> roots = Set.of("org.junit.start"); // single root module to lookup
final String version = "6.1.0-M1"; // of JUnit Framework
final String repository = "https://repo.maven.apache.org/maven2"; // of JUnit Framework
final String lookup =
        //language=Properties
        """
        org.apiguardian.api=https://repo.maven.apache.org/maven2/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar
        org.jspecify=https://repo.maven.apache.org/maven2/org/jspecify/jspecify/1.0.0/jspecify-1.0.0.jar
        org.junit.jupiter.api={{repository}}/org/junit/jupiter/junit-jupiter-api/{{version}}/junit-jupiter-api-{{version}}.jar
        org.junit.jupiter.engine={{repository}}/org/junit/jupiter/junit-jupiter-engine/{{version}}/junit-jupiter-engine-{{version}}.jar
        org.junit.jupiter.params={{repository}}/org/junit/jupiter/junit-jupiter-params/{{version}}/junit-jupiter-params-{{version}}.jar
        org.junit.jupiter={{repository}}/org/junit/jupiter/junit-jupiter/{{version}}/junit-jupiter-{{version}}.jar
        org.junit.platform.commons={{repository}}/org/junit/platform/junit-platform-commons/{{version}}/junit-platform-commons-{{version}}.jar
        org.junit.platform.console={{repository}}/org/junit/platform/junit-platform-console/{{version}}/junit-platform-console-{{version}}.jar
        org.junit.platform.engine={{repository}}/org/junit/platform/junit-platform-engine/{{version}}/junit-platform-engine-{{version}}.jar
        org.junit.platform.launcher={{repository}}/org/junit/platform/junit-platform-launcher/{{version}}/junit-platform-launcher-{{version}}.jar
        org.junit.platform.reporting={{repository}}/org/junit/platform/junit-platform-reporting/{{version}}/junit-platform-reporting-{{version}}.jar
        org.junit.platform.suite.api={{repository}}/org/junit/platform/junit-platform-suite-api/{{version}}/junit-platform-suite-api-{{version}}.jar
        org.junit.platform.suite.engine={{repository}}/org/junit/platform/junit-platform-suite-engine/{{version}}/junit-platform-suite-engine-{{version}}.jar
        org.junit.platform.suite={{repository}}/org/junit/platform/junit-platform-suite/{{version}}/junit-platform-suite-{{version}}.jar
        org.junit.start={{repository}}/org/junit/junit-start/{{version}}/junit-start-{{version}}.jar
        org.opentest4j.reporting.tooling.spi=https://repo.maven.apache.org/maven2/org/opentest4j/reporting/open-test-reporting-tooling-spi/0.2.5/open-test-reporting-tooling-spi-0.2.5.jar
        org.opentest4j=https://repo.maven.apache.org/maven2/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar
        """
           .replace("{{repository}}", repository)
           .replace("{{version}}", version);

void main() throws Exception {
  // Ensure being launched inside expected working directory
  var program = Path.of("src", "HelloTests.java");
  if (!Files.exists(program)) {
    throw new AssertionError("Expected %s in current working directory".formatted(program));
  }

  // Read mapping file to locate remote modules
  var properties = new Properties();
  properties.load(new StringReader(lookup));

  // Create and initialize lib directory with root module(s)
  Files.createDirectories(lib);
  downloadModules(roots, properties);

  // Compute missing modules and download them transitively
  var missing = computeMissingModuleNames();
  while (!missing.isEmpty()) {
    downloadModules(missing, properties);
    missing = computeMissingModuleNames();
  }

  IO.println("%nList modules of %s directory".formatted(lib));
  listModules();
}

void downloadModules(Set<String> names, Properties properties) {
  IO.println("Downloading %d module%s".formatted(names.size(), names.size() == 1 ? "" : "s"));
  names.stream().parallel().forEach(name -> {
      var target = lib.resolve(name + ".jar");
      if (Files.exists(target)) return; // Don't overwrite existing JAR file
      var source = URI.create(properties.getProperty(name));
      try (var stream = source.toURL().openStream()) {
          IO.println(name + " <- " + source + "...");
          Files.copy(stream, target);
      } catch (IOException cause) {
          throw new UncheckedIOException(cause);
      }
  });
  // Ensure that every name can be found to avoid eternal loops
  var finder = ModuleFinder.of(lib);
  var remainder = new TreeSet<>(names);
  remainder.removeIf(name -> finder.find(name).isPresent());
  if (remainder.isEmpty()) return;
  throw new AssertionError("Modules not downloaded: " + remainder);
}

Set<String> computeMissingModuleNames() {
  var system = ModuleFinder.ofSystem();
  var finder = ModuleFinder.of(lib);
  var names =
      finder.findAll().stream()
          .parallel()
          .map(ModuleReference::descriptor)
          .map(ModuleDescriptor::requires)
          .flatMap(Collection::stream)
          .filter(this::mustBePresentAtCompileTime)
          .map(ModuleDescriptor.Requires::name)
          .filter(name -> finder.find(name).isEmpty())
          .filter(name -> system.find(name).isEmpty())
          .toList();
  return new TreeSet<>(names);
}

boolean mustBePresentAtCompileTime(ModuleDescriptor.Requires requires) {
  var isStatic = requires.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC);
  var isTransitive = requires.modifiers().contains(ModuleDescriptor.Requires.Modifier.TRANSITIVE);
  return !isStatic || isTransitive;
}

void listModules() {
  var finder = ModuleFinder.of(lib);
  var modules = finder.findAll();
  modules.stream()
      .map(ModuleReference::descriptor)
      .map(ModuleDescriptor::toNameAndVersion)
      .sorted()
      .forEach(IO::println);
  IO.println("    %d modules".formatted(modules.size()));
}
