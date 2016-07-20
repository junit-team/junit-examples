# junit5-maven-consumer

The `junit5-maven-consumer` project demonstrates how to execute tests based on JUnit 5
snapshots using Maven. In addition, it showcases that existing JUnit 4 based tests can be
executed in the same run.

## Executing JUnit 4 and JUnit 5 Tests

Invoking `mvn clean test` from the command line will execute all tests in the test source
folder that follow one of following patterns: `Test*`, `*Test`, `*Tests` or `*TestCase`.
Note that [Surefire's default naming patterns](http://maven.apache.org/surefire/maven-surefire-plugin/examples/inclusion-exclusion.html) have been overridden in the pom.xml file.  Surefire's execution of
the sample tests should result in output similar to the following:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.example.project.FirstTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.073 sec - in com.example.project.FirstTest
Running com.example.project.OtherTests
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec - in com.example.project.OtherTests
Running com.example.project.JUnit4Test
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 sec - in com.example.project.JUnit4Test
Running com.example.project.SecondTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0.004 sec - in com.example.project.SecondTest

Results :

Tests run: 3, Failures: 0, Errors: 0, Skipped: 1

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
Running com.example.project.FirstTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.059 sec - in com.example.project.FirstTest
Running com.example.project.JUnit4Test
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 sec - in com.example.project.JUnit4Test
Running com.example.project.SecondTest
Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.012 sec <<< FAILURE! - in com.example.project.SecondTest
mySecondTest()  Time elapsed: 0.01 sec  <<< FAILURE!
org.opentest4j.AssertionFailedError: 2 is not equal to 1 ==> expected: <2> but was: <1>
	at com.example.project.SecondTest.mySecondTest(SecondTest.java:14)


Results :

Failed tests:
  SecondTest.mySecondTest:14 2 is not equal to 1 ==> expected: <2> but was: <1>

Tests run: 3, Failures: 1, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
```

### Test Reports

Maven Surefire writes plain text and XML test reports to `target/surefire-reports`.

### Limitations

Advanced Maven Surefire parameters, such as `forkCount` or `parallel`, do not work yet.
