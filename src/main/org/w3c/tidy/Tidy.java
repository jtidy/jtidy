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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Properties;


/**
 * HTML parser and pretty printer.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @version $Revision$ ($Author$)
 */
public class Tidy implements Serializable
{

    static final long serialVersionUID = -2794371560623987718L;

    private boolean initialized;

    /**
     * error output stream
     */
    private PrintWriter errout;
    private PrintWriter stderr;
    private Configuration configuration;
    private String inputStreamName = "InputStream";
    private int parseErrors;
    private int parseWarnings;

    private Report report;

    public Tidy()
    {
        this.report = new Report();
        configuration = new Configuration(this.report);
        if (configuration == null)
        {
            return;
        }

        AttributeTable at = AttributeTable.getDefaultAttributeTable();
        if (at == null)
        {
            return;
        }
        TagTable tt = new TagTable();
        if (tt == null)
        {
            return;
        }
        tt.setConfiguration(configuration);
        configuration.tt = tt;
        EntityTable et = EntityTable.getDefaultEntityTable();
        if (et == null)
        {
            return;
        }

        configuration.errfile = null;
        stderr = new PrintWriter(System.err, true);
        errout = stderr;
        initialized = true;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public PrintWriter getStderr()
    {
        return stderr;
    }

    /**
     * ParseErrors - the number of errors that occurred in the most recent parse operation
     */
    public int getParseErrors()
    {
        return parseErrors;
    }

    /**
     * ParseWarnings - the number of warnings that occurred in the most recent parse operation
     */
    public int getParseWarnings()
    {
        return parseWarnings;
    }

    /**
     * Errout - the error output stream
     */
    public PrintWriter getErrout()
    {
        return errout;
    }

    public void setErrout(PrintWriter errout)
    {
        this.errout = errout;
    }

    /**
     * Spaces - default indentation
     * @see Configuration#spaces
     */
    public void setSpaces(int spaces)
    {
        configuration.spaces = spaces;
    }

    public int getSpaces()
    {
        return configuration.spaces;
    }

    /**
     * Wraplen - default wrap margin
     * @see Configuration#wraplen
     */
    public void setWraplen(int wraplen)
    {
        configuration.wraplen = wraplen;
    }

    public int getWraplen()
    {
        return configuration.wraplen;
    }

    /**
     * CharEncoding
     * @see Configuration#charEncoding
     */
    public void setCharEncoding(int charencoding)
    {
        configuration.charEncoding = charencoding;
    }

    public int getCharEncoding()
    {
        return configuration.charEncoding;
    }

    /**
     * Tabsize
     * @see Configuration#tabsize
     */
    public void setTabsize(int tabsize)
    {
        configuration.tabsize = tabsize;
    }

    public int getTabsize()
    {
        return configuration.tabsize;
    }

    /**
     * Errfile - file name to write errors to
     * @see Configuration#errfile
     */
    public void setErrfile(String errfile)
    {
        configuration.errfile = errfile;
    }

    public String getErrfile()
    {
        return configuration.errfile;
    }

    /**
     * Writeback - if true then output tidied markup NOTE: this property is ignored when parsing from an InputStream.
     * @see Configuration#writeback
     */
    public void setWriteback(boolean writeback)
    {
        configuration.writeback = writeback;
    }

    public boolean getWriteback()
    {
        return configuration.writeback;
    }

    /**
     * OnlyErrors - if true normal output is suppressed
     * @see Configuration#onlyErrors
     */
    public void setOnlyErrors(boolean onlyErrors)
    {
        configuration.onlyErrors = onlyErrors;
    }

    public boolean getOnlyErrors()
    {
        return configuration.onlyErrors;
    }

    /**
     * ShowWarnings - however errors are always shown
     * @see Configuration#showWarnings
     */
    public void setShowWarnings(boolean showWarnings)
    {
        configuration.showWarnings = showWarnings;
    }

    public boolean getShowWarnings()
    {
        return configuration.showWarnings;
    }

    /**
     * Quiet - no 'Parsing X', guessed DTD or summary
     * @see Configuration#quiet
     */
    public void setQuiet(boolean quiet)
    {
        configuration.quiet = quiet;
    }

    public boolean getQuiet()
    {
        return configuration.quiet;
    }

    /**
     * IndentContent - indent content of appropriate tags
     * @see Configuration#indentContent
     */
    public void setIndentContent(boolean indentContent)
    {
        configuration.indentContent = indentContent;
    }

    public boolean getIndentContent()
    {
        return configuration.indentContent;
    }

    /**
     * SmartIndent - does text/block level content effect indentation
     * @see Configuration#smartIndent
     */
    public void setSmartIndent(boolean smartIndent)
    {
        configuration.smartIndent = smartIndent;
    }

    public boolean getSmartIndent()
    {
        return configuration.smartIndent;
    }

    /**
     * HideEndTags - suppress optional end tags
     * @see Configuration#hideEndTags
     */
    public void setHideEndTags(boolean hideEndTags)
    {
        configuration.hideEndTags = hideEndTags;
    }

    public boolean getHideEndTags()
    {
        return configuration.hideEndTags;
    }

    /**
     * XmlTags - treat input as XML
     * @see Configuration#xmlTags
     */
    public void setXmlTags(boolean xmlTags)
    {
        configuration.xmlTags = xmlTags;
    }

    public boolean getXmlTags()
    {
        return configuration.xmlTags;
    }

    /**
     * XmlOut - create output as XML
     * @see Configuration#xmlOut
     */
    public void setXmlOut(boolean xmlOut)
    {
        configuration.xmlOut = xmlOut;
    }

    public boolean getXmlOut()
    {
        return configuration.xmlOut;
    }

    /**
     * XHTML - output extensible HTML
     * @see Configuration#xHTML
     */
    public void setXHTML(boolean xHTML)
    {
        configuration.xHTML = xHTML;
    }

    public boolean getXHTML()
    {
        return configuration.xHTML;
    }

    /**
     * RawOut - avoid mapping values > 127 to entities
     * @see Configuration#rawOut
     */
    public void setRawOut(boolean rawOut)
    {
        configuration.rawOut = rawOut;
    }

    public boolean getRawOut()
    {
        return configuration.rawOut;
    }

    /**
     * UpperCaseTags - output tags in upper not lower case
     * @see Configuration#upperCaseTags
     */
    public void setUpperCaseTags(boolean upperCaseTags)
    {
        configuration.upperCaseTags = upperCaseTags;
    }

    public boolean getUpperCaseTags()
    {
        return configuration.upperCaseTags;
    }

    /**
     * UpperCaseAttrs - output attributes in upper not lower case
     * @see Configuration#upperCaseAttrs
     */
    public void setUpperCaseAttrs(boolean upperCaseAttrs)
    {
        configuration.upperCaseAttrs = upperCaseAttrs;
    }

    public boolean getUpperCaseAttrs()
    {
        return configuration.upperCaseAttrs;
    }

    /**
     * MakeClean - remove presentational clutter
     * @see Configuration#makeClean
     */
    public void setMakeClean(boolean makeClean)
    {
        configuration.makeClean = makeClean;
    }

    public boolean getMakeClean()
    {
        return configuration.makeClean;
    }

    /**
     * BreakBeforeBR - o/p newline before &lt;br&gt; or not?
     * @see Configuration#breakBeforeBR
     */
    public void setBreakBeforeBR(boolean breakBeforeBR)
    {
        configuration.breakBeforeBR = breakBeforeBR;
    }

    public boolean getBreakBeforeBR()
    {
        return configuration.breakBeforeBR;
    }

    /**
     * BurstSlides - create slides on each h2 element
     * @see Configuration#burstSlides
     */
    public void setBurstSlides(boolean burstSlides)
    {
        configuration.burstSlides = burstSlides;
    }

    public boolean getBurstSlides()
    {
        return configuration.burstSlides;
    }

    /**
     * NumEntities - use numeric entities
     * @see Configuration#numEntities
     */
    public void setNumEntities(boolean numEntities)
    {
        configuration.numEntities = numEntities;
    }

    public boolean getNumEntities()
    {
        return configuration.numEntities;
    }

    /**
     * QuoteMarks - output " marks as &amp;quot;
     * @see Configuration#quoteMarks
     */
    public void setQuoteMarks(boolean quoteMarks)
    {
        configuration.quoteMarks = quoteMarks;
    }

    public boolean getQuoteMarks()
    {
        return configuration.quoteMarks;
    }

    /**
     * QuoteNbsp - output non-breaking space as entity
     * @see Configuration#quoteNbsp
     */
    public void setQuoteNbsp(boolean quoteNbsp)
    {
        configuration.quoteNbsp = quoteNbsp;
    }

    public boolean getQuoteNbsp()
    {
        return configuration.quoteNbsp;
    }

    /**
     * QuoteAmpersand - output naked ampersand as &amp;
     * @see Configuration#quoteAmpersand
     */
    public void setQuoteAmpersand(boolean quoteAmpersand)
    {
        configuration.quoteAmpersand = quoteAmpersand;
    }

    public boolean getQuoteAmpersand()
    {
        return configuration.quoteAmpersand;
    }

    /**
     * WrapAttVals - wrap within attribute values
     * @see Configuration#wrapAttVals
     */
    public void setWrapAttVals(boolean wrapAttVals)
    {
        configuration.wrapAttVals = wrapAttVals;
    }

    public boolean getWrapAttVals()
    {
        return configuration.wrapAttVals;
    }

    /**
     * WrapScriptlets - wrap within JavaScript string literals
     * @see Configuration#wrapScriptlets
     */
    public void setWrapScriptlets(boolean wrapScriptlets)
    {
        configuration.wrapScriptlets = wrapScriptlets;
    }

    public boolean getWrapScriptlets()
    {
        return configuration.wrapScriptlets;
    }

    /**
     * WrapSection - wrap within &lt;![ ... ]&gt; section tags
     * @see Configuration#wrapSection
     */
    public void setWrapSection(boolean wrapSection)
    {
        configuration.wrapSection = wrapSection;
    }

    public boolean getWrapSection()
    {
        return configuration.wrapSection;
    }

    /**
     * AltText - default text for alt attribute
     * @see Configuration#altText
     */
    public void setAltText(String altText)
    {
        configuration.altText = altText;
    }

    public String getAltText()
    {
        return configuration.altText;
    }

    /**
     * Slidestyle - style sheet for slides
     * @see Configuration#slidestyle
     */
    public void setSlidestyle(String slidestyle)
    {
        configuration.slidestyle = slidestyle;
    }

    public String getSlidestyle()
    {
        return configuration.slidestyle;
    }

    /**
     * XmlPi - add &lt;?xml?&gt; for XML docs
     * @see Configuration#xmlPi
     */
    public void setXmlPi(boolean xmlPi)
    {
        configuration.xmlPi = xmlPi;
    }

    public boolean getXmlPi()
    {
        return configuration.xmlPi;
    }

    /**
     * DropFontTags - discard presentation tags
     * @see Configuration#dropFontTags
     */
    public void setDropFontTags(boolean dropFontTags)
    {
        configuration.dropFontTags = dropFontTags;
    }

    public boolean getDropFontTags()
    {
        return configuration.dropFontTags;
    }

    /**
     * drop-proprietary-attributes - discard proprietary attributes.
     * @see Configuration#dropProprietaryAttributes
     */
    public void setDropProprietaryAttributes(boolean dropProprietaryAttributes)
    {
        configuration.dropProprietaryAttributes = dropProprietaryAttributes;
    }

    public boolean getDropProprietaryAttributes()
    {
        return configuration.dropProprietaryAttributes;
    }

    /**
     * DropEmptyParas - discard empty p elements
     * @see Configuration#dropEmptyParas
     */
    public void setDropEmptyParas(boolean dropEmptyParas)
    {
        configuration.dropEmptyParas = dropEmptyParas;
    }

    public boolean getDropEmptyParas()
    {
        return configuration.dropEmptyParas;
    }

    /**
     * FixComments - fix comments with adjacent hyphens
     * @see Configuration#fixComments
     */
    public void setFixComments(boolean fixComments)
    {
        configuration.fixComments = fixComments;
    }

    public boolean getFixComments()
    {
        return configuration.fixComments;
    }

    /**
     * WrapAsp - wrap within ASP pseudo elements
     * @see Configuration#wrapAsp
     */
    public void setWrapAsp(boolean wrapAsp)
    {
        configuration.wrapAsp = wrapAsp;
    }

    public boolean getWrapAsp()
    {
        return configuration.wrapAsp;
    }

    /**
     * WrapJste - wrap within JSTE pseudo elements
     * @see Configuration#wrapJste
     */
    public void setWrapJste(boolean wrapJste)
    {
        configuration.wrapJste = wrapJste;
    }

    public boolean getWrapJste()
    {
        return configuration.wrapJste;
    }

    /**
     * WrapPhp - wrap within PHP pseudo elements
     * @see Configuration#wrapPhp
     */
    public void setWrapPhp(boolean wrapPhp)
    {
        configuration.wrapPhp = wrapPhp;
    }

    public boolean getWrapPhp()
    {
        return configuration.wrapPhp;
    }

    /**
     * FixBackslash - fix URLs by replacing \ with /
     * @see Configuration#fixBackslash
     */
    public void setFixBackslash(boolean fixBackslash)
    {
        configuration.fixBackslash = fixBackslash;
    }

    public boolean getFixBackslash()
    {
        return configuration.fixBackslash;
    }

    /**
     * IndentAttributes - newline+indent before each attribute
     * @see Configuration#indentAttributes
     */
    public void setIndentAttributes(boolean indentAttributes)
    {
        configuration.indentAttributes = indentAttributes;
    }

    public boolean getIndentAttributes()
    {
        return configuration.indentAttributes;
    }

    /**
     * DocType - user specified doctype omit | auto | strict | loose | <i>fpi </i> where the <i>fpi </i> is a string
     * similar to &quot;-//ACME//DTD HTML 3.14159//EN&quot; Note: for <i>fpi </i> include the double-quotes in the
     * string.
     * @see Configuration#docTypeStr
     * @see Configuration#docTypeMode
     */
    public void setDocType(String doctype)
    {
        if (doctype != null)
        {
            configuration.docTypeStr = configuration.parseDocType(doctype, "doctype");
        }
    }

    public String getDocType()
    {
        String result = null;
        switch (configuration.docTypeMode)
        {
            case Configuration.DOCTYPE_OMIT :
                result = "omit";
                break;
            case Configuration.DOCTYPE_AUTO :
                result = "auto";
                break;
            case Configuration.DOCTYPE_STRICT :
                result = "strict";
                break;
            case Configuration.DOCTYPE_LOOSE :
                result = "loose";
                break;
            case Configuration.DOCTYPE_USER :
                result = configuration.docTypeStr;
                break;
        }
        return result;
    }

    /**
     * LogicalEmphasis - replace i by em and b by strong
     * @see Configuration#logicalEmphasis
     */
    public void setLogicalEmphasis(boolean logicalEmphasis)
    {
        configuration.logicalEmphasis = logicalEmphasis;
    }

    public boolean getLogicalEmphasis()
    {
        return configuration.logicalEmphasis;
    }

    /**
     * XmlPIs - if set to true PIs must end with ?>
     * @see Configuration#xmlPIs
     */

    public void setXmlPIs(boolean xmlPIs)
    {
        configuration.xmlPIs = xmlPIs;
    }

    public boolean getXmlPIs()
    {
        return configuration.xmlPIs;
    }

    /**
     * EncloseText - if true text at body is wrapped in &lt;p&gt;'s
     * @see Configuration#encloseBodyText
     */
    public void setEncloseText(boolean encloseText)
    {
        configuration.encloseBodyText = encloseText;
    }

    public boolean getEncloseText()
    {
        return configuration.encloseBodyText;
    }

    /**
     * EncloseBlockText - if true text in blocks is wrapped in &lt;p&gt;'s
     * @see Configuration#encloseBlockText
     */
    public void setEncloseBlockText(boolean encloseBlockText)
    {
        configuration.encloseBlockText = encloseBlockText;
    }

    public boolean getEncloseBlockText()
    {
        return configuration.encloseBlockText;
    }

    /**
     * KeepFileTimes - if true last modified time is preserved <strong>this is NOT supported at this time. </strong>
     * @see Configuration#keepFileTimes
     */
    public void setKeepFileTimes(boolean keepFileTimes)
    {
        configuration.keepFileTimes = keepFileTimes;
    }

    public boolean getKeepFileTimes()
    {
        return configuration.keepFileTimes;
    }

    /**
     * Word2000 - draconian cleaning for Word2000
     * @see Configuration#word2000
     */
    public void setWord2000(boolean word2000)
    {
        configuration.word2000 = word2000;
    }

    public boolean getWord2000()
    {
        return configuration.word2000;
    }

    /**
     * TidyMark - add meta element indicating tidied doc
     * @see Configuration#tidyMark
     */
    public void setTidyMark(boolean tidyMark)
    {
        configuration.tidyMark = tidyMark;
    }

    public boolean getTidyMark()
    {
        return configuration.tidyMark;
    }

    /**
     * XmlSpace - if set to yes adds xml:space attr as needed
     * @see Configuration#xmlSpace
     */
    public void setXmlSpace(boolean xmlSpace)
    {
        configuration.xmlSpace = xmlSpace;
    }

    public boolean getXmlSpace()
    {
        return configuration.xmlSpace;
    }

    /**
     * Emacs - if true format error output for GNU Emacs
     * @see Configuration#emacs
     */
    public void setEmacs(boolean emacs)
    {
        configuration.emacs = emacs;
    }

    public boolean getEmacs()
    {
        return configuration.emacs;
    }

    /**
     * LiteralAttribs - if true attributes may use newlines
     * @see Configuration#literalAttribs
     */
    public void setLiteralAttribs(boolean literalAttribs)
    {
        configuration.literalAttribs = literalAttribs;
    }

    public boolean getLiteralAttribs()
    {
        return configuration.literalAttribs;
    }

    /**
     * print-body-only - output BODY content only.
     * @param bodyOnly true = print only the document body
     * @see Configuration#bodyOnly
     */
    public void setPrintBodyOnly(boolean bodyOnly)
    {
        configuration.bodyOnly = bodyOnly;
    }

    /**
     * print-body-only - output BODY content only.
     * @return true if tidy will print only the document body
     */
    public boolean getPrintBodyOnly()
    {
        return configuration.bodyOnly;
    }

    /**
     * InputStreamName - the name of the input stream (printed in the header information).
     */
    public void setInputStreamName(String name)
    {
        if (name != null)
        {
            inputStreamName = name;
        }
    }

    public String getInputStreamName()
    {
        return inputStreamName;
    }

    /**
     * Sets the configuration from a configuration file.
     */
    public void setConfigurationFromFile(String filename)
    {
        configuration.parseFile(filename);
    }

    /**
     * Sets the configuration from a properties object.
     */
    public void setConfigurationFromProps(Properties props)
    {
        configuration.addProps(props);
    }

    /**
     * Parses InputStream in and returns the root Node. If out is non-null, pretty prints to OutputStream out.
     */
    public Node parse(InputStream in, OutputStream out)
    {
        Node document = null;

        try
        {
            document = parse(in, null, out);
        }
        catch (FileNotFoundException fnfe)
        {
        }
        catch (IOException e)
        {
        }

        return document;
    }

    /**
     * Internal routine that actually does the parsing. The caller can pass either an InputStream or file name. If both
     * are passed, the file name is preferred.
     */
    private Node parse(InputStream in, String file, OutputStream out) throws FileNotFoundException, IOException
    {
        Lexer lexer;
        Node document = null;
        Node doctype;
        Out o = new OutImpl(); // normal output stream
        PPrint pprint;

        if (!initialized)
        {
            return null;
        }

        if (errout == null)
        {
            return null;
        }

        parseErrors = 0;
        parseWarnings = 0;

        // ensure config is self-consistent
        configuration.adjust();

        if (file != null)
        {
            in = new FileInputStream(file);
            inputStreamName = file;
        }
        else if (in == null)
        {
            in = System.in;
            inputStreamName = "stdin";
        }

        if (in != null)
        {
            lexer = new Lexer(
                new StreamInImpl(in, configuration.charEncoding, configuration.tabsize),
                configuration,
                this.report);
            lexer.errout = errout;

            // store pointer to lexer in input stream to allow character encoding errors to be reported
            lexer.in.lexer = lexer;

            this.report.setFilename(file); // #431895 - fix by Dave Bryan 04 Jan 01

            // Tidy doesn't alter the doctype for generic XML docs
            if (configuration.xmlTags)
            {
                document = ParserImpl.parseXMLDocument(lexer);
            }
            else
            {
                lexer.warnings = 0;
                if (!configuration.quiet)
                {
                    this.report.helloMessage(errout, this.report.RELEASE_DATE, inputStreamName);
                }

                document = ParserImpl.parseDocument(lexer);

                if (!document.checkNodeIntegrity())
                {
                    this.report.badTree(errout);
                    return null;
                }

                Clean cleaner = new Clean(configuration.tt);

                // simplifies <b><b> ... </b> ... </b> etc.
                cleaner.nestedEmphasis(document);

                // cleans up <dir> indented text </dir> etc.
                cleaner.list2BQ(document);
                cleaner.bQ2Div(document);

                // replaces i by em and b by strong
                if (configuration.logicalEmphasis)
                {
                    cleaner.emFromI(document);
                }

                if (configuration.word2000 && cleaner.isWord2000(document, configuration.tt))
                {
                    // prune Word2000's <![if ...]> ... <![endif]>
                    cleaner.dropSections(lexer, document);

                    // drop style & class attributes and empty p, span elements
                    cleaner.cleanWord2000(lexer, document);
                }

                // replaces presentational markup by style rules
                if (configuration.makeClean || configuration.dropFontTags)
                {
                    cleaner.cleanTree(lexer, document);
                }

                if (!document.checkNodeIntegrity())
                {
                    this.report.badTree(errout);
                    return null;
                }
                doctype = document.findDocType();
                if (document.content != null)
                {
                    this.report.reportVersion(errout, lexer, inputStreamName, doctype);

                    if (configuration.xHTML)
                    {
                        lexer.setXHTMLDocType(document);
                    }
                    else
                    {
                        lexer.fixDocType(document);
                    }

                    if (configuration.tidyMark)
                    {
                        lexer.addGenerator(document);
                    }
                }

                // ensure presence of initial <?XML version="1.0"?>
                if (configuration.xmlOut && configuration.xmlPi)
                {
                    lexer.fixXMLPI(document);
                }

                if (!configuration.quiet && document.content != null)
                {
                    // this.report.reportVersion(errout, lexer, inputStreamName, doctype);
                    this.report.reportNumWarnings(errout, lexer);
                }
            }

            parseWarnings = lexer.warnings;
            parseErrors = lexer.errors;

            // Try to close the InputStream but only if if we created it.

            if ((file != null) && (in != System.in))
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }

            if (lexer.errors > 0)
            {
                this.report.needsAuthorIntervention(errout);
            }

            o.state = StreamIn.FSM_ASCII;
            o.encoding = configuration.charEncoding;

            if (!configuration.onlyErrors && lexer.errors == 0)
            {
                if (configuration.burstSlides)
                {
                    Node body;

                    body = null;
                    // remove doctype to avoid potential clash with markup introduced when bursting into slides

                    // discard the document type
                    doctype = document.findDocType();

                    if (doctype != null)
                    {
                        Node.discardElement(doctype);
                    }

                    /* slides use transitional features */
                    lexer.versions |= Dict.VERS_HTML40_LOOSE;

                    // and patch up doctype to match
                    if (configuration.xHTML)
                    {
                        lexer.setXHTMLDocType(document);
                    }
                    else
                    {
                        lexer.fixDocType(document);
                    }

                    // find the body element which may be implicit
                    body = document.findBody(configuration.tt);

                    if (body != null)
                    {
                        pprint = new PPrint(configuration);
                        this.report.reportNumberOfSlides(errout, pprint.countSlides(body));
                        pprint.createSlides(lexer, document);
                    }
                    else
                    {
                        this.report.missingBody(errout);
                    }
                }
                else if (configuration.writeback && (file != null))
                {
                    try
                    {
                        pprint = new PPrint(configuration);
                        o.out = new FileOutputStream(file);

                        if (configuration.bodyOnly)
                        {
                            // Feature request #434940 - fix by Dave Raggett/Ignacio Vazquez-Abrams 21 Jun 01
                            pprint.PrintBody(o, lexer, document, configuration.xmlOut);
                        }
                        else if (configuration.xmlOut)
                        {
                            pprint.printXMLTree(o, (short) 0, 0, lexer, document);
                        }
                        else
                        {
                            pprint.printTree(o, (short) 0, 0, lexer, document);
                        }

                        pprint.flushLine(o, 0);
                        o.out.close();
                    }
                    catch (IOException e)
                    {
                        errout.println(file + e.toString());
                    }
                }
                else if (out != null)
                {
                    pprint = new PPrint(configuration);
                    o.out = out;

                    if (configuration.bodyOnly)
                    {
                        // Feature request #434940 - fix by Dave Raggett/Ignacio Vazquez-Abrams 21 Jun 01
                        pprint.PrintBody(o, lexer, document, configuration.xmlOut);
                    }
                    else if (configuration.xmlOut)
                    {
                        pprint.printXMLTree(o, (short) 0, 0, lexer, document);
                    }
                    else
                    {
                        pprint.printTree(o, (short) 0, 0, lexer, document);
                    }

                    pprint.flushLine(o, 0);
                }

            }

            this.report.errorSummary(lexer);
        }
        return document;
    }

