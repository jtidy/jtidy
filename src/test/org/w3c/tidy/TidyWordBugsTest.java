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
 * Testcase for Tidy resolved bugs (Word documents cleaning bugs).
 * <p>
 * see <code>http://sourceforge.net/support/tracker.php?aid=(item number)</code>
 * </p>
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class TidyWordBugsTest extends TidyTestCase
{

    /**
     * test for Tidy [431721] : Cleaning list bullets for Word 2000.
     * @throws Exception any exception generated during the test
     */
    public void test431721() throws Exception
    {
        executeTidyTest("431721.html");
    }

    /**
     * test for Tidy [444394] : Tidy strips images from Word2000 docs.
     * @throws Exception any exception generated during the test
     */
    public void test444394() throws Exception
    {
        executeTidyTest("444394.html");
    }

    /**
     * test for Tidy [463066] : CleanWord2000 misses mso-list bullets.
     * @throws Exception any exception generated during the test
     */
    public void test463066() throws Exception
    {
        executeTidyTest("463066.html");
    }

    /**
     * test for Tidy [634889] : Problem with &lt;o:p&gt; ms word tag.
     * @throws Exception any exception generated during the test
     */
    public void test634889() throws Exception
    {
        executeTidyTest("634889.html");
    }

}
