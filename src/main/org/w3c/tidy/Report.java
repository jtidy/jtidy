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
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Error/informational message reporter. You should only need to edit the file TidyMessages.properties to localize HTML
 * tidy.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision $ ($Author $)
 */
public final class Report
{

    /**
     * used to point to Web Accessibility Guidelines.
     */
    public static final String ACCESS_URL = "http://www.w3.org/WAI/GL";

    /**
     * Release date String.
     */
    public static final String RELEASE_DATE = "4th August 2000";

    /**
     * invalid entity: missing semicolon.
     */
    public static final short MISSING_SEMICOLON = 1;

    /**
     * invalid entity: missing semicolon.
     */
    public static final short MISSING_SEMICOLON_NCR = 2;

    /**
     * invalid entity: unknown entity.
     */
    public static final short UNKNOWN_ENTITY = 3;

    /**
     * invalid entity: unescaped ampersand.
     */
    public static final short UNESCAPED_AMPERSAND = 4;

    /**
     * invalid entity: apos undefined in current definition.
     */
    public static final short APOS_UNDEFINED = 5;

    /**
     * missing an end tag.
     */
    public static final short MISSING_ENDTAG_FOR = 6;

    /**
     * missing end tag before.
     */
    public static final short MISSING_ENDTAG_BEFORE = 7;

    /**
     * discarding unexpected element.
     */
    public static final short DISCARDING_UNEXPECTED = 8;

    /**
     * nested emphasis.
     */
    public static final short NESTED_EMPHASIS = 9;

    /**
     * non matching end tag.
     */
    public static final short NON_MATCHING_ENDTAG = 10;

    /**
     * tag not allowed in.
     */
    public static final short TAG_NOT_ALLOWED_IN = 11;

    /**
     * missing start tag.
     */
    public static final short MISSING_STARTTAG = 12;

    /**
     * unexpected end tag.
     */
    public static final short UNEXPECTED_ENDTAG = 13;

    /**
     * unsing br in place of.
     */
    public static final short USING_BR_INPLACE_OF = 14;

    /**
     * inserting tag.
     */
    public static final short INSERTING_TAG = 15;

    /**
     * suspected missing quote.
     */
    public static final short SUSPECTED_MISSING_QUOTE = 16;

    /**
     * missing title element.
     */
    public static final short MISSING_TITLE_ELEMENT = 17;

    /**
     * duplicate frameset.
     */
    public static final short DUPLICATE_FRAMESET = 18;

    /**
     * elments can be nested.
     */
    public static final short CANT_BE_NESTED = 19;

    /**
     * obsolete element.
     */
    public static final short OBSOLETE_ELEMENT = 20;

    /**
     * proprietary element.
     */
    public static final short PROPRIETARY_ELEMENT = 21;

    /**
     * unknown element.
     */
    public static final short UNKNOWN_ELEMENT = 22;

    /**
     * trim empty element.
     */
    public static final short TRIM_EMPTY_ELEMENT = 23;

    /**
     * coerce to end tag.
     */
    public static final short COERCE_TO_ENDTAG = 24;

    /**
     * illegal nesting.
     */
    public static final short ILLEGAL_NESTING = 25;

    /**
     * noframes content.
     */
    public static final short NOFRAMES_CONTENT = 26;

    /**
     * content after body.
     */
    public static final short CONTENT_AFTER_BODY = 27;

    /**
     * inconsistent version.
     */
    public static final short INCONSISTENT_VERSION = 28;

    /**
     * malformed comment.
     */
    public static final short MALFORMED_COMMENT = 29;

    /**
     * bad coment chars.
     */
    public static final short BAD_COMMENT_CHARS = 30;

    /**
     * bad xml comment.
     */
    public static final short BAD_XML_COMMENT = 31;

    /**
     * bad cdata comment.
     */
    public static final short BAD_CDATA_CONTENT = 32;

    /**
     * inconsistent namespace.
     */
    public static final short INCONSISTENT_NAMESPACE = 33;

    /**
     * doctype after tags.
     */
    public static final short DOCTYPE_AFTER_TAGS = 34;

    /**
     * malformed doctype.
     */
    public static final short MALFORMED_DOCTYPE = 35;

    /**
     * unexpected end of file.
     */
    public static final short UNEXPECTED_END_OF_FILE = 36;

    /**
     * doctype not upper case.
     */
    public static final short DTYPE_NOT_UPPER_CASE = 37;

    /**
     * too many element.
     */
    public static final short TOO_MANY_ELEMENTS = 38;

    /**
     * unescaped element.
     */
    public static final short UNESCAPED_ELEMENT = 39;

    /**
     * nested quotation.
     */
    public static final short NESTED_QUOTATION = 40;

    /**
     * element not empty.
     */
    public static final short ELEMENT_NOT_EMPTY = 41;

