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

import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class ConfigurationTest extends TestCase
{

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(ConfigurationTest.class);

    /**
     * Test for -help-config.
     * @throws Exception any exception thrown during test
     */
    public void testPrintConfig() throws Exception
    {
        Tidy tidy = new Tidy();
        Configuration configuration = tidy.getConfiguration();
        StringWriter writer = new StringWriter();
        configuration.printConfigOptions(writer, false);
        String result = writer.toString();
        // just test that no exception occurred and that something was printed
        assertTrue(result.length() > 200);
        log.info(result);
    }

    /**
     * Test for -show-config.
     * @throws Exception any exception thrown during test
     */
    public void testPrintActualConfig() throws Exception
    {
        Tidy tidy = new Tidy();
        tidy.getConfiguration().tt.defineTag(Dict.TAGTYPE_INLINE, "something");
        tidy.getConfiguration().tt.defineTag(Dict.TAGTYPE_INLINE, "second");
        Configuration configuration = tidy.getConfiguration();
        StringWriter writer = new StringWriter();
        configuration.printConfigOptions(writer, true);
        String result = writer.toString();
        // just test that no exception occurred and that something was printed
        assertTrue(result.length() > 200);
        log.info(result);
    }

}