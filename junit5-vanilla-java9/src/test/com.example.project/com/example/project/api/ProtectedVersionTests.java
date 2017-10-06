/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProtectedVersionTests {
  @Test
  void versionEquals4711() {
    assertEquals("47.11", ProtectedVersion.VERSION);
    // assertEquals("Project", Project.class.getSimpleName());
  }
}
