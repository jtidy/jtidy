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
 * Node.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */

/*
 * Used for elements and text nodes element name is null for text nodes start and end are offsets into lexbuf which
 * contains the textual content of all elements in the parse tree. parent and content allow traversal of the parse tree
 * in any direction. attributes are represented as a linked list of AttVal nodes which hold the strings for
 * attribute/value pairs.
 */

public class Node
{

    public static final short RootNode = 0;
    public static final short DocTypeTag = 1;
    public static final short CommentTag = 2;
    public static final short ProcInsTag = 3;
    public static final short TextNode = 4;
    public static final short StartTag = 5;
    public static final short EndTag = 6;
    public static final short StartEndTag = 7;
    public static final short CDATATag = 8;
    public static final short SectionTag = 9;
    public static final short AspTag = 10;
    public static final short JsteTag = 11;
    public static final short PhpTag = 12;

    protected Node parent;
    protected Node prev;
    protected Node next;
    protected Node last;
    protected int start; /* start of span onto text array */
    protected int end; /* end of span onto text array */
    protected byte[] textarray; /* the text array */
    protected short type; /* TextNode, StartTag, EndTag etc. */
    protected boolean closed; /* true if closed by explicit end tag */
    protected boolean implicit; /* true if inferred */
    protected boolean linebreak; /* true if followed by a line break */
    protected Dict was; /* old tag when it was changed */
    protected Dict tag; /* tag's dictionary definition */
    protected String element; /* name (null for text nodes) */
    protected AttVal attributes;
    protected Node content;

    public Node()
    {
        this(TextNode, null, 0, 0);
    }

    public Node(short type, byte[] textarray, int start, int end)
    {
        this.parent = null;
        this.prev = null;
        this.next = null;
        this.last = null;
        this.start = start;
        this.end = end;
        this.textarray = textarray;
        this.type = type;
        this.closed = false;
        this.implicit = false;
        this.linebreak = false;
        this.was = null;
        this.tag = null;
        this.element = null;
        this.attributes = null;
        this.content = null;
    }

    public Node(short type, byte[] textarray, int start, int end, String element, TagTable tt)
    {
        this.parent = null;
        this.prev = null;
        this.next = null;
        this.last = null;
        this.start = start;
        this.end = end;
        this.textarray = textarray;
        this.type = type;
        this.closed = false;
        this.implicit = false;
        this.linebreak = false;
        this.was = null;
        this.tag = null;
        this.element = element;
        this.attributes = null;
        this.content = null;
        if (type == StartTag || type == StartEndTag || type == EndTag)
        {
            tt.findTag(this);
        }
    }

    /* used to clone heading nodes when split by an <HR> */
    protected Object clone()
    {
        Node node = new Node();

        node.parent = this.parent;
        if (this.textarray != null)
        {
            node.textarray = new byte[this.end - this.start];
            node.start = 0;
            node.end = this.end - this.start;
            if (node.end > 0)
            {
                System.arraycopy(this.textarray, this.start, node.textarray, node.start, node.end);
            }
        }
        node.type = this.type;
        node.closed = this.closed;
        node.implicit = this.implicit;
        node.linebreak = this.linebreak;
        node.was = this.was;
        node.tag = this.tag;
        if (this.element != null)
        {
            node.element = this.element;
        }
        if (this.attributes != null)
        {
            node.attributes = (AttVal) this.attributes.clone();
        }
        return node;
    }

    public AttVal getAttrByName(String name)
    {
        AttVal attr;

        for (attr = this.attributes; attr != null; attr = attr.next)
        {
            if (name != null && attr.attribute != null && attr.attribute.equals(name))
            {
                break;
            }
        }

        return attr;
    }

    /* default method for checking an element's attributes */
    public void checkAttributes(Lexer lexer)
    {
        AttVal attval;

        for (attval = this.attributes; attval != null; attval = attval.next)
        {
            attval.checkAttribute(lexer, this);
        }
    }

    public void checkUniqueAttributes(Lexer lexer)
    {
        AttVal attval;

        for (attval = this.attributes; attval != null; attval = attval.next)
        {
            if (attval.asp == null && attval.php == null)
            {
                attval.checkUniqueAttribute(lexer, this);
            }
        }
    }

