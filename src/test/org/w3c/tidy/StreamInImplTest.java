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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;


/**
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class StreamInImplTest extends TestCase
{

    /**
     * test instance.
     */
    private StreamInImpl in;

    /**
     * Lexer instance saved in StreamInImpl.
     */
    private Lexer lexer;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        InputStream stream = new ByteArrayInputStream(new byte[]{0});
        in = new StreamInImpl(stream, Configuration.ASCII, 8);
        Report report = new Report();
        Configuration configuration = new Configuration(report);
        lexer = new Lexer(in, configuration, report);
        lexer.configuration = configuration;
        in.setLexer(lexer);
    }

    /**
     * test readChar() with ascii encoding.
     */
    public final void testReadCharFromStreamAscii()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{97, 97, 97});
        in = new StreamInImpl(stream, Configuration.ASCII, 8);
        lexer.configuration.inCharEncoding = Configuration.ASCII;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

    /**
     * test readChar() with UTF16 encoding and no BOM.
     */
    public final void testReadCharFromStreamUTF16()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{00, 97, 00, 97});
        in = new StreamInImpl(stream, Configuration.UTF16BE, 8);
        lexer.configuration.inCharEncoding = Configuration.UTF16BE;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

    /**
     * test readChar() with UTF16 encoding and BE BOM.
     */
    public final void testReadCharFromStreamUTF16WithBOMLE()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{-1, -2, 97, 00});
        in = new StreamInImpl(stream, Configuration.UTF16, 8);
        lexer.configuration.inCharEncoding = Configuration.UTF16;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals(StreamIn.UNICODE_BOM, thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

    /**
     * test readChar() with UTF16 encoding and LE BOM.
     */
    public final void testReadCharFromStreamUTF16WithBOMBE()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{-2, -1, 00, 97});
        in = new StreamInImpl(stream, Configuration.UTF16, 8);
        lexer.configuration.inCharEncoding = Configuration.UTF16;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals(StreamIn.UNICODE_BOM, thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

}