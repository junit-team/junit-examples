/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package ice.cream;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

/**
 * Simple test engine implementation.
 */
public class Machine implements TestEngine {

	@Override
	public String getId() {
		return "ice-cream-machine";
	}

	/**
	 * Build caption used as the engine's display name.
	 */
	String getCaption() {
		StringBuilder builder = new StringBuilder();
		builder.append("Ice Cream Machine");
		if (getVersion().isPresent()) {
			builder.append(" ").append(getVersion().get());
		}
		if (getArtifactId().isPresent()) {
			builder.append(" (").append(getArtifactId().get()).append(")");
		}
		return builder.toString();
	}

	/**
	 * Extract amount of scoops to generate.
	 */
	int getScoops(EngineDiscoveryRequest discoveryRequest, int defaultScoops) {
		ConfigurationParameters parameters = discoveryRequest.getConfigurationParameters();
		String scoops = parameters.get("scoops").orElse(Integer.toString(defaultScoops));
		return Integer.valueOf(scoops);
	}

	@Override
	public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
		TestDescriptor engine = new EngineDescriptor(uniqueId, getCaption());
		for (int i = 0; i < getScoops(discoveryRequest, 5); i++) {
			engine.addChild(new Scoop(engine.getUniqueId(), i, Flavor.random()));
		}
		return engine;
	}

	@Override
	public void execute(ExecutionRequest request) {
		TestDescriptor engine = request.getRootTestDescriptor();
		EngineExecutionListener listener = request.getEngineExecutionListener();
		listener.executionStarted(engine);
		for (TestDescriptor child : engine.getChildren()) {
			listener.executionStarted(child);
			listener.executionFinished(child, TestExecutionResult.successful());
		}
		listener.executionFinished(engine, TestExecutionResult.successful());
	}
}
