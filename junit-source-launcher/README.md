# junit-source-launcher

Starting with Java 25 it is possible to write minimal source code test programs using the `org.junit.start` module.
For example, take a look at the [HelloTests.java](src/example/HelloTests.java) file reading:

```java
package example;

import module org.junit.start;

class HelloTests {
    void main() {
        JUnit.run();
    }

    @Test
    void stringLength() {
        Assertions.assertEquals(11, "Hello JUnit".length());
    }
}
```

Download `org.junit.start` module and its transitively required modules into a local `lib/` directory by running in a shell:

```shell
java lib/DownloadRequiredModules.java
```

With all required modular JAR files available in a local `lib/` directory, the following Java command will discover and execute tests using the JUnit Platform.

```shell
java --module-path lib src/example/HelloTests.java
```

It will also print the result tree to the console.

```text
╷
└─ JUnit Jupiter ✔
   └─ HelloTests ✔
      └─ stringLength() ✔
```
