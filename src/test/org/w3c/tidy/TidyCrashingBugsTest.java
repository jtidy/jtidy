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

import java.io.File;
import java.text.NumberFormat;


/**
 * Testcase for Tidy resolved bugs. Bugs that crashed tidy or caused infinite loops.
 * 
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class TidyCrashingBugsTest extends TidyTestCase
{

    /**
     * Instantiate a new Test case.
     * 
     * @param name test name
     */
    public TidyCrashingBugsTest(String name)
    {
        super(name);
    }

    /**
     * test for Tidy [427664] : Missing attr values cause NULL segfault.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427664() throws Exception
    {
        executeTidyTest("427664.html");
    }

    /**
     * test for Tidy [427671] : &lt;LI&gt; w/FRAME/FRAMESET/OPTGROUP/OPTION loop.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427671() throws Exception
    {
        executeTidyTest("427671.html");
    }

    /**
     * test for Tidy [427675] : Frameset followed by frame infinite loop.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427675() throws Exception
    {
        executeTidyTest("427675.html");
    }

    /**
     * test for Tidy [427676] : Missing = from attr value NULL segfault.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427676() throws Exception
    {
        executeTidyTest("427676.html");
    }

    /**
     * test for Tidy [427672] : Non-std attrs w/multibyte names segfault.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427672() throws Exception
    {
        executeTidyTest("427672.html");
    }

    /**
     * test for Tidy [426885] : Definition list w/Center crashes.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test426885() throws Exception
    {
        executeTidyTest("426885.html");
    }

    /**
     * test for Tidy [427811] : FRAME inside NOFRAME infinite loop.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427811() throws Exception
    {
        executeTidyTest("427811.html");
    }

    /**
     * test for Tidy [427813] : Missing = from attr value segfaults.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427813() throws Exception
    {
        executeTidyTest("427813.html");
    }

    /**
     * test for Tidy [427816] : Mismatched quotes for attr segfaults.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427816() throws Exception
    {
        executeTidyTest("427816.html");
    }

    /**
     * test for Tidy [427818] : Missing quotes cause segfaults.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427818() throws Exception
    {
        executeTidyTest("427818.html");
    }

    /**
     * test for Tidy [427840] : Span causes infinite loop.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427840() throws Exception
    {
        executeTidyTest("427840.html");
    }

    /**
     * test for Tidy [427841] : Tidy crashes on badly formed HTML involving nested lists.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test427841() throws Exception
    {
        executeTidyTest("427841.html");
    }

    /**
     * test for Tidy [431716] : -slides causes a seg fault.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test431716() throws Exception
    {
        executeTidyTest("431716.html");

        // delete generates slides (this should be done in tear down be I'm pretty confident tidy will not crash here)
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(3);
        for (int j = 1; j <= 26; j++)
        {
            String slide = "slide" + numberFormat.format(j) + ".html";
            (new File(slide)).delete();
        }
    }

    /**
     * test for Tidy [443362] : null-pointer exception for doctype in pre.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test443362() throws Exception
    {
        executeTidyTest("443362.html");
    }

    /**
     * test for Tidy [433856] : Access violation w/Word files w/font tag.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test433856() throws Exception
    {
        executeTidyTest("433856.html");
    }

    /**
     * test for Tidy [532535] : Hang when in code &lt;?xml /&gt;.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test532535() throws Exception
    {
        executeTidyTest("532535.html");
    }

    /**
     * test for Tidy [539369] : Infinite loop &lt;/frame&gt; after &lt;/frameset&gt;.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test539369() throws Exception
    {
        executeTidyTest("539369.html");
    }

    /**
     * test for Tidy [539369a] : Test &lt;/frameset&gt; inside &lt;noframes&gt; (infinite loop).
     * 
     * @throws Exception any exception generated during the test
     */
    public void test539369a() throws Exception
    {
        executeTidyTest("539369a.html");
    }

    /**
     * test for Tidy [540296] : Tidy dumps.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test540296() throws Exception
    {
        executeTidyTest("540296.html");
    }

    /**
     * test for Tidy [542029] : PPrintXmlDecl reads outside array range.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test542029() throws Exception
    {
        executeTidyTest("542029.html");
    }

    /**
     * test for Tidy [543262] : tidy eats all memory.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test543262() throws Exception
    {
        executeTidyTest("543262.html");
    }

    /**
     * test for Tidy [545772] : --output-xhtml hangs on most files.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test545772() throws Exception
    {
        executeTidyTest("545772.html");
    }

    /**
     * test for Tidy [566542] : parser hangs.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test566542() throws Exception
    {
        executeTidyTest("566542.html");
    }

    /**
     * test for Tidy [570027] : Fixes crash in Word2000 cleanup.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test570027() throws Exception
    {
        executeTidyTest("570027.html");
    }

    /**
     * test for Tidy [588061] : Crash on www.tvnav.com.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test588061() throws Exception
    {
        executeTidyTest("588061.html");
    }

    /**
     * test for Tidy [661606] : Two bytes at the last line, w/ asian options.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test661606() throws Exception
    {
        executeTidyTest("661606.html");
    }

    /**
     * test for Tidy [671087] : tidy loops with --new-inline-tags table,tr,td.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test671087() throws Exception
    {
        executeTidyTest("671087.html");
    }

    /**
     * test for Tidy [676205] : &lt;img src=&quot;&gt; crashes Tidy.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test676205() throws Exception
    {
        executeTidyTest("676205.html");
    }

    /**
     * test for Tidy [679135] : Crashes while checking attributes.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test679135() throws Exception
    {
        executeTidyTest("679135.html");
    }

    /**
     * test for Tidy [696799] : Crash: &lt;script language=&quot;&quot;&gt;.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test696799() throws Exception
    {
        executeTidyTest("696799.html");
    }

    /**
     * test for Tidy [788031] : tidy hangs on input.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test788031() throws Exception
    {
        executeTidyTest("788031.html");
    }

    /**
     * test for Tidy [837023] : segfault on doctype-like element.
     * 
     * @throws Exception any exception generated during the test
     */
    public void test837023() throws Exception
    {
        executeTidyTest("837023.html");
    }

}