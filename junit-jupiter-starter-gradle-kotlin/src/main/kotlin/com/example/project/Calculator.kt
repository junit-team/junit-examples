/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project

class Calculator {

    fun add(a: Int, b: Int): Int {
        return a + b
    }

    fun div(a: Int, b: Int): Double {
        assert(b != 0) { "Division by Zero" }
        return a / b * 1.0
    }

}