    /**
     * encoding IO conflict.
     */
    public static final short ENCODING_IO_CONFLICT = 42;

    /**
     * mixed content in block.
     */
    public static final short MIXED_CONTENT_IN_BLOCK = 43;

    /**
     * missing doctype.
     */
    public static final short MISSING_DOCTYPE = 44;

    /**
     * space preceding xml declaration.
     */
    public static final short SPACE_PRECEDING_XMLDECL = 45;

    /**
     * too many elements in.
     */
    public static final short TOO_MANY_ELEMENTS_IN = 46;

    /**
     * unexpected endag in.
     */
    public static final short UNEXPECTED_ENDTAG_IN = 47;

    /**
     * replacing element.
     */
    public static final short REPLACING_ELEMENT = 83;

    /**
     * replacing unexcaped element.
     */
    public static final short REPLACING_UNEX_ELEMENT = 84;

    /**
     * coerce to endtag.
     */
    public static final short COERCE_TO_ENDTAG_WARN = 85;

    /**
     * attribute: unknown attribute.
     */
    public static final short UNKNOWN_ATTRIBUTE = 48;

    /**
     * attribute: missing attribute.
     */
    public static final short MISSING_ATTRIBUTE = 49;

    /**
     * attribute: missing attribute value.
     */
    public static final short MISSING_ATTR_VALUE = 50;

    /**
     * attribute: bad attribute value.
     */
    public static final short BAD_ATTRIBUTE_VALUE = 51;

    /**
     * attribute: unexpected gt.
     */
    public static final short UNEXPECTED_GT = 52;

    /**
     * attribute: proprietary attribute.
     */
    public static final short PROPRIETARY_ATTRIBUTE = 53;

    /**
     * attribute: proprietary attribute value.
     */
    public static final short PROPRIETARY_ATTR_VALUE = 54;

    /**
     * attribute: repeated attribute.
     */
    public static final short REPEATED_ATTRIBUTE = 55;

    /**
     * attribute: missing image map.
     */
    public static final short MISSING_IMAGEMAP = 56;

    /**
     * attribute: xml attribute value.
     */
    public static final short XML_ATTRIBUTE_VALUE = 57;

    /**
     * attribute: missing quotemark.
     */
    public static final short MISSING_QUOTEMARK = 58;

    /**
     * attribute: unexpected quotemark.
     */
    public static final short UNEXPECTED_QUOTEMARK = 59;

    /**
     * attribute: id and name mismatch.
     */
    public static final short ID_NAME_MISMATCH = 60;

    /**
     * attribute: backslash in URI.
     */
    public static final short BACKSLASH_IN_URI = 61;

    /**
     * attribute: fixed backslash.
     */
    public static final short FIXED_BACKSLASH = 62;

    /**
     * attribute: illegal URI reference.
     */
    public static final short ILLEGAL_URI_REFERENCE = 63;

    /**
     * attribute: escaped illegal URI.
     */
    public static final short ESCAPED_ILLEGAL_URI = 64;

    /**
     * attribute: newline in URI.
     */
    public static final short NEWLINE_IN_URI = 65;

    /**
     * attribute: anchor not unique.
     */
    public static final short ANCHOR_NOT_UNIQUE = 66;

    /**
     * attribute: joining attribute.
     */
    public static final short JOINING_ATTRIBUTE = 68;

    /**
     * attribute: expected equalsign.
     */
    public static final short UNEXPECTED_EQUALSIGN = 69;

    /**
     * attribute: attribute value not lower case.
     */
    public static final short ATTR_VALUE_NOT_LCASE = 70;

    /**
     * attribute: id sintax.
     */
    public static final short ID_SYNTAX = 71;

    /**
     * attribute: invalid attribute.
     */
    public static final short INVALID_ATTRIBUTE = 72;

    /**
     * attribute: bad attribute value replaced.
     */
    public static final short BAD_ATTRIBUTE_VALUE_REPLACED = 73;

    /**
     * attribute: invalid xml id.
     */
    public static final short INVALID_XML_ID = 74;

    /**
     * attribute: unexpected end of file.
     */
    public static final short UNEXPECTED_END_OF_FILE_ATTR = 75;

    /**
     * character encoding: vendor specific chars.
     */
    public static final short VENDOR_SPECIFIC_CHARS = 76;

    /**
     * character encoding: invalid sgml chars.
     */
    public static final short INVALID_SGML_CHARS = 77;

    /**
     * character encoding: invalid utf8.
     */
    public static final short INVALID_UTF8 = 78;

    /**
     * character encoding: invalid utf16.
     */
    public static final short INVALID_UTF16 = 79;

    /**
     * character encoding: encoding mismatch.
     */
    public static final short ENCODING_MISMATCH = 80;

    /**
     * character encoding: nvalid URI.
     */
    public static final short INVALID_URI = 81;

    /**
     * character encoding: invalid NCR.
     */
    public static final short INVALID_NCR = 82;

