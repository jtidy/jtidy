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
 * Read configuration file and manage configuration properties. (c) 1998-2000 (W3C) MIT, INRIA, Keio University See
 * Tidy.java for the copyright notice. Derived from <a href="http://www.w3.org/People/Raggett/tidy">HTML Tidy Release
 * 4 Aug 2000</a>
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

/*
 * Configuration files associate a property name with a value. The format is that of a Java .properties file.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

public class Configuration implements java.io.Serializable
{

    /* character encodings */
    public static final int RAW = 0;
    public static final int ASCII = 1;
    public static final int LATIN1 = 2;
    public static final int UTF8 = 3;
    public static final int ISO2022 = 4;
    public static final int MACROMAN = 5;

    /* mode controlling treatment of doctype */
    public static final int DOCTYPE_OMIT = 0;
    public static final int DOCTYPE_AUTO = 1;
    public static final int DOCTYPE_STRICT = 2;
    public static final int DOCTYPE_LOOSE = 3;
    public static final int DOCTYPE_USER = 4;

    protected int spaces = 2; /* default indentation */
    protected int wraplen = 68; /* default wrap margin */
    protected int CharEncoding = ASCII;
    protected int tabsize = 4;

    protected int docTypeMode = DOCTYPE_AUTO; /* see doctype property */
    protected String altText = null; /* default text for alt attribute */
    protected String slidestyle = null; /* style sheet for slides */
    protected String docTypeStr = null; /* user specified doctype */
    protected String errfile = null; /* file name to write errors to */
    protected boolean writeback = false; /* if true then output tidied markup */

    protected boolean OnlyErrors = false; /* if true normal output is suppressed */
    protected boolean ShowWarnings = true; /* however errors are always shown */
    protected boolean Quiet = false; /* no 'Parsing X', guessed DTD or summary */
    protected boolean IndentContent = false; /* indent content of appropriate tags */
    protected boolean SmartIndent = false; /* does text/block level content effect indentation */
    protected boolean HideEndTags = false; /* suppress optional end tags */
    protected boolean XmlTags = false; /* treat input as XML */
    protected boolean XmlOut = false; /* create output as XML */
    protected boolean xHTML = false; /* output extensible HTML */
    protected boolean XmlPi = false; /* add <?xml?> for XML docs */
    protected boolean RawOut = false; /* avoid mapping values > 127 to entities */
    protected boolean UpperCaseTags = false; /* output tags in upper not lower case */
    protected boolean UpperCaseAttrs = false; /* output attributes in upper not lower case */
    protected boolean MakeClean = false; /* remove presentational clutter */
    protected boolean LogicalEmphasis = false; /* replace i by em and b by strong */
    protected boolean DropFontTags = false; /* discard presentation tags */
    protected boolean DropEmptyParas = true; /* discard empty p elements */
    protected boolean FixComments = true; /* fix comments with adjacent hyphens */
    protected boolean BreakBeforeBR = false; /* o/p newline before <br> or not? */
    protected boolean BurstSlides = false; /* create slides on each h2 element */
    protected boolean NumEntities = false; /* use numeric entities */
    protected boolean QuoteMarks = false; /* output " marks as &quot; */
    protected boolean QuoteNbsp = true; /* output non-breaking space as entity */
    protected boolean QuoteAmpersand = true; /* output naked ampersand as &amp; */
    protected boolean WrapAttVals = false; /* wrap within attribute values */
    protected boolean WrapScriptlets = false; /* wrap within JavaScript string literals */
    protected boolean WrapSection = true; /* wrap within <![ ... ]> section tags */
    protected boolean WrapAsp = true; /* wrap within ASP pseudo elements */
    protected boolean WrapJste = true; /* wrap within JSTE pseudo elements */
    protected boolean WrapPhp = true; /* wrap within PHP pseudo elements */
    protected boolean FixBackslash = true; /* fix URLs by replacing \ with / */
    protected boolean IndentAttributes = false; /* newline+indent before each attribute */
    protected boolean XmlPIs = false; /* if set to yes PIs must end with ?> */
    protected boolean XmlSpace = false; /* if set to yes adds xml:space attr as needed */
    protected boolean EncloseBodyText = false; /* if yes text at body is wrapped in <p> 's */
    protected boolean EncloseBlockText = false; /* if yes text in blocks is wrapped in <p> 's */
    protected boolean KeepFileTimes = true; /* if yes last modied time is preserved */
    protected boolean Word2000 = false; /* draconian cleaning for Word2000 */
    protected boolean TidyMark = true; /* add meta element indicating tidied doc */
    protected boolean Emacs = false; /* if true format error output for GNU Emacs */
    protected boolean LiteralAttribs = false; /* if true attributes may use newlines */

    protected TagTable tt; /* TagTable associated with this Configuration */

    private transient Properties _properties = new Properties();

    public Configuration()
    {
    }

    public void addProps(Properties p)
    {
        Enumeration enum = p.propertyNames();
        while (enum.hasMoreElements())
        {
            String key = (String) enum.nextElement();
            String value = p.getProperty(key);
            _properties.put(key, value);
        }
        parseProps();
    }

    public void parseFile(String filename)
    {
        try
        {
            _properties.load(new FileInputStream(filename));
        }
        catch (IOException e)
        {
            System.err.println(filename + e.toString());
            return;
        }
        parseProps();
    }

    private void parseProps()
    {
        String value;

        value = _properties.getProperty("indent-spaces");
        if (value != null)
            spaces = parseInt(value, "indent-spaces");

        value = _properties.getProperty("wrap");
        if (value != null)
            wraplen = parseInt(value, "wrap");

        value = _properties.getProperty("wrap-attributes");
        if (value != null)
            WrapAttVals = parseBool(value, "wrap-attributes");

        value = _properties.getProperty("wrap-script-literals");
        if (value != null)
            WrapScriptlets = parseBool(value, "wrap-script-literals");

        value = _properties.getProperty("wrap-sections");
        if (value != null)
            WrapSection = parseBool(value, "wrap-sections");

        value = _properties.getProperty("wrap-asp");
        if (value != null)
            WrapAsp = parseBool(value, "wrap-asp");

        value = _properties.getProperty("wrap-jste");
        if (value != null)
            WrapJste = parseBool(value, "wrap-jste");

        value = _properties.getProperty("wrap-php");
        if (value != null)
            WrapPhp = parseBool(value, "wrap-php");

        value = _properties.getProperty("literal-attributes");
        if (value != null)
            LiteralAttribs = parseBool(value, "literal-attributes");

        value = _properties.getProperty("tab-size");
        if (value != null)
            tabsize = parseInt(value, "tab-size");

        value = _properties.getProperty("markup");
        if (value != null)
            OnlyErrors = parseInvBool(value, "markup");

        value = _properties.getProperty("quiet");
        if (value != null)
            Quiet = parseBool(value, "quiet");

        value = _properties.getProperty("tidy-mark");
        if (value != null)
            TidyMark = parseBool(value, "tidy-mark");

        value = _properties.getProperty("indent");
        if (value != null)
            IndentContent = parseIndent(value, "indent");

        value = _properties.getProperty("indent-attributes");
        if (value != null)
            IndentAttributes = parseBool(value, "ident-attributes");

        value = _properties.getProperty("hide-endtags");
        if (value != null)
            HideEndTags = parseBool(value, "hide-endtags");

        value = _properties.getProperty("input-xml");
        if (value != null)
            XmlTags = parseBool(value, "input-xml");

        value = _properties.getProperty("output-xml");
        if (value != null)
            XmlOut = parseBool(value, "output-xml");

        value = _properties.getProperty("output-xhtml");
        if (value != null)
            xHTML = parseBool(value, "output-xhtml");

        value = _properties.getProperty("add-xml-pi");
        if (value != null)
            XmlPi = parseBool(value, "add-xml-pi");

        value = _properties.getProperty("add-xml-decl");
        if (value != null)
            XmlPi = parseBool(value, "add-xml-decl");

        value = _properties.getProperty("assume-xml-procins");
        if (value != null)
            XmlPIs = parseBool(value, "assume-xml-procins");

        value = _properties.getProperty("raw");
        if (value != null)
            RawOut = parseBool(value, "raw");

        value = _properties.getProperty("uppercase-tags");
        if (value != null)
            UpperCaseTags = parseBool(value, "uppercase-tags");

        value = _properties.getProperty("uppercase-attributes");
        if (value != null)
            UpperCaseAttrs = parseBool(value, "uppercase-attributes");

        value = _properties.getProperty("clean");
        if (value != null)
            MakeClean = parseBool(value, "clean");

        value = _properties.getProperty("logical-emphasis");
        if (value != null)
            LogicalEmphasis = parseBool(value, "logical-emphasis");

        value = _properties.getProperty("word-2000");
        if (value != null)
            Word2000 = parseBool(value, "word-2000");

        value = _properties.getProperty("drop-empty-paras");
        if (value != null)
            DropEmptyParas = parseBool(value, "drop-empty-paras");

        value = _properties.getProperty("drop-font-tags");
        if (value != null)
            DropFontTags = parseBool(value, "drop-font-tags");

        value = _properties.getProperty("enclose-text");
        if (value != null)
            EncloseBodyText = parseBool(value, "enclose-text");

        value = _properties.getProperty("enclose-block-text");
        if (value != null)
            EncloseBlockText = parseBool(value, "enclose-block-text");

        value = _properties.getProperty("alt-text");
        if (value != null)
            altText = value;

        value = _properties.getProperty("add-xml-space");
        if (value != null)
            XmlSpace = parseBool(value, "add-xml-space");

        value = _properties.getProperty("fix-bad-comments");
        if (value != null)
            FixComments = parseBool(value, "fix-bad-comments");

        value = _properties.getProperty("split");
        if (value != null)
            BurstSlides = parseBool(value, "split");

        value = _properties.getProperty("break-before-br");
        if (value != null)
            BreakBeforeBR = parseBool(value, "break-before-br");

        value = _properties.getProperty("numeric-entities");
        if (value != null)
            NumEntities = parseBool(value, "numeric-entities");

        value = _properties.getProperty("quote-marks");
        if (value != null)
            QuoteMarks = parseBool(value, "quote-marks");

        value = _properties.getProperty("quote-nbsp");
        if (value != null)
            QuoteNbsp = parseBool(value, "quote-nbsp");

        value = _properties.getProperty("quote-ampersand");
        if (value != null)
            QuoteAmpersand = parseBool(value, "quote-ampersand");

        value = _properties.getProperty("write-back");
        if (value != null)
            writeback = parseBool(value, "write-back");

        value = _properties.getProperty("keep-time");
        if (value != null)
            KeepFileTimes = parseBool(value, "keep-time");

        value = _properties.getProperty("show-warnings");
        if (value != null)
            ShowWarnings = parseBool(value, "show-warnings");

        value = _properties.getProperty("error-file");
        if (value != null)
            errfile = parseName(value, "error-file");

        value = _properties.getProperty("slide-style");
        if (value != null)
            slidestyle = parseName(value, "slide-style");

        value = _properties.getProperty("new-inline-tags");
        if (value != null)
            parseInlineTagNames(value, "new-inline-tags");

        value = _properties.getProperty("new-blocklevel-tags");
        if (value != null)
            parseBlockTagNames(value, "new-blocklevel-tags");

        value = _properties.getProperty("new-empty-tags");
        if (value != null)
            parseEmptyTagNames(value, "new-empty-tags");

        value = _properties.getProperty("new-pre-tags");
        if (value != null)
            parsePreTagNames(value, "new-pre-tags");

        value = _properties.getProperty("char-encoding");
        if (value != null)
            CharEncoding = parseCharEncoding(value, "char-encoding");

        value = _properties.getProperty("doctype");
        if (value != null)
            docTypeStr = parseDocType(value, "doctype");

        value = _properties.getProperty("fix-backslash");
        if (value != null)
            FixBackslash = parseBool(value, "fix-backslash");

        value = _properties.getProperty("gnu-emacs");
        if (value != null)
            Emacs = parseBool(value, "gnu-emacs");
    }

    /* ensure that config is self consistent */
    public void adjust()
    {
        if (EncloseBlockText)
            EncloseBodyText = true;

        /* avoid the need to set IndentContent when SmartIndent is set */

        if (SmartIndent)
            IndentContent = true;

        /* disable wrapping */
        if (wraplen == 0)
            wraplen = 0x7FFFFFFF;

        /* Word 2000 needs o:p to be declared as inline */
        if (Word2000)
        {
            tt.defineInlineTag("o:p");
        }

        /* XHTML is written in lower case */
        if (xHTML)
        {
            XmlOut = true;
            UpperCaseTags = false;
            UpperCaseAttrs = false;
        }

        /* if XML in, then XML out */
        if (XmlTags)
        {
            XmlOut = true;
            XmlPIs = true;
        }

        /* XML requires end tags */
        if (XmlOut)
        {
            QuoteAmpersand = true;
            HideEndTags = false;
        }
    }

    private static int parseInt(String s, String option)
    {
        int i = 0;
        try
        {
            i = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            Report.badArgument(option);
            i = -1;
        }
        return i;
    }

    private static boolean parseBool(String s, String option)
    {
        boolean b = false;
        if (s != null && s.length() > 0)
        {
            char c = s.charAt(0);
            if ((c == 't') || (c == 'T') || (c == 'Y') || (c == 'y') || (c == '1'))
                b = true;
            else if ((c == 'f') || (c == 'F') || (c == 'N') || (c == 'n') || (c == '0'))
                b = false;
            else
                Report.badArgument(option);
        }
        return b;
    }

    private static boolean parseInvBool(String s, String option)
    {
        boolean b = false;
        if (s != null && s.length() > 0)
        {
            char c = s.charAt(0);
            if ((c == 't') || (c == 'T') || (c == 'Y') || (c == 'y'))
                b = true;
            else if ((c == 'f') || (c == 'F') || (c == 'N') || (c == 'n'))
                b = false;
            else
                Report.badArgument(option);
        }
        return !b;
    }

    private static String parseName(String s, String option)
    {
        StringTokenizer t = new StringTokenizer(s);
        String rs = null;
        if (t.countTokens() >= 1)
            rs = t.nextToken();
        else
            Report.badArgument(option);
        return rs;
    }

    private static int parseCharEncoding(String s, String option)
    {
        int result = ASCII;

        if (Lexer.wstrcasecmp(s, "ascii") == 0)
            result = ASCII;
        else if (Lexer.wstrcasecmp(s, "latin1") == 0)
            result = LATIN1;
        else if (Lexer.wstrcasecmp(s, "raw") == 0)
            result = RAW;
        else if (Lexer.wstrcasecmp(s, "utf8") == 0)
            result = UTF8;
        else if (Lexer.wstrcasecmp(s, "iso2022") == 0)
            result = ISO2022;
        else if (Lexer.wstrcasecmp(s, "mac") == 0)
            result = MACROMAN;
        else
            Report.badArgument(option);

        return result;
    }

    /* slight hack to avoid changes to pprint.c */
    private boolean parseIndent(String s, String option)
    {
        boolean b = IndentContent;

        if (Lexer.wstrcasecmp(s, "yes") == 0)
        {
            b = true;
            SmartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "true") == 0)
        {
            b = true;
            SmartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "no") == 0)
        {
            b = false;
            SmartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "false") == 0)
        {
            b = false;
            SmartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "auto") == 0)
        {
            b = true;
            SmartIndent = true;
        }
        else
            Report.badArgument(option);
        return b;
    }

    private void parseInlineTagNames(String s, String option)
    {
        StringTokenizer t = new StringTokenizer(s, " \t\n\r,");
        while (t.hasMoreTokens())
        {
            tt.defineInlineTag(t.nextToken());
        }
    }

    private void parseBlockTagNames(String s, String option)
    {
        StringTokenizer t = new StringTokenizer(s, " \t\n\r,");
        while (t.hasMoreTokens())
        {
            tt.defineBlockTag(t.nextToken());
        }
    }

    private void parseEmptyTagNames(String s, String option)
    {
        StringTokenizer t = new StringTokenizer(s, " \t\n\r,");
        while (t.hasMoreTokens())
        {
            tt.defineEmptyTag(t.nextToken());
        }
    }

    private void parsePreTagNames(String s, String option)
    {
        StringTokenizer t = new StringTokenizer(s, " \t\n\r,");
        while (t.hasMoreTokens())
        {
            tt.definePreTag(t.nextToken());
        }
    }

    /*
     * doctype: omit | auto | strict | loose | <fpi> where the fpi is a string similar to "-//ACME//DTD HTML
     * 3.14159//EN"
     */
    protected String parseDocType(String s, String option)
    {
        s = s.trim();

        /* "-//ACME//DTD HTML 3.14159//EN" or similar */

        if (s.startsWith("\""))
        {
            docTypeMode = DOCTYPE_USER;
            return s;
        }

        /* read first word */
        String word = "";
        StringTokenizer t = new StringTokenizer(s, " \t\n\r,");
        if (t.hasMoreTokens())
            word = t.nextToken();

        if (Lexer.wstrcasecmp(word, "omit") == 0)
            docTypeMode = DOCTYPE_OMIT;
        else if (Lexer.wstrcasecmp(word, "strict") == 0)
            docTypeMode = DOCTYPE_STRICT;
        else if (Lexer.wstrcasecmp(word, "loose") == 0 || Lexer.wstrcasecmp(word, "transitional") == 0)
            docTypeMode = DOCTYPE_LOOSE;
        else if (Lexer.wstrcasecmp(word, "auto") == 0)
            docTypeMode = DOCTYPE_AUTO;
        else
        {
            docTypeMode = DOCTYPE_AUTO;
            Report.badArgument(option);
        }
        return null;
    }

}
