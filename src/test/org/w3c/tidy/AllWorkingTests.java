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

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Testsuite for all the test cases actually working in JTidy. Must be used to assure there are no regressions after a
 * change.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public final class AllWorkingTests
{

    /**
     * Should not be instantiated.
     */
    private AllWorkingTests()
    {
    }

    /**
     * Generates the suite.
     * @return junit test suite.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("All the working test cases for JTidy");
        suite.addTestSuite(TidyCrashingBugsTest.class);
        suite.addTestSuite(TidyBugsTest.class);
        suite.addTestSuite(JTidyParserBugsTest.class);
        suite.addTestSuite(JTidyWarningBugsTest.class);

        suite.addTest(new JTidyBugsTest("test475643"));
        suite.addTest(new JTidyBugsTest("test527118"));
        suite.addTest(new JTidyBugsTest("test538727"));
        suite.addTest(new JTidyBugsTest("test547976"));
        suite.addTest(new JTidyBugsTest("test610244"));
        suite.addTest(new JTidyBugsTest("test648768"));
        suite.addTest(new JTidyBugsTest("test663197"));
        suite.addTest(new JTidyBugsTest("test791933"));
        suite.addTest(new JTidyBugsTest("test929936"));

        suite.addTest(new TidyOutputBugsTest("test427812"));
        suite.addTest(new TidyOutputBugsTest("test427662"));
        suite.addTest(new TidyOutputBugsTest("test427677"));
        suite.addTest(new TidyOutputBugsTest("test427819"));
        suite.addTest(new TidyOutputBugsTest("test427821"));
        suite.addTest(new TidyOutputBugsTest("test427823"));
        suite.addTest(new TidyOutputBugsTest("test427825"));
        suite.addTest(new TidyOutputBugsTest("test427835"));
        suite.addTest(new TidyOutputBugsTest("test427837"));
        suite.addTest(new TidyOutputBugsTest("test427839"));
        suite.addTest(new TidyOutputBugsTest("test427846"));
        suite.addTest(new TidyOutputBugsTest("test431731"));
        suite.addTest(new TidyOutputBugsTest("test431889"));
        suite.addTest(new TidyOutputBugsTest("test431965"));
        suite.addTest(new TidyOutputBugsTest("test431736"));
        suite.addTest(new TidyOutputBugsTest("test432677"));
        suite.addTest(new TidyOutputBugsTest("test433040"));
        suite.addTest(new TidyOutputBugsTest("test433359"));
        suite.addTest(new TidyOutputBugsTest("test433604"));
        suite.addTest(new TidyOutputBugsTest("test433656"));
        suite.addTest(new TidyOutputBugsTest("test433666"));
        suite.addTest(new TidyOutputBugsTest("test433672"));
        suite.addTest(new TidyOutputBugsTest("test434940"));
        suite.addTest(new TidyOutputBugsTest("test434940b"));
        suite.addTest(new TidyOutputBugsTest("test435919"));
        suite.addTest(new TidyOutputBugsTest("test438650"));
        suite.addTest(new TidyOutputBugsTest("test438658"));
        suite.addTest(new TidyOutputBugsTest("test441568"));
        suite.addTest(new TidyOutputBugsTest("test441740"));
        suite.addTest(new TidyOutputBugsTest("test443381"));
        suite.addTest(new TidyOutputBugsTest("test445074"));
        suite.addTest(new TidyOutputBugsTest("test470688"));
        suite.addTest(new TidyOutputBugsTest("test471264"));
        suite.addTest(new TidyOutputBugsTest("test473490"));
        suite.addTest(new TidyOutputBugsTest("test456596"));
        suite.addTest(new TidyOutputBugsTest("test480406"));
        suite.addTest(new TidyOutputBugsTest("test480701"));
        suite.addTest(new TidyOutputBugsTest("test487204"));
        suite.addTest(new TidyOutputBugsTest("test487283"));
        suite.addTest(new TidyOutputBugsTest("test500236"));
        suite.addTest(new TidyOutputBugsTest("test502348"));
        suite.addTest(new TidyOutputBugsTest("test517550"));
        suite.addTest(new TidyOutputBugsTest("test603128"));
        suite.addTest(new TidyOutputBugsTest("test616744"));
        suite.addTest(new TidyOutputBugsTest("test996484"));

        suite.addTest(new TidyWarningBugsTest("test427810"));
        suite.addTest(new TidyWarningBugsTest("test431874"));
        suite.addTest(new TidyWarningBugsTest("test427827"));
        suite.addTest(new TidyWarningBugsTest("test427834"));
        suite.addTest(new TidyWarningBugsTest("test427844"));
        suite.addTest(new TidyWarningBugsTest("test431719"));
        suite.addTest(new TidyWarningBugsTest("test431883"));
        suite.addTest(new TidyWarningBugsTest("test431956"));
        suite.addTest(new TidyWarningBugsTest("test433021"));
        suite.addTest(new TidyWarningBugsTest("test434100"));
        suite.addTest(new TidyWarningBugsTest("test435917"));
        suite.addTest(new TidyWarningBugsTest("test435917b"));
        suite.addTest(new TidyWarningBugsTest("test438956"));
        suite.addTest(new TidyWarningBugsTest("test450389"));
        suite.addTest(new TidyWarningBugsTest("test501230"));
        suite.addTest(new TidyWarningBugsTest("test501669"));
        suite.addTest(new TidyWarningBugsTest("test516370"));
        suite.addTest(new TidyWarningBugsTest("test517528"));
        suite.addTest(new TidyWarningBugsTest("test525081"));
        suite.addTest(new TidyWarningBugsTest("test538536"));
        suite.addTest(new TidyWarningBugsTest("test545067"));
        suite.addTest(new TidyWarningBugsTest("test553468"));
        suite.addTest(new TidyWarningBugsTest("test706260"));
        return suite;
    }

}