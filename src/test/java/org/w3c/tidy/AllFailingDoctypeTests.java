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
 * Testsuite for all the test cases failing with a bad dtd.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public final class AllFailingDoctypeTests
{

    /**
     * Should not be instantiated.
     */
    private AllFailingDoctypeTests()
    {
    }

    /**
     * Generates the suite.
     * @return junit test suite.
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("All the tests failing because of a wrong doctype");

        suite.addTest(new JTidyBugsTest("test763186"));

        suite.addTest(new TidyOutputBugsTest("test427633"));
        suite.addTest(new TidyOutputBugsTest("test427820"));
        suite.addTest(new TidyOutputBugsTest("test427822"));
        suite.addTest(new TidyOutputBugsTest("test427830"));
        suite.addTest(new TidyOutputBugsTest("test427833"));
        suite.addTest(new TidyOutputBugsTest("test431739"));
        suite.addTest(new TidyOutputBugsTest("test431958"));
        suite.addTest(new TidyOutputBugsTest("test433012"));
        suite.addTest(new TidyOutputBugsTest("test437468"));
        suite.addTest(new TidyOutputBugsTest("test441508"));
        suite.addTest(new TidyOutputBugsTest("test445557"));
        suite.addTest(new TidyOutputBugsTest("test470663"));
        suite.addTest(new TidyOutputBugsTest("test502346"));
        suite.addTest(new TidyOutputBugsTest("test504206"));
        suite.addTest(new TidyOutputBugsTest("test508936"));
        suite.addTest(new TidyOutputBugsTest("test514893"));
        suite.addTest(new TidyOutputBugsTest("test540555"));
        suite.addTest(new TidyOutputBugsTest("test540571"));
        suite.addTest(new TidyOutputBugsTest("test578216"));
        suite.addTest(new TidyOutputBugsTest("test586562"));
        suite.addTest(new TidyOutputBugsTest("test646946"));
        suite.addTest(new TidyOutputBugsTest("test647900"));
        suite.addTest(new TidyOutputBugsTest("test655338"));
        suite.addTest(new TidyOutputBugsTest("test656889"));
        suite.addTest(new TidyOutputBugsTest("test765852"));
        suite.addTest(new TidyOutputBugsTest("test994841"));
        suite.addTest(new TidyWarningBugsTest("test516370"));
        suite.addTest(new TidyWarningBugsTest("test552861"));

        return suite;
    }

}