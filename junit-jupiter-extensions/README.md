# junit-jupiter-extensions

A collection of JUnit Jupiter example extensions.

* The `RandomParametersExtension` showcases the `ParameterResolver` extension
  API by providing injection support for random values at the method parameter
  level.

* The `@CartesianProductTest` showcases the `@TestTemplate` extension API by
  providing an example implementation for invoking a method multiple times. It
  uses the `TestTemplateInvocationContextProvider`, `TestTemplateInvocationContext`
  and the `ParameterResolver` extension points to generate the [cartesian product]
  from a single or n-ary source sets.

For real-world usage examples of the JUnit Jupiter Extension APIs, check out the
list of [third-party extensions], e.g. the ones provided by Spring and Mockito.

[third-party extensions]: https://github.com/junit-team/junit-framework/wiki/Third-party-Extensions#junit-jupiter-extensions
[cartesian product]: https://en.wikipedia.org/wiki/Cartesian_product
