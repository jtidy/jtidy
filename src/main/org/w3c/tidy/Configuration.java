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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * Read configuration file and manage configuration properties. Configuration files associate a property name with a
 * value. The format is that of a Java .properties file.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class Configuration implements java.io.Serializable
{

    /**
     * character encoding = RAW.
     */
    public static final int RAW = 0;

    /**
     * character encoding = ASCII.
     */
    public static final int ASCII = 1;

    /**
     * character encoding = LATIN1.
     */
    public static final int LATIN1 = 2;

    /**
     * character encoding = UTF8.
     */
    public static final int UTF8 = 3;

    /**
     * character encoding = ISO2022.
     */
    public static final int ISO2022 = 4;

    /**
     * character encoding = MACROMAN.
     */
    public static final int MACROMAN = 5;

    /**
     * character encoding = UTF16LE.
     */
    public static final int UTF16LE = 6;

    /**
     * character encoding = UTF16BE.
     */
    public static final int UTF16BE = 7;

    /**
     * character encoding = UTF16.
     */
    public static final int UTF16 = 8;

    /**
     * character encoding = WIN1252.
     */
    public static final int WIN1252 = 9;

    /**
     * character encoding = BIG5.
     */
    public static final int BIG5 = 10; // #431953 - RJ

    /**
     * character encoding = SHIFTJIS.
     */
    public static final int SHIFTJIS = 11; // #431953 - RJ

    /*
     * RJ. Note that Big5 and SHIFTJIS are not converted to ISO 10646 codepoints (i.e., to Unicode) before being recoded
     * into UTF-8. This may be confusing: usually UTF-8 implies ISO10646 codepoints.
     */

    /**
     * treatment of doctype: omit.
     * @todo should be an enumeration DocTypeMode
     */
    public static final int DOCTYPE_OMIT = 0;

    /**
     * treatment of doctype: auto.
     */
    public static final int DOCTYPE_AUTO = 1;

    /**
     * treatment of doctype: strict.
     */
    public static final int DOCTYPE_STRICT = 2;

    /**
     * treatment of doctype: loose.
     */
    public static final int DOCTYPE_LOOSE = 3;

    /**
     * treatment of doctype: user.
     */
    public static final int DOCTYPE_USER = 4;

    /**
     * Keep last duplicate attribute.
     * @todo should be an enumeration DupAttrMode
     */
    public static final int KEEP_LAST = 0;

    /**
     * Keep first duplicate attribute.
     */
    public static final int KEEP_FIRST = 1;

    /**
     * default indentation.
     */
    protected int spaces = 2;

    /**
     * default wrap margin (68).
     */
    protected int wraplen = 68;

    /**
     * default character encoding (ASCII).
     */
    protected int charEncoding = ASCII;

    /**
     * default tab size (4).
     */
    protected int tabsize = 4;

    /**
     * see doctype property.
     */
    protected int docTypeMode = DOCTYPE_AUTO;

    /**
     * Keep first or last duplicate attribute.
     */
    protected int dupAttrMode = KEEP_LAST;

    /**
     * default text for alt attribute.
     */
    protected String altText;

    /**
     * style sheet for slides.
     */
    protected String slidestyle;

    /**
     * user specified doctype.
     */
    protected String docTypeStr;

    /**
     * file name to write errors to.
     */
    protected String errfile;

    /**
     * if true then output tidied markup.
     */
    protected boolean writeback;

    /**
     * if true normal output is suppressed.
     */
    protected boolean onlyErrors;

    /**
     * however errors are always shown.
     */
    protected boolean showWarnings = true;

    /**
     * no 'Parsing X', guessed DTD or summary.
     */
    protected boolean quiet;

    /**
     * indent content of appropriate tags.
     */
    protected boolean indentContent;

    /**
     * does text/block level content effect indentation.
     */
    protected boolean smartIndent;

    /**
     * suppress optional end tags.
     */
    protected boolean hideEndTags;

    /**
     * treat input as XML.
     */
    protected boolean xmlTags;

    /**
     * create output as XML.
     */
    protected boolean xmlOut;

    /**
     * output extensible HTML.
     */
    protected boolean xHTML;

    /**
     * add <code>&lt;?xml?&gt;</code> for XML docs.
     */
    protected boolean xmlPi;

    /**
     * avoid mapping values > 127 to entities.
     */
    protected boolean rawOut;

    /**
     * output tags in upper not lower case.
     */
    protected boolean upperCaseTags;

    /**
     * output attributes in upper not lower case.
     */
    protected boolean upperCaseAttrs;

    /**
     * remove presentational clutter.
     */
    protected boolean makeClean;

    /**
     * replace i by em and b by strong.
     */
    protected boolean logicalEmphasis;

    /**
     * discard presentation tags.
     */
    protected boolean dropFontTags;

    /**
     * discard proprietary attributes.
     */
    protected boolean dropProprietaryAttributes;

    /**
     * discard empty p elements.
     */
    protected boolean dropEmptyParas = true;

    /**
     * fix comments with adjacent hyphens.
     */
    protected boolean fixComments = true;

    /**
     * o/p newline before br or not?
     */
    protected boolean breakBeforeBR;

    /**
     * create slides on each h2 element.
     */
    protected boolean burstSlides;

    /**
     * use numeric entities.
     */
    protected boolean numEntities;

    /**
     * output " marks as &quot;.
     */
    protected boolean quoteMarks;

    /**
     * output non-breaking space as entity.
     */
    protected boolean quoteNbsp = true;

    /**
     * output naked ampersand as &amp;.
     */
    protected boolean quoteAmpersand = true;

    /**
     * wrap within attribute values.
     */
    protected boolean wrapAttVals;

    /**
     * wrap within JavaScript string literals.
     */
    protected boolean wrapScriptlets;

    /**
     * wrap within CDATA section tags.
     */
    protected boolean wrapSection = true;

    /**
     * wrap within ASP pseudo elements.
     */
    protected boolean wrapAsp = true;

    /**
     * wrap within JSTE pseudo elements.
     */
    protected boolean wrapJste = true;

    /**
     * wrap within PHP pseudo elements.
     */
    protected boolean wrapPhp = true;

    /**
     * fix URLs by replacing \ with /.
     */
    protected boolean fixBackslash = true;

    /**
     * newline+indent before each attribute.
     */
    protected boolean indentAttributes;

    /**
     * if set to yes PIs must end with <code>?&gt;</code>.
     */
    protected boolean xmlPIs;

    /**
     * if set to yes adds xml:space attr as needed.
     */
    protected boolean xmlSpace;

    /**
     * if yes text at body is wrapped in p's.
     */
    protected boolean encloseBodyText;

    /**
     * if yes text in blocks is wrapped in p's.
     */
    protected boolean encloseBlockText;

    /**
     * if yes last modied time is preserved.
     */
    protected boolean keepFileTimes = true;

    /**
     * draconian cleaning for Word2000.
     */
    protected boolean word2000;

    /**
     * add meta element indicating tidied doc.
     */
    protected boolean tidyMark = true;

    /**
     * if true format error output for GNU Emacs.
     */
    protected boolean emacs;

    /**
     * if true attributes may use newlines.
     */
    protected boolean literalAttribs;

    /**
     * output BODY content only.
     */
    protected boolean bodyOnly;

    /**
     * properly escape URLs.
     */
    protected boolean fixUri = true;

    /**
     * folds known attribute values to lower case.
     */
    protected boolean lowerLiterals = true;

    /**
     * replace hex color attribute values with names.
     */
    protected boolean replaceColor;

    /**
     * hides all (real) comments in output.
     */
    protected boolean hideComments;

    /**
     * indent CDATA sections.
     */
    protected boolean indentCdata;

    /**
     * output document even if errors were found.
     */
    protected boolean forceOutput;

    /**
     * number of errors to put out.
     */
    protected int showErrors = 6;

    /**
     * convert quotes and dashes to nearest ASCII char.
     */
    protected boolean asciiChars = true;

    /**
     * join multiple class attributes.
     */
    protected boolean joinClasses;

    /**
     * join multiple style attributes.
     */
    protected boolean joinStyles = true;

    /**
     * char encoding used when replacing illegal SGML chars, regardless of specified encoding.
     */
    protected int replacementCharEncoding = WIN1252; // by default

    /**
     * TagTable associated with this Configuration.
     */
    protected TagTable tt;

    /**
     * configuration properties.
     */
    private transient Properties properties = new Properties();

    /**
     * Report instance. Used for messages.
     */
    private Report report;

    /**
     * Instantiates a new Configuration. This method should be called by Tidy only.
     * @param report Report instance
     */
    protected Configuration(Report report)
    {
        this.report = report;
    }

    public void addProps(Properties p)
    {
        Enumeration enum = p.propertyNames();
        while (enum.hasMoreElements())
        {
            String key = (String) enum.nextElement();
            String value = p.getProperty(key);
            properties.put(key, value);
        }
        parseProps();
    }

    public void parseFile(String filename)
    {
        try
        {
            properties.load(new FileInputStream(filename));
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

        value = properties.getProperty("indent-spaces");
        if (value != null)
        {
            spaces = parseInt(value, "indent-spaces");
        }

        value = properties.getProperty("wrap");
        if (value != null)
        {
            wraplen = parseInt(value, "wrap");
        }

        value = properties.getProperty("wrap-attributes");
        if (value != null)
        {
            wrapAttVals = parseBool(value, "wrap-attributes");
        }

        value = properties.getProperty("wrap-script-literals");
        if (value != null)
        {
            wrapScriptlets = parseBool(value, "wrap-script-literals");
        }

        value = properties.getProperty("wrap-sections");
        if (value != null)
        {
            wrapSection = parseBool(value, "wrap-sections");
        }

        value = properties.getProperty("wrap-asp");
        if (value != null)
        {
            wrapAsp = parseBool(value, "wrap-asp");
        }

        value = properties.getProperty("wrap-jste");
        if (value != null)
        {
            wrapJste = parseBool(value, "wrap-jste");
        }

        value = properties.getProperty("wrap-php");
        if (value != null)
        {
            wrapPhp = parseBool(value, "wrap-php");
        }

        value = properties.getProperty("literal-attributes");
        if (value != null)
        {
            literalAttribs = parseBool(value, "literal-attributes");
        }

        value = properties.getProperty("show-body-only");
        if (value != null)
        {
            this.bodyOnly = parseBool(value, "show-body-only");
        }

        value = properties.getProperty("fix-uri");
        if (value != null)
        {
            this.fixUri = parseBool(value, "fix-uri");
        }

        value = properties.getProperty("lower-literals");
        if (value != null)
        {
            this.lowerLiterals = parseBool(value, "lower-literals");
        }

        value = properties.getProperty("hide-comments");
        if (value != null)
        {
            this.hideComments = parseBool(value, "hide-comments");
        }

        value = properties.getProperty("indent-cdata");
        if (value != null)
        {
            this.indentCdata = parseBool(value, "indent-cdata");
        }

        value = properties.getProperty("force-output");
        if (value != null)
        {
            this.forceOutput = parseBool(value, "force-output");
        }

        value = properties.getProperty("show-errors");
        if (value != null)
        {
            this.showErrors = parseInt(value, "show-errors");
        }

        value = properties.getProperty("ascii-chars");
        if (value != null)
        {
            this.asciiChars = parseBool(value, "ascii-chars");
        }

        value = properties.getProperty("join-classes");
        if (value != null)
        {
            this.joinClasses = parseBool(value, "join-classes");
        }

        value = properties.getProperty("join-styles");
        if (value != null)
        {
            this.joinStyles = parseBool(value, "join-styles");
        }

        value = properties.getProperty("replace-color");
        if (value != null)
        {
            this.replaceColor = parseBool(value, "replace-color");
        }

        value = properties.getProperty("tab-size");
        if (value != null)
        {
            tabsize = parseInt(value, "tab-size");
        }

        value = properties.getProperty("markup");
        if (value != null)
        {
            onlyErrors = parseInvBool(value, "markup");
        }

        value = properties.getProperty("quiet");
        if (value != null)
        {
            quiet = parseBool(value, "quiet");
        }

        value = properties.getProperty("tidy-mark");
        if (value != null)
        {
            tidyMark = parseBool(value, "tidy-mark");
        }

        value = properties.getProperty("indent");
        if (value != null)
        {
            indentContent = parseIndent(value, "indent");
        }

        value = properties.getProperty("indent-attributes");
        if (value != null)
        {
            indentAttributes = parseBool(value, "indent-attributes");
        }

        value = properties.getProperty("hide-endtags");
        if (value != null)
        {
            hideEndTags = parseBool(value, "hide-endtags");
        }

        value = properties.getProperty("input-xml");
        if (value != null)
        {
            xmlTags = parseBool(value, "input-xml");
        }

        value = properties.getProperty("output-xml");
        if (value != null)
        {
            xmlOut = parseBool(value, "output-xml");
        }

        value = properties.getProperty("output-xhtml");
        if (value != null)
        {
            xHTML = parseBool(value, "output-xhtml");
        }

        value = properties.getProperty("add-xml-pi");
        if (value != null)
        {
            xmlPi = parseBool(value, "add-xml-pi");
        }

        value = properties.getProperty("add-xml-decl");
        if (value != null)
        {
            xmlPi = parseBool(value, "add-xml-decl");
        }

        value = properties.getProperty("assume-xml-procins");
        if (value != null)
        {
            xmlPIs = parseBool(value, "assume-xml-procins");
        }

        value = properties.getProperty("raw");
        if (value != null)
        {
            rawOut = parseBool(value, "raw");
        }

        value = properties.getProperty("uppercase-tags");
        if (value != null)
        {
            upperCaseTags = parseBool(value, "uppercase-tags");
        }

        value = properties.getProperty("uppercase-attributes");
        if (value != null)
        {
            upperCaseAttrs = parseBool(value, "uppercase-attributes");
        }

        value = properties.getProperty("clean");
        if (value != null)
        {
            makeClean = parseBool(value, "clean");
        }

        value = properties.getProperty("logical-emphasis");
        if (value != null)
        {
            logicalEmphasis = parseBool(value, "logical-emphasis");
        }

        value = properties.getProperty("word-2000");
        if (value != null)
        {
            word2000 = parseBool(value, "word-2000");
        }

        value = properties.getProperty("drop-empty-paras");
        if (value != null)
        {
            dropEmptyParas = parseBool(value, "drop-empty-paras");
        }

        value = properties.getProperty("drop-font-tags");
        if (value != null)
        {
            dropFontTags = parseBool(value, "drop-font-tags");
        }

        value = properties.getProperty("drop-proprietary-attributes");
        if (value != null)
        {
            dropProprietaryAttributes = parseBool(value, "drop-proprietary-attributes");
        }

        value = properties.getProperty("enclose-text");
        if (value != null)
        {
            encloseBodyText = parseBool(value, "enclose-text");
        }

        value = properties.getProperty("enclose-block-text");
        if (value != null)
        {
            encloseBlockText = parseBool(value, "enclose-block-text");
        }

        value = properties.getProperty("alt-text");
        if (value != null)
        {
            altText = value;
        }

        value = properties.getProperty("add-xml-space");
        if (value != null)
        {
            xmlSpace = parseBool(value, "add-xml-space");
        }

        value = properties.getProperty("fix-bad-comments");
        if (value != null)
        {
            fixComments = parseBool(value, "fix-bad-comments");
        }

        value = properties.getProperty("split");
        if (value != null)
        {
            burstSlides = parseBool(value, "split");
        }

        value = properties.getProperty("break-before-br");
        if (value != null)
        {
            breakBeforeBR = parseBool(value, "break-before-br");
        }

        value = properties.getProperty("numeric-entities");
        if (value != null)
        {
            numEntities = parseBool(value, "numeric-entities");
        }

        value = properties.getProperty("quote-marks");
        if (value != null)
        {
            quoteMarks = parseBool(value, "quote-marks");
        }

        value = properties.getProperty("quote-nbsp");
        if (value != null)
        {
            quoteNbsp = parseBool(value, "quote-nbsp");
        }

        value = properties.getProperty("quote-ampersand");
        if (value != null)
        {
            quoteAmpersand = parseBool(value, "quote-ampersand");
        }

        value = properties.getProperty("write-back");
        if (value != null)
        {
            writeback = parseBool(value, "write-back");
        }

        value = properties.getProperty("keep-time");
        if (value != null)
        {
            keepFileTimes = parseBool(value, "keep-time");
        }

        value = properties.getProperty("show-warnings");
        if (value != null)
        {
            showWarnings = parseBool(value, "show-warnings");
        }

        value = properties.getProperty("error-file");
        if (value != null)
        {
            errfile = parseName(value, "error-file");
        }

        value = properties.getProperty("slide-style");
        if (value != null)
        {
            slidestyle = parseName(value, "slide-style");
        }

        value = properties.getProperty("new-inline-tags");
        if (value != null)
        {
            parseInlineTagNames(value, "new-inline-tags");
        }

        value = properties.getProperty("new-blocklevel-tags");
        if (value != null)
        {
            parseBlockTagNames(value, "new-blocklevel-tags");
        }

        value = properties.getProperty("new-empty-tags");
        if (value != null)
        {
            parseEmptyTagNames(value, "new-empty-tags");
        }

        value = properties.getProperty("new-pre-tags");
        if (value != null)
        {
            parsePreTagNames(value, "new-pre-tags");
        }

        value = properties.getProperty("char-encoding");
        if (value != null)
        {
            charEncoding = parseCharEncoding(value, "char-encoding");
        }

        value = properties.getProperty("doctype");
        if (value != null)
        {
            docTypeStr = parseDocType(value, "doctype");
        }

        value = properties.getProperty("fix-backslash");
        if (value != null)
        {
            fixBackslash = parseBool(value, "fix-backslash");
        }

        value = properties.getProperty("gnu-emacs");
        if (value != null)
        {
            emacs = parseBool(value, "gnu-emacs");
        }
    }

    /* ensure that config is self consistent */
    public void adjust()
    {
        if (encloseBlockText)
        {
            encloseBodyText = true;
        }

        /* avoid the need to set IndentContent when SmartIndent is set */

        if (smartIndent)
        {
            indentContent = true;
        }

        /* disable wrapping */
        if (wraplen == 0)
        {
            wraplen = 0x7FFFFFFF;
        }

        /* Word 2000 needs o:p to be declared as inline */
        if (word2000)
        {
            tt.defineInlineTag("o:p");
        }

        /* XHTML is written in lower case */
        if (xHTML)
        {
            xmlOut = true;
            upperCaseTags = false;
            upperCaseAttrs = false;
        }

        /* if XML in, then XML out */
        if (xmlTags)
        {
            xmlOut = true;
            xmlPIs = true;
        }

        // #427837 - fix by Dave Raggett 02 Jun 01
        // generate <?xml version="1.0" encoding="iso-8859-1"?> if the character encoding is Latin-1 etc.
        if (charEncoding != UTF8 && charEncoding != ASCII && xmlOut)
        {
            xmlPi = true;
        }

        /* XML requires end tags */
        if (xmlOut)
        {
            quoteAmpersand = true;
            hideEndTags = false;
        }
    }

    private int parseInt(String s, String option)
    {
        int i = 0;
        try
        {
            i = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            report.badArgument(option);
            i = -1;
        }
        return i;
    }

    private boolean parseBool(String s, String option)
    {
        boolean b = false;
        if (s != null && s.length() > 0)
        {
            char c = s.charAt(0);
            if ((c == 't') || (c == 'T') || (c == 'Y') || (c == 'y') || (c == '1'))
            {
                b = true;
            }
            else if ((c == 'f') || (c == 'F') || (c == 'N') || (c == 'n') || (c == '0'))
            {
                b = false;
            }
            else
            {
                report.badArgument(option);
            }
        }
        return b;
    }

    private boolean parseInvBool(String s, String option)
    {
        boolean b = false;
        if (s != null && s.length() > 0)
        {
            char c = s.charAt(0);
            if ((c == 't') || (c == 'T') || (c == 'Y') || (c == 'y'))
            {
                b = true;
            }
            else if ((c == 'f') || (c == 'F') || (c == 'N') || (c == 'n'))
            {
                b = false;
            }
            else
            {
                report.badArgument(option);
            }
        }
        return !b;
    }

    private String parseName(String s, String option)
    {
        StringTokenizer t = new StringTokenizer(s);
        String rs = null;
        if (t.countTokens() >= 1)
        {
            rs = t.nextToken();
        }
        else
        {
            report.badArgument(option);
        }
        return rs;
    }

    private int parseCharEncoding(String s, String option)
    {
        int result = ASCII;

        if (Lexer.wstrcasecmp(s, "ascii") == 0)
        {
            result = ASCII;
        }
        else if (Lexer.wstrcasecmp(s, "latin1") == 0)
        {
            result = LATIN1;
        }
        else if (Lexer.wstrcasecmp(s, "raw") == 0)
        {
            result = RAW;
        }
        else if (Lexer.wstrcasecmp(s, "utf8") == 0)
        {
            result = UTF8;
        }
        else if (Lexer.wstrcasecmp(s, "iso2022") == 0)
        {
            result = ISO2022;
        }
        else if (Lexer.wstrcasecmp(s, "mac") == 0)
        {
            result = MACROMAN;
        }
        else
        {
            report.badArgument(option);
        }

        return result;
    }

    /* slight hack to avoid changes to pprint.c */
    private boolean parseIndent(String s, String option)
    {
        boolean b = indentContent;

        if (Lexer.wstrcasecmp(s, "yes") == 0)
        {
            b = true;
            smartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "true") == 0)
        {
            b = true;
            smartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "no") == 0)
        {
            b = false;
            smartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "false") == 0)
        {
            b = false;
            smartIndent = false;
        }
        else if (Lexer.wstrcasecmp(s, "auto") == 0)
        {
            b = true;
            smartIndent = true;
        }
        else
        {
            report.badArgument(option);
        }
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

    /**
     * doctype: <code>omit | auto | strict | loose | [fpi]</code> where the fpi is a string similar to
     * <code>"-//ACME//DTD HTML 3.14159//EN"</code>.
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
        {
            word = t.nextToken();
        }
        // #443663 - fix by Terry Teague 23 Jul 01
        if ("auto".equalsIgnoreCase(word))
        {
            docTypeMode = DOCTYPE_AUTO;
        }
        else if ("omit".equalsIgnoreCase(word))
        {
            docTypeMode = DOCTYPE_OMIT;
        }
        else if ("strict".equalsIgnoreCase(word))
        {
            docTypeMode = DOCTYPE_STRICT;
        }
        else if ("loose".equalsIgnoreCase(word) || "transitional".equalsIgnoreCase(word))
        {
            docTypeMode = DOCTYPE_LOOSE;
        }
        else
        {
            report.badArgument(option);
        }
        return null;
    }

}