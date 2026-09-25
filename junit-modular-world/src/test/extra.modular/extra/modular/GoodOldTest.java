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
import org.junit.*;

public class GoodOldTest {

	@Test
	public void eighteenEqualsNineAndNine() {
		Assert.assertEquals(18, new Calculator().add(9, 9));
	}
}
