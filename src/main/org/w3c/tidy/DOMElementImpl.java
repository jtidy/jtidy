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
 * DOMElementImpl.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public class DOMElementImpl extends DOMNodeImpl implements org.w3c.dom.Element
{

    protected DOMElementImpl(Node adaptee)
    {
        super(adaptee);
    }

    /* --------------------- DOM ---------------------------- */

    /**
     * @see org.w3c.dom.Node#getNodeType
     */
    public short getNodeType()
    {
        return org.w3c.dom.Node.ELEMENT_NODE;
    }

    /**
     * @see org.w3c.dom.Element#getTagName
     */
    public String getTagName()
    {
        return super.getNodeName();
    }

    /**
     * @see org.w3c.dom.Element#getAttribute
     */
    public String getAttribute(String name)
    {
        if (this.adaptee == null)
        {
            return null;
        }

        AttVal att = this.adaptee.attributes;
        while (att != null)
        {
            if (att.attribute.equals(name))
            {
                break;
            }
            att = att.next;
        }
        if (att != null)
        {
            return att.value;
        }
        else
        {
            return "";
        }
    }

    /**
     * @see org.w3c.dom.Element#setAttribute
     */
    public void setAttribute(String name, String value) throws DOMException
    {
        if (this.adaptee == null)
        {
            return;
        }

        AttVal att = this.adaptee.attributes;
        while (att != null)
        {
            if (att.attribute.equals(name))
            {
                break;
            }
            att = att.next;
        }
        if (att != null)
        {
            att.value = value;
        }
        else
        {
            att = new AttVal(null, null, '"', name, value);
            att.dict = AttributeTable.getDefaultAttributeTable().findAttribute(att);
            if (this.adaptee.attributes == null)
            {
                this.adaptee.attributes = att;
            }
            else
            {
                att.next = this.adaptee.attributes;
                this.adaptee.attributes = att;
            }
        }
    }

    /**
     * @see org.w3c.dom.Element#removeAttribute
     */
    public void removeAttribute(String name) throws DOMException
    {
        if (this.adaptee == null)
        {
            return;
        }

        AttVal att = this.adaptee.attributes;
        AttVal pre = null;
        while (att != null)
        {
            if (att.attribute.equals(name))
            {
                break;
            }
            pre = att;
            att = att.next;
        }
        if (att != null)
        {
            if (pre == null)
            {
                this.adaptee.attributes = att.next;
            }
            else
            {
                pre.next = att.next;
            }
        }
    }

    /**
     * @see org.w3c.dom.Element#getAttributeNode
     */
    public org.w3c.dom.Attr getAttributeNode(String name)
    {
        if (this.adaptee == null)
        {
            return null;
        }

        AttVal att = this.adaptee.attributes;
        while (att != null)
        {
            if (att.attribute.equals(name))
            {
                break;
            }
            att = att.next;
        }
        if (att != null)
        {
            return att.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Element#setAttributeNode
     */
    public org.w3c.dom.Attr setAttributeNode(org.w3c.dom.Attr newAttr) throws DOMException
    {
        if (newAttr == null)
        {
            return null;
        }
        if (!(newAttr instanceof DOMAttrImpl))
        {
            throw new DOMExceptionImpl(DOMException.WRONG_DOCUMENT_ERR, "newAttr not instanceof DOMAttrImpl");
        }

        DOMAttrImpl newatt = (DOMAttrImpl) newAttr;
        String name = newatt.avAdaptee.attribute;
        org.w3c.dom.Attr result = null;

        AttVal att = this.adaptee.attributes;
        while (att != null)
        {
            if (att.attribute.equals(name))
            {
                break;
            }
            att = att.next;
        }
        if (att != null)
        {
            result = att.getAdapter();
            att.adapter = newAttr;
        }
        else
        {
            if (this.adaptee.attributes == null)
            {
                this.adaptee.attributes = newatt.avAdaptee;
            }
            else
            {
                newatt.avAdaptee.next = this.adaptee.attributes;
                this.adaptee.attributes = newatt.avAdaptee;
            }
        }
        return result;
    }

    /**
     * @see org.w3c.dom.Element#removeAttributeNode
     */
    public org.w3c.dom.Attr removeAttributeNode(org.w3c.dom.Attr oldAttr) throws DOMException
    {
        if (oldAttr == null)
        {
            return null;
        }

        org.w3c.dom.Attr result = null;
        AttVal att = this.adaptee.attributes;
        AttVal pre = null;
        while (att != null)
        {
            if (att.getAdapter() == oldAttr)
            {
                break;
            }
            pre = att;
            att = att.next;
        }
        if (att != null)
        {
            if (pre == null)
            {
                this.adaptee.attributes = att.next;
            }
            else
            {
                pre.next = att.next;
            }
            result = oldAttr;
        }
        else
        {
            throw new DOMExceptionImpl(DOMException.NOT_FOUND_ERR, "oldAttr not found");
        }
        return result;
    }

    /**
     * @see org.w3c.dom.Element#getElementsByTagName
     */
    public org.w3c.dom.NodeList getElementsByTagName(String name)
    {
        return new DOMNodeListByTagNameImpl(this.adaptee, name);
    }

    /**
     * @see org.w3c.dom.Element#normalize
     */
    public void normalize()
    {
        // NOT SUPPORTED
    }

    /**
     * DOM2 - not implemented.
     */
    public String getAttributeNS(String namespaceURI, String localName)
    {
        return null;
    }

    /**
     * DOM2 - not implemented.
     * @exception org.w3c.dom.DOMException
     */
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws org.w3c.dom.DOMException
    {
    }

    /**
     * DOM2 - not implemented.
     * @exception org.w3c.dom.DOMException
     */
    public void removeAttributeNS(String namespaceURI, String localName) throws org.w3c.dom.DOMException
    {
    }

    /**
     * DOM2 - not implemented.
     */
    public org.w3c.dom.Attr getAttributeNodeNS(String namespaceURI, String localName)
    {
        return null;
    }

    /**
     * DOM2 - not implemented.
     * @exception org.w3c.dom.DOMException
     */
    public org.w3c.dom.Attr setAttributeNodeNS(org.w3c.dom.Attr newAttr) throws org.w3c.dom.DOMException
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
    public boolean hasAttribute(String name)
    {
        return false;
    }

    /**
     * DOM2 - not implemented.
     */
    public boolean hasAttributeNS(String namespaceURI, String localName)
    {
        return false;
    }

}
