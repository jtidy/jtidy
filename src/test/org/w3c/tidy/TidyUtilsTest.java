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
        assertTrue(TidyUtils.isCharEncodingSupported("utf-8"));
        assertTrue(TidyUtils.isCharEncodingSupported("ISO2022JP"));
        assertTrue(TidyUtils.isCharEncodingSupported("ASCII"));
        assertFalse(TidyUtils.isCharEncodingSupported("xyz"));
    }

    /**
     * Test for toJavaEncodingName().
     */
    public void testToJavaEncodingName()
    {
        assertEquals("UTF8", TidyUtils.toJavaEncodingName("utf8"));
        assertEquals("UTF8", TidyUtils.toJavaEncodingName("UTF-8"));
        assertEquals("ASCII", TidyUtils.toJavaEncodingName("US-ASCII"));
        assertEquals("ASCII", TidyUtils.toJavaEncodingName("ASCII"));
        assertEquals("ISO8859_1", TidyUtils.toJavaEncodingName("LATIN1"));
        assertEquals("ISO8859_1", TidyUtils.toJavaEncodingName("ISO-8859-1"));
        assertEquals("CP1252", TidyUtils.toJavaEncodingName("WiN1252"));
        assertEquals("CP1252", TidyUtils.toJavaEncodingName("WINDOWS-1252"));
        assertEquals("SJIS", TidyUtils.toJavaEncodingName("SHIFTJIS"));
        assertEquals("ISO2022JP", TidyUtils.toJavaEncodingName("ISO2022"));
        assertEquals("ISO2022JP", TidyUtils.toJavaEncodingName("ISO-2022-JP"));
        assertEquals("BIG5", TidyUtils.toJavaEncodingName("BIG5"));
        assertEquals("UTF-16", TidyUtils.toJavaEncodingName("UTF16"));
        assertEquals("UNICODEBIGUNMARKED", TidyUtils.toJavaEncodingName("UTF16BE"));
        assertEquals("UNICODELITTLEUNMARKED", TidyUtils.toJavaEncodingName("UTF16LE"));
        // assertEquals("MACROMAN", TidyUtils.toJavaEncodingName("Macintosh Roman"));
    }

}