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

import org.w3c.dom.DOMException;

/**
 * DOMDocumentImpl.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public class DOMDocumentImpl extends DOMNodeImpl implements org.w3c.dom.Document
{

    private TagTable tt; // a DOM Document has its own TagTable.

    protected DOMDocumentImpl(Node adaptee)
    {
        super(adaptee);
        this.tt = new TagTable();
    }

    public void setTagTable(TagTable tt)
    {
        this.tt = tt;
    }

    /* --------------------- DOM ---------------------------- */

    /**
     * @see org.w3c.dom.Node#getNodeName
     */
    public String getNodeName()
    {
        return "#document";
    }

    /**
     * @see org.w3c.dom.Node#getNodeType
     */
    public short getNodeType()
    {
        return org.w3c.dom.Node.DOCUMENT_NODE;
    }

    /**
     * @see org.w3c.dom.Document#getDoctype
     */
    public org.w3c.dom.DocumentType getDoctype()
    {
        Node node = this.adaptee.content;
        while (node != null)
        {
            if (node.type == Node.DocTypeTag)
            {
                break;
            }
            node = node.next;
        }
        if (node != null)
        {
            return (org.w3c.dom.DocumentType) node.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Document#getImplementation
     */
    public org.w3c.dom.DOMImplementation getImplementation()
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#getDocumentElement
     */
    public org.w3c.dom.Element getDocumentElement()
    {
        Node node = this.adaptee.content;
        while (node != null)
        {
            if (node.type == Node.StartTag || node.type == Node.StartEndTag)
            {
                break;
            }
            node = node.next;
        }
        if (node != null)
        {
            return (org.w3c.dom.Element) node.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Document#createElement
     */
    public org.w3c.dom.Element createElement(String tagName) throws DOMException
    {
        Node node = new Node(Node.StartEndTag, null, 0, 0, tagName, this.tt);
        if (node != null)
        {
            if (node.tag == null) // Fix Bug 121206
            {
                node.tag = this.tt.xmlTags;
            }
            return (org.w3c.dom.Element) node.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Document#createDocumentFragment
     */
    public org.w3c.dom.DocumentFragment createDocumentFragment()
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#createTextNode
     */
    public org.w3c.dom.Text createTextNode(String data)
    {
        byte[] textarray = Lexer.getBytes(data);
        Node node = new Node(Node.TextNode, textarray, 0, textarray.length);
        if (node != null)
        {
            return (org.w3c.dom.Text) node.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Document#createComment
     */
    public org.w3c.dom.Comment createComment(String data)
    {
        byte[] textarray = Lexer.getBytes(data);
        Node node = new Node(Node.CommentTag, textarray, 0, textarray.length);
        if (node != null)
        {
            return (org.w3c.dom.Comment) node.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Document#createCDATASection
     */
    public org.w3c.dom.CDATASection createCDATASection(String data) throws DOMException
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#createProcessingInstruction
     */
    public org.w3c.dom.ProcessingInstruction createProcessingInstruction(String target, String data)
        throws DOMException
    {
        throw new DOMExceptionImpl(DOMException.NOT_SUPPORTED_ERR, "HTML document");
    }

    /**
     * @see org.w3c.dom.Document#createAttribute
     */
    public org.w3c.dom.Attr createAttribute(String name) throws DOMException
    {
        AttVal av = new AttVal(null, null, '"', name, null);
        if (av != null)
        {
            av.dict = AttributeTable.getDefaultAttributeTable().findAttribute(av);
            return av.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Document#createEntityReference
     */
    public org.w3c.dom.EntityReference createEntityReference(String name) throws DOMException
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#getElementsByTagName
     */
    public org.w3c.dom.NodeList getElementsByTagName(String tagname)
    {
        return new DOMNodeListByTagNameImpl(this.adaptee, tagname);
    }

    /**
     * DOM2 - not implemented.
     * @exception org.w3c.dom.DOMException
     */
    public org.w3c.dom.Node importNode(org.w3c.dom.Node importedNode, boolean deep) throws org.w3c.dom.DOMException
    {
        return null;
    }

    /**
     * DOM2 - not implemented.
     * @exception org.w3c.dom.DOMException
     */
    public org.w3c.dom.Attr createAttributeNS(String namespaceURI, String qualifiedName)
        throws org.w3c.dom.DOMException
    {
        return null;
    }

    /**
     * DOM2 - not implemented.
     * @exception org.w3c.dom.DOMException
     */
    public org.w3c.dom.Element createElementNS(String namespaceURI, String qualifiedName)
        throws org.w3c.dom.DOMException
    {
        return null;
    }

    /**
     * DOM2 - not implemented.
     */
    public org.w3c.dom.NodeList getElementsByTagNameNS(String namespaceURI, String localName)
    {
        return null;
    }

    /**
     * DOM2 - not implemented.
     */
    public org.w3c.dom.Element getElementById(String elementId)
    {
        return null;
    }

}