    /**
     * Parses InputStream in and returns a DOM Document node. If out is non-null, pretty prints to OutputStream out.
     */
    public org.w3c.dom.Document parseDOM(InputStream in, OutputStream out)
    {
        Node document = parse(in, out);
        if (document != null)
        {
            return (org.w3c.dom.Document) document.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * Creates an empty DOM Document.
     * @return a new org.w3c.dom.Document
     */
    public static org.w3c.dom.Document createEmptyDocument()
    {
        Node document = new Node(Node.RootNode, new byte[0], 0, 0);
        Node node = new Node(Node.StartTag, new byte[0], 0, 0, "html", new TagTable());
        if (document != null && node != null)
        {
            Node.insertNodeAtStart(document, node);
            return (org.w3c.dom.Document) document.getAdapter();
        }
        else
        {
            return null;
        }
    }

    /**
     * Pretty-prints a DOM Document. Must be an instance of org.w3c.tidy.DOMDocumentImpl.
     * @param doc org.w3c.dom.Document
     * @param out output stream
     */
    public void pprint(org.w3c.dom.Document doc, OutputStream out)
    {
        if (!(doc instanceof DOMDocumentImpl))
        {
            // @todo should we inform users that tidy can't print a generic Document or change the method signature?
            return;
        }

        pprint(((DOMDocumentImpl) doc).adaptee, out);
    }

    /**
     * Pretty-prints a DOM Node.
     * @param node org.w3c.dom.Node. Must be an instance of org.w3c.tidy.DOMNodeImpl.
     * @param out output stream
     */
    public void pprint(org.w3c.dom.Node node, OutputStream out)
    {
        if (!(node instanceof DOMNodeImpl))
        {
            // @todo should we inform users than tidy can't print a generic Node or change the method signature?
            return;
        }

        pprint(((DOMNodeImpl) node).adaptee, out);
    }

    /**
     * Pretty-prints a tidy Node.
     * @param node org.w3c.tidy.Node
     * @param out output stream
     */
    private void pprint(Node node, OutputStream out)
    {
        Out o = new OutImpl();
        PPrint pprint;

        o.state = StreamIn.FSM_ASCII;
        o.encoding = configuration.charEncoding;

        if (out != null)
        {
            pprint = new PPrint(configuration);
            o.out = out;

            if (configuration.xmlTags)
            {
                pprint.printXMLTree(o, (short) 0, 0, null, node);
            }
            else
            {
                pprint.printTree(o, (short) 0, 0, null, node);
            }

            pprint.flushLine(o, 0);
        }
    }

    /**
     * Command line interface to parser and pretty printer.
     * @param argv command line parameters
     */
    public static void main(String[] argv)
    {
        Tidy tidy = new Tidy();
        int returnCode = tidy.mainExec(argv);
        System.exit(returnCode);
    }

    /**
     * Main method, but returns the return code as an int instead of calling System.exit(code). Needed for testing main
     * method without shutting down tests.
     * @param argv command line parameters
     * @return return code
     */
    protected int mainExec(String[] argv)
    {
        int totalerrors = 0;
        int totalwarnings = 0;
        String file;
        String prog = "Tidy";
        String s;
        int argc = argv.length + 1;
        int argIndex = 0;
        String arg;
        String current_errorfile = "stderr";


        // read command line

        while (argc > 0)
        {
            if (argc > 1 && argv[argIndex].startsWith("-"))
            {
                // support -foo and --foo
                arg = argv[argIndex].substring(1);

                if (arg.length() > 0 && arg.charAt(0) == '-')
                {
                    arg = arg.substring(1);
                }

                if (arg.equals("xml"))
                {
                    configuration.xmlTags = true;
                }
                else if (arg.equals("asxml") || arg.equals("asxhtml"))
                {
                    configuration.xHTML = true;
                }
                else if (arg.equals("indent"))
                {
                    configuration.indentContent = true;
                    configuration.smartIndent = true;
                }
                else if (arg.equals("omit"))
                {
                    configuration.hideEndTags = true;
                }
                else if (arg.equals("upper"))
                {
                    configuration.upperCaseTags = true;
                }
                else if (arg.equals("clean"))
                {
                    configuration.makeClean = true;
                }
                else if (arg.equals("raw"))
                {
                    configuration.charEncoding = Configuration.RAW;
                }
                else if (arg.equals("ascii"))
                {
                    configuration.charEncoding = Configuration.ASCII;
                }
                else if (arg.equals("latin1"))
                {
                    configuration.charEncoding = Configuration.LATIN1;
                }
                else if (arg.equals("utf8"))
                {
                    configuration.charEncoding = Configuration.UTF8;
                }
                else if (arg.equals("iso2022"))
                {
                    configuration.charEncoding = Configuration.ISO2022;
                }
                else if (arg.equals("mac"))
                {
                    configuration.charEncoding = Configuration.MACROMAN;
                }
                else if (arg.equals("numeric"))
                {
                    configuration.numEntities = true;
                }
                else if (arg.equals("modify"))
                {
                    configuration.writeback = true;
                }
                else if (arg.equals("change")) // obsolete
                {
                    configuration.writeback = true;
                }
                else if (arg.equals("update")) // obsolete
                {
                    configuration.writeback = true;
                }
                else if (arg.equals("errors"))
                {
                    configuration.onlyErrors = true;
                }
                else if (arg.equals("quiet"))
                {
                    configuration.quiet = true;
                }
                else if (arg.equals("slides"))
                {
                    configuration.burstSlides = true;
                }
                else if (arg.equals("help") || argv[argIndex].charAt(1) == '?' || argv[argIndex].charAt(1) == 'h')
                {
                    this.report.helpText(new PrintWriter(System.out, true), prog);
                    return 1;
                }
                else if (arg.equals("config"))
                {
                    if (argc >= 3)
                    {
                        configuration.parseFile(argv[argIndex + 1]);
                        --argc;
                        ++argIndex;
                    }
                }
                else if (argv[argIndex].equals("-file") || argv[argIndex].equals("--file")
                    || argv[argIndex].equals("-f"))
                {
                    if (argc >= 3)
                    {
                        configuration.errfile = argv[argIndex + 1];
                        --argc;
                        ++argIndex;
                    }
                }
                else if (argv[argIndex].equals("-wrap") || argv[argIndex].equals("--wrap")
                    || argv[argIndex].equals("-w"))
                {
                    if (argc >= 3)
                    {
                        configuration.wraplen = Integer.parseInt(argv[argIndex + 1]);
                        --argc;
                        ++argIndex;
                    }
                }
                else if (argv[argIndex].equals("-version") || argv[argIndex].equals("--version")
                    || argv[argIndex].equals("-v"))
                {
                    this.report.showVersion(errout);
                    return 0;
                }
                else
                {
                    s = argv[argIndex];

                    for (int i = 1; i < s.length(); i++)
                    {
                        if (s.charAt(i) == 'i')
                        {
                            configuration.indentContent = true;
                            configuration.smartIndent = true;
                        }
                        else if (s.charAt(i) == 'o')
                        {
                            configuration.hideEndTags = true;
                        }
                        else if (s.charAt(i) == 'u')
                        {
                            configuration.upperCaseTags = true;
                        }
                        else if (s.charAt(i) == 'c')
                        {
                            configuration.makeClean = true;
                        }
                        else if (s.charAt(i) == 'n')
                        {
                            configuration.numEntities = true;
                        }
                        else if (s.charAt(i) == 'm')
                        {
                            configuration.writeback = true;
                        }
                        else if (s.charAt(i) == 'e')
                        {
                            configuration.onlyErrors = true;
                        }
                        else if (s.charAt(i) == 'q')
                        {
                            configuration.quiet = true;
                        }
                        else
                        {
                            this.report.unknownOption(this.errout, s.charAt(i));
                        }
                    }
                }

                --argc;
                ++argIndex;
                continue;
            }

            // ensure config is self-consistent
            configuration.adjust();

            // user specified error file
            if (configuration.errfile != null)
            {
                // is it same as the currently opened file?
                if (!configuration.errfile.equals(current_errorfile))
                {
                    // no so close previous error file

                    if (this.errout != this.stderr)
                    {
                        this.errout.close();
                    }

                    // and try to open the new error file
                    try
                    {
                        this.setErrout(new PrintWriter(new FileWriter(configuration.errfile), true));
                        current_errorfile = configuration.errfile;
                    }
                    catch (IOException e)
                    {
                        // can't be opened so fall back to stderr
                        current_errorfile = "stderr";
                        this.setErrout(stderr);
                    }
                }
            }

            if (argc > 1)
            {
                file = argv[argIndex];
            }
            else
            {
                file = "stdin";
            }

            try
            {
                parse(null, file, System.out);
                totalwarnings += this.parseWarnings;
                totalerrors += this.parseErrors;
            }
            catch (FileNotFoundException fnfe)
            {
                this.report.unknownFile(this.errout, prog, file);
            }
            catch (IOException ioe)
            {
                this.report.unknownFile(this.errout, prog, file);
            }

            --argc;
            ++argIndex;

            if (argc <= 1)
            {
                break;
            }
        }

        if (totalerrors + totalwarnings > 0)
        {
            this.report.generalInfo(this.errout);
        }

        if (this.errout != this.stderr)
        {
            this.errout.close();
        }

        // return status can be used by scripts

        if (totalerrors > 0)
        {
            return 2;
        }

        if (totalwarnings > 0)
        {
            return 1;
        }

        // 0 means all is ok
        return 0;
    }
}