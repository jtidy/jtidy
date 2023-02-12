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

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Tag dictionary node. If the document uses just HTML 2.0 tags and attributes described it as HTML 2.0 Similarly for
 * HTML 3.2 and the 3 flavors of HTML 4.0. If there are proprietary tags and attributes then describe it as HTML
 * Proprietary. If it includes the xml-lang or xmlns attributes but is otherwise HTML 2.0, 3.2 or 4.0 then describe it
 * as one of the flavors of Voyager (strict, loose or frameset).
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class Dict
{

    /**
     * Content model: unknown.
     */
    public static final int CM_UNKNOWN = 0;

    /**
     * Content model: empty.
     */
    public static final int CM_EMPTY = (1);

    /**
     * Content model: html.
     */
    public static final int CM_HTML = (1 << 1);

    /**
     * Content model: head.
     */
    public static final int CM_HEAD = (1 << 2);

    /**
     * Content model: block.
     */
    public static final int CM_BLOCK = (1 << 3);

    /**
     * Content model: inline.
     */
    public static final int CM_INLINE = (1 << 4);

    /**
     * Content model: list.
     */
    public static final int CM_LIST = (1 << 5);

    /**
     * Content model: definition list.
     */
    public static final int CM_DEFLIST = (1 << 6);

    /**
     * Content model: table.
     */
    public static final int CM_TABLE = (1 << 7);

    /**
     * Content model: rowgroup.
     */
    public static final int CM_ROWGRP = (1 << 8);

    /**
     * Content model: row.
     */
    public static final int CM_ROW = (1 << 9);

    /**
     * Content model: field.
     */
    public static final int CM_FIELD = (1 << 10);

    /**
     * Content model: object.
     */
    public static final int CM_OBJECT = (1 << 11);

    /**
     * Content model: param.
     */
    public static final int CM_PARAM = (1 << 12);

    /**
     * Content model: frames.
     */
    public static final int CM_FRAMES = (1 << 13);

    /**
     * Content model: heading.
     */
    public static final int CM_HEADING = (1 << 14);

    /**
     * Content model: opt.
     */
    public static final int CM_OPT = (1 << 15);

    /**
     * Content model: img.
     */
    public static final int CM_IMG = (1 << 16);

    /**
     * Content model: mixed.
     */
    public static final int CM_MIXED = (1 << 17);

    /**
     * Content model: no indent.
     */
    public static final int CM_NO_INDENT = (1 << 18);

    /**
     * Content model: obsolete.
     */
    public static final int CM_OBSOLETE = (1 << 19);

    /**
     * Content model: new.
     */
    public static final int CM_NEW = (1 << 20);

    /**
     * Content model: omitst.
     */
    public static final int CM_OMITST = (1 << 21);

    /**
     * Version: unknown.
     */
    public static final EnumSet<HtmlVersion> VERS_UNKNOWN = EnumSet.of(HtmlVersion.UNKNOWN);

    /**
     * Version: html 2.0.
     */
    public static final EnumSet<HtmlVersion> VERS_HTML20 = EnumSet.of(HtmlVersion.HTML20);
    
    /**
     * All versions except HTML 2.0.
     */
    public static final EnumSet<HtmlVersion> NOT_HTML20 = allExcept(VERS_HTML20);

    /**
     * Version: html 3.2.
     */
    public static final EnumSet<HtmlVersion> VERS_HTML32 = EnumSet.of(HtmlVersion.HTML32);

    /**
     * Version: html 4.0 strict.
     */
    public static final EnumSet<HtmlVersion> VERS_HTML40_STRICT = EnumSet.of(HtmlVersion.HTML40_STRICT);
    
    /**
     * All versions except html 4.0 strict.
     */
    public static final EnumSet<HtmlVersion> NOT_HTML40_STRICT = Dict.without(EnumSet.allOf(HtmlVersion.class), VERS_HTML40_STRICT);

    /**
     * Version: html 4.0 transitional.
     */
    public static final EnumSet<HtmlVersion> VERS_HTML40_LOOSE = EnumSet.of(HtmlVersion.HTML40_LOOSE);

    /**
     * Version: html 4.0 frameset.
     */
    public static final EnumSet<HtmlVersion> VERS_FRAMESET = EnumSet.of(HtmlVersion.FRAMESET);
    
    /**
     * All HTML versions except HTML 4.0 frameset.
     */
    public static final EnumSet<HtmlVersion> NOT_FRAMESET = allExcept(VERS_FRAMESET);

    /**
     * Version: xml.
     */
    public static final EnumSet<HtmlVersion> VERS_XML = EnumSet.of(HtmlVersion.XML);

    /**
     * Version: netscape.
     */
    public static final EnumSet<HtmlVersion> VERS_NETSCAPE = EnumSet.of(HtmlVersion.NETSCAPE);

    /**
     * Version: microsoft.
     */
    public static final EnumSet<HtmlVersion> VERS_MICROSOFT = EnumSet.of(HtmlVersion.MICROSOFT);

    /**
     * Version: sun.
     */
    public static final EnumSet<HtmlVersion> VERS_SUN = EnumSet.of(HtmlVersion.SUN);

    /**
     * Version: malformed.
     */
    public static final EnumSet<HtmlVersion> VERS_MALFORMED = EnumSet.of(HtmlVersion.MALFORMED);

    /**
     * Version: xhtml 1.1.
     */
    public static final EnumSet<HtmlVersion> VERS_XHTML11 = EnumSet.of(HtmlVersion.XHTML11);
    
    /**
     * All versions except XHTML 1.1.
     */
    public static final EnumSet<HtmlVersion> NOT_XHTML11 = allExcept(VERS_XHTML11);

    /**
     * Version: xhtml basic.
     */
    public static final EnumSet<HtmlVersion> VERS_BASIC = EnumSet.of(HtmlVersion.BASIC);
    /**
     * Version: HTML 5.
     */
    public static final EnumSet<HtmlVersion> VERS_HTML5 = EnumSet.of(HtmlVersion.HTML5);

    /**
     * all tags and attributes are ok in proprietary version of HTML.
     */
    public static final EnumSet<HtmlVersion> VERS_PROPRIETARY = EnumSet.of(HtmlVersion.NETSCAPE, HtmlVersion.MICROSOFT, HtmlVersion.SUN);

    /**
     * tags/attrs in HTML4 but not in earlier version.
     */
    public static final EnumSet<HtmlVersion> VERS_HTML40 = EnumSet.of(HtmlVersion.HTML5, HtmlVersion.HTML40_STRICT, HtmlVersion.HTML40_LOOSE, HtmlVersion.FRAMESET);

    /**
     * tags/attrs which are in all versions of HTML except strict.
     */
    public static final EnumSet<HtmlVersion> VERS_LOOSE = EnumSet.of(HtmlVersion.HTML32, HtmlVersion.HTML40_LOOSE, HtmlVersion.FRAMESET);

    /**
     * tags/attrs in HTML 4 loose and frameset.
     */
    public static final EnumSet<HtmlVersion> VERS_IFRAME = EnumSet.of(HtmlVersion.HTML40_LOOSE, HtmlVersion.FRAMESET);

    /**
     * tags/attrs in all versions from HTML 3.2 onwards.
     */
    public static final EnumSet<HtmlVersion> VERS_FROM32 = combine(VERS_LOOSE, HtmlVersion.HTML5, HtmlVersion.HTML40_STRICT);

    /**
     * versions with on... attributes.
     */
    public static final EnumSet<HtmlVersion> VERS_EVENTS = combine(VERS_HTML40, HtmlVersion.XHTML11);

    /**
     * tags/attrs in any version.
     */
    public static final EnumSet<HtmlVersion> VERS_ALL = combine(VERS_HTML40, HtmlVersion.HTML20, HtmlVersion.HTML32, HtmlVersion.XHTML11, HtmlVersion.BASIC);

    /**
     * types of tags that the user can define: empty tag.
     */
    public static final short TAGTYPE_EMPTY = 1;

    /**
     * types of tags that the user can define: inline tag.
     */
    public static final short TAGTYPE_INLINE = 2;

    /**
     * types of tags that the user can define: block tag.
     */
    public static final short TAGTYPE_BLOCK = 4;

    /**
     * types of tags that the user can define: pre tag.
     */
    public static final short TAGTYPE_PRE = 8;

    /**
     * Tag name.
     */
    protected String name;

    /**
     * Version in which this tag is defined.
     */
    protected EnumSet<HtmlVersion> versions;

    /**
     * model (CM_* constants).
     */
    protected int model;

    /**
     * Parser for this tag.
     */
    private Parser parser;

    /**
     * Validator for this tag.
     */
    private TagCheck chkattrs;

    /**
     * Instantiates a new Tag definition.
     * @param name tag name
     * @param versions version in which this tag is defined
     * @param model model (CM_* constants)
     * @param parser parser for this tag
     * @param chkattrs validator for this tag (can be null)
     */
    public Dict(String name, EnumSet<HtmlVersion> versions, int model, Parser parser, TagCheck chkattrs)
    {
        this.name = name;
        this.versions = versions;
        this.model = model;
        this.parser = parser;
        this.chkattrs = chkattrs;
    }

    /** 
	 * All {@link HtmlVersion}s except the given ones.
	 */
	public static EnumSet<HtmlVersion> allExcept(EnumSet<HtmlVersion> except) {
		EnumSet<HtmlVersion> result = EnumSet.allOf(HtmlVersion.class);
		result.removeAll(except);
		return result;
	}

	/** 
	 * The combined set of literals.
	 */
	@SafeVarargs
	public static <T extends Enum<T>> EnumSet<T> combine(EnumSet<T> set, T ...others) {
		EnumSet<T> result = set.clone();
		result.addAll(Arrays.asList(others));
		return result;
	}

	/** 
	 * The combined set of literals.
	 */
	public static <T extends Enum<T>> EnumSet<T> combine(EnumSet<T> set1, EnumSet<T> set2) {
		EnumSet<T> result = set1.clone();
		result.addAll(set2);
		return result;
	}
	
	/** 
	 * The combined set of literals.
	 */
	@SafeVarargs
	public static <T extends Enum<T>> EnumSet<T> combine(EnumSet<T> ...sets) {
		EnumSet<T> result = sets[0].clone();
		for (int n = 1, cnt = sets.length; n < cnt; n++) {
			result.addAll(sets[n]);
		}
		return result;
	}
	
	/**
     * Getter for <code>chkattrs</code>.
     * @return Returns the chkattrs.
     */
    public TagCheck getChkattrs()
    {
        return this.chkattrs;
    }

    /**
     * Getter for <code>model</code>.
     * @return Returns the model.
     */
    public int getModel()
    {
        return this.model;
    }

    /**
     * Getter for <code>name</code>.
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Getter for <code>parser</code>.
     * @return Returns the parser.
     */
    public Parser getParser()
    {
        return this.parser;
    }

    /**
     * Setter for <code>chkattrs</code>.
     * @param chkattrs The chkattrs to set.
     */
    public void setChkattrs(TagCheck chkattrs)
    {
        this.chkattrs = chkattrs;
    }
    /**
     * Getter for <code>versions</code>.
     * @return Returns the versions.
     */
    public EnumSet<HtmlVersion> getVersions()
    {
        return this.versions;
    }
    /**
     * Setter for <code>parser</code>.
     * @param parser The parser to set.
     */
    public void setParser(Parser parser)
    {
        this.parser = parser;
    }

	/** 
	 * The set of literals in the given set reduced by the ones in the excluded set.
	 */
	public static EnumSet<HtmlVersion> without(EnumSet<HtmlVersion> set, EnumSet<HtmlVersion> excluded) {
		EnumSet<HtmlVersion> result = set.clone();
		result.removeAll(excluded);
		return result;
	}
}