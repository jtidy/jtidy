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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.w3c.tidy.Tidy;


/**
 * JTidy ant task, kindly donated to JTidy by Nicola Ken Barozzi from the krysalis project. See
 * http://sourceforge.net/tracker/index.php?func=detail&aid=780131&group_id=13153&atid=363153
 * 
 * <pre>
 * &lt;jtidy destDir="" log="" summary="" warn="">
 *   &lt;fileset dir="" />
 * &lt;/jtidy>
 * </pre>
 * 
 * @author <a href="mailto:barozzi@nicolaken.com">Nicola Ken Barozzi </a>
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class JTidyTask extends Task
{

    /**
     * Filesets.
     */
    private Collection filesets = new ArrayList();

    /**
     * destination directory.
     */
    private String dest;

    /**
     * log file.
     */
    private String log = "JTidy.log";

    /**
     * tidy instance.
     */
    private Tidy tidy;

    /**
     * show warnings.
     */
    private boolean warn;

    /**
     * show summary.
     */
    private boolean summary;

    /**
     * sets the destination directory.
     * @param file destination directory
     */
    public void setDestDir(String file)
    {
        this.dest = file;
    }

    /**
     * sets the tidy log file.
     * @param file file name
     */
    public void setLog(String file)
    {
        this.log = file;
    }

    /**
     * show warnings?
     * @param showWarnings boolean
     */
    public void setWarn(String showWarnings)
    {
        this.warn = Boolean.getBoolean(showWarnings);
    }

    /**
     * show tidy summary?
     * @param showSummary boolean
     */
    public void setSummary(String showSummary)
    {
        this.summary = Boolean.getBoolean(showSummary);
    }

    /**
     * Initializes the task.
     */
    public void init()
    {
        super.init();

        // Setup an instance of Tidy.
        tidy = new Tidy();
        tidy.setXmlOut(true);
        tidy.setXHTML(true);
        tidy.setDropFontTags(true);
        tidy.setLiteralAttribs(true);
        tidy.setMakeClean(true);
        tidy.setDropProprietaryAttributes(true);
        tidy.setFixBackslash(true);
        tidy.setForceOutput(true);
        tidy.setIndentContent(true);
        tidy.setShowWarnings(this.warn);
        tidy.setQuiet(!this.summary);
    }

    protected void processFile(String src, File taskRootDirectory) throws FileNotFoundException, IOException
    {
        // Extract the document using JTidy and stream it.
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(taskRootDirectory, src)));

        // create directory
        getSubdirAlways(new File("."), new File(dest, src).getParent());
        FileOutputStream out = new FileOutputStream(new File(dest, src));

        tidy.parse(in, out);

        out.flush();
        out.close();
        in.close();
    }

    public static File getSubdirAlways(File dir, String path)
    {
        if (!dir.exists())
        {
            throw new RuntimeException("base directory " + dir + " does not exist");
        }

        File subdir = dir;

        String[] split = path.split("[\\\\/]");
        for (int i = 0; i < split.length; i++)
        {
            subdir = new File(subdir, split[i]);
            if (!subdir.exists())
            {
                if (!subdir.mkdir())
                {
                    throw new RuntimeException("failed to create directory " + subdir);
                }
            }
        }

        return subdir;
    }

    /**
     * Generate the static html pages for the task's file sets.
     */
    private void processFileSets()
    {
        for (Iterator iterator = filesets.iterator(); iterator.hasNext();)
        {
            FileSet fileSet = (FileSet) iterator.next();
            DirectoryScanner directoryScanner = getDirectoryScanner(fileSet);
            String[] sourceFiles = directoryScanner.getIncludedFiles();

            for (int pageIndex = 0; pageIndex < sourceFiles.length; pageIndex++)
            {
                try
                {
                    processFile(sourceFiles[pageIndex], fileSet.getDir(project));
                }
                catch (Exception e)
                {
                    throw new BuildException("Failed to process " + sourceFiles[pageIndex], e);
                }
            }
        }
    }

    /**
     * Run the task.
     * @exception BuildException The exception raised during task execution.
     */
    public void execute() throws BuildException
    {
        try
        {

            PrintWriter logWriter = null;

            if (log != null)
            {
                logWriter = new PrintWriter(new FileWriter(log));
            }
            tidy.setErrout(logWriter);

            processFileSets();
            logWriter.flush();
            logWriter.close();
        }
        catch (IOException ioe)
        {
            throw new BuildException(ioe);
        }
    }

    DirectoryScanner getDirectoryScanner(FileSet fileSet) throws BuildException
    {
        File dir = fileSet.getDir(project);
        if (!dir.exists())
        {
            throw new BuildException("source directory '" + dir.getPath() + "' not found");
        }
        return fileSet.getDirectoryScanner(project);
    }

    public void addFileset(FileSet fileSet)
    {
        filesets.add(fileSet);
    }

}