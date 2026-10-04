/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package ice.cream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MachineTests {

	@Test
	void caption() {
		String actual = new Machine().getCaption();
		assertTrue(actual.startsWith("Ice Cream Machine"));
	}

	@Test
	void id() {
		String expected = "ice-cream-machine";
		String actual = new Machine().getId();
		assertEquals(expected, actual, "Machine ID mismatch!");
	}

}
