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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class TidyTestCase extends TestCase
{

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(TidyTestCase.class);

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

        // file name without extension, needed for config and out file names
        String noExtensionName = fileName.substring(0, fileName.lastIndexOf("."));
        String configFileName = noExtensionName + ".cfg";
        String outFileName = noExtensionName + ".out";

        ClassLoader classLoader = getClass().getClassLoader();

        // input file
        URL inputURL = classLoader.getResource(fileName);
        assertNotNull("Can't find input file [" + fileName + "]", inputURL);

        // configuration file
        URL configurationFile = classLoader.getResource(configFileName);

        // existing file for comparison
        URL outFile = classLoader.getResource(outFileName);

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
        //creates a new Tidy
        Tidy tidy = new Tidy();

        // set log
        StringWriter errorWriter = new StringWriter();
        tidy.setErrout(new PrintWriter(errorWriter));

        // if configuration file exists load and set it
        if (configurationFile != null)
        {
            Properties testProperties = new Properties();
            testProperties.load(configurationFile.openStream());
            tidy.setConfigurationFromProps(testProperties);
        }
        // set out
        OutputStream out = new ByteArrayOutputStream();

        // go!
        tidy.parse(inputURL.openStream(), out);

        if (outFile != null)
        {
            log.debug("Comparing file using [" + outFileName + "]");

            if (log.isDebugEnabled())
            {
                log.debug("\n---- out ----\n\n" + out + "\n\n---- out ----");
            }
        }
    }
}
