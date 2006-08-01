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
        log.debug(result);
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
        log.debug(result);
    }

    /**
     * Test for configuration getters and setters.
     * @throws Exception any exception thrown during test
     */
    public void testGetSet() throws Exception
    {
        Tidy tidy = new Tidy();

        tidy.setAltText("alt");
        assertEquals("alt", tidy.getAltText());

        tidy.setAsciiChars(false);
        assertEquals(false, tidy.getAsciiChars());

        tidy.setBreakBeforeBR(true);
        assertEquals(true, tidy.getBreakBeforeBR());

        tidy.setBurstSlides(true);
        assertEquals(true, tidy.getBurstSlides());

        tidy.setDropEmptyParas(false);
        assertEquals(false, tidy.getDropEmptyParas());

        tidy.setDropFontTags(true);
        assertEquals(true, tidy.getDropFontTags());

        tidy.setDropProprietaryAttributes(true);
        assertEquals(true, tidy.getDropProprietaryAttributes());

        tidy.setEmacs(true);
        assertEquals(true, tidy.getEmacs());

        tidy.setEncloseBlockText(true);
        assertEquals(true, tidy.getEncloseBlockText());

        tidy.setEncloseText(true);
        assertEquals(true, tidy.getEncloseText());

        tidy.setEscapeCdata(true);
        assertEquals(true, tidy.getEscapeCdata());

        tidy.setFixBackslash(true);
        assertEquals(true, tidy.getFixBackslash());

        tidy.setFixComments(true);
        assertEquals(true, tidy.getFixComments());

        tidy.setFixUri(true);
        assertEquals(true, tidy.getFixUri());

        tidy.setForceOutput(true);
        assertEquals(true, tidy.getForceOutput());

        tidy.setHideComments(true);
        assertEquals(true, tidy.getHideComments());

        tidy.setHideEndTags(true);
        assertEquals(true, tidy.getHideEndTags());

        tidy.setIndentAttributes(true);
        assertEquals(true, tidy.getIndentAttributes());

        tidy.setIndentCdata(true);
        assertEquals(true, tidy.getIndentCdata());

        tidy.setIndentContent(true);
        assertEquals(true, tidy.getIndentContent());

        tidy.setJoinClasses(true);
        assertEquals(true, tidy.getJoinClasses());

        tidy.setJoinStyles(true);
        assertEquals(true, tidy.getJoinStyles());

        tidy.setKeepFileTimes(true);
        assertEquals(true, tidy.getKeepFileTimes());

        tidy.setLiteralAttribs(true);
        assertEquals(true, tidy.getLiteralAttribs());

        tidy.setLogicalEmphasis(true);
        assertEquals(true, tidy.getLogicalEmphasis());

        tidy.setLowerLiterals(true);
        assertEquals(true, tidy.getLowerLiterals());

        tidy.setMakeBare(true);
        assertEquals(true, tidy.getMakeBare());

        tidy.setMakeClean(true);
        assertEquals(true, tidy.getMakeClean());

        tidy.setNumEntities(true);
        assertEquals(true, tidy.getNumEntities());

        tidy.setOnlyErrors(true);
        assertEquals(true, tidy.getOnlyErrors());

        tidy.setPrintBodyOnly(true);
        assertEquals(true, tidy.getPrintBodyOnly());

        tidy.setQuiet(true);
        assertEquals(true, tidy.getQuiet());

        tidy.setQuoteAmpersand(true);
        assertEquals(true, tidy.getQuoteAmpersand());

        tidy.setQuoteMarks(true);
        assertEquals(true, tidy.getQuoteMarks());

        tidy.setQuoteNbsp(true);
        assertEquals(true, tidy.getQuoteNbsp());

        tidy.setRawOut(true);
        assertEquals(true, tidy.getRawOut());

        tidy.setReplaceColor(true);
        assertEquals(true, tidy.getReplaceColor());

        tidy.setShowWarnings(true);
        assertEquals(true, tidy.getShowWarnings());

        tidy.setSmartIndent(true);
        assertEquals(true, tidy.getSmartIndent());

        tidy.setTidyMark(true);
        assertEquals(true, tidy.getTidyMark());

        tidy.setTrimEmptyElements(true);
        assertEquals(true, tidy.getTrimEmptyElements());

        tidy.setUpperCaseAttrs(true);
        assertEquals(true, tidy.getUpperCaseAttrs());

        tidy.setUpperCaseTags(true);
        assertEquals(true, tidy.getUpperCaseTags());

        tidy.setWord2000(true);
        assertEquals(true, tidy.getWord2000());

        tidy.setWrapAsp(true);
        assertEquals(true, tidy.getWrapAsp());

        tidy.setWrapAttVals(true);
        assertEquals(true, tidy.getWrapAttVals());

        tidy.setWrapJste(true);
        assertEquals(true, tidy.getWrapJste());

        tidy.setWrapPhp(true);
        assertEquals(true, tidy.getWrapPhp());

        tidy.setWrapScriptlets(true);
        assertEquals(true, tidy.getWrapScriptlets());

        tidy.setWrapSection(true);
        assertEquals(true, tidy.getWrapSection());

        tidy.setWraplen(5);
        assertEquals(5, tidy.getWraplen());

        tidy.setWriteback(true);
        assertEquals(true, tidy.getWriteback());

        tidy.setXHTML(true);
        assertEquals(true, tidy.getXHTML());

        tidy.setXmlOut(true);
        assertEquals(true, tidy.getXmlOut());

        tidy.setXmlPi(true);
        assertEquals(true, tidy.getXmlPi());

        tidy.setXmlPIs(true);
        assertEquals(true, tidy.getXmlPIs());

        tidy.setXmlSpace(true);
        assertEquals(true, tidy.getXmlSpace());

        tidy.setXmlTags(true);
        assertEquals(true, tidy.getXmlTags());

        tidy.setTabsize(5);
        assertEquals(5, tidy.getTabsize());

        tidy.setOutputEncoding("UTF8");
        assertEquals("UTF8", tidy.getOutputEncoding());

        tidy.setInputEncoding("UTF8");
        assertEquals("UTF8", tidy.getInputEncoding());

        tidy.setRepeatedAttributes(Configuration.KEEP_FIRST);
        assertEquals(Configuration.KEEP_FIRST, tidy.getRepeatedAttributes());

        tidy.setShowErrors(10);
        assertEquals(10, tidy.getShowErrors());

        tidy.setDocType("strict");
        assertEquals("strict", tidy.getDocType());

        tidy.setErrfile("errfile");
        assertEquals("errfile", tidy.getErrfile());

        tidy.setSpaces(5);
        assertEquals(5, tidy.getSpaces());

        tidy.setInputStreamName("inputname");
        assertEquals("inputname", tidy.getInputStreamName());

    }
}