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

/**
 * Check HTML attributes implementation (c) 1998-2000 (W3C) MIT, INRIA, Keio University See Tidy.java for the copyright
 * notice. Derived from <a href="http://www.w3.org/People/Raggett/tidy">HTML Tidy Release 4 Aug 2000</a>
 * @author Dave Raggett <dsr@w3.org>
 * @author Andy Quick <ac.quick@sympatico.ca>(translation to Java)
 * @version 1.0, 1999/05/22
 * @version 1.0.1, 1999/05/29
 * @version 1.1, 1999/06/18 Java Bean
 * @version 1.2, 1999/07/10 Tidy Release 7 Jul 1999
 * @version 1.3, 1999/07/30 Tidy Release 26 Jul 1999
 * @version 1.4, 1999/09/04 DOM support
 * @version 1.5, 1999/10/23 Tidy Release 27 Sep 1999
 * @version 1.6, 1999/11/01 Tidy Release 22 Oct 1999
 * @version 1.7, 1999/12/06 Tidy Release 30 Nov 1999
 * @version 1.8, 2000/01/22 Tidy Release 13 Jan 2000
 * @version 1.9, 2000/06/03 Tidy Release 30 Apr 2000
 * @version 1.10, 2000/07/22 Tidy Release 8 Jul 2000
 * @version 1.11, 2000/08/16 Tidy Release 4 Aug 2000
 */

public class CheckAttribsImpl
{

    public static class CheckHTML implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal attval;
            Attribute attribute;

            node.checkUniqueAttributes(lexer);

