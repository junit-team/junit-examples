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
final String version = "6.1.1"; // of JUnit Framework
final Set<String> roots = Set.of("org.junit.start@" + version); // single root module to lookup
final String pins = // module versions to amend missing versions or override compiled versions
    // language=Properties
    """
    # https://github.com/jspecify/jspecify/issues/746
    org.jspecify=1.0.0
    """;

void main() throws Exception {
  // Ensure being launched inside expected working directory
  var program = Path.of("src", "HelloTests.java");
  if (!Files.exists(program)) {
    throw new AssertionError("Expected %s in current working directory".formatted(program));
  }

  // Read mapping file to locate remote modules
  var properties = new Properties();
  properties.load(new StringReader(pins));

  // Create and initialize lib directory with root module(s)
  Files.createDirectories(lib);
  downloadModules(roots, properties);

  // Compute missing modules and download them transitively
  var missing = computeMissingModules();
  while (!missing.isEmpty()) {
    downloadModules(missing, properties);
    missing = computeMissingModules();
  }

  IO.println("%nList modules of %s directory".formatted(lib));
  listModules();
}

void downloadModules(Set<String> entries, Properties properties) {
  IO.println("Downloading %d module%s".formatted(entries.size(), entries.size() == 1 ? "" : "s"));
  var downloaded = new TreeSet<String>();
  entries.stream()
      .parallel()
      .forEach(
          entry -> {
            var target = lib.resolve(entry + ".jar");
            if (Files.exists(target)) return; // Don't overwrite existing JAR file
            var at = entry.indexOf('@');
            var name = at <= 0 ? entry : entry.substring(0, at);
            var version = properties.getProperty(name, at <= 0 ? "?" : entry.substring(at + 1));
            if (version == null) {
              throw new IllegalStateException("Module entry without a version hint: " + entry);
            }
            var source = computeJenesisModuleURI(name, version);
            try (var stream = source.toURL().openStream()) {
              IO.println(name + " <- " + source + "...");
              Files.copy(stream, target);
              downloaded.add(name);
            } catch (IOException cause) {
              throw new UncheckedIOException(cause);
            }
          });
  // Ensure that every name can be found to avoid eternal loops
  var finder = ModuleFinder.of(lib);
  downloaded.removeIf(name -> finder.find(name).isPresent());
  if (downloaded.isEmpty()) return;
  throw new AssertionError("Modules not downloaded: " + downloaded);
}

URI computeJenesisModuleURI(String name, String version) {
  var joiner = new StringJoiner("/", "https://repo.jenesis.build/module/", ".jar");
  return URI.create(joiner.add(name).add(version).add(name).toString());
}

Set<String> computeMissingModules() {
  var system = ModuleFinder.ofSystem();
  var finder = ModuleFinder.of(lib);
  var entries =
      finder.findAll().stream()
          .parallel()
          .map(ModuleReference::descriptor)
          .map(ModuleDescriptor::requires)
          .flatMap(Collection::stream)
          .filter(this::mustBePresentAtCompileTime)
          .filter(requires -> finder.find(requires.name()).isEmpty())
          .filter(requires -> system.find(requires.name()).isEmpty())
          .map(this::computeNameAndVersion)
          .toList();
  return new TreeSet<>(entries);
}

boolean mustBePresentAtCompileTime(ModuleDescriptor.Requires requires) {
  var isStatic = requires.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC);
  var isTransitive = requires.modifiers().contains(ModuleDescriptor.Requires.Modifier.TRANSITIVE);
  return !isStatic || isTransitive;
}

String computeNameAndVersion(ModuleDescriptor.Requires requires) {
  return requires.name() + requires.compiledVersion().map(v -> "@" + v).orElse("");
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
