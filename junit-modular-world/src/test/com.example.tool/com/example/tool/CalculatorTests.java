/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.tool;

import org.junit.jupiter.api.*;

@DisplayName("com.example.tool/com.example.tool.CalculatorTests")
class CalculatorTests {

	private final Calculator calculator = new Calculator();

	@Test
	void add() {
		Assertions.assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
	}

	@Test
	void mul() {
		Assertions.assertEquals(4, calculator.mul(2, 2), "2 * 2 should equal 4");
	}

	@Test
	void pow() {
		Assertions.assertEquals(8, calculator.pow(2, 3), "2 ^ 3 should equal 8");
	}

}