    /**
     * accessibility flaw: missing image map.
     */
    public static final short MISSING_IMAGE_ALT = 1;

    /**
     * accessibility flaw: missing link alt.
     */
    public static final short MISSING_LINK_ALT = 2;

    /**
     * accessibility flaw: missing summary.
     */
    public static final short MISSING_SUMMARY = 4;

    /**
     * accessibility flaw: missing image map.
     */
    public static final short MISSING_IMAGE_MAP = 8;

    /**
     * accessibility flaw: using frames.
     */
    public static final short USING_FRAMES = 16;

    /**
     * accessibility flaw: using noframes.
     */
    public static final short USING_NOFRAMES = 32;

    /**
     * presentation flaw: using spacer.
     */
    public static final short USING_SPACER = 1;

    /**
     * presentation flaw: using layer.
     */
    public static final short USING_LAYER = 2;

    /**
     * presentation flaw: using nobr.
     */
    public static final short USING_NOBR = 4;

    /**
     * presentation flaw: using font.
     */
    public static final short USING_FONT = 8;

    /**
     * presentation flaw: using body.
     */
    public static final short USING_BODY = 16;

    /**
     * character encoding error: windows chars.
     */
    public static final short WINDOWS_CHARS = 1;

    /**
     * character encoding error: non ascii.
     */
    public static final short NON_ASCII = 2;

    /**
     * character encoding error: found utf16.
     */
    public static final short FOUND_UTF16 = 4;

    /**
     * Resource bundle with messages.
     */
    private static ResourceBundle res;

    /**
     * Printed in GNU Emacs messages.
     */
    private String currentFile;

    static
    {
        try
        {
            res = ResourceBundle.getBundle("org/w3c/tidy/TidyMessages");
        }
        catch (MissingResourceException e)
        {
            throw new Error(e.toString());
        }
    }

    /**
     * Instantiated only in Tidy() constructor.
     */
    protected Report()
    {
    }

    /**
     * Generates a complete message for the warning/error. The message is composed by:
     * <ul>
     * <li>position in file</li>
     * <li>prefix for the error level (warning: | error:)</li>
     * <li>message read from ResourceBundle</li>
     * <li>optional parameters added to message using MessageFormat</li>
     * </ul>
     * @param lexer Lexer
     * @param message key for the ResourceBundle
     * @param params optional parameters added with MessageFormat
     * @param level message level. One of <code>TidyMessage.LEVEL_ERROR</code>,
     * <code>TidyMessage.LEVEL_WARNING</code>,<code>TidyMessage.LEVEL_INFO</code>
     * @return formatted message
     * @throws MissingResourceException if <code>message</code> key is not available in jtidy resource bundle.
     * @see TidyMessage
     */
    protected String getMessage(Lexer lexer, String message, Object[] params, short level)
        throws MissingResourceException
    {
        String resource;
        resource = res.getString(message);

        String position;

        if (lexer != null && level != TidyMessage.Level.SUMMARY)
        {
            position = getPosition(lexer);
        }
        else
        {
            position = "";
        }

        String prefix;

        switch (level)
        {
            case TidyMessage.Level.ERROR :
                prefix = res.getString("error");
                break;
            case TidyMessage.Level.WARNING :
                prefix = res.getString("warning");
                break;
            default :
                prefix = "";
                break;
        }

        if (params != null)
        {
            return position + prefix + MessageFormat.format(resource, params);
        }
        else
        {
            return position + prefix + resource;
        }
    }

    /**
     * Prints a message to lexer.errout after calling getMessage().
     * @param lexer Lexer
     * @param message key for the ResourceBundle
     * @param params optional parameters added with MessageFormat
     * @param level message level. One of <code>TidyMessage.LEVEL_ERROR</code>,
     * <code>TidyMessage.LEVEL_WARNING</code>,<code>TidyMessage.LEVEL_INFO</code>
     * @see TidyMessage
     */
    private void printMessage(Lexer lexer, String message, Object[] params, short level)
    {
        String resource;
        try
        {
            resource = getMessage(lexer, message, params, level);
        }
        catch (MissingResourceException e)
        {
            lexer.errout.println(e.toString());
            return;
        }

        lexer.errout.println(resource);
    }

    /**
     * Prints a message to errout after calling getMessage(). Used when lexer is not yet defined.
     * @param errout PrintWriter
     * @param message key for the ResourceBundle
     * @param params optional parameters added with MessageFormat
     * @param level message level. One of <code>TidyMessage.LEVEL_ERROR</code>,
     * <code>TidyMessage.LEVEL_WARNING</code>,<code>TidyMessage.LEVEL_INFO</code>
     * @see TidyMessage
     */
    private void printMessage(PrintWriter errout, String message, Object[] params, short level)
    {
        String resource;
        try
        {
            resource = getMessage(null, message, params, level);
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
            return;
        }
        errout.println(resource);
    }

