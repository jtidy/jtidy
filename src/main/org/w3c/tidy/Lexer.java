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

import java.io.PrintWriter;
import java.util.List;
import java.util.Stack;
import java.util.Vector;


/**
 * Lexer for html parser.
 * <p>
 * Given a file stream fp it returns a sequence of tokens. GetToken(fp) gets the next token UngetToken(fp) provides one
 * level undo The tags include an attribute list: - linked list of attribute/value nodes - each node has 2
 * null-terminated strings. - entities are replaced in attribute values white space is compacted if not in preformatted
 * mode If not in preformatted mode then leading white space is discarded and subsequent white space sequences compacted
 * to single space chars. If XmlTags is no then Tag names are folded to upper case and attribute names to lower case.
 * Not yet done: - Doctype subset and marked sections
 * </p>
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class Lexer
{

    /**
     * state: ignore whitespace.
     */
    public static final short IGNORE_WHITESPACE = 0;

    /**
     * state: mixed content.
     */
    public static final short MIXED_CONTENT = 1;

    /**
     * state: preformatted.
     */
    public static final short PREFORMATTED = 2;

    /**
     * state: ignore markup.
     */
    public static final short IGNORE_MARKUP = 3;

    /**
     * URI for XHTML 1.0 transitional DTD.
     */
    private static final String VOYAGER_LOOSE = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";

    /**
     * URI for XHTML 1.0 strict DTD.
     */
    private static final String VOYAGER_STRICT = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";

    /**
     * URI for XHTML 1.0 frameset DTD.
     */
    private static final String VOYAGER_FRAMESET = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd";

    /**
     * URI for XHTML 1.1.
     */
    private static final String VOYAGER_11 = "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";

    /**
     * URI for XHTML Basic 1.0.
     */
    private static final String VOYAGER_BASIC = "http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd";

    /**
     * xhtml namespace.
     */
    private static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    /**
     * lists all the known versions.
     */
    private static final Lexer.W3CVersionInfo[] W3CVERSION = {
        new W3CVersionInfo("HTML 4.01", "XHTML 1.0 Strict", VOYAGER_STRICT, Dict.VERS_HTML40_STRICT),
        new W3CVersionInfo("HTML 4.01 Transitional", "XHTML 1.0 Transitional", VOYAGER_LOOSE, Dict.VERS_HTML40_LOOSE),
        new W3CVersionInfo("HTML 4.01 Frameset", "XHTML 1.0 Frameset", VOYAGER_FRAMESET, Dict.VERS_FRAMESET),
        new W3CVersionInfo("HTML 4.0", "XHTML 1.0 Strict", VOYAGER_STRICT, Dict.VERS_HTML40_STRICT),
        new W3CVersionInfo("HTML 4.0 Transitional", "XHTML 1.0 Transitional", VOYAGER_LOOSE, Dict.VERS_HTML40_LOOSE),
        new W3CVersionInfo("HTML 4.0 Frameset", "XHTML 1.0 Frameset", VOYAGER_FRAMESET, Dict.VERS_FRAMESET),
        new W3CVersionInfo("HTML 3.2", "XHTML 1.0 Transitional", VOYAGER_LOOSE, Dict.VERS_HTML32),
        new W3CVersionInfo("HTML 3.2 Final", "XHTML 1.0 Transitional", VOYAGER_LOOSE, Dict.VERS_HTML32),
        new W3CVersionInfo("HTML 3.2 Draft", "XHTML 1.0 Transitional", VOYAGER_LOOSE, Dict.VERS_HTML32),
        new W3CVersionInfo("HTML 2.0", "XHTML 1.0 Strict", VOYAGER_STRICT, Dict.VERS_HTML20)};

    /**
     * char type: digit.
     */
    private static final short DIGIT = 1;

    /**
     * char type: letter.
     */
    private static final short LETTER = 2;

    /**
     * char type: namechar.
     */
    private static final short NAMECHAR = 4;

    /**
     * char type: whitespace.
     */
    private static final short WHITE = 8;

    /**
     * char type: newline.
     */
    private static final short NEWLINE = 16;

    /**
     * char type: lowercase.
     */
    private static final short LOWERCASE = 32;

    /**
     * char type: uppercase.
     */
    private static final short UPPERCASE = 64;

    /**
     * getToken state: content.
     */
    private static final short LEX_CONTENT = 0;

    /**
     * getToken state: gt.
     */
    private static final short LEX_GT = 1;

    /**
     * getToken state: endtag.
     */
    private static final short LEX_ENDTAG = 2;

    /**
     * getToken state: start tag.
     */
    private static final short LEX_STARTTAG = 3;

    /**
     * getToken state: comment.
     */
    private static final short LEX_COMMENT = 4;

    /**
     * getToken state: doctype.
     */
    private static final short LEX_DOCTYPE = 5;

    /**
     * getToken state: procinstr.
     */
    private static final short LEX_PROCINSTR = 6;

    /**
     * getToken state: cdata.
     */
    private static final short LEX_CDATA = 8;

    /**
     * getToken state: section.
     */
    private static final short LEX_SECTION = 9;

    /**
     * getToken state: asp.
     */
    private static final short LEX_ASP = 10;

    /**
     * getToken state: jste.
     */
    private static final short LEX_JSTE = 11;

    /**
     * getToken state: php.
     */
    private static final short LEX_PHP = 12;

    /**
     * getToken state: xml declaration.
     */
    private static final short LEX_XMLDECL = 13;

    /**
     * used to classify chars for lexical purposes.
     */
    private static short[] lexmap = new short[128];

    /**
     * file stream.
     */
    protected StreamIn in;

    /**
     * error output stream.
     */
    protected PrintWriter errout;

    /**
     * for accessibility errors.
     */
    protected short badAccess;

    /**
     * for bad style errors.
     */
    protected short badLayout;

    /**
     * for bad char encodings.
     */
    protected short badChars;

    /**
     * for mismatched/mispositioned form tags.
     */
    protected short badForm;

    /**
     * count of warnings in this document.
     */
    protected short warnings;

    /**
     * count of errors.
     */
    protected short errors;

    /**
     * lines seen.
     */
    protected int lines;

    /**
     * at start of current token.
     */
    protected int columns;

    /**
     * used to collapse contiguous white space.
     */
    protected boolean waswhite;

    /**
     * true after token has been pushed back.
     */
    protected boolean pushed;

    /**
     * when space is moved after end tag.
     */
    protected boolean insertspace;

    /**
     * Netscape compatibility.
     */
    protected boolean excludeBlocks;

    /**
     * true if moved out of table.
     */
    protected boolean exiled;

    /**
     * true if xmlns attribute on html element.
     */
    protected boolean isvoyager;

    /**
     * bit vector of HTML versions.
     */
    protected short versions;

    /**
     * version as given by doctype (if any).
     */
    protected int doctype;

    /**
     * set if html or PUBLIC is missing.
     */
    protected boolean badDoctype;

    /**
     * start of current node.
     */
    protected int txtstart;

    /**
     * end of current node.
     */
    protected int txtend;

    /**
     * state of lexer's finite state machine.
     */
    protected short state;

    /**
     * current node.
     */
    protected Node token;

    /**
     * Lexer character buffer parse tree nodes span onto this buffer which contains the concatenated text contents of
     * all of the elements. Lexsize must be reset for each file. Byte buffer of UTF-8 chars.
     */
    protected byte[] lexbuf;

    /**
     * allocated.
     */
    protected int lexlength;

    /**
     * used.
     */
    protected int lexsize;

    /**
     * Inline stack for compatibility with Mosaic. For deferring text node.
     */
    protected Node inode;

    /**
     * for inferring inline tags.
     */
    protected int insert;

    /**
     * stack.
     */
    protected Stack istack;

    /**
     * start of frame.
     */
    protected int istackbase;

    /**
     * used for cleaning up presentation markup.
     */
    protected Style styles;

    /**
     * configuration.
     */
    protected Configuration configuration;

    /**
     * already seen end body tag?
     */
    protected boolean seenEndBody;

    /**
     * already seen end html tag?
     */
    protected boolean seenEndHtml;

    /**
     * report.
     */
    protected Report report;

    /**
     * node list.
     */
    private List nodeList;

    public Lexer(StreamIn in, Configuration configuration, Report report)
    {
        this.report = report;
        this.in = in;
        this.lines = 1;
        this.columns = 1;
        this.state = LEX_CONTENT;
        this.versions = (Dict.VERS_ALL | Dict.VERS_PROPRIETARY);
        this.doctype = Dict.VERS_UNKNOWN;
        this.insert = -1;
        this.istack = new Stack();
        this.configuration = configuration;
        this.nodeList = new Vector();
    }

    public Node newNode()
    {
        Node node = new Node();
        this.nodeList.add(node);
        return node;
    }

    public Node newNode(short type, byte[] textarray, int start, int end)
    {
        Node node = new Node(type, textarray, start, end);
        this.nodeList.add(node);
        return node;
    }

    public Node newNode(short type, byte[] textarray, int start, int end, String element)
    {
        Node node = new Node(type, textarray, start, end, element, this.configuration.tt);
        this.nodeList.add(node);
        return node;
    }

    public Node cloneNode(Node node)
    {
        Node cnode = (Node) node.clone();
        this.nodeList.add(cnode);
        for (AttVal att = cnode.attributes; att != null; att = att.next)
        {
            if (att.asp != null)
            {
                this.nodeList.add(att.asp);
            }
            if (att.php != null)
            {
                this.nodeList.add(att.php);
            }
        }
        return cnode;
    }

    public AttVal cloneAttributes(AttVal attrs)
    {
        AttVal cattrs = (AttVal) attrs.clone();
        for (AttVal att = cattrs; att != null; att = att.next)
        {
            if (att.asp != null)
            {
                this.nodeList.add(att.asp);
            }
            if (att.php != null)
            {
                this.nodeList.add(att.php);
            }
        }
        return cattrs;
    }

    protected void updateNodeTextArrays(byte[] oldtextarray, byte[] newtextarray)
    {
        Node node;
        for (int i = 0; i < this.nodeList.size(); i++)
        {
            node = (Node) (this.nodeList.get(i));
            if (node.textarray == oldtextarray)
            {
                node.textarray = newtextarray;
            }
        }
    }

    /**
     * Used for creating preformatted text from Word2000.
     */
    public Node newLineNode()
    {
        Node node = newNode();

        node.textarray = this.lexbuf;
        node.start = this.lexsize;
        addCharToLexer('\n');
        node.end = this.lexsize;
        return node;
    }

    /**
     * Should always be able convert to/from UTF-8, so encoding exceptions are converted to an Error to avoid adding
     * throws declarations in lots of methods.
     */
    public static byte[] getBytes(String str)
    {
        try
        {
            return str.getBytes("UTF8");
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            throw new Error("string to UTF-8 conversion failed: " + e.getMessage());
        }
    }

    public static String getString(byte[] bytes, int offset, int length)
    {
        try
        {
            return new String(bytes, offset, length, "UTF8");
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            throw new Error("UTF-8 to string conversion failed: " + e.getMessage());
        }
    }

    public boolean endOfInput()
    {
        return this.in.isEndOfStream();
    }

    public void addByte(int c)
    {
        if (this.lexsize + 1 >= this.lexlength)
        {
            while (this.lexsize + 1 >= this.lexlength)
            {
                if (this.lexlength == 0)
                {
                    this.lexlength = 8192;
                }
                else
                {
                    this.lexlength = this.lexlength * 2;
                }
            }

            byte[] temp = this.lexbuf;
            this.lexbuf = new byte[this.lexlength];
            if (temp != null)
            {
                System.arraycopy(temp, 0, this.lexbuf, 0, temp.length);
                updateNodeTextArrays(temp, this.lexbuf);
            }
        }

        this.lexbuf[this.lexsize++] = (byte) c;
        this.lexbuf[this.lexsize] = (byte) '\0'; // debug
    }

    public void changeChar(byte c)
    {
        if (this.lexsize > 0)
        {
            this.lexbuf[this.lexsize - 1] = c;
        }
    }

    /**
     * store char c as UTF-8 encoded byte stream.
     */
    public void addCharToLexer(int c)
    {
        // Allow only valid XML characters. See: http://www.w3.org/TR/2004/REC-xml-20040204/#NT-Char
        // Fix by Pablo Mayrgundter 17-08-2004
        if ((this.configuration.xmlOut || this.configuration.xHTML) // only for xml output
            && !((c >= 0x20 && c <= 0xD7FF) // Check the common-case first.
                || c == 0x9 || c == 0xA || c == 0xD // Then white-space.
                || (c >= 0xE000 && c <= 0xFFFD) // Then high-range unicode.
            || (c >= 0x10000 && c <= 0x10FFFF)))
        {
            return;
        }

        if (c < 128)
        {
            addByte(c);
        }
        else if (c <= 0x7FF)
        {
            addByte(0xC0 | (c >> 6));
            addByte(0x80 | (c & 0x3F));
        }
        else if (c <= 0xFFFF)
        {
            addByte(0xE0 | (c >> 12));
            addByte(0x80 | ((c >> 6) & 0x3F));
            addByte(0x80 | (c & 0x3F));
        }
        else if (c <= 0x1FFFFF)
        {
            addByte(0xF0 | (c >> 18));
            addByte(0x80 | ((c >> 12) & 0x3F));
            addByte(0x80 | ((c >> 6) & 0x3F));
            addByte(0x80 | (c & 0x3F));
        }
        else
        {
            addByte(0xF8 | (c >> 24));
            addByte(0x80 | ((c >> 18) & 0x3F));
            addByte(0x80 | ((c >> 12) & 0x3F));
            addByte(0x80 | ((c >> 6) & 0x3F));
            addByte(0x80 | (c & 0x3F));
        }
    }

    public void addStringToLexer(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            addCharToLexer(str.charAt(i));
        }
    }

    /**
     * Parse an html entoty
     */
    public void parseEntity(short mode)
    {
        // No longer attempts to insert missing ';' for unknown enitities unless one was present already, since this
        // gives unexpected results. For example: &lt;a href="something.htm?foo&bar&fred"> was tidied to: &lt;a
        // href="something.htm?foo&amp;bar;&amp;fred;"> rather than: &lt;a href="something.htm?foo&amp;bar&amp;fred"> My
        // thanks for Maurice Buxton for spotting this.

        int start;
        boolean first = true;
        boolean semicolon = false;
        boolean numeric = false;
        int c, ch, startcol;
        String str;

        start = this.lexsize - 1; // to start at "&"
        startcol = this.in.curcol - 1;

        while ((c = this.in.readChar()) != StreamIn.EndOfStream)
        {
            if (c == ';')
            {
                semicolon = true;
                break;
            }

            if (first && c == '#')
            {
                addCharToLexer(c);
                first = false;
                numeric = true;
                continue;
            }

            first = false;

            if (isNamechar((char) c))
            {
                addCharToLexer(c);
                continue;
            }

            // otherwise put it back
            this.in.ungetChar(c);
            break;
        }

        str = getString(this.lexbuf, start, this.lexsize - start);

        if ("&apos".equals(str) && !configuration.xmlOut && !this.isvoyager && !configuration.xHTML)
        {
            report.entityError(this, Report.APOS_UNDEFINED, str, 39);
        }

        ch = EntityTable.getDefaultEntityTable().entityCode(str);

        // drops invalid numeric entities from XML mode. Fix by Pablo Mayrgundter 17-08-2004
        if ((this.configuration.xmlOut || this.configuration.xHTML) // only for xml output
            && !((ch >= 0x20 && ch <= 0xD7FF) // Check the common-case first.
                || ch == 0x9 || ch == 0xA || ch == 0xD // Then white-space.
            || (ch >= 0xE000 && ch <= 0xFFFD)))
        {
            this.lexsize = start;
            return;
        }

        // deal with unrecognized entities
        // #433012 - fix by Randy Waki 17 Feb 01
        // report invalid NCR's - Terry Teague 01 Sep 01
        if (ch <= 0 || (ch >= 256 && c != ';'))
        {
            // set error position just before offending character
            this.lines = this.in.curline;
            this.columns = startcol;

            if (this.lexsize > start + 1)
            {
                if (ch >= 128 && ch <= 159)
                {
                    // invalid numeric character reference
                    int c1 = 0;

                    if (configuration.replacementCharEncoding == Configuration.WIN1252)
                    {
                        c1 = StreamInImpl.decodeWin1252(ch);
                    }
                    else if (configuration.replacementCharEncoding == Configuration.MACROMAN)
                    {
                        c1 = StreamInImpl.decodeMacRoman(ch);
                    }

                    // "or" DISCARDED_CHAR with the other errors if discarding char; otherwise default is replacing
                    final short REPLACED_CHAR = 0;
                    final short DISCARDED_CHAR = 1;

                    int replaceMode = c1 != 0 ? REPLACED_CHAR : DISCARDED_CHAR;

                    if (c != ';') /* issue warning if not terminated by ';' */
                    {
                        report.entityError(this, Report.MISSING_SEMICOLON_NCR, str, c);
                    }

                    report.encodingError(this, (short) (Report.INVALID_NCR | replaceMode), ch);

                    if (c1 != 0)
                    {
                        // make the replacement
                        this.lexsize = start;
                        addCharToLexer(c1);
                        semicolon = false;
                    }
                    else
                    {
                        /* discard */
                        this.lexsize = start;
                        semicolon = false;
                    }

                }
                else
                {
                    report.entityError(this, Report.UNKNOWN_ENTITY, str, ch);
                }

                if (semicolon)
                {
                    addCharToLexer(';');
                }
            }
            else
            {
                // naked &
                report.entityError(this, Report.UNESCAPED_AMPERSAND, str, ch);
            }
        }
        else
        {
            // issue warning if not terminated by ';'
            if (c != ';')
            {
                // set error position just before offending character
                this.lines = this.in.curline;
                this.columns = startcol;
                report.entityError(this, Report.MISSING_SEMICOLON, str, c);
            }

            this.lexsize = start;

            if (ch == 160 && (mode & PREFORMATTED) != 0)
            {
                ch = ' ';
            }

            addCharToLexer(ch);

            if (ch == '&' && !this.configuration.quoteAmpersand)
            {
                addCharToLexer('a');
                addCharToLexer('m');
                addCharToLexer('p');
                addCharToLexer(';');
            }
        }
    }

    public char parseTagName()
    {
        int c;

        // fold case of first char in buffer

        c = this.lexbuf[this.txtstart];

        if (!this.configuration.xmlTags && isUpper((char) c))
        {
            c = toLower((char) c);
            this.lexbuf[this.txtstart] = (byte) c;
        }

        while (true)
        {
            c = this.in.readChar();
            if (c == StreamIn.EndOfStream)
            {
                break;
            }

            if (!isNamechar((char) c))
            {
                break;
            }

            // fold case of subsequent chars
            if (!this.configuration.xmlTags && isUpper((char) c))
            {
                c = toLower((char) c);
            }

            addCharToLexer(c);
        }

        this.txtend = this.lexsize;
        return (char) c;
    }

    public void addStringLiteral(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            addCharToLexer(str.charAt(i));
        }
    }

    /**
     * Choose what version to use for new doctype.
     * @return
     */
    public short htmlVersion()
    {
        if ((versions & Dict.VERS_HTML20) != 0)
        {
            return Dict.VERS_HTML20;
        }

        if (!(this.configuration.xmlOut | this.configuration.xmlTags | this.isvoyager)
            && (versions & Dict.VERS_HTML32) != 0)
        {
            return Dict.VERS_HTML32;
        }
        if ((versions & Dict.VERS_XHTML11) != 0)
        {
            return Dict.VERS_XHTML11;
        }
        if ((versions & Dict.VERS_HTML40_STRICT) != 0)
        {
            return Dict.VERS_HTML40_STRICT;
        }

        if ((versions & Dict.VERS_HTML40_LOOSE) != 0)
        {
            return Dict.VERS_HTML40_LOOSE;
        }

        if ((versions & Dict.VERS_FRAMESET) != 0)
        {
            return Dict.VERS_FRAMESET;
        }

        return Dict.VERS_UNKNOWN;
    }

    public String htmlVersionName()
    {
        short guessed;
        int j;

        guessed = apparentVersion();

        for (j = 0; j < W3CVERSION.length; ++j)
        {
            if (guessed == W3CVERSION[j].code)
            {
                if (this.isvoyager)
                {
                    return W3CVERSION[j].voyagerName;
                }

                return W3CVERSION[j].name;
            }
        }

        return null;
    }

    /**
     * add meta element for Tidy.
     */
    public boolean addGenerator(Node root)
    {
        AttVal attval;
        Node node;
        Node head = root.findHEAD(this.configuration.tt);

        if (head != null)
        {
            for (node = head.content; node != null; node = node.next)
            {
                if (node.tag == this.configuration.tt.tagMeta)
                {
                    attval = node.getAttrByName("name");

                    if (attval != null && attval.value != null && "generator".equalsIgnoreCase(attval.value))
                    {
                        attval = node.getAttrByName("content");

                        if (attval != null
                            && attval.value != null
                            && attval.value.length() >= 9
                            && "HTML Tidy".equalsIgnoreCase(attval.value.substring(0, 9)))
                        {
                            return false;
                        }
                    }
                }
            }

            node = this.inferredTag("meta");
            node.addAttribute("content", "HTML Tidy, see www.w3.org");
            node.addAttribute("name", "generator");
            Node.insertNodeAtStart(head, node);
            return true;
        }

        return false;
    }

    /**
     * return true if substring s is in p and isn't all in upper case this is used to check the case of SYSTEM, PUBLIC,
     * DTD and EN len is how many chars to check in p.
     */
    private static boolean findBadSubString(String s, String p, int len)
    {
        int n = s.length();
        int i = 0;
        String ps;

        while (n < len)
        {
            ps = p.substring(i, i + n);
            if (s.equalsIgnoreCase(ps))
            {
                return (!ps.equals(s.substring(0, n)));
            }

            ++i;
            --len;
        }

        return false;
    }

    public boolean checkDocTypeKeyWords(Node doctype)
    {
        int len = doctype.end - doctype.start;
        String s = getString(this.lexbuf, doctype.start, len);

        return !(findBadSubString("SYSTEM", s, len)
            || findBadSubString("PUBLIC", s, len)
            || findBadSubString("//DTD", s, len)
            || findBadSubString("//W3C", s, len) || findBadSubString("//EN", s, len));
    }

    /**
     * examine DOCTYPE to identify version.
     */
    public short findGivenVersion(Node doctype)
    {
        String p, s;
        int i, j;
        int len;
        String str1;
        String str2;

        // if root tag for doctype isn't html give up now
        str1 = getString(this.lexbuf, doctype.start, 5);
        if (!"html ".equalsIgnoreCase(str1))
        {
            return 0;
        }

        if (!checkDocTypeKeyWords(doctype))
        {
            report.warning(this, doctype, null, Report.DTYPE_NOT_UPPER_CASE);
        }

        // give up if all we are given is the system id for the doctype
        str1 = getString(this.lexbuf, doctype.start + 5, 7);
        if ("SYSTEM ".equalsIgnoreCase(str1))
        {
            // but at least ensure the case is correct
            if (!str1.substring(0, 6).equals("SYSTEM"))
            {
                System.arraycopy(getBytes("SYSTEM"), 0, this.lexbuf, doctype.start + 5, 6);
            }
            return 0; // unrecognized
        }

        if ("PUBLIC ".equalsIgnoreCase(str1))
        {
            if (!str1.substring(0, 6).equals("PUBLIC"))
            {
                System.arraycopy(getBytes("PUBLIC "), 0, this.lexbuf, doctype.start + 5, 6);
            }
        }
        else
        {
            this.badDoctype = true;
        }

        for (i = doctype.start; i < doctype.end; ++i)
        {
            if (this.lexbuf[i] == (byte) '"')
            {
                str1 = getString(this.lexbuf, i + 1, 12);
                str2 = getString(this.lexbuf, i + 1, 13);
                if (str1.equals("-//W3C//DTD "))
                {
                    // compute length of identifier e.g. "HTML 4.0 Transitional"
                    for (j = i + 13; j < doctype.end && this.lexbuf[j] != (byte) '/'; ++j)
                    {
                        //
                    }
                    len = j - i - 13;
                    p = getString(this.lexbuf, i + 13, len);

                    for (j = 1; j < W3CVERSION.length; ++j)
                    {
                        s = W3CVERSION[j].name;
                        if (len == s.length() && s.equals(p))
                        {
                            return W3CVERSION[j].code;
                        }
                    }

                    // else unrecognized version
                }
                else if (str2.equals("-//IETF//DTD "))
                {
                    // compute length of identifier e.g. "HTML 2.0"
                    for (j = i + 14; j < doctype.end && this.lexbuf[j] != (byte) '/'; ++j)
                    {
                        //
                    }
                    len = j - i - 14;

                    p = getString(this.lexbuf, i + 14, len);
                    s = W3CVERSION[0].name;
                    if (len == s.length() && s.equals(p))
                    {
                        return W3CVERSION[0].code;
                    }

                    // else unrecognized version
                }
                break;
            }
        }

        return 0;
    }

    public void fixHTMLNameSpace(Node root, String profile)
    {
        Node node;
        AttVal attr;

        for (node = root.content; node != null && node.tag != this.configuration.tt.tagHtml; node = node.next)
        {
            //
        }

        if (node != null)
        {

            for (attr = node.attributes; attr != null; attr = attr.next)
            {
                if (attr.attribute.equals("xmlns"))
                {
                    break;
                }

            }

            if (attr != null)
            {
                if (!attr.value.equals(profile))
                {
                    report.warning(this, node, null, Report.INCONSISTENT_NAMESPACE);
                    attr.value = profile;
                }
            }
            else
            {
                attr = new AttVal(node.attributes, null, '"', "xmlns", profile);
                attr.dict = AttributeTable.getDefaultAttributeTable().findAttribute(attr);
                node.attributes = attr;
            }
        }
    }

    /**
     * Put DOCTYPE declaration between the &lt:?xml version "1.0" ... ?&gt; declaration, if any, and the
     * <code>html</code> tag. Should also work for any comments, etc. that may precede the <code>html</code> tag.
     */
    Node newXhtmlDocTypeNode(Node root)
    {
        Node html = root.findHTML(this.configuration.tt);
        if (html == null)
        {
            return null;
        }

        Node newdoctype = newNode();
        newdoctype.setType(Node.DOCTYPE_TAG);
        newdoctype.next = html;
        newdoctype.parent = root;
        newdoctype.prev = null;

        if (html == root.content)
        {
            // No <?xml ... ?> declaration.
            root.content.prev = newdoctype;
            root.content = newdoctype;
            newdoctype.prev = null;
        }
        else
        {
            // we have an <?xml ... ?> declaration.
            newdoctype.prev = html.prev;
            newdoctype.prev.next = newdoctype;
        }
        html.prev = newdoctype;
        return newdoctype;
    }

    public boolean setXHTMLDocType(Node root)
    {
        String fpi = " ";
        String sysid = "";
        String namespace = XHTML_NAMESPACE;
        Node doctype;

        doctype = root.findDocType();

        fixHTMLNameSpace(root, namespace); // #427839 - fix by Evan Lenz 05 Sep 00

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_OMIT)
        {
            if (doctype != null)
            {
                Node.discardElement(doctype);
            }
            return true;
        }

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_AUTO)
        {
            // see what flavor of XHTML this document matches
            if ((this.versions & Dict.VERS_HTML40_STRICT) != 0)
            {
                // use XHTML strict
                fpi = "-//W3C//DTD XHTML 1.0 Strict//EN";
                sysid = VOYAGER_STRICT;
            }
            else if ((this.versions & Dict.VERS_LOOSE) != 0)
            {
                fpi = "-//W3C//DTD XHTML 1.0 Transitional//EN";
                sysid = VOYAGER_LOOSE;
            }
            else if ((this.versions & Dict.VERS_FRAMESET) != 0)
            {
                // use XHTML frames
                fpi = "-//W3C//DTD XHTML 1.0 Frameset//EN";
                sysid = VOYAGER_FRAMESET;
            }
            else
            {
                // proprietary
                fpi = null;
                sysid = "";
                if (doctype != null)// #473490 - fix by Bjšrn Hšhrmann 10 Oct 01
                {
                    Node.discardElement(doctype);
                }
            }
        }
        else if (this.configuration.docTypeMode == Configuration.DOCTYPE_STRICT)
        {
            fpi = "-//W3C//DTD XHTML 1.0 Strict//EN";
            sysid = VOYAGER_STRICT;
        }
        else if (this.configuration.docTypeMode == Configuration.DOCTYPE_LOOSE)
        {
            fpi = "-//W3C//DTD XHTML 1.0 Transitional//EN";
            sysid = VOYAGER_LOOSE;
        }

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_USER && this.configuration.docTypeStr != null)
        {
            fpi = this.configuration.docTypeStr;
            sysid = "";
        }

        if (fpi == null)
        {
            return false;
        }

        if (doctype == null)
        {
            if ((doctype = newXhtmlDocTypeNode(root)) == null)
            {
                return false;
            }
        }

        this.txtstart = this.lexsize;
        this.txtend = this.lexsize;

        // add public identifier
        addStringLiteral("html PUBLIC ");

        // check if the fpi is quoted or not
        if (fpi.charAt(0) == '"')
        {
            addStringLiteral(fpi);
        }
        else
        {
            addStringLiteral("\"");
            addStringLiteral(fpi);
            addStringLiteral("\"");
        }

        if (sysid.length() + 6 >= this.configuration.wraplen)
        {
            addStringLiteral("\n\"");
        }
        else
        {
            // FG: don't wrap
            addStringLiteral(" \"");
        }

        // add system identifier
        addStringLiteral(sysid);
        addStringLiteral("\"");

        this.txtend = this.lexsize;

        doctype.start = this.txtstart;
        doctype.end = this.txtend;

        return false;
    }

    public short apparentVersion()
    {
        switch (this.doctype)
        {
            case Dict.VERS_UNKNOWN :
                return htmlVersion();

            case Dict.VERS_HTML20 :
                if ((this.versions & Dict.VERS_HTML20) != 0)
                {
                    return Dict.VERS_HTML20;
                }

                break;

            case Dict.VERS_HTML32 :
                if ((this.versions & Dict.VERS_HTML32) != 0)
                {
                    return Dict.VERS_HTML32;
                }

                break; // to replace old version by new

            case Dict.VERS_HTML40_STRICT :
                if ((this.versions & Dict.VERS_HTML40_STRICT) != 0)
                {
                    return Dict.VERS_HTML40_STRICT;
                }

                break;

            case Dict.VERS_HTML40_LOOSE :
                if ((this.versions & Dict.VERS_HTML40_LOOSE) != 0)
                {
                    return Dict.VERS_HTML40_LOOSE;
                }

                break; // to replace old version by new

            case Dict.VERS_FRAMESET :
                if ((this.versions & Dict.VERS_FRAMESET) != 0)
                {
                    return Dict.VERS_FRAMESET;
                }

                break;
        }

        // kludge to avoid error appearing at end of file
        // it would be better to note the actual position
        // when first encountering the doctype declaration

        this.lines = 1;
        this.columns = 1;

        report.warning(this, null, null, Report.INCONSISTENT_VERSION);
        return this.htmlVersion();
    }

    // fixup doctype if missing
    public boolean fixDocType(Node root)
    {
        Node doctype;
        int guessed = Dict.VERS_HTML40_STRICT, i;

        if (this.badDoctype)
        {
            report.warning(this, null, null, Report.MALFORMED_DOCTYPE);
        }

        doctype = root.findDocType();

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_OMIT)
        {
            if (doctype != null)
            {
                Node.discardElement(doctype);
            }
            return true;
        }

        if (this.configuration.xmlOut)
        {
            return true;
        }

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_STRICT)
        {
            Node.discardElement(doctype);
            doctype = null;
            guessed = Dict.VERS_HTML40_STRICT;
        }
        else if (this.configuration.docTypeMode == Configuration.DOCTYPE_LOOSE)
        {
            Node.discardElement(doctype);
            doctype = null;
            guessed = Dict.VERS_HTML40_LOOSE;
        }
        else if (this.configuration.docTypeMode == Configuration.DOCTYPE_AUTO)
        {
            if (doctype != null)
            {
                if (this.doctype == Dict.VERS_UNKNOWN)
                {
                    return false;
                }

                switch (this.doctype)
                {
                    case Dict.VERS_UNKNOWN :
                        return false;

                    case Dict.VERS_HTML20 :
                        if ((this.versions & Dict.VERS_HTML20) != 0)
                        {
                            return true;
                        }

                        break; // to replace old version by new

                    case Dict.VERS_HTML32 :
                        if ((this.versions & Dict.VERS_HTML32) != 0)
                        {
                            return true;
                        }

                        break; // to replace old version by new

                    case Dict.VERS_HTML40_STRICT :
                        if ((this.versions & Dict.VERS_HTML40_STRICT) != 0)
                        {
                            return true;
                        }

                        break; // to replace old version by new

                    case Dict.VERS_HTML40_LOOSE :
                        if ((this.versions & Dict.VERS_HTML40_LOOSE) != 0)
                        {
                            return true;
                        }

                        break; // to replace old version by new

                    case Dict.VERS_FRAMESET :
                        if ((this.versions & Dict.VERS_FRAMESET) != 0)
                        {
                            return true;
                        }

                        break; // to replace old version by new
                }

                // INCONSISTENT_VERSION warning is now issued by ApparentVersion()
            }

            // choose new doctype
            guessed = htmlVersion();
        }

        if (guessed == Dict.VERS_UNKNOWN)
        {
            return false;
        }

        // for XML use the Voyager system identifier
        if (this.configuration.xmlOut || this.configuration.xmlTags || this.isvoyager)
        {
            if (doctype != null)
            {
                Node.discardElement(doctype);
            }

            fixHTMLNameSpace(root, XHTML_NAMESPACE);

            // Namespace is the same for all XHTML variants
            // Also, don't return yet. Still need to add DOCTYPE declaration.
            //
            //          for (i = 0; i < W3CVersion.length; ++i)
            //            {
            //                if (guessed == W3CVersion[i].code)
            //                {
            //                    fixHTMLNameSpace(root, W3CVersion[i].profile);
            //                    break;
            //                }
            //            }
            //            return true;
        }

        if (doctype == null)
        {
            if ((doctype = newXhtmlDocTypeNode(root)) == null)
            {
                return false;
            }
        }

        this.txtstart = this.lexsize;
        this.txtend = this.lexsize;

        // use the appropriate public identifier
        addStringLiteral("html PUBLIC ");

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_USER
            && this.configuration.docTypeStr != null
            && this.configuration.docTypeStr.length() > 0)
        {
            // check if the fpi is quoted or not
            if (this.configuration.docTypeStr.charAt(0) == '"')
            {
                addStringLiteral(this.configuration.docTypeStr);
            }
            else
            {
                addStringLiteral("\""); // #431889 - fix by Dave Bryan 04 Jan 2001
                addStringLiteral(this.configuration.docTypeStr);
                addStringLiteral("\""); // #431889 - fix by Dave Bryan 04 Jan 2001
            }
        }
        else if (guessed == Dict.VERS_HTML20)
        {
            addStringLiteral("\"-//IETF//DTD HTML 2.0//EN\"");
        }
        else
        {
            addStringLiteral("\"-//W3C//DTD ");

            for (i = 0; i < W3CVERSION.length; ++i)
            {
                if (guessed == W3CVERSION[i].code)
                {
                    addStringLiteral(W3CVERSION[i].name);
                    break;
                }
            }

            addStringLiteral("//EN\"");
        }

        this.txtend = this.lexsize;

        doctype.start = this.txtstart;
        doctype.end = this.txtend;

        return true;
    }

    /**
     * ensure XML document starts with <code>&lt;?XML version="1.0"?&gt;</code>. Add encoding attribute if not using
     * ASCII or UTF-8.
     */
    public boolean fixXmlDecl(Node root)
    {
        Node xml;
        AttVal version;
        AttVal encoding;

        if (root.content != null && root.content.type == Node.XML_DECL)
        {
            xml = root.content;
        }
        else
        {
            xml = newNode(Node.XML_DECL, this.lexbuf, 0, 0);
            xml.next = root.content;

            if (root.content != null)
            {
                root.content.prev = xml;
                xml.next = root.content;
            }

            root.content = xml;
        }

        version = xml.getAttrByName("version");
        encoding = xml.getAttrByName("encoding");

        // We need to insert a check if declared encoding and output encoding mismatch
        // and fix the Xml declaration accordingly!!!
        if (encoding == null && this.configuration.charEncoding != Configuration.UTF8)
        {
            if (this.configuration.charEncoding == Configuration.LATIN1)
            {
                xml.addAttribute("encoding", "iso-8859-1");
            }
            if (this.configuration.charEncoding == Configuration.ISO2022)
            {
                xml.addAttribute("encoding", "iso-2022");
            }
        }

        if (version == null)
        {
            xml.addAttribute("version", "1.0");
        }

        return true;
    }

    public Node inferredTag(String name)
    {
        Node node;

        node = newNode(Node.START_TAG, this.lexbuf, this.txtstart, this.txtend, name);
        node.implicit = true;
        return node;
    }

    public static boolean expectsContent(Node node)
    {
        if (node.type != Node.START_TAG)
        {
            return false;
        }

        // unknown element?
        if (node.tag == null)
        {
            return true;
        }

        if ((node.tag.model & Dict.CM_EMPTY) != 0)
        {
            return false;
        }

        return true;
    }

    /**
     * create a text node for the contents of a CDATA element like style or script which ends with &lt;/foo> for some
     * foo.
     */
    public Node getCDATA(Node container)
    {
        int c, lastc, start, len, i;
        String str;
        boolean endtag = false;

        this.lines = this.in.curline;
        this.columns = this.in.curcol;
        this.waswhite = false;
        this.txtstart = this.lexsize;
        this.txtend = this.lexsize;

        lastc = '\0';
        start = -1;

        while (true)
        {
            c = this.in.readChar();
            if (c == StreamIn.EndOfStream)
            {
                break;
            }
            // treat \r\n as \n and \r as \n

            if (c == '/' && lastc == '<')
            {
                if (endtag)
                {
                    this.lines = this.in.curline;
                    this.columns = this.in.curcol - 3;

                    report.warning(this, null, null, Report.BAD_CDATA_CONTENT);
                }

                start = this.lexsize + 1; // to first letter
                endtag = true;
            }
            else if (c == '>' && start >= 0)
            {
                len = this.lexsize - start;
                if (len == container.element.length())
                {
                    str = getString(this.lexbuf, start, len);
                    if (container.element.equalsIgnoreCase(str))
                    {
                        this.txtend = start - 2; // #433857 - fix by Huajun Zeng
                        break;
                    }
                }

                this.lines = this.in.curline;
                this.columns = this.in.curcol - 3;

                report.warning(this, null, null, Report.BAD_CDATA_CONTENT);

                // if javascript insert backslash before /

                if (ParserImpl.isJavaScript(container))
                {
                    for (i = this.lexsize; i > start - 1; --i)
                    {
                        this.lexbuf[i] = this.lexbuf[i - 1];
                    }

                    this.lexbuf[start - 1] = (byte) '\\';
                    this.lexsize++;
                }

                start = -1;
                endtag = false;
            }
            // #427844 - fix by Markus Hoenicka 21 Oct 00
            else if (c == '\r')
            {
                if (endtag)
                {
                    continue; // discard whitespace in endtag
                }
                else
                {

                    c = this.in.readChar();

                    if (c != '\n')
                    {
                        this.in.ungetChar(c);
                    }

                    c = '\n';
                }
            }
            else if ((c == '\n' || c == '\t' || c == ' ') && endtag)
            {
                continue; // discard whitespace in endtag
            }

            addCharToLexer(c);
            this.txtend = this.lexsize;
            lastc = c;
        }

        if (c == StreamIn.EndOfStream)
        {
            report.warning(this, container, null, Report.MISSING_ENDTAG_FOR);
        }

        if (this.txtend > this.txtstart)
        {
            this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
            return this.token;
        }

        return null;
    }

    public void ungetToken()
    {
        this.pushed = true;
    }

    /**
     * Gets a token.
     * @param mode one of the following:
     * <ul>
     * <li><code>MixedContent</code>-- for elements which don't accept PCDATA</li>
     * <li><code>Preformatted</code>-- white spacepreserved as is</li>
     * <li><code>IgnoreMarkup</code>-- for CDATA elements such as script, style</li>
     * </ul>
     */
    public Node getToken(short mode)
    {
        int c = 0;
        int badcomment = 0;
        MutableBoolean isempty = new MutableBoolean();
        boolean inDTDSubset = false;
        AttVal attributes = null;

        if (this.pushed)
        {
            // duplicate inlines in preference to pushed text nodes when appropriate
            if (this.token.type != Node.TEXT_NODE || (this.insert == -1 && this.inode == null))
            {
                this.pushed = false;
                return this.token;
            }
        }

        // at start of block elements, unclosed inline
        if (this.insert != -1 || this.inode != null)
        {
            return insertedToken();
        }

        this.lines = this.in.curline;
        this.columns = this.in.curcol;
        this.waswhite = false;

        this.txtstart = this.lexsize;
        this.txtend = this.lexsize;

        while (true)
        {
            c = this.in.readChar();
            if (c == StreamIn.EndOfStream)
            {
                break;
            }

            // FG fix for [427846] different from tidy
            // if (this.insertspace && ((mode & IGNORE_WHITESPACE) != 1))
            if (this.insertspace && mode != IGNORE_WHITESPACE)
            {
                addCharToLexer(' ');
            }
            if (this.insertspace && ((mode & IGNORE_WHITESPACE) != 1))
            {
                this.waswhite = true;
                this.insertspace = false;
            }

            // treat \r\n as \n and \r as \n

            if (c == '\r')
            {
                c = this.in.readChar();

                if (c != '\n')
                {
                    this.in.ungetChar(c);
                }

                c = '\n';
            }

            addCharToLexer(c);

            switch (this.state)
            {
                case LEX_CONTENT :
                    // element content

                    // Discard white space if appropriate.
                    // Its cheaper to do this here rather than in parser methods for elements that
                    // don't have mixed content.
                    if (isWhite((char) c) && (mode == IGNORE_WHITESPACE) && this.lexsize == this.txtstart + 1)
                    {
                        --this.lexsize;
                        this.waswhite = false;
                        this.lines = this.in.curline;
                        this.columns = this.in.curcol;
                        continue;
                    }

                    if (c == '<')
                    {
                        this.state = LEX_GT;
                        continue;
                    }

                    if (isWhite((char) c))
                    {
                        // was previous char white?
                        if (this.waswhite)
                        {
                            if (mode != PREFORMATTED && mode != IGNORE_MARKUP)
                            {
                                --this.lexsize;
                                this.lines = this.in.curline;
                                this.columns = this.in.curcol;
                            }
                        }
                        else
                        {
                            // prev char wasn't white
                            this.waswhite = true;

                            if (mode != PREFORMATTED && mode != IGNORE_MARKUP && c != ' ')
                            {
                                changeChar((byte) ' ');
                            }
                        }

                        continue;
                    }
                    else if (c == '&' && mode != IGNORE_MARKUP)
                    {
                        parseEntity(mode);
                    }

                    // this is needed to avoid trimming trailing whitespace
                    if (mode == IGNORE_WHITESPACE)
                    {
                        mode = MIXED_CONTENT;
                    }

                    this.waswhite = false;
                    continue;

                case LEX_GT :
                    // <

                    // check for endtag
                    if (c == '/')
                    {
                        c = this.in.readChar();
                        if (c == StreamIn.EndOfStream)
                        {
                            this.in.ungetChar(c);
                            continue;
                        }

                        addCharToLexer(c);

                        if (isLetter((char) c))
                        {
                            this.lexsize -= 3;
                            this.txtend = this.lexsize;
                            this.in.ungetChar(c);
                            this.state = LEX_ENDTAG;
                            this.lexbuf[this.lexsize] = (byte) '\0'; // debug
                            this.in.curcol -= 2;

                            // if some text before the </ return it now
                            if (this.txtend > this.txtstart)
                            {
                                // trim space char before end tag
                                if (mode == IGNORE_WHITESPACE && this.lexbuf[this.lexsize - 1] == (byte) ' ')
                                {
                                    this.lexsize -= 1;
                                    this.txtend = this.lexsize;
                                }

                                this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                                return this.token;
                            }

                            continue; // no text so keep going
                        }

                        // otherwise treat as CDATA
                        this.waswhite = false;
                        this.state = LEX_CONTENT;
                        continue;
                    }

                    if (mode == IGNORE_MARKUP)
                    {
                        // otherwise treat as CDATA
                        this.waswhite = false;
                        this.state = LEX_CONTENT;
                        continue;
                    }

                    // look out for comments, doctype or marked sections this isn't quite right, but its getting there
                    if (c == '!')
                    {
                        c = this.in.readChar();

                        if (c == '-')
                        {
                            c = this.in.readChar();

                            if (c == '-')
                            {
                                this.state = LEX_COMMENT; // comment
                                this.lexsize -= 2;
                                this.txtend = this.lexsize;

                                // if some text before < return it now
                                if (this.txtend > this.txtstart)
                                {
                                    this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                                    return this.token;
                                }

                                this.txtstart = this.lexsize;
                                continue;
                            }

                            report.warning(this, null, null, Report.MALFORMED_COMMENT);
                        }
                        else if (c == 'd' || c == 'D')
                        {
                            this.state = LEX_DOCTYPE; // doctype
                            this.lexsize -= 2;
                            this.txtend = this.lexsize;
                            mode = IGNORE_WHITESPACE;

                            // skip until white space or '>'

                            for (;;)
                            {
                                c = this.in.readChar();

                                if (c == StreamIn.EndOfStream || c == '>')
                                {
                                    this.in.ungetChar(c);
                                    break;
                                }

                                if (!isWhite((char) c))
                                {
                                    continue;
                                }

                                // and skip to end of whitespace

                                for (;;)
                                {
                                    c = this.in.readChar();

                                    if (c == StreamIn.EndOfStream || c == '>')
                                    {
                                        this.in.ungetChar(c);
                                        break;
                                    }

                                    if (isWhite((char) c))
                                    {
                                        continue;
                                    }

                                    this.in.ungetChar(c);
                                    break;
                                }

                                break;
                            }

                            // if some text before < return it now
                            if (this.txtend > this.txtstart)
                            {
                                this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                                return this.token;
                            }

                            this.txtstart = this.lexsize;
                            continue;
                        }
                        else if (c == '[')
                        {
                            // Word 2000 embeds <![if ...]> ... <![endif]> sequences
                            this.lexsize -= 2;
                            this.state = LEX_SECTION;
                            this.txtend = this.lexsize;

                            // if some text before < return it now
                            if (this.txtend > this.txtstart)
                            {
                                this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                                return this.token;
                            }

                            this.txtstart = this.lexsize;
                            continue;
                        }

                        // otherwise swallow chars up to and including next '>'
                        while (true)
                        {
                            c = this.in.readChar();
                            if (c == '>')
                            {
                                break;
                            }
                            if (c == -1)
                            {
                                this.in.ungetChar(c);
                                break;
                            }
                        }

                        this.lexsize -= 2;
                        this.lexbuf[this.lexsize] = (byte) '\0';
                        this.state = LEX_CONTENT;
                        continue;
                    }

                    // processing instructions

                    if (c == '?')
                    {
                        this.lexsize -= 2;
                        this.state = LEX_PROCINSTR;
                        this.txtend = this.lexsize;

                        // if some text before < return it now
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        this.txtstart = this.lexsize;
                        continue;
                    }

                    // Microsoft ASP's e.g. <% ... server-code ... %>
                    if (c == '%')
                    {
                        this.lexsize -= 2;
                        this.state = LEX_ASP;
                        this.txtend = this.lexsize;

                        // if some text before < return it now
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        this.txtstart = this.lexsize;
                        continue;
                    }

                    // Netscapes JSTE e.g. <# ... server-code ... #>
                    if (c == '#')
                    {
                        this.lexsize -= 2;
                        this.state = LEX_JSTE;
                        this.txtend = this.lexsize;

                        // if some text before < return it now
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        this.txtstart = this.lexsize;
                        continue;
                    }

                    // check for start tag
                    if (isLetter((char) c))
                    {
                        this.in.ungetChar(c); // push back letter
                        this.lexsize -= 2; // discard " <" + letter
                        this.txtend = this.lexsize;
                        this.state = LEX_STARTTAG; // ready to read tag name

                        // if some text before < return it now
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        continue; // no text so keep going
                    }

                    // otherwise treat as CDATA
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    continue;

                case LEX_ENDTAG :
                    // </letter
                    this.txtstart = this.lexsize - 1;
                    this.in.curcol += 2;
                    c = parseTagName();
                    this.token = newNode(Node.END_TAG, // create endtag token
                        this.lexbuf, this.txtstart, this.txtend, getString(this.lexbuf, this.txtstart, this.txtend
                            - this.txtstart));
                    this.lexsize = this.txtstart;
                    this.txtend = this.txtstart;

                    // skip to '>'
                    while (c != '>')
                    {
                        c = this.in.readChar();

                        if (c == StreamIn.EndOfStream)
                        {
                            break;
                        }
                    }

                    if (c == StreamIn.EndOfStream)
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    return this.token; // the endtag token

                case LEX_STARTTAG :
                    // first letter of tagname
                    this.txtstart = this.lexsize - 1; // set txtstart to first letter
                    c = parseTagName();
                    isempty.setValue(false);
                    attributes = null;
                    this.token = newNode(
                        (isempty.getValue() ? Node.START_END_TAG : Node.START_TAG),
                        this.lexbuf,
                        this.txtstart,
                        this.txtend,
                        getString(this.lexbuf, this.txtstart, this.txtend - this.txtstart));

                    // parse attributes, consuming closing ">"
                    if (c != '>')
                    {
                        if (c == '/')
                        {
                            this.in.ungetChar(c);
                        }

                        attributes = parseAttrs(isempty);
                    }

                    if (isempty.getValue())
                    {
                        this.token.type = Node.START_END_TAG;
                    }

                    this.token.attributes = attributes;
                    this.lexsize = this.txtstart;
                    this.txtend = this.txtstart;

                    // swallow newline following start tag
                    // special check needed for CRLF sequence
                    // this doesn't apply to empty elements
                    // nor to preformatted content that needs escaping

                    if (

                    (mode != PREFORMATTED || preContent(this.token))
                        && (expectsContent(this.token) || this.token.tag == this.configuration.tt.tagBr))
                    {

                        c = this.in.readChar();

                        if (c == '\r')
                        {
                            c = this.in.readChar();

                            if (c != '\n')
                            {
                                this.in.ungetChar(c);
                            }
                        }
                        else if (c != '\n' && c != '\f')
                        {
                            this.in.ungetChar(c);
                        }

                        this.waswhite = true; // to swallow leading whitespace
                    }
                    else
                    {
                        this.waswhite = false;
                    }

                    this.state = LEX_CONTENT;

                    if (this.token.tag == null)
                    {
                        report.error(this, null, this.token, Report.UNKNOWN_ELEMENT);
                    }
                    else if (!this.configuration.xmlTags)
                    {
                        constrainVersion(this.token.tag.versions);

                        if ((this.token.tag.versions & Dict.VERS_PROPRIETARY) != 0)
                        {
                            // #427810 - fix by Gary Deschaines 24 May 00
                            if (this.configuration.makeClean && (this.token.tag != this.configuration.tt.tagNobr && //
                                this.token.tag != this.configuration.tt.tagWbr))
                            {
                                report.warning(this, null, this.token, Report.PROPRIETARY_ELEMENT);
                            }
                            // #427810 - fix by Terry Teague 2 Jul 01
                            else if (!this.configuration.makeClean)
                            {
                                report.warning(this, null, this.token, Report.PROPRIETARY_ELEMENT);
                            }
                        }

                        if (this.token.tag.chkattrs != null)
                        {
                            this.token.tag.chkattrs.check(this, this.token);
                        }
                        else
                        {
                            this.token.checkAttributes(this);
                        }

                    }

                    // FG repair attributes fo xml on input or output
                    if (this.configuration.xmlTags | this.configuration.xmlOut | this.configuration.xHTML)
                    {
                        // should this be called before attribute checks?
                        this.token.repairDuplicateAttributes(this);
                    }

                    return this.token; // return start tag

                case LEX_COMMENT :
                    // seen <!-- so look for -->

                    if (c != '-')
                    {
                        continue;
                    }

                    c = this.in.readChar();
                    addCharToLexer(c);

                    if (c != '-')
                    {
                        continue;
                    }

                    end_comment : while (true)
                    {
                        c = this.in.readChar();

                        if (c == '>')
                        {
                            if (badcomment != 0)
                            {
                                report.warning(this, null, null, Report.MALFORMED_COMMENT);
                            }

                            this.txtend = this.lexsize - 2; // AQ 8Jul2000
                            this.lexbuf[this.lexsize] = (byte) '\0';
                            this.state = LEX_CONTENT;
                            this.waswhite = false;
                            this.token = newNode(Node.COMMENT_TAG, this.lexbuf, this.txtstart, this.txtend);

                            // now look for a line break

                            c = this.in.readChar();

                            if (c == '\r')
                            {
                                c = this.in.readChar();

                                if (c != '\n')
                                {
                                    this.token.linebreak = true;
                                }
                            }

                            if (c == '\n')
                            {
                                this.token.linebreak = true;
                            }
                            else
                            {
                                this.in.ungetChar(c);
                            }

                            return this.token;
                        }

                        // note position of first such error in the comment
                        if (badcomment == 0)
                        {
                            this.lines = this.in.curline;
                            this.columns = this.in.curcol - 3;
                        }

                        badcomment++;
                        if (this.configuration.fixComments)
                        {
                            this.lexbuf[this.lexsize - 2] = (byte) '=';
                        }

                        addCharToLexer(c);

                        // if '-' then look for '>' to end the comment
                        if (c != '-')
                        {
                            break end_comment;
                        }

                    }
                    // otherwise continue to look for -->
                    this.lexbuf[this.lexsize - 2] = (byte) '=';
                    continue;

                case LEX_DOCTYPE :
                    // seen <!d so look for '> ' munging whitespace

                    if (isWhite((char) c))
                    {
                        if (this.waswhite)
                        {
                            this.lexsize -= 1;
                        }

                        this.waswhite = true;
                    }
                    else
                    {
                        this.waswhite = false;
                    }

                    if (inDTDSubset)
                    {
                        if (c == ']')
                        {
                            inDTDSubset = false;
                        }
                    }
                    else if (c == '[')
                    {
                        inDTDSubset = true;
                    }
                    if (inDTDSubset || c != '>')
                    {
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.DOCTYPE_TAG, this.lexbuf, this.txtstart, this.txtend);
                    // make a note of the version named by the doctype
                    this.doctype = findGivenVersion(this.token);
                    return this.token;

                case LEX_PROCINSTR :
                    // seen <? so look for '> '
                    // check for PHP preprocessor instructions <?php ... ?>

                    if (this.lexsize - this.txtstart == 3)
                    {
                        if ((getString(this.lexbuf, this.txtstart, 3)).equals("php"))
                        {
                            this.state = LEX_PHP;
                            continue;
                        }
                    }

                    if (this.lexsize - this.txtstart == 3)
                    {
                        if ((getString(this.lexbuf, this.txtstart, 3)).equals("xml"))
                        {
                            this.state = LEX_XMLDECL;
                            attributes = null;
                            continue;
                        }
                    }

                    if (this.configuration.xmlPIs) // insist on ?> as terminator
                    {
                        if (c != '?')
                        {
                            continue;
                        }

                        // now look for '>'
                        c = this.in.readChar();

                        if (c == StreamIn.EndOfStream)
                        {
                            report.warning(this, null, null, Report.UNEXPECTED_END_OF_FILE);
                            this.in.ungetChar(c);
                            continue;
                        }

                        addCharToLexer(c);
                    }

                    if (c != '>')
                    {
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.PROC_INS_TAG, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_ASP :
                    // seen <% so look for "%> "
                    if (c != '%')
                    {
                        continue;
                    }

                    // now look for '>'
                    c = this.in.readChar();

                    if (c != '>')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.ASP_TAG, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_JSTE :
                    // seen <# so look for "#> "
                    if (c != '#')
                    {
                        continue;
                    }

                    // now look for '>'
                    c = this.in.readChar();

                    if (c != '>')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.JSTE_TAG, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_PHP :
                    // seen " <?php" so look for "?> "
                    if (c != '?')
                    {
                        continue;
                    }

                    // now look for '>'
                    c = this.in.readChar();

                    if (c != '>')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.PHP_TAG, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_XMLDECL : // seen "<?xml" so look for "?>"

                    if (isWhite((char) c) && c != '?')
                    {
                        continue;
                    }

                    // get pseudo-attribute
                    if (c != '?')
                    {
                        String name;
                        MutableObject asp = new MutableObject();
                        MutableObject php = new MutableObject();
                        AttVal av = new AttVal();
                        MutableInteger pdelim = new MutableInteger();
                        isempty.setValue(false);

                        this.in.ungetChar(c);

                        name = this.parseAttribute(isempty, asp, php);
                        av.attribute = name;

                        av.value = this.parseValue(name, true, isempty, pdelim);
                        av.delim = pdelim.getValue();
                        av.next = attributes;

                        attributes = av;
                        // continue;
                    }

                    // now look for '>'
                    c = this.in.readChar();

                    if (c != '>')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }
                    this.lexsize -= 1;
                    this.txtend = this.txtstart;
                    this.lexbuf[this.txtend] = '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.XML_DECL, this.lexbuf, this.txtstart, this.txtend);
                    this.token.attributes = attributes;
                    return this.token;

                case LEX_SECTION :
                    // seen " <![" so look for "]> "
                    if (c == '[')
                    {
                        if (this.lexsize == (this.txtstart + 6)
                            && (getString(this.lexbuf, this.txtstart, 6)).equals("CDATA["))
                        {
                            this.state = LEX_CDATA;
                            this.lexsize -= 6;
                            continue;
                        }
                    }

                    if (c != ']')
                    {
                        continue;
                    }

                    // now look for '>'
                    c = this.in.readChar();

                    if (c != '>')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.SECTION_TAG, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_CDATA :
                    // seen " <![CDATA[" so look for "]]> "
                    if (c != ']')
                    {
                        continue;
                    }

                    // now look for ']'
                    c = this.in.readChar();

                    if (c != ']')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    // now look for '>'
                    c = this.in.readChar();

                    if (c != '>')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.CDATA_TAG, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;
            }
        }

        if (this.state == LEX_CONTENT) // text string
        {
            this.txtend = this.lexsize;

            if (this.txtend > this.txtstart)
            {
                this.in.ungetChar(c);

                if (this.lexbuf[this.lexsize - 1] == (byte) ' ')
                {
                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                }

                this.token = newNode(Node.TEXT_NODE, this.lexbuf, this.txtstart, this.txtend);
                return this.token;
            }
        }
        else if (this.state == LEX_COMMENT) // comment
        {
            if (c == StreamIn.EndOfStream)
            {
                report.warning(this, null, null, Report.MALFORMED_COMMENT);
            }

            this.txtend = this.lexsize;
            this.lexbuf[this.lexsize] = (byte) '\0';
            this.state = LEX_CONTENT;
            this.waswhite = false;
            this.token = newNode(Node.COMMENT_TAG, this.lexbuf, this.txtstart, this.txtend);
            return this.token;
        }

        return null;
    }

    /**
     * parser for ASP within start tags Some people use ASP for to customize attributes Tidy isn't really well suited to
     * dealing with ASP This is a workaround for attributes, but won't deal with the case where the ASP is used to
     * tailor the attribute value. Here is an example of a work around for using ASP in attribute values:
     * <code>href='<%=rsSchool.Fields("ID").Value%>'</code> where the ASP that generates the attribute value is
     * masked from Tidy by the quotemarks.
     */
    public Node parseAsp()
    {
        int c;
        Node asp = null;

        this.txtstart = this.lexsize;

        for (;;)
        {
            if ((c = this.in.readChar()) == StreamIn.EndOfStream)
            {
                break;
            }

            addCharToLexer(c);

            if (c != '%')
            {
                continue;
            }

            if ((c = this.in.readChar()) == StreamIn.EndOfStream)
            {
                break;
            }
            addCharToLexer(c);

            if (c == '>')
            {
                break;
            }
        }

        this.lexsize -= 2;
        this.txtend = this.lexsize;

        if (this.txtend > this.txtstart)
        {
            asp = newNode(Node.ASP_TAG, this.lexbuf, this.txtstart, this.txtend);
        }

        this.txtstart = this.txtend;
        return asp;
    }

    /**
     * PHP is like ASP but is based upon XML processing instructions, e.g. <code>&lt;?php ... ?&gt;</code>.
     */
    public Node parsePhp()
    {
        int c;
        Node php = null;

        this.txtstart = this.lexsize;

        for (;;)
        {
            if ((c = this.in.readChar()) == StreamIn.EndOfStream)
            {
                break;
            }
            addCharToLexer(c);

            if (c != '?')
            {
                continue;
            }

            if ((c = this.in.readChar()) == StreamIn.EndOfStream)
            {
                break;
            }
            addCharToLexer(c);

            if (c == '>')
            {
                break;
            }
        }

        this.lexsize -= 2;
        this.txtend = this.lexsize;

        if (this.txtend > this.txtstart)
        {
            php = newNode(Node.PHP_TAG, this.lexbuf, this.txtstart, this.txtend);
        }

        this.txtstart = this.txtend;
        return php;
    }

    /**
     * consumes the '>' terminating start tags.
     */
    public String parseAttribute(MutableBoolean isempty, MutableObject asp, MutableObject php)
    {
        int start = 0;
        String attr;
        int c = 0;
        int lastc = 0;

        asp.setObject(null); // clear asp pointer
        php.setObject(null); // clear php pointer
        // skip white space before the attribute

        for (;;)
        {
            c = this.in.readChar();

            if (c == '/')
            {
                c = this.in.readChar();

                if (c == '>')
                {
                    isempty.setValue(true);
                    return null;
                }

                this.in.ungetChar(c);
                c = '/';
                break;
            }

            if (c == '>')
            {
                return null;
            }

            if (c == '<')
            {
                c = this.in.readChar();

                if (c == '%')
                {
                    asp.setObject(parseAsp());
                    return null;
                }
                else if (c == '?')
                {
                    php.setObject(parsePhp());
                    return null;
                }

                this.in.ungetChar(c);
                if (this.state != LEX_XMLDECL) // FG fix for 532535
                {
                    this.in.ungetChar('<'); // fix for 433360
                }
                report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
                return null;
            }

            if (c == '=')
            {
                report.attrError(this, this.token, null, Report.UNEXPECTED_EQUALSIGN);
                continue;
            }

            if (c == '"' || c == '\'')
            {
                report.attrError(this, this.token, null, Report.UNEXPECTED_QUOTEMARK);
                continue;
            }

            if (c == StreamIn.EndOfStream)
            {
                report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
                this.in.ungetChar(c);
                return null;
            }

            if (!isWhite((char) c))
            {
                break;
            }
        }

        start = this.lexsize;
        lastc = c;

        for (;;)
        {
            // but push back '=' for parseValue()
            if (c == '=' || c == '>')
            {
                this.in.ungetChar(c);
                break;
            }

            if (c == '<' || c == StreamIn.EndOfStream)
            {
                this.in.ungetChar(c);
                break;
            }
            if (lastc == '-' && (c == '"' || c == '\''))
            {
                this.lexsize--;
                this.in.ungetChar(c);
                break;
            }
            if (isWhite((char) c))
            {
                break;
            }

            // what should be done about non-namechar characters?
            // currently these are incorporated into the attr name

            if (!this.configuration.xmlTags && isUpper((char) c))
            {
                c = toLower((char) c);
            }

            //  ++len; #427672 - handle attribute names with multibyte chars - fix by Randy Waki - 10 Aug 00
            addCharToLexer(c);

            lastc = c;
            c = this.in.readChar();
        }

        // #427672 - handle attribute names with multibyte chars - fix by Randy Waki - 10 Aug 00
        int len = this.lexsize - start;
        attr = (len > 0 ? getString(this.lexbuf, start, len) : null);
        this.lexsize = start;

        return attr;
    }

    /**
     * invoked when &lt; is seen in place of attribute value but terminates on whitespace if not ASP, PHP or Tango this
     * routine recognizes ' and " quoted strings.
     */
    public int parseServerInstruction()
    {
        int c, delim = '"';
        boolean isrule = false;

        c = this.in.readChar();
        addCharToLexer(c);

        // check for ASP, PHP or Tango
        if (c == '%' || c == '?' || c == '@')
        {
            isrule = true;
        }

        for (;;)
        {
            c = this.in.readChar();

            if (c == StreamIn.EndOfStream)
            {
                break;
            }

            if (c == '>')
            {
                if (isrule)
                {
                    addCharToLexer(c);
                }
                else
                {
                    this.in.ungetChar(c);
                }

                break;
            }

            // if not recognized as ASP, PHP or Tango
            // then also finish value on whitespace
            if (!isrule)
            {
                if (isWhite((char) c))
                {
                    break;
                }
            }

            addCharToLexer(c);

            if (c == '"')
            {
                do
                {
                    c = this.in.readChar();

                    if (endOfInput()) // #427840 - fix by Terry Teague 30 Jun 01
                    {
                        report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
                        this.in.ungetChar(c);
                        return 0;
                    }
                    if (c == '>') // #427840 - fix by Terry Teague 30 Jun 01
                    {
                        this.in.ungetChar(c);
                        report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
                        return 0;
                    }

                    addCharToLexer(c);
                }
                while (c != '"');
                delim = '\'';
                continue;
            }

            if (c == '\'')
            {
                do
                {
                    c = this.in.readChar();

                    if (endOfInput()) // #427840 - fix by Terry Teague 30 Jun 01
                    {
                        report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
                        this.in.ungetChar(c);
                        return 0;
                    }
                    if (c == '>') // #427840 - fix by Terry Teague 30 Jun 01
                    {
                        this.in.ungetChar(c);
                        report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
                        return 0;
                    }

                    addCharToLexer(c);
                }
                while (c != '\'');
            }
        }

        return delim;
    }

    // values start with "=" or " = " etc.
    // doesn't consume the ">" at end of start tag

    public String parseValue(String name, boolean foldCase, MutableBoolean isempty, MutableInteger pdelim)
    {
        int len = 0;
        int start;
        boolean seenGt = false;
        boolean munge = true;
        int c = 0;
        int lastc, delim, quotewarning;
        String value;

        delim = 0;
        pdelim.setValue('"');

        // Henry Zrepa reports that some folk are using the embed element with script attributes where newlines are
        // significant and must be preserved

        if (this.configuration.literalAttribs)
        {
            munge = false;
        }

        // skip white space before the '='

        for (;;)
        {
            c = this.in.readChar();

            if (c == StreamIn.EndOfStream)
            {
                this.in.ungetChar(c);
                break;
            }

            if (!isWhite((char) c))
            {
                break;
            }
        }

        // c should be '=' if there is a value other legal possibilities are white space, '/' and '>'

        if (c != '=' && c != '"' && c != '\'')
        {
            this.in.ungetChar(c);
            return null;
        }

        // skip white space after '='

        for (;;)
        {
            c = this.in.readChar();

            if (c == StreamIn.EndOfStream)
            {
                this.in.ungetChar(c);
                break;
            }

            if (!isWhite((char) c))
            {
                break;
            }
        }

        // check for quote marks

        if (c == '"' || c == '\'')
        {
            delim = c;
        }
        else if (c == '<')
        {
            start = this.lexsize;
            addCharToLexer(c);
            pdelim.setValue(parseServerInstruction());
            len = this.lexsize - start;
            this.lexsize = start;
            return (len > 0 ? getString(this.lexbuf, start, len) : null);
        }
        else
        {
            this.in.ungetChar(c);
        }

        // and read the value string check for quote mark if needed

        quotewarning = 0;
        start = this.lexsize;
        c = '\0';

        for (;;)
        {
            lastc = c; // track last character
            c = this.in.readChar();

            if (c == StreamIn.EndOfStream)
            {
                report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
                this.in.ungetChar(c);
                break;
            }

            if (delim == (char) 0)
            {
                if (c == '>')
                {
                    this.in.ungetChar(c);
                    break;
                }

                if (c == '"' || c == '\'')
                {
                    report.attrError(this, this.token, null, Report.UNEXPECTED_QUOTEMARK);
                    break;
                }

                if (c == '<')
                {
                    this.in.ungetChar(c); // fix for 433360
                    c = '>';
                    this.in.ungetChar(c);
                    report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
                    break;
                }

                // For cases like <br clear=all/> need to avoid treating /> as part of the attribute value, however
                // care is needed to avoid so treating <a href=http://www.acme.com /> in this way, which would map the
                // <a> tag to <a href="http://www.acme.com"/>

                if (c == '/')
                {
                    // peek ahead in case of />
                    c = this.in.readChar();

                    if (c == '>' && !AttributeTable.getDefaultAttributeTable().isUrl(name))
                    {
                        isempty.setValue(true);
                        this.in.ungetChar(c);
                        break;
                    }

                    // unget peeked char
                    this.in.ungetChar(c);
                    c = '/';
                }
            }
            else
            {
                // delim is '\'' or '"'
                if (c == delim)
                {
                    break;
                }

                // treat CRLF, CR and LF as single line break

                if (c == '\r')
                {
                    c = this.in.readChar();
                    if (c != '\n')
                    {
                        this.in.ungetChar(c);
                    }

                    c = '\n';
                }

                if (c == '\n' || c == '<' || c == '>')
                {
                    ++quotewarning;
                }

                if (c == '>')
                {
                    seenGt = true;
                }
            }

            if (c == '&')
            {
                // no entities in ID attributes
                if ("id".equalsIgnoreCase(name))
                {
                    report.attrError(this, null, null, Report.ENTITY_IN_ID);
                    continue;
                }
                else
                {
                    addCharToLexer(c);
                    parseEntity((short) 0);
                    continue;
                }
            }

            // kludge for JavaScript attribute values with line continuations in string literals

            if (c == '\\')
            {
                c = this.in.readChar();

                if (c != '\n')
                {
                    this.in.ungetChar(c);
                    c = '\\';
                }
            }

            if (isWhite((char) c))
            {
                if (delim == (char) 0)
                {
                    break;
                }

                if (munge)
                {
                    // discard line breaks in quoted URLs
                    // #438650 - fix by Randy Waki
                    if (c == '\n' && AttributeTable.getDefaultAttributeTable().isUrl(name))
                    {
                        // warn that we discard this newline
                        report.attrError(this, this.token, null, Report.NEWLINE_IN_URI);
                        continue;
                    }

                    c = ' ';

                    if (lastc == ' ')
                    {
                        continue;
                    }
                }
            }
            else if (foldCase && isUpper((char) c))
            {
                c = toLower((char) c);
            }

            addCharToLexer(c);
        }

        if (quotewarning > 10 && seenGt && munge)
        {
            // there is almost certainly a missing trailing quote mark as we have see too many newlines, < or >
            // characters. an exception is made for Javascript attributes and the javascript URL scheme which may
            // legitimately include < and >

            if (!AttributeTable.getDefaultAttributeTable().isScript(name)
                && !(AttributeTable.getDefaultAttributeTable().isUrl(name) && (getString(this.lexbuf, start, 11))
                    .equals("javascript:")))
            {
                report.error(this, null, null, Report.SUSPECTED_MISSING_QUOTE);
            }
        }

        len = this.lexsize - start;
        this.lexsize = start;

        if (len > 0 || delim != 0)
        {
            value = getString(this.lexbuf, start, len);
        }
        else
        {
            value = null;
        }

        // note delimiter if given
        if (delim != 0)
        {
            pdelim.setValue(delim);
        }
        else
        {
            pdelim.setValue('"');
        }

        return value;
    }

    // attr must be non-null
    public static boolean isValidAttrName(String attr)
    {
        char c;
        int i;

        // first character should be a letter
        c = attr.charAt(0);

        if (!isLetter(c))
        {
            return false;
        }

        // remaining characters should be namechars
        for (i = 1; i < attr.length(); i++)
        {
            c = attr.charAt(i);

            if (isNamechar(c))
            {
                continue;
            }

            return false;
        }

        return true;
    }

    // swallows closing '>'
    public AttVal parseAttrs(MutableBoolean isempty)
    {
        AttVal av, list;
        String attribute, value;
        MutableInteger delim = new MutableInteger();
        MutableObject asp = new MutableObject();
        MutableObject php = new MutableObject();

        list = null;

        while (!endOfInput())
        {
            attribute = parseAttribute(isempty, asp, php);

            if (attribute == null)
            {
                // check if attributes are created by ASP markup
                if (asp.getObject() != null)
                {
                    av = new AttVal(list, null, (Node) asp.getObject(), null, '\0', null, null);
                    list = av;
                    continue;
                }

                // check if attributes are created by PHP markup
                if (php.getObject() != null)
                {
                    av = new AttVal(list, null, null, (Node) php.getObject(), '\0', null, null);
                    list = av;
                    continue;
                }

                break;
            }

            value = parseValue(attribute, false, isempty, delim);

            if (attribute != null && isValidAttrName(attribute))
            {
                av = new AttVal(list, null, null, null, delim.getValue(), attribute, value);
                av.dict = AttributeTable.getDefaultAttributeTable().findAttribute(av);
                list = av;
            }
            else
            {
                av = new AttVal(null, null, null, null, 0, attribute, value);

                // #427664 - fix by Gary Peskin 04 Aug 00; other fixes by Dave Raggett
                if (value != null)
                {
                    report.attrError(this, this.token, av, Report.BAD_ATTRIBUTE_VALUE);
                }
                else if (lastChar(attribute) == '"')
                {
                    report.attrError(this, this.token, av, Report.MISSING_QUOTEMARK);
                }
                else
                {
                    report.attrError(this, this.token, av, Report.UNKNOWN_ATTRIBUTE);
                }
            }
        }

        return list;
    }

    /**
     * push a copy of an inline node onto stack but don't push if implicit or OBJECT or APPLET (implicit tags are ones
     * generated from the istack) One issue arises with pushing inlines when the tag is already pushed. For instance:
     * <code>&lt;p>&lt;em> text &lt;p>&lt;em> more text</code> Shouldn't be mapped to
     * <code>&lt;p>&lt;em> text &lt;/em>&lt;/p>&lt;p>&lt;em>&lt;em> more text &lt;/em>&lt;/em></code>
     */
    public void pushInline(Node node)
    {
        IStack is;

        if (node.implicit)
        {
            return;
        }

        if (node.tag == null)
        {
            return;
        }

        if ((node.tag.model & Dict.CM_INLINE) == 0)
        {
            return;
        }

        if ((node.tag.model & Dict.CM_OBJECT) != 0)
        {
            return;
        }

        if (node.tag != this.configuration.tt.tagFont && isPushed(node))
        {
            return;
        }

        // make sure there is enough space for the stack
        is = new IStack();
        is.tag = node.tag;
        is.element = node.element;
        if (node.attributes != null)
        {
            is.attributes = cloneAttributes(node.attributes);
        }
        this.istack.push(is);
    }

    // pop inline stack
    public void popInline(Node node)
    {
        IStack is;

        if (node != null)
        {

            if (node.tag == null)
            {
                return;
            }

            if ((node.tag.model & Dict.CM_INLINE) == 0)
            {
                return;
            }

            if ((node.tag.model & Dict.CM_OBJECT) != 0)
            {
                return;
            }

            // if node is </a> then pop until we find an <a>
            if (node.tag == this.configuration.tt.tagA)
            {

                while (this.istack.size() > 0)
                {
                    is = (IStack) this.istack.pop();
                    if (is.tag == this.configuration.tt.tagA)
                    {
                        break;
                    }
                }

                if (this.insert >= this.istack.size())
                {
                    this.insert = -1;
                }
                return;
            }
        }

        if (this.istack.size() > 0)
        {
            is = (IStack) this.istack.pop();
            if (this.insert >= this.istack.size())
            {
                this.insert = -1;
            }
        }
    }

    public boolean isPushed(Node node)
    {
        int i;
        IStack is;

        for (i = this.istack.size() - 1; i >= 0; --i)
        {
            is = (IStack) this.istack.elementAt(i);
            if (is.tag == node.tag)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * This has the effect of inserting "missing" inline elements around the contents of blocklevel elements such as P,
     * TD, TH, DIV, PRE etc. This procedure is called at the start of ParseBlock. when the inline stack is not empty, as
     * will be the case in: <i>
     * <h1>italic heading</h1>
     * </i> which is then treated as equivalent to
     * <h1><i>italic heading </i></h1>
     * This is implemented by setting the lexer into a mode where it gets tokens from the inline stack rather than from
     * the input stream.
     */
    public int inlineDup(Node node)
    {
        int n;

        n = this.istack.size() - this.istackbase;
        if (n > 0)
        {
            this.insert = this.istackbase;
            this.inode = node;
        }

        return n;
    }

    public Node insertedToken()
    {
        Node node;
        IStack is;
        int n;

        // this will only be null if inode != null
        if (this.insert == -1)
        {
            node = this.inode;
            this.inode = null;
            return node;
        }

        // is this is the "latest" node then update
        // the position, otherwise use current values

        if (this.inode == null)
        {
            this.lines = this.in.curline;
            this.columns = this.in.curcol;
        }

        node = newNode(Node.START_TAG, this.lexbuf, this.txtstart, this.txtend);
        // GLP: Bugfix 126261. Remove when this change
        //       is fixed in istack.c in the original Tidy
        node.implicit = true;
        is = (IStack) this.istack.elementAt(this.insert);
        node.element = is.element;
        node.tag = is.tag;
        if (is.attributes != null)
        {
            node.attributes = cloneAttributes(is.attributes);
        }

        // advance lexer to next item on the stack
        n = this.insert;

        // and recover state if we have reached the end
        if (++n < this.istack.size())
        {
            this.insert = n;
        }
        else
        {
            this.insert = -1;
        }

        return node;
    }

    public static boolean wsubstr(String s1, String s2)
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

    public boolean canPrune(Node element)
    {
        if (element.type == Node.TEXT_NODE)
        {
            return true;
        }

        if (element.content != null)
        {
            return false;
        }

        if (element.tag == this.configuration.tt.tagA && element.attributes != null)
        {
            return false;
        }

        if (element.tag == this.configuration.tt.tagP && !this.configuration.dropEmptyParas)
        {
            return false;
        }

        if (element.tag == null)
        {
            return false;
        }

        if ((element.tag.model & Dict.CM_ROW) != 0)
        {
            return false;
        }

        if (element.tag == this.configuration.tt.tagApplet)
        {
            return false;
        }

        if (element.tag == this.configuration.tt.tagObject)
        {
            return false;
        }

        // #540555 Empty title tag is trimmed
        if (element.tag == this.configuration.tt.tagTitle)
        {
            return false;
        }

        // #433359 - fix by Randy Waki 12 Mar 01 - Empty iframe is trimmed
        if (element.tag == this.configuration.tt.tagIframe)
        {
            return false;
        }

        if (element.attributes != null
            && (element.getAttrByName("id") != null || element.getAttrByName("name") != null))
        {
            return false;
        }

        return true;
    }

    /**
     * duplicate name attribute as an id and check if id and name match.
     * @param node Node to check for name/it attributes
     */
    public void fixId(Node node)
    {
        AttVal name = node.getAttrByName("name");
        AttVal id = node.getAttrByName("id");

        if (name != null)
        {
            if (id != null)
            {
                if (!id.value.equals(name.value))
                {
                    report.attrError(this, node, name, Report.ID_NAME_MISMATCH);
                }
            }
            else if (this.configuration.xmlOut)
            {
                node.addAttribute("id", name.value);
            }
        }
    }

    /**
     * defer duplicates when entering a table or other element where the inlines shouldn't be duplicated.
     */
    public void deferDup()
    {
        this.insert = -1;
        this.inode = null;
    }

    private static void mapStr(String str, short code)
    {
        int j;

        for (int i = 0; i < str.length(); i++)
        {
            j = str.charAt(i);
            lexmap[j] |= code;
        }
    }

    static
    {
        mapStr("\r\n\f", (short) (NEWLINE | WHITE));
        mapStr(" \t", WHITE);
        mapStr("-.:_", NAMECHAR);
        mapStr("0123456789", (short) (DIGIT | NAMECHAR));
        mapStr("abcdefghijklmnopqrstuvwxyz", (short) (LOWERCASE | LETTER | NAMECHAR));
        mapStr("ABCDEFGHIJKLMNOPQRSTUVWXYZ", (short) (UPPERCASE | LETTER | NAMECHAR));
    }

    private static short map(char c)
    {
        return (c < 128 ? lexmap[c] : 0);
    }

    /**
     * Everything is allowed in proprietary version of HTML this is handled here rather than in the tag/attr dicts.
     */
    void constrainVersion(int vers)
    {
        this.versions &= (vers | Dict.VERS_PROPRIETARY);
    }

    /**
     * Determines if the specified character is whitespace.
     * @param c char
     * @return <code>true</code> if char is whitespace.
     */
    private static boolean isWhite(char c)
    {
        short m = map(c);

        return (m & WHITE) != 0;
    }

    protected static boolean isDigit(char c)
    {
        short m;

        m = map(c);

        return (m & DIGIT) != 0;
    }

    protected static boolean isLetter(char c)
    {
        short m;

        m = map(c);

        return (m & LETTER) != 0;
    }

    protected static boolean isNamechar(char c)
    {
        short map = map(c);

        return (map & NAMECHAR) != 0;
    }

    /**
     * Determines if the specified character is a lowercase character.
     * @param c char
     * @return <code>true</code> if char is lower case.
     */
    private static boolean isLower(char c)
    {
        short map = map(c);

        return (map & LOWERCASE) != 0;
    }

    /**
     * Determines if the specified character is a uppercase character.
     * @param c char
     * @return <code>true</code> if char is upper case.
     */
    private static boolean isUpper(char c)
    {
        short map = map(c);

        return (map & UPPERCASE) != 0;
    }

    /**
     * Maps the given character to its lowercase equivalent.
     * @param c char
     * @return lowercase char.
     */
    private static char toLower(char c)
    {
        short m = map(c);

        if ((m & UPPERCASE) != 0)
        {
            c = (char) (c + 'a' - 'A');
        }

        return c;
    }

    /**
     * Maps the given character to its uppercase equivalent.
     * @param c char
     * @return uppercase char.
     */
    private static char toUpper(char c)
    {
        short m = map(c);

        if ((m & LOWERCASE) != 0)
        {
            c = (char) (c + 'A' - 'a');
        }

        return c;
    }

    public static char foldCase(char c, boolean tocaps, boolean xmlTags)
    {

        if (!xmlTags)
        {

            if (tocaps)
            {
                if (isLower(c))
                {
                    c = toUpper(c);
                }
            }
            else
            {
                // force to lower case
                if (isUpper(c))
                {
                    c = toLower(c);
                }
            }
        }

        return c;
    }

    /**
     * Return last char in string. This is useful when trailing quotemark is missing on an attribute
     */
    static int lastChar(String str)
    {
        if (str != null && str.length() > 0)
        {
            return str.charAt(str.length() - 1);
        }
        return 0;
    }

    /**
     * acceptable content for pre elements.
     */
    protected boolean preContent(Node node)
    {
        // p is coerced to br's
        if (node.tag == this.configuration.tt.tagP)
        {
            return true;
        }

        if (node.tag == null
            || node.tag == this.configuration.tt.tagP
            || (node.tag.model & (Dict.CM_INLINE | Dict.CM_NEW)) == 0)
        {
            return false;
        }
        return true;
    }

    /**
     * document type.
     */
    private static class W3CVersionInfo
    {

        /**
         * name.
         */
        String name;

        /**
         * voyager name.
         */
        String voyagerName;

        /**
         * profile.
         */
        String profile;

        /**
         * code.
         */
        short code;

        public W3CVersionInfo(String name, String voyagerName, String profile, short code)
        {
            this.name = name;
            this.voyagerName = voyagerName;
            this.profile = profile;
            this.code = code;
        }
    }

}