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
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public class AttrCheckImpl
{

    public static class CheckUrl implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if (attval.value == null)
                Report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
            else if (lexer.configuration.FixBackslash)
            {
                attval.value = attval.value.replace('\\', '/');
            }
        }

    }

    public static class CheckScript implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
        }

    }

    public static class CheckAlign implements AttrCheck
    {

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
                Report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
            else if (
                !(Lexer.wstrcasecmp(value, "left") == 0
                    || Lexer.wstrcasecmp(value, "center") == 0
                    || Lexer.wstrcasecmp(value, "right") == 0
                    || Lexer.wstrcasecmp(value, "justify") == 0))
                Report.attrError(lexer, node, attval.value, Report.BAD_ATTRIBUTE_VALUE);
        }

    }

    public static class CheckValign implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            String value;

            value = attval.value;

            if (value == null)
                Report.attrError(lexer, node, attval.attribute, Report.MISSING_ATTR_VALUE);
            else if (
                Lexer.wstrcasecmp(value, "top") == 0
                    || Lexer.wstrcasecmp(value, "middle") == 0
                    || Lexer.wstrcasecmp(value, "bottom") == 0
                    || Lexer.wstrcasecmp(value, "baseline") == 0)
            {
                /* all is fine */
            }
            else if (Lexer.wstrcasecmp(value, "left") == 0 || Lexer.wstrcasecmp(value, "right") == 0)
            {
                if (!(node.tag != null && ((node.tag.model & Dict.CM_IMG) != 0)))
                    Report.attrError(lexer, node, value, Report.BAD_ATTRIBUTE_VALUE);
            }
            else if (
                Lexer.wstrcasecmp(value, "texttop") == 0
                    || Lexer.wstrcasecmp(value, "absmiddle") == 0
                    || Lexer.wstrcasecmp(value, "absbottom") == 0
                    || Lexer.wstrcasecmp(value, "textbottom") == 0)
            {
                lexer.versions &= Dict.VERS_PROPRIETARY;
                Report.attrError(lexer, node, value, Report.PROPRIETARY_ATTR_VALUE);
            }
            else
                Report.attrError(lexer, node, value, Report.BAD_ATTRIBUTE_VALUE);
        }

    }

    public static class CheckBool implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
        }

    }

    public static class CheckId implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
        }

    }

    public static class CheckName implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
        }

    }

    public static AttrCheck getCheckUrl()
    {
        return _checkUrl;
    }

    public static AttrCheck getCheckScript()
    {
        return _checkScript;
    }

    public static AttrCheck getCheckAlign()
    {
        return _checkAlign;
    }

    public static AttrCheck getCheckValign()
    {
        return _checkValign;
    }

    public static AttrCheck getCheckBool()
    {
        return _checkBool;
    }

    public static AttrCheck getCheckId()
    {
        return _checkId;
    }

    public static AttrCheck getCheckName()
    {
        return _checkName;
    }

    private static AttrCheck _checkUrl = new CheckUrl();
    private static AttrCheck _checkScript = new CheckScript();
    private static AttrCheck _checkAlign = new CheckAlign();
    private static AttrCheck _checkValign = new CheckValign();
    private static AttrCheck _checkBool = new CheckBool();
    private static AttrCheck _checkId = new CheckId();
    private static AttrCheck _checkName = new CheckName();

}
