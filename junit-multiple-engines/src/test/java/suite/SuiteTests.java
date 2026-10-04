/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package suite;

import org.junit.platform.suite.api.ExcludeEngines;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"spock", "jqwik", "junit.jupiter", "junit.vintage", "mainrunner", "testng", "kotest", "spek"})
@IncludeClassNamePatterns(".*((Tests?)|(Spec))$")
@ExcludeEngines("spek") // https://github.com/spekframework/spek/issues/986
public class SuiteTests {
}
