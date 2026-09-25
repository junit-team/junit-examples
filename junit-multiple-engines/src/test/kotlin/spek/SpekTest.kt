/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package spek

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SpekTest : Spek({
    describe("a simple test") {
        it("asserts that 1 + 2 equals 3") {
            assert(1 + 2 == 3)
        }
    }
})
