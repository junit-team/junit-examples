/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CalculatorTests {

  @Test
  @DisplayName("1 + 1 = 2")
  def addsTwoNumbers(): Unit =
    assertEquals(2, Calculator.add(1, 1), "1 + 1 should equal 2")

  @ParameterizedTest(name = "{0} + {1} = {2}", quoteTextArguments = false)
  @CsvSource(textBlock =
    """0,    1,   1
       1,    2,   3
       49,  51, 100
       1,  100, 101""")
  def add(first: Int, second: Int, expectedResult: Int): Unit =
    assertEquals(expectedResult, Calculator.add(first, second), () => s"$first + $second should equal $expectedResult")

}
