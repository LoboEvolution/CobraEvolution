/*
 * GNU GENERAL LICENSE
 * Copyright (C) 2014 - 2023 Lobo Evolution
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * verion 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General License for more details.
 *
 * You should have received a copy of the GNU General Public
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact info: ivan.difrancesco@yahoo.it
 */
/*
 * Created on Oct 29, 2005
 */
package org.loboevolution.html.dom.nodeimpl;

import com.gargoylesoftware.css.dom.DOMException;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.SelectorList;
import org.loboevolution.common.Nodes;
import org.loboevolution.common.Strings;
import org.loboevolution.gui.HtmlRendererContext;
import org.loboevolution.html.CSSValues;
import org.loboevolution.html.dom.HTMLBodyElement;
import org.loboevolution.html.dom.HTMLCollection;
import org.loboevolution.html.dom.domimpl.*;
import org.loboevolution.html.dom.filter.ClassNameFilter;
import org.loboevolution.html.dom.filter.ElementFilter;
import org.loboevolution.html.dom.filter.TagNameFilter;
import org.loboevolution.html.dom.filter.TagNsNameFilter;
import org.loboevolution.gui.HtmlPanel;
import org.loboevolution.html.js.geom.DOMRectImpl;
import org.loboevolution.html.js.geom.DOMRectListImpl;
import org.loboevolution.html.node.*;
import org.loboevolution.html.node.css.CSSStyleDeclaration;
import org.loboevolution.html.node.js.Window;
import org.loboevolution.html.node.js.geom.DOMRect;
import org.loboevolution.html.node.js.geom.DOMRectList;
import org.loboevolution.html.parser.XHtmlParser;
import org.loboevolution.html.renderer.RBlock;
import org.loboevolution.html.renderstate.RenderState;
import org.loboevolution.html.style.CSSUtilities;
import org.loboevolution.html.style.HtmlValues;
import org.loboevolution.html.style.StyleSheetAggregator;
import org.mozilla.javascript.annotations.JSFunction;

import javax.swing.*;
import java.awt.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

/**
 * <p>ElementImpl class.</p>
 */
public class ElementImpl extends WindowEventHandlersImpl implements Element {

	private final NamedNodeMapImpl map;

	private final String name;

	private String outer;

	/**
	 * <p>Constructor for ElementImpl.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public ElementImpl(final String name) {
		this.name = name;
		this.map = new NamedNodeMapImpl(this, new NodeListImpl());
	}

	/** {@inheritDoc} */
	@Override
	public boolean equalAttributes(Node arg) {
		if (arg instanceof ElementImpl) {
			return Objects.equals(map, arg.getAttributes());
		} else {
			return false;
		}
	}

	/** {@inheritDoc} */
	@Override
	@JSFunction
	public String getAttribute(String name) {
		final Attr attr = getAttributeNode(name);
		return attr == null ? null : attr.getValue();
	}

