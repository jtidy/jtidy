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
 * Test cases for EncodingNameMapper.
 * @author Fabrizio Giustina
 * @version $Revision $ ($Author $)
 */
public class EncodingNameMapperTest extends TestCase
{

    /**
     * instantiates a new test.
     * @param name test name
     */
    public EncodingNameMapperTest(String name)
    {
        super(name);
    }

    /**
     * Test for toJava().
     */
    public void testToJava()
    {
        assertEquals("UTF8", EncodingNameMapper.toJava("utf8"));
        assertEquals("UTF8", EncodingNameMapper.toJava("UTF-8"));
        assertEquals("ASCII", EncodingNameMapper.toJava("US-ASCII"));
        assertEquals("ASCII", EncodingNameMapper.toJava("ASCII"));
        assertEquals("ISO8859_1", EncodingNameMapper.toJava("LATIN1"));
        assertEquals("ISO8859_1", EncodingNameMapper.toJava("ISO-8859-1"));
        assertEquals("Cp1252", EncodingNameMapper.toJava("WINDOWS-1252"));
        assertEquals("JIS", EncodingNameMapper.toJava("ISO2022"));
        assertEquals("JIS", EncodingNameMapper.toJava("ISO-2022-JP"));
        assertEquals("Big5", EncodingNameMapper.toJava("BIG5"));
        assertEquals("Unicode", EncodingNameMapper.toJava("UTF16"));
        assertEquals("UnicodeBig", EncodingNameMapper.toJava("UTF16BE"));
        assertEquals("UnicodeLittle", EncodingNameMapper.toJava("UTF16LE"));
        assertEquals("Unicode", EncodingNameMapper.toJava("UTF-16"));
        assertEquals("UnicodeBig", EncodingNameMapper.toJava("UTF-16BE"));
        assertEquals("UnicodeLittle", EncodingNameMapper.toJava("UTF-16LE"));
        assertEquals("Cp858", EncodingNameMapper.toJava("CP858"));
        assertEquals("Cp858", EncodingNameMapper.toJava("ibm858"));
        assertEquals("MacRoman", EncodingNameMapper.toJava("Macintosh Roman"));
        assertEquals("Cp1252", EncodingNameMapper.toJava("WiN1252"));
        assertEquals("SJIS", EncodingNameMapper.toJava("SHIFTJIS"));
        assertEquals("MS932", EncodingNameMapper.toJava("WINDOWS-31J"));
        assertEquals(null, EncodingNameMapper.toJava("IBM-"));
    }

}