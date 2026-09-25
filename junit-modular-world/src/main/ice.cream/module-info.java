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

module ice.cream {
	requires org.junit.platform.engine;

	provides TestEngine with ice.cream.Machine;
}
