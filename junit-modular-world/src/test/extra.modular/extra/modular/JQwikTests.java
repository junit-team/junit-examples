/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package black.box;

import com.example.tool.*;
import net.jqwik.api.*;

class JQwikTests {

	@Example
	boolean exampleFor1And3Equals4() {
		return new Calculator().add(1, 3) == 4;
	}

	@Property
	boolean propertyAdd(@ForAll int a, @ForAll int b) {
		return new Calculator().add(a, b) == a + b;
	}

}