    public void addAttribute(String name, String value)
    {
        AttVal av = new AttVal(null, null, null, null, '"', name, value);
        av.dict = AttributeTable.getDefaultAttributeTable().findAttribute(av);

        if (this.attributes == null)
        {
            this.attributes = av;
        }
        else /* append to end of attributes */ {
            AttVal here = this.attributes;

            while (here.next != null)
            {
                here = here.next;
            }

            here.next = av;
        }
    }

    /* remove attribute from node then free it */
    public void removeAttribute(AttVal attr)
    {
        AttVal av;
        AttVal prev = null;
        AttVal next;

        for (av = this.attributes; av != null; av = next)
        {
            next = av.next;

            if (av == attr)
            {
                if (prev != null)
                {
                    prev.next = next;
                }
                else
                {
                    this.attributes = next;
                }
            }
            else
            {
                prev = av;
            }
        }
    }

    /* find doctype element */
    public Node findDocType()
    {
        Node node;

        for (node = this.content; node != null && node.type != DocTypeTag; node = node.next);

        return node;
    }

    public void discardDocType()
    {
        Node node;

        node = findDocType();
        if (node != null)
        {
            if (node.prev != null)
            {
                node.prev.next = node.next;
            }
            else
            {
                node.parent.content = node.next;
            }

            if (node.next != null)
            {
                node.next.prev = node.prev;
            }

            node.next = null;
        }
    }

    /* remove node from markup tree and discard it */
    public static Node discardElement(Node element)
    {
        Node next = null;

        if (element != null)
        {
            next = element.next;
            removeNode(element);
        }

        return next;
    }

    /* insert node into markup tree */
    public static void insertNodeAtStart(Node element, Node node)
    {
        node.parent = element;

        if (element.content == null)
        {
            element.last = node;
        }
        else
        {
            element.content.prev = node; // AQ added 13 Apr 2000
        }

        node.next = element.content;
        node.prev = null;
        element.content = node;
    }

    /* insert node into markup tree */
    public static void insertNodeAtEnd(Node element, Node node)
    {
        node.parent = element;
        node.prev = element.last;

        if (element.last != null)
        {
            element.last.next = node;
        }
        else
        {
            element.content = node;
        }

        element.last = node;
    }

    /*
     * insert node into markup tree in pace of element which is moved to become the child of the node
     */
    public static void insertNodeAsParent(Node element, Node node)
    {
        node.content = element;
        node.last = element;
        node.parent = element.parent;
        element.parent = node;

        if (node.parent.content == element)
        {
            node.parent.content = node;
        }

        if (node.parent.last == element)
        {
            node.parent.last = node;
        }

        node.prev = element.prev;
        element.prev = null;

        if (node.prev != null)
        {
            node.prev.next = node;
        }

        node.next = element.next;
        element.next = null;

        if (node.next != null)
        {
            node.next.prev = node;
        }
    }

    /* insert node into markup tree before element */
    public static void insertNodeBeforeElement(Node element, Node node)
    {
        Node parent;

        parent = element.parent;
        node.parent = parent;
        node.next = element;
        node.prev = element.prev;
        element.prev = node;

        if (node.prev != null)
        {
            node.prev.next = node;
        }

        if (parent != null && parent.content == element)
        {
            parent.content = node;
        }
    }

    /* insert node into markup tree after element */
    public static void insertNodeAfterElement(Node element, Node node)
    {
        Node parent;

        parent = element.parent;
        node.parent = parent;

        // AQ - 13Jan2000 fix for parent == null
        if (parent != null && parent.last == element)
        {
            parent.last = node;
        }
        else
        {
            node.next = element.next;
            // AQ - 13Jan2000 fix for node.next == null
            if (node.next != null)
            {
                node.next.prev = node;
            }
        }

        element.next = node;
        node.prev = element;
    }

