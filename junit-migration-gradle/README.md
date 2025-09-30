# junit-migration-gradle

The `junit-migration-gradle` project demonstrates how to execute tests based on JUnit 5
using Gradle. In addition, it showcases that existing JUnit 4 based tests can be executed
in the same test suite as JUnit Jupiter based tests or any other tests supported on
the JUnit Platform.

This example project does not aim to demonstrate how to use the JUnit Jupiter APIs.
For detailed information on the JUnit Jupiter programming and extension models,
please consult the [User Guide](https://docs.junit.org/current/user-guide/).

## Enabling the JUnit Platform

To use the JUnit Platform with Gradle, you need to configure `build.gradle` as follows.

```groovy
test {
	useJUnitPlatform()
}
```

## Configuring the Test task

Optionally, you can configure the `test` task as follows.

```groovy
test {
	useJUnitPlatform {
		// includeEngines("junit-jupiter", "junit-vintage")
		// excludeEngines("custom-engine")

		// includeTags("fast")
		excludeTags("slow")
	}
	testLogging {
		events("passed", "skipped", "failed")
	}
}
```

By default all engines and tags are included in the test plan.

If you supply a _Test Engine ID_ via `includeEngines(...)` or `excludeEngines(...)`,
Gradle will only run tests for the desired test engines.

If you supply a _tag_ via `includeTags(...)`, Gradle will only
run tests that are _tagged_ accordingly (e.g., via the `@Tag` annotation for
JUnit Jupiter based tests). Similarly, if you supply a _tag_ via `excludeTags(...)`,
Gradle will not run tests that are _tagged_ accordingly.

## Configuring Test Engines

In order to have Gradle's `test` task run any tests at all, a `TestEngine`
implementation must be on the classpath.

To configure support for JUnit Jupiter based tests, configure a `testImplementation`
dependency on the `junit-jupiter` artifact. That will cause `testImplementation`
dependency on the JUnit Jupiter API and a `testRuntimeOnly` dependency on the JUnit
Jupiter TestEngine.

```groovy
dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
}
```

Gradle can also run JUnit 3 and JUnit 4 based tests as long as you
configure a `testImplementation` dependency on JUnit 4 and a `testRuntimeOnly` dependency
on the JUnit Vintage TestEngine implementation similar to the following.

```groovy
dependencies {
	testImplementation("junit:junit:4.13.2")
	testRuntimeOnly("org.junit.vintage:junit-vintage-engine:6.0.0")
}
```

## Executing Tests on the JUnit Platform

Once the JUnit Platform Gradle plugin has been applied and configured, you can use the
standard `test` task as usual.

Invoking `gradlew clean test` from the command line will execute all tests within the
project. This will result in output similar to the following:

```
> Task :test

com.example.project.SecondTest > mySecondTest() SKIPPED

com.example.project.OtherTests > testThisThing() PASSED

com.example.project.OtherTests > testThisOtherThing() PASSED

com.example.project.FirstTest > myFirstTest(TestInfo) PASSED

com.example.project.JUnit4Test > test PASSED

BUILD SUCCESSFUL in 2s
4 actionable tasks: 3 executed, 1 up-to-date
```

If you comment out the `@Disabled` annotation on `SecondTest#mySecondTest()`, you will
then see the build fail with output similar to the following:

```
> Task :test FAILED

com.example.project.OtherTests > testThisThing() PASSED

com.example.project.OtherTests > testThisOtherThing() PASSED

com.example.project.FirstTest > myFirstTest(TestInfo) PASSED

com.example.project.SecondTest > mySecondTest() FAILED
    org.opentest4j.AssertionFailedError at SecondTest.java:24

com.example.project.JUnit4Test > test PASSED

5 tests completed, 1 failed

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':test'.
> There were failing tests. See the report at: file:///Users/junit-team/junit-examples/junit-migration-gradle/build/reports/tests/test/index.html

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 2s
4 actionable tasks: 3 executed, 1 up-to-date
```

### Test Reports

Gradle writes XML test reports to `build/test-results/test` and HTML test reports to `build/reports/tests/test`.
