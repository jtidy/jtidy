/**
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
 * Test for Report messages. <strong>This test case actually requires EN locale to run successfully. </strong>.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class ReportTest extends TestCase
{

    /**
     * report instance.
     */
    private Report report;

    /**
     * lexer instance.
     */
    private Lexer lexer;

    /**
     * instantiates a new test.
     * @param name test name
     */
    public ReportTest(String name)
    {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        this.report = new Report();
        this.lexer = new Lexer(null, new Configuration(report), this.report);
        lexer.lines = 12;
        lexer.columns = 34;
    }

    /**
     * test getMessage with the <code>missing_endtag_for</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageMissingEndtagFor() throws Exception
    {
        String message = this.report.getMessage(lexer, "missing_endtag_for", new Object[]{"test"},
            TidyMessage.Level.WARNING);
        assertEquals(message, "line 12 column 34 - Warning: missing </test>");
    }

    /**
     * test getMessage with the <code>missing_endtag_before</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageMissingEndtagBefore() throws Exception
    {
        String message = this.report.getMessage(lexer, "missing_endtag_before", new Object[]{"test", "bee"},
            TidyMessage.Level.WARNING);
        assertEquals(message, "line 12 column 34 - Warning: missing </test> before bee");
    }

    /**
     * test getMessage with the <code>discarding_unexpected</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageDiscardingUnexpected() throws Exception
    {
        String message = this.report.getMessage(lexer, "discarding_unexpected", new Object[]{"test"},
            TidyMessage.Level.WARNING);
        assertEquals(message, "line 12 column 34 - Warning: discarding unexpected test");
    }

    /**
     * test getMessage with the <code>nested_emphasis</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageNestedEmphasis() throws Exception
    {
        String message = this.report.getMessage(lexer, "nested_emphasis", new Object[]{"test"}, TidyMessage.Level.INFO);
        assertEquals(message, "line 12 column 34 - nested emphasis test");
    }

    /**
     * test getMessage with the <code>coerce_to_endtag</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageCoerceToEndtag() throws Exception
    {
        String message = this.report
            .getMessage(lexer, "coerce_to_endtag", new Object[]{"test"}, TidyMessage.Level.INFO);
        assertEquals(message, "line 12 column 34 - <test> is probably intended as </test>");
    }

    /**
     * test getMessage with the <code>non_matching_endtag</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageNonMatchingEndtag() throws Exception
    {
        String message = this.report.getMessage(lexer, "non_matching_endtag", new Object[]{"<test>", "bee"},
            TidyMessage.Level.WARNING);
        assertEquals(message, "line 12 column 34 - Warning: replacing unexpected <test> by </bee>");
    }

    /**
     * test getMessage with the <code>tag_not_allowed_in</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageTagNonAllowedIn() throws Exception
    {
        String message = this.report.getMessage(lexer, "tag_not_allowed_in", new Object[]{"<test>", "bee"},
            TidyMessage.Level.WARNING);
        assertEquals(message, "line 12 column 34 - Warning: <test> isn't allowed in <bee> elements");
    }

    /**
     * test getMessage with the <code>doctype_after_tags</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageDoctypeAfterTags() throws Exception
    {
        String message = this.report.getMessage(lexer, "doctype_after_tags", null, TidyMessage.Level.WARNING);
        assertEquals(message, "line 12 column 34 - Warning: <!DOCTYPE> isn't allowed after elements");
    }

    /**
     * test getMessage with the <code>missing_starttag</code> key.
     * @throws Exception any Exception generated during test
     */
    public void testGetMessageMissingStarttag() throws Exception
    {
        String message = this.report.getMessage(lexer, "missing_starttag", new Object[]{"test"},
            TidyMessage.Level.WARNING);
        assertEquals(message, "line 12 column 34 - Warning: missing <test>");
    }


}