            for (attval = node.attributes; attval != null; attval = attval.next)
            {
                attribute = attval.checkAttribute(lexer, node);

                if (attribute == AttributeTable.attrXmlns)
                    lexer.isvoyager = true;
            }
        }

    }

    public static class CheckSCRIPT implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal lang, type;

            node.checkUniqueAttributes(lexer);

            lang = node.getAttrByName("language");
            type = node.getAttrByName("type");

            if (type == null)
            {
                Report.attrError(lexer, node, "type", Report.MISSING_ATTRIBUTE);

                /* check for javascript */

                if (lang != null)
                {
                    String str = lang.value;
                    if (str.length() > 10)
                        str = str.substring(0, 10);
                    if ((Lexer.wstrcasecmp(str, "javascript") == 0) || (Lexer.wstrcasecmp(str, "jscript") == 0))
                    {
                        node.addAttribute("type", "text/javascript");
                    }
                }
                else
                    node.addAttribute("type", "text/javascript");
            }
        }

    }

    public static class CheckTABLE implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal attval;
            Attribute attribute;
            boolean hasSummary = false;

            node.checkUniqueAttributes(lexer);

            for (attval = node.attributes; attval != null; attval = attval.next)
            {
                attribute = attval.checkAttribute(lexer, node);

                if (attribute == AttributeTable.attrSummary)
                    hasSummary = true;
            }

            /* suppress warning for missing summary for HTML 2.0 and HTML 3.2 */
            if (!hasSummary && lexer.doctype != Dict.VERS_HTML20 && lexer.doctype != Dict.VERS_HTML32)
            {
                lexer.badAccess |= Report.MISSING_SUMMARY;
                Report.attrError(lexer, node, "summary", Report.MISSING_ATTRIBUTE);
            }

            /* convert <table border> to <table border="1"> */
            if (lexer.configuration.XmlOut)
            {
                attval = node.getAttrByName("border");
                if (attval != null)
                {
                    if (attval.value == null)
                        attval.value = "1";
                }
            }
        }

    }

    public static class CheckCaption implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal attval;
            String value = null;

            node.checkUniqueAttributes(lexer);

            for (attval = node.attributes; attval != null; attval = attval.next)
            {
                if (Lexer.wstrcasecmp(attval.attribute, "align") == 0)
                {
                    value = attval.value;
                    break;
                }
            }

            if (value != null)
            {
                if (Lexer.wstrcasecmp(value, "left") == 0 || Lexer.wstrcasecmp(value, "right") == 0)
                    lexer.versions &= (short) (Dict.VERS_HTML40_LOOSE | Dict.VERS_FRAMES);
                else if (Lexer.wstrcasecmp(value, "top") == 0 || Lexer.wstrcasecmp(value, "bottom") == 0)
                    lexer.versions &= Dict.VERS_FROM32;
                else
                    Report.attrError(lexer, node, value, Report.BAD_ATTRIBUTE_VALUE);
            }
        }

    }

    public static class CheckHR implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            if (node.getAttrByName("src") != null)
                Report.attrError(lexer, node, "src", Report.PROPRIETARY_ATTR_VALUE);
        }
    }

    public static class CheckIMG implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal attval;
            Attribute attribute;
            boolean hasAlt = false;
            boolean hasSrc = false;
            boolean hasUseMap = false;
            boolean hasIsMap = false;
            boolean hasDataFld = false;

            node.checkUniqueAttributes(lexer);

            for (attval = node.attributes; attval != null; attval = attval.next)
            {
                attribute = attval.checkAttribute(lexer, node);

                if (attribute == AttributeTable.attrAlt)
                    hasAlt = true;
                else if (attribute == AttributeTable.attrSrc)
                    hasSrc = true;
                else if (attribute == AttributeTable.attrUsemap)
                    hasUseMap = true;
                else if (attribute == AttributeTable.attrIsmap)
                    hasIsMap = true;
                else if (attribute == AttributeTable.attrDatafld)
                    hasDataFld = true;
                else if (attribute == AttributeTable.attrWidth || attribute == AttributeTable.attrHeight)
                    lexer.versions &= ~Dict.VERS_HTML20;
            }

            if (!hasAlt)
            {
                lexer.badAccess |= Report.MISSING_IMAGE_ALT;
                Report.attrError(lexer, node, "alt", Report.MISSING_ATTRIBUTE);
                if (lexer.configuration.altText != null)
                    node.addAttribute("alt", lexer.configuration.altText);
            }

            if (!hasSrc && !hasDataFld)
                Report.attrError(lexer, node, "src", Report.MISSING_ATTRIBUTE);

            if (hasIsMap && !hasUseMap)
                Report.attrError(lexer, node, "ismap", Report.MISSING_IMAGEMAP);
        }

    }

    public static class CheckAREA implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal attval;
            Attribute attribute;
            boolean hasAlt = false;
            boolean hasHref = false;

            node.checkUniqueAttributes(lexer);

            for (attval = node.attributes; attval != null; attval = attval.next)
            {
                attribute = attval.checkAttribute(lexer, node);

                if (attribute == AttributeTable.attrAlt)
                    hasAlt = true;
                else if (attribute == AttributeTable.attrHref)
                    hasHref = true;
            }

            if (!hasAlt)
            {
                lexer.badAccess |= Report.MISSING_LINK_ALT;
                Report.attrError(lexer, node, "alt", Report.MISSING_ATTRIBUTE);
            }
            if (!hasHref)
                Report.attrError(lexer, node, "href", Report.MISSING_ATTRIBUTE);
        }

    }

    public static class CheckAnchor implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            node.checkUniqueAttributes(lexer);

            lexer.fixId(node);
        }
    }

    public static class CheckMap implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            node.checkUniqueAttributes(lexer);

            lexer.fixId(node);
        }
    }

    public static class CheckSTYLE implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal type = node.getAttrByName("type");

            node.checkUniqueAttributes(lexer);

            if (type == null)
            {
                Report.attrError(lexer, node, "type", Report.MISSING_ATTRIBUTE);

                node.addAttribute("type", "text/css");
            }
        }
    }

    public static class CheckTableCell implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            node.checkUniqueAttributes(lexer);

            /*
             * HTML4 strict doesn't allow mixed content for elements with %block; as their content model
             */
            if (node.getAttrByName("width") != null || node.getAttrByName("height") != null)
                lexer.versions &= ~Dict.VERS_HTML40_STRICT;
        }
    }

    /* add missing type attribute when appropriate */
    public static class CheckLINK implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal rel = node.getAttrByName("rel");

            node.checkUniqueAttributes(lexer);

            if (rel != null && rel.value != null && rel.value.equals("stylesheet"))
            {
                AttVal type = node.getAttrByName("type");

                if (type == null)
                {
                    Report.attrError(lexer, node, "type", Report.MISSING_ATTRIBUTE);

                    node.addAttribute("type", "text/css");
                }
            }
        }
    }

    public static CheckAttribs getCheckHTML()
    {
        return _checkHTML;
    }

    public static CheckAttribs getCheckSCRIPT()
    {
        return _checkSCRIPT;
    }

    public static CheckAttribs getCheckTABLE()
    {
        return _checkTABLE;
    }

    public static CheckAttribs getCheckCaption()
    {
        return _checkCaption;
    }

    public static CheckAttribs getCheckIMG()
    {
        return _checkIMG;
    }

    public static CheckAttribs getCheckAREA()
    {
        return _checkAREA;
    }

    public static CheckAttribs getCheckAnchor()
    {
        return _checkAnchor;
    }

    public static CheckAttribs getCheckMap()
    {
        return _checkMap;
    }

    public static CheckAttribs getCheckSTYLE()
    {
        return _checkStyle;
    }

    public static CheckAttribs getCheckTableCell()
    {
        return _checkTableCell;
    }

    public static CheckAttribs getCheckLINK()
    {
        return _checkLINK;
    }

    public static CheckAttribs getCheckHR()
    {
        return _checkHR;
    }

    private static CheckAttribs _checkHTML = new CheckHTML();
    private static CheckAttribs _checkSCRIPT = new CheckSCRIPT();
    private static CheckAttribs _checkTABLE = new CheckTABLE();
    private static CheckAttribs _checkCaption = new CheckCaption();
    private static CheckAttribs _checkIMG = new CheckIMG();
    private static CheckAttribs _checkAREA = new CheckAREA();
    private static CheckAttribs _checkAnchor = new CheckAnchor();
    private static CheckAttribs _checkMap = new CheckMap();
    private static CheckAttribs _checkStyle = new CheckSTYLE();
    private static CheckAttribs _checkTableCell = new CheckTableCell();
    private static CheckAttribs _checkLINK = new CheckLINK();
    private static CheckAttribs _checkHR = new CheckHR();

}
