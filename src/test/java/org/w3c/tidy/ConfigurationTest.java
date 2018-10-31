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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class ConfigurationTest extends TestCase
{

    /**
     * logger.
     */
    private static Logger log = LoggerFactory.getLogger(ConfigurationTest.class);

    /**
     * Test for -help-config.
     * @throws Exception any exception thrown during test
     */
    public void testPrintConfig()
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
    public void testPrintActualConfig()
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
    public void testGetSet()
    {
        Tidy tidy = new Tidy();

        tidy.setAltText("alt");
        assertEquals("alt", tidy.getAltText());

        tidy.setAsciiChars(false);
        assertFalse(tidy.getAsciiChars());

        tidy.setBreakBeforeBR(true);
        assertTrue(tidy.getBreakBeforeBR());

        tidy.setBurstSlides(true);
        assertTrue(tidy.getBurstSlides());

        tidy.setDropEmptyParas(false);
        assertFalse(tidy.getDropEmptyParas());

        tidy.setDropFontTags(true);
        assertTrue(tidy.getDropFontTags());

        tidy.setDropProprietaryAttributes(true);
        assertTrue(tidy.getDropProprietaryAttributes());

        tidy.setEmacs(true);
        assertTrue(tidy.getEmacs());

        tidy.setEncloseBlockText(true);
        assertTrue(tidy.getEncloseBlockText());

        tidy.setEncloseText(true);
        assertTrue(tidy.getEncloseText());

        tidy.setEscapeCdata(true);
        assertTrue(tidy.getEscapeCdata());

        tidy.setFixBackslash(true);
        assertTrue(tidy.getFixBackslash());

        tidy.setFixComments(true);
        assertTrue(tidy.getFixComments());

        tidy.setFixUri(true);
        assertTrue(tidy.getFixUri());

        tidy.setForceOutput(true);
        assertTrue(tidy.getForceOutput());

        tidy.setHideComments(true);
        assertTrue(tidy.getHideComments());

        tidy.setHideEndTags(true);
        assertTrue(tidy.getHideEndTags());

        tidy.setIndentAttributes(true);
        assertTrue(tidy.getIndentAttributes());

        tidy.setIndentCdata(true);
        assertTrue(tidy.getIndentCdata());

        tidy.setIndentContent(true);
        assertTrue(tidy.getIndentContent());

        tidy.setJoinClasses(true);
        assertTrue(tidy.getJoinClasses());

        tidy.setJoinStyles(true);
        assertTrue(tidy.getJoinStyles());

        tidy.setKeepFileTimes(true);
        assertTrue(tidy.getKeepFileTimes());

        tidy.setLiteralAttribs(true);
        assertTrue(tidy.getLiteralAttribs());

        tidy.setLogicalEmphasis(true);
        assertTrue(tidy.getLogicalEmphasis());

        tidy.setLowerLiterals(true);
        assertTrue(tidy.getLowerLiterals());

        tidy.setMakeBare(true);
        assertTrue(tidy.getMakeBare());

        tidy.setMakeClean(true);
        assertTrue(tidy.getMakeClean());

        tidy.setNumEntities(true);
        assertTrue(tidy.getNumEntities());

        tidy.setOnlyErrors(true);
        assertTrue(tidy.getOnlyErrors());

        tidy.setPrintBodyOnly(true);
        assertTrue(tidy.getPrintBodyOnly());

        tidy.setQuiet(true);
        assertTrue(tidy.getQuiet());

        tidy.setQuoteAmpersand(true);
        assertTrue(tidy.getQuoteAmpersand());

        tidy.setQuoteMarks(true);
        assertTrue(tidy.getQuoteMarks());

        tidy.setQuoteNbsp(true);
        assertTrue(tidy.getQuoteNbsp());

        tidy.setRawOut(true);
        assertTrue(tidy.getRawOut());

        tidy.setReplaceColor(true);
        assertTrue(tidy.getReplaceColor());

        tidy.setShowWarnings(true);
        assertTrue(tidy.getShowWarnings());

        tidy.setSmartIndent(true);
        assertTrue(tidy.getSmartIndent());

        tidy.setTidyMark(true);
        assertTrue(tidy.getTidyMark());

        tidy.setTrimEmptyElements(true);
        assertTrue(tidy.getTrimEmptyElements());

        tidy.setUpperCaseAttrs(true);
        assertTrue(tidy.getUpperCaseAttrs());

        tidy.setUpperCaseTags(true);
        assertTrue(tidy.getUpperCaseTags());

        tidy.setWord2000(true);
        assertTrue(tidy.getWord2000());

        tidy.setWrapAsp(true);
        assertTrue(tidy.getWrapAsp());

        tidy.setWrapAttVals(true);
        assertTrue(tidy.getWrapAttVals());

        tidy.setWrapJste(true);
        assertTrue(tidy.getWrapJste());

        tidy.setWrapPhp(true);
        assertTrue(tidy.getWrapPhp());

        tidy.setWrapScriptlets(true);
        assertTrue(tidy.getWrapScriptlets());

        tidy.setWrapSection(true);
        assertTrue(tidy.getWrapSection());

        tidy.setWraplen(5);
        assertEquals(5, tidy.getWraplen());

        tidy.setWriteback(true);
        assertTrue(tidy.getWriteback());

        tidy.setXHTML(true);
        assertTrue(tidy.getXHTML());

        tidy.setXmlOut(true);
        assertTrue(tidy.getXmlOut());

        tidy.setXmlPi(true);
        assertTrue(tidy.getXmlPi());

        tidy.setXmlPIs(true);
        assertTrue(tidy.getXmlPIs());

        tidy.setXmlSpace(true);
        assertTrue(tidy.getXmlSpace());

        tidy.setXmlTags(true);
        assertTrue(tidy.getXmlTags());

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