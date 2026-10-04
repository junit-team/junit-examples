/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.process.CommandLineArgumentProvider

abstract class ReportsDirArgumentProvider : CommandLineArgumentProvider {

    @get:OutputDirectory
    abstract val reportsDir: DirectoryProperty

    override fun asArguments() = listOf("--reports-dir", reportsDir.asFile.get().absolutePath)
}