    /**
     * print version information.
     * @param p printWriter
     */
    public void showVersion(PrintWriter p)
    {
        printMessage(p, "version_summary", new Object[]{RELEASE_DATE}, TidyMessage.Level.SUMMARY);
    }

    /**
     * Returns a formatted tag name handling start and ent tags, nulls, doctypes, and text.
     * @param tag Node
     * @return formatted tag name
     */
    private String getTagName(Node tag)
    {
        if (tag != null)
        {
            if (tag.type == Node.StartTag)
            {
                return "<" + tag.element + ">";
            }
            else if (tag.type == Node.EndTag)
            {
                return "</" + tag.element + ">";
            }
            else if (tag.type == Node.DocTypeTag)
            {
                return "<!DOCTYPE>";
            }
            else if (tag.type == Node.TextNode)
            {
                return "plain text";
            }
            else
            {
                return tag.element;
            }
        }
        return "";
    }

    /**
     * Prints an "unknown option" error message. Lexer is not defined when this is called.
     * @param option unknown option name
     */
    public void unknownOption(String option)
    {
        try
        {
            System.err.println(MessageFormat.format(res.getString("unknown_option"), new Object[]{option}));
        }
        catch (MissingResourceException e)
        {
            System.err.println(e.toString());
        }
    }

    /**
     * Prints a "bad argument" error message. Lexer is not defined when this is called.
     * @param option bad argument value
     */
    public void badArgument(String option)
    {
        try
        {
            System.err.println(MessageFormat.format(res.getString("bad_argument"), new Object[]{option}));
        }
        catch (MissingResourceException e)
        {
            System.err.println(e.toString());
        }
    }

    /**
     * Returns a formatted String describing the current position in file.
     * @param lexer Lexer
     * @return String position ("line:column")
     */
    private String getPosition(Lexer lexer)
    {
        try
        {
            // Change formatting to be parsable by GNU Emacs
            if (lexer.configuration.emacs)
            {
                return MessageFormat.format(res.getString("emacs_format"), new Object[]{
                    this.currentFile,
                    new Integer(lexer.lines),
                    new Integer(lexer.columns)})
                    + " ";
            }
            else
            {
                // traditional format
                return MessageFormat.format(res.getString("line_column"), new Object[]{
                    new Integer(lexer.lines),
                    new Integer(lexer.columns)});
            }
        }
        catch (MissingResourceException e)
        {
            lexer.errout.println(e.toString());
        }
        return "";
    }

    /**
     * Prints encoding error messages.
     * @param lexer Lexer
     * @param code error code
     * @param c invalid char
     */
    public void encodingError(Lexer lexer, short code, int c)
    {
        lexer.warnings++;

        if (lexer.configuration.showWarnings)
        {
            if (code == WINDOWS_CHARS)
            {
                lexer.badChars |= WINDOWS_CHARS;
                printMessage(lexer, "illegal_char", new Object[]{new Integer(c)}, TidyMessage.Level.ERROR);
            }
        }
    }

    /**
     * Prints entity error messages.
     * @param lexer Lexer
     * @param code error code
     * @param entity invalid entity String
     * @param c invalid char
     */
    public void entityError(Lexer lexer, short code, String entity, int c)
    {
        lexer.warnings++;

        if (lexer.configuration.showWarnings)
        {
            if (code == MISSING_SEMICOLON)
            {
                printMessage(lexer, "missing_semicolon", new Object[]{entity}, TidyMessage.Level.WARNING);
            }
            else if (code == UNKNOWN_ENTITY)
            {
                printMessage(lexer, "unknown_entity", new Object[]{entity}, TidyMessage.Level.WARNING);
            }
            else if (code == UNESCAPED_AMPERSAND)
            {
                printMessage(lexer, "unescaped_ampersand", null, TidyMessage.Level.WARNING);
            }
        }
    }

