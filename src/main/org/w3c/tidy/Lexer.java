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

import java.io.PrintWriter;
import java.util.Stack;
import java.util.Vector;

/**
 * Lexer for html parser.
 * <p>
 * Given a file stream fp it returns a sequence of tokens. GetToken(fp) gets the next token UngetToken(fp) provides one
 * level undo The tags include an attribute list: - linked list of attribute/value nodes - each node has 2
 * null-terminated strings. - entities are replaced in attribute values white space is compacted if not in preformatted
 * mode If not in preformatted mode then leading white space is discarded and subsequent white space sequences
 * compacted to single space chars. If XmlTags is no then Tag names are folded to upper case and attribute names to
 * lower case. Not yet done: - Doctype subset and marked sections
 * </p>
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public class Lexer
{

    public static final short IgnoreWhitespace = 0;
    public static final short MixedContent = 1;
    public static final short Preformatted = 2;
    public static final short IgnoreMarkup = 3;

    /* the 3 URIs for the XHTML 1.0 DTDs */
    private static final String voyager_loose = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
    private static final String voyager_strict = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
    private static final String voyager_frameset = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd";

    private static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    private static Lexer.W3CVersionInfo[] W3CVersion =
        {
            new W3CVersionInfo("HTML 4.01", "XHTML 1.0 Strict", voyager_strict, Dict.VERS_HTML40_STRICT),
            new W3CVersionInfo(
                "HTML 4.01 Transitional",
                "XHTML 1.0 Transitional",
                voyager_loose,
                Dict.VERS_HTML40_LOOSE),
            new W3CVersionInfo("HTML 4.01 Frameset", "XHTML 1.0 Frameset", voyager_frameset, Dict.VERS_FRAMES),
            new W3CVersionInfo("HTML 4.0", "XHTML 1.0 Strict", voyager_strict, Dict.VERS_HTML40_STRICT),
            new W3CVersionInfo(
                "HTML 4.0 Transitional",
                "XHTML 1.0 Transitional",
                voyager_loose,
                Dict.VERS_HTML40_LOOSE),
            new W3CVersionInfo("HTML 4.0 Frameset", "XHTML 1.0 Frameset", voyager_frameset, Dict.VERS_FRAMES),
            new W3CVersionInfo("HTML 3.2", "XHTML 1.0 Transitional", voyager_loose, Dict.VERS_HTML32),
            new W3CVersionInfo("HTML 2.0", "XHTML 1.0 Strict", voyager_strict, Dict.VERS_HTML20)};

    /* lexer char types */
    private static final short DIGIT = 1;
    private static final short LETTER = 2;
    private static final short NAMECHAR = 4;
    private static final short WHITE = 8;
    private static final short NEWLINE = 16;
    private static final short LOWERCASE = 32;
    private static final short UPPERCASE = 64;

    /* lexer GetToken states */

    private static final short LEX_CONTENT = 0;
    private static final short LEX_GT = 1;
    private static final short LEX_ENDTAG = 2;
    private static final short LEX_STARTTAG = 3;
    private static final short LEX_COMMENT = 4;
    private static final short LEX_DOCTYPE = 5;
    private static final short LEX_PROCINSTR = 6;
    private static final short LEX_ENDCOMMENT = 7;
    private static final short LEX_CDATA = 8;
    private static final short LEX_SECTION = 9;
    private static final short LEX_ASP = 10;
    private static final short LEX_JSTE = 11;
    private static final short LEX_PHP = 12;

    /**
     * used to classify chars for lexical purposes.
     */
    private static short[] lexmap = new short[128];

    /**
     * file stream.
     */
    public StreamIn in;

    /**
     * error output stream.
     */
    public PrintWriter errout;

    /**
     * for accessibility errors.
     */
    public short badAccess;

    /**
     * for bad style errors.
     */
    public short badLayout;

    /**
     * for bad char encodings.
     */
    public short badChars;

    /**
     * for mismatched/mispositioned form tags.
     */
    public short badForm;

    /**
     * count of warnings in this document.
     */
    public short warnings;

    /**
     * count of errors.
     */
    public short errors;

    /**
     * lines seen.
     */
    public int lines;

    /**
     * at start of current token.
     */
    public int columns;

    /**
     * used to collapse contiguous white space.
     */
    public boolean waswhite;

    /**
     * true after token has been pushed back.
     */
    public boolean pushed;

    /**
     * when space is moved after end tag.
     */
    public boolean insertspace;

    /**
     * Netscape compatibility.
     */
    public boolean excludeBlocks;

    /**
     * true if moved out of table.
     */
    public boolean exiled;

    /**
     * true if xmlns attribute on html element.
     */
    public boolean isvoyager;

    /**
     * bit vector of HTML versions.
     */
    public short versions;

    /**
     * version as given by doctype (if any).
     */
    public int doctype;

    /**
     * e.g. if html or PUBLIC is missing.
     */
    public boolean badDoctype;

    /**
     * start of current node.
     */
    public int txtstart;

    /**
     * end of current node.
     */
    public int txtend;

    /**
     * state of lexer's finite state machine.
     */
    public short state;

    public Node token;

    /**
     * lexer character buffer parse tree nodes span onto this buffer which contains the concatenated text contents of
     * all of the elements. lexsize must be reset for each file. Byte buffer of UTF-8 chars.
     */
    public byte[] lexbuf;

    /**
     * allocated.
     */
    public int lexlength;

    /**
     * used.
     */
    public int lexsize;

    /**
     * Inline stack for compatibility with Mosaic. For deferring text node.
     */
    public Node inode;

    /**
     * for inferring inline tags.
     */
    public int insert;

    public Stack istack;

    /**
     * start of frame.
     */
    public int istackbase;

    /**
     * used for cleaning up presentation markup.
     */
    public Style styles;

    public Configuration configuration;
    protected int seenBodyEndTag; /* used by parser */

    private Vector nodeList;

    public Lexer(StreamIn in, Configuration configuration)
    {
        this.in = in;
        this.lines = 1;
        this.columns = 1;
        this.state = LEX_CONTENT;
        this.badAccess = 0;
        this.badLayout = 0;
        this.badChars = 0;
        this.badForm = 0;
        this.warnings = 0;
        this.errors = 0;
        this.waswhite = false;
        this.pushed = false;
        this.insertspace = false;
        this.exiled = false;
        this.isvoyager = false;
        this.versions = Dict.VERS_EVERYTHING;
        this.doctype = Dict.VERS_UNKNOWN;
        this.badDoctype = false;
        this.txtstart = 0;
        this.txtend = 0;
        this.token = null;
        this.lexbuf = null;
        this.lexlength = 0;
        this.lexsize = 0;
        this.inode = null;
        this.insert = -1;
        this.istack = new Stack();
        this.istackbase = 0;
        this.styles = null;
        this.configuration = configuration;
        this.seenBodyEndTag = 0;
        this.nodeList = new Vector();
    }

    public Node newNode()
    {
        Node node = new Node();
        this.nodeList.addElement(node);
        return node;
    }

    public Node newNode(short type, byte[] textarray, int start, int end)
    {
        Node node = new Node(type, textarray, start, end);
        this.nodeList.addElement(node);
        return node;
    }

    public Node newNode(short type, byte[] textarray, int start, int end, String element)
    {
        Node node = new Node(type, textarray, start, end, element, this.configuration.tt);
        this.nodeList.addElement(node);
        return node;
    }

    public Node cloneNode(Node node)
    {
        Node cnode = (Node) node.clone();
        this.nodeList.addElement(cnode);
        for (AttVal att = cnode.attributes; att != null; att = att.next)
        {
            if (att.asp != null)
            {
                this.nodeList.addElement(att.asp);
            }
            if (att.php != null)
            {
                this.nodeList.addElement(att.php);
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
                this.nodeList.addElement(att.asp);
            }
            if (att.php != null)
            {
                this.nodeList.addElement(att.php);
            }
        }
        return cattrs;
    }

    protected void updateNodeTextArrays(byte[] oldtextarray, byte[] newtextarray)
    {
        Node node;
        for (int i = 0; i < this.nodeList.size(); i++)
        {
            node = (Node) (this.nodeList.elementAt(i));
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
        this.lexbuf[this.lexsize] = (byte) '\0'; /* debug */
    }

    public void changeChar(byte c)
    {
        if (this.lexsize > 0)
        {
            this.lexbuf[this.lexsize - 1] = c;
        }
    }

    /**
     * store char c as UTF-8 encoded byte stream
     */
    public void addCharToLexer(int c)
    {
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
     * No longer attempts to insert missing ';' for unknown enitities unless one was present already, since this gives
     * unexpected results. For example: &lt;a href="something.htm?foo&bar&fred"> was tidied to: &lt;a
     * href="something.htm?foo&amp;bar;&amp;fred;"> rather than: &lt;a href="something.htm?foo&amp;bar&amp;fred"> My
     * thanks for Maurice Buxton for spotting this.
     */
    public void parseEntity(short mode)
    {
        short map;
        int start;
        boolean first = true;
        boolean semicolon = false;
        boolean numeric = false;
        int c, ch, startcol;
        String str;

        start = this.lexsize - 1; /* to start at "&" */
        startcol = this.in.curcol - 1;

        while (true)
        {
            c = this.in.readChar();
            if (c == StreamIn.EndOfStream)
            {
                break;
            }
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
            map = MAP((char) c);

            // AQ: Added flag for numeric entities so that numeric entities with missing semi-colons are recognized.
            // Eg. "&#114e&#112;..." is recognized as "rep"
            if (numeric && ((c == 'x') || ((map & DIGIT) != 0)))
            {
                addCharToLexer(c);
                continue;
            }
            if (!numeric && ((map & NAMECHAR) != 0))
            {
                addCharToLexer(c);
                continue;
            }

            // otherwise put it back

            this.in.ungetChar(c);
            break;
        }

        str = getString(this.lexbuf, start, this.lexsize - start);
        ch = EntityTable.getDefaultEntityTable().entityCode(str);

        // deal with unrecognized entities
        if (ch <= 0)
        {
            // set error position just before offending chararcter
            this.lines = this.in.curline;
            this.columns = startcol;

            if (this.lexsize > start + 1)
            {
                Report.entityError(this, Report.UNKNOWN_ENTITY, str, ch);

                if (semicolon)
                {
                    addCharToLexer(';');
                }
            }
            else
            {
                // naked &
                Report.entityError(this, Report.UNESCAPED_AMPERSAND, str, ch);
            }
        }
        else
        {
            // issue warning if not terminated by ';'
            if (c != ';')
            {
                // set error position just before offending chararcter
                this.lines = this.in.curline;
                this.columns = startcol;
                Report.entityError(this, Report.MISSING_SEMICOLON, str, c);
            }

            this.lexsize = start;

            if (ch == 160 && (mode & Preformatted) != 0)
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
        short map;
        int c;

        /* fold case of first char in buffer */

        c = this.lexbuf[this.txtstart];
        map = MAP((char) c);

        if (!this.configuration.xmlTags && (map & UPPERCASE) != 0)
        {
            c += ('a' - 'A');
            this.lexbuf[this.txtstart] = (byte) c;
        }

        while (true)
        {
            c = this.in.readChar();
            if (c == StreamIn.EndOfStream)
            {
                break;
            }
            map = MAP((char) c);

            if ((map & NAMECHAR) == 0)
            {
                break;
            }

            // fold case of subsequent chars
            if (!this.configuration.xmlTags && (map & UPPERCASE) != 0)
            {
                c += ('a' - 'A');
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
     * choose what version to use for new doctype
     * @param root
     * @return
     */
    public short HTMLVersion()
    {
        short versions;

        versions = this.versions;

        if ((versions & Dict.VERS_HTML20) != 0)
        {
            return Dict.VERS_HTML20;
        }

        if ((versions & Dict.VERS_HTML32) != 0)
        {
            return Dict.VERS_HTML32;
        }

        if ((versions & Dict.VERS_HTML40_STRICT) != 0)
        {
            return Dict.VERS_HTML40_STRICT;
        }

        if ((versions & Dict.VERS_HTML40_LOOSE) != 0)
        {
            return Dict.VERS_HTML40_LOOSE;
        }

        if ((versions & Dict.VERS_FRAMES) != 0)
        {
            return Dict.VERS_FRAMES;
        }

        return Dict.VERS_UNKNOWN;
    }

    public String HTMLVersionName()
    {
        short guessed;
        int j;

        guessed = apparentVersion();

        for (j = 0; j < W3CVersion.length; ++j)
        {
            if (guessed == W3CVersion[j].code)
            {
                if (this.isvoyager)
                {
                    return W3CVersion[j].voyagerName;
                }

                return W3CVersion[j].name;
            }
        }

        return null;
    }

    /* add meta element for Tidy */
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

                    if (attval != null && attval.value != null && Lexer.wstrcasecmp(attval.value, "generator") == 0)
                    {
                        attval = node.getAttrByName("content");

                        if (attval != null
                            && attval.value != null
                            && attval.value.length() >= 9
                            && Lexer.wstrcasecmp(attval.value.substring(0, 9), "HTML Tidy") == 0)
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

    /* return true if substring s is in p and isn't all in upper case */
    /* this is used to check the case of SYSTEM, PUBLIC, DTD and EN */
    /* len is how many chars to check in p */
    private static boolean findBadSubString(String s, String p, int len)
    {
        int n = s.length();
        int i = 0;
        String ps;

        while (n < len)
        {
            ps = p.substring(i, i + n);
            if (wstrcasecmp(s, ps) == 0)
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

        return !(
            findBadSubString("SYSTEM", s, len)
                || findBadSubString("PUBLIC", s, len)
                || findBadSubString("//DTD", s, len)
                || findBadSubString("//W3C", s, len)
                || findBadSubString("//EN", s, len));
    }

    /* examine <!DOCTYPE> to identify version */
    public short findGivenVersion(Node doctype)
    {
        String p, s;
        int i, j;
        int len;
        String str1;
        String str2;

        /* if root tag for doctype isn't html give up now */
        str1 = getString(this.lexbuf, doctype.start, 5);
        if (wstrcasecmp(str1, "html ") != 0)
        {
            return 0;
        }

        if (!checkDocTypeKeyWords(doctype))
        {
            Report.warning(this, doctype, null, Report.DTYPE_NOT_UPPER_CASE);
        }

        /* give up if all we are given is the system id for the doctype */
        str1 = getString(this.lexbuf, doctype.start + 5, 7);
        if (wstrcasecmp(str1, "SYSTEM ") == 0)
        {
            /* but at least ensure the case is correct */
            if (!str1.substring(0, 6).equals("SYSTEM"))
            {
                System.arraycopy(getBytes("SYSTEM"), 0, this.lexbuf, doctype.start + 5, 6);
            }
            return 0; /* unrecognized */
        }

        if (wstrcasecmp(str1, "PUBLIC ") == 0)
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
                    /* compute length of identifier e.g. "HTML 4.0 Transitional" */
                    for (j = i + 13; j < doctype.end && this.lexbuf[j] != (byte) '/'; ++j);
                    len = j - i - 13;
                    p = getString(this.lexbuf, i + 13, len);

                    for (j = 1; j < W3CVersion.length; ++j)
                    {
                        s = W3CVersion[j].name;
                        if (len == s.length() && s.equals(p))
                        {
                            return W3CVersion[j].code;
                        }
                    }

                    /* else unrecognized version */
                }
                else if (str2.equals("-//IETF//DTD "))
                {
                    /* compute length of identifier e.g. "HTML 2.0" */
                    for (j = i + 14; j < doctype.end && this.lexbuf[j] != (byte) '/'; ++j);
                    len = j - i - 14;

                    p = getString(this.lexbuf, i + 14, len);
                    s = W3CVersion[0].name;
                    if (len == s.length() && s.equals(p))
                    {
                        return W3CVersion[0].code;
                    }

                    /* else unrecognized version */
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

        for (node = root.content; node != null && node.tag != this.configuration.tt.tagHtml; node = node.next);

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
                    Report.warning(this, node, null, Report.INCONSISTENT_NAMESPACE);
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

    public boolean setXHTMLDocType(Node root)
    {
        String fpi = " ";
        String sysid = "";
        String namespace = XHTML_NAMESPACE;
        Node doctype;

        doctype = root.findDocType();

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
            /* see what flavor of XHTML this document matches */
            if ((this.versions & Dict.VERS_HTML40_STRICT) != 0)
            { /* use XHTML strict */
                fpi = "-//W3C//DTD XHTML 1.0 Strict//EN";
                sysid = voyager_strict;
            }
            else if ((this.versions & Dict.VERS_LOOSE) != 0)
            {
                fpi = "-//W3C//DTD XHTML 1.0 Transitional//EN";
                sysid = voyager_loose;
            }
            else if ((this.versions & Dict.VERS_FRAMES) != 0)
            { /* use XHTML frames */
                fpi = "-//W3C//DTD XHTML 1.0 Frameset//EN";
                sysid = voyager_frameset;
            }
            else
            {
                // lets assume XHTML transitional
                fpi = "-//W3C//DTD XHTML 1.0 Transitional//EN";
                sysid = voyager_loose;
            }
        }
        else if (this.configuration.docTypeMode == Configuration.DOCTYPE_STRICT)
        {
            fpi = "-//W3C//DTD XHTML 1.0 Strict//EN";
            sysid = voyager_strict;
        }
        else if (this.configuration.docTypeMode == Configuration.DOCTYPE_LOOSE)
        {
            fpi = "-//W3C//DTD XHTML 1.0 Transitional//EN";
            sysid = voyager_loose;
        }

        fixHTMLNameSpace(root, namespace);

        if (doctype == null)
        {
            doctype = newNode(Node.DocTypeTag, this.lexbuf, 0, 0);
            doctype.next = root.content;
            doctype.parent = root;
            doctype.prev = null;
            root.content = doctype;
        }

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_USER && this.configuration.docTypeStr != null)
        {
            fpi = this.configuration.docTypeStr;
            sysid = "";
        }

        this.txtstart = this.lexsize;
        this.txtend = this.lexsize;

        /* add public identifier */
        addStringLiteral("html PUBLIC ");

        /* check if the fpi is quoted or not */
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
            addStringLiteral("\n    \"");
        }

        /* add system identifier */
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
                return HTMLVersion();

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

                break; /* to replace old version by new */

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

                break; /* to replace old version by new */

            case Dict.VERS_FRAMES :
                if ((this.versions & Dict.VERS_FRAMES) != 0)
                {
                    return Dict.VERS_FRAMES;
                }

                break;
        }

        Report.warning(this, null, null, Report.INCONSISTENT_VERSION);
        return this.HTMLVersion();
    }

    /* fixup doctype if missing */
    public boolean fixDocType(Node root)
    {
        Node doctype;
        int guessed = Dict.VERS_HTML40_STRICT, i;

        if (this.badDoctype)
        {
            Report.warning(this, null, null, Report.MALFORMED_DOCTYPE);
        }

        if (this.configuration.xmlOut)
        {
            return true;
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

                        break; /* to replace old version by new */

                    case Dict.VERS_HTML32 :
                        if ((this.versions & Dict.VERS_HTML32) != 0)
                        {
                            return true;
                        }

                        break; /* to replace old version by new */

                    case Dict.VERS_HTML40_STRICT :
                        if ((this.versions & Dict.VERS_HTML40_STRICT) != 0)
                        {
                            return true;
                        }

                        break; /* to replace old version by new */

                    case Dict.VERS_HTML40_LOOSE :
                        if ((this.versions & Dict.VERS_HTML40_LOOSE) != 0)
                        {
                            return true;
                        }

                        break; /* to replace old version by new */

                    case Dict.VERS_FRAMES :
                        if ((this.versions & Dict.VERS_FRAMES) != 0)
                        {
                            return true;
                        }

                        break; /* to replace old version by new */
                }

                /* INCONSISTENT_VERSION warning is now issued by ApparentVersion() */
            }

            /* choose new doctype */
            guessed = HTMLVersion();
        }

        if (guessed == Dict.VERS_UNKNOWN)
        {
            return false;
        }

        /* for XML use the Voyager system identifier */
        if (this.configuration.xmlOut || this.configuration.xmlTags || this.isvoyager)
        {
            if (doctype != null)
            {
                Node.discardElement(doctype);
            }

            for (i = 0; i < W3CVersion.length; ++i)
            {
                if (guessed == W3CVersion[i].code)
                {
                    fixHTMLNameSpace(root, W3CVersion[i].profile);
                    break;
                }
            }

            return true;
        }

        if (doctype == null)
        {
            doctype = newNode(Node.DocTypeTag, this.lexbuf, 0, 0);
            doctype.next = root.content;
            doctype.parent = root;
            doctype.prev = null;
            root.content = doctype;
        }

        this.txtstart = this.lexsize;
        this.txtend = this.lexsize;

        /* use the appropriate public identifier */
        addStringLiteral("html PUBLIC ");

        if (this.configuration.docTypeMode == Configuration.DOCTYPE_USER && this.configuration.docTypeStr != null)
        {
            addStringLiteral(this.configuration.docTypeStr);
        }
        else if (guessed == Dict.VERS_HTML20)
        {
            addStringLiteral("\"-//IETF//DTD HTML 2.0//EN\"");
        }
        else
        {
            addStringLiteral("\"-//W3C//DTD ");

            for (i = 0; i < W3CVersion.length; ++i)
            {
                if (guessed == W3CVersion[i].code)
                {
                    addStringLiteral(W3CVersion[i].name);
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

    /* ensure XML document starts with <?XML version="1.0"?> */
    public boolean fixXMLPI(Node root)
    {
        Node xml;
        int s;

        if (root.content != null && root.content.type == Node.ProcInsTag)
        {
            s = root.content.start;

            if (this.lexbuf[s] == (byte) 'x' && this.lexbuf[s + 1] == (byte) 'm' && this.lexbuf[s + 2] == (byte) 'l')
            {
                return true;
            }
        }

        xml = newNode(Node.ProcInsTag, this.lexbuf, 0, 0);
        xml.next = root.content;

        if (root.content != null)
        {
            root.content.prev = xml;
            xml.next = root.content;
        }

        root.content = xml;

        this.txtstart = this.lexsize;
        this.txtend = this.lexsize;
        addStringLiteral("xml version=\"1.0\"");
        if (this.configuration.charEncoding == Configuration.LATIN1)
        {
            addStringLiteral(" encoding=\"ISO-8859-1\"");
        }
        this.txtend = this.lexsize;

        xml.start = this.txtstart;
        xml.end = this.txtend;
        return false;
    }

    public Node inferredTag(String name)
    {
        Node node;

        node = newNode(Node.StartTag, this.lexbuf, this.txtstart, this.txtend, name);
        node.implicit = true;
        return node;
    }

    public static boolean expectsContent(Node node)
    {
        if (node.type != Node.StartTag)
        {
            return false;
        }

        /* unknown element? */
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

    /*
     * create a text node for the contents of a CDATA element like style or script which ends with </foo> for some foo.
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
            /* treat \r\n as \n and \r as \n */

            if (c == '/' && lastc == '<')
            {
                if (endtag)
                {
                    this.lines = this.in.curline;
                    this.columns = this.in.curcol - 3;

                    Report.warning(this, null, null, Report.BAD_CDATA_CONTENT);
                }

                start = this.lexsize + 1; /* to first letter */
                endtag = true;
            }
            else if (c == '>' && start >= 0)
            {
                len = this.lexsize - start;
                if (len == container.element.length())
                {
                    str = getString(this.lexbuf, start, len);
                    if (Lexer.wstrcasecmp(str, container.element) == 0)
                    {
                        this.txtend = start - 2;
                        break;
                    }
                }

                this.lines = this.in.curline;
                this.columns = this.in.curcol - 3;

                Report.warning(this, null, null, Report.BAD_CDATA_CONTENT);

                /* if javascript insert backslash before / */

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
            }
            else if (c == '\r')
            {
                c = this.in.readChar();

                if (c != '\n')
                {
                    this.in.ungetChar(c);
                }

                c = '\n';
            }

            addCharToLexer(c);
            this.txtend = this.lexsize;
            lastc = c;
        }

        if (c == StreamIn.EndOfStream)
        {
            Report.warning(this, container, null, Report.MISSING_ENDTAG_FOR);
        }

        if (this.txtend > this.txtstart)
        {
            this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
            return this.token;
        }

        return null;
    }

    public void ungetToken()
    {
        this.pushed = true;
    }

    /*
     * modes for GetToken() MixedContent -- for elements which don't accept PCDATA Preformatted -- white space
     * preserved as is IgnoreMarkup -- for CDATA elements such as script, style
     */

    public Node getToken(short mode)
    {
        short map;
        int c = 0;
        int badcomment = 0;
        MutableBoolean isempty = new MutableBoolean();
        AttVal attributes;

        if (this.pushed)
        {
            /* duplicate inlines in preference to pushed text nodes when appropriate */
            if (this.token.type != Node.TextNode || (this.insert == -1 && this.inode == null))
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
            
            // fix for [427846]
            // if (lexer->insertspace && !(mode & IgnoreWhitespace))
            if (this.insertspace && !((mode & IgnoreWhitespace) != 0))
            {
                addCharToLexer(' ');
                this.waswhite = true;
                this.insertspace = false;
            }

            /* treat \r\n as \n and \r as \n */

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
                case LEX_CONTENT : /* element content */
                    map = MAP((char) c);

                    /*
                     * Discard white space if appropriate. Its cheaper to do this here rather than in parser methods
                     * for elements that don't have mixed content.
                     */
                    if (((map & WHITE) != 0) && (mode == IgnoreWhitespace) && this.lexsize == this.txtstart + 1)
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

                    if ((map & WHITE) != 0)
                    {
                        /* was previous char white? */
                        if (this.waswhite)
                        {
                            if (mode != Preformatted && mode != IgnoreMarkup)
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

                            if (mode != Preformatted && mode != IgnoreMarkup && c != ' ')
                            {
                                changeChar((byte) ' ');
                            }
                        }

                        continue;
                    }
                    else if (c == '&' && mode != IgnoreMarkup)
                    {
                        parseEntity(mode);
                    }

                    /* this is needed to avoid trimming trailing whitespace */
                    if (mode == IgnoreWhitespace)
                    {
                        mode = MixedContent;
                    }

                    this.waswhite = false;
                    continue;

                case LEX_GT : /* <         */

                    /* check for endtag */
                    if (c == '/')
                    {
                        c = this.in.readChar();
                        if (c == StreamIn.EndOfStream)
                        {
                            this.in.ungetChar(c);
                            continue;
                        }

                        addCharToLexer(c);
                        map = MAP((char) c);

                        if ((map & LETTER) != 0)
                        {
                            this.lexsize -= 3;
                            this.txtend = this.lexsize;
                            this.in.ungetChar(c);
                            this.state = LEX_ENDTAG;
                            this.lexbuf[this.lexsize] = (byte) '\0'; /* debug */
                            this.in.curcol -= 2;

                            /* if some text before the </ return it now         */
                            if (this.txtend > this.txtstart)
                            {
                                /* trim space char before end tag */
                                if (mode == IgnoreWhitespace && this.lexbuf[this.lexsize - 1] == (byte) ' ')
                                {
                                    this.lexsize -= 1;
                                    this.txtend = this.lexsize;
                                }

                                this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                                return this.token;
                            }

                            continue; /* no text so keep going */
                        }

                        /* otherwise treat as CDATA */
                        this.waswhite = false;
                        this.state = LEX_CONTENT;
                        continue;
                    }

                    if (mode == IgnoreMarkup)
                    {
                        /* otherwise treat as CDATA */
                        this.waswhite = false;
                        this.state = LEX_CONTENT;
                        continue;
                    }

                    /*
                     * look out for comments, doctype or marked sections this isn't quite right, but its getting there
                     * ...
                     */
                    if (c == '!')
                    {
                        c = this.in.readChar();

                        if (c == '-')
                        {
                            c = this.in.readChar();

                            if (c == '-')
                            {
                                this.state = LEX_COMMENT; /* comment */
                                this.lexsize -= 2;
                                this.txtend = this.lexsize;

                                /* if some text before < return it now         */
                                if (this.txtend > this.txtstart)
                                {
                                    this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                                    return this.token;
                                }

                                this.txtstart = this.lexsize;
                                continue;
                            }

                            Report.warning(this, null, null, Report.MALFORMED_COMMENT);
                        }
                        else if (c == 'd' || c == 'D')
                        {
                            this.state = LEX_DOCTYPE; /* doctype */
                            this.lexsize -= 2;
                            this.txtend = this.lexsize;
                            mode = IgnoreWhitespace;

                            /* skip until white space or '>' */

                            for (;;)
                            {
                                c = this.in.readChar();

                                if (c == StreamIn.EndOfStream || c == '>')
                                {
                                    this.in.ungetChar(c);
                                    break;
                                }

                                map = MAP((char) c);

                                if ((map & WHITE) == 0)
                                {
                                    continue;
                                }

                                /* and skip to end of whitespace */

                                for (;;)
                                {
                                    c = this.in.readChar();

                                    if (c == StreamIn.EndOfStream || c == '>')
                                    {
                                        this.in.ungetChar(c);
                                        break;
                                    }

                                    map = MAP((char) c);

                                    if ((map & WHITE) != 0)
                                    {
                                        continue;
                                    }

                                    this.in.ungetChar(c);
                                    break;
                                }

                                break;
                            }

                            /* if some text before < return it now         */
                            if (this.txtend > this.txtstart)
                            {
                                this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                                return this.token;
                            }

                            this.txtstart = this.lexsize;
                            continue;
                        }
                        else if (c == '[')
                        {
                            /* Word 2000 embeds <![if ...]> ... <![endif]> sequences */
                            this.lexsize -= 2;
                            this.state = LEX_SECTION;
                            this.txtend = this.lexsize;

                            /* if some text before < return it now         */
                            if (this.txtend > this.txtstart)
                            {
                                this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                                return this.token;
                            }

                            this.txtstart = this.lexsize;
                            continue;
                        }

                        /* otherwise swallow chars up to and including next '>' */
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

                    /*
                     * processing instructions
                     */

                    if (c == '?')
                    {
                        this.lexsize -= 2;
                        this.state = LEX_PROCINSTR;
                        this.txtend = this.lexsize;

                        /* if some text before < return it now         */
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        this.txtstart = this.lexsize;
                        continue;
                    }

                    /* Microsoft ASP's e.g. <% ... server-code ... %> */
                    if (c == '%')
                    {
                        this.lexsize -= 2;
                        this.state = LEX_ASP;
                        this.txtend = this.lexsize;

                        /* if some text before < return it now         */
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        this.txtstart = this.lexsize;
                        continue;
                    }

                    /* Netscapes JSTE e.g. <# ... server-code ... #> */
                    if (c == '#')
                    {
                        this.lexsize -= 2;
                        this.state = LEX_JSTE;
                        this.txtend = this.lexsize;

                        /* if some text before < return it now         */
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        this.txtstart = this.lexsize;
                        continue;
                    }

                    map = MAP((char) c);

                    /* check for start tag */
                    if ((map & LETTER) != 0)
                    {
                        this.in.ungetChar(c); /* push back letter */
                        this.lexsize -= 2; /* discard " <" + letter         */
                        this.txtend = this.lexsize;
                        this.state = LEX_STARTTAG; /* ready to read tag name */

                        /* if some text before < return it now         */
                        if (this.txtend > this.txtstart)
                        {
                            this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                            return this.token;
                        }

                        continue; /* no text so keep going */
                    }

                    /* otherwise treat as CDATA */
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    continue;

                case LEX_ENDTAG : /* </letter         */
                    this.txtstart = this.lexsize - 1;
                    this.in.curcol += 2;
                    c = parseTagName();
                    this.token = newNode(Node.EndTag, /* create endtag token */
                    this.lexbuf,
                        this.txtstart,
                        this.txtend,
                        getString(this.lexbuf, this.txtstart, this.txtend - this.txtstart));
                    this.lexsize = this.txtstart;
                    this.txtend = this.txtstart;

                    /* skip to '>' */
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
                    return this.token; /* the endtag token */

                case LEX_STARTTAG : /* first letter of tagname */
                    this.txtstart = this.lexsize - 1; /* set txtstart to first letter */
                    c = parseTagName();
                    isempty.value = false;
                    attributes = null;
                    this.token =
                        newNode(
                            (isempty.value ? Node.StartEndTag : Node.StartTag),
                            this.lexbuf,
                            this.txtstart,
                            this.txtend,
                            getString(this.lexbuf, this.txtstart, this.txtend - this.txtstart));

                    /* parse attributes, consuming closing ">" */
                    if (c != '>')
                    {
                        if (c == '/')
                        {
                            this.in.ungetChar(c);
                        }

                        attributes = parseAttrs(isempty);
                    }

                    if (isempty.value)
                    {
                        this.token.type = Node.StartEndTag;
                    }

                    this.token.attributes = attributes;
                    this.lexsize = this.txtstart;
                    this.txtend = this.txtstart;

                    /* swallow newline following start tag */
                    /* special check needed for CRLF sequence */
                    /* this doesn't apply to empty elements */

                    if (expectsContent(this.token) || this.token.tag == this.configuration.tt.tagBr)
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

                        this.waswhite = true; /* to swallow leading whitespace */
                    }
                    else
                    {
                        this.waswhite = false;
                    }

                    this.state = LEX_CONTENT;

                    if (this.token.tag == null)
                    {
                        Report.error(this, null, this.token, Report.UNKNOWN_ELEMENT);
                    }
                    else if (!this.configuration.xmlTags)
                    {
                        this.versions &= this.token.tag.versions;

                        if ((this.token.tag.versions & Dict.VERS_PROPRIETARY) != 0)
                        {
                            if (!this.configuration.makeClean
                                && (this.token.tag == this.configuration.tt.tagNobr
                                    || this.token.tag == this.configuration.tt.tagWbr))
                            {
                                Report.warning(this, null, this.token, Report.PROPRIETARY_ELEMENT);
                            }
                        }

                        if (this.token.tag.chkattrs != null)
                        {
                            this.token.checkUniqueAttributes(this);
                            this.token.tag.chkattrs.check(this, this.token);
                        }
                        else
                        {
                            this.token.checkAttributes(this);
                        }
                    }

                    return this.token; /* return start tag */

                case LEX_COMMENT : /* seen <!-- so look for --> */

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
                                Report.warning(this, null, null, Report.MALFORMED_COMMENT);
                            }

                            this.txtend = this.lexsize - 2; // AQ 8Jul2000
                            this.lexbuf[this.lexsize] = (byte) '\0';
                            this.state = LEX_CONTENT;
                            this.waswhite = false;
                            this.token = newNode(Node.CommentTag, this.lexbuf, this.txtstart, this.txtend);

                            /* now look for a line break */

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

                        /* note position of first such error in the comment */
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

                        /* if '-' then look for '>' to end the comment */
                        if (c != '-')
                        {
                            break end_comment;
                        }

                    }
                    /* otherwise continue to look for --> */
                    this.lexbuf[this.lexsize - 2] = (byte) '=';
                    continue;

                case LEX_DOCTYPE : /* seen <!d so look for '> ' munging whitespace */
                    map = MAP((char) c);

                    if ((map & WHITE) != 0)
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

                    if (c != '>')
                    {
                        continue;
                    }

                    this.lexsize -= 1;
                    this.txtend = this.lexsize;
                    this.lexbuf[this.lexsize] = (byte) '\0';
                    this.state = LEX_CONTENT;
                    this.waswhite = false;
                    this.token = newNode(Node.DocTypeTag, this.lexbuf, this.txtstart, this.txtend);
                    /* make a note of the version named by the doctype */
                    this.doctype = findGivenVersion(this.token);
                    return this.token;

                case LEX_PROCINSTR : /* seen <? so look for '> ' */
                    /* check for PHP preprocessor instructions <?php ... ?> */

                    if (this.lexsize - this.txtstart == 3)
                    {
                        if ((getString(this.lexbuf, this.txtstart, 3)).equals("php"))
                        {
                            this.state = LEX_PHP;
                            continue;
                        }
                    }

                    if (this.configuration.xmlPIs) /* insist on ?> as terminator */
                    {
                        if (c != '?')
                        {
                            continue;
                        }

                        /* now look for '>' */
                        c = this.in.readChar();

                        if (c == StreamIn.EndOfStream)
                        {
                            Report.warning(this, null, null, Report.UNEXPECTED_END_OF_FILE);
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
                    this.token = newNode(Node.ProcInsTag, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_ASP : /* seen <% so look for "%> " */
                    if (c != '%')
                    {
                        continue;
                    }

                    /* now look for '>' */
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
                    this.token = newNode(Node.AspTag, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_JSTE : /* seen <# so look for "#> " */
                    if (c != '#')
                    {
                        continue;
                    }

                    /* now look for '>' */
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
                    this.token = newNode(Node.JsteTag, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_PHP : /* seen " <?php" so look for "?> " */
                    if (c != '?')
                    {
                        continue;
                    }

                    /* now look for '>' */
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
                    this.token = newNode(Node.PhpTag, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_SECTION : /* seen " <![" so look for "]> " */
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

                    /* now look for '>' */
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
                    this.token = newNode(Node.SectionTag, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;

                case LEX_CDATA : /* seen " <![CDATA[" so look for "]]> " */
                    if (c != ']')
                    {
                        continue;
                    }

                    /* now look for ']' */
                    c = this.in.readChar();

                    if (c != ']')
                    {
                        this.in.ungetChar(c);
                        continue;
                    }

                    /* now look for '>' */
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
                    this.token = newNode(Node.CDATATag, this.lexbuf, this.txtstart, this.txtend);
                    return this.token;
            }
        }

        if (this.state == LEX_CONTENT) /* text string */
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

                this.token = newNode(Node.TextNode, this.lexbuf, this.txtstart, this.txtend);
                return this.token;
            }
        }
        else if (this.state == LEX_COMMENT) /* comment */
        {
            if (c == StreamIn.EndOfStream)
            {
                Report.warning(this, null, null, Report.MALFORMED_COMMENT);
            }

            this.txtend = this.lexsize;
            this.lexbuf[this.lexsize] = (byte) '\0';
            this.state = LEX_CONTENT;
            this.waswhite = false;
            this.token = newNode(Node.CommentTag, this.lexbuf, this.txtstart, this.txtend);
            return this.token;
        }

        return null;
    }

    /*
     * parser for ASP within start tags Some people use ASP for to customize attributes Tidy isn't really well suited
     * to dealing with ASP This is a workaround for attributes, but won't deal with the case where the ASP is used to
     * tailor the attribute value. Here is an example of a work around for using ASP in attribute values: href="
     * <%=rsSchool.Fields("ID").Value%> " where the ASP that generates the attribute value is masked from Tidy by the
     * quotemarks.
     */

    public Node parseAsp()
    {
        int c;
        Node asp = null;

        this.txtstart = this.lexsize;

        for (;;)
        {
            c = this.in.readChar();
            addCharToLexer(c);

            if (c != '%')
            {
                continue;
            }

            c = this.in.readChar();
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
            asp = newNode(Node.AspTag, this.lexbuf, this.txtstart, this.txtend);
        }

        this.txtstart = this.txtend;
        return asp;
    }

    /*
     * PHP is like ASP but is based upon XML processing instructions, e.g. <?php ... ?>
     */
    public Node parsePhp()
    {
        int c;
        Node php = null;

        this.txtstart = this.lexsize;

        for (;;)
        {
            c = this.in.readChar();
            addCharToLexer(c);

            if (c != '?')
            {
                continue;
            }

            c = this.in.readChar();
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
            php = newNode(Node.PhpTag, this.lexbuf, this.txtstart, this.txtend);
        }

        this.txtstart = this.txtend;
        return php;
    }

    /* consumes the '>' terminating start tags */
    public String parseAttribute(MutableBoolean isempty, MutableObject asp, MutableObject php)
    {
        int start = 0;
        // int len = 0; Removed by BUGFIX for 126265
        short map;
        String attr;
        int c = 0;

        asp.setObject(null); /* clear asp pointer */
        php.setObject(null); /* clear php pointer */
        /* skip white space before the attribute */

        for (;;)
        {
            c = this.in.readChar();

            if (c == '/')
            {
                c = this.in.readChar();

                if (c == '>')
                {
                    isempty.value = true;
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
                Report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
                return null;
            }

            if (c == '"' || c == '\'')
            {
                Report.attrError(this, this.token, null, Report.UNEXPECTED_QUOTEMARK);
                continue;
            }

            if (c == StreamIn.EndOfStream)
            {
                Report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
                this.in.ungetChar(c);
                return null;
            }

            map = MAP((char) c);

            if ((map & WHITE) == 0)
            {
                break;
            }
        }

        start = this.lexsize;

        for (;;)
        {
            /* but push back '=' for parseValue() */
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

            map = MAP((char) c);

            if ((map & WHITE) != 0)
            {
                break;
            }

            /* what should be done about non-namechar characters? */
            /* currently these are incorporated into the attr name */

            if (!this.configuration.xmlTags && (map & UPPERCASE) != 0)
            {
                c += ('a' - 'A');
            }

            //  ++len; Removed by BUGFIX for 126265
            addCharToLexer(c);

            c = this.in.readChar();
        }

        // Following line added by GLP to fix BUG 126265. This is a temporary comment
        // and should be removed when Tidy is fixed.
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
        int c, map, delim = '"';
        boolean isrule = false;

        c = this.in.readChar();
        addCharToLexer(c);

        /* check for ASP, PHP or Tango */
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

            /* if not recognized as ASP, PHP or Tango */
            /* then also finish value on whitespace */
            if (!isrule)
            {
                map = MAP((char) c);

                if ((map & WHITE) != 0)
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

                    if (endOfInput()) /* #427840 - fix by Terry Teague 30 Jun 01 */
                    {
                        Report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
                        this.in.ungetChar(c);
                        return 0;
                    }
                    if (c == '>') /* #427840 - fix by Terry Teague 30 Jun 01 */
                    {
                        this.in.ungetChar(c);
                        Report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
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

                    if (endOfInput()) /* #427840 - fix by Terry Teague 30 Jun 01 */
                    {
                        Report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
                        this.in.ungetChar(c);
                        return 0;
                    }
                    if (c == '>') /* #427840 - fix by Terry Teague 30 Jun 01 */
                    {
                        this.in.ungetChar(c);
                        Report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
                        return 0;
                    }

                    addCharToLexer(c);
                }
                while (c != '\'');
            }
        }

        return delim;
    }

    /* values start with "=" or " = " etc. */
    /* doesn't consume the ">" at end of start tag */

    public String parseValue(String name, boolean foldCase, MutableBoolean isempty, MutableInteger pdelim)
    {
        int len = 0;
        int start;
        short map;
        boolean seen_gt = false;
        boolean munge = true;
        int c = 0;
        int lastc, delim, quotewarning;
        String value;

        delim = 0;
        pdelim.value = '"';

        /*
         * Henry Zrepa reports that some folk are using the embed element with script attributes where newlines are
         * significant and must be preserved
         */
        if (this.configuration.literalAttribs)
        {
            munge = false;
        }

        /* skip white space before the '=' */

        for (;;)
        {
            c = this.in.readChar();

            if (c == StreamIn.EndOfStream)
            {
                this.in.ungetChar(c);
                break;
            }

            map = MAP((char) c);

            if ((map & WHITE) == 0)
            {
                break;
            }
        }

        /*
         * c should be '=' if there is a value other legal possibilities are white space, '/' and '>'
         */

        if (c != '=')
        {
            this.in.ungetChar(c);
            return null;
        }

        /* skip white space after '=' */

        for (;;)
        {
            c = this.in.readChar();

            if (c == StreamIn.EndOfStream)
            {
                this.in.ungetChar(c);
                break;
            }

            map = MAP((char) c);

            if ((map & WHITE) == 0)
            {
                break;
            }
        }

        /* check for quote marks */

        if (c == '"' || c == '\'')
        {
            delim = c;
        }
        else if (c == '<')
        {
            start = this.lexsize;
            addCharToLexer(c);
            pdelim.value = parseServerInstruction();
            len = this.lexsize - start;
            this.lexsize = start;
            return (len > 0 ? getString(this.lexbuf, start, len) : null);
        }
        else
        {
            this.in.ungetChar(c);
        }

        /*
         * and read the value string check for quote mark if needed
         */

        quotewarning = 0;
        start = this.lexsize;
        c = '\0';

        for (;;)
        {
            lastc = c; /* track last character */
            c = this.in.readChar();

            if (c == StreamIn.EndOfStream)
            {
                Report.attrError(this, this.token, null, Report.UNEXPECTED_END_OF_FILE);
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
                    Report.attrError(this, this.token, null, Report.UNEXPECTED_QUOTEMARK);
                    break;
                }

                if (c == '<')
                {
                    /* this.in.ungetChar(c); */
                    Report.attrError(this, this.token, null, Report.UNEXPECTED_GT);
                    /* break; */
                }

                /*
                 * For cases like <br clear=all/> need to avoid treating /> as part of the attribute value, however
                 * care is needed to avoid so treating <a href=http://www.acme.com /> in this way, which would map the
                 * <a> tag to <a href="http://www.acme.com"/>
                 */
                if (c == '/')
                {
                    /* peek ahead in case of /> */
                    c = this.in.readChar();

                    if (c == '>' && !AttributeTable.getDefaultAttributeTable().isUrl(name))
                    {
                        isempty.value = true;
                        this.in.ungetChar(c);
                        break;
                    }

                    /* unget peeked char */
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

                /* treat CRLF, CR and LF as single line break */

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
                    seen_gt = true;
                }
            }

            if (c == '&')
            {
                addCharToLexer(c);
                parseEntity((short) 0);
                continue;
            }

            /*
             * kludge for JavaScript attribute values with line continuations in string literals
             */
            if (c == '\\')
            {
                c = this.in.readChar();

                if (c != '\n')
                {
                    this.in.ungetChar(c);
                    c = '\\';
                }
            }

            map = MAP((char) c);

            if ((map & WHITE) != 0)
            {
                if (delim == (char) 0)
                {
                    break;
                }

                if (munge)
                {
                    c = ' ';

                    if (lastc == ' ')
                    {
                        continue;
                    }
                }
            }
            else if (foldCase && (map & UPPERCASE) != 0)
            {
                c += ('a' - 'A');
            }

            addCharToLexer(c);
        }

        if (quotewarning > 10 && seen_gt && munge)
        {
            /*
             * there is almost certainly a missing trailling quote mark as we have see too many newlines, < or >
             * characters. an exception is made for Javascript attributes and the javascript URL scheme which may
             * legitimately include < and >
             */
            if (!AttributeTable.getDefaultAttributeTable().isScript(name)
                && !(AttributeTable.getDefaultAttributeTable().isUrl(name)
                    && (getString(this.lexbuf, start, 11)).equals("javascript:")))
            {
                Report.error(this, null, null, Report.SUSPECTED_MISSING_QUOTE);
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

        /* note delimiter if given */
        if (delim != 0)
        {
            pdelim.value = delim;
        }
        else
        {
            pdelim.value = '"';
        }

        return value;
    }

    /* attr must be non-null */
    public static boolean isValidAttrName(String attr)
    {
        short map;
        char c;
        int i;

        /* first character should be a letter */
        c = attr.charAt(0);
        map = MAP(c);

        if (!((map & LETTER) != 0))
        {
            return false;
        }

        /* remaining characters should be namechars */
        for (i = 1; i < attr.length(); i++)
        {
            c = attr.charAt(i);
            map = MAP(c);

            if ((map & NAMECHAR) != 0)
            {
                continue;
            }

            return false;
        }

        return true;
    }

    /* swallows closing '>' */

    public AttVal parseAttrs(MutableBoolean isempty)
    {
        AttVal av, list;
        String attribute, value;
        MutableInteger delim = new MutableInteger();
        MutableObject asp = new MutableObject();
        MutableObject php = new MutableObject();

        list = null;

        for (; !endOfInput();)
        {
            attribute = parseAttribute(isempty, asp, php);

            if (attribute == null)
            {
                /* check if attributes are created by ASP markup */
                if (asp.getObject() != null)
                {
                    av = new AttVal(list, null, (Node) asp.getObject(), null, '\0', null, null);
                    list = av;
                    continue;
                }

                /* check if attributes are created by PHP markup */
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
                av = new AttVal(list, null, null, null, delim.value, attribute, value);
                av.dict = AttributeTable.getDefaultAttributeTable().findAttribute(av);
                list = av;
            }
            else
            {
                av = new AttVal(null, null, null, null, 0, attribute, value);
                Report.attrError(this, this.token, value, Report.BAD_ATTRIBUTE_VALUE);
            }
        }

        return list;
    }

    /*
     * push a copy of an inline node onto stack but don't push if implicit or OBJECT or APPLET (implicit tags are ones
     * generated from the istack) One issue arises with pushing inlines when the tag is already pushed. For instance:
     * <p><em> text <p><em> more text Shouldn't be mapped to <p><em> text </em></p><p><em><em> more text </em>
     * </em>
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

    /* pop inline stack */
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

    /*
     * This has the effect of inserting "missing" inline elements around the contents of blocklevel elements such as P,
     * TD, TH, DIV, PRE etc. This procedure is called at the start of ParseBlock. when the inline stack is not empty,
     * as will be the case in: <i><h1> italic heading </h1></i> which is then treated as equivalent to <h1><i>
     * italic heading </i></h1> This is implemented by setting the lexer into a mode where it gets tokens from the
     * inline stack rather than from the input stream.
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

        node = newNode(Node.StartTag, this.lexbuf, this.txtstart, this.txtend);
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

    /* AQ: Try this for speed optimization */
    public static int wstrcasecmp(String s1, String s2)
    {
        return (s1.equalsIgnoreCase(s2) ? 0 : 1);
    }

    public static int wstrcaselexcmp(String s1, String s2)
    {
        char c;
        int i = 0;

        while (i < s1.length() && i < s2.length())
        {
            c = s1.charAt(i);
            if (toLower(c) != toLower(s2.charAt(i)))
            {
                break;
            }
            i += 1;
        }
        if (i == s1.length() && i == s2.length())
        {
            return 0;
        }
        else if (i == s1.length())
        {
            return -1;
        }
        else if (i == s2.length())
        {
            return 1;
        }
        else
        {
            return (s1.charAt(i) > s2.charAt(i) ? 1 : -1);
        }
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
        if (element.type == Node.TextNode)
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

        if (element.attributes != null
            && (element.getAttrByName("id") != null || element.getAttrByName("name") != null))
        {
            return false;
        }

        return true;
    }

    /* duplicate name attribute as an id */
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
                    Report.attrError(this, node, "name", Report.ID_NAME_MISMATCH);
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

    static {
        mapStr("\r\n\f", (short) (NEWLINE | WHITE));
        mapStr(" \t", WHITE);
        mapStr("-.:_", NAMECHAR);
        mapStr("0123456789", (short) (DIGIT | NAMECHAR));
        mapStr("abcdefghijklmnopqrstuvwxyz", (short) (LOWERCASE | LETTER | NAMECHAR));
        mapStr("ABCDEFGHIJKLMNOPQRSTUVWXYZ", (short) (UPPERCASE | LETTER | NAMECHAR));
    }

    private static short MAP(char c)
    {
        return (c < 128 ? lexmap[c] : 0);
    }

    private static boolean isWhite(char c)
    {
        short m = MAP(c);

        return (m & WHITE) != 0;
    }

    private static boolean isDigit(char c)
    {
        short m;

        m = MAP(c);

        return (m & DIGIT) != 0;
    }

    private static boolean isLetter(char c)
    {
        short m;

        m = MAP(c);

        return (m & LETTER) != 0;
    }

    private static char toLower(char c)
    {
        short m = MAP(c);

        if ((m & UPPERCASE) != 0)
        {
            c = (char) (c + 'a' - 'A');
        }

        return c;
    }

    private static char toUpper(char c)
    {
        short m = MAP(c);

        if ((m & LOWERCASE) != 0)
        {
            c = (char) (c + 'A' - 'a');
        }

        return c;
    }

    public static char foldCase(char c, boolean tocaps, boolean xmlTags)
    {
        short m;

        if (!xmlTags)
        {
            m = MAP(c);

            if (tocaps)
            {
                if ((m & LOWERCASE) != 0)
                {
                    c = (char) (c + 'A' - 'a');
                }
            }
            else
            {
                // force to lower case
                if ((m & UPPERCASE) != 0)
                {
                    c = (char) (c + 'a' - 'A');
                }
            }
        }

        return c;
    }

    private static class W3CVersionInfo
    {
        String name;
        String voyagerName;
        String profile;
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
