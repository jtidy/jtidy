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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;


/**
 * Pretty print parse tree. Block-level and unknown elements are printed on new lines and their contents indented 2
 * spaces Inline elements are printed inline. Inline content is wrapped on spaces (except in attribute values or
 * preformatted text, after start tags and before end tags.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public class PPrint
{

    /* page transition effects */

    public static final short EFFECT_BLEND = -1;
    public static final short EFFECT_BOX_IN = 0;
    public static final short EFFECT_BOX_OUT = 1;
    public static final short EFFECT_CIRCLE_IN = 2;
    public static final short EFFECT_CIRCLE_OUT = 3;
    public static final short EFFECT_WIPE_UP = 4;
    public static final short EFFECT_WIPE_DOWN = 5;
    public static final short EFFECT_WIPE_RIGHT = 6;
    public static final short EFFECT_WIPE_LEFT = 7;
    public static final short EFFECT_VERT_BLINDS = 8;
    public static final short EFFECT_HORZ_BLINDS = 9;
    public static final short EFFECT_CHK_ACROSS = 10;
    public static final short EFFECT_CHK_DOWN = 11;
    public static final short EFFECT_RND_DISSOLVE = 12;
    public static final short EFFECT_SPLIT_VIRT_IN = 13;
    public static final short EFFECT_SPLIT_VIRT_OUT = 14;
    public static final short EFFECT_SPLIT_HORZ_IN = 15;
    public static final short EFFECT_SPLIT_HORZ_OUT = 16;
    public static final short EFFECT_STRIPS_LEFT_DOWN = 17;
    public static final short EFFECT_STRIPS_LEFT_UP = 18;
    public static final short EFFECT_STRIPS_RIGHT_DOWN = 19;
    public static final short EFFECT_STRIPS_RIGHT_UP = 20;
    public static final short EFFECT_RND_BARS_HORZ = 21;
    public static final short EFFECT_RND_BARS_VERT = 22;
    public static final short EFFECT_RANDOM = 23;

    private static final short NORMAL = 0;
    private static final short PREFORMATTED = 1;
    private static final short COMMENT = 2;
    private static final short ATTRIBVALUE = 4;
    private static final short NOWRAP = 8;
    private static final short CDATA = 16;

    private int[] linebuf;
    private int lbufsize;
    private int linelen;
    private int wraphere;
    private boolean inAttVal;
    private boolean InString;

    private int slide = 0;
    private int count = 0;
    private Node slidecontent;

    private Configuration configuration;

    public PPrint(Configuration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * return one less that the number of bytes used by UTF-8 char <code>1010 A 1011 B 1100 C 1101 D 1110 E 1111 F</code>
     * @param str points to 1st byte
     * @param ch initialized to 1st byte
     */
    public static int getUTF8(byte[] str, int start, MutableInteger ch)
    {
        int c, n, i, bytes;

        c = str[start] & 0xFF; // Convert to unsigned.

        if ((c & 0xE0) == 0xC0) /* 110X XXXX two bytes */
        {
            n = c & 31;
            bytes = 2;
        }
        else if ((c & 0xF0) == 0xE0) /* 1110 XXXX three bytes */
        {
            n = c & 15;
            bytes = 3;
        }
        else if ((c & 0xF8) == 0xF0) /* 1111 0XXX four bytes */
        {
            n = c & 7;
            bytes = 4;
        }
        else if ((c & 0xFC) == 0xF8) /* 1111 10XX five bytes */
        {
            n = c & 3;
            bytes = 5;
        }
        else if ((c & 0xFE) == 0xFC) /* 1111 110X six bytes */
        {
            n = c & 1;
            bytes = 6;
        }
        else
        {
            /* 0XXX XXXX one byte */
            ch.value = c;
            return 0;
        }

        /* successor bytes should have the form 10XX XXXX */
        for (i = 1; i < bytes; ++i)
        {
            c = str[start + i] & 0xFF; // Convert to unsigned.
            n = (n << 6) | (c & 0x3F);
        }

        ch.value = n;
        return bytes - 1;
    }

    /* store char c as UTF-8 encoded byte stream */
    public static int putUTF8(byte[] buf, int start, int c)
    {
        if (c < 128)
        {
            buf[start++] = (byte) c;
        }
        else if (c <= 0x7FF)
        {
            buf[start++] = (byte) (0xC0 | (c >> 6));
            buf[start++] = (byte) (0x80 | (c & 0x3F));
        }
        else if (c <= 0xFFFF)
        {
            buf[start++] = (byte) (0xE0 | (c >> 12));
            buf[start++] = (byte) (0x80 | ((c >> 6) & 0x3F));
            buf[start++] = (byte) (0x80 | (c & 0x3F));
        }
        else if (c <= 0x1FFFFF)
        {
            buf[start++] = (byte) (0xF0 | (c >> 18));
            buf[start++] = (byte) (0x80 | ((c >> 12) & 0x3F));
            buf[start++] = (byte) (0x80 | ((c >> 6) & 0x3F));
            buf[start++] = (byte) (0x80 | (c & 0x3F));
        }
        else
        {
            buf[start++] = (byte) (0xF8 | (c >> 24));
            buf[start++] = (byte) (0x80 | ((c >> 18) & 0x3F));
            buf[start++] = (byte) (0x80 | ((c >> 12) & 0x3F));
            buf[start++] = (byte) (0x80 | ((c >> 6) & 0x3F));
            buf[start++] = (byte) (0x80 | (c & 0x3F));
        }

        return start;
    }

    private void addC(int c, int index)
    {
        if (index + 1 >= lbufsize)
        {
            while (index + 1 >= lbufsize)
            {
                if (lbufsize == 0)
                {
                    lbufsize = 256;
                }
                else
                {
                    lbufsize = lbufsize * 2;
                }
            }

            int[] temp = new int[lbufsize];
            if (linebuf != null)
            {
                System.arraycopy(linebuf, 0, temp, 0, index);
            }
            linebuf = temp;
        }

        linebuf[index] = c;
    }

    private void wrapLine(Out fout, int indent)
    {
        int i, p, q;

        if (wraphere == 0)
        {
            return;
        }

        for (i = 0; i < indent; ++i)
        {
            fout.outc(' ');
        }

        for (i = 0; i < wraphere; ++i)
        {
            fout.outc(linebuf[i]);
        }

        if (InString)
        {
            fout.outc(' ');
            fout.outc('\\');
        }

        fout.newline();

        if (linelen > wraphere)
        {
            p = 0;

            if (linebuf[wraphere] == ' ')
            {
                ++wraphere;
            }

            q = wraphere;
            addC('\0', linelen);

            while (true)
            {
                linebuf[p] = linebuf[q];
                if (linebuf[q] == 0)
                {
                    break;
                }
                p++;
                q++;
            }
            linelen -= wraphere;
        }
        else
        {
            linelen = 0;
        }

        wraphere = 0;
    }

    private void wrapAttrVal(Out fout, int indent, boolean inString)
    {
        int i, p, q;

        for (i = 0; i < indent; ++i)
        {
            fout.outc(' ');
        }

        for (i = 0; i < wraphere; ++i)
        {
            fout.outc(linebuf[i]);
        }

        fout.outc(' ');

        if (inString)
        {
            fout.outc('\\');
        }

        fout.newline();

        if (linelen > wraphere)
        {
            p = 0;

            if (linebuf[wraphere] == ' ')
            {
                ++wraphere;
            }

            q = wraphere;
            addC('\0', linelen);

            while (true)
            {
                linebuf[p] = linebuf[q];
                if (linebuf[q] == 0)
                {
                    break;
                }
                p++;
                q++;
            }
            linelen -= wraphere;
        }
        else
        {
            linelen = 0;
        }

        wraphere = 0;
    }

    public void flushLine(Out fout, int indent)
    {
        int i;

        if (linelen > 0)
        {
            if (indent + linelen >= this.configuration.wraplen)
            {
                wrapLine(fout, indent);
            }

            if (!inAttVal || this.configuration.indentAttributes)
            {
                for (i = 0; i < indent; ++i)
                {
                    fout.outc(' ');
                }
            }

            for (i = 0; i < linelen; ++i)
            {
                fout.outc(linebuf[i]);
            }
        }

        fout.newline();
        linelen = 0;
        wraphere = 0;
        inAttVal = false;
    }

    public void condFlushLine(Out fout, int indent)
    {
        int i;

        if (linelen > 0)
        {
            if (indent + linelen >= this.configuration.wraplen)
            {
                wrapLine(fout, indent);
            }

            if (!inAttVal || this.configuration.indentAttributes)
            {
                for (i = 0; i < indent; ++i)
                {
                    fout.outc(' ');
                }
            }

            for (i = 0; i < linelen; ++i)
            {
                fout.outc(linebuf[i]);
            }

            fout.newline();
            linelen = 0;
            wraphere = 0;
            inAttVal = false;
        }
    }

    private void printChar(int c, short mode)
    {
        String entity;

        if (c == ' ' && !((mode & (PREFORMATTED | COMMENT | ATTRIBVALUE)) != 0))
        {
            // coerce a space character to a non-breaking space
            if ((mode & NOWRAP) != 0)
            {
                // by default XML doesn't define &nbsp;
                if (this.configuration.numEntities || this.configuration.xmlTags)
                {
                    addC('&', linelen++);
                    addC('#', linelen++);
                    addC('1', linelen++);
                    addC('6', linelen++);
                    addC('0', linelen++);
                    addC(';', linelen++);
                }
                else
                {
                    // otherwise use named entity
                    addC('&', linelen++);
                    addC('n', linelen++);
                    addC('b', linelen++);
                    addC('s', linelen++);
                    addC('p', linelen++);
                    addC(';', linelen++);
                }
                return;
            }
            else
            {
                wraphere = linelen;
            }
        }

        // comment characters are passed raw
        if ((mode & COMMENT) != 0)
        {
            addC(c, linelen++);
            return;
        }

        // except in CDATA map < to &lt; etc.
        if (!((mode & CDATA) != 0))
        {
            if (c == '<')
            {
                addC('&', linelen++);
                addC('l', linelen++);
                addC('t', linelen++);
                addC(';', linelen++);
                return;
            }

            if (c == '>')
            {
                addC('&', linelen++);
                addC('g', linelen++);
                addC('t', linelen++);
                addC(';', linelen++);
                return;
            }

            // naked '&' chars can be left alone or quoted as &amp;
            // The latter is required for XML where naked '&' are illegal.
            if (c == '&' && this.configuration.quoteAmpersand)
            {
                addC('&', linelen++);
                addC('a', linelen++);
                addC('m', linelen++);
                addC('p', linelen++);
                addC(';', linelen++);
                return;
            }

            if (c == '"' && this.configuration.quoteMarks)
            {
                addC('&', linelen++);
                addC('q', linelen++);
                addC('u', linelen++);
                addC('o', linelen++);
                addC('t', linelen++);
                addC(';', linelen++);
                return;
            }

            if (c == '\'' && this.configuration.quoteMarks)
            {
                addC('&', linelen++);
                addC('#', linelen++);
                addC('3', linelen++);
                addC('9', linelen++);
                addC(';', linelen++);
                return;
            }

            if (c == 160 && this.configuration.charEncoding != Configuration.RAW)
            {
                if (this.configuration.quoteNbsp)
                {
                    addC('&', linelen++);

                    if (this.configuration.numEntities || this.configuration.xmlTags)
                    {
                        addC('#', linelen++);
                        addC('1', linelen++);
                        addC('6', linelen++);
                        addC('0', linelen++);
                    }
                    else
                    {
                        addC('n', linelen++);
                        addC('b', linelen++);
                        addC('s', linelen++);
                        addC('p', linelen++);
                    }

                    addC(';', linelen++);
                }
                else
                {
                    addC(c, linelen++);
                }

                return;
            }
        }

        // otherwise ISO 2022 characters are passed raw
        if (this.configuration.charEncoding == Configuration.ISO2022
            || this.configuration.charEncoding == Configuration.RAW)
        {
            addC(c, linelen++);
            return;
        }

        // if preformatted text, map &nbsp; to space
        if (c == 160 && ((mode & PREFORMATTED) != 0))
        {
            addC(' ', linelen++);
            return;
        }

        // Filters from Word and PowerPoint often use smart quotes resulting in character codes between 128 and 159.
        // Unfortunately, the corresponding HTML 4.0 entities for these are not widely supported.
        // The following converts dashes and quotation marks to the nearest ASCII equivalent.
        // My thanks to Andrzej Novosiolov for his help with this code.

        if (this.configuration.makeClean)
        {
            if (c >= 0x2013 && c <= 0x201E)
            {
                switch (c)
                {
                    case 0x2013 :
                    case 0x2014 :
                        c = '-';
                        break;
                    case 0x2018 :
                    case 0x2019 :
                    case 0x201A :
                        c = '\'';
                        break;
                    case 0x201C :
                    case 0x201D :
                    case 0x201E :
                        c = '"';
                        break;
                }
            }
        }

        /* don't map latin-1 chars to entities */
        if (this.configuration.charEncoding == Configuration.LATIN1)
        {
            if (c > 255) /* multi byte chars */
            {
                if (!this.configuration.numEntities)
                {
                    entity = EntityTable.getDefaultEntityTable().entityName((short) c);
                    if (entity != null)
                    {
                        entity = "&" + entity + ";";
                    }
                    else
                    {
                        entity = "&#" + c + ";";
                    }
                }
                else
                {
                    entity = "&#" + c + ";";
                }

                for (int i = 0; i < entity.length(); i++)
                {
                    addC(entity.charAt(i), linelen++);
                }

                return;
            }

            if (c > 126 && c < 160)
            {
                entity = "&#" + c + ";";

                for (int i = 0; i < entity.length(); i++)
                {
                    addC(entity.charAt(i), linelen++);
                }

                return;
            }

            addC(c, linelen++);
            return;
        }

        // don't map utf8 chars to entities
        if (this.configuration.charEncoding == Configuration.UTF8)
        {
            addC(c, linelen++);
            return;
        }

        // use numeric entities only for XML
        if (this.configuration.xmlTags)
        {
            // if ASCII use numeric entities for chars > 127
            if (c > 127 && this.configuration.charEncoding == Configuration.ASCII)
            {
                entity = "&#" + c + ";";

                for (int i = 0; i < entity.length(); i++)
                {
                    addC(entity.charAt(i), linelen++);
                }

                return;
            }

            // otherwise output char raw
            addC(c, linelen++);
            return;
        }

        // default treatment for ASCII
        if (c > 126 || (c < ' ' && c != '\t'))
        {
            if (!this.configuration.numEntities)
            {
                entity = EntityTable.getDefaultEntityTable().entityName((short) c);
                if (entity != null)
                {
                    entity = "&" + entity + ";";
                }
                else
                {
                    entity = "&#" + c + ";";
                }
            }
            else
            {
                entity = "&#" + c + ";";
            }

            for (int i = 0; i < entity.length(); i++)
            {
                addC(entity.charAt(i), linelen++);
            }

            return;
        }

        addC(c, linelen++);
    }

    /**
     * The line buffer is uint not char so we can hold Unicode values unencoded. The translation to UTF-8 is deferred
     * to the outc routine called to flush the line buffer.
     */
    private void printText(Out fout, short mode, int indent, byte[] textarray, int start, int end)
    {
        int i, c;
        MutableInteger ci = new MutableInteger();

        for (i = start; i < end; ++i)
        {
            if (indent + linelen >= this.configuration.wraplen)
            {
                wrapLine(fout, indent);
            }

            c = (textarray[i]) & 0xFF; // Convert to unsigned.

            // look for UTF-8 multibyte character
            if (c > 0x7F)
            {
                i += getUTF8(textarray, i, ci);
                c = ci.value;
            }

            if (c == '\n')
            {
                flushLine(fout, indent);
                continue;
            }

            printChar(c, mode);
        }
    }

    private void printString(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            addC(str.charAt(i), linelen++);
        }
    }

    private void printAttrValue(Out fout, int indent, String value, int delim, boolean wrappable)
    {
        int c;
        MutableInteger ci = new MutableInteger();
        boolean wasinstring = false;
        byte[] valueChars = null;
        int i;
        short mode = (wrappable ? (short) (NORMAL | ATTRIBVALUE) : (short) (PREFORMATTED | ATTRIBVALUE));

        if (value != null)
        {
            valueChars = Lexer.getBytes(value);
        }

        // look for ASP, Tango or PHP instructions for computed attribute value
        if (valueChars != null && valueChars.length >= 5 && valueChars[0] == '<')
        {
            if (valueChars[1] == '%' || valueChars[1] == '@' || (new String(valueChars, 0, 5)).equals("<?php"))
            {
                mode |= CDATA;
            }
        }

        if (delim == 0)
        {
            delim = '"';
        }

        addC('=', linelen++);

        // don't wrap after "=" for xml documents
        if (!this.configuration.xmlOut)
        {

            if (indent + linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }

            if (indent + linelen >= this.configuration.wraplen)
            {
                wrapLine(fout, indent);
            }

            if (indent + linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }
            else
            {
                condFlushLine(fout, indent);
            }
        }

        addC(delim, linelen++);

        if (value != null)
        {
            InString = false;

            i = 0;
            while (i < valueChars.length)
            {
                c = (valueChars[i]) & 0xFF; // Convert to unsigned.

                if (wrappable && c == ' ' && indent + linelen < this.configuration.wraplen)
                {
                    wraphere = linelen;
                    wasinstring = InString;
                }

                if (wrappable && wraphere > 0 && indent + linelen >= this.configuration.wraplen)
                {
                    wrapAttrVal(fout, indent, wasinstring);
                }

                if (c == delim)
                {
                    String entity;

                    entity = (c == '"' ? "&quot;" : "&#39;");

                    for (int j = 0; j < entity.length(); j++)
                    {
                        addC(entity.charAt(j), linelen++);
                    }

                    ++i;
                    continue;
                }
                else if (c == '"')
                {
                    if (this.configuration.quoteMarks)
                    {
                        addC('&', linelen++);
                        addC('q', linelen++);
                        addC('u', linelen++);
                        addC('o', linelen++);
                        addC('t', linelen++);
                        addC(';', linelen++);
                    }
                    else
                    {
                        addC('"', linelen++);
                    }

                    if (delim == '\'')
                    {
                        InString = !InString;
                    }

                    ++i;
                    continue;
                }
                else if (c == '\'')
                {
                    if (this.configuration.quoteMarks)
                    {
                        addC('&', linelen++);
                        addC('#', linelen++);
                        addC('3', linelen++);
                        addC('9', linelen++);
                        addC(';', linelen++);
                    }
                    else
                    {
                        addC('\'', linelen++);
                    }

                    if (delim == '"')
                    {
                        InString = !InString;
                    }

                    ++i;
                    continue;
                }

                // look for UTF-8 multibyte character
                if (c > 0x7F)
                {
                    i += getUTF8(valueChars, i, ci);
                    c = ci.value;
                }

                ++i;

                if (c == '\n')
                {
                    flushLine(fout, indent);
                    continue;
                }

                printChar(c, mode);
            }
        }

        InString = false;
        addC(delim, linelen++);
    }

    private void printAttribute(Out fout, int indent, Node node, AttVal attr)
    {
        String name;
        boolean wrappable = false;

        if (this.configuration.indentAttributes)
        {
            flushLine(fout, indent);
            indent += this.configuration.spaces;
        }

        name = attr.attribute;

        if (indent + linelen >= this.configuration.wraplen)
        {
            wrapLine(fout, indent);
        }

        if (!this.configuration.xmlTags && !this.configuration.xmlOut && attr.dict != null)
        {
            if (AttributeTable.getDefaultAttributeTable().isScript(name))
            {
                wrappable = this.configuration.wrapScriptlets;
            }
            else if (!attr.dict.isNowrap() && this.configuration.wrapAttVals)
            {
                wrappable = true;
            }
        }

        if (indent + linelen < this.configuration.wraplen)
        {
            wraphere = linelen;
            addC(' ', linelen++);
        }
        else
        {
            condFlushLine(fout, indent);
            addC(' ', linelen++);
        }

        for (int i = 0; i < name.length(); i++)
        {
            addC(Lexer.foldCase(name.charAt(i), this.configuration.upperCaseAttrs, this.configuration.xmlTags),
                linelen++);
        }

        if (indent + linelen >= this.configuration.wraplen)
        {
            wrapLine(fout, indent);
        }

        if (attr.value == null)
        {
            if (this.configuration.xmlTags || this.configuration.xmlOut)
            {
                printAttrValue(fout, indent, attr.attribute, attr.delim, true);
            }
            else if (!attr.isBoolAttribute() && !Node.isNewNode(node))
            {
                printAttrValue(fout, indent, "", attr.delim, true);
            }
            else if (indent + linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }

        }
        else
        {
            printAttrValue(fout, indent, attr.value, attr.delim, wrappable);
        }
    }

    private void printAttrs(Out fout, int indent, Node node, AttVal attr)
    {
        if (attr != null)
        {
            if (attr.next != null)
            {
                printAttrs(fout, indent, node, attr.next);
            }

            if (attr.attribute != null)
            {
                printAttribute(fout, indent, node, attr);
            }
            else if (attr.asp != null)
            {
                addC(' ', linelen++);
                printAsp(fout, indent, attr.asp);
            }
            else if (attr.php != null)
            {
                addC(' ', linelen++);
                printPhp(fout, indent, attr.php);
            }
        }

        // add xml:space attribute to pre and other elements
        if (configuration.xmlOut && configuration.xmlSpace && ParserImpl.XMLPreserveWhiteSpace(node, configuration.tt)
            && node.getAttrByName("xml:space") == null)
        {
            printString(" xml:space=\"preserve\"");
        }
    }

    /**
     * Line can be wrapped immediately after inline start tag provided if follows a text node ending in a space, or it
     * parent is an inline element that that rule applies to. This behaviour was reverse engineered from Netscape 3.0
     */
    private static boolean afterSpace(Node node)
    {
        Node prev;
        int c;

        if (node == null || node.tag == null || !((node.tag.model & Dict.CM_INLINE) != 0))
        {
            return true;
        }

        prev = node.prev;

        if (prev != null)
        {
            if (prev.type == Node.TextNode && prev.end > prev.start)
            {
                c = (prev.textarray[prev.end - 1]) & 0xFF; // Convert to unsigned.

                if (c == 160 || c == ' ' || c == '\n')
                {
                    return true;
                }
            }

            return false;
        }

        return afterSpace(node.parent);
    }

    private void printTag(Lexer lexer, Out fout, short mode, int indent, Node node)
    {
        String p;
        TagTable tt = this.configuration.tt;

        addC('<', linelen++);

        if (node.type == Node.EndTag)
        {
            addC('/', linelen++);
        }

        p = node.element;
        for (int i = 0; i < p.length(); i++)
        {
            addC(Lexer.foldCase(p.charAt(i), this.configuration.upperCaseTags, this.configuration.xmlTags), linelen++);
        }

        printAttrs(fout, indent, node, node.attributes);

        if ((this.configuration.xmlOut || lexer != null && lexer.isvoyager)
            && (node.type == Node.StartEndTag || (node.tag.model & Dict.CM_EMPTY) != 0))
        {
            addC(' ', linelen++); // compatibility hack
            addC('/', linelen++);
        }

        addC('>', linelen++);

        if (node.type != Node.StartEndTag && !((mode & PREFORMATTED) != 0))
        {
            if (indent + linelen >= this.configuration.wraplen)
            {
                wrapLine(fout, indent);
            }

            if (indent + linelen < this.configuration.wraplen)
            {

                // wrap after start tag if is <br/> or if it's not inline
                // fix for [514348]
                if (!((mode & NOWRAP) != 0) && (!((node.tag.model & Dict.CM_INLINE) != 0) || (node.tag == tt.tagBr))
                    && afterSpace(node))
                {
                    wraphere = linelen;
                }

            }
        }
        else
        {
            condFlushLine(fout, indent);
        }

    }

    private void printEndTag(short mode, int indent, Node node)
    {
        String p;

        // Netscape ignores SGML standard by not ignoring a line break before </A> or </U> etc.
        // To avoid rendering this as an underlined space, I disable line wrapping before inline end tags
        // by the #if 0 ... #endif

        if (false)
        {
            if (indent + linelen < this.configuration.wraplen && !((mode & NOWRAP) != 0))
            {
                wraphere = linelen;
            }
        }

        addC('<', linelen++);
        addC('/', linelen++);

        p = node.element;
        for (int i = 0; i < p.length(); i++)
        {
            addC(Lexer.foldCase(p.charAt(i), this.configuration.upperCaseTags, this.configuration.xmlTags), linelen++);
        }

        addC('>', linelen++);
    }

    private void printComment(Out fout, int indent, Node node)
    {
        if (indent + linelen < this.configuration.wraplen)
        {
            wraphere = linelen;
        }

        addC('<', linelen++);
        addC('!', linelen++);
        addC('-', linelen++);
        addC('-', linelen++);
        if (false)
        {
            if (linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }
        }
        printText(fout, COMMENT, indent, node.textarray, node.start, node.end);
        if (false)
        {
            if (indent + linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }
        }
        // See Lexer.java: AQ 8Jul2000
        addC('-', linelen++);
        addC('-', linelen++);
        addC('>', linelen++);

        if (node.linebreak)
        {
            flushLine(fout, indent);
        }
    }

    private void printDocType(Out fout, int indent, Lexer lexer, Node node)
    {
        int i, c = 0;
        short mode = 0;
        boolean q = this.configuration.quoteMarks;

        this.configuration.quoteMarks = false;

        if (indent + linelen < this.configuration.wraplen)
        {
            wraphere = linelen;
        }

        condFlushLine(fout, indent);

        addC('<', linelen++);
        addC('!', linelen++);
        addC('D', linelen++);
        addC('O', linelen++);
        addC('C', linelen++);
        addC('T', linelen++);
        addC('Y', linelen++);
        addC('P', linelen++);
        addC('E', linelen++);
        addC(' ', linelen++);

        if (indent + linelen < this.configuration.wraplen)
        {
            wraphere = linelen;
        }

        for (i = node.start; i < node.end; ++i)
        {
            if (indent + linelen >= this.configuration.wraplen)
            {
                wrapLine(fout, indent);
            }

            c = lexer.lexbuf[i] & 0xFF; // Convert to unsigned.

            // inDTDSubset?
            if ((mode & CDATA) != 0)
            {
                if (c == ']')
                {
                    mode &= ~CDATA;
                }
            }
            else if (c == '[')
            {
                mode |= CDATA;
            }
            MutableInteger ci = new MutableInteger();

            // look for UTF-8 multibyte character
            if (c > 0x7F)
            {
                i += getUTF8(lexer.lexbuf, i, ci);
                c = ci.value;
            }

            if (c == '\n')
            {
                flushLine(fout, indent);
                continue;
            }

            printChar(c, mode);
        }

        if (linelen < this.configuration.wraplen)
        {
            wraphere = linelen;
        }

        addC('>', linelen++);
        this.configuration.quoteMarks = q;
        condFlushLine(fout, indent);
    }


    private void printPI(Out fout, int indent, Node node)
    {
        if (indent + linelen < this.configuration.wraplen)
        {
            wraphere = linelen;
        }

        addC('<', linelen++);
        addC('?', linelen++);

        // set CDATA to pass < and > unescaped
        printText(fout, CDATA, indent, node.textarray, node.start, node.end);

        if (node.textarray[node.end - 1] != (byte) '?')
        {
            addC('?', linelen++);
        }

        addC('>', linelen++);
        condFlushLine(fout, indent);
    }

    /**
     * note ASP and JSTE share <% ... %> syntax
     */
    private void printAsp(Out fout, int indent, Node node)
    {
        int savewraplen = this.configuration.wraplen;

        // disable wrapping if so requested

        if (!this.configuration.wrapAsp || !this.configuration.wrapJste)
        {
            this.configuration.wraplen = 0xFFFFFF; // a very large number
        }
        if (false)
        { //#if 0
            if (indent + linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }
        } //#endif

        addC('<', linelen++);
        addC('%', linelen++);

        printText(fout, (this.configuration.wrapAsp ? CDATA : COMMENT), indent, node.textarray, node.start, node.end);

        addC('%', linelen++);
        addC('>', linelen++);
        /* condFlushLine(fout, indent); */
        this.configuration.wraplen = savewraplen;
    }

    /**
     * JSTE also supports <# ... #> syntax
     */
    private void printJste(Out fout, int indent, Node node)
    {
        int savewraplen = this.configuration.wraplen;

        // disable wrapping if so requested

        if (!this.configuration.wrapJste)
        {
            this.configuration.wraplen = 0xFFFFFF; // a very large number
        }

        addC('<', linelen++);
        addC('#', linelen++);

        printText(fout, (this.configuration.wrapJste ? CDATA : COMMENT), indent, node.textarray, node.start, node.end);

        addC('#', linelen++);
        addC('>', linelen++);
        // condFlushLine(fout, indent);
        this.configuration.wraplen = savewraplen;
    }

    /**
     * PHP is based on XML processing instructions
     */
    private void printPhp(Out fout, int indent, Node node)
    {
        int savewraplen = this.configuration.wraplen;

        // disable wrapping if so requested

        if (!this.configuration.wrapPhp)
        {
            this.configuration.wraplen = 0xFFFFFF; // a very large number
        }

        if (false)
        { //#if 0
            if (indent + linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }
        } //#endif
        addC('<', linelen++);
        addC('?', linelen++);

        printText(fout, (this.configuration.wrapPhp ? CDATA : COMMENT), indent, node.textarray, node.start, node.end);

        addC('?', linelen++);
        addC('>', linelen++);
        // PCondFlushLine(fout, indent);
        this.configuration.wraplen = savewraplen;
    }

    private void printCDATA(Out fout, int indent, Node node)
    {
        int savewraplen = this.configuration.wraplen;

        condFlushLine(fout, indent);

        // disable wrapping

        this.configuration.wraplen = 0xFFFFFF; // a very large number

        addC('<', linelen++);
        addC('!', linelen++);
        addC('[', linelen++);
        addC('C', linelen++);
        addC('D', linelen++);
        addC('A', linelen++);
        addC('T', linelen++);
        addC('A', linelen++);
        addC('[', linelen++);

        printText(fout, COMMENT, indent, node.textarray, node.start, node.end);

        addC(']', linelen++);
        addC(']', linelen++);
        addC('>', linelen++);
        condFlushLine(fout, indent);
        this.configuration.wraplen = savewraplen;
    }

    private void printSection(Out fout, int indent, Node node)
    {
        int savewraplen = this.configuration.wraplen;

        // disable wrapping if so requested

        if (!this.configuration.wrapSection)
        {
            this.configuration.wraplen = 0xFFFFFF; // a very large number
        }

        if (false)
        { //#if 0
            if (indent + linelen < this.configuration.wraplen)
            {
                wraphere = linelen;
            }
        } //#endif
        addC('<', linelen++);
        addC('!', linelen++);
        addC('[', linelen++);

        printText(fout, (this.configuration.wrapSection ? CDATA : COMMENT), indent, node.textarray, node.start,
            node.end);

        addC(']', linelen++);
        addC('>', linelen++);
        // PCondFlushLine(fout, indent);
        this.configuration.wraplen = savewraplen;
    }

    private boolean shouldIndent(Node node)
    {
        TagTable tt = this.configuration.tt;

        if (!this.configuration.indentContent)
        {
            return false;
        }

        if (this.configuration.smartIndent)
        {
            if (node.content != null && ((node.tag.model & Dict.CM_NO_INDENT) != 0))
            {
                for (node = node.content; node != null; node = node.next)
                {
                    if (node.tag != null && (node.tag.model & Dict.CM_BLOCK) != 0)
                    {
                        return true;
                    }
                }

                return false;
            }

            if ((node.tag.model & Dict.CM_HEADING) != 0)
            {
                return false;
            }

            if (node.tag == tt.tagP)
            {
                return false;
            }

            if (node.tag == tt.tagTitle)
            {
                return false;
            }
        }

        if ((node.tag.model & (Dict.CM_FIELD | Dict.CM_OBJECT)) != 0)
        {
            return true;
        }

        if (node.tag == tt.tagMap)
        {
            return true;
        }

        return !((node.tag.model & Dict.CM_INLINE) != 0);
    }

    public void printTree(Out fout, short mode, int indent, Lexer lexer, Node node)
    {
        Node content, last;
        TagTable tt = this.configuration.tt;

        if (node == null)
        {
            return;
        }

        if (node.type == Node.TextNode)
        {
            printText(fout, mode, indent, node.textarray, node.start, node.end);
        }
        else if (node.type == Node.CommentTag)
        {
            printComment(fout, indent, node);
        }
        else if (node.type == Node.RootNode)
        {
            for (content = node.content; content != null; content = content.next)
            {
                printTree(fout, mode, indent, lexer, content);
            }
        }
        else if (node.type == Node.DocTypeTag)
        {
            printDocType(fout, indent, lexer, node);
        }
        else if (node.type == Node.ProcInsTag)
        {
            printPI(fout, indent, node);
        }
        else if (node.type == Node.CDATATag)
        {
            printCDATA(fout, indent, node);
        }
        else if (node.type == Node.SectionTag)
        {
            printSection(fout, indent, node);
        }
        else if (node.type == Node.AspTag)
        {
            printAsp(fout, indent, node);
        }
        else if (node.type == Node.JsteTag)
        {
            printJste(fout, indent, node);
        }
        else if (node.type == Node.PhpTag)
        {
            printPhp(fout, indent, node);
        }
        else if ((node.tag.model & Dict.CM_EMPTY) != 0 || node.type == Node.StartEndTag)
        {
            if (!((node.tag.model & Dict.CM_INLINE) != 0))
            {
                condFlushLine(fout, indent);
            }

            if (node.tag == tt.tagBr && node.prev != null && node.prev.tag != tt.tagBr
                && this.configuration.breakBeforeBR)
            {
                flushLine(fout, indent);
            }

            if (this.configuration.makeClean && node.tag == tt.tagWbr)
            {
                printString(" ");
            }
            else
            {
                printTag(lexer, fout, mode, indent, node);
            }

            if (node.tag == tt.tagParam || node.tag == tt.tagArea)
            {
                condFlushLine(fout, indent);
            }
            else if (node.tag == tt.tagBr || node.tag == tt.tagHr)
            {
                flushLine(fout, indent);
            }
        }
        else
        {
            // some kind of container element
            if (node.tag != null && node.tag.parser == ParserImpl.getParsePre())
            {
                condFlushLine(fout, indent);

                indent = 0;
                condFlushLine(fout, indent);
                printTag(lexer, fout, mode, indent, node);
                flushLine(fout, indent);

                for (content = node.content; content != null; content = content.next)
                {
                    printTree(fout, (short) (mode | PREFORMATTED | NOWRAP), indent, lexer, content);
                }

                condFlushLine(fout, indent);
                printEndTag(mode, indent, node);
                flushLine(fout, indent);

                if (!this.configuration.indentContent && node.next != null)
                {
                    flushLine(fout, indent);
                }
            }
            else if (node.tag == tt.tagStyle || node.tag == tt.tagScript)
            {
                condFlushLine(fout, indent);

                indent = 0;
                condFlushLine(fout, indent);
                printTag(lexer, fout, mode, indent, node);
                flushLine(fout, indent);

                for (content = node.content; content != null; content = content.next)
                {
                    printTree(fout, (short) (mode | PREFORMATTED | NOWRAP | CDATA), indent, lexer, content);
                }

                condFlushLine(fout, indent);
                printEndTag(mode, indent, node);
                flushLine(fout, indent);

                if (!this.configuration.indentContent && node.next != null)
                {
                    flushLine(fout, indent);

                }
            }
            else if ((node.tag.model & Dict.CM_INLINE) != 0)
            {
                if (this.configuration.makeClean)
                {
                    // discards <font> and </font> tags
                    if (node.tag == tt.tagFont)
                    {
                        for (content = node.content; content != null; content = content.next)
                        {
                            printTree(fout, mode, indent, lexer, content);
                        }
                        return;
                    }

                    // replace <nobr> ... </nobr> by &nbsp; or &#160; etc.
                    if (node.tag == tt.tagNobr)
                    {
                        for (content = node.content; content != null; content = content.next)
                        {
                            printTree(fout, (short) (mode | NOWRAP), indent, lexer, content);
                        }
                        return;
                    }
                }

                // otherwise a normal inline element

                printTag(lexer, fout, mode, indent, node);

                // indent content for SELECT, TEXTAREA, MAP, OBJECT and APPLET

                if (shouldIndent(node))
                {
                    condFlushLine(fout, indent);
                    indent += this.configuration.spaces;

                    for (content = node.content; content != null; content = content.next)
                    {
                        printTree(fout, mode, indent, lexer, content);
                    }

                    condFlushLine(fout, indent);
                    indent -= this.configuration.spaces;
                    condFlushLine(fout, indent);
                }
                else
                {

                    for (content = node.content; content != null; content = content.next)
                    {
                        printTree(fout, mode, indent, lexer, content);
                    }
                }

                printEndTag(mode, indent, node);
            }
            else
            {
                // other tags
                condFlushLine(fout, indent);

                if (this.configuration.smartIndent && node.prev != null)
                {
                    flushLine(fout, indent);
                }

                if (!this.configuration.hideEndTags || !(node.tag != null && ((node.tag.model & Dict.CM_OMITST) != 0)))
                {
                    printTag(lexer, fout, mode, indent, node);

                    if (shouldIndent(node))
                    {
                        condFlushLine(fout, indent);
                    }
                    else if ((node.tag.model & Dict.CM_HTML) != 0 || node.tag == tt.tagNoframes
                        || ((node.tag.model & Dict.CM_HEAD) != 0 && !(node.tag == tt.tagTitle)))
                    {
                        flushLine(fout, indent);
                    }
                }

                if (node.tag == tt.tagBody && this.configuration.burstSlides)
                {
                    printSlide(fout, mode, (this.configuration.indentContent
                        ? indent + this.configuration.spaces
                        : indent), lexer);
                }
                else
                {
                    last = null;

                    for (content = node.content; content != null; content = content.next)
                    {
                        // kludge for naked text before block level tag
                        if (last != null && !this.configuration.indentContent && last.type == Node.TextNode
                            && content.tag != null && (content.tag.model & Dict.CM_BLOCK) != 0)
                        {
                            flushLine(fout, indent);
                            flushLine(fout, indent);
                        }

                        printTree(fout, mode, (shouldIndent(node) ? indent + this.configuration.spaces : indent),
                            lexer, content);

                        last = content;
                    }
                }

                // don't flush line for td and th
                if (shouldIndent(node)
                    || (((node.tag.model & Dict.CM_HTML) != 0 || node.tag == tt.tagNoframes || ((node.tag.model & Dict.CM_HEAD) != 0 && !(node.tag == tt.tagTitle))) && !this.configuration.hideEndTags))
                {
                    condFlushLine(fout,
                        (this.configuration.indentContent ? indent + this.configuration.spaces : indent));

                    if (!this.configuration.hideEndTags || !((node.tag.model & Dict.CM_OPT) != 0))
                    {
                        printEndTag(mode, indent, node);

                        // #603128 tidy adds newslines after </html> tag
                        // Fix by Fabrizio Giustina 12-02-2004
                        // fix is different from the one in original tidy
                        if (lexer.seenEndHtml == 0)
                        {
                            flushLine(fout, indent);
                        }
                    }
                }
                else
                {
                    if (!this.configuration.hideEndTags || !((node.tag.model & Dict.CM_OPT) != 0))
                    {
                        printEndTag(mode, indent, node);
                    }

                    flushLine(fout, indent);
                }

                if (!this.configuration.indentContent && node.next != null && !this.configuration.hideEndTags
                    && (node.tag.model & (Dict.CM_BLOCK | Dict.CM_LIST | Dict.CM_DEFLIST | Dict.CM_TABLE)) != 0)
                {
                    flushLine(fout, indent);
                }
            }
        }
    }

    public void printXMLTree(Out fout, short mode, int indent, Lexer lexer, Node node)
    {
        TagTable tt = this.configuration.tt;

        if (node == null)
        {
            return;
        }

        if (node.type == Node.TextNode)
        {
            printText(fout, mode, indent, node.textarray, node.start, node.end);
        }
        else if (node.type == Node.CommentTag)
        {
            condFlushLine(fout, indent);
            printComment(fout, 0, node);
            condFlushLine(fout, 0);
        }
        else if (node.type == Node.RootNode)
        {
            Node content;

            for (content = node.content; content != null; content = content.next)
            {
                printXMLTree(fout, mode, indent, lexer, content);
            }
        }
        else if (node.type == Node.DocTypeTag)
        {
            printDocType(fout, indent, lexer, node);
        }
        else if (node.type == Node.ProcInsTag)
        {
            printPI(fout, indent, node);
        }
        else if (node.type == Node.CDATATag)
        {
            printCDATA(fout, indent, node);
        }
        else if (node.type == Node.SectionTag)
        {
            printSection(fout, indent, node);
        }
        else if (node.type == Node.AspTag)
        {
            printAsp(fout, indent, node);
        }
        else if (node.type == Node.JsteTag)
        {
            printJste(fout, indent, node);
        }
        else if (node.type == Node.PhpTag)
        {
            printPhp(fout, indent, node);
        }
        else if ((node.tag.model & Dict.CM_EMPTY) != 0 || node.type == Node.StartEndTag)
        {
            condFlushLine(fout, indent);
            printTag(lexer, fout, mode, indent, node);
            flushLine(fout, indent);

            // CPR: folks don't want so much vertical spacing in XML
            // if (node.next != null) { flushLine(fout, indent); }

        }
        else
        {
            // some kind of container element
            Node content;
            boolean mixed = false;
            int cindent;

            for (content = node.content; content != null; content = content.next)
            {
                if (content.type == Node.TextNode)
                {
                    mixed = true;
                    break;
                }
            }

            condFlushLine(fout, indent);

            if (ParserImpl.XMLPreserveWhiteSpace(node, tt))
            {
                indent = 0;
                cindent = 0;
                mixed = false;
            }
            else if (mixed)
            {
                cindent = indent;
            }
            else
            {
                cindent = indent + this.configuration.spaces;
            }

            printTag(lexer, fout, mode, indent, node);

            if (!mixed && node.content != null)
            {
                flushLine(fout, indent);
            }

            for (content = node.content; content != null; content = content.next)
            {
                printXMLTree(fout, mode, cindent, lexer, content);
            }

            if (!mixed && node.content != null)
            {
                condFlushLine(fout, cindent);
            }
            printEndTag(mode, indent, node);
            condFlushLine(fout, indent);

            // CPR: folks don't want so much vertical spacing in XML
            // if (node.next != null) { flushLine(fout, indent); }

        }
    }

    /**
     * Split parse tree by h2 elements and output to separate files. counts number of h2 children (if any) belonging to
     * node.
     */
    public int countSlides(Node node)
    {
        // assume minimum of 1 slide
        int n = 1;

        TagTable tt = this.configuration.tt;

        //fix for [431716] avoid empty slides
        if (node != null && node.content != null && node.content.tag == tt.tagH2)
        {
            // "first" slide is empty, so ignore it
            n--;
        }

        for (node = node.content; node != null; node = node.next)
        {
            if (node.tag == tt.tagH2)
            {
                ++n;
            }
        }

        return n;
    }

    private void printNavBar(Out fout, int indent)
    {
        String buf;

        condFlushLine(fout, indent);
        printString("<center><small>");

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(3);

        if (slide > 1)
        {
            buf = "<a href=\"slide" + numberFormat.format(slide - 1) + ".html\">previous</a> | ";
            // #427666 - fix by Eric Rossen 02 Aug 00
            printString(buf);
            condFlushLine(fout, indent);

            if (slide < count)
            {
                printString("<a href=\"slide001.html\">start</a> | ");
                // #427666 - fix by Eric Rossen 02 Aug 00
            }
            else
            {
                printString("<a href=\"slide001.html\">start</a>");
                // #427666 - fix by Eric Rossen 02 Aug 00
            }

            condFlushLine(fout, indent);
        }

        if (slide < count)
        {
            buf = "<a href=\"slide" + numberFormat.format(slide + 1) + ".html\">next</a>";
            // #427666 - fix by Eric Rossen 02 Aug 00
            printString(buf);
        }

        printString("</small></center>");
        condFlushLine(fout, indent);
    }

    /**
     * Called from printTree to print the content of a slide from the node slidecontent. On return slidecontent points
     * to the node starting the next slide or null. The variables slide and count are used to customise the navigation
     * bar.
     */
    public void printSlide(Out fout, short mode, int indent, Lexer lexer)
    {
        Node content, last;
        TagTable tt = this.configuration.tt;

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(3);

        /* insert div for onclick handler */
        String s;
        s = "<div onclick=\"document.location='slide" + numberFormat.format(slide < count ? slide + 1 : 1)
            + ".html'\">";
        // #427666 - fix by Eric Rossen 02 Aug 00
        printString(s);
        condFlushLine(fout, indent);

        /* first print the h2 element and navbar */
        if (slidecontent != null && slidecontent.tag == tt.tagH2)
        {
            printNavBar(fout, indent);

            /* now print an hr after h2 */

            addC('<', linelen++);

            addC(Lexer.foldCase('h', this.configuration.upperCaseTags, this.configuration.xmlTags), linelen++);
            addC(Lexer.foldCase('r', this.configuration.upperCaseTags, this.configuration.xmlTags), linelen++);

            if (this.configuration.xmlOut)
            {
                printString(" />");
            }
            else
            {
                addC('>', linelen++);
            }

            if (this.configuration.indentContent)
            {
                condFlushLine(fout, indent);
            }

            // PrintVertSpacer(fout, indent);

            // condFlushLine(fout, indent);

            // print the h2 element
            printTree(fout, mode, (this.configuration.indentContent ? indent + this.configuration.spaces : indent),
                lexer, slidecontent);

            slidecontent = slidecontent.next;
        }

        // now continue until we reach the next h2

        last = null;
        content = slidecontent;

        for (; content != null; content = content.next)
        {
            if (content.tag == tt.tagH2)
            {
                break;
            }

            // kludge for naked text before block level tag
            if (last != null && !this.configuration.indentContent && last.type == Node.TextNode && content.tag != null
                && (content.tag.model & Dict.CM_BLOCK) != 0)
            {
                flushLine(fout, indent);
                flushLine(fout, indent);
            }

            printTree(fout, mode, (this.configuration.indentContent ? indent + this.configuration.spaces : indent),
                lexer, content);

            last = content;
        }

        slidecontent = content;

        // now print epilog

        condFlushLine(fout, indent);

        printString("<br clear=\"all\">");
        condFlushLine(fout, indent);

        addC('<', linelen++);

        addC(Lexer.foldCase('h', this.configuration.upperCaseTags, this.configuration.xmlTags), linelen++);
        addC(Lexer.foldCase('r', this.configuration.upperCaseTags, this.configuration.xmlTags), linelen++);

        if (this.configuration.xmlOut)
        {
            printString(" />");
        }
        else
        {
            addC('>', linelen++);
        }

        if (this.configuration.indentContent)
        {
            condFlushLine(fout, indent);
        }

        printNavBar(fout, indent);

        // end tag for div
        printString("</div>");
        condFlushLine(fout, indent);
    }

    /**
     * Add meta element for page transition effect, this works on IE but not NS.
     */
    public void addTransitionEffect(Lexer lexer, Node root, short effect, double duration)
    {
        Node head = root.findHEAD(lexer.configuration.tt);
        String transition;

        if (0 <= effect && effect <= 23)
        {
            transition = "revealTrans(Duration=" + (new Double(duration)).toString() + ",Transition=" + effect + ")";
        }
        else
        {
            transition = "blendTrans(Duration=" + (new Double(duration)).toString() + ")";
        }

        if (head != null)
        {
            Node meta = lexer.inferredTag("meta");
            meta.addAttribute("http-equiv", "Page-Enter");
            meta.addAttribute("content", transition);
            Node.insertNodeAtStart(head, meta);
        }
    }

    public void createSlides(Lexer lexer, Node root)
    {
        Node body;
        String buf;
        Out out = new OutImpl();

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(3);

        body = root.findBody(lexer.configuration.tt);
        count = countSlides(body);
        slidecontent = body.content;
        addTransitionEffect(lexer, root, EFFECT_BLEND, 3.0);

        for (slide = 1; slide <= count; ++slide)
        {
            buf = "slide" + numberFormat.format(slide) + ".html";
            // #427666 - fix by Eric Rossen 02 Aug 00
            out.state = StreamIn.FSM_ASCII;
            out.encoding = this.configuration.charEncoding;

            try
            {
                out.out = new FileOutputStream(buf);
                printTree(out, (short) 0, 0, lexer, root);
                flushLine(out, 0);
                out.out.close();
            }
            catch (IOException e)
            {
                System.err.println(buf + e.toString());
            }
        }

        // delete superfluous slides by deleting slideN.html for N = count+1, count+2, etc.
        // until no such file is found.

        // #427666 - fix by Eric Rossen 02 Aug 00
        while ((new File("slide" + numberFormat.format(slide) + ".html")).delete())
        {
            ++slide;
        }
    }

}