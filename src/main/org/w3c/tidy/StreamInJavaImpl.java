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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class StreamInJavaImpl implements StreamIn
{

    /**
     * Java input stream reader.
     */
    private InputStreamReader isr;

    /**
     * has end of stream been reached?
     */
    private boolean endOfStream;

    private boolean pushed;

    private int c;

    public StreamInJavaImpl(InputStream stream, String encoding) throws UnsupportedEncodingException
    {
        isr = new InputStreamReader(stream, encoding);
    }

    /**
     * @see org.w3c.tidy.StreamIn#readCharFromStream()
     */
    public int readCharFromStream()
    {
        try
        {
            int rtn = isr.read();
            if (rtn < 0)
            {
                endOfStream = true;
            }
            return rtn;
        }
        catch (IOException e)
        {
            // @todo how to handle?
            endOfStream = true;
            return -1;
        }
    }

    /**
     * @see org.w3c.tidy.StreamIn#readChar()
     */
    public int readChar()
    {
        if (pushed)
        {
            pushed = false;
            return c;
        }
        else
        {
            return readCharFromStream();
        }
    }

    /**
     * @see org.w3c.tidy.StreamIn#ungetChar(int)
     */
    public void ungetChar(int c)
    {
        pushed = true;
        this.c = c;
    }

    /**
     * @see org.w3c.tidy.StreamIn#isEndOfStream()
     */
    public boolean isEndOfStream()
    {
        return endOfStream;
    }

    /**
     * Getter for <code>curcol</code>.
     * @return Returns the curcol.
     */
    public int getCurcol()
    {
        // @todo
        return 0;
    }

    /**
     * Getter for <code>curline</code>.
     * @return Returns the curline.
     */
    public int getCurline()
    {
        // @todo
        return 0;
    }

    /**
     * Getter for <code>encoding</code>.
     * @return Returns the encoding.
     */
    public int getEncoding()
    {
        // @todo
        return 0;
    }

}