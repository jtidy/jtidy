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
 * Testcase for Tidy resolved bugs (wrong output).
 * <p>
 * see <code>http://sourceforge.net/support/tracker.php?aid=(item number)</code>
 * </p>
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class TidyOutputBugsTest extends TidyTestCase
{

    /**
     * test for Tidy [427812] : Reprocessing OBJECT removes PARAM.
     * @throws Exception any exception generated during the test
     */
    public void test427812() throws Exception
    {
        executeTidyTest("427812.xhtml");
    }

    /**
     * test for Tidy [427662] : BLOCK/INLINE before TABLE parsed wrong.
     * @throws Exception any exception generated during the test
     */
    public void test427662() throws Exception
    {
        executeTidyTest("427662.html");
    }

    /**
     * test for Tidy [427677] : TrimInitialSpace() can trim too much.
     * @throws Exception any exception generated during the test
     */
    public void test427677() throws Exception
    {
        executeTidyTest("427677.html");
    }

    /**
     * test for Tidy [427633] : Line endings not supported correctly.
     * @throws Exception any exception generated during the test
     */
    public void test427633() throws Exception
    {
        // check line endings
        executeTidyTest("427633.html");
    }

    /**
     * test for Tidy [427819] : OPTION w/illegal FONT eats whitespace.
     * @throws Exception any exception generated during the test
     */
    public void test427819() throws Exception
    {
        executeTidyTest("427819.html");
    }

    /**
     * test for Tidy [427820] : indent config options ignored for &lt;BR>.
     * @throws Exception any exception generated during the test
     */
    public void test427820() throws Exception
    {
        executeTidyTest("427820.html");
    }

    /**
     * test for Tidy [427821] : XHTML TRANSITIONAL doctype set wrongly.
     * @throws Exception any exception generated during the test
     */
    public void test427821() throws Exception
    {
        executeTidyTest("427821.html");
    }

    /**
     * test for Tidy [427822] : PopInLine() doesn't check stack.
     * @throws Exception any exception generated during the test
     */
    public void test427822() throws Exception
    {
        // should be already fixed in jtidy
        executeTidyTest("427822.html");
    }

    /**
     * test for Tidy [427823] : Multiple &lt;BODY&gt;'s in &lt;NOFRAMES&gt; allowed.
     * @throws Exception any exception generated during the test
     */
    public void test427823() throws Exception
    {
        executeTidyTest("427823.html");
    }

    /**
     * test for Tidy [427825] : Test user defined tags.
     * @throws Exception any exception generated during the test
     */
    public void test427825() throws Exception
    {
        executeTidyTest("427825.html");
    }

    /**
     * test for Tidy [427826] : Script source needs escaping/CDATA section.
     * @throws Exception any exception generated during the test
     */
    public void test427826() throws Exception
    {
        executeTidyTest("427826.html");
    }

    /**
     * test for Tidy [427830] : Tidy uses an incorrect XHTML 1.0 Namespace, even if the correct namespace is given.
     * @throws Exception any exception generated during the test
     */
    public void test427830() throws Exception
    {
        executeTidyTest("427830.html");
    }

    /**
     * test for Tidy [427833] : Uppercase &amp;#X; escape sequences ignored.
     * @throws Exception any exception generated during the test
     */
    public void test427833() throws Exception
    {
        executeTidyTest("427833.html");
    }

    /**
     * test for Tidy [427836] : OBJECT should be wrapped in BODY.
     * @throws Exception any exception generated during the test
     */
    public void test427836() throws Exception
    {
        executeTidyTest("427836.html");
    }

    /**
     * test for Tidy [427837] : No encoding specified in XML output.
     * @throws Exception any exception generated during the test
     */
    public void test427837() throws Exception
    {
        executeTidyTest("427837.xml");
    }

    /**
     * test for Tidy [427838] : Name Anchor thrown away.
     * @throws Exception any exception generated during the test
     */
    public void test427838() throws Exception
    {
        executeTidyTest("427838.html");
    }

    /**
     * test for Tidy [427839] : Output DOCTYPE/Namespace decl separately.
     * @throws Exception any exception generated during the test
     */
    public void test427839() throws Exception
    {
        executeTidyTest("427839.html");
    }

    /**
     * test for Tidy [427845] : Doctypes are output on multiple lines.
     * @throws Exception any exception generated during the test @todo complete test
     */
    public void test427845() throws Exception
    {
        // doctype should be one line
        // still 2 lines for tidy?

        throw new Error("Test not yet implemented");
        //executeTidyTest("427845.html");
    }

    /**
     * test for Tidy [427846] : Blank char inserted after first char. HIGH PRIORITY.
     * @throws Exception any exception generated during the test
     */
    public void test427846() throws Exception
    {
        executeTidyTest("427846.html");
    }

    /**
     * test for Tidy [431731] : Inline emphasis inconsistent propagation.
     * @throws Exception any exception generated during the test
     */
    public void test431731() throws Exception
    {
        executeTidyTest("431731.html");
    }

    /**
     * test for Tidy [431736] : Doctype decl added before XML decl.
     * @throws Exception any exception generated during the test
     */
    public void test431736() throws Exception
    {
        executeTidyTest("431736.html");
    }

    /**
     * test for Tidy [431739] : Spaces carried into empty block tags.
     * @throws Exception any exception generated during the test
     */
    public void test431739() throws Exception
    {
        executeTidyTest("431739.html");
    }

    /**
     * test for Tidy [431889] : The "alt-text:" and "doctype: &lt;fpi>" options do not work when specified in a config
     * file with a quoted string parameter.
     * @throws Exception any exception generated during the test
     */
    public void test431889() throws Exception
    {
        executeTidyTest("431889.html");
    }

    /**
     * test for Tidy [431898] : Tidy messes up X(HT)ML documents.
     * @throws Exception any exception generated during the test
     */
    public void test431898() throws Exception
    {
        executeTidyTest("431898.html");
    }

    /**
     * test for Tidy [431958] : Comments always indented.
     * @throws Exception any exception generated during the test
     */
    public void test431958() throws Exception
    {
        executeTidyTest("431958.html");
    }

    /**
     * test for Tidy [431965] : XHTML Strict seen as Transitional w/div.
     * @throws Exception any exception generated during the test
     */
    public void test431965() throws Exception
    {
        executeTidyTest("431965.xhtml");
    }

    /**
     * test for Tidy [432677] : Null value changed to "value" for -asxml.
     * @throws Exception any exception generated during the test
     */
    public void test432677() throws Exception
    {
        executeTidyTest("432677.html");
    }

    /**
     * test for Tidy [433012] : Illegal ampersands/character entities.
     * @throws Exception any exception generated during the test
     */
    public void test433012() throws Exception
    {
        executeTidyTest("433012.html");
    }

    /**
     * test for Tidy [433040] : Anchor tag without attributes deleted.
     * @throws Exception any exception generated during the test
     */
    public void test433040() throws Exception
    {
        executeTidyTest("433040.html");
    }

    /**
     * test for Tidy [433359] : Empty iframe elements trimmed.
     * @throws Exception any exception generated during the test
     */
    public void test433359() throws Exception
    {
        executeTidyTest("433359.html");
    }
    /**
     * test for Tidy [433360] : Tags with missing > can't be repaired.
     * @throws Exception any exception generated during the test
     */
    public void test433360() throws Exception
    {
        executeTidyTest("433360.html");
    }
    /**
     * test for Tidy [433604] : Tidy inserts &amp;nbsp; entity in -xml mode.
     * @throws Exception any exception generated during the test
     */
    public void test433604() throws Exception
    {
        executeTidyTest("433604.xml");
    }

    /**
     * test for Tidy [433656] : Improve support for PHP.
     * @throws Exception any exception generated during the test
     */
    public void test433656() throws Exception
    {
        executeTidyTest("433656.html");
    }

    /**
     * test for Tidy [433666] : Attempt to repair duplicate attributes. HIGH PRIORITY.
     * @throws Exception any exception generated during the test
     */
    public void test433666() throws Exception
    {
        executeTidyTest("433666.html");
    }

    /**
     * test for Tidy [433672] : Anchor enclosing Header tags is omitted.
     * @throws Exception any exception generated during the test
     */
    public void test433672() throws Exception
    {
        executeTidyTest("433672.html");
    }

    /**
     * test for Tidy [434940] : --show-body-only: print only body contents.
     * @throws Exception any exception generated during the test
     */
    public void test434940() throws Exception
    {
        executeTidyTest("434940.html");
    }

    /**
     * test for Tidy [435903] : Script element w/body child to table bug.
     * @throws Exception any exception generated during the test
     */
    public void test435903() throws Exception
    {
        executeTidyTest("435903.html");
    }

    /**
     * test for Tidy [435909] : &lt;noscript&gt;&lt;/noscript&gt; in &lt;head&gt;&lt;/head&gt;.
     * @throws Exception any exception generated during the test
     */
    public void test435909() throws Exception
    {
        executeTidyTest("435909.html");
    }

    /**
     * test for Tidy [435919] : Nested &lt;q&gt;&lt;/q&gt;'s not handled correctly.
     * @throws Exception any exception generated during the test
     */
    public void test435919() throws Exception
    {
        executeTidyTest("435919.html");
    }

    /**
     * test for Tidy [435920] : Space inserted before &lt;/td&gt; causes probs.
     * @throws Exception any exception generated during the test
     */
    public void test435920() throws Exception
    {
        executeTidyTest("435920.html");
    }

    /**
     * test for Tidy [435923] : Preserve case of attribute names.
     * @throws Exception any exception generated during the test
     */
    public void test435923() throws Exception
    {
        executeTidyTest("435923.html");
    }

    /**
     * test for Tidy [437468] : Test input file for iso-8859-1 character entities.
     * @throws Exception any exception generated during the test
     */
    public void test437468() throws Exception
    {
        executeTidyTest("437468.html");
    }
    /**
     * test for Tidy [438650] : Newline in URL attr value becomes space.
     * @throws Exception any exception generated during the test
     */
    public void test438650() throws Exception
    {
        executeTidyTest("438650.html");
    }

    /**
     * test for Tidy [438658] : Missing / in title endtag makes 2 titles.
     * @throws Exception any exception generated during the test
     */
    public void test438658() throws Exception
    {
        executeTidyTest("438658.html");
    }

    /**
     * test for Tidy [438954] : Body tag w/attributes omitted w/hide-end.
     * @throws Exception any exception generated during the test
     */
    public void test438954() throws Exception
    {
        executeTidyTest("438954.html");
    }

    /**
     * test for Tidy [441508] : parser.c: BadForm() function broken.
     * @throws Exception any exception generated during the test
     */
    public void test441508() throws Exception
    {
        executeTidyTest("441508.html");
    }

    /**
     * test for Tidy [441568] : Font tags handling different.
     * @throws Exception any exception generated during the test
     */
    public void test441568() throws Exception
    {
        executeTidyTest("441568.html");
    }

    /**
     * test for Tidy [441740] : XHTML 1.1 Support.
     * @throws Exception any exception generated during the test
     */
    public void test441740() throws Exception
    {
        executeTidyTest("441740.xhtml");
    }

    /**
     * test for Tidy [443381] : end tags for empty elements in XHTML.
     * @throws Exception any exception generated during the test
     */
    public void test443381() throws Exception
    {
        executeTidyTest("443381.xhtml");
    }

    /**
     * test for Tidy [443576] : End script tag inside scripts problem.
     * @throws Exception any exception generated during the test
     */
    public void test443576() throws Exception
    {
        executeTidyTest("443576.html");
    }

    /**
     * test for Tidy [443678] : Unclosed &lt;script&gt; in &lt;head&gt; messes Tidy.
     * @throws Exception any exception generated during the test
     */
    public void test443678() throws Exception
    {
        executeTidyTest("443678.html");
    }

    /**
     * test for Tidy [445074] : XHTML requires form method="post".
     * @throws Exception any exception generated during the test
     */
    public void test445074() throws Exception
    {
        executeTidyTest("445074.html");
    }

    /**
     * test for Tidy [445394] : Improve handling of missing trailing ".
     * @throws Exception any exception generated during the test
     */
    public void test445394() throws Exception
    {
        executeTidyTest("445394.html");
    }

    /**
     * test for Tidy [445557] : Convert Symbol font chars to Unicode.
     * @throws Exception any exception generated during the test
     */
    public void test445557() throws Exception
    {
        executeTidyTest("445557.html");
    }

    /**
     * test for Tidy [449348] : Whitespace added/removed to inline tags.
     * @throws Exception any exception generated during the test
     */
    public void test449348() throws Exception
    {
        executeTidyTest("449348.html");
    }

    /**
     * test for Tidy [456596] : Missing attribute name garbles output.
     * @throws Exception any exception generated during the test
     */
    public void test456596() throws Exception
    {
        executeTidyTest("456596.html");
    }

    /**
     * test for Tidy [467863] : un-nest &lt;a&gt;.
     * @throws Exception any exception generated during the test
     */
    public void test467863() throws Exception
    {
        executeTidyTest("467863.html");
    }

    /**
     * test for Tidy [467865] : un-nesting is incorrect.
     * @throws Exception any exception generated during the test
     */
    public void test467865() throws Exception
    {
        executeTidyTest("467865.html");
    }

    /**
     * test for Tidy [470663] : Unterminated tags in &lt;head>confuse pars.
     * @throws Exception any exception generated during the test
     */
    public void test470663() throws Exception
    {
        executeTidyTest("470663.html");
    }

    /**
     * test for Tidy [470688] : doesn't cleanup badly nested tags right.
     * @throws Exception any exception generated during the test
     */
    public void test470688() throws Exception
    {
        executeTidyTest("470688.html");
    }

    /**
     * test for Tidy [471264] : Reduce blank lines in output. HIGH PRIORITY (needed for comparing files in tests).
     * @throws Exception any exception generated during the test
     */
    public void test471264() throws Exception
    {
        executeTidyTest("471264.html");
    }

    /**
     * test for Tidy [473490] : DOCTYPE for Proprietary HTML to XHTML bad.
     * @throws Exception any exception generated during the test
     */
    public void test473490() throws Exception
    {
        executeTidyTest("473490.html");
    }

    /**
     * test for Tidy [480406] : Single document element discarded.
     * @throws Exception any exception generated during the test
     */
    public void test480406() throws Exception
    {
        executeTidyTest("480406.xml");
    }

    /**
     * test for Tidy [480701] : -xml conflicts with -output-xhtml.
     * @throws Exception any exception generated during the test
     */
    public void test480701() throws Exception
    {
        executeTidyTest("480701.xml");
    }

    /**
     * test for Tidy [480843] : Proposed change to FixID().
     * @throws Exception any exception generated during the test
     */
    public void test480843() throws Exception
    {
        // still open in tidy
        executeTidyTest("480843.xhtml");
    }

    /**
     * test for Tidy [487204] : Duplicate DIV style attribute generated. HIGH PRIORITY.
     * @throws Exception any exception generated during the test
     */
    public void test487204() throws Exception
    {
        executeTidyTest("487204.html");
    }

    /**
     * test for Tidy [487283] : &gt;/select&lt; does not terminate &gt;option&lt;.
     * @throws Exception any exception generated during the test
     */
    public void test487283() throws Exception
    {
        executeTidyTest("487283.html");
    }

    /**
     * test for Tidy [500236] : Test case for MS Access files failing with Error: missing quote mark for attribute
     * value.
     * @throws Exception any exception generated during the test
     */
    public void test500236() throws Exception
    {
        executeTidyTest("500236.xml");
    }

    /**
     * test for Tidy [503436] : Detect duplicate attribute specifications in XML docs. HIGH PRIORITY.
     * @throws Exception any exception generated during the test
     */
    public void test503436() throws Exception
    {
        executeTidyTest("503436.xml");
    }

    /**
     * test for Tidy [504206] : Tidy errors in processing forms.
     * @throws Exception any exception generated during the test
     */
    public void test504206() throws Exception
    {
        executeTidyTest("504206.html");
    }

    /**
     * test for Tidy [505770] : Unclosed %lt;option> tag causing problems.
     * @throws Exception any exception generated during the test
     */
    public void test505770() throws Exception
    {
        executeTidyTest("505770.html");
    }

    /**
     * test for Tidy [508936] : Parse CSS Selector prefix in config file.
     * @throws Exception any exception generated during the test
     */
    public void test508936() throws Exception
    {
        executeTidyTest("508936.html");
    }

    /**
     * test for Tidy [511243] : xhtml utf8 format bug.
     * @throws Exception any exception generated during the test
     */
    public void test511243() throws Exception
    {
        // need wrapping
        executeTidyTest("511243.xhtml");
    }

    /**
     * test for Tidy [511679] : No end tag for PRE.
     * @throws Exception any exception generated during the test
     */
    public void test511679() throws Exception
    {
        executeTidyTest("511679.html");
    }

    /**
     * test for Tidy [514348] : Incorrect wrap behaviour. HIGH PRIORITY (needed for comparison)
     * @throws Exception any exception generated during the test
     */
    public void test514348() throws Exception
    {
        executeTidyTest("514348.html");
    }

    /**
     * test for Tidy [514893] : Incorrect http-equiv &lt;meta&gt; tag.
     * @throws Exception any exception generated during the test
     */
    public void test514893() throws Exception
    {
        executeTidyTest("514893.html");
    }

    /**
     * test for Tidy [517550] : parser misinterprets ?xml-stylesheet PI.
     * @throws Exception any exception generated during the test
     */
    public void test517550() throws Exception
    {
        executeTidyTest("517550.xhtml");
    }

    /**
     * test for Tidy [531964] : &lt;p /&gt; gets tidied into &lt;p /&gt;&lt;/p&gt;.
     * @throws Exception any exception generated during the test
     */
    public void test531964() throws Exception
    {
        executeTidyTest("531964.xhtml");
    }

    /**
     * test for Tidy [533105] : Tidy confused: HTML in VBScript.
     * @throws Exception any exception generated during the test @todo complete test
     */
    public void test533105() throws Exception
    {
        throw new Error("Test not yet implemented");
        // no output in tidy
        //executeTidyTest("533105.html");
    }

    /**
     * test for Tidy [533233] : Extra newline around inline &lt;script>.
     * @throws Exception any exception generated during the test
     */
    public void test533233() throws Exception
    {
        executeTidyTest("533233.html");
    }

    /**
     * test for Tidy [537604] : Expansion of entity references in -xml.
     * @throws Exception any exception generated during the test
     */
    public void test537604() throws Exception
    {
        executeTidyTest("537604.xml");
    }

    /**
     * test for Tidy [540045] : Tidy strips all the IMG tags out.
     * @throws Exception any exception generated during the test
     */
    public void test540045() throws Exception
    {
        executeTidyTest("540045.xhtml");
    }

    /**
     * test for Tidy [540555] : Empty title tag is trimmed.
     * @throws Exception any exception generated during the test
     */
    public void test540555() throws Exception
    {
        executeTidyTest("540555.html");
    }

    /**
     * test for Tidy [540571] : Inconsistent behaviour with span inline element.
     * @throws Exception any exception generated during the test
     */
    public void test540571() throws Exception
    {
        executeTidyTest("540571.html");
    }

    /**
     * test for Tidy [545067] : Implicit closing of head broken.
     * @throws Exception any exception generated during the test @todo complete test
     */
    public void test545067() throws Exception
    {
        // no output in tidy
        throw new Error("Test not yet implemented");
        //executeTidyTest("545067.html");
    }

    /**
     * test for Tidy [552861] : &lt;td with=""> not recognized.
     * @throws Exception any exception generated during the test
     */
    public void test552861() throws Exception
    {
        executeTidyTest("552861.html");
    }

    /**
     * test for Tidy [578216] : Incorrect indent of &lt;SPAN&gt; elements.
     * @throws Exception any exception generated during the test
     */
    public void test578216() throws Exception
    {
        executeTidyTest("578216.html");
    }

    /**
     * test for Tidy [586555] : Misplaced backslash caused by newline.
     * @throws Exception any exception generated during the test
     */
    public void test586555() throws Exception
    {
        executeTidyTest("586555.html");
    }

    /**
     * test for Tidy [586562] : Two Doctypes.
     * @throws Exception any exception generated during the test
     */
    public void test586562() throws Exception
    {
        executeTidyTest("586562.html");
    }

    /**
     * test for Tidy [593705] : Use of &lt; comparison symbol confuses Tidy.
     * @throws Exception any exception generated during the test
     */
    public void test593705() throws Exception
    {
        executeTidyTest("593705.html");
    }

    /**
     * test for Tidy [598860] : Script parsing fails with quote chars.
     * @throws Exception any exception generated during the test
     */
    public void test598860() throws Exception
    {
        executeTidyTest("598860.html");
    }

    /**
     * test for Tidy [603128] : tidy adds newlines after &lt;/html&gt;.
     * @throws Exception any exception generated during the test
     */
    public void test603128() throws Exception
    {
        executeTidyTest("603128.html");
    }

    /**
     * test for Tidy [616744] : Incorrect line break after closing tag.
     * @throws Exception any exception generated during the test
     */
    public void test616744() throws Exception
    {
        executeTidyTest("616744.xml");
    }

    /**
     * test for Tidy [620531] : br in pre must not cause line break.
     * @throws Exception any exception generated during the test
     */
    public void test620531() throws Exception
    {
        executeTidyTest("620531.html");
    }

    /**
     * test for Tidy [629885] : Unbalanced quote in CSS Scrambles Doc.
     * @throws Exception any exception generated during the test
     */
    public void test629885() throws Exception
    {
        executeTidyTest("629885.html");
    }
    /**
     * test for Tidy [640473] : new-empty-tags doesn't work, breaks doc.
     * @throws Exception any exception generated during the test
     */
    public void test640473() throws Exception
    {
        executeTidyTest("640473.html");
    }

    /**
     * test for Tidy [640474] : XML decl, attributes in wrong order.
     * @throws Exception any exception generated during the test
     */
    public void test640474() throws Exception
    {
        executeTidyTest("640474.xml");
    }

    /**
     * test for Tidy [646946] : Bad doctype guessing in XML mode.
     * @throws Exception any exception generated during the test
     */
    public void test646946() throws Exception
    {
        executeTidyTest("646946.xml");
    }
    /**
     * test for Tidy [647900] : tables are incorrectly merged.
     * @throws Exception any exception generated during the test
     */
    public void test647900() throws Exception
    {
        executeTidyTest("647900.html");
    }

    /**
     * test for Tidy [649812] : Does TidyLib correctly handle Mac files?.
     * @throws Exception any exception generated during the test @todo complete test
     */
    public void test649812() throws Exception
    {
        throw new Error("Test not yet implemented");
        //executeTidyTest("649812.html");
    }

    /**
     * test for Tidy [655338] : Tidy leaves XML decl in wrong place.
     * @throws Exception any exception generated during the test
     */
    public void test655338() throws Exception
    {
        executeTidyTest("655338.html");
    }

    /**
     * test for Tidy [656889] : textarea text and line wrapping.
     * @throws Exception any exception generated during the test
     */
    public void test656889() throws Exception
    {
        executeTidyTest("656889.html");
    }

    /**
     * test for Tidy [663548] : Javascript and Tidy - missing code.
     * @throws Exception any exception generated during the test
     */
    public void test663548() throws Exception
    {
        executeTidyTest("663548.html");
    }

    /**
     * test for Tidy [678268] : --output-xhtml produces bad xml. HIGH PRIORITY.
     * @throws Exception any exception generated during the test
     */
    public void test678268() throws Exception
    {
        executeTidyTest("678268.html");
    }

    /**
     * test for Tidy [680664] : Malformed comment generates bad (X)HTML.
     * @throws Exception any exception generated during the test
     */
    public void test680664() throws Exception
    {
        executeTidyTest("680664.xhtml");
    }

    /**
     * test for Tidy [695408] : Empty spans getting dropped, even if they have attrs.
     * @throws Exception any exception generated during the test
     */
    public void test695408() throws Exception
    {
        executeTidyTest("695408.html");
    }

}
