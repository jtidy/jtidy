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
 * Check HTML attributes implementation.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @version $Revision $ ($Author $)
 * @author Fabrizio Giustina
 */
public final class CheckAttribsImpl
{

    /**
     * CheckHTML instance.
     */
    private static final CheckAttribs CHECK_HTML = new CheckHTML();

    /**
     * CheckSCRIPT instance.
     */
    private static final CheckAttribs CHECK_SCRIPT = new CheckSCRIPT();

    /**
     * CheckTABLE instance.
     */
    private static final CheckAttribs CHECK_TABLE = new CheckTABLE();

    /**
     * CheckCaption instance.
     */
    private static final CheckAttribs CHECK_CAPTION = new CheckCaption();

    /**
     * CheckIMG instance.
     */
    private static final CheckAttribs CHECK_IMG = new CheckIMG();

    /**
     * CheckAREA instance.
     */
    private static final CheckAttribs CHECK_AREA = new CheckAREA();

    /**
     * CheckAnchor instance.
     */
    private static final CheckAttribs CHECK_ANCHOR = new CheckAnchor();
    /**
     * CheckMap instance.
     */
    private static final CheckAttribs CHECK_MAP = new CheckMap();

    /**
     * CheckSTYLE instance.
     */
    private static final CheckAttribs CHECK_STYLE = new CheckSTYLE();

    /**
     * CheckTableCell instance.
     */
    private static final CheckAttribs CHECK_TABLECELL = new CheckTableCell();

    /**
     * CheckLINK instance.
     */
    private static final CheckAttribs CHECK_LINK = new CheckLINK();

    /**
     * CheckHR instance.
     */
    private static final CheckAttribs CHECK_HR = new CheckHR();

    /**
     * don't instantiate.
     */
    private CheckAttribsImpl()
    {
    }

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
                {
                    lexer.isvoyager = true;
                }
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
                AttVal missingType = new AttVal(null, null, '"', "type", "");
                lexer.report.attrError(lexer, node, missingType, Report.MISSING_ATTRIBUTE);

                /* check for javascript */

