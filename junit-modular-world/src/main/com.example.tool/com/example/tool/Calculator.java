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

import com.example.tool.internal.MathHelper;

public class Calculator {

	public int add(int a, int b) {
		return a + b;
	}

	protected int mul(int a, int b) {
		return a * b;
	}

	int pow(int a, int b) {
		return MathHelper.pow(a, b);
	}

}
