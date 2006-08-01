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
package org.w3c.tidy.ant;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.util.FileUtils;


/**
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class JTidyTaskTest extends TestCase
{

    /**
     * test instance.
     */
    private JTidyTask task;

    /**
     * Temp dir used for output.
     */
    private String tempDir;

    /**
     * Test dir.
     */
    private String testDir;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        task = new JTidyTask();
        Project p = new Project();
        task.setProject(p);
        task.init();
        tempDir = System.getProperty("java.io.tmpdir");
        testDir = new File(getClass().getClassLoader().getResource("test.dir").getPath()).getParent();
    }

    /**
     * Test with invalid parameters.
     */
    public void testExceptionMissingParameters()
    {
        try
        {
            task.execute();
            fail("Invalid parameters not detected");
        }
        catch (BuildException e)
        {
            // ok
        }
    }

    /**
     * Test with invalid parameters.
     */
    public void testExceptionBothSrcfileAndFilesets()
    {
        try
        {
            task.setSrcfile(new File("."));
            task.addFileset(new FileSet());
            task.validateParameters();
            fail("Invalid parameters not detected");
        }
        catch (BuildException e)
        {
            // ok
        }
    }

    /**
     * Test with invalid parameters.
     */
    public void testDestFileAndDestDirNull()
    {
        try
        {
            task.setSrcfile(new File("."));
            task.validateParameters();
            fail("Invalid parameters not detected");
        }
        catch (BuildException e)
        {
            // ok
        }
    }

    /**
     * Test with invalid parameters.
     */
    public void testDestFileAndFilesets()
    {
        try
        {
            task.addFileset(new FileSet());
            task.setDestfile(new File("."));
            task.validateParameters();
            fail("Invalid parameters not detected");
        }
        catch (BuildException e)
        {
            // ok
        }
    }

    /**
     * Test with invalid parameters.
     */
    public void testScrFileIsADir()
    {
        try
        {
            task.setSrcfile(new File("/"));
            task.setDestfile(new File("test.out"));
            task.validateParameters();
            fail("Invalid parameters not detected");
        }
        catch (BuildException e)
        {
            // ok
        }
    }

    /**
     * Test with invalid parameters.
     */
    public void testScrFileDoesntExist()
    {

        task.setSrcfile(new File("xyz.123"));
        task.setDestfile(new File("test.out"));
        task.validateParameters();
        try
        {
            task.execute();
            fail("Missing source file not detected");
        }
        catch (BuildException e)
        {
            // ok
        }
    }

    /**
     * Test with invalid parameters.
     */
    public void testInvalidProperties()
    {
        try
        {
            task.setSrcfile(new File("test.in"));
            task.setDestfile(new File("test.out"));
            task.setProperties(new File("x2ui34"));
            task.validateParameters();
            fail("Invalid parameters not detected");
        }
        catch (BuildException e)
        {
            // ok
        }
    }

    /**
     * Test with a fileset.
     */
    public void testFileset()
    {
        FileSet fileset = new FileSet();
        fileset.setDir(new File(testDir, "ant"));

        task.addFileset(fileset);
        task.setDestdir(new File(tempDir));

        task.execute();

        assertTrue("Expected output file not created", new File(tempDir, "file1.html").exists());
        assertTrue("Expected output file not created", new File(tempDir, "file2.html").exists());

        new File(tempDir, "file1.html").delete();
        new File(tempDir, "file2.html").delete();
    }

    /**
     * Test with a fileset.
     */
    public void testFilesetWithDirStructure()
    {
        FileSet fileset = new FileSet();
        fileset.setDir(new File(testDir));
        fileset.setIncludes("ant/*.html");

        task.addFileset(fileset);
        task.setDestdir(new File(tempDir));

        task.execute();

        assertTrue("Expected output file not created", new File(tempDir, "ant/file1.html").exists());
        assertTrue("Expected output file not created", new File(tempDir, "ant/file2.html").exists());

        new File(tempDir, "ant/file1.html").delete();
        new File(tempDir, "ant/file2.html").delete();
        new File(tempDir, "ant").delete();
    }

    /**
     * Test with a fileset.
     */
    public void testFilesetWithDirStructureFlatten()
    {
        FileSet fileset = new FileSet();
        fileset.setDir(new File(testDir));
        fileset.setIncludes("ant/*.html");

        task.addFileset(fileset);
        task.setDestdir(new File(tempDir));
        task.setFlatten(true);

        task.execute();

        assertTrue("Expected output file not created", new File(tempDir, "file1.html").exists());
        assertTrue("Expected output file not created", new File(tempDir, "file2.html").exists());

        new File(tempDir, "file1.html").delete();
        new File(tempDir, "file2.html").delete();
    }

    /**
     * Test nested parameter element.
     */
    public void testWithParameters()
    {
        FileSet fileset = new FileSet();
        fileset.setDir(new File(testDir));
        fileset.setIncludes("ant/*1.html");

        task.addFileset(fileset);
        task.setDestdir(new File(tempDir));
        task.setFlatten(true);
        Parameter parameter = new Parameter();
        parameter.setName("tidy-mark");
        parameter.setValue("false");
        task.addConfiguredParameter(parameter);
        task.execute();

        assertTrue("Expected output file not created", new File(tempDir, "file1.html").exists());

        try
        {
            Reader reader = new FileReader(new File(tempDir, "file1.html"));
            String output = FileUtils.readFully(reader);
            reader.close();

            // output file should not contain "generator"
            assertTrue("Configured parameter doesn't have effect on output.", output.indexOf("generator") == -1);
        }
        catch (IOException e)
        {
            fail("Unable to read generated file.");
        }

        new File(tempDir, "file1.html").delete();
    }

    /**
     * Test with a properties file.
     */
    public void testWithProperties()
    {
        FileSet fileset = new FileSet();
        fileset.setDir(new File(testDir));
        fileset.setIncludes("ant/*1.html");

        task.addFileset(fileset);
        task.setDestdir(new File(tempDir));
        task.setFlatten(true);
        task.setProperties(new File(testDir, "default.cfg"));

        task.execute();

        assertTrue("Expected output file not created", new File(tempDir, "file1.html").exists());

        try
        {
            Reader reader = new FileReader(new File(tempDir, "file1.html"));
            String output = FileUtils.readFully(reader);
            reader.close();

            // output file should not contain "generator"
            assertTrue("Configured parameter doesn't have effect on output.", output.indexOf("generator") == -1);
        }
        catch (IOException e)
        {
            fail("Unable to read generated file.");
        }

        new File(tempDir, "file1.html").delete();
    }

    /**
     * Test with a fileset.
     */
    public void testFailonerrorFalse()
    {
        task.setSrcfile(new File(testDir, "ant/file3.html"));
        task.setDestdir(new File(tempDir));
        task.setFailonerror(false);

        task.execute();

        // ok if no buildexception is thrown
    }

    /**
     * Test with a fileset.
     */
    public void testFailonerrorTrue()
    {
        task.setSrcfile(new File(testDir, "ant/file3.html"));
        task.setDestdir(new File(tempDir));
        task.setFailonerror(true);

        try
        {
            task.execute();
            fail("Expected BuildException not thrown.");
        }
        catch (BuildException e)
        {
            // ok if buildexception IS thrown
        }
    }

    /**
     * Test with srcfile/destdir.
     */
    public void testSrcfileDestDir()
    {
        task.setSrcfile(new File(testDir, "ant/file1.html"));
        task.setDestdir(new File(tempDir));
        task.setFailonerror(true);

        task.execute();

        assertTrue("Expected output file not created", new File(tempDir, "file1.html").exists());

        new File(tempDir, "file1.html").delete();
    }

    /**
     * Test with srcfile/destfile.
     */
    public void testSrcfileDestFile()
    {
        task.setSrcfile(new File(testDir, "ant/file1.html"));
        task.setDestfile(new File(tempDir, "newfile.html"));
        task.setFailonerror(true);

        task.execute();

        assertTrue("Expected output file not created", new File(tempDir, "newfile.html").exists());
        assertFalse("Expected output file is a dir!", new File(tempDir, "newfile.html").isDirectory());

        new File(tempDir, "newfile.html").delete();
    }

    /**
     * Test with srcfile/destfile.
     */
    public void testMissingSrcFile()
    {
        try
        {
            task
                .processFile(new File(testDir, "non/existing/file.html"), new File(tempDir, "non/existing/output.html"));
            fail("Expected BuildException not thrown");
        }
        catch (BuildException e)
        {
            // ok, this is expected
        }
    }

    /**
     * Test with srcfile/destfile.
     */
    public void testMissingOutputFile()
    {
        try
        {
            task.processFile(new File(testDir, "ant/file1.html"), new File(tempDir, "///::non/existing/output.html"));
            fail("Expected BuildException not thrown");
        }
        catch (BuildException e)
        {
            // ok, this is expected
        }
    }

    /**
     * Test with invalid properties file.
     */
    public void testMissingProperties()
    {
        task.setProperties(new File(testDir, "non/existing/propertyfile.properties"));
        task.setSrcfile(new File(testDir, "ant/file1.html"));
        task.setDestfile(new File(tempDir, "newfile.html"));

        try
        {
            task.execute();
            fail("Expected BuildException not thrown");
        }
        catch (BuildException e)
        {
            // ok, this is expected
        }
    }

    /**
     * Test with srcfile/destfile.
     */
    public void testPropertiesIsADir()
    {
        task.setProperties(new File(testDir));
        task.setSrcfile(new File(testDir, "ant/file1.html"));
        task.setDestfile(new File(tempDir, "newfile.html"));

        try
        {
            task.execute();
            fail("Expected BuildException not thrown");
        }
        catch (BuildException e)
        {
            // ok, this is expected
        }
    }
}