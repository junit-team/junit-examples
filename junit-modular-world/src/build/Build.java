/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

// default package

public class Build {
  public static void main(String[] args) throws Exception {
    var project = Project.ofCurrentWorkingDirectory();
    project.clean();
    project.compile();
    project.test();
  }
}
