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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.tidy.Tidy;


/**
 * JTidy ant task, kindly donated to JTidy by Nicola Ken Barozzi from the krysalis project. See
 * http://sourceforge.net/tracker/index.php?func=detail&aid=780131&group_id=13153&atid=363153
 * @author <a href="mailto:barozzi@nicolaken.com">Nicola Ken Barozzi </a>
 * @version $Revision$ ($Author$)
 */

public class JTidyTask extends Task
{

    /**
     * input file.
     */
    private String src;

    /**
     * destination file.
     */
    private String dest;

    /**
     * log file.
     */
    private String log;

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
     * sets the input file.
     * @param file input file
     */
    public void setSrc(String file)
    {
        this.src = file;
    }

    /**
     * sets the destination file.
     * @param file destination file
     */
    public void setDest(String file)
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
        tidy.setShowWarnings(this.warn);
        tidy.setQuiet(!this.summary);
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

            // Extract the document using JTidy and stream it.
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
            FileOutputStream out = new FileOutputStream(dest);

            tidy.parse(in, out);

            out.flush();
            out.close();
            in.close();
            logWriter.flush();
            logWriter.close();
        }
        catch (IOException ioe)
        {
            throw new BuildException(ioe);
        }
    }

}