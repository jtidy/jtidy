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

import org.w3c.dom.Attr;


/**
 * Attribute/Value linked list node.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision $ ($Author $)
 */
public class AttVal extends Object implements Cloneable
{

    public AttVal next;

    public Attribute dict;

    public Node asp;

    public Node php;

    public int delim;

    public String attribute;

    public String value;

    // DOM
    protected Attr adapter;

    public AttVal()
    {
    }

    public AttVal(AttVal next, Attribute dict, int delim, String attribute, String value)
    {
        this.next = next;
        this.dict = dict;
        this.asp = null;
        this.php = null;
        this.delim = delim;
        this.attribute = attribute;
        this.value = value;
    }

    public AttVal(AttVal next, Attribute dict, Node asp, Node php, int delim, String attribute, String value)
    {
        this.next = next;
        this.dict = dict;
        this.asp = asp;
        this.php = php;
        this.delim = delim;
        this.attribute = attribute;
        this.value = value;
    }

    protected Object clone()
    {
        AttVal av = new AttVal();
        if (this.next != null)
        {
            av.next = (AttVal) this.next.clone();
        }
        if (this.attribute != null)
        {
            av.attribute = this.attribute;
        }
        if (this.value != null)
        {
            av.value = this.value;
        }
        av.delim = this.delim;
        if (this.asp != null)
        {
            av.asp = (Node) this.asp.clone();
        }
        if (this.php != null)
        {
            av.php = (Node) this.php.clone();
        }
        av.dict = AttributeTable.getDefaultAttributeTable().findAttribute(this);
        return av;
    }

    public boolean isBoolAttribute()
    {
        Attribute attr = this.dict;
        if (attr != null)
        {
            if (attr.getAttrchk() == AttrCheckImpl.getCheckBool())
            {
                return true;
            }
        }

        return false;
    }

    /* ignore unknown attributes for proprietary elements */
    public Attribute checkAttribute(Lexer lexer, Node node)
    {
        TagTable tt = lexer.configuration.tt;

        if (this.asp == null && this.php == null)
        {
            this.checkUniqueAttribute(lexer, node);
        }

        Attribute attr = this.dict;
        if (attr != null)
        {

            // if attribute looks like <foo/> check XML is ok
            if ((attr.getVersions() & Dict.VERS_XML) != 0)
            {
                if (!(lexer.configuration.xmlTags || lexer.configuration.xmlOut))
                {
                    lexer.report.attrError(lexer, node, this, Report.XML_ATTRIBUTE_VALUE);
                }
            }
            // title first appeared in HTML 4.0 except for a/link
            else if (attr != AttributeTable.attrTitle || !(node.tag == tt.tagA || node.tag == tt.tagLink))
            {
                lexer.constrainVersion(attr.getVersions());
            }

            if (attr.getAttrchk() != null)
            {
                attr.getAttrchk().check(lexer, node, this);
            }
            else if ((this.dict.getVersions() & Dict.VERS_PROPRIETARY) != 0)
            {
                lexer.report.attrError(lexer, node, this, Report.PROPRIETARY_ATTRIBUTE);
            }

        }
        else if (!lexer.configuration.xmlTags
            && !(node.tag == null)
            && this.asp == null
            && !(node.tag != null && ((node.tag.versions & Dict.VERS_PROPRIETARY) != 0)))
        {
            lexer.report.attrError(lexer, node, this, Report.UNKNOWN_ATTRIBUTE);
        }

        return attr;
    }

    /**
     * the same attribute name can't be used more than once in each element.
     */
    public void checkUniqueAttribute(Lexer lexer, Node node)
    {
        AttVal attr;
        int count = 0;

        for (attr = this.next; attr != null; attr = attr.next)
        {
            if (this.attribute != null
                && attr.attribute != null
                && attr.asp == null
                && attr.php == null
                && Lexer.wstrcasecmp(this.attribute, attr.attribute) == 0)
            {
                ++count;
            }
        }

        if (count > 0)
        {
            lexer.report.attrError(lexer, node, this, Report.REPEATED_ATTRIBUTE);
        }
    }

    protected org.w3c.dom.Attr getAdapter()
    {
        if (this.adapter == null)
        {
            this.adapter = new DOMAttrImpl(this);
        }
        return this.adapter;
    }

}