    public static void trimEmptyElement(Lexer lexer, Node element)
    {
        TagTable tt = lexer.configuration.tt;

        if (lexer.canPrune(element))
        {
            if (element.type != TextNode)
            {
                Report.warning(lexer, element, null, Report.TRIM_EMPTY_ELEMENT);
            }

            discardElement(element);
        }
        else if (element.tag == tt.tagP && element.content == null)
        {
            /* replace <p></p> by <br><br> to preserve formatting */
            Node node = lexer.inferredTag("br");
            Node.coerceNode(lexer, element, tt.tagBr);
            Node.insertNodeAfterElement(element, node);
        }
    }

    /*
     * This maps <em> hello </em><strong> world </strong> to <em> hello </em><strong> world </strong> If last child
     * of element is a text node then trim trailing white space character moving it to after element's end tag.
     */
    public static void trimTrailingSpace(Lexer lexer, Node element, Node last)
    {
        byte c;
        TagTable tt = lexer.configuration.tt;

        if (last != null && last.type == Node.TextNode && last.end > last.start)
        {
            c = lexer.lexbuf[last.end - 1];

            if (c == 160 || c == (byte) ' ')
            {
                /* take care with <td> &nbsp; </td> */
                if (element.tag == tt.tagTd || element.tag == tt.tagTh)
                {
                    if (last.end > last.start + 1)
                    {
                        last.end -= 1;
                    }
                }
                else
                {
                    last.end -= 1;

                    if (((element.tag.model & Dict.CM_INLINE) != 0) && !((element.tag.model & Dict.CM_FIELD) != 0))
                    {
                        lexer.insertspace = true;
                    }

                    /* if empty string then delete from parse tree */
                    if (last.start == last.end)
                    {
                        trimEmptyElement(lexer, last);
                    }
                }
            }
        }
    }

    /*
     * This maps <p> hello <em> world </em> to <p> hello <em> world </em> Trims initial space, by moving it before the
     * start tag, or if this element is the first in parent's content, then by discarding the space
     */
    public static void trimInitialSpace(Lexer lexer, Node element, Node text)
    {
        Node prev, node;

        // GLP: Local fix to Bug 119789. Remove this comment when parser.c is updated.
        //      31-Oct-00.
        if (text.type == TextNode && text.textarray[text.start] == (byte) ' ' && (text.start < text.end))
        {
            if (((element.tag.model & Dict.CM_INLINE) != 0)
                && !((element.tag.model & Dict.CM_FIELD) != 0)
                && element.parent.content != element)
            {
                prev = element.prev;

                if (prev != null && prev.type == TextNode)
                {
                    if (prev.textarray[prev.end - 1] != (byte) ' ')
                    {
                        prev.textarray[prev.end++] = (byte) ' ';
                    }

                    ++element.start;
                }
                else /* create new node */ {
                    node = lexer.newNode();
                    // Local fix for bug 228486 (GLP). This handles the case
                    // where we need to create a preceeding text node but there are
                    // no "slots" in textarray that we can steal from the current
                    // element. Therefore, we create a new textarray containing
                    // just the blank. When Tidy is fixed, this should be removed.
                    if (element.start >= element.end)
                    {
                        node.start = 0;
                        node.end = 1;
                        node.textarray = new byte[1];
                    }
                    else
                    {
                        node.start = element.start++;
                        node.end = element.start;
                        node.textarray = element.textarray;
                    }
                    node.textarray[node.start] = (byte) ' ';
                    node.prev = prev;
                    if (prev != null)
                    {
                        prev.next = node;
                    }
                    node.next = element;
                    element.prev = node;
                    node.parent = element.parent;
                }
            }

            /* discard the space in current node */
            ++text.start;
        }
    }

    /*
     * Move initial and trailing space out. This routine maps: hello <em> world </em> to hello <em> world </em> and
     * <em> hello </em><strong> world </strong> to <em> hello </em><strong> world </strong>
     */
    public static void trimSpaces(Lexer lexer, Node element)
    {
        Node text = element.content;
        TagTable tt = lexer.configuration.tt;

        if (text != null && text.type == Node.TextNode && element.tag != tt.tagPre)
        {
            trimInitialSpace(lexer, element, text);
        }

        text = element.last;

        if (text != null && text.type == Node.TextNode)
        {
            trimTrailingSpace(lexer, element, text);
        }
    }

