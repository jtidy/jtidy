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
 * @author Fabrizio Giustina
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
     * checker for "target" attribute.
     */
    private static AttrCheck checkTarget = new CheckTarget();

    /**
     * checker for "submit" attribute.
     */
    private static AttrCheck checkFsubmit = new CheckFsubmit();

    /**
     * checker for "clear" attribute.
     */
    private static AttrCheck checkClear = new CheckClear();

    /**
     * checker for "shape" attribute.
     */
    private static AttrCheck checkShape = new CheckShape();

    /**
     * checker for "number" attribute.
     */
    private static AttrCheck checkNumber = new CheckNumber();

    /**
     * checker for "scope" attribute.
     */
    private static AttrCheck checkScope = new CheckScope();

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
         * @todo add error messages
         * @todo add configuration option?
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            /*
             * if (attval.value == null) { lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE); }
             * else if (lexer.configuration.fixBackslash) { attval.value = attval.value.replace('\\', '/'); }
             */

            char c;
            StringBuffer dest;
            String p = attval.value;
            boolean escapeFound = false;
            boolean backslashFound = false;
            int i = 0;

            if (p == null)
            {
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
                return;
            }

            for (i = 0; i < p.length(); ++i)
            {
                c = p.charAt(i);
                // find \
                if (c == '\\')
                {
                    backslashFound = true;
                }
                // find non-ascii chars
                else if ((c > 0x7e) || (c <= 0x20) || (c == '<') || (c == '>'))
                {
                    escapeFound = true;
                }
            }

            // backslashes found, fix them
            if (lexer.configuration.fixBackslash && backslashFound)
            {
                attval.value = attval.value.replace('\\', '/');
                p = attval.value;
            }

            // non-ascii chars found, fix them
            if (lexer.configuration.fixUri && escapeFound)
            {
                dest = new StringBuffer();

                for (i = 0; i < p.length(); ++i)
                {
                    c = p.charAt(i);
                    if ((c > 0x7e) || (c <= 0x20) || (c == '<') || (c == '>'))
                    {
                        dest.append('%');
                        dest.append(Integer.toHexString(c).toUpperCase());
                    }
                    else
                    {
                        dest.append(c);
                    }
                }

                attval.value = dest.toString();
            }
            if (backslashFound)
            {
                if (lexer.configuration.fixBackslash)
                {
                    lexer.report.attrError(lexer, node, attval, Report.FIXED_BACKSLASH);
                }
                else
                {
                    lexer.report.attrError(lexer, node, attval, Report.BACKSLASH_IN_URI);
                }
            }
            if (escapeFound)
            {
                if (lexer.configuration.fixUri)
                {
                    lexer.report.attrError(lexer, node, attval, Report.ESCAPED_ILLEGAL_URI);
                }
                else
                {
                    lexer.report.attrError(lexer, node, attval, Report.ILLEGAL_URI_REFERENCE);
                }

                lexer.badChars |= Report.INVALID_URI;
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
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
            }
            else if (!(Lexer.wstrcasecmp(value, "left") == 0
                || Lexer.wstrcasecmp(value, "center") == 0
                || Lexer.wstrcasecmp(value, "right") == 0 || Lexer.wstrcasecmp(value, "justify") == 0))
            {
                lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
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
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
            }
            else if (Lexer.wstrcasecmp(value, "top") == 0
                || Lexer.wstrcasecmp(value, "middle") == 0
                || Lexer.wstrcasecmp(value, "bottom") == 0
                || Lexer.wstrcasecmp(value, "baseline") == 0)
            {
                // all is fine
            }
            else if (Lexer.wstrcasecmp(value, "left") == 0 || Lexer.wstrcasecmp(value, "right") == 0)
            {
                if (!(node.tag != null && ((node.tag.model & Dict.CM_IMG) != 0)))
                {
                    lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
                }
            }
            else if (Lexer.wstrcasecmp(value, "texttop") == 0
                || Lexer.wstrcasecmp(value, "absmiddle") == 0
                || Lexer.wstrcasecmp(value, "absbottom") == 0
                || Lexer.wstrcasecmp(value, "textbottom") == 0)
            {
                lexer.versions &= Dict.VERS_PROPRIETARY;
                lexer.report.attrError(lexer, node, attval, Report.PROPRIETARY_ATTR_VALUE);
            }
            else
            {
                lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
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
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
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
     * AttrCheck implementation for checking the "target" attribute.
     */
    public static class CheckTarget implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if (attval == null || attval.value == null || attval.value.length() == 0)
            {
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
                return;
            }

            String value = attval.value;

            // target names must begin with A-Za-z ...
            if (Character.isLetter(value.charAt(0)))
            {
                return;
            }

            // or be one of _blank, _self, _parent and _top
            if (!("_blank".equals(value) || "_self".equals(value) || "_parent".equals(value) || "_top".equals(value)))
            {
                lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
            }

        }
    }

    /**
     * AttrCheck implementation for checking the "submit" attribute.
     */
    public static class CheckFsubmit implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if (attval == null || attval.value == null || attval.value.length() == 0)
            {
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
                return;
            }

            String value = attval.value;

            if (!("get".equalsIgnoreCase(value) || "post".equalsIgnoreCase(value)))
            {
                lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
            }
        }
    }

    /**
     * AttrCheck implementation for checking the "clear" attribute.
     */
    public static class CheckClear implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if (attval == null || attval.value == null || attval.value.length() == 0)
            {
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
                return;
            }

            String value = attval.value;

            if (!("none".equalsIgnoreCase(value) || "left".equalsIgnoreCase(value) || "right".equalsIgnoreCase(value) || "all"
                .equalsIgnoreCase(value)))
            {
                lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
            }

        }
    }

    /**
     * AttrCheck implementation for checking the "shape" attribute.
     */
    public static class CheckShape implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if (attval == null || attval.value == null || attval.value.length() == 0)
            {
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
                return;
            }

            String value = attval.value;

            if (!("rect".equalsIgnoreCase(value)
                || "default".equalsIgnoreCase(value)
                || "circle".equalsIgnoreCase(value) || "poly".equalsIgnoreCase(value)))
            {
                lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
            }

        }
    }

    /**
     * AttrCheck implementation for checking Scope.
     */
    public static class CheckScope implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {

            String value = attval.value;

            if (value == null)
            {
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
            }

            if (!("row".equalsIgnoreCase(value) || "rowgroup".equalsIgnoreCase(value) //
                || "col".equalsIgnoreCase(value) || "colgroup".equalsIgnoreCase(value)))
                lexer.report.attrError(lexer, node, attval, Report.BAD_ATTRIBUTE_VALUE);
        }
    }

    /**
     * AttrCheck implementation for checking numbers.
     */
    public static class CheckNumber implements AttrCheck
    {

        /**
         * @see AttrCheck#check(Lexer, Node, AttVal)
         */
        public void check(Lexer lexer, Node node, AttVal attval)
        {

            String value = attval.value;

            if (value == null)
            {
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
            }

            int j = 0;

            // font size may be preceded by + or -
            if (node.tag == lexer.configuration.tt.tagFont && (value.startsWith("+") || value.startsWith("-")))
            {
                ++j;
            }

            for (; j < value.length(); j++)
            {
                char p = value.charAt(j);
                if (!Character.isDigit(p))
                {
                    break; // and shout: illegal number
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
                lexer.report.attrError(lexer, node, attval, Report.MISSING_ATTR_VALUE);
                return;
            }

            if (p.length() == 0 || !Character.isLetter(p.charAt(0)))
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

    /**
     * Getter for the CheckTarget instance.
     * @return checker for "target" attribute
     */
    public static AttrCheck getCheckTarget()
    {
        return checkTarget;
    }

    /**
     * Getter for the CheckFsubmit instance.
     * @return checker for "submit" attribute
     */
    public static AttrCheck getCheckFsubmit()
    {
        return checkFsubmit;
    }

    /**
     * Getter for the CheckClear instance.
     * @return checker for "clear" attribute
     */
    public static AttrCheck getCheckClear()
    {
        return checkClear;
    }

    /**
     * Getter for the CheckShape instance.
     * @return checker for "shape" attribute
     */
    public static AttrCheck getCheckShape()
    {
        return checkShape;
    }

}