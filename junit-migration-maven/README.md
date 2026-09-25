# junit-migration-maven

The `junit-migration-maven` project demonstrates how to execute tests based on JUnit 5
using Maven. In addition, it showcases that existing JUnit 4 based tests can be executed
in the same test suite as JUnit Jupiter based tests or any other tests supported on
the JUnit Platform.

This example project does not aim to demonstrate how to use the JUnit Jupiter APIs.
For detailed information on the JUnit Jupiter programming and extension models,
please consult the [User Guide](https://docs.junit.org).

Please note that this project uses the [Maven Wrapper](https://github.com/takari/maven-wrapper).
Thus, to ensure that the correct version of Maven is used, invoke `mvnw` instead of `mvn`.

## Executing JUnit 4 and JUnit Jupiter Tests

Invoking `mvnw clean test` from the command line will execute all tests in the test source
folder that follow one of following patterns: `Test*`, `*Test`, `*Tests`, or `*TestCase`.
Note that [Surefire's default naming patterns](https://maven.apache.org/surefire/maven-surefire-plugin/examples/inclusion-exclusion.html)
have been overridden in the `pom.xml` file. Surefire's execution of
the example tests should result in output similar to the following:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Jan 07, 2017 11:49:00 PM org.junit.platform.launcher.core.ServiceLoaderTestEngineRegistry loadTestEngines
INFO: Discovered TestEngines with IDs: [junit-jupiter, junit-vintage]
Running com.example.project.FirstTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.038 sec - in com.example.project.FirstTest
Running com.example.project.JUnit4Test
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec - in com.example.project.JUnit4Test
Running com.example.project.OtherTests
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec - in com.example.project.OtherTests
Running com.example.project.SecondTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0.001 sec - in com.example.project.SecondTest

Results :

Tests run: 5, Failures: 0, Errors: 0, Skipped: 1

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

If you comment out the `@Disabled` annotation on `SecondTest#mySecondTest()`, you will
then see the build fail with output similar to the following:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Jan 07, 2017 11:50:07 PM org.junit.platform.launcher.core.ServiceLoaderTestEngineRegistry loadTestEngines
INFO: Discovered TestEngines with IDs: [junit-jupiter, junit-vintage]
Running com.example.project.FirstTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.049 sec - in com.example.project.FirstTest
Running com.example.project.JUnit4Test
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.017 sec - in com.example.project.JUnit4Test
Running com.example.project.OtherTests
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 sec - in com.example.project.OtherTests
Running com.example.project.SecondTest
Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.032 sec <<< FAILURE! - in com.example.project.SecondTest
mySecondTest()  Time elapsed: 0.019 sec  <<< FAILURE!
org.opentest4j.AssertionFailedError: 2 is not equal to 1 ==> expected: <2> but was: <1>
	at com.example.project.SecondTest.mySecondTest(SecondTest.java:24)


Results :

Failed tests:
  SecondTest.mySecondTest:24 2 is not equal to 1 ==> expected: <2> but was: <1>

Tests run: 5, Failures: 1, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
```

### Test Reports

Maven Surefire writes plain text and XML test reports to `target/surefire-reports`.

### Limitations

Advanced Maven Surefire parameters, such as `forkCount` or `parallel`, do not work yet.
