/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

import org.junit.platform.engine.TestEngine;

open module ice.cream {
	//
	// copied from "main"
	//
	requires org.junit.platform.engine;

	provides TestEngine with ice.cream.Machine;

	//
	// test dependencies
	//
	requires org.junit.jupiter.api;

	// "open module" or "opens ice.cream to org.junit.platform.commons;"
}
