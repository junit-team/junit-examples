/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.application;

import com.example.tool.Calculator;

class Main {

	public static void main(String... args) {
		int a = args.length > 0 ? (Integer.valueOf(args[0])) : 1;
		int b = args.length > 1 ? (Integer.valueOf(args[1])) : 2;
		System.out.printf("%d + %d = %d%n", a, b, new Calculator().add(a, b));
	}

}
