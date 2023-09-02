/*
 * Copyright (c) 2023 Business Operation Systems GmbH. All Rights Reserved.
 */
package org.w3c.tidy;

import java.io.StringReader;

import junit.framework.TestCase;

/**
 * Test case for CVE-2023-34623.
 * 
 * @see "https://nvd.nist.gov/vuln/detail/CVE-2023-34623"
 */
public class TestCVE_2023_34623 extends TestCase {

    private static final int NESTING_LEVEL = 9999;

    /**
     * Checks that excessive nesting does not result in a crash.
     */
	public void testDeepNesting() {
        String htmlData = deeplyNestedDoc();
        Tidy tidy = new Tidy();
        StringReader stringReader = new StringReader(htmlData);
        try {
        	tidy.parse(stringReader, System.out);
        } finally
        {
            stringReader.close();
        }
        assertEquals(1, tidy.getParseErrors());
    }

	protected String deeplyNestedDoc() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 9999; ++i) {
		    result.append("<div>");
		    if ((i & 31) == 0) {
		        result.append("\n");
		    }
		}
		result.append("\n").append("").append("\n");
		for (int i = 0; i < NESTING_LEVEL; ++i) {
		    result.append("</div>");
		    if ((i & 31) == 0) {
		        result.append("\n");
		    }
		}
		return result.toString();
	}	
}