    /**
     * Prints error messages for attributes.
     * @param lexer Lexer
     * @param node current tag
     * @param attribute attribute
     * @param code error code
     */
    public void attrError(Lexer lexer, Node node, AttVal attribute, short code)
    {
        lexer.warnings++;
        if (lexer.errors > 6) // keep quiet after 6 errors
        {
            return;
        }

        if (code == UNEXPECTED_GT) // error
        {
            printMessage(lexer, "unexpected_gt", new Object[]{getTagName(node)}, TidyMessage.Level.ERROR);
            lexer.errors++;
        }

        if (!lexer.configuration.showWarnings) // warnings
        {
            return;
        }

        switch (code)
        {
            case UNKNOWN_ATTRIBUTE :
                printMessage(lexer, "unknown_attribute", new Object[]{attribute.attribute}, TidyMessage.Level.WARNING);
                break;

            case MISSING_ATTRIBUTE :
                printMessage(
                    lexer,
                    "missing_attribute",
                    new Object[]{getTagName(node), attribute.attribute},
                    TidyMessage.Level.WARNING);
                break;

            case MISSING_ATTR_VALUE :
                printMessage(
                    lexer,
                    "missing_attr_value",
                    new Object[]{getTagName(node), attribute.attribute},
                    TidyMessage.Level.WARNING);
                break;

            case MISSING_IMAGEMAP :
                printMessage(lexer, "missing_imagemap", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                lexer.badAccess |= MISSING_IMAGE_MAP;
                break;

            case BAD_ATTRIBUTE_VALUE :
                printMessage(lexer, "bad_attribute_value", new Object[]{
                    getTagName(node),
                    attribute.attribute,
                    attribute.value}, TidyMessage.Level.WARNING);
                break;

            case XML_ATTRIBUTE_VALUE :
                printMessage(
                    lexer,
                    "xml_attribute_value",
                    new Object[]{getTagName(node), attribute.attribute},
                    TidyMessage.Level.WARNING);
                break;

            case UNEXPECTED_QUOTEMARK :
                printMessage(lexer, "unexpected_quotemark", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case MISSING_QUOTEMARK :
                printMessage(lexer, "missing_quotemark", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case REPEATED_ATTRIBUTE :
                printMessage(lexer, "repeated_attribute", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case PROPRIETARY_ATTR_VALUE :
                printMessage(
                    lexer,
                    "proprietary_attr_value",
                    new Object[]{getTagName(node), attribute.value},
                    TidyMessage.Level.WARNING);
                break;

            case PROPRIETARY_ATTRIBUTE :
                printMessage(
                    lexer,
                    "proprietary_attribute",
                    new Object[]{getTagName(node), attribute.attribute},
                    TidyMessage.Level.WARNING);
                break;

            case UNEXPECTED_END_OF_FILE :
                // on end of file adjust reported position to end of input
                lexer.lines = lexer.in.curline;
                lexer.columns = lexer.in.curcol;
                printMessage(lexer, "unexpected_end_of_file", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case ID_NAME_MISMATCH :
                printMessage(lexer, "id_name_mismatch", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case BACKSLASH_IN_URI :
                printMessage(lexer, "backslash_in_uri", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case FIXED_BACKSLASH :
                printMessage(lexer, "fixed_backslash", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case ILLEGAL_URI_REFERENCE :
                printMessage(lexer, "illegal_uri_reference", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case ESCAPED_ILLEGAL_URI :
                printMessage(lexer, "escaped_illegal_uri", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case NEWLINE_IN_URI :
                printMessage(lexer, "newline_in_uri", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case ANCHOR_NOT_UNIQUE :
                printMessage(
                    lexer,
                    "anchor_not_unique",
                    new Object[]{getTagName(node), attribute.value},
                    TidyMessage.Level.WARNING);
                break;

            case JOINING_ATTRIBUTE :
                printMessage(
                    lexer,
                    "joining_attribute",
                    new Object[]{getTagName(node), attribute.attribute},
                    TidyMessage.Level.WARNING);
                break;

            case UNEXPECTED_EQUALSIGN :
                printMessage(lexer, "expected_equalsign", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case ATTR_VALUE_NOT_LCASE :
                printMessage(lexer, "attr_value_not_lcase", new Object[]{
                    getTagName(node),
                    attribute.value,
                    attribute.attribute}, TidyMessage.Level.WARNING);
                break;

            default :
                break;
        }
    }

    /**
     * Prints warnings.
     * @param lexer Lexer
     * @param element parent/missing tag
     * @param node current tag
     * @param code error code
     */
    public void warning(Lexer lexer, Node element, Node node, short code)
    {

        TagTable tt = lexer.configuration.tt;

        lexer.warnings++;

        // keep quiet after 6 errors
        if (lexer.errors > 6 || !lexer.configuration.showWarnings)
        {
            return;
        }

        switch (code)
        {
            case MISSING_ENDTAG_FOR :
                printMessage(lexer, "missing_endtag_for", new Object[]{element.element}, TidyMessage.Level.WARNING);
                break;

            case MISSING_ENDTAG_BEFORE :
                printMessage(
                    lexer,
                    "missing_endtag_before",
                    new Object[]{element.element, getTagName(node)},
                    TidyMessage.Level.WARNING);
                break;

            case DISCARDING_UNEXPECTED :
                printMessage(lexer, "discarding_unexpected", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case NESTED_EMPHASIS :
                printMessage(lexer, "nested_emphasis", new Object[]{getTagName(node)}, TidyMessage.Level.INFO);
                break;

            case COERCE_TO_ENDTAG :
                printMessage(lexer, "coerce_to_endtag", new Object[]{element.element}, TidyMessage.Level.INFO);
                break;

            case NON_MATCHING_ENDTAG :
                printMessage(
                    lexer,
                    "non_matching_endtag",
                    new Object[]{getTagName(node), element.element},
                    TidyMessage.Level.WARNING);
                break;

            case TAG_NOT_ALLOWED_IN :
                printMessage(
                    lexer,
                    "tag_not_allowed_in",
                    new Object[]{getTagName(node), element.element},
                    TidyMessage.Level.WARNING);
                break;

            case DOCTYPE_AFTER_TAGS :
                printMessage(lexer, "doctype_after_tags", null, TidyMessage.Level.WARNING);
                break;

            case MISSING_STARTTAG :
                printMessage(lexer, "missing_starttag", new Object[]{node.element}, TidyMessage.Level.WARNING);
                break;

            case UNEXPECTED_ENDTAG :
                if (element != null)
                {
                    printMessage(
                        lexer,
                        "unexpected_endtag_in",
                        new Object[]{node.element, element.element},
                        TidyMessage.Level.WARNING);
                }
                else
                {
                    printMessage(lexer, "unexpected_endtag", new Object[]{node.element}, TidyMessage.Level.WARNING);
                }
                break;

            case TOO_MANY_ELEMENTS :
                if (element != null)
                {
                    printMessage(
                        lexer,
                        "too_many_elements_in",
                        new Object[]{node.element, element.element},
                        TidyMessage.Level.WARNING);
                }
                else
                {
                    printMessage(lexer, "too_many_elements", new Object[]{node.element}, TidyMessage.Level.WARNING);
                }
                break;

            case USING_BR_INPLACE_OF :
                printMessage(lexer, "using_br_inplace_of", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case INSERTING_TAG :
                printMessage(lexer, "inserting_tag", new Object[]{node.element}, TidyMessage.Level.WARNING);
                break;

            case CANT_BE_NESTED :
                printMessage(lexer, "cant_be_nested", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case PROPRIETARY_ELEMENT :
                printMessage(lexer, "proprietary_element", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);

                if (node.tag == tt.tagLayer)
                {
                    lexer.badLayout |= USING_LAYER;
                }
                else if (node.tag == tt.tagSpacer)
                {
                    lexer.badLayout |= USING_SPACER;
                }
                else if (node.tag == tt.tagNobr)
                {
                    lexer.badLayout |= USING_NOBR;
                }
                break;

            case OBSOLETE_ELEMENT :
                if (element.tag != null && (element.tag.model & Dict.CM_OBSOLETE) != 0)
                {
                    printMessage(
                        lexer,
                        "obsolete_element",
                        new Object[]{getTagName(element), getTagName(node)},
                        TidyMessage.Level.WARNING);
                }
                else
                {
                    printMessage(
                        lexer,
                        "replacing_element",
                        new Object[]{getTagName(element), getTagName(node)},
                        TidyMessage.Level.WARNING);
                }
                break;

            case UNESCAPED_ELEMENT :
                printMessage(lexer, "unescaped_element", new Object[]{getTagName(element)}, TidyMessage.Level.WARNING);
                break;

            case TRIM_EMPTY_ELEMENT :
                printMessage(lexer, "trim_empty_element", new Object[]{getTagName(element)}, TidyMessage.Level.WARNING);
                break;

            case MISSING_TITLE_ELEMENT :
                printMessage(lexer, "missing_title_element", null, TidyMessage.Level.WARNING);
                break;

            case ILLEGAL_NESTING :
                printMessage(lexer, "illegal_nesting", new Object[]{getTagName(element)}, TidyMessage.Level.WARNING);
                break;

            case NOFRAMES_CONTENT :
                printMessage(lexer, "noframes_content", new Object[]{getTagName(node)}, TidyMessage.Level.WARNING);
                break;

            case INCONSISTENT_VERSION :
                printMessage(lexer, "inconsistent_version", null, TidyMessage.Level.WARNING);
                break;

            case MALFORMED_DOCTYPE :
                printMessage(lexer, "malformed_doctype", null, TidyMessage.Level.WARNING);
                break;

            case CONTENT_AFTER_BODY :
                printMessage(lexer, "content_after_body", null, TidyMessage.Level.WARNING);
                break;

            case MALFORMED_COMMENT :
                printMessage(lexer, "malformed_comment", null, TidyMessage.Level.WARNING);
                break;

            case BAD_COMMENT_CHARS :
                printMessage(lexer, "bad_comment_chars", null, TidyMessage.Level.WARNING);
                break;

            case BAD_XML_COMMENT :
                printMessage(lexer, "bad_xml_comment", null, TidyMessage.Level.WARNING);
                break;

            case BAD_CDATA_CONTENT :
                printMessage(lexer, "bad_cdata_content", null, TidyMessage.Level.WARNING);
                break;

            case INCONSISTENT_NAMESPACE :
                printMessage(lexer, "inconsistent_namespace", null, TidyMessage.Level.WARNING);
                break;

            case DTYPE_NOT_UPPER_CASE :
                printMessage(lexer, "dtype_not_upper_case", null, TidyMessage.Level.WARNING);
                break;

            case UNEXPECTED_END_OF_FILE :
                // on end of file adjust reported position to end of input
                lexer.lines = lexer.in.curline;
                lexer.columns = lexer.in.curcol;
                printMessage(
                    lexer,
                    "unexpected_end_of_file",
                    new Object[]{getTagName(element)},
                    TidyMessage.Level.WARNING);
                break;

            case MISSING_DOCTYPE :
                printMessage(lexer, "missing_doctype", null, TidyMessage.Level.WARNING);
                break;

            default :
                break;
        }
    }

    /**
     * Prints errors.
     * @param lexer Lexer
     * @param element parent/missing tag
     * @param node current tag
     * @param code error code
     */
    public void error(Lexer lexer, Node element, Node node, short code)
    {
        lexer.warnings++;

        // keep quiet after 6 errors
        if (lexer.errors > 6)
        {
            return;
        }

        lexer.errors++;

        if (code == SUSPECTED_MISSING_QUOTE)
        {
            printMessage(lexer, "suspected_missing_quote", null, TidyMessage.Level.ERROR);
        }
        else if (code == DUPLICATE_FRAMESET)
        {
            printMessage(lexer, "duplicate_frameset", null, TidyMessage.Level.ERROR);
        }
        else if (code == UNKNOWN_ELEMENT)
        {
            printMessage(lexer, "unknown_element", new Object[]{getTagName(node)}, TidyMessage.Level.ERROR);
        }
        else if (code == UNEXPECTED_ENDTAG)
        {
            if (element != null)
            {
                printMessage(
                    lexer,
                    "unexpected_endtag_in",
                    new Object[]{node.element, element.element},
                    TidyMessage.Level.ERROR);
            }
            else
            {
                printMessage(lexer, "unexpected_endtag", new Object[]{node.element}, TidyMessage.Level.ERROR);
            }
        }
    }

    /**
     * Prints error summary.
     * @param lexer Lexer
     */
    public void errorSummary(Lexer lexer)
    {
        // adjust badAccess to that its null if frames are ok
        if ((lexer.badAccess & (USING_FRAMES | USING_NOFRAMES)) != 0)
        {
            if (!(((lexer.badAccess & USING_FRAMES) != 0) && ((lexer.badAccess & USING_NOFRAMES) == 0)))
            {
                lexer.badAccess &= ~(USING_FRAMES | USING_NOFRAMES);
            }
        }
        if (lexer.badChars != 0)
        {
            if ((lexer.badChars & WINDOWS_CHARS) != 0)
            {
                printMessage(lexer, "badchars_summary", null, TidyMessage.Level.SUMMARY);
            }
            if ((lexer.badChars & INVALID_URI) != 0)
            {
                printMessage(lexer, "invaliduri_summary", null, TidyMessage.Level.SUMMARY);
            }
        }

        if (lexer.badForm != 0)
        {
            printMessage(lexer, "badform_summary", null, TidyMessage.Level.SUMMARY);
        }

        if (lexer.badAccess != 0)
        {
            if ((lexer.badAccess & MISSING_SUMMARY) != 0)
            {
                printMessage(lexer, "badaccess_missing_summary", null, TidyMessage.Level.SUMMARY);
            }

            if ((lexer.badAccess & MISSING_IMAGE_ALT) != 0)
            {
                printMessage(lexer, "badaccess_missing_image_alt", null, TidyMessage.Level.SUMMARY);
            }

            if ((lexer.badAccess & MISSING_IMAGE_MAP) != 0)
            {
                printMessage(lexer, "badaccess_missing_image_map", null, TidyMessage.Level.SUMMARY);
            }

            if ((lexer.badAccess & MISSING_LINK_ALT) != 0)
            {
                printMessage(lexer, "badaccess_missing_link_alt", null, TidyMessage.Level.SUMMARY);
            }

            if (((lexer.badAccess & USING_FRAMES) != 0) && ((lexer.badAccess & USING_NOFRAMES) == 0))
            {
                printMessage(lexer, "badaccess_frames", null, TidyMessage.Level.SUMMARY);
            }

            printMessage(lexer, "badaccess_summary", new Object[]{ACCESS_URL}, TidyMessage.Level.SUMMARY);
        }

        if (lexer.badLayout != 0)
        {
            if ((lexer.badLayout & USING_LAYER) != 0)
            {
                printMessage(lexer, "badlayout_using_layer", null, TidyMessage.Level.SUMMARY);
            }

            if ((lexer.badLayout & USING_SPACER) != 0)
            {
                printMessage(lexer, "badlayout_using_spacer", null, TidyMessage.Level.SUMMARY);
            }

            if ((lexer.badLayout & USING_FONT) != 0)
            {
                printMessage(lexer, "badlayout_using_font", null, TidyMessage.Level.SUMMARY);
            }

            if ((lexer.badLayout & USING_NOBR) != 0)
            {
                printMessage(lexer, "badlayout_using_nobr", null, TidyMessage.Level.SUMMARY);
            }

            if ((lexer.badLayout & USING_BODY) != 0)
            {
                printMessage(lexer, "badlayout_using_body", null, TidyMessage.Level.SUMMARY);
            }
        }
    }

    /**
     * Prints the "unknown option" message.
     * @param errout PrintWriter
     * @param c invalid option char
     */
    public void unknownOption(PrintWriter errout, char c)
    {
        printMessage(errout, "unrecognized_option", new Object[]{new String(new char[]{c})}, TidyMessage.Level.ERROR);
    }

    /**
     * Prints the "unknown file" message.
     * @param errout PrintWriter
     * @param file invalid file name
     */
    public void unknownFile(PrintWriter errout, String file)
    {
        printMessage(errout, "unknown_file", new Object[]{"Tidy", file}, TidyMessage.Level.ERROR);
    }

    /**
     * Prints the "needs author intervention" message.
     * @param errout PrintWriter
     */
    public void needsAuthorIntervention(PrintWriter errout)
    {
        printMessage(errout, "needs_author_intervention", null, TidyMessage.Level.SUMMARY);
    }

    /**
     * Prints the "missing body" message.
     * @param errout PrintWriter
     */
    public void missingBody(PrintWriter errout)
    {
        printMessage(errout, "missing_body", null, TidyMessage.Level.ERROR);
    }

    /**
     * Prints the number of generated slides.
     * @param errout PrintWriter
     * @param count slides count
     */
    public void reportNumberOfSlides(PrintWriter errout, int count)
    {
        printMessage(errout, "slides_found", new Object[]{new Integer(count)}, TidyMessage.Level.SUMMARY);
    }

    /**
     * Prints tidy general info.
     * @param errout PrintWriter
     */
    public void generalInfo(PrintWriter errout)
    {
        printMessage(errout, "general_info", null, TidyMessage.Level.SUMMARY);
    }

    /**
     * Prints tidy hello message.
     * @param errout PrintWriter
     */
    public void helloMessage(PrintWriter errout)
    {
        printMessage(
            errout,
            "hello_message",
            new Object[]{Report.RELEASE_DATE, this.currentFile},
            TidyMessage.Level.SUMMARY);
    }

    /**
     * Sets the current file name.
     * @param filename current file.
     */
    public void setFilename(String filename)
    {
        this.currentFile = filename; // for use with Gnu Emacs
    }

    /**
     * Prints information for html version in input file.
     * @param errout PrintWriter
     * @param lexer Lexer
     * @param filename file name
     * @param doctype doctype Node
     */
    public void reportVersion(PrintWriter errout, Lexer lexer, String filename, Node doctype)
    {
        int i, c;
        int state = 0;
        String vers = lexer.HTMLVersionName();
        MutableInteger cc = new MutableInteger();

        if (doctype != null)
        {

            StringBuffer doctypeBuffer = new StringBuffer();
            for (i = doctype.start; i < doctype.end; ++i)
            {
                c = doctype.textarray[i];

                // look for UTF-8 multibyte character
                if (c < 0)
                {
                    i += PPrint.getUTF8(doctype.textarray, i, cc);
                    c = cc.getValue();
                }

                if (c == '"')
                {
                    ++state;
                }
                else if (state == 1)
                {
                    doctypeBuffer.append((char) c);
                }
            }

            printMessage(lexer, "doctype_given", new Object[]{filename, doctypeBuffer}, TidyMessage.Level.SUMMARY);
        }

        printMessage(
            lexer,
            "report_version",
            new Object[]{filename, (vers != null ? vers : "HTML proprietary")},
            TidyMessage.Level.SUMMARY);
    }

    /**
     * Prints the number of error/warnings found.
     * @param errout PrintWriter
     * @param lexer Lexer
     */
    public void reportNumWarnings(PrintWriter errout, Lexer lexer)
    {
        if (lexer.warnings > 0)
        {
            printMessage(errout, "num_warnings", new Object[]{new Integer(lexer.warnings)}, TidyMessage.Level.SUMMARY);
        }
        else
        {
            printMessage(errout, "no_warnings", null, TidyMessage.Level.SUMMARY);
        }
    }

    /**
     * Prints tidy help.
     * @param out PrintWriter
     */
    public void helpText(PrintWriter out)
    {
        printMessage(out, "help_text", new Object[]{"Tidy", RELEASE_DATE}, TidyMessage.Level.SUMMARY);
    }

    /**
     * Prints the "bad tree" message.
     * @param errout PrintWriter
     */
    public void badTree(PrintWriter errout)
    {
        printMessage(errout, "bad_tree", null, TidyMessage.Level.ERROR);
    }

}