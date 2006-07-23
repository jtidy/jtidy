/*
 *  Java HTML Tidy - JTidy
 *  HTML parser and pretty printer
 *
 *  Copyright (c) 1998-2000 World Wide Web Consortium (Massachusetts
 *  Institute of Technology, Institut National de Recherche en
 *  Informatique et en Automatique, Keio University). All Rights
 *  Reserved.
 *
 *  Contributing Author(s):
 *
 *     Dave Raggett <dsr@w3.org>
 *     Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 *     Gary L Peskin <garyp@firstech.com> (Java development)
 *     Sami Lempinen <sami@lempinen.net> (release management)
 *     Fabrizio Giustina <fgiust at users.sourceforge.net>
 *
 *  The contributing author(s) would like to thank all those who
 *  helped with testing, bug fixes, and patience.  This wouldn't
 *  have been possible without all of you.
 *
 *  COPYRIGHT NOTICE:
 * 
 *  This software and documentation is provided "as is," and
 *  the copyright holders and contributing author(s) make no
 *  representations or warranties, express or implied, including
 *  but not limited to, warranties of merchantability or fitness
 *  for any particular purpose or that the use of the software or
 *  documentation will not infringe any third party patents,
 *  copyrights, trademarks or other rights. 
 *
 *  The copyright holders and contributing author(s) will not be
 *  liable for any direct, indirect, special or consequential damages
 *  arising out of any use of the software or documentation, even if
 *  advised of the possibility of such damage.
 *
 *  Permission is hereby granted to use, copy, modify, and distribute
 *  this source code, or portions hereof, documentation and executables,
 *  for any purpose, without fee, subject to the following restrictions:
 *
 *  1. The origin of this source code must not be misrepresented.
 *  2. Altered versions must be plainly marked as such and must
 *     not be misrepresented as being the original source.
 *  3. This Copyright notice may not be removed or altered from any
 *     source or altered source distribution.
 * 
 *  The copyright holders and contributing author(s) specifically
 *  permit, without fee, and encourage the use of this source code
 *  as a component for supporting the Hypertext Markup Language in
 *  commercial products. If you use this source code in a product,
 *  acknowledgment is not required but would be appreciated.
 *
 */
package org.w3c.tidy;

import junit.framework.TestCase;


/**
 * Test cases for TidyUtils.
 * @author Fabrizio Giustina
 * @version $Revision $ ($Author $)
 */
public class TidyUtilsTest extends TestCase
{

    /**
     * instantiates a new test.
     * @param name test name
     */
    public TidyUtilsTest(String name)
    {
        super(name);
    }

    /**
     * Tests isInValuesIgnoreCase with a valid string.
     */
    public void testIsInValuesIgnoreCaseSuccessfull()
    {
        String[] validValues = new String[]{"first", "Second", "THIRD"};
        String stringToCheck = "second";
        assertTrue(TidyUtils.isInValuesIgnoreCase(validValues, stringToCheck));
    }

    /**
     * Tests isInValuesIgnoreCase with an invalid string.
     */
    public void testIsInValuesIgnoreCaseFail()
    {
        String[] validValues = new String[]{"first", "Second", "THIRD"};
        String stringToCheck = "secon";
        assertFalse(TidyUtils.isInValuesIgnoreCase(validValues, stringToCheck));
    }

    /**
     * Test for isCharEncodingSupported().
     */
    public void testIsCharEncodingSupported()
    {
        assertTrue(TidyUtils.isCharEncodingSupported("utf8"));
        assertTrue(TidyUtils.isCharEncodingSupported("UTF-8"));
        assertTrue(TidyUtils.isCharEncodingSupported("US-ASCII"));
        assertTrue(TidyUtils.isCharEncodingSupported("ASCII"));
        assertTrue(TidyUtils.isCharEncodingSupported("LATIN1"));
        assertTrue(TidyUtils.isCharEncodingSupported("ISO-8859-1"));
        assertTrue(TidyUtils.isCharEncodingSupported("WINDOWS-1252"));
        assertTrue(TidyUtils.isCharEncodingSupported("ISO2022"));
        assertTrue(TidyUtils.isCharEncodingSupported("ISO-2022-JP"));
        assertTrue(TidyUtils.isCharEncodingSupported("BIG5"));
        assertTrue(TidyUtils.isCharEncodingSupported("UTF16"));
        assertTrue(TidyUtils.isCharEncodingSupported("UTF16BE"));
        assertTrue(TidyUtils.isCharEncodingSupported("UTF16LE"));
        assertTrue(TidyUtils.isCharEncodingSupported("UTF-16"));
        assertTrue(TidyUtils.isCharEncodingSupported("UTF-16BE"));
        assertTrue(TidyUtils.isCharEncodingSupported("UTF-16LE"));
        assertTrue(TidyUtils.isCharEncodingSupported("CP858"));
        assertTrue(TidyUtils.isCharEncodingSupported("ibm858"));
        assertTrue(TidyUtils.isCharEncodingSupported("Macintosh Roman"));
        assertTrue(TidyUtils.isCharEncodingSupported("WiN1252"));
        assertTrue(TidyUtils.isCharEncodingSupported("SHIFTJIS"));
    }

}