	/** {@inheritDoc} */
	@Override
	@JSFunction
	public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
		final Attr attr = getAttributeNodeNS(namespaceURI, localName);
		if (attr != null) {
			return attr.getValue();
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	@JSFunction
	public Attr getAttributeNode(String name) {
		AttrImpl attribute = (AttrImpl) map.getNamedItem(name);
		if (attribute != null) {
			attribute.setSpecified(true);
			return attribute;
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	@JSFunction
	public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {

		if (Strings.isBlank(namespaceURI)) {
			return getAttributeNode(localName);
		}

		String local = localName.contains(":") ? localName.split(":")[1] : localName;
		AttrImpl attribute = (AttrImpl) map.getNamedItemNS(namespaceURI, local);
		if (attribute != null) {
			attribute.setSpecified(true);
			return attribute;
		}

		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void removeAttribute(String name) {
		try {
			map.removeNamedItem(name);
		} catch (DOMException ex) {
			logger.severe(ex.getMessage());
		}
	}

	/** {@inheritDoc} */
	@Override
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		map.removeNamedItemNS(namespaceURI, localName);
	}

	/** {@inheritDoc} */
	@Override
	public Attr removeAttributeNode(Attr oldAttr) {

		if (oldAttr.getOwnerElement() == null) {
			throw new DOMException(DOMException.NOT_FOUND_ERR, "refChild not found");
		}

		oldAttr.setOwnerElement(null);
		try {
			return (Attr) map.removeNamedItem(oldAttr.getLocalName());
		} catch (DOMException ex) {
			try {
				return (Attr) map.removeNamedItemNS("*", oldAttr.getLocalName());
			} catch (DOMException ex1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "Attribute not found");
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void setAttribute(String name, String value) {
		String prefix = null;
		if (Strings.isBlank(name)) {
			throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "The qualified name contains null value");
		}

		if (name.contains(":")) {
			String[] split = name.split(":");
			if (split.length != 2) {
				throw new DOMException(DOMException.NAMESPACE_ERR, "The qualified name provided has an empty local name.");
			}
			if (Strings.isBlank(split[0]) || Strings.isBlank(split[1])) {
				throw new DOMException(DOMException.NAMESPACE_ERR, "The qualified name provided has an empty local name.");
			}

			if (!Strings.isXMLIdentifier(split[0]) || !Strings.isXMLIdentifier(split[1])) {
				throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "The qualified name contains the invalid character");
			}

			prefix = split[0];
			name = split[1];
		} else {
			if (!Strings.isXMLIdentifier(name)) {
				throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "The qualified name contains the invalid character");
			}
		}

		Node node = map.getNamedItem(name);
		if (node != null) {
			removeAttributeNode((AttrImpl) node);
		}

		final AttrImpl attr = new AttrImpl(name, value, "id".equalsIgnoreCase(name), this, true);
		Document doc = getOwnerDocument();
		attr.setOwnerDocument(doc);
		attr.setNamespaceURI(getNamespaceURI() != null ? getNamespaceURI() : doc != null ? doc.getNamespaceURI() : getParentNode() != null ? getParentNode().getNamespaceURI() : null);
		if (Strings.isNotBlank(prefix) && Strings.isNotBlank(attr.getNamespaceURI())) {
			attr.setPrefix(prefix);
		}
		map.setNamedItem(attr);
		assignAttributeField(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
		String prefix = null;

		if (Strings.isBlank(qualifiedName)) {
			throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "The qualified name contains null value");
		}

		if (qualifiedName.contains(":")) {
			String[] split = qualifiedName.split(":");
			if (split.length != 2) {
				throw new DOMException(DOMException.NAMESPACE_ERR, "The qualified name provided has an empty local name.");
			}

			if (Strings.isBlank(split[0]) || Strings.isBlank(split[1])) {
				throw new DOMException(DOMException.NAMESPACE_ERR, "The qualified name provided has an empty local name.");
			}

			if (split[0].equals("xml") && !"http://www.w3.org/XML/1998/namespace".equals(namespaceURI)) {
				throw new DOMException(DOMException.NAMESPACE_ERR, "The namespaceURI is not http://www.w3.org/XML/1998/namespace.");
			}

			if (!Strings.isXMLIdentifier(split[0]) || !Strings.isXMLIdentifier(split[1])) {
				throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "The qualified name contains the invalid character");
			}

			prefix = split[0];
			qualifiedName = split[1];

		} else {
			if (!Strings.isXMLIdentifier(qualifiedName)) {
				throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "The qualified name contains the invalid character");
			}
		}

		Node node = map.getNamedItemNS(namespaceURI, qualifiedName);

		if (node != null && Strings.isNotBlank(namespaceURI) && namespaceURI.equals(node.getNamespaceURI())) {
			removeAttributeNode((AttrImpl) node);
		}

		final AttrImpl attr = new AttrImpl(qualifiedName, value, "id".equalsIgnoreCase(name), this, true);
		attr.setNamespaceURI(namespaceURI);
		attr.setOwnerDocument(getOwnerDocument());
		if (Strings.isNotBlank(prefix)) attr.setPrefix(prefix);
		map.setNamedItemNS(attr);
		assignAttributeField(qualifiedName, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setIdAttribute(String localName, boolean isId) throws DOMException {
		final AttrImpl attr = (AttrImpl)getAttributeNode(name);
		if(attr != null) attr.setNameId(isId);
	}

		/** {@inheritDoc} */
	@Override
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
		final AttrImpl attr = (AttrImpl) getAttributeNodeNS(namespaceURI, localName);
		if (attr != null) {
			attr.setNameId(isId);
		}
	}
	/** {@inheritDoc} */
	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) {
		Attr checkAttr = getAttributeNode(idAttr.getName());
		if(checkAttr == null) {
			throw new DOMException(DOMException.NOT_FOUND_ERR, "Attribute not found");
		}

		final AttrImpl attr = (AttrImpl)idAttr;
		attr.setNameId(isId);
	}

