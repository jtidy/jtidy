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
 * Input Stream.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
import java.io.InputStream;

public abstract class StreamIn {

    public static final int EndOfStream = -1; // EOF

    /* states for ISO 2022 

     A document in ISO-2022 based encoding uses some ESC sequences called 
     "designator" to switch character sets. The designators defined and 
     used in ISO-2022-JP are:

        "ESC" + "(" + ?     for ISO646 variants

        "ESC" + "$" + ?     and
        "ESC" + "$" + "(" + ?   for multibyte character sets
    */

    public static final int FSM_ASCII    = 0;
    public static final int FSM_ESC      = 1;
    public static final int FSM_ESCD     = 2;
    public static final int FSM_ESCDP    = 3;
    public static final int FSM_ESCP     = 4;
    public static final int FSM_NONASCII = 5;

    /* non-raw input is cleaned up*/
    public int state;     /* FSM for ISO2022 */
    public boolean pushed;
    public int c;
    public int tabs;
    public int tabsize;
    public int lastcol;
    public int curcol;
    public int curline;
    public int encoding;
    public InputStream stream;
    public boolean endOfStream;
    public Object lexer;  /* needed for error reporting */

    /* read char from stream */
    public abstract int readCharFromStream();

    public abstract int readChar();

    public abstract void ungetChar(int c);

    public abstract boolean isEndOfStream();

}
