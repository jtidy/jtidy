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
 * Testsuite for all the test cases actually NOT working in JTidy.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public final class AllNotWorkingTests
{

    /**
     * Should not be instantiated.
     */
    private AllNotWorkingTests()
    {
        // unused
    }

    /**
     * Generates the suite.
     * @return junit test suite.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("All the NOT working test cases for JTidy");

        suite.addTest(new JTidyBugsTest("test508245"));
        suite.addTest(new JTidyBugsTest("test531962"));
        suite.addTest(new JTidyBugsTest("test574158"));
        suite.addTest(new JTidyBugsTest("test763191"));
        suite.addTest(new JTidyBugsTest("test763186"));
        suite.addTest(new JTidyBugsTest("test917012"));
        suite.addTest(new JTidyBugsTest("test922302"));
        suite.addTest(new JTidyBugsTest("test943559"));
        suite.addTest(new JTidyBugsTest("test935796"));

        suite.addTest(new TidyEncodingBugsTest("test647255"));
        suite.addTest(new TidyEncodingBugsTest("test649812"));
        suite.addTest(new TidyEncodingBugsTest("test658230"));
        suite.addTest(new TidyEncodingBugsTest("test660397"));
        suite.addTest(new TidyEncodingBugsTest("test688746"));

        suite.addTest(new TidyOutputBugsTest("test427633"));
        suite.addTest(new TidyOutputBugsTest("test427820"));
        suite.addTest(new TidyOutputBugsTest("test427822"));
        suite.addTest(new TidyOutputBugsTest("test427826"));
        suite.addTest(new TidyOutputBugsTest("test427830"));
        suite.addTest(new TidyOutputBugsTest("test427833"));
        suite.addTest(new TidyOutputBugsTest("test427836"));
        suite.addTest(new TidyOutputBugsTest("test427838"));
        suite.addTest(new TidyOutputBugsTest("test427845"));
        suite.addTest(new TidyOutputBugsTest("test431739"));
        suite.addTest(new TidyOutputBugsTest("test431898"));
        suite.addTest(new TidyOutputBugsTest("test431958"));
        suite.addTest(new TidyOutputBugsTest("test433012"));
        suite.addTest(new TidyOutputBugsTest("test433360"));
        suite.addTest(new TidyOutputBugsTest("test435903"));
        suite.addTest(new TidyOutputBugsTest("test435909"));
        suite.addTest(new TidyOutputBugsTest("test435920"));
        suite.addTest(new TidyOutputBugsTest("test435923"));
        suite.addTest(new TidyOutputBugsTest("test437468"));
        suite.addTest(new TidyOutputBugsTest("test438954"));
        suite.addTest(new TidyOutputBugsTest("test441508"));
        suite.addTest(new TidyOutputBugsTest("test443576"));
        suite.addTest(new TidyOutputBugsTest("test443678"));
        suite.addTest(new TidyOutputBugsTest("test445394"));
        suite.addTest(new TidyOutputBugsTest("test445557"));
        suite.addTest(new TidyOutputBugsTest("test449348"));
        suite.addTest(new TidyOutputBugsTest("test467863"));
        suite.addTest(new TidyOutputBugsTest("test467865"));
        suite.addTest(new TidyOutputBugsTest("test470663"));
        suite.addTest(new TidyOutputBugsTest("test480843"));
        suite.addTest(new TidyOutputBugsTest("test502346"));
        suite.addTest(new TidyOutputBugsTest("test503436"));
        suite.addTest(new TidyOutputBugsTest("test504206"));
        suite.addTest(new TidyOutputBugsTest("test505770"));
        suite.addTest(new TidyOutputBugsTest("test508936"));
        suite.addTest(new TidyOutputBugsTest("test511243"));
        suite.addTest(new TidyOutputBugsTest("test511679"));
        suite.addTest(new TidyOutputBugsTest("test514348"));
        suite.addTest(new TidyOutputBugsTest("test514893"));

        suite.addTest(new TidyOutputBugsTest("test533105"));
        suite.addTest(new TidyOutputBugsTest("test533233"));
        suite.addTest(new TidyOutputBugsTest("test537604"));
        suite.addTest(new TidyOutputBugsTest("test540045"));
        suite.addTest(new TidyOutputBugsTest("test540555"));
        suite.addTest(new TidyOutputBugsTest("test540571"));
        suite.addTest(new TidyOutputBugsTest("test578216"));
        suite.addTest(new TidyOutputBugsTest("test586555"));
        suite.addTest(new TidyOutputBugsTest("test586562"));
        suite.addTest(new TidyOutputBugsTest("test593705"));
        suite.addTest(new TidyOutputBugsTest("test598860"));
        suite.addTest(new TidyOutputBugsTest("test620531"));
        suite.addTest(new TidyOutputBugsTest("test629885"));
        suite.addTest(new TidyOutputBugsTest("test640473"));
        suite.addTest(new TidyOutputBugsTest("test640474"));
        suite.addTest(new TidyOutputBugsTest("test646946"));
        suite.addTest(new TidyOutputBugsTest("test647900"));
        suite.addTest(new TidyOutputBugsTest("test655338"));
        suite.addTest(new TidyOutputBugsTest("test656889"));
        suite.addTest(new TidyOutputBugsTest("test663548"));
        suite.addTest(new TidyOutputBugsTest("test678268"));
        suite.addTest(new TidyOutputBugsTest("test680664"));
        suite.addTest(new TidyOutputBugsTest("test695408"));
        suite.addTest(new TidyOutputBugsTest("test708322"));
        suite.addTest(new TidyOutputBugsTest("test735603"));
        suite.addTest(new TidyOutputBugsTest("test765852"));
        suite.addTest(new TidyOutputBugsTest("test994841"));

        suite.addTest(new TidyWarningBugsTest("test431964"));
        suite.addTest(new TidyWarningBugsTest("test433607"));
        suite.addTest(new TidyWarningBugsTest("test433670"));
        suite.addTest(new TidyWarningBugsTest("test434047"));
        suite.addTest(new TidyWarningBugsTest("test435922"));
        suite.addTest(new TidyWarningBugsTest("test446019"));
        suite.addTest(new TidyWarningBugsTest("test552861"));
        suite.addTest(new TidyWarningBugsTest("test553414"));

        suite.addTest(new TidyWordBugsTest("test431721"));
        suite.addTest(new TidyWordBugsTest("test444394"));
        suite.addTest(new TidyWordBugsTest("test463066"));
        suite.addTest(new TidyWordBugsTest("test634889"));

        // previously working
        suite.addTest(new TidyOutputBugsTest("test531964"));

        return suite;
    }

}