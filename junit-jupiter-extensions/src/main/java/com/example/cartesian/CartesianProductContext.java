/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.cartesian;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

class CartesianProductContext implements TestTemplateInvocationContext {

	private final List<?> parameters;

	CartesianProductContext(List<?> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String getDisplayName(int invocationIndex) {
		return invocationIndex + ": " + parameters;
	}

	@Override
	public List<Extension> getAdditionalExtensions() {
		return Collections.singletonList(new CartesianProductResolver(parameters));
	}

}
