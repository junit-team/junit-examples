/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

final Path lib = Path.of("lib");
final Set<String> roots = Set.of("org.junit.start");
final String lookup =
        //language=Properties
        """
        org.apiguardian.api=https://repo.maven.apache.org/maven2/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar
        org.jspecify=https://repo.maven.apache.org/maven2/org/jspecify/jspecify/1.0.0/jspecify-1.0.0.jar
        org.junit.jupiter.api=https://repo.maven.apache.org/maven2/org/junit/jupiter/junit-jupiter-api/6.1.0-M1/junit-jupiter-api-6.1.0-M1.jar
        org.junit.jupiter.engine=https://repo.maven.apache.org/maven2/org/junit/jupiter/junit-jupiter-engine/6.1.0-M1/junit-jupiter-engine-6.1.0-M1.jar
        org.junit.jupiter.params=https://repo.maven.apache.org/maven2/org/junit/jupiter/junit-jupiter-params/6.1.0-M1/junit-jupiter-params-6.1.0-M1.jar
        org.junit.jupiter=https://repo.maven.apache.org/maven2/org/junit/jupiter/junit-jupiter/6.1.0-M1/junit-jupiter-6.1.0-M1.jar
        org.junit.platform.commons=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-commons/6.1.0-M1/junit-platform-commons-6.1.0-M1.jar
        org.junit.platform.console=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console/6.1.0-M1/junit-platform-console-6.1.0-M1.jar
        org.junit.platform.engine=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-engine/6.1.0-M1/junit-platform-engine-6.1.0-M1.jar
        org.junit.platform.launcher=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-launcher/6.1.0-M1/junit-platform-launcher-6.1.0-M1.jar
        org.junit.platform.reporting=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-reporting/6.1.0-M1/junit-platform-reporting-6.1.0-M1.jar
        org.junit.platform.suite.api=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-suite-api/6.1.0-M1/junit-platform-suite-api-6.1.0-M1.jar
        org.junit.platform.suite.engine=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-suite-engine/6.1.0-M1/junit-platform-suite-engine-6.1.0-M1.jar
        org.junit.platform.suite=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-suite/6.1.0-M1/junit-platform-suite-6.1.0-M1.jar
        org.junit.start=https://repo.maven.apache.org/maven2/org/junit/junit-start/6.1.0-M1/junit-start-6.1.0-M1.jar
        org.opentest4j.reporting.tooling.spi=https://repo.maven.apache.org/maven2/org/opentest4j/reporting/open-test-reporting-tooling-spi/0.2.5/open-test-reporting-tooling-spi-0.2.5.jar
        org.opentest4j=https://repo.maven.apache.org/maven2/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar
        """;

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
