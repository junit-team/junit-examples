/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package kotest

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class KotestSpec : StringSpec({
  "1 + 2 should be 3" {
    1 + 2 shouldBe 3
  }
})
