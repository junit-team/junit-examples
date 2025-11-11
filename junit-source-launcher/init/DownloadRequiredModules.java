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

void main() throws Exception {
  // Ensure being launched inside expected working directory
  var program = Path.of("src", "HelloTests.java");
  if (!Files.exists(program)) {
    throw new AssertionError("Expected %s in current working directory".formatted(program));
  }

  // Read mapping file to locate remote modules
  var properties = new Properties();
  properties.load(new FileReader("init/module-uri.properties"));

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
