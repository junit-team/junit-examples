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

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

class Scoop extends AbstractTestDescriptor {

	Scoop(UniqueId uniqueId, int counter, Flavor flavor) {
		super(uniqueId.append("scoop", "#" + counter), flavor.name());
	}

	@Override
	public Type getType() {
		return Type.TEST;
	}
}