	/** {@inheritDoc} */
	@Override
	public Attr setAttributeNode(Attr newAttr) {
		Attr checkAttr = getAttributeNode(newAttr.getName());

		if (checkAttr == null && Objects.equals(newAttr.getOwnerElement(), this)) {
			throw new DOMException(DOMException.INUSE_ATTRIBUTE_ERR,
					"Attr is already an attribute of another Element object. The DOM user must explicitly clone Attr nodes to re-use them in other elements.");
		}

		if(!Objects.equals(newAttr.getOwnerDocument(), getOwnerDocument())) {
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, "Different Document");
		}

		if (checkAttr != null && Strings.isBlank(checkAttr.getNamespaceURI())) {
			removeAttributeNode(checkAttr);
		}

		newAttr.setOwnerElement(this);
		assignAttributeField(newAttr.getName(), newAttr.getValue());
		return (Attr) map.setNamedItem(newAttr);
	}

	/** {@inheritDoc} */
	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		Attr checkAttr = getAttributeNodeNS(newAttr.getNamespaceURI(), newAttr.getLocalName());
		if (checkAttr == null && Objects.equals(newAttr.getOwnerElement(), this)) {
			throw new DOMException(DOMException.INUSE_ATTRIBUTE_ERR,
					"Attr is already an attribute of another Element object. The DOM user must explicitly clone Attr nodes to re-use them in other elements.");
		}

		if(!Objects.equals(newAttr.getOwnerDocument(), getOwnerDocument())) {
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, "Different Document");
		}

		if (checkAttr != null && Objects.equals(checkAttr.getNamespaceURI(), newAttr.getNamespaceURI())) {
			removeAttributeNS(newAttr.getNamespaceURI(), newAttr.getLocalName());
		}

		newAttr.setOwnerElement(this);
		assignAttributeField(newAttr.getName(), newAttr.getValue());
		return (Attr) map.setNamedItemNS(newAttr);
	}

	/** {@inheritDoc} */
	@Override
	public NamedNodeMap getAttributes() {
		return this.map;
	}

	/**
	 * <p>getDir.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDir() {
		return getAttribute("dir");
	}

	/**
	 * <p>Getter for the field id.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId() {
		String id = getAttribute("id");
		id = Strings.isBlank(id) ? getAttributeNS("*", "id") : id;
		return id == null ? "" : id;
	}

	/**
	 * <p>getLang.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLang() {
		return getAttribute("lang");
	}

	/** {@inheritDoc} */
	@Override
	public String getLocalName() {
		if (this.name.contains(":")) {
			return this.name.split(":")[1];
		}
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public String getNodeName() {
		StringBuilder builder = new StringBuilder();
		if (Strings.isNotBlank(getPrefix())) {
			builder.append(getPrefix()).append(":");
		}

		return builder.append(getLocalName()).toString();
	}

	/** {@inheritDoc} */
	@Override
	public int getNodeType() {
		return Node.ELEMENT_NODE;
	}

	/** {@inheritDoc} */
	@Override
	public String getNodeValue() {
		return null;
	}

	/**
	 * Gets inner text of the element, possibly including text in comments. This can
	 * be used to get Javascript code out of a SCRIPT element.
	 *
	 * @param includeComment a boolean.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getRawInnerText(boolean includeComment) {
		StringBuilder sb = null;
		for (Node node : nodeList) {
			if (node instanceof Text) {
				final Text tn = (Text) node;
				final String txt = tn.getNodeValue();
				if (Strings.isNotBlank(txt)) {
					if (sb == null) {
						sb = new StringBuilder();
					}
					sb.append(txt);
				}
			} else if (node instanceof ElementImpl) {
				final ElementImpl en = (ElementImpl) node;
				final String txt = en.getRawInnerText(includeComment);
				if (Strings.isNotBlank(txt)) {
					if (sb == null) {
						sb = new StringBuilder();
					}
					sb.append(txt);
				}
			} else if (includeComment && node instanceof Comment) {
				final Comment cn = (Comment) node;
				final String txt = cn.getNodeValue();
				if (Strings.isNotBlank(txt)) {
					if (sb == null) {
						sb = new StringBuilder();
					}
					sb.append(txt);
				}
			}
		}
		return sb == null ? "" : sb.toString();

	}

	/** {@inheritDoc} */
	@Override
	public String getTagName() {
		return getNodeName();
	}

	/**
	 * <p>getTitle.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return getAttribute("title");
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAttribute(String name) {
		return map.getNamedItem(name) != null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {

		if (Strings.isBlank(localName)) {
			throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "null localName");
		}

		return map.getNamedItemNS(namespaceURI, localName) != null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAttributes() {
		return map.getAttributes().getLength() > 0;
	}

	/** {@inheritDoc} */
	@Override
	protected String htmlEncodeChildText(String text) {
		if (XHtmlParser.isDecodeEntities(this.name)) {
			return Strings.strictHtmlEncode(text, false);
		} else {
			return text;
		}
	}

	/**
	 * <p>setDir.</p>
	 *
	 * @param dir a {@link java.lang.String} object.
	 */
	public void setDir(String dir) {
		setAttribute("dir", dir);
	}

	/** {@inheritDoc} */
	@Override
	public void setId(String id) {
		setAttribute("id", id);
	}

	/**
	 * <p>setInnerText.</p>
	 *
	 * @param newText a {@link java.lang.String} object.
	 */
	public void setInnerText(String newText) {
		final Document document = this.document;
		if (document != null) {
			this.nodeList.clear();
			final Node textNode = document.createTextNode(newText);
			appendChild(textNode);
		} else {
			this.warn("setInnerText(): Element " + this + " does not belong to a document.");
		}
	}

	/**
	 * <p>setLang.</p>
	 *
	 * @param lang a {@link java.lang.String} object.
	 */
	public void setLang(String lang) {
		setAttribute("lang", lang);
	}

	/** {@inheritDoc} */
	@Override
	public void setNodeValue(String nodeValue) {
		// nop
	}

	/**
	 * <p>setTitle.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 */
	public void setTitle(String title) {
		setAttribute("title", title);
	}

	/** {@inheritDoc} */
	@Override
	public void setInnerHTML(String newHtml) {
		final HTMLDocumentImpl document = (HTMLDocumentImpl) this.document;
		if (document != null) {
			final XHtmlParser parser = new XHtmlParser(document.getUserAgentContext(), document, false);
			this.nodeList.clear();
			try {
				try (Reader reader = new StringReader(newHtml)) {
					parser.parse(reader, this);
				}
			} catch (final Exception thrown) {
				this.warn("setInnerHTML(): Error setting inner HTML.", thrown);
			}
		} else {
			this.warn("setInnerHTML(): Element " + this + " does not belong to a document.");
		}
	}

	/**
	 * <p>Getter for the field <code>outer</code>.</p>
	 *
	 * @return the outer
	 */
	public String getOuter() {
		return outer;
	}

	/**
	 * <p>Setter for the field <code>outer</code>.</p>
	 *
	 * @param outer the outer to set
	 */
	public void setOuter(String outer) {
		this.outer = outer;
	}

	/** {@inheritDoc} */
	@Override
	public DOMTokenList getClassList() {
		DOMTokenListImpl tokList = new DOMTokenListImpl(this);
		final String className = getClassName();
		if(Strings.isNotBlank(className)){
			final String[] listString = className.split(" ");
			List<String> names = Arrays.asList(listString);
			names.forEach(tokList::populate);
		}
        return tokList;
	}

	/** {@inheritDoc} */
	@Override
	public String getClassName() {
		final String className = getAttribute("class");
		return className == null ? "" : className;
	}

	/** {@inheritDoc} */
	@Override
	public void setClassName(String className) {
		setAttribute("class", className);
	}

	/** {@inheritDoc} */
	@Override
	public int getClientHeight() {
		return calculateHeight(false, true);
	}

	/** {@inheritDoc} */
	@Override
	public int getClientLeft() {
		final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		CSSStyleDeclaration currentStyle = ((HTMLElementImpl)this).getCurrentStyle();
		return HtmlValues.getPixelSize(currentStyle.getBorderLeftWidth(), null, doc.getDefaultView(), 0);
	}

	/** {@inheritDoc} */
	@Override
	public int getClientTop() {
		final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		CSSStyleDeclaration currentStyle = ((HTMLElementImpl)this).getCurrentStyle();
		return HtmlValues.getPixelSize(currentStyle.getBorderTopWidth(), null, doc.getDefaultView(), 0);
	}

	/** {@inheritDoc} */
	@Override
	public int getClientWidth() {
		return calculateWidth(false, true);
	}

	/** {@inheritDoc} */
	@Override
	public String getOuterHTML() {
		final StringBuilder buffer = new StringBuilder();
		synchronized (this) {
			appendOuterHTMLImpl(buffer);
		}
		return buffer.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void setOuterHTML(String newHtml) {
		this.outer = outerNewHtml(newHtml);
		if (this.outer != null) {
			final HTMLDocumentImpl document = (HTMLDocumentImpl) this.document;
			if (document != null) {
				final XHtmlParser parser = new XHtmlParser(document.getUserAgentContext(), document, false);
				this.nodeList.clear();
				try {
					try (Reader reader = new StringReader(newHtml)) {
						parser.parse(reader, this);
					}
				} catch (final Exception thrown) {
					this.warn("setOuterHTML(): Error setting inner HTML.", thrown);
				}
			} else {
				this.warn("setOuterHTML(): Element " + this + " does not belong to a document.");
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getSlot() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setSlot(String slot) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public <E extends Element> E closest(String selector) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String[] getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasPointerCapture(int pointerId) {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean matches(String selectors) {
		if (Strings.isBlank(selectors)) {
			throw new DOMException(DOMException.NOT_FOUND_ERR, "The provided selector is empty.");
		}

		try {
			SelectorList selectorList = CSSUtilities.getSelectorList(selectors);
			if (selectorList != null) {
				for (Selector select : selectorList) {
					if (StyleSheetAggregator.selects(select, this, null)) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "Is not a valid selector.");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void releasePointerCapture(int pointerId) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public void requestPointerLock() {
		// TODO Auto-generated method stub

	}
	/** {@inheritDoc} */
	@Override
	public void scroll(int x, int y) {
		setScrollLeft(x);
		setScrollTop(y);
	}

	/** {@inheritDoc} */
	@Override
	public void scrollBy(int x, int y) {
		scroll(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public void scrollIntoView(boolean arg) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public void scrollIntoView() {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public void scrollTo(int x, int y) {
		scroll(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public double getScrollHeight() {
		return isVScrollable() ? getClientHeight() : 0;
	}

	/** {@inheritDoc} */
	@Override
    public double getScrollLeft() {
        final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
        HtmlRendererContext htmlRendererContext = doc.getHtmlRendererContext();
        return isHScrollable() ? htmlRendererContext.getScrollx() : 0;
    }

	/** {@inheritDoc} */
	@Override
    public void setScrollLeft(double scrollLeft) {
        final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
        HtmlRendererContext htmlRendererContext = doc.getHtmlRendererContext();

        if (scrollLeft < 0 || !isHScrollable()) {
            scrollLeft = 0;
        }

        htmlRendererContext.setScrollx(scrollLeft);
        RBlock bodyBlock = (RBlock) this.getUINode();
        if (bodyBlock != null && bodyBlock.getScroll() != null)
            bodyBlock.getScroll().scrollBy(JScrollBar.HORIZONTAL, scrollLeft);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getScrollTop() {
        final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
        HtmlRendererContext htmlRendererContext = doc.getHtmlRendererContext();
		return isVScrollable() ? htmlRendererContext.getScrolly() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScrollTop(double scrollTop) {
        final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
        HtmlRendererContext htmlRendererContext = doc.getHtmlRendererContext();

        if (scrollTop < 0 || !isVScrollable()) {
            scrollTop = 0;
        }

        htmlRendererContext.setScrolly(scrollTop);
        RBlock bodyBlock = (RBlock) this.getUINode();
        if (bodyBlock != null && bodyBlock.getScroll() != null)
            bodyBlock.getScroll().scrollBy(JScrollBar.VERTICAL, scrollTop);
    }

	/** {@inheritDoc} */
	@Override
	public double getScrollWidth() {
		return isHScrollable() ? getClientWidth() : 0;
	}

	/** {@inheritDoc} */
	@Override
	public void setPointerCapture(int pointerId) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public boolean toggleAttribute(String qualifiedName, boolean force) {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean toggleAttribute(String qualifiedName) {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public DOMRect getBoundingClientRect() {

		CSSStyleDeclaration currentStyle = ((HTMLElementImpl) this).getStyle();
		final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		final Window win = doc.getDefaultView();
		final RenderState rs  = doc.getRenderState();
		int width = calculateWidth(true, true);
		int height = calculateHeight(true, true);
		String position = currentStyle.getPosition();
		int topLeft = currentStyle.getLength() > 0 ? 8 : 0;
		int top = topLeft;
		int left = topLeft;

		if(CSSValues.ABSOLUTE.isEqual(position)){
			top = HtmlValues.getPixelSize(currentStyle.getTop(), rs, win, 0);
			left = HtmlValues.getPixelSize(currentStyle.getLeft(), rs, win, 0);
		}

		for (Node n = getParentNode(); n != null; n = n.getParentNode()) {

			if (!(n instanceof HTMLBodyElement) && !(n instanceof TextImpl) && !(n instanceof HTMLDocumentImpl) && CSSValues.ABSOLUTE.isEqual(position)) {
				HTMLElementImpl p = (HTMLElementImpl) n;
				CSSStyleDeclaration pCurrentStyle = p.getStyle();
				String topTxt = pCurrentStyle.getTop();
				String leftTxt = pCurrentStyle.getLeft();
				int scrollTop = (int) p.getScrollTop();
				int scrollLeft = (int) p.getScrollLeft();
				if (Strings.isNotBlank(topTxt)) {
					top += HtmlValues.getPixelSize(topTxt, rs, win, 0);
				}

				if (Strings.isNotBlank(leftTxt)) {
					left += HtmlValues.getPixelSize(leftTxt, rs, win, 0);
				}

				top -= scrollTop;
				left -= scrollLeft;
			}
		}

		final HTMLElementImpl elem = ((HTMLElementImpl) this);
		final int bottom = (int) (top + elem.getOffsetHeight());
		final int right = left + 50;
		return new DOMRectImpl(width, height, top, right, bottom, left);
	}

	/** {@inheritDoc} */
	@Override
	public DOMRectList getClientRects() {
		DOMRectListImpl list = new DOMRectListImpl();
		CSSStyleDeclaration style = ((HTMLElementImpl) this).getCurrentStyle();
		String display = Strings.isNotBlank(style.getDisplay()) ? style.getDisplay() : getAttribute("display");
		if (!"none".equals(display)) {
			for (Node n = getParentNode(); n != null; n = n.getPreviousSibling()) {
				if (!(n instanceof HTMLBodyElement) && !(n instanceof TextImpl) && !(n instanceof HTMLDocumentImpl)) {
					HTMLElementImpl p = (HTMLElementImpl) n;
					CSSStyleDeclaration st = p.getStyle();
					display = st.getDisplay();
				}
			}
		}

		if (!"none".equals(display)) {
			list.add(getBoundingClientRect());
		}

		return list;
	}

	/** {@inheritDoc} */
	@Override
	public TypeInfo getSchemaTypeInfo() {
		return new AttributeTypeInfo(false);
	}

	/** {@inheritDoc} */
	@Override
	public HTMLCollection getElementsByClassName(String classNames) {
		return new HTMLCollectionImpl(this, Arrays.asList(this.getNodeList(new ClassNameFilter(classNames)).toArray()));
	}

	/** {@inheritDoc} */
	@Override
	public HTMLCollection getElementsByTagName(String tagname) {
		if ("*".equals(tagname)) {
			return new HTMLCollectionImpl(this, Arrays.asList(this.getNodeList(new ElementFilter(null)).toArray()));
		} else {
			return new HTMLCollectionImpl(this, Arrays.asList(this.getNodeList(new TagNameFilter(tagname)).toArray()));
		}
	}

	/** {@inheritDoc} */
	@Override
	public HTMLCollection getElementsByTagNameNS(String namespaceURI, String localName) {

		if("*".equals(namespaceURI) && "*".equals(localName)) {
			return new HTMLCollectionImpl(this, Arrays.asList(this.getNodeList(new ElementFilter(null)).toArray()));
		}

		return new HTMLCollectionImpl(this, Arrays.asList(this.getNodeList(new TagNsNameFilter(localName, namespaceURI)).toArray()));
	}

	/** {@inheritDoc} */
	@Override
	public Element getFirstElementChild() {
		return (Element) nodeList.stream().filter(n -> n instanceof Element).findFirst().orElse(null);
	}

	/** {@inheritDoc} */
	@Override
	public Element getLastElementChild() {
		long count = nodeList.stream().filter(n -> n instanceof Element).count();
		if(count == 0) count = 1;
		Stream<Node> stream = nodeList.stream();
		return (Element) stream.filter(n -> n instanceof Element).skip(count - 1).findFirst().orElse(null);
	}

	/** {@inheritDoc} */
	@Override
	public int getChildElementCount() {
		return (int) nodeList.stream().filter(n -> n instanceof Element).count();
	}

	/** {@inheritDoc} */
	@Override
	public Element querySelector(String selectors) {
		try {
			SelectorList selectorList = CSSUtilities.getSelectorList(selectors);
			List<Element> elem = new ArrayList<>();
			if (selectorList != null) {
				NodeListImpl childNodes = (NodeListImpl) getDescendents(new ElementFilter(null), true);
				childNodes.forEach(child -> {
					for (Selector selector : selectorList) {
						if (child instanceof Element && StyleSheetAggregator.selects(selector, child, null)) {
							elem.add((Element) child);
						}
					}
				});
			}
			return elem.size() > 0 ? elem.get(0) : null;
		} catch (Exception e) {
			throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "Is not a valid selector.");
		}
	}

	/** {@inheritDoc} */
	@Override
	public NodeList querySelectorAll(String selector) {

		final ArrayList<Node> al = new ArrayList<>();

		if(selector == null) {
			return new NodeListImpl(al);
		}

		if(selector.isEmpty()){
			throw new DOMException(DOMException.NOT_FOUND_ERR, "The provided selector is empty.");
		}

		if(selector.trim().isEmpty()){
			throw new DOMException(DOMException.NOT_FOUND_ERR, "is not a valid selector.");
		}

		try {
			SelectorList selectorList = CSSUtilities.getSelectorList(selector);
			if (selectorList != null) {
				NodeListImpl childNodes = (NodeListImpl) getDescendents(new ElementFilter(null), true);
				childNodes.forEach(child -> {
					for (Selector select : selectorList) {
						if (child instanceof Element && StyleSheetAggregator.selects(select, child, null)) {
							al.add(child);
						}
					}
				});
			}
			return new NodeListImpl(al);
		} catch (Exception e) {
			throw new DOMException(DOMException.INVALID_CHARACTER_ERR, "Is not a valid selector.");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void assignAttributeField(String normalName, String value) {
		boolean isName = false;
		if ("id".equalsIgnoreCase(normalName) || (isName = "name".equals(normalName))) {
			final HTMLDocumentImpl document = (HTMLDocumentImpl) this.document;
			if (document != null) {
				document.setElementById(value, this);
				if (isName) {
					final String oldName = getAttribute("name");
					if (oldName != null) {
						document.removeNamedItem(oldName);
					}
					document.setNamedItem(value, this);
				}
			}
		}
	}

	/**
	 * <p>appendOuterHTMLImpl.</p>
	 *
	 * @param buffer a {@link java.lang.StringBuilder} object.
	 */
	public void appendOuterHTMLImpl(StringBuilder buffer) {
		final String tagName = getTagName().toUpperCase();
		buffer.append('<');
		buffer.append(tagName);
		for (Node attrNode : Nodes.iterable(map)) {
			Attr attr = (Attr) attrNode;
			buffer.append(' ');
			buffer.append(attr.getName());
			buffer.append("=\"");
			buffer.append(Strings.strictHtmlEncode(attr.getValue(), true));
			buffer.append("\"");
		}

		if (nodeList.getLength() == 0) {
			buffer.append("/>");
		} else {
			buffer.append('>');
			appendInnerHTMLImpl(buffer);
			buffer.append("</");
			buffer.append(tagName);
			buffer.append('>');
		}
	}

	private String outerNewHtml(final String newHtml) {
		if (newHtml != null) {
			return newHtml.endsWith(">") || ! newHtml.startsWith("<") ? newHtml : newHtml + ">";
		}
		return "";
	}

    private boolean isHScrollable() {
        String overflow;
        CSSStyleDeclaration currentStyle = ((HTMLElementImpl) this).getStyle();
        overflow = currentStyle.getOverflow();
        int widthChild = 0;

        for (final Node child : (NodeListImpl) this.getChildNodes()) {
            if (child instanceof HTMLElementImpl) widthChild += ((HTMLElementImpl) child).getClientWidth();
        }

        return ("scroll".equals(overflow) || "auto".equals(overflow)) && (widthChild > this.getClientWidth());
    }

    private boolean isVScrollable() {
        String overflow;
        CSSStyleDeclaration currentStyle = ((HTMLElementImpl) this).getStyle();
        overflow = currentStyle.getOverflow();
        int heightChild = 0;

        for (final Node child : (NodeListImpl) this.getChildNodes()) {
            if (child instanceof HTMLElementImpl) heightChild += ((HTMLElementImpl) child).getClientHeight();
        }

        return ("scroll".equals(overflow) || "auto".equals(overflow)) && (heightChild > this.getClientHeight());
    }

	protected int calculateWidth(boolean border, boolean padding) {
		final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		final HtmlRendererContext htmlRendererContext = doc.getHtmlRendererContext();
		final HtmlPanel htmlPanel = htmlRendererContext.getHtmlPanel();
		final Dimension preferredSize = htmlPanel.getPreferredSize();
		final CSSStyleDeclaration currentStyle = ((HTMLElementImpl)this).getStyle();
		String width = currentStyle.getWidth();
		String borderLeftWidth = currentStyle.getBorderLeftWidth();
		String borderRightWidth = currentStyle.getBorderRightWidth();
		String boxSizing = currentStyle.getBoxSizing();
		String position = currentStyle.getPosition();
		String display = currentStyle.getDisplay();
		String cssFloat = currentStyle.getFloat();
		int sizeWidth = preferredSize.width;

		if (getParentNode() == null || CSSValues.INLINE.isEqual(display) || CSSValues.NONE.isEqual(display)) {
			return 0;
		}

		if (this instanceof HTMLBodyElementImpl) {
			width = String.valueOf(doc.getDefaultView().getInnerWidth());
		}

		if(Strings.isBlank(width)){
			width = "-1px";
		}

		final Node nodeObj = getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl elem = (HTMLElementImpl)nodeObj;
			if(elem.getClientHeight() != -1) {
				sizeWidth = elem.getClientWidth();
			}
		}

		if ((CSSValues.RIGHT.isEqual(cssFloat) || CSSValues.LEFT.isEqual(cssFloat)) ||
				(CSSValues.ABSOLUTE.isEqual(position))) {

			if (Strings.isNotBlank(getTextContent())) {
				width = String.valueOf(getTextContent().length() * 4);
			} else {
				width = "0px";
			}
		}

		if (Strings.isNotBlank(display) && !CSSValues.INLINE.isEqual(display) && (Strings.isBlank(width) || "auto".equalsIgnoreCase(width) || "-1px".equals(width))) {
			width = "100%";
		}

		int widthSize = HtmlValues.getPixelSize(width, null, doc.getDefaultView(), -1, sizeWidth);

		if ("border-box".equals(boxSizing)) {
			padding = false;
			border = false;
		}

		if (padding) {
			String paddingRight = currentStyle.getPaddingRight();
			String paddingLeft = currentStyle.getPaddingLeft();
			widthSize += HtmlValues.getPixelSize(paddingRight, null, doc.getDefaultView(), 0);
			widthSize += HtmlValues.getPixelSize(paddingLeft, null, doc.getDefaultView(), 0);
		}


		if (border) {
			widthSize += HtmlValues.getPixelSize(borderRightWidth, null, doc.getDefaultView(), 0);
			widthSize += HtmlValues.getPixelSize(borderLeftWidth, null, doc.getDefaultView(), 0);
		}
		return widthSize;
	}

	protected int calculateHeight(boolean border, boolean padding) {
		final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		final HtmlRendererContext htmlRendererContext = doc.getHtmlRendererContext();
		final HtmlPanel htmlPanel = htmlRendererContext.getHtmlPanel();
		final Dimension preferredSize = htmlPanel.getPreferredSize();
		final CSSStyleDeclaration currentStyle = ((HTMLElementImpl)this).getStyle();
		String height = currentStyle.getHeight();
		String borderTopWidth = currentStyle.getBorderTopWidth();
		String borderBottomWidth = currentStyle.getBorderBottomWidth();
		String boxSizing = currentStyle.getBoxSizing();
		String dispaly = currentStyle.getDisplay();
		int sizeHeight = preferredSize.height;

		if (getParentNode() == null || CSSValues.NONE.isEqual(dispaly)) {
			return 0;
		}

		if(Strings.isBlank(height)){
			height = "-1px";
		}

		if (this instanceof HTMLBodyElementImpl) {
			height = String.valueOf(doc.getDefaultView().getInnerHeight());
		}

		final Node nodeObj = getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl elem = (HTMLElementImpl)nodeObj;
			if(elem.getClientHeight() != -1) {
				sizeHeight = elem.getClientHeight();
			}
		}

		switch (height) {
			case "auto":
				height = "100%";
				break;
			case "-1px":
				if (Strings.isBlank(getTextContent())) {
					height = "0px";
				} else {
					height = "18px";
				}
				break;
			default:
				break;
		}

		int heightSize = HtmlValues.getPixelSize(height, null, doc.getDefaultView(), -1, sizeHeight);

		if ("border-box".equals(boxSizing)) {
			padding = false;
			border = false;
		}

		if (padding) {
			String paddingTop = currentStyle.getPaddingTop();
			String paddingBottom = currentStyle.getPaddingBottom();
			heightSize += HtmlValues.getPixelSize(paddingTop, null, doc.getDefaultView(), 0);
			heightSize += HtmlValues.getPixelSize(paddingBottom, null, doc.getDefaultView(), 0);
		}

		if (border) {
			heightSize += HtmlValues.getPixelSize(borderTopWidth, null, doc.getDefaultView(), 0);
			heightSize += HtmlValues.getPixelSize(borderBottomWidth, null, doc.getDefaultView(), 0);
		}

		return heightSize;
	}
}
