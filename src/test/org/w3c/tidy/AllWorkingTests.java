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

        suite.addTest(new JTidyBugsTest("test538727"));
        suite.addTest(new JTidyBugsTest("test610244"));
        suite.addTest(new JTidyBugsTest("test663197"));
        suite.addTest(new JTidyBugsTest("test929936"));

        suite.addTest(new TidyOutputBugsTest("test427812"));
        suite.addTest(new TidyOutputBugsTest("test427662"));
        suite.addTest(new TidyOutputBugsTest("test427677"));
        suite.addTest(new TidyOutputBugsTest("test427819"));
        suite.addTest(new TidyOutputBugsTest("test427825"));
        suite.addTest(new TidyOutputBugsTest("test427835"));
        suite.addTest(new TidyOutputBugsTest("test427837"));
        suite.addTest(new TidyOutputBugsTest("test427839"));
        suite.addTest(new TidyOutputBugsTest("test431889"));
        suite.addTest(new TidyOutputBugsTest("test431958"));
        suite.addTest(new TidyOutputBugsTest("test432677"));
        suite.addTest(new TidyOutputBugsTest("test433040"));
        suite.addTest(new TidyOutputBugsTest("test433359"));
        suite.addTest(new TidyOutputBugsTest("test433604"));
        suite.addTest(new TidyOutputBugsTest("test433656"));
        suite.addTest(new TidyOutputBugsTest("test433672"));
        suite.addTest(new TidyOutputBugsTest("test434940"));
        suite.addTest(new TidyOutputBugsTest("test434940b"));
        suite.addTest(new TidyOutputBugsTest("test435919"));
        suite.addTest(new TidyOutputBugsTest("test438650"));
        suite.addTest(new TidyOutputBugsTest("test438658"));
        suite.addTest(new TidyOutputBugsTest("test441568"));
        suite.addTest(new TidyOutputBugsTest("test470688"));
        suite.addTest(new TidyOutputBugsTest("test471264"));
        suite.addTest(new TidyOutputBugsTest("test456596"));
        suite.addTest(new TidyOutputBugsTest("test480406"));
        suite.addTest(new TidyOutputBugsTest("test487204"));
        suite.addTest(new TidyOutputBugsTest("test603128"));
        suite.addTest(new TidyOutputBugsTest("test616744"));

        suite.addTest(new TidyWarningBugsTest("test427810"));
        suite.addTest(new TidyWarningBugsTest("test431874"));
        suite.addTest(new TidyWarningBugsTest("test427827"));
        suite.addTest(new TidyWarningBugsTest("test427834"));
        suite.addTest(new TidyWarningBugsTest("test427844"));
        suite.addTest(new TidyWarningBugsTest("test431719"));
        suite.addTest(new TidyWarningBugsTest("test431883"));
        suite.addTest(new TidyWarningBugsTest("test431956"));
        suite.addTest(new TidyWarningBugsTest("test433021"));
        suite.addTest(new TidyWarningBugsTest("test435917"));
        suite.addTest(new TidyWarningBugsTest("test435917b"));
        suite.addTest(new TidyWarningBugsTest("test450389"));
        suite.addTest(new TidyWarningBugsTest("test501230"));
        suite.addTest(new TidyWarningBugsTest("test545067"));
        suite.addTest(new TidyWarningBugsTest("test553468"));
        suite.addTest(new TidyWarningBugsTest("test706260"));
        return suite;
    }

}