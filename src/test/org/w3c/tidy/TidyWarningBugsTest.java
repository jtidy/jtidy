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

/**
 * Testcase for Tidy resolved bugs (Tidy warning and errors).
 * <p>
 * see <code>http://sourceforge.net/support/tracker.php?aid=(item number)</code>
 * </p>
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class TidyWarningBugsTest extends TidyTestCase
{

    /**
     * test for Tidy [427810] : Proprietary elements not reported as err.
     * @throws Exception any exception generated during the test
     */
    public void test427810() throws Exception
    {
        // line 1 column 1 - Warning: missing <!DOCTYPE> declaration
        // line 8 column 1 - Warning: <blink> is not approved by W3C
        // line 9 column 1 - Warning: <wbr> is not approved by W3C
        // line 10 column 1 - Warning: <nobr> is not approved by W3C
        // Info: Document content looks like HTML Proprietary
        // 4 warnings, 0 errors were found!

        executeTidyTest("427810.html");
        assertWarnings(4);
    }

    /**
     * test for Tidy [431874] : Nested anchors not detected.
     * @throws Exception any exception generated during the test
     */
    public void test431874() throws Exception
    {
        executeTidyTest("431874.html");
        assertWarnings(2);
    }

    /**
     * test for Tidy [427827] : Nested anchor elements allowed.
     * @throws Exception any exception generated during the test
     */
    public void test427827() throws Exception
    {
        // line 6 column 1 - Warning: missing </a> before <a>
        // line 7 column 6 - Warning: discarding unexpected </a>
        // 2 warnings, 0 errors were found!

        executeTidyTest("427827.html");
        assertWarnings(2);
    }

    /**
     * test for Tidy [427834] : Warning given for newline in DOCTYPE.
     * @throws Exception any exception generated during the test
     */
    public void test427834() throws Exception
    {
        // no warnings
        executeTidyTest("427834.html");
        assertNoWarnings();
    }

    /**
     * test for Tidy [427844] : End tags containing whitespace warning.
     * @throws Exception any exception generated during the test
     */
    public void test427844() throws Exception
    {
        executeTidyTest("427844.html");

        assertNoWarnings();
    }

    /**
     * test for Tidy [431719] : Spec want "HTML 3.2 Final", but everyone in the world, including Tidy, uses "HTML 3.2".
     * So the software has to recognize both FPI's as equivalent.
     * @throws Exception any exception generated during the test
     */
    public void test431719() throws Exception
    {
        // line 11 column 3 - Warning: <table> lacks "summary" attribute
        // Info: Doctype given is "-//W3C//DTD HTML 3.2//EN"
        // Info: Document content looks like HTML 3.2
        // 1 warning, 0 errors were found!

        // still bad in tidy?

        executeTidyTest("431719.html");
        assertNoWarnings();
    }

    /**
     * test for Tidy [431883] : Running Tidy on this file produces the diagnostic: Doctype given is "-//W3C//DTD HTML
     * 3.2//EN" when the DOCTYPE is not as shown.
     * @throws Exception any exception generated during the test
     */
    public void test431883() throws Exception
    {
        // line 40 column 1 - Warning: <table> lacks "summary" attribute
        // Info: Doctype given is "-//W3C//DTD HTML 4.0//EN"
        // Info: Document content looks like HTML 4.01 Strict

        executeTidyTest("431883.html");
        // the warning for table lacking summary only shows up if tidy correctly skips the 3.2 false doctype
        assertWarnings(1);
    }

    /**
     * test for Tidy [431956] : Well formed XSL xsl:text gives error.
     * @throws Exception any exception generated during the test
     */
    public void test431956() throws Exception
    {
        // No warnings or errors were found. (-xml)

        executeTidyTest("431956.xml");
        assertNoWarnings();
    }

    /**
     * test for Tidy [431964] : table height="" not flagged as error.
     * @throws Exception any exception generated during the test
     */
    public void test431964() throws Exception
    {
        // line 7 column 1 - Warning: <table> attribute "height" lacks value
        // line 7 column 1 - Warning: <table> proprietary attribute "height"
        // Info: Doctype given is "-//W3C//DTD HTML 4.01 Transitional//EN"
        // Info: Document content looks like HTML Proprietary
        // 2 warnings, 0 errors were found!

        executeTidyTest("431964.html");
        assertWarnings(2);
    }

    /**
     * test for Tidy [433607] : No warning for omitted end tag with -xml.
     * @throws Exception any exception generated during the test
     */
    public void test433607() throws Exception
    {
        // No warnings or errors were found. (-xml)

        // still bad in tidy?
        executeTidyTest("433607.xml");

        assertWarnings(1);
    }

    /**
     * test for Tidy [433670] : &amp;apos not recognized as valid XML entity.
     * @throws Exception any exception generated during the test
     */
    public void test433670() throws Exception
    {
        // No warnings or errors were found. (-xml)
        executeTidyTest("433670.xml");
        assertNoWarnings();
    }

    /**
     * test for Tidy [434047] : Mixed content in 4.01 Strict not allowed.
     * @throws Exception any exception generated during the test
     */
    public void test434047() throws Exception
    {
        // Info: Doctype given is "-//W3C//DTD HTML 4.01//EN"
        // Info: Document content looks like HTML 4.01 Strict
        // No warnings or errors were found.

        executeTidyTest("434047.html");

        assertLogContains("HTML 4.01 Strict");

    }

    /**
     * test for Tidy [434100] : Error actually reported as a warning (-xml).
     * @throws Exception any exception generated during the test
     */
    public void test434100() throws Exception
    {
        // -xml
        // line 13 column 1 - Error: unexpected </head> in <link>
        // 0 warnings, 1 error were found!

        executeTidyTest("434100.html");
        assertErrors(1);
        assertNoWarnings();
    }

    /**
     * test for Tidy [435917] : &lt;input onfocus=""&gt; reported unknown attr.
     * @throws Exception any exception generated during the test
     */
    public void test435917() throws Exception
    {
        // line 11 column 1 - Warning: <input> attribute with missing trailing quote mark
        // Info: Doctype given is "-//W3C//DTD HTML 4.01 Transitional//EN"
        // Info: Document content looks like HTML 4.01 Transitional
        // 1 warning, 0 errors were found!

        executeTidyTest("435917.html");
        assertWarnings(1);
    }

    /**
     * test for Tidy [435922] : Missing &lt;form&gt; around &lt;input&gt; no warning.
     * @throws Exception any exception generated during the test
     */
    public void test435922() throws Exception
    {
        // line 6 column 1 - Warning: <input> isn't allowed in <body> elements
        // line 7 column 3 - Warning: inserting implicit <form>
        // line 7 column 3 - Warning: missing </form>
        // line 7 column 3 - Warning: <form> lacks "action" attribute
        // Info: Doctype given is "-//W3C//DTD HTML 4.01 Transitional//EN"
        // Info: Document content looks like HTML 4.01 Transitional
        // 4 warnings, 0 errors were found!

        executeTidyTest("435922.html");
        assertWarnings(4);
    }

    /**
     * test for Tidy [438956] : Bad head-endtag reported incorrectly.
     * @throws Exception any exception generated during the test
     */
    public void test438956() throws Exception
    {
        // line 3 column 1 - Warning: plain text isn't allowed in <head> elements
        // line 6 column 1 - Warning: discarding unexpected <body>
        // Info: Doctype given is "-//W3C//DTD HTML 4.01 Transitional//EN"
        // Info: Document content looks like HTML 4.01 Transitional
        // 2 warnings, 0 errors were found!

        executeTidyTest("438956.html");

        // jtidy incorrectly reports only 1 (bad) warning: <body> isn't allowed in <body> elements
        assertWarnings(2);
    }
    /**
     * test for Tidy [446019] : &lt;img name="foo"&gt; allowed in XTHML-Strict.
     * @throws Exception any exception generated during the test
     */
    public void test446019() throws Exception
    {
        // Info: Doctype given is "-//W3C//DTD XHTML 1.0 Strict//EN"
        // Info: Document content looks like XHTML 1.0 Transitional
        // No warnings or errors were found.

        executeTidyTest("446019.xhtml");

        assertLogContains("XHTML 1.0 Transitional");
    }
    /**
     * test for Tidy [450389] : Color attval check allows only black/#.
     * @throws Exception any exception generated during the test
     */
    public void test450389() throws Exception
    {
        // line 1 column 1 - Warning: missing <!DOCTYPE> declaration
        // line 44 column 1 - Warning: <font> attribute "color" has invalid value "reddish"
        // line 72 column 1 - Warning: <font> attribute "color" has invalid value "#FF"
        // line 76 column 1 - Warning: <font> attribute "color" has invalid value "grurple"
        // line 77 column 1 - Warning: <font> attribute "color" has invalid value "#grurple"
        // line 78 column 1 - Warning: <font> attribute "color" has invalid value "#1234567"
        // Info: Document content looks like HTML 3.2

        executeTidyTest("450389.html");
        assertWarnings(6);
    }

    /**
     * test for Tidy [501669] : width="n*" marked invalid on &lt;COL&gt;.
     * @throws Exception any exception generated during the test
     */
    public void test501669() throws Exception
    {
        // Info: Doctype given is "-//W3C//DTD HTML 4.01 Transitional//EN"
        // Info: Document content looks like HTML 4.01 Strict
        // No warnings or errors were found.

        executeTidyTest("501669.html");

        assertNoWarnings();
    }

    /**
     * test for Tidy [516370] : Invalid ID value.
     * @throws Exception any exception generated during the test
     */
    public void test516370() throws Exception
    {
        // line 10 column 1 - Warning: <h2> ID "_ValidID2" uses XML ID syntax
        // line 11 column 1 - Warning: <h2> ID ":ValidID3" uses XML ID syntax
        // line 12 column 1 - Warning: <h2> attribute "id" has invalid value ".InvalidID1"
        // line 13 column 1 - Warning: <h2> attribute "id" has invalid value "2InvalidID2"
        // Info: Doctype given is "-//W3C//DTD XHTML 1.1//EN"
        // Info: Document content looks like XHTML 1.1
        // 4 warnings, 0 errors were found!

        executeTidyTest("516370.xhtml");
        assertWarnings(4);
    }

    /**
     * test for Tidy [525081] : frameset rows attr. not recognized.
     * @throws Exception any exception generated during the test
     */
    public void test525081() throws Exception
    {
        // Info: Doctype given is "-//W3C//DTD HTML 4.01 Frameset//EN"
        // Info: Document content looks like HTML 4.01 Frameset
        // No warnings or errors were found.

        executeTidyTest("525081.html");
        assertNoWarnings();
    }

    /**
     * test for Tidy [553468] : Doesn't warn about &lt;u&gt; in XHTML strict.
     * @throws Exception any exception generated during the test
     */
    public void test553468() throws Exception
    {

        // Info: Doctype given is "-//W3C//DTD XHTML 1.0 Strict//EN"
        // Info: Document content looks like XHTML 1.0 Transitional
        // No warnings or errors were found.

        executeTidyTest("553468.xhtml");

        assertLogContains("XHTML 1.0 Transitional");
    }

    /**
     * test for Tidy [706260] : size not accepted for input.
     * @throws Exception any exception generated during the test
     */
    public void test706260() throws Exception
    {
        // Info: Doctype given is "-//W3C//DTD XHTML 1.0 Strict//EN"
        // Info: Document content looks like XHTML 1.0 Transitional
        // No warnings or errors were found.

        executeTidyTest("706260.html");
        assertNoWarnings();
    }

}
