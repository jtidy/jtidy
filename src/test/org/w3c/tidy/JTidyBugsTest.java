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
 * testcase for JTidy resolved bugs.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class JTidyBugsTest extends TidyTestCase
{

    /**
     * Instantiate a new Test case.
     * @param name test name
     */
    public JTidyBugsTest(String name)
    {
        super(name);
    }

    /**
     * test for JTidy [475643]: Hex character references not handled.
     * @throws Exception any exception generated during the test
     */
    public void test475643() throws Exception
    {
        executeTidyTest("j475643.html");

        // no warnings for unknown entities
        assertNoWarnings();
    }

    /**
     * test for JTidy [508245]: Do not convert the & or &lt; to Entity Ref. Actually is "Tidy fails in completing
     * unclosed tags"
     * @throws Exception any exception generated during the test
     */
    public void test508245() throws Exception
    {
        executeTidyTest("j508245.html");
        assertNoErrors();
    }

    /**
     * test for JTidy [527118]: Suppress duplicate attributes.
     * @throws Exception any exception generated during the test
     */
    public void test527118() throws Exception
    {
        executeTidyTest("527118.html");
    }

    /**
     * test for JTidy [538727]: setDocType uncorrectly adds "".
     * @throws Exception any exception generated during the test
     */
    public void test538727() throws Exception
    {
        //this has the same result of setting it in the config file
        //tidy.setDocType("\"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"file:///E:/xhtml1-transitional.dtd\"");
        executeTidyTest("538727.html");
    }

    /**
     * test for JTidy [610244]: NullPointerException in parsing.
     * @throws Exception any exception generated during the test
     */
    public void test610244() throws Exception
    {
        executeTidyTest("j610244.html");
    }

    /**
     * test for JTidy [791933]: German special character converted to upper case.
     * @throws Exception any exception generated during the test
     */
    public void test791933() throws Exception
    {
        executeTidyTest("j791933.html");
    }

    /**
     * test for JTidy [663197]: nbsp handling is wrong.
     * @throws Exception any exception generated during the test
     */
    public void test663197() throws Exception
    {
        executeTidyTest("j663197.html");
    }

    /**
     * test for JTidy [763191]: Again DOM Parsing error (tidy removes spaces in attribute values).
     * @throws Exception any exception generated during the test
     */
    public void test763191() throws Exception
    {
        executeTidyTest("j763191.html");
    }

    /**
     * test for JTidy [763186]: Another DOM Parsing error (tidy inserting whitespaces).
     * @throws Exception any exception generated during the test
     */
    public void test763186() throws Exception
    {
        executeTidyTest("j763186.html");
    }

    /**
     * test for JTidy [917012]: Spaces are moved from content to between tags.
     * @throws Exception any exception generated during the test
     */
    public void test917012() throws Exception
    {
        executeTidyTest("917012.html");
    }

    /**
     * test for JTidy [922302]: Add comment to script tag to produce valid XML.
     * @throws Exception any exception generated during the test
     */
    public void test922302() throws Exception
    {
        executeTidyTest("922302.html");
    }


}