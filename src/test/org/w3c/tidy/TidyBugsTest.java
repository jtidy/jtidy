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
 * Testcase for Tidy resolved bugs.
 * <p>
 * see <code>http://sourceforge.net/support/tracker.php?aid=(item number)</code>
 * </p>
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class TidyBugsTest extends TidyTestCase
{
    /**
     * test for Tidy [427822] : PopInLine() doesn't check stack.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test427822() throws Exception
    {
        executeTidyTest("427822.html");
    }

    /**
     * test for Tidy [427825] : Test user defined tags.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test427825() throws Exception
    {
        executeTidyTest("427825.html");
    }
    /**
     * test for Tidy [431889] : The "alt-text:" and "doctype: &lt;fpi>" options do not work when specified in a config
     * file with a quoted string parameter.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test431889() throws Exception
    {
        executeTidyTest("431889.html");
    }
    /**
     * test for Tidy [431895] : Filename not set when processing XML or if the "-quiet" option is used when processing
     * HTML; "(null)" is reported instead.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test431895() throws Exception
    {
        executeTidyTest("431895.html");
    }
    /**
     * test for Tidy [431965] : XHTML Strict seen as Transitional w/div.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test431965() throws Exception
    {
        executeTidyTest("431965.xhtml");
    }
    /**
     * test for Tidy [432677] : Null value changed to "value" for -asxml.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test432677() throws Exception
    {
        executeTidyTest("432677.html");
    }
    /**
     * test for Tidy [433012] : Illegal ampersands/character entities.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test433012() throws Exception
    {
        executeTidyTest("433012.html");
    }
    /**
     * test for Tidy [433021] : Identify attribute whose value is bad.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test433021() throws Exception
    {
        executeTidyTest("433021.html");
    }
    /**
     * test for Tidy [433856] : use "--drop-font-tags yes" on command line.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test433856() throws Exception
    {
        executeTidyTest("433856.html");
    }

    /**
     * test for Tidy [434940] : --show-body-only: print only body contents.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test434940() throws Exception
    {
        executeTidyTest("434940.html");
    }

    /**
     * test for Tidy [441508] : parser.c: BadForm() function broken.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test441508() throws Exception
    {
        executeTidyTest("441508.html");
    }
    /**
     * test for Tidy [480701] : -xml conflicts with -output-xhtml.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test480701() throws Exception
    {
        executeTidyTest("480701.xml");
    }
    /**
     * test for Tidy [480843] : Proposed change to FixID().
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test480843() throws Exception
    {
        executeTidyTest("480843.xhtml");
    }

    /**
     * test for Tidy [508936] : Parse CSS Selector prefix in config file.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test508936() throws Exception
    {
        executeTidyTest("508936.html");
    }

    /**
     * test for Tidy [511243] : xhtml utf8 format bug.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test511243() throws Exception
    {
        executeTidyTest("511243.xhtml");
    }

    /**
     * test for Tidy [517550] : parser misinterprets ?xml-stylesheet PI.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test517550() throws Exception
    {
        executeTidyTest("517550.xhtml");
    }

    /**
     * test for Tidy [539369a] : Test &lt;/frameset&gt; inside &lt;noframes&gt;.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test539369a() throws Exception
    {
        executeTidyTest("539369a.html");
    }

    /**
     * test for Tidy [649812] : Does TidyLib correctly handle Mac files?.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test649812() throws Exception
    {
        executeTidyTest("649812.html");
    }

    /**
     * test for Tidy [661606] : Two bytes at the last line, w/ asian options.
     * @throws Exception any exception generated during the test @todo finish test
     */
    public void test661606() throws Exception
    {
        executeTidyTest("661606.html");
    }

}