    public boolean isDescendantOf(Dict tag)
    {
        Node parent;

        for (parent = this.parent; parent != null; parent = parent.parent)
        {
            if (parent.tag == tag)
            {
                return true;
            }
        }

        return false;
    }

    /*
     * the doctype has been found after other tags, and needs moving to before the html element
     */
    public static void insertDocType(Lexer lexer, Node element, Node doctype)
    {
        TagTable tt = lexer.configuration.tt;

        Report.warning(lexer, element, doctype, Report.DOCTYPE_AFTER_TAGS);

        while (element.tag != tt.tagHtml)
        {
            element = element.parent;
        }

        insertNodeBeforeElement(element, doctype);
    }

    public Node findBody(TagTable tt)
    {
        Node node;

        node = this.content;

        while (node != null && node.tag != tt.tagHtml)
        {
            node = node.next;
        }

        if (node == null)
        {
            return null;
        }

        node = node.content;

        while (node != null && node.tag != tt.tagBody)
        {
            node = node.next;
        }

        return node;
    }

    public boolean isElement()
    {
        return (this.type == StartTag || this.type == StartEndTag ? true : false);
    }

    /*
     * unexpected content in table row is moved to just before the table in accordance with Netscape and IE. This code
     * assumes that node hasn't been inserted into the row.
     */
    public static void moveBeforeTable(Node row, Node node, TagTable tt)
    {
        Node table;

        /* first find the table element */
        for (table = row.parent; table != null; table = table.parent)
        {
            if (table.tag == tt.tagTable)
            {
                if (table.parent.content == table)
                {
                    table.parent.content = node;
                }

                node.prev = table.prev;
                node.next = table;
                table.prev = node;
                node.parent = table.parent;

                if (node.prev != null)
                {
                    node.prev.next = node;
                }

                break;
            }
        }
    }

    /*
     * if a table row is empty then insert an empty cell this practice is consistent with browser behavior and avoids
     * potential problems with row spanning cells
     */
    public static void fixEmptyRow(Lexer lexer, Node row)
    {
        Node cell;

        if (row.content == null)
        {
            cell = lexer.inferredTag("td");
            insertNodeAtEnd(row, cell);
            Report.warning(lexer, row, cell, Report.MISSING_STARTTAG);
        }
    }

    public static void coerceNode(Lexer lexer, Node node, Dict tag)
    {
        Node tmp = lexer.inferredTag(tag.name);
        Report.warning(lexer, node, tmp, Report.OBSOLETE_ELEMENT);
        node.was = node.tag;
        node.tag = tag;
        node.type = StartTag;
        node.implicit = true;
        node.element = tag.name;
    }

    /* extract a node and its children from a markup tree */
    public static void removeNode(Node node)
    {
        if (node.prev != null)
        {
            node.prev.next = node.next;
        }

        if (node.next != null)
        {
            node.next.prev = node.prev;
        }

        if (node.parent != null)
        {
            if (node.parent.content == node)
            {
                node.parent.content = node.next;
            }

            if (node.parent.last == node)
            {
                node.parent.last = node.prev;
            }
        }

        node.parent = node.prev = node.next = null;
    }

    public static boolean insertMisc(Node element, Node node)
    {
        if (node.type == CommentTag
            || node.type == ProcInsTag
            || node.type == CDATATag
            || node.type == SectionTag
            || node.type == AspTag
            || node.type == JsteTag
            || node.type == PhpTag)
        {
            insertNodeAtEnd(element, node);
            return true;
        }

        return false;
    }

    /*
     * used to determine how attributes without values should be printed this was introduced to deal with user defined
     * tags e.g. Cold Fusion
     */
    public static boolean isNewNode(Node node)
    {
        if (node != null && node.tag != null)
        {
            return ((node.tag.model & Dict.CM_NEW) != 0);
        }

        return true;
    }

    public boolean hasOneChild()
    {
        return (this.content != null && this.content.next == null);
    }

    /* find html element */
    public Node findHTML(TagTable tt)
    {
        Node node;

        for (node = this.content; node != null && node.tag != tt.tagHtml; node = node.next);

        return node;
    }

