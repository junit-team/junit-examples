/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

import static java.lang.module.ModuleDescriptor.Requires.Modifier.STATIC;

void main() throws Exception {
  var program = Path.of("src", "HelloTests.java");
  if (!Files.exists(program)) {
    throw new AssertionError("Expected %s in current working directory".formatted(program));
  }
  var properties = new Properties();
  properties.load(new FileReader("init/module-uri.properties"));
  var lib = Files.createDirectories(Path.of("lib"));
  downloadModules(lib, properties, Set.of("org.junit.start", "org.apiguardian.api"));
  var missing = computeMissingModuleNames(lib);
  while (!missing.isEmpty()) {
    downloadModules(lib, properties, missing);
    missing = computeMissingModuleNames(lib);
  }
  System.out.printf("%nList modules of %s directory%n", lib);
  listModules(lib);
}

static void downloadModules(Path directory, Properties properties, Set<String> names) throws Exception {
  for (var name : names) {
    var target = directory.resolve(name + ".jar");
    if (Files.exists(target)) continue;
    var source = URI.create(properties.getProperty(name));
    try (var stream = source.toURL().openStream()) {
      System.out.println(name + " < " + source + "...");
      Files.copy(stream, target);
    }
  }
  var finder = ModuleFinder.of(directory);
  var remainder = new TreeSet<>(names);
  remainder.removeIf(name -> finder.find(name).isPresent());
  if (remainder.isEmpty()) return;
  throw new AssertionError("Modules not downloaded: " + remainder);
}

static Set<String> computeMissingModuleNames(Path directory) {
  var system = ModuleFinder.ofSystem();
  var finder = ModuleFinder.of(directory);
  var names =
      finder.findAll().stream()
          .parallel()
          .map(ModuleReference::descriptor)
          .map(ModuleDescriptor::requires)
          .flatMap(Collection::stream)
          .filter(requires -> !requires.modifiers().contains(STATIC))
          .map(ModuleDescriptor.Requires::name)
          .filter(name -> finder.find(name).isEmpty())
          .filter(name -> system.find(name).isEmpty())
          .toList();
  return new TreeSet<>(names);
}

static void listModules(Path directory) {
  var finder = ModuleFinder.of(directory);
  var modules = finder.findAll();
  modules.stream()
      .map(ModuleReference::descriptor)
      .map(ModuleDescriptor::toNameAndVersion)
      .sorted()
      .forEach(System.out::println);
  System.out.printf("    %d modules%n", modules.size());
}
