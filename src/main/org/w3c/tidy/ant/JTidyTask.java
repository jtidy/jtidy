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
package org.w3c.tidy.ant;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.tidy.Tidy;

/**
 * JTidy ant task, kindly donated to JTidy by Nicola Ken Barozzi from the krysalis project. See
 * http://sourceforge.net/tracker/index.php?func=detail&aid=780131&group_id=13153&atid=363153
 * @author <a href="mailto:barozzi@nicolaken.com">Nicola Ken Barozzi</a>
 * @version $Revision$ ($Author$)
 */

public class JTidyTask extends org.apache.tools.ant.Task
{
    private String src;
    private String dest;
    private String log;
    private Tidy tidy;
    private String warn = "false";
    private String summary = "false";
    PrintWriter pw;

    /**
     * Constructor.
     */

    public JTidyTask()
    {
        super();
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
        tidy.setShowWarnings(Boolean.getBoolean(warn));
        tidy.setQuiet(!Boolean.getBoolean(summary));
    }

    /**
     * Run the task.
     * @exception org.apache.tools.ant.BuildException The exception raised during task execution.
     */

    public void execute() throws org.apache.tools.ant.BuildException
    {
        try
        {
            PrintWriter pw = new PrintWriter(new FileWriter(log));

            tidy.setErrout(pw);

            // Extract the document using JTidy and stream it.
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));

            // FileOutputStream out = new FileOutputStream(dest);
            PrintWriter out = new PrintWriter(new FileWriter(dest));

            // using null as output to get dom so to remove duplicate attributes
            org.w3c.dom.Document domDoc = tidy.parseDOM(in, null);

            domDoc.normalize();
            stripDuplicateAttributes(domDoc, null);
            org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat();

            format.setIndenting(true);
            format.setEncoding("ISO-8859-1");
            format.setPreserveSpace(true);
            format.setLineSeparator("\n");
            org.apache.xml.serialize.XMLSerializer serializer = new org.apache.xml.serialize.XMLSerializer(out, format);

            serializer.serialize(domDoc);
            out.flush();
            out.close();
            in.close();
            pw.flush();
            pw.close();
        }
        catch (IOException ioe)
        {
            throw new BuildException(ioe);
        }
    }

    public void setSrc(String src)
    {
        this.src = src;
    }

    public void setDest(String dest)
    {
        this.dest = dest;
    }

    public void setLog(String log)
    {
        this.log = log;
    }

    public void setWarn(String warn)
    {
        this.warn = warn;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    // using parent because jtidy dom is bugged, cannot get parent or delete child
    public static void stripDuplicateAttributes(Node node, Node parent)
    {

        // The output depends on the type of the node
        switch (node.getNodeType())
        {

            case Node.DOCUMENT_NODE :
                {
                    Document doc = (Document) node;
                    Node child = doc.getFirstChild();

                    while (child != null)
                    {
                        stripDuplicateAttributes(child, node);
                        child = child.getNextSibling();
                    }
                    break;
                }
            case Node.ELEMENT_NODE :
                {
                    Element elt = (Element) node;
                    NamedNodeMap attrs = elt.getAttributes();
                    ArrayList nodesToRemove = new ArrayList();
                    int nodesToRemoveNum = 0;

                    for (int i = 0; i < attrs.getLength(); i++)
                    {
                        Node a = attrs.item(i);

                        for (int j = 0; j < attrs.getLength(); j++)
                        {
                            Node b = attrs.item(j);

                            // if there are two attributes with same name
                            if ((i != j) && (a.getNodeName().equals(b.getNodeName())))
                            {
                                nodesToRemove.add(b);
                                nodesToRemoveNum++;
                            }
                        }
                    }
                    for (int i = 0; i < nodesToRemoveNum; i++)
                    {
                        org.w3c.dom.Attr nodeToDelete = (org.w3c.dom.Attr) nodesToRemove.get(i);
                        org.w3c.dom.Element nodeToDeleteParent = (org.w3c.dom.Element) node;
                        // nodeToDelete.getParentNode();

                        nodeToDeleteParent.removeAttributeNode(nodeToDelete);
                    }
                    nodesToRemove.clear();
                    Node child = elt.getFirstChild();

                    while (child != null)
                    {
                        stripDuplicateAttributes(child, node);
                        child = child.getNextSibling();
                    }
                    break;
                }
            default :

                // do nothing
                break;
        }
    }
}