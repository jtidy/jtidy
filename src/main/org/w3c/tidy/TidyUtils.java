/*
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
 * Utility class with handy methods, mainly for String handling or for reproducing c behaviours.
 * @author Fabrizio Giustina
 * @version $Revision $ ($Author $)
 */
public class TidyUtils
{

    /**
     * utility class, don't instantiate.
     */
    private TidyUtils()
    {
    }

    /**
     * Converts a int to a boolean.
     * @param value
     * @return
     */
    static boolean toBoolean(int value)
    {
        return value == 1;
    }

    /**
     * check if the first String contains the second one.
     * @param s1 full String
     * @param len1 maximum position in String
     * @param s2 String to search for
     * @return true if s1 contains s2 in the range 0-len1
     */
    static boolean wsubstrn(String s1, int len1, String s2)
    {
        int searchIndex = s1.indexOf(s2);
        return searchIndex > -1 && searchIndex <= len1;
    }

    /**
     * check if the first String contains the second one (ignore case).
     * @param s1 full String
     * @param len1 maximum position in String
     * @param s2 String to search for
     * @return true if s1 contains s2 in the range 0-len1
     */
    static boolean wsubstrncase(String s1, int len1, String s2)
    {
        return wsubstrn(s1.toLowerCase(), len1, s2.toLowerCase());
    }

    /**
     * return offset of cc from beginning of s1, -1 if not found.
     */
    static int wstrnchr(String s1, int len1, char cc)
    {
        int indexOf = s1.indexOf(cc);
        if (indexOf < len1)
        {
            return indexOf;
        }

        return -1;
    }

    static boolean wsubstr(String s1, String s2)
    {
        int i;
        int len1 = s1.length();
        int len2 = s2.length();

        for (i = 0; i <= len1 - len2; ++i)
        {
            if (s2.equalsIgnoreCase(s1.substring(i)))
            {
                return true;
            }
        }

        return false;
    }

}