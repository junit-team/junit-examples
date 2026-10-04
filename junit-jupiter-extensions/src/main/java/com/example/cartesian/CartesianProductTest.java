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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@TestTemplate
@ExtendWith(CartesianProductProvider.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CartesianProductTest {

	String[] value() default {};

	class Sets {

		private final List<List<?>> sets = new ArrayList<>();

		public Sets add(Object... entries) {
			sets.add(new ArrayList<>(Arrays.asList(entries)));
			return this;
		}

		List<List<?>> getSets() {
			return sets;
		}
	}

}
