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
 * Check attribute values implementations.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public final class AttrCheckImpl
{

    /**
     * checker for URLs.
     */
    private static AttrCheck checkUrl = new CheckUrl();

    /**
     * checker for scripts.
     */
    private static AttrCheck checkScript = new CheckScript();

    /**
     * checker for "name" attribute.
     */
    private static AttrCheck checkName = new CheckName();

    /**
     * checker for ids.
     */
    private static AttrCheck checkId = new CheckId();

    /**
     * checker for "align" attribute.
     */
    private static AttrCheck checkAlign = new CheckAlign();

    /**
     * checker for "valign" attribute.
     */
    private static AttrCheck checkValign = new CheckValign();

    /**
     * checker for boolean attributes.
     */
    private static AttrCheck checkBool = new CheckBool();

    /**
     * checker for "lenght" attribute.
     */
    private static AttrCheck checkLength = new CheckLength();

    /**
     * utility class, don't instantiate.
     */
    private AttrCheckImpl()
    {
        // empty private constructor
    }

    /**
     * AttrCheck implementation for checking URLs.
     */
    public static class CheckUrl implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if (attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
            }
            else if (lexer.configuration.fixBackslash)
            {
                attval.value = attval.value.replace('\\', '/');
            }
        }

    }

    /**
     * AttrCheck implementation for checking scripts.
     */
    public static class CheckScript implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
        }

    }

    /**
     * AttrCheck implementation for checking the "align" attribute.
     */
    public static class CheckAlign implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            String value;

            /* IMG, OBJECT, APPLET and EMBED use align for vertical position */
            if (node.tag != null && ((node.tag.model & Dict.CM_IMG) != 0))
            {
                getCheckValign().check(lexer, node, attval);
                return;
            }

            value = attval.value;

            if (value == null)
            {
                lexer.report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
            }
            else if (!(Lexer.wstrcasecmp(value, "left") == 0 || Lexer.wstrcasecmp(value, "center") == 0
                || Lexer.wstrcasecmp(value, "right") == 0 || Lexer.wstrcasecmp(value, "justify") == 0))
            {
                lexer.report.attrError(lexer, node, attval.value, Report.BAD_ATTRIBUTE_VALUE);
            }
        }

    }

    /**
     * AttrCheck implementation for checking the "valign" attribute.
     */
    public static class CheckValign implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            String value;

            value = attval.value;

            if (value == null)
            {
                lexer.report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
            }
            else if (Lexer.wstrcasecmp(value, "top") == 0 || Lexer.wstrcasecmp(value, "middle") == 0
                || Lexer.wstrcasecmp(value, "bottom") == 0 || Lexer.wstrcasecmp(value, "baseline") == 0)
            {
                // all is fine
            }
            else if (Lexer.wstrcasecmp(value, "left") == 0 || Lexer.wstrcasecmp(value, "right") == 0)
            {
                if (!(node.tag != null && ((node.tag.model & Dict.CM_IMG) != 0)))
                {
                    lexer.report.attrError(lexer, node, value, Report.BAD_ATTRIBUTE_VALUE);
                }
            }
            else if (Lexer.wstrcasecmp(value, "texttop") == 0 || Lexer.wstrcasecmp(value, "absmiddle") == 0
                || Lexer.wstrcasecmp(value, "absbottom") == 0 || Lexer.wstrcasecmp(value, "textbottom") == 0)
            {
                lexer.versions &= Dict.VERS_PROPRIETARY;
                lexer.report.attrError(lexer, node, value, Report.PROPRIETARY_ATTR_VALUE);
            }
            else
            {
                lexer.report.attrError(lexer, node, value, Report.BAD_ATTRIBUTE_VALUE);
            }
        }

    }

    /**
     * AttrCheck implementation for checking boolean attributes.
     */
    public static class CheckBool implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
        }

    }


    /**
     * AttrCheck implementation for checking the "length" attribute.
     */
    public static class CheckLength implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            String p = attval.value;

            if (p == null)
            {
                lexer.report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
            }
            if ("".equals(p) || !Character.isDigit(p.charAt(0)))
            {
                // shout: bad length
            }
            else
            {

                TagTable tt = lexer.configuration.tt;

                for (int j = 1; j < p.length(); j++)
                {
                    // elements th and td must not use percentages
                    if ((!Character.isDigit(p.charAt(j)) && (node.tag == tt.tagTd || node.tag == tt.tagTh))
                        || (!Character.isDigit(p.charAt(j)) && p.charAt(j) != '%'))
                    {
                        // shout: bad length
                        break;
                    }
                }
            }
        }
    }

    /**
     * AttrCheck implementation for checking ids.
     */
    public static class CheckId implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {

            String p = attval.value;
            if (p == null)
            {
                lexer.report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
                return;
            }

            if ("".equals(p) || !Character.isLetter(p.charAt(0)))
            {
                // shout: illegal ID value in HTML
            }
            else
            {

                for (int j = 1; j < p.length(); j++)
                {
                    if (!Lexer.isNamechar(p.charAt(j)))
                    {
                        // shout: illegal ID value in HTML
                        break;
                    }
                }
            }


        }

    }

    /**
     * AttrCheck implementation for checking the "name" attribute.
     */
    public static class CheckName implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
        }

    }

    /**
     * Getter for the CheckUrl instance.
     * @return checker for URLs
     */
    public static AttrCheck getCheckUrl()
    {
        return checkUrl;
    }

    /**
     * Getter for the CheckScript instance.
     * @return checker for scripts
     */
    public static AttrCheck getCheckScript()
    {
        return checkScript;
    }

    /**
     * Getter for the CheckAlign instance.
     * @return checker for "align" attribute
     */
    public static AttrCheck getCheckAlign()
    {
        return checkAlign;
    }

    /**
     * Getter for the CheckValign instance.
     * @return checker for "valign" attribute
     */
    public static AttrCheck getCheckValign()
    {
        return checkValign;
    }

    /**
     * Getter for the CheckBoolean instance.
     * @return checker for boolean attributes
     */
    public static AttrCheck getCheckBool()
    {
        return checkBool;
    }

    /**
     * Getter for the CheckId instance.
     * @return checker for ids
     */
    public static AttrCheck getCheckId()
    {
        return checkId;
    }

    /**
     * Getter for the CheckName instance.
     * @return checker for "name" attribute
     */
    public static AttrCheck getCheckName()
    {
        return checkName;
    }

    /**
     * Getter for the CheckLength instance.
     * @return checker for "length" attribute
     */
    public static AttrCheck getCheckLength()
    {
        return checkLength;
    }

}