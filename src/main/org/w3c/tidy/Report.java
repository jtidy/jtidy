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
 * Error/informational message reporter. You should only need to edit the file TidyMessages.properties to localize HTML
 * tidy.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public final class Report
{

    /**
     * used to point to Web Accessibility Guidelines
     */
    public static final String ACCESS_URL = "http://www.w3.org/WAI/GL";

    public static final String RELEASE_DATE = "4th August 2000";

    public static String currentFile; /* sasdjb 01May00 for GNU Emacs error parsing */

    /* error codes for entities */

    public static final short MISSING_SEMICOLON = 1;
    public static final short UNKNOWN_ENTITY = 2;
    public static final short UNESCAPED_AMPERSAND = 3;

    /* error codes for element messages */

    public static final short MISSING_ENDTAG_FOR = 1;
    public static final short MISSING_ENDTAG_BEFORE = 2;
    public static final short DISCARDING_UNEXPECTED = 3;
    public static final short NESTED_EMPHASIS = 4;
    public static final short NON_MATCHING_ENDTAG = 5;
    public static final short TAG_NOT_ALLOWED_IN = 6;
    public static final short MISSING_STARTTAG = 7;
    public static final short UNEXPECTED_ENDTAG = 8;
    public static final short USING_BR_INPLACE_OF = 9;
    public static final short INSERTING_TAG = 10;
    public static final short SUSPECTED_MISSING_QUOTE = 11;
    public static final short MISSING_TITLE_ELEMENT = 12;
    public static final short DUPLICATE_FRAMESET = 13;
    public static final short CANT_BE_NESTED = 14;
    public static final short OBSOLETE_ELEMENT = 15;
    public static final short PROPRIETARY_ELEMENT = 16;
    public static final short UNKNOWN_ELEMENT = 17;
    public static final short TRIM_EMPTY_ELEMENT = 18;
    public static final short COERCE_TO_ENDTAG = 19;
    public static final short ILLEGAL_NESTING = 20;
    public static final short NOFRAMES_CONTENT = 21;
    public static final short CONTENT_AFTER_BODY = 22;
    public static final short INCONSISTENT_VERSION = 23;
    public static final short MALFORMED_COMMENT = 24;
    public static final short BAD_COMMENT_CHARS = 25;
    public static final short BAD_XML_COMMENT = 26;
    public static final short BAD_CDATA_CONTENT = 27;
    public static final short INCONSISTENT_NAMESPACE = 28;
    public static final short DOCTYPE_AFTER_TAGS = 29;
    public static final short MALFORMED_DOCTYPE = 30;
    public static final short UNEXPECTED_END_OF_FILE = 31;
    public static final short DTYPE_NOT_UPPER_CASE = 32;
    public static final short TOO_MANY_ELEMENTS = 33;

    /* error codes used for attribute messages */

    public static final short UNKNOWN_ATTRIBUTE = 48;
    public static final short MISSING_ATTRIBUTE = 49;
    public static final short MISSING_ATTR_VALUE = 50;
    public static final short BAD_ATTRIBUTE_VALUE = 51;
    public static final short UNEXPECTED_GT = 52;
    public static final short PROPRIETARY_ATTRIBUTE = 53;
    public static final short PROPRIETARY_ATTR_VALUE = 54;
    public static final short REPEATED_ATTRIBUTE = 55;
    public static final short MISSING_IMAGEMAP = 56;
    public static final short XML_ATTRIBUTE_VALUE = 57;
    public static final short MISSING_QUOTEMARK = 58;
    public static final short UNEXPECTED_QUOTEMARK = 59;
    public static final short ID_NAME_MISMATCH = 59;

    /* accessibility flaws */

    public static final short MISSING_IMAGE_ALT = 1;
    public static final short MISSING_LINK_ALT = 2;
    public static final short MISSING_SUMMARY = 4;
    public static final short MISSING_IMAGE_MAP = 8;
    public static final short USING_FRAMES = 16;
    public static final short USING_NOFRAMES = 32;

    /* presentation flaws */

    public static final short USING_SPACER = 1;
    public static final short USING_LAYER = 2;
    public static final short USING_NOBR = 4;
    public static final short USING_FONT = 8;
    public static final short USING_BODY = 16;

    /* character encoding errors */
    public static final short WINDOWS_CHARS = 1;
    public static final short NON_ASCII = 2;
    public static final short FOUND_UTF16 = 4;

    private static short optionerrors;

    private static ResourceBundle res;

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

    private void tidyPrint(PrintWriter p, String msg)
    {
        p.print(msg);
    }

    private void tidyPrintln(PrintWriter p, String msg)
    {
        p.println(msg);
    }

    private void tidyPrintln(PrintWriter p)
    {
        p.println();
    }

    public void showVersion(PrintWriter p)
    {
        tidyPrintln(p, "Java HTML Tidy release date: " + RELEASE_DATE);
        tidyPrintln(p, "See http://www.w3.org/People/Raggett for details");
    }

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

    /* lexer is not defined when this is called */
    public void unknownOption(String option)
    {
        optionerrors++;
        try
        {
            System.err.println(MessageFormat.format(res.getString("unknown_option"), new Object[]{option}));
        }
        catch (MissingResourceException e)
        {
            System.err.println(e.toString());
        }
    }

    /* lexer is not defined when this is called */
    public void badArgument(String option)
    {
        optionerrors++;
        try
        {
            System.err.println(MessageFormat.format(res.getString("bad_argument"), new Object[]{option}));
        }
        catch (MissingResourceException e)
        {
            System.err.println(e.toString());
        }
    }

    public void position(Lexer lexer)
    {
        try
        {
            /* Change formatting to be parsable by GNU Emacs */
            if (lexer.configuration.emacs)
            {
                tidyPrint(lexer.errout, MessageFormat.format(res.getString("emacs_format"), new Object[]{
                    currentFile,
                    new Integer(lexer.lines),
                    new Integer(lexer.columns)}));
                tidyPrint(lexer.errout, " ");
            }
            else
            {
                // traditional format
                tidyPrint(lexer.errout, MessageFormat.format(res.getString("line_column"), new Object[]{
                    new Integer(lexer.lines),
                    new Integer(lexer.columns)}));
            }
        }
        catch (MissingResourceException e)
        {
            lexer.errout.println(e.toString());
        }
    }

    public void encodingError(Lexer lexer, short code, int c)
    {
        lexer.warnings++;

        if (lexer.configuration.showWarnings)
        {
            position(lexer);

            if (code == WINDOWS_CHARS)
            {
                lexer.badChars |= WINDOWS_CHARS;
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("illegal_char"),
                        new Object[]{new Integer(c)}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            tidyPrintln(lexer.errout);
        }
    }

    public void entityError(Lexer lexer, short code, String entity, int c)
    {
        lexer.warnings++;

        if (lexer.configuration.showWarnings)
        {
            position(lexer);

            if (code == MISSING_SEMICOLON)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("missing_semicolon"),
                        new Object[]{entity}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == UNKNOWN_ENTITY)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("unknown_entity"), new Object[]{entity}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == UNESCAPED_AMPERSAND)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("unescaped_ampersand"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            tidyPrintln(lexer.errout);
        }
    }

    public void attrError(Lexer lexer, Node node, String attr, short code)
    {
        lexer.warnings++;

        /* keep quiet after 6 errors */
        if (lexer.errors > 6)
        {
            return;
        }

        if (lexer.configuration.showWarnings)
        {
            /* on end of file adjust reported position to end of input */
            if (code == UNEXPECTED_END_OF_FILE)
            {
                lexer.lines = lexer.in.curline;
                lexer.columns = lexer.in.curcol;
            }

            position(lexer);

            if (code == UNKNOWN_ATTRIBUTE)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat
                        .format(res.getString("unknown_attribute"), new Object[]{attr}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == MISSING_ATTRIBUTE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node)
                        + MessageFormat.format(res.getString("missing_attribute"), new Object[]{attr}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == MISSING_ATTR_VALUE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node)
                        + MessageFormat.format(res.getString("missing_attr_value"), new Object[]{attr}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == MISSING_IMAGEMAP)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("missing_imagemap"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
                lexer.badAccess |= MISSING_IMAGE_MAP;
            }
            else if (code == BAD_ATTRIBUTE_VALUE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node)
                        + MessageFormat.format(res.getString("bad_attribute_value"), new Object[]{attr}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == XML_ATTRIBUTE_VALUE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node)
                        + MessageFormat.format(res.getString("xml_attribute_value"), new Object[]{attr}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == UNEXPECTED_GT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("error"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("unexpected_gt"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
                lexer.errors++;

            }
            else if (code == UNEXPECTED_QUOTEMARK)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("unexpected_quotemark"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == REPEATED_ATTRIBUTE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("repeated_attribute"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == PROPRIETARY_ATTR_VALUE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node)
                        + MessageFormat.format(res.getString("proprietary_attr_value"), new Object[]{attr}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == PROPRIETARY_ATTRIBUTE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node)
                        + MessageFormat.format(res.getString("proprietary_attribute"), new Object[]{attr}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == UNEXPECTED_END_OF_FILE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("unexpected_end_of_file"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == ID_NAME_MISMATCH)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("id_name_mismatch"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            tidyPrintln(lexer.errout);
        }
        else if (code == UNEXPECTED_GT)
        {
            position(lexer);
            try
            {
                tidyPrint(lexer.errout, res.getString("error"));
                tidyPrint(lexer.errout, getTagName(node) + res.getString("unexpected_gt"));
            }
            catch (MissingResourceException e)
            {
                lexer.errout.println(e.toString());
            }
            tidyPrintln(lexer.errout);
            lexer.errors++;

        }
    }

    public void warning(Lexer lexer, Node element, Node node, short code)
    {

        TagTable tt = lexer.configuration.tt;

        lexer.warnings++;

        /* keep quiet after 6 errors */
        if (lexer.errors > 6)
        {
            return;
        }

        if (lexer.configuration.showWarnings)
        {
            /* on end of file adjust reported position to end of input */
            if (code == UNEXPECTED_END_OF_FILE)
            {
                lexer.lines = lexer.in.curline;
                lexer.columns = lexer.in.curcol;
            }

            position(lexer);

            if (code == MISSING_ENDTAG_FOR)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("missing_endtag_for"),
                        new Object[]{element.element}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == MISSING_ENDTAG_BEFORE)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("missing_endtag_before")
                        + getTagName(node), new Object[]{element.element}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == DISCARDING_UNEXPECTED)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("discarding_unexpected") + getTagName(node));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == NESTED_EMPHASIS)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("nested_emphasis") + getTagName(node));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == COERCE_TO_ENDTAG)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("coerce_to_endtag"),
                        new Object[]{element.element}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == NON_MATCHING_ENDTAG)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("non_matching_endtag_1") + getTagName(node));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }

                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("non_matching_endtag_2"),
                        new Object[]{element.element}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == TAG_NOT_ALLOWED_IN)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node)
                        + MessageFormat.format(res.getString("tag_not_allowed_in"), new Object[]{element.element}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == DOCTYPE_AFTER_TAGS)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("doctype_after_tags"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == MISSING_STARTTAG)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("missing_starttag"),
                        new Object[]{node.element}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == UNEXPECTED_ENDTAG)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("unexpected_endtag"),
                        new Object[]{node.element}));
                    if (element != null)
                    {

                        tidyPrint(lexer.errout, MessageFormat.format(res.getString("unexpected_endtag_suffix"),
                            new Object[]{element.element}));
                    }
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == TOO_MANY_ELEMENTS)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("too_many_elements"),
                        new Object[]{node.element}));
                    if (element != null)
                    {
                        tidyPrint(lexer.errout, MessageFormat.format(res.getString("too_many_elements_suffix"),
                            new Object[]{element.element}));
                    }
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == USING_BR_INPLACE_OF)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("using_br_inplace_of") + getTagName(node));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == INSERTING_TAG)
            {
                try
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("inserting_tag"),
                        new Object[]{node.element}));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == CANT_BE_NESTED)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("cant_be_nested"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == PROPRIETARY_ELEMENT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("proprietary_element"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }

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
            }
            else if (code == OBSOLETE_ELEMENT)
            {
                try
                {
                    if (element.tag != null && (element.tag.model & Dict.CM_OBSOLETE) != 0)
                    {
                        tidyPrint(lexer.errout, res.getString("obsolete_element"));
                    }
                    else
                    {
                        tidyPrint(lexer.errout, res.getString("replacing_element"));
                    }

                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }

                try
                {
                    tidyPrint(lexer.errout, getTagName(element) + res.getString("by") + getTagName(node));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == TRIM_EMPTY_ELEMENT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("trim_empty_element") + getTagName(element));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }

            }
            else if (code == MISSING_TITLE_ELEMENT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("missing_title_element"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == ILLEGAL_NESTING)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(element) + res.getString("illegal_nesting"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == NOFRAMES_CONTENT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("warning"));
                    tidyPrint(lexer.errout, getTagName(node) + res.getString("noframes_content"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == INCONSISTENT_VERSION)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("inconsistent_version"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == MALFORMED_DOCTYPE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("malformed_doctype"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == CONTENT_AFTER_BODY)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("content_after_body"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == MALFORMED_COMMENT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("malformed_comment"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == BAD_COMMENT_CHARS)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("bad_comment_chars"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == BAD_XML_COMMENT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("bad_xml_comment"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == BAD_CDATA_CONTENT)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("bad_cdata_content"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == INCONSISTENT_NAMESPACE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("inconsistent_namespace"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == DTYPE_NOT_UPPER_CASE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("dtype_not_upper_case"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
            else if (code == UNEXPECTED_END_OF_FILE)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("unexpected_end_of_file") + getTagName(element));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }

            }

            tidyPrintln(lexer.errout);
        }
    }

    public void error(Lexer lexer, Node element, Node node, short code)
    {
        lexer.warnings++;

        /* keep quiet after 6 errors */
        if (lexer.errors > 6)
        {
            return;
        }

        lexer.errors++;

        position(lexer);

        if (code == SUSPECTED_MISSING_QUOTE)
        {
            try
            {
                tidyPrint(lexer.errout, res.getString("suspected_missing_quote"));
            }
            catch (MissingResourceException e)
            {
                lexer.errout.println(e.toString());
            }
        }
        else if (code == DUPLICATE_FRAMESET)
        {
            try
            {
                tidyPrint(lexer.errout, res.getString("duplicate_frameset"));
            }
            catch (MissingResourceException e)
            {
                lexer.errout.println(e.toString());
            }
        }
        else if (code == UNKNOWN_ELEMENT)
        {
            try
            {
                tidyPrint(lexer.errout, res.getString("error"));
                tidyPrint(lexer.errout, getTagName(node) + res.getString("unknown_element"));
            }
            catch (MissingResourceException e)
            {
                lexer.errout.println(e.toString());
            }
        }
        else if (code == UNEXPECTED_ENDTAG)
        {
            try
            {
                tidyPrint(lexer.errout, MessageFormat.format(res.getString("unexpected_endtag"),
                    new Object[]{node.element}));
                if (element != null)
                {
                    tidyPrint(lexer.errout, MessageFormat.format(res.getString("unexpected_endtag_suffix"),
                        new Object[]{element.element}));
                }
            }
            catch (MissingResourceException e)
            {
                lexer.errout.println(e.toString());
            }
        }

        tidyPrintln(lexer.errout);
    }

    public void errorSummary(Lexer lexer)
    {
        /* adjust badAccess to that its null if frames are ok */
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
                try
                {
                    tidyPrint(lexer.errout, res.getString("badchars_summary"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
        }

        if (lexer.badForm != 0)
        {
            try
            {
                tidyPrint(lexer.errout, res.getString("badform_summary"));
            }
            catch (MissingResourceException e)
            {
                lexer.errout.println(e.toString());
            }
        }

        if (lexer.badAccess != 0)
        {
            if ((lexer.badAccess & MISSING_SUMMARY) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badaccess_missing_summary"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if ((lexer.badAccess & MISSING_IMAGE_ALT) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badaccess_missing_image_alt"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if ((lexer.badAccess & MISSING_IMAGE_MAP) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badaccess_missing_image_map"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if ((lexer.badAccess & MISSING_LINK_ALT) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badaccess_missing_link_alt"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if (((lexer.badAccess & USING_FRAMES) != 0) && ((lexer.badAccess & USING_NOFRAMES) == 0))
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badaccess_frames"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            try
            {
                tidyPrint(lexer.errout, MessageFormat.format(res.getString("badaccess_summary"),
                    new Object[]{ACCESS_URL}));
            }
            catch (MissingResourceException e)
            {
                lexer.errout.println(e.toString());
            }
        }

        if (lexer.badLayout != 0)
        {
            if ((lexer.badLayout & USING_LAYER) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badlayout_using_layer"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if ((lexer.badLayout & USING_SPACER) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badlayout_using_spacer"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if ((lexer.badLayout & USING_FONT) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badlayout_using_font"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if ((lexer.badLayout & USING_NOBR) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badlayout_using_nobr"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }

            if ((lexer.badLayout & USING_BODY) != 0)
            {
                try
                {
                    tidyPrint(lexer.errout, res.getString("badlayout_using_body"));
                }
                catch (MissingResourceException e)
                {
                    lexer.errout.println(e.toString());
                }
            }
        }
    }

    public void unknownOption(PrintWriter errout, char c)
    {
        try
        {
            tidyPrintln(errout, MessageFormat.format(res.getString("unrecognized_option"), new Object[]{new String(
                new char[]{c})}));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void unknownFile(PrintWriter errout, String program, String file)
    {
        try
        {
            tidyPrintln(errout, MessageFormat.format(res.getString("unknown_file"), new Object[]{program, file}));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void needsAuthorIntervention(PrintWriter errout)
    {
        try
        {
            tidyPrintln(errout, res.getString("needs_author_intervention"));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void missingBody(PrintWriter errout)
    {
        try
        {
            tidyPrintln(errout, res.getString("missing_body"));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void reportNumberOfSlides(PrintWriter errout, int count)
    {
        try
        {
            tidyPrintln(errout, MessageFormat.format(res.getString("slides_found"), new Object[]{new Integer(count)}));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void generalInfo(PrintWriter errout)
    {
        try
        {
            tidyPrintln(errout, res.getString("general_info"));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void helloMessage(PrintWriter errout, String date, String filename)
    {
        currentFile = filename; /* for use with Gnu Emacs */

        try
        {
            tidyPrintln(errout, MessageFormat.format(res.getString("hello_message"), new Object[]{date, filename}));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void reportVersion(PrintWriter errout, Lexer lexer, String filename, Node doctype)
    {
        int i, c;
        int state = 0;
        String vers = lexer.HTMLVersionName();
        MutableInteger cc = new MutableInteger();

        try
        {
            if (doctype != null)
            {
                tidyPrint(errout, MessageFormat.format(res.getString("doctype_given"), new Object[]{filename}));

                for (i = doctype.start; i < doctype.end; ++i)
                {
                    c = doctype.textarray[i];

                    /* look for UTF-8 multibyte character */
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
                        errout.print((char) c);
                    }
                }

                errout.print('"');
            }

            tidyPrintln(errout, MessageFormat.format(res.getString("report_version"), new Object[]{
                filename,
                (vers != null ? vers : "HTML proprietary")}));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

    public void reportNumWarnings(PrintWriter errout, Lexer lexer)
    {
        if (lexer.warnings > 0)
        {
            try
            {
                tidyPrintln(errout, MessageFormat.format(res.getString("num_warnings"), new Object[]{new Integer(
                    lexer.warnings)}));
            }
            catch (MissingResourceException e)
            {
                errout.println(e.toString());
            }
        }
        else
        {
            try
            {
                tidyPrintln(errout, res.getString("no_warnings"));
            }
            catch (MissingResourceException e)
            {
                errout.println(e.toString());
            }
        }
    }

    public void helpText(PrintWriter out, String prog)
    {
        try
        {
            tidyPrintln(out, MessageFormat.format(res.getString("help_text"), new Object[]{prog, RELEASE_DATE}));
        }
        catch (MissingResourceException e)
        {
            out.println(e.toString());
        }
    }

    public void badTree(PrintWriter errout)
    {
        try
        {
            tidyPrintln(errout, res.getString("bad_tree"));
        }
        catch (MissingResourceException e)
        {
            errout.println(e.toString());
        }
    }

}