    public Node findHEAD(TagTable tt)
    {
        Node node;

        node = this.findHTML(tt);

        if (node != null)
        {
            for (node = node.content; node != null && node.tag != tt.tagHead; node = node.next);
        }

        return node;
    }

    public boolean checkNodeIntegrity()
    {
        Node child;
        boolean found = false;

        if (this.prev != null)
        {
            if (this.prev.next != this)
            {
                return false;
            }
        }

        if (this.next != null)
        {
            if (this.next.prev != this)
            {
                return false;
            }
        }

        if (this.parent != null)
        {
            if (this.prev == null && this.parent.content != this)
            {
                return false;
            }

            if (this.next == null && this.parent.last != this)
            {
                return false;
            }

            for (child = this.parent.content; child != null; child = child.next)
            {
                if (child == this)
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                return false;
            }
        }

        for (child = this.content; child != null; child = child.next)
        {
            if (!child.checkNodeIntegrity())
            {
                return false;
            }
        }
        return true;
    }

    /*
     * Add class="foo" to node
     */
    public static void addClass(Node node, String classname)
    {
        AttVal classattr = node.getAttrByName("class");

        /*
         * if there already is a class attribute then append class name after a space
         */
        if (classattr != null)
        {
            classattr.value = classattr.value + " " + classname;
        }
        else /* create new class attribute */ {
            node.addAttribute("class", classname);
        }
    }

    /* --------------------- DEBUG -------------------------- */

    private static final String[] nodeTypeString =
        {
            "RootNode",
            "DocTypeTag",
            "CommentTag",
            "ProcInsTag",
            "TextNode",
            "StartTag",
            "EndTag",
            "StartEndTag",
            "SectionTag",
            "AspTag",
            "PhpTag" };

    public String toString()
    {
        String s = "";
        Node n = this;

        while (n != null)
        {
            s += "[Node type=";
            s += nodeTypeString[n.type];
            s += ",element=";
            if (n.element != null)
            {
                s += n.element;
            }
            else
            {
                s += "null";
            }
            if (n.type == TextNode || n.type == CommentTag || n.type == ProcInsTag)
            {
                s += ",text=";
                if (n.textarray != null && n.start <= n.end)
                {
                    s += "\"";
                    s += Lexer.getString(n.textarray, n.start, n.end - n.start);
                    s += "\"";
                }
                else
                {
                    s += "null";
                }
            }
            s += ",content=";
            if (n.content != null)
            {
                s += n.content.toString();
            }
            else
            {
                s += "null";
            }
            s += "]";
            if (n.next != null)
            {
                s += ",";
            }
            n = n.next;
        }
        return s;
    }
    /* --------------------- END DEBUG ---------------------- */

    /* --------------------- DOM ---------------------------- */

    protected org.w3c.dom.Node adapter = null;

    protected org.w3c.dom.Node getAdapter()
    {
        if (adapter == null)
        {
            switch (this.type)
            {
                case RootNode :
                    adapter = new DOMDocumentImpl(this);
                    break;
                case StartTag :
                case StartEndTag :
                    adapter = new DOMElementImpl(this);
                    break;
                case DocTypeTag :
                    adapter = new DOMDocumentTypeImpl(this);
                    break;
                case CommentTag :
                    adapter = new DOMCommentImpl(this);
                    break;
                case TextNode :
                    adapter = new DOMTextImpl(this);
                    break;
                case CDATATag :
                    adapter = new DOMCDATASectionImpl(this);
                    break;
                case ProcInsTag :
                    adapter = new DOMProcessingInstructionImpl(this);
                    break;
                default :
                    adapter = new DOMNodeImpl(this);
            }
        }
        return adapter;
    }

    protected Node cloneNode(boolean deep)
    {
        Node node = (Node) this.clone();
        if (deep)
        {
            Node child;
            Node newChild;
            for (child = this.content; child != null; child = child.next)
            {
                newChild = child.cloneNode(deep);
                insertNodeAtEnd(node, newChild);
            }
        }
        return node;
    }

    protected void setType(short newType)
    {
        this.type = newType;
    }

    /* --------------------- END DOM ------------------------ */

}
