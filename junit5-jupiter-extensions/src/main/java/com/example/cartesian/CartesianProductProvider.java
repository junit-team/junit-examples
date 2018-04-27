/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.cartesian;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.platform.commons.support.ReflectionSupport;

public class CartesianProductProvider implements TestTemplateInvocationContextProvider {

	@Override
	public boolean supportsTestTemplate(ExtensionContext context) {
		return context.getRequiredTestMethod().isAnnotationPresent(CartesianProductTest.class);
	}

	@Override
	public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
		List<List<?>> sets = computeSets(context.getRequiredTestMethod());
		return cartesianProduct(sets).stream().map(CartesianProductContext::new);
	}

	private List<List<?>> computeSets(Method testMethod) {
		String[] value = testMethod.getAnnotation(CartesianProductTest.class).value();
		// Compute A ⨯ A ⨯ ... ⨯ A from single source "set"
		if (value.length > 0) {
			List<String> strings = Arrays.asList(value);
			List<List<?>> sets = new ArrayList<>();
			for(int i = 0; i < testMethod.getParameterTypes().length; i++) {
				sets.add(strings);
			}
			return sets;
		}
		// No single entry supplied? Try the sets factory method instead...
		return invokeSetsFactory(testMethod).getSets();
	}

	private CartesianProductTest.Sets invokeSetsFactory(Method testMethod) {
		Class<?> declaringClass = testMethod.getDeclaringClass();
		String name = testMethod.getName();
		Method factory = ReflectionSupport.findMethod(declaringClass, name)
				.orElseThrow(() -> new IllegalStateException("Method `CartesianProductTest.Sets " + name + "()` not found in " + declaringClass));
		if (!Modifier.isStatic(factory.getModifiers())) {
			throw new IllegalArgumentException("Method `" + factory + "` must be static");
		}
		if (!CartesianProductTest.Sets.class.isAssignableFrom(factory.getReturnType())) {
			throw new IllegalArgumentException("Method `" + factory + "` must return `CartesianProductTest.Sets`");
		}
		return (CartesianProductTest.Sets) ReflectionSupport.invokeMethod(factory, null);
	}

	private static List<List<?>> cartesianProduct(List<List<?>> lists) {
		List<List<?>> resultLists = new ArrayList<>();
		if (lists.isEmpty()) {
			resultLists.add(Collections.emptyList());
			return resultLists;
		}
		List<?> firstList = lists.get(0);
		List<List<?>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
		for (Object item : firstList) {
			for (List<?> remainingList : remainingLists) {
				ArrayList<Object> resultList = new ArrayList<>();
				resultList.add(item);
				resultList.addAll(remainingList);
				resultLists.add(resultList);
			}
		}
		return resultLists;
	}

}
