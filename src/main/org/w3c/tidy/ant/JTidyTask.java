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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.FlatFileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.w3c.tidy.Tidy;


/**
 * JTidy ant task, initially donated to JTidy by Nicola Ken Barozzi from the krysalis project. See
 * http://sourceforge.net/tracker/index.php?func=detail&aid=780131&group_id=13153&atid=363153
 * 
 * <pre>
 * &lt;tidy destdir="" >
 *   &lt;fileset dir="" />
 * &lt;/tidy>
 * </pre>
 * 
 * <table><thead>
 * <tr>
 * <td>Attribute</td>
 * <td>Description</td>
 * <td>Required</td>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>srcfile</td>
 * <td>source file</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>destfile</td>
 * <td>destination file for output</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>destdir</td>
 * <td>destination directory for output</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>properties</td>
 * <td>properties file</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>flatten</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>failonerror</td>
 * <td>boolean to control whether failure to execute should throw a BuildException or just print an error</td>
 * <td></td>
 * </tr>
 * </tbody> </table>
 * @author <a href="mailto:barozzi@nicolaken.com">Nicola Ken Barozzi </a>
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class JTidyTask extends Task
{

    /**
     * Ant utility class for file operations.
     */
    private FileUtils fileUtils = FileUtils.newFileUtils();

    /**
     * Filesets.
     */
    private List filesets = new ArrayList();

    /**
     * Destination directory for output.
     */
    private File destdir;

    /**
     * Destination file for output.
     */
    private File destfile;

    /**
     * Source file.
     */
    private File srcfile;

    /**
     * Control whether failure to execute should throw a BuildException.
     */
    private boolean failonerror;

    /**
     * Don't output directories.
     */
    private boolean flatten;

    /**
     * tidy instance.
     */
    private Tidy tidy;

    /**
     * Configured properties.
     */
    private Properties props;

    /**
     * Properties file.
     */
    private File properties;

    /**
     * @param destdir The destdir to set.
     */
    public void setDestdir(File destdir)
    {
        this.destdir = destdir;
    }

    /**
     * @param destfile The destfile to set.
     */
    public void setDestfile(File destfile)
    {
        this.destfile = destfile;
    }

    /**
     * @param srcfile The srcfile to set.
     */
    public void setSrcfile(File srcfile)
    {
        this.srcfile = srcfile;
    }

    /**
     * @param failonerror The failonerror to set.
     */
    public void setFailonerror(boolean failonerror)
    {
        this.failonerror = failonerror;
    }

    /**
     * @param flatten The flatten to set.
     */
    public void setFlatten(boolean flatten)
    {
        this.flatten = flatten;
    }

    /**
     * @param properties The properties to set.
     */
    public void setProperties(File properties)
    {
        this.properties = properties;
    }

    /**
     * Adds a fileset to be processed Fileset
     * @param fileSet
     */
    public void addFileset(FileSet fileSet)
    {
        filesets.add(fileSet);
    }

    /**
     * Setter method for any property using the ant type Parameter.
     * @param prop Ant type Parameter
     */
    public void addConfiguredParameter(Parameter prop)
    {
        props.setProperty(prop.getName(), prop.getValue());
    }

    /**
     * Initializes the task.
     */
    public void init()
    {
        super.init();

        // Setup a Tidy instance
        tidy = new Tidy();
        props = new Properties();
    }

    /**
     * Validates task parameters.
     * @throws BuildException if any invalid parameter is found
     */
    protected void validateParameters() throws BuildException
    {
        if (srcfile == null && filesets.size() == 0)
        {
            throw new BuildException("Specify at least srcfile or a fileset.");
        }
        if (srcfile != null && filesets.size() > 0)
        {
            throw new BuildException("You can't specify both srcfile and nested filesets.");
        }

        if (destfile == null && destdir == null)
        {
            throw new BuildException("One of destfile or destdir must be set.");
        }

        if (srcfile == null && destfile != null)
        {
            throw new BuildException("You only can use destfile with srcfile.");
        }

        if (srcfile != null && srcfile.exists() && srcfile.isDirectory())
        {
            throw new BuildException("srcfile can't be a directory.");
        }

        if (properties != null && (!properties.exists() || properties.isDirectory()))
        {
            throw new BuildException("Invalid properties file specified: " + properties.getPath());
        }

    }

    /**
     * Run the task.
     * @exception BuildException The exception raised during task execution.
     */
    public void execute() throws BuildException
    {
        // validate
        validateParameters();

        // load configuration
        if (this.properties != null)
        {
            try
            {
                this.props.load(new FileInputStream(this.properties));
            }
            catch (FileNotFoundException e)
            {
                // should not happen
                throw new BuildException("Unable to find properties file " + properties, e);
            }
            catch (IOException e)
            {
                throw new BuildException("Unable to load properties file " + properties, e);
            }
        }

        tidy.setConfigurationFromProps(props);

        if (this.srcfile != null)
        {
            // process a single file
            executeSingle();
        }
        else if (this.filesets.size() > 0)
        {
            // process filesets
            executeSet();
        }
        else
        {
            // should not happen, condition is already validated in validateAttributes()
            throw new BuildException("No srcfile or nested filesets configured.");
        }

    }

    protected void executeSingle()
    {
        if (srcfile.exists())
        {
            if (destfile == null)
            {
                if (destdir == null)
                {
                    throw new BuildException("No destfile or destdir configured.");
                }
                destfile = new File(destdir, srcfile.getName());
            }
            processFile(srcfile, destfile);

        }
        else
        {
            String message = "Could not find source file " + srcfile.getAbsolutePath() + ".";
            if (!failonerror)
            {
                log(message);
            }
            else
            {
                throw new BuildException(message);
            }
        }
    }

    protected void executeSet()
    {

        FileNameMapper mapper = null;
        if (flatten)
        {
            mapper = new FlatFileNameMapper();
        }
        else
        {
            mapper = new IdentityMapper();
        }

        mapper.setTo(this.destdir.getAbsolutePath());

        Iterator iterator = filesets.iterator();
        while (iterator.hasNext())
        {
            FileSet fileSet = (FileSet) iterator.next();
            DirectoryScanner directoryScanner = fileSet.getDirectoryScanner(getProject());
            String[] sourceFiles = directoryScanner.getIncludedFiles();

            mapper.setFrom(fileSet.getDir(getProject()).getAbsolutePath());

            for (int j = 0; j < sourceFiles.length; j++)
            {
                String[] mapped = mapper.mapFileName(sourceFiles[j]);
                if (mapped == null || mapped.length == 0)
                {
                    throw new BuildException("Uh oh, unable to map "
                        + sourceFiles[j]
                        + "using from "
                        + fileSet.getDir(getProject()).getAbsolutePath()
                        + "and to "
                        + this.destdir.getAbsolutePath());
                }
                processFile(new File(sourceFiles[j]), new File(mapped[0]));
            }
        }
    }

    protected void processFile(File inputFile, File outputFile)
    {

        InputStream is;
        OutputStream os;
        try
        {
            is = new BufferedInputStream(new FileInputStream(srcfile));
        }
        catch (FileNotFoundException e)
        {
            throw new BuildException("Unable to open file " + srcfile);
        }
        try
        {
            os = new BufferedOutputStream(new FileOutputStream(destfile));
        }
        catch (FileNotFoundException e)
        {
            throw new BuildException("Unable to open file " + destfile);
        }

        tidy.parse(is, os);

        try
        {
            is.close();
        }
        catch (IOException e1)
        {
            // ignore
        }
        try
        {
            os.flush();
            os.close();
        }
        catch (IOException e1)
        {
            // ignore
        }

        if (failonerror && tidy.getParseErrors() > 0)
        {
            throw new BuildException("Tidy was unable to process file "
                + srcfile
                + ", "
                + tidy.getParseErrors()
                + " returned.");
        }

    }

}