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

}
