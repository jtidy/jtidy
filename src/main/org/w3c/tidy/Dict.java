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
 * Tag dictionary node.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision $ ($Author $)
 */
public class Dict
{

    /* content model shortcut encoding */

    public static final int CM_UNKNOWN = 0;

    public static final int CM_EMPTY = (1 << 0);

    public static final int CM_HTML = (1 << 1);

    public static final int CM_HEAD = (1 << 2);

    public static final int CM_BLOCK = (1 << 3);

    public static final int CM_INLINE = (1 << 4);

    public static final int CM_LIST = (1 << 5);

    public static final int CM_DEFLIST = (1 << 6);

    public static final int CM_TABLE = (1 << 7);

    public static final int CM_ROWGRP = (1 << 8);

    public static final int CM_ROW = (1 << 9);

    public static final int CM_FIELD = (1 << 10);

    public static final int CM_OBJECT = (1 << 11);

    public static final int CM_PARAM = (1 << 12);

    public static final int CM_FRAMES = (1 << 13);

    public static final int CM_HEADING = (1 << 14);

    public static final int CM_OPT = (1 << 15);

    public static final int CM_IMG = (1 << 16);

    public static final int CM_MIXED = (1 << 17);

    public static final int CM_NO_INDENT = (1 << 18);

    public static final int CM_OBSOLETE = (1 << 19);

    public static final int CM_NEW = (1 << 20);

    public static final int CM_OMITST = (1 << 21);

    /*
     * If the document uses just HTML 2.0 tags and attributes described it as HTML 2.0 Similarly for HTML 3.2 and the 3
     * flavors of HTML 4.0. If there are proprietary tags and attributes then describe it as HTML Proprietary. If it
     * includes the xml-lang or xmlns attributes but is otherwise HTML 2.0, 3.2 or 4.0 then describe it as one of the
     * flavors of Voyager (strict, loose or frameset).
     */

    public static final short VERS_UNKNOWN = 0;

    public static final short VERS_HTML20 = 1;

    public static final short VERS_HTML32 = 2;

    public static final short VERS_HTML40_STRICT = 4;

    public static final short VERS_HTML40_LOOSE = 8;

    public static final short VERS_FRAMESET = 16;

    public static final short VERS_XML = 32;

    public static final short VERS_NETSCAPE = 64;

    public static final short VERS_MICROSOFT = 128;

    public static final short VERS_SUN = 256;

    public static final short VERS_MALFORMED = 512;

    public static final short VERS_XHTML11 = 1024;

    public static final short VERS_ALL = (VERS_HTML20 | VERS_HTML32 | VERS_HTML40_STRICT | VERS_HTML40_LOOSE | VERS_FRAMESET);

    public static final short VERS_HTML40 = (VERS_HTML40_STRICT | VERS_HTML40_LOOSE | VERS_FRAMESET);

    public static final short VERS_LOOSE = (VERS_HTML32 | VERS_HTML40_LOOSE | VERS_FRAMESET);

    public static final short VERS_IFRAME = (VERS_HTML40_LOOSE | VERS_FRAMESET);

    public static final short VERS_FROM32 = (VERS_HTML40_STRICT | VERS_LOOSE);

    public static final short VERS_PROPRIETARY = (VERS_NETSCAPE | VERS_MICROSOFT | VERS_SUN);

    public static final short VERS_EVERYTHING = (VERS_ALL | VERS_PROPRIETARY);

    public String name;

    public short versions;

    public int model;

    public Parser parser;

    public CheckAttribs chkattrs;

    public Dict(String name, short versions, int model, Parser parser, CheckAttribs chkattrs)
    {
        this.name = name;
        this.versions = versions;
        this.model = model;
        this.parser = parser;
        this.chkattrs = chkattrs;
    }

}