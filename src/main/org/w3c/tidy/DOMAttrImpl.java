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
 * DOMAttrImpl.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public class DOMAttrImpl extends DOMNodeImpl implements org.w3c.dom.Attr
{

    protected AttVal avAdaptee;

    protected DOMAttrImpl(AttVal adaptee)
    {
        super(null); // must override all methods of DOMNodeImpl
        this.avAdaptee = adaptee;
    }

    /* --------------------- DOM ---------------------------- */

    public String getNodeValue() throws DOMException
    {
        return getValue();
    }

    public void setNodeValue(String nodeValue) throws DOMException
    {
        setValue(nodeValue);
    }

    public String getNodeName()
    {
        return getName();
    }

    public short getNodeType()
    {
        return org.w3c.dom.Node.ATTRIBUTE_NODE;
    }

    public org.w3c.dom.Node getParentNode()
    {
        return null;
    }

    public org.w3c.dom.NodeList getChildNodes()
    {
        // NOT SUPPORTED
        return null;
    }

    public org.w3c.dom.Node getFirstChild()
    {
        // NOT SUPPORTED
        return null;
    }

    public org.w3c.dom.Node getLastChild()
    {
        // NOT SUPPORTED
        return null;
    }

    public org.w3c.dom.Node getPreviousSibling()
    {
        return null;
    }

    public org.w3c.dom.Node getNextSibling()
    {
        return null;
    }

    public org.w3c.dom.NamedNodeMap getAttributes()
    {
        return null;
    }

    public org.w3c.dom.Document getOwnerDocument()
    {
        return null;
    }

    public org.w3c.dom.Node insertBefore(org.w3c.dom.Node newChild, org.w3c.dom.Node refChild) throws DOMException
    {
        throw new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Not supported");
    }

    public org.w3c.dom.Node replaceChild(org.w3c.dom.Node newChild, org.w3c.dom.Node oldChild) throws DOMException
    {
        throw new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Not supported");
    }

    public org.w3c.dom.Node removeChild(org.w3c.dom.Node oldChild) throws DOMException
    {
        throw new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Not supported");
    }

    public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild) throws DOMException
    {
        throw new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR, "Not supported");
    }

    public boolean hasChildNodes()
    {
        return false;
    }

    public org.w3c.dom.Node cloneNode(boolean deep)
    {
        return null;
    }

    /**
     * @see org.w3c.dom.Attr#getName
     */
    public String getName()
    {
        return avAdaptee.attribute;
    }

    /**
     * @see org.w3c.dom.Attr#getSpecified
     */
    public boolean getSpecified()
    {
        return true;
    }

    /**
     * Returns value of this attribute. If this attribute has a null value, then the attribute name is returned
     * instead. Thanks to Brett Knights brett@knightsofthenet.com for this fix.
     * @see org.w3c.dom.Attr#getValue
     */
    public String getValue()
    {
        return (avAdaptee.value == null) ? avAdaptee.attribute : avAdaptee.value;
    }

    /**
     * @see org.w3c.dom.Attr#setValue
     */
    public void setValue(String value)
    {
        avAdaptee.value = value;
    }

    /**
     * DOM2 - not implemented.
     */
    public org.w3c.dom.Element getOwnerElement()
    {
        return null;
    }

}
