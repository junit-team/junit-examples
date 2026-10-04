/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

open module extra.modular {
	//
	// modules under test
	//
	requires com.example.application;
	requires com.example.tool;

	//
	// test framework api
	//
	requires org.junit.jupiter.api;
	requires junit; // JUnit 4 "automatic module"
	requires net.jqwik.api;
}
