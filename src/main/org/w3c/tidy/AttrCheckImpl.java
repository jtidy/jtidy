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
 * Check attribute values implementations (c) 1998-2000 (W3C) MIT, INRIA, Keio University See Tidy.java for the
 * copyright notice. Derived from <a href="http://www.w3.org/People/Raggett/tidy">HTML Tidy Release 4 Aug 2000</a>
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
