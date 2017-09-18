/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package com.example.project;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorTests {

	@Test
	void add() {
		Calculator calculator = new Calculator();
		Assertions.assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
	}

}
