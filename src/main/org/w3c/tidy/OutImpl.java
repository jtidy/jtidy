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
 * Output Stream Implementation.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
import java.io.IOException;


public class OutImpl extends Out
{

    private static final byte[] nlBytes = (System.getProperty("line.separator")).getBytes();

    public OutImpl()
    {
        this.out = null;
    }

    public void outc(byte c)
    {
        outc(c & 0xFF); // Convert to unsigned.
    }

    /* For mac users, should we map Unicode back to MacRoman? */
    public void outc(int c)
    {
        int ch;

        try
        {
            if (this.encoding == Configuration.UTF8)
            {
                if (c < 128)
                {
                    this.out.write(c);
                }
                else if (c <= 0x7FF)
                {
                    ch = (0xC0 | (c >> 6));
                    this.out.write(ch);
                    ch = (0x80 | (c & 0x3F));
                    this.out.write(ch);
                }
                else if (c <= 0xFFFF)
                {
                    ch = (0xE0 | (c >> 12));
                    this.out.write(ch);
                    ch = (0x80 | ((c >> 6) & 0x3F));
                    this.out.write(ch);
                    ch = (0x80 | (c & 0x3F));
                    this.out.write(ch);
                }
                else if (c <= 0x1FFFFF)
                {
                    ch = (0xF0 | (c >> 18));
                    this.out.write(ch);
                    ch = (0x80 | ((c >> 12) & 0x3F));
                    this.out.write(ch);
                    ch = (0x80 | ((c >> 6) & 0x3F));
                    this.out.write(ch);
                    ch = (0x80 | (c & 0x3F));
                    this.out.write(ch);
                }
                else
                {
                    ch = (0xF8 | (c >> 24));
                    this.out.write(ch);
                    ch = (0x80 | ((c >> 18) & 0x3F));
                    this.out.write(ch);
                    ch = (0x80 | ((c >> 12) & 0x3F));
                    this.out.write(ch);
                    ch = (0x80 | ((c >> 6) & 0x3F));
                    this.out.write(ch);
                    ch = (0x80 | (c & 0x3F));
                    this.out.write(ch);
                }
            }
            else if (this.encoding == Configuration.ISO2022)
            {
                if (c == 0x1b) /* ESC */
                {
                    this.state = StreamIn.FSM_ESC;
                }
                else
                {
                    switch (this.state)
                    {
                        case StreamIn.FSM_ESC :
                            if (c == '$')
                            {
                                this.state = StreamIn.FSM_ESCD;
                            }
                            else if (c == '(')
                            {
                                this.state = StreamIn.FSM_ESCP;
                            }
                            else
                            {
                                this.state = StreamIn.FSM_ASCII;
                            }
                            break;

                        case StreamIn.FSM_ESCD :
                            if (c == '(')
                            {
                                this.state = StreamIn.FSM_ESCDP;
                            }
                            else
                            {
                                this.state = StreamIn.FSM_NONASCII;
                            }
                            break;

                        case StreamIn.FSM_ESCDP :
                            this.state = StreamIn.FSM_NONASCII;
                            break;

                        case StreamIn.FSM_ESCP :
                            this.state = StreamIn.FSM_ASCII;
                            break;

                        case StreamIn.FSM_NONASCII :
                            c &= 0x7F;
                            break;
                    }
                }

                this.out.write(c);
            }
            else
            {
                this.out.write(c);
            }
        }
        catch (IOException e)
        {
            System.err.println("OutImpl.outc: " + e.toString());
        }
    }

    public void newline()
    {
        try
        {
            this.out.write(nlBytes);
            this.out.flush();
        }
        catch (IOException e)
        {
            System.err.println("OutImpl.newline: " + e.toString());
        }
    }

}