                if (lang != null)
                {
                    String str = lang.value;
                    if (str.length() > 10)
                    {
                        str = str.substring(0, 10);
                    }
                    if ((Lexer.wstrcasecmp(str, "javascript") == 0) || (Lexer.wstrcasecmp(str, "jscript") == 0))
                    {
                        node.addAttribute("type", "text/javascript");
                    }
                }
                else
                {
                    node.addAttribute("type", "text/javascript");
                }
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
                {
                    hasSummary = true;
                }
            }

            /* suppress warning for missing summary for HTML 2.0 and HTML 3.2 */
            if (!hasSummary && lexer.doctype != Dict.VERS_HTML20 && lexer.doctype != Dict.VERS_HTML32)
            {
                lexer.badAccess |= Report.MISSING_SUMMARY;

                AttVal missingSummary = new AttVal(null, null, '"', "summary", "");
                lexer.report.attrError(lexer, node, missingSummary, Report.MISSING_ATTRIBUTE);
            }

            /* convert <table border> to <table border="1"> */
            if (lexer.configuration.xmlOut)
            {
                attval = node.getAttrByName("border");
                if (attval != null)
                {
                    if (attval.value == null)
                    {
                        attval.value = "1";
                    }
                }
            }

            /* <table height="..."> is proprietary */
            if ((attval = node.getAttrByName("height")) != null)
            {
                lexer.report.attrError(lexer, node, attval, Report.PROPRIETARY_ATTRIBUTE);
                lexer.versions &= Dict.VERS_PROPRIETARY;
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
                {
                    lexer.versions &= (short) (Dict.VERS_HTML40_LOOSE | Dict.VERS_FRAMESET);
                }
                else if (Lexer.wstrcasecmp(value, "top") == 0 || Lexer.wstrcasecmp(value, "bottom") == 0)
                {
                    lexer.versions &= Dict.VERS_FROM32;
                }
                else
                {
                    lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
                }
            }
        }

    }

    public static class CheckHR implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            AttVal av = node.getAttrByName("src");
            if (av != null)
            {
                lexer.report.attrError(lexer, node, av, Report.PROPRIETARY_ATTR_VALUE);
            }
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
                {
                    hasAlt = true;
                }
                else if (attribute == AttributeTable.attrSrc)
                {
                    hasSrc = true;
                }
                else if (attribute == AttributeTable.attrUsemap)
                {
                    hasUseMap = true;
                }
                else if (attribute == AttributeTable.attrIsmap)
                {
                    hasIsMap = true;
                }
                else if (attribute == AttributeTable.attrDatafld)
                {
                    hasDataFld = true;
                }
                else if (attribute == AttributeTable.attrWidth || attribute == AttributeTable.attrHeight)
                {
                    lexer.versions &= ~Dict.VERS_HTML20;
                }
            }

            if (!hasAlt)
            {
                lexer.badAccess |= Report.MISSING_IMAGE_ALT;
                AttVal missingAlt = new AttVal(null, null, '"', "alt", "");
                lexer.report.attrError(lexer, node, missingAlt, Report.MISSING_ATTRIBUTE);
                if (lexer.configuration.altText != null)
                {
                    node.addAttribute("alt", lexer.configuration.altText);
                }
            }

            if (!hasSrc && !hasDataFld)
            {
                AttVal missingSrc = new AttVal(null, null, '"', "src", "");
                lexer.report.attrError(lexer, node, missingSrc, Report.MISSING_ATTRIBUTE);
            }

            if (hasIsMap && !hasUseMap)
            {
                AttVal missingIsMap = new AttVal(null, null, '"', "ismap", "");
                lexer.report.attrError(lexer, node, missingIsMap, Report.MISSING_IMAGEMAP);
            }
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
                {
                    hasAlt = true;
                }
                else if (attribute == AttributeTable.attrHref)
                {
                    hasHref = true;
                }
            }

            if (!hasAlt)
            {
                lexer.badAccess |= Report.MISSING_LINK_ALT;
                AttVal missingAlt = new AttVal(null, null, '"', "alt", "");
                lexer.report.attrError(lexer, node, missingAlt, Report.MISSING_ATTRIBUTE);
            }
            if (!hasHref)
            {
                AttVal missingHref = new AttVal(null, null, '"', "href", "");
                lexer.report.attrError(lexer, node, missingHref, Report.MISSING_ATTRIBUTE);
            }
        }

    }

    public static class CheckAnchor implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            node.checkUniqueAttributes(lexer);
            node.checkAttributes(lexer);

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
                AttVal missingType = new AttVal(null, null, '"', "type", "");
                lexer.report.attrError(lexer, node, missingType, Report.MISSING_ATTRIBUTE);

                node.addAttribute("type", "text/css");
            }
        }
    }

    public static class CheckTableCell implements CheckAttribs
    {

        public void check(Lexer lexer, Node node)
        {
            node.checkUniqueAttributes(lexer);
            node.checkAttributes(lexer);

            // HTML4 strict doesn't allow mixed content for elements with %block; as their content model

            if (node.getAttrByName("width") != null || node.getAttrByName("height") != null)
            {
                lexer.versions &= ~Dict.VERS_HTML40_STRICT;
            }
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
                    AttVal missingType = new AttVal(null, null, '"', "type", "");
                    lexer.report.attrError(lexer, node, missingType, Report.MISSING_ATTRIBUTE);

                    node.addAttribute("type", "text/css");
                }
            }
        }
    }

    public static CheckAttribs getCheckHTML()
    {
        return CHECK_HTML;
    }

    public static CheckAttribs getCheckSCRIPT()
    {
        return CHECK_SCRIPT;
    }

    public static CheckAttribs getCheckTABLE()
    {
        return CHECK_TABLE;
    }

    public static CheckAttribs getCheckCaption()
    {
        return CHECK_CAPTION;
    }

    public static CheckAttribs getCheckIMG()
    {
        return CHECK_IMG;
    }

    public static CheckAttribs getCheckAREA()
    {
        return CHECK_AREA;
    }

    public static CheckAttribs getCheckAnchor()
    {
        return CHECK_ANCHOR;
    }

    public static CheckAttribs getCheckMap()
    {
        return CHECK_MAP;
    }

    public static CheckAttribs getCheckSTYLE()
    {
        return CHECK_STYLE;
    }

    public static CheckAttribs getCheckTableCell()
    {
        return CHECK_TABLECELL;
    }

    public static CheckAttribs getCheckLINK()
    {
        return CHECK_LINK;
    }

    public static CheckAttribs getCheckHR()
    {
        return CHECK_HR;
    }

}