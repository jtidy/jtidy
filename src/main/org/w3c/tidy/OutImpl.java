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

import java.io.IOException;
import java.io.OutputStream;


/**
 * Output Stream Implementation.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class OutImpl implements Out
{

    /**
     * bytes for the newline marker.
     */
    private static final byte[] NL_BYTES = (System.getProperty("line.separator")).getBytes();

    /**
     * output encoding.
     */
    private int encoding;

    /**
     * actual state for ISO 2022.
     */
    private int state;

    /**
     * output stream.
     */
    private OutputStream out;

    /**
     * Getter for <code>encoding</code>.
     * @return Returns the encoding.
     */
    public int getEncoding()
    {
        return this.encoding;
    }

    /**
     * Getter for <code>out</code>.
     * @return Returns the out.
     */
    public OutputStream getOut()
    {
        return this.out;
    }

    /**
     * Getter for <code>state</code>.
     * @return Returns the state.
     */
    public int getState()
    {
        return this.state;
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
            // #431953 - start RJ
            else if (this.encoding == Configuration.BIG5 || this.encoding == Configuration.SHIFTJIS)
            {
                if (c < 128)
                {
                    this.out.write(c);
                }
                else
                {
                    ch = (c >> 8) & 0xFF;
                    this.out.write(c);
                    ch = c & 0xFF;
                    this.out.write(c);
                }
            }
            // #431953 - end RJ
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
            this.out.write(NL_BYTES);
            this.out.flush();
        }
        catch (IOException e)
        {
            System.err.println("OutImpl.newline: " + e.toString());
        }
    }

    /**
     * Setter for <code>encoding</code>.
     * @param encoding The encoding to set.
     */
    public void setEncoding(int encoding)
    {
        this.encoding = encoding;
    }

    /**
     * Setter for <code>out</code>.
     * @param out The out to set.
     */
    public void setOut(OutputStream out)
    {
        this.out = out;
    }

    /**
     * Setter for <code>state</code>.
     * @param state The state to set.
     */
    public void setState(int state)
    {
        this.state = state;
    }
}