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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;


/**
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class TidyTestCase extends TestCase
{

    /**
     * Tidy executable name, if you want to produce output files for comparison.
     */
    private static final String TIDY_EXECUTABLE = "tidy.exe";

    /**
     * Logger used to enable/disable output file generation using tidy c executable. Setting this logger to
     * <code>debug</code> in your log4j configuration file will cause the TIDY_EXECUTABLE to be run against the actual
     * test file. If set to false the command line used to manually run tidy will appear in the log.
     */
    private static final Log RUN_TIDY_EXECUTABLE = LogFactory.getLog("runtidy");

    /**
     * Tidy test instance.
     */
    protected Tidy tidy;

    /**
     * Error out.
     */
    protected StringWriter errorLog;

    /**
     * Tidy output.
     */
    protected String tidyOut;

    /**
     * logger.
     */
    private Log log = LogFactory.getLog(TidyTestCase.class);

    /**
     * write directly to out. Useful for debugging (but it will make the test fail!).
     */
    private boolean writeToOut;

    /**
     * Instantiate a new Test case.
     * @param name test name
     */
    public TidyTestCase(String name)
    {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        //creates a new Tidy
        this.tidy = new Tidy();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        this.tidy = null;
        this.errorLog = null;
        this.tidyOut = null;

        super.tearDown();
    }

    /**
     * Executes a tidy test. This method simply requires the input file name. If a file with the same name but with a
     * ".cfg" extension is found is used as configuration file for the test, otherwise the default config will be used.
     * If a file with the same name, but with the ".out" extension is found, tidy will the result with the content of
     * such file.
     * @param fileName input file name
     * @throws Exception any exception generated during the test
     */
    protected void executeTidyTest(String fileName) throws Exception
    {

        // set up Tidy using supplied configuration
        setUpTidy(fileName);

        // input file
        URL inputURL = getClass().getClassLoader().getResource(fileName);
        assertNotNull("Can't find input file [" + fileName + "]", inputURL);

        OutputStream out;
        // out
        if (!writeToOut)
        {
            out = new ByteArrayOutputStream();
        }
        else
        {
            out = System.out;
        }

        // go!
        this.tidy.parse(inputURL.openStream(), out);

        if (log.isDebugEnabled())
        {
            log.debug("out:\n---- out ----\n" + out + "\n---- out ----");
            log.debug("log:\n---- log ----\n" + this.errorLog + "\n---- log ----");
        }

        // existing file for comparison
        String outFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".out";
        URL outFile = getClass().getClassLoader().getResource(outFileName);

        this.tidyOut = out.toString();

        if (outFile != null)
        {
            log.debug("Comparing file using [" + outFileName + "]");
            assertEquals(this.tidyOut, outFile);
        }
    }

    /**
     * Basic test for DOM parser. Test is set up using [fileName.cfg] configuration if the file exists. Calls
     * tidy.parseDOM and returns the Document to the caller.
     * @param fileName input file name
     * @return parsed Document
     * @throws Exception any exception generated during the test
     */
    protected Document parseDomTest(String fileName) throws Exception
    {
        //creates a new Tidy
        setUpTidy(fileName);

        // input file
        URL inputURL = getClass().getClassLoader().getResource(fileName);
        assertNotNull("Can't find input file [" + fileName + "]", inputURL);

        // out
        OutputStream out = new ByteArrayOutputStream();

        Document doc = this.tidy.parseDOM(inputURL.openStream(), out);
        this.tidyOut = out.toString();

        return doc;
    }

    /**
     * assert generated output and test file are equals.
     * @param tidyOutput tidy output as string
     * @param correctFile URL used to load the file for comparison
     * @throws FileNotFoundException if test file is not found
     * @throws IOException in reading file
     */
    protected void assertEquals(String tidyOutput, URL correctFile) throws FileNotFoundException, IOException
    {
        // assume the expected output has the same encoding tidy has in its configuration
        String encodingName = ParsePropertyImpl.CHAR_ENCODING.getFriendlyName("out-encoding", new Integer(tidy
            .getConfiguration().outCharEncoding), tidy.getConfiguration());

        diff(
            new BufferedReader((new InputStreamReader(new ByteArrayInputStream(tidyOutput.getBytes()), encodingName))),
            new BufferedReader(new InputStreamReader(new FileInputStream(correctFile.getPath()), encodingName)));
    }

    /**
     * Utility method: assert no warnings were reported in the last tidy run.
     */
    protected void assertNoWarnings()
    {
        int warningNum = this.tidy.getParseWarnings();
        if (warningNum != 0)
        {
            fail("Test failed, [" + warningNum + "] false warnings were reported");
        }
    }

    /**
     * Utility method: assert no errors were reported in the last tidy run.
     */
    protected void assertNoErrors()
    {
        int errorNum = this.tidy.getParseErrors();
        if (errorNum != 0)
        {
            fail("Test failed, [" + errorNum + "] false errors were reported");
        }
    }

    /**
     * Utility method: assert no warnings were reported in the last tidy run.
     * @param expectedNumber expected number of warnings.
     */
    protected void assertWarnings(int expectedNumber)
    {
        int warningNum = this.tidy.getParseWarnings();
        if (warningNum != expectedNumber)
        {
            fail("Test failed, [" + expectedNumber + "] warnings expected, [" + warningNum + "] were reported");
        }
    }

    /**
     * Utility method: assert no errors were reported in the last tidy run.
     * @param expectedNumber expected number of errors.
     */
    protected void assertErrors(int expectedNumber)
    {
        int errorNum = this.tidy.getParseErrors();
        if (errorNum != expectedNumber)
        {
            fail("Test failed, [" + expectedNumber + "] errors expected, [" + errorNum + "] were reported");
        }
    }

    /**
     * Utility method: asserts a given String can be found in the error log.
     * @param expectedString expected String in error log.
     */
    protected void assertLogContains(String expectedString)
    {
        String logString = this.errorLog.toString();

        if (logString.indexOf(expectedString) == -1)
        {
            fail("Test failed, expected [" + expectedString + "] couldn't be found in error log.");
        }
    }

    /**
     * Utility method: asserts a given String can't be found in the error log.
     * @param expectedString expected String in error log.
     */
    protected void assertLogDoesntContains(String expectedString)
    {
        String logString = this.errorLog.toString();

        if (logString.indexOf(expectedString) != -1)
        {
            fail("Test failed, [" + expectedString + "] was found in error log.");
        }
    }

    /**
     * set up the tidy instance.
     * @param fileName input file name (needed to determine configuration file name)
     * @throws IOException in reading configuration file
     */
    private void setUpTidy(String fileName) throws IOException
    {
        // config file names
        String configFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".cfg";

        // input file
        URL inputURL = getClass().getClassLoader().getResource(fileName);
        assertNotNull("Can't find input file [" + fileName + "]", inputURL);

        // configuration file
        URL configurationFile = getClass().getClassLoader().getResource(configFileName);

        // debug runing test info
        if (log.isDebugEnabled())
        {
            StringBuffer message = new StringBuffer();
            message.append("Testing [" + fileName + "]");
            if (configurationFile != null)
            {
                message.append(" using configuration file [" + configFileName + "]");
            }
            log.debug(message.toString());
        }

        if (configurationFile == null)
        {
            configurationFile = getClass().getClassLoader().getResource("default.cfg");
        }

        generateOutputUsingTidyC(inputURL.getFile(), configurationFile.getFile(), RUN_TIDY_EXECUTABLE.isDebugEnabled());

        // if configuration file exists load and set it
        Properties testProperties = new Properties();
        testProperties.load(configurationFile.openStream());
        this.tidy.setConfigurationFromProps(testProperties);

        // set up error log
        this.errorLog = new StringWriter();
        this.tidy.setErrout(new PrintWriter(this.errorLog));
    }

    /**
     * Diff between two buffered readers. If comparison fails an AssertionFailedException is thrown with the line
     * number, actual and expected output. Content is tested to be identical (same wrapping).
     * @param tidyOutput reader for tidy generated output
     * @param correctFile reader for test file
     * @throws IOException in reading from readers
     */
    private static void diff(BufferedReader tidyOutput, BufferedReader correctFile) throws IOException
    {
        String tidyLine, testLine;
        int i = 1;
        do
        {
            tidyLine = tidyOutput.readLine();
            testLine = correctFile.readLine();
            i++;
        }
        while ((tidyLine != null) && (testLine != null) && (tidyLine.equals(testLine)));
        tidyOutput.close();
        correctFile.close();

        if ((tidyLine != null) || (testLine != null))
        {
            fail("Wrong output, file comparison failed at line ["
                + (i - 1)
                + "]:\n"
                + "[tidy]["
                + tidyLine
                + "]\n"
                + "[test]["
                + testLine
                + "]");
        }
        return;
    }

    /**
     * Run TIDY_EXECUTABLE to produce an output file. Used to generates output files using tidy c for comparison with
     * jtidy. A file ".out" will be written in the same folder of the input file.
     * @param inputFileName input file for tidy.
     * @param configurationFileName configuration file name (default if there is no not test-specific file).
     * @param runIt if true the output is generated using tidy, if false simply output the command line.
     */
    private void generateOutputUsingTidyC(String inputFileName, String configurationFileName, boolean runIt)
    {

        String outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + ".out";

        String strCmd = TIDY_EXECUTABLE
            + " -config \""
            + cleanUpFilePath(configurationFileName)
            + "\" -o \""
            + cleanUpFilePath(outputFileName)
            + "\" \""
            + cleanUpFilePath(inputFileName)
            + "\"";

        log.debug("cmd line:\n***\n" + strCmd + "\n***");

        if (runIt)
        {
            log.debug("running " + TIDY_EXECUTABLE);
            try
            {
                Runtime.getRuntime().exec(strCmd);
            }
            catch (IOException e)
            {
                log.warn("Error running [" + strCmd + "] cmd: " + e.getMessage());
            }
        }

    }

    /**
     * Utility method to clean up file path returned by URLs.
     * @param fileName file name as given by URL.getFile()
     * @return String fileName
     */
    protected String cleanUpFilePath(String fileName)
    {
        if (fileName.length() > 3 && fileName.charAt(2) == ':')
        {
            // assuming something like ""/C:/program files/..."
            return fileName.substring(1);
        }
        else if (fileName.startsWith("file://"))
        {
            return fileName.substring(7);
        }

        return fileName;

    }

}