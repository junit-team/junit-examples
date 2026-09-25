/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.tool.internal;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@DisplayName("com.example.tool/com.example.tool.internal.MathHelperTests")
class MathHelperTests {

    @Test
    void pow() {
        Assertions.assertEquals(8, MathHelper.pow(2, 3), "2 ^ 3 should equal 8");
    }

    @ParameterizedTest(name = "{0} ^ {1} = {2}")
    @CsvSource({
                    "0,   0,   1",
                    "1,   0,   1",
                    "2,   2,   4",
                    "2,   3,   8",
                    "2,   8, 256",
                    "2,   9, 512"
    })
    void pow(int first, int second, int expectedResult) {
        Assertions.assertEquals(expectedResult, MathHelper.pow(first, second),
                     () -> first + " ^ " + second + " should equal " + expectedResult);
    }

}
