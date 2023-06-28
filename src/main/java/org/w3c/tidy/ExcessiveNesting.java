/*
 * Copyright (c) 2023 Business Operation Systems GmbH. All Rights Reserved.
 */
package org.w3c.tidy;

/**
 * Exception signaling a document with excessive nesting that is considered a denial-of-service attack.
 */
public class ExcessiveNesting extends Exception {

	/** 
	 * Creates a {@link ExcessiveNesting}.
	 */
	public ExcessiveNesting() {
		super();
	}
}
