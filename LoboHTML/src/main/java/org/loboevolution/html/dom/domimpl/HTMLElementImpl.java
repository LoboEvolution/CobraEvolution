/*
 * MIT License
 *
 * Copyright (c) 2014 - 2023 LoboEvolution
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Contact info: ivan.difrancesco@yahoo.it
 */
/*
 * Created on Sep 3, 2005
 */
package org.loboevolution.html.dom.domimpl;

import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl;
import org.htmlunit.cssparser.dom.Property;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.javacc.CSS3Parser;
import org.loboevolution.common.Strings;
import org.loboevolution.html.CSSValues;
import org.loboevolution.html.dom.HTMLElement;
import org.loboevolution.html.dom.input.FormInput;
import org.loboevolution.html.dom.nodeimpl.ElementImpl;
import org.loboevolution.html.dom.nodeimpl.NodeListImpl;
import org.loboevolution.html.js.css.CSSStyleDeclarationImpl;
import org.loboevolution.html.node.Element;
import org.loboevolution.html.node.Node;
import org.loboevolution.html.node.css.CSSStyleDeclaration;
import org.loboevolution.html.node.css.ComputedCSSStyleDeclaration;
import org.loboevolution.html.renderer.HtmlController;
import org.loboevolution.html.renderstate.RenderState;
import org.loboevolution.html.renderstate.StyleSheetRenderState;
import org.loboevolution.html.style.CSSPropertiesContext;
import org.loboevolution.html.style.ComputedCSSStyleDeclarationImpl;
import org.loboevolution.html.style.HtmlValues;
import org.loboevolution.html.style.StyleSheetAggregator;
import org.loboevolution.info.PropertyCssInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * <p>HTMLElementImpl class.</p>
 */
public class HTMLElementImpl extends ElementImpl implements HTMLElement, CSSPropertiesContext {

	private ComputedCSSStyleDeclaration computedStyles;

	private volatile CSSStyleDeclaration currentStyleDeclarationState;

	private CSSStyleDeclarationImpl localStyleDeclarationState = null;

	private boolean hasMouseOver;
	
	/**
	 * <p>Constructor for HTMLElementImpl.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public HTMLElementImpl(final String name) {
		super(name);
	}

	/** {@inheritDoc} */
	@Override
	public void assignAttributeField(String normalName, String value) {
		if (!this.notificationsSuspended) {
			informInvalidAttibute(normalName);
		} else {
			if ("style".equals(normalName)) {
				forgetLocalStyle();
			}
		}
		super.assignAttributeField(normalName, value);
	}


	/** {@inheritDoc} */
	@Override
	protected RenderState createRenderState(RenderState prevRenderState) {
		return new StyleSheetRenderState(prevRenderState, this);
	}

	/**
	 * <p>forgetLocalStyle.</p>
	 */
	protected final void forgetLocalStyle() {
		synchronized (this) {
			this.currentStyleDeclarationState = null;
			this.localStyleDeclarationState = null;
			this.computedStyles = null;
		}
	}

	/**
	 * <p>forgetStyle.</p>
	 *
	 * @param deep a boolean.
	 */
	protected final void forgetStyle(boolean deep) {
		synchronized (this) {
			this.currentStyleDeclarationState = null;
			this.computedStyles = null;
			if (deep) {
				nodeList.forEach(node -> {
					if (node instanceof HTMLElementImpl) {
						((HTMLElementImpl) node).forgetStyle(deep);
					}
				});
			}
		}
	}

	/**
	 * <p>getAncestorForJavaClass.</p>
	 *
	 * @param javaClass a {@link java.lang.Class} object.
	 * @return a {@link java.lang.Object} object.
	 */
	protected Object getAncestorForJavaClass(Class<?> javaClass) {
		final Object nodeObj = getParentNode();
		if (nodeObj == null || javaClass.isInstance(nodeObj)) {
			return nodeObj;
		} else if (nodeObj instanceof HTMLElementImpl) {
			return ((HTMLElementImpl) nodeObj).getAncestorForJavaClass(javaClass);
		} else {
			return null;
		}
	}

	/**
	 * <p>getAttributeAsBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean getAttributeAsBoolean(String name) {
		return getAttribute(name) != null;
	}

	/**
	 * <p>getAttributeAsInt.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param defaultValue a int.
	 * @return a int.
	 */
	protected int getAttributeAsInt(String name, int defaultValue) {
		final String value = getAttribute(name);
		HTMLDocumentImpl doc =  (HTMLDocumentImpl)this.document;
		return HtmlValues.getPixelSize(value, null, doc.getDefaultView(), defaultValue);
	}

	/**
	 * <p>getCharset.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCharset() {
		return getAttribute("charset");
	}


	public ComputedCSSStyleDeclaration getComputedStyle() {
		return getComputedStyle(getTagName());
	}

	/**
	 * <p>getComputedStyle.</p>
	 *
	 * @param pseudoElement a {@link java.lang.String} object.
	 * @return a {@link org.loboevolution.html.node.css.ComputedCSSStyleDeclaration} object.
	 */
	public ComputedCSSStyleDeclaration getComputedStyle(String pseudoElement) {

		if (pseudoElement == null) {
			pseudoElement = "";
		}

		final ComputedCSSStyleDeclaration computedStyles = this.computedStyles;
		if (computedStyles != null) {
			return computedStyles;
		}

		final CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) addStyleSheetDeclarations(false, pseudoElement);
		final CSSStyleDeclarationImpl localStyle = (CSSStyleDeclarationImpl) getStyle();
		localStyle.merge(style);
		this.computedStyles = new ComputedCSSStyleDeclarationImpl(this, localStyle);
		return this.computedStyles;
	}

	/**
	 * Gets the style object associated with the element.
	 * It may return null only if the type of element does not handle stylesheets.
	 *
	 * @return a {@link org.loboevolution.html.node.css.CSSStyleDeclaration} object.
	 */
	public CSSStyleDeclaration getCurrentStyle() {
		CSSStyleDeclarationImpl style;
		synchronized (this) {
			style = (CSSStyleDeclarationImpl) this.currentStyleDeclarationState;
			if (style != null) {
				return style;
			}
		}

		style = (CSSStyleDeclarationImpl) addStyleSheetDeclarations(false, getTagName());
		final CSSStyleDeclarationImpl localStyle = (CSSStyleDeclarationImpl) getStyle();
		localStyle.merge(style);
		this.currentStyleDeclarationState = localStyle;
		return this.currentStyleDeclarationState;
	}


	/** {@inheritDoc} */
	@Override
	public String getDocumentBaseURI() {
		final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		if (doc != null) {
			return doc.getBaseURI();
		} else {
			return null;
		}
	}

	/**
	 * Gets form input due to the current element. It should return
	 * null except when the element is a form input element.
	 *
	 * @return an array of {@link org.loboevolution.html.dom.input.FormInput} objects.
	 */
	protected FormInput[] getFormInputs() {
		// Override in input elements
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public double getOffsetHeight() {
		return calculateHeight(true, true);
	}

	/** {@inheritDoc} */
	@Override
	public double getOffsetLeft() {
		return getBoundingClientRect().getX();
	}

	/** {@inheritDoc} */
	@Override
	public int getOffsetTop() {
		return getBoundingClientRect().getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getOffsetWidth() {
		return calculateWidth(true, true);
	}

	/**
	 * <p>getParent.</p>
	 *
	 * @param elementTL a {@link java.lang.String} object.
	 * @return a {@link org.loboevolution.html.dom.domimpl.HTMLElementImpl} object.
	 */
	public HTMLElementImpl getParent(String elementTL) {
		final Object nodeObj = getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			final HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			if ("*".equals(elementTL)) {
				return parentElement;
			}
			final String pelementTL = parentElement.getTagName().toLowerCase();
			if (elementTL.equals(pelementTL)) {
				return parentElement;
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public CSSStyleDeclaration getParentStyle() {
		final Object parent = this.parentNode;
		if (parent instanceof HTMLElementImpl) {
			return ((HTMLElementImpl) parent).getCurrentStyle();
		}
		return null;
	}

	public CSSStyleDeclaration getStyle() {
		CSSStyleDeclarationImpl styleDeclaration = new CSSStyleDeclarationImpl(this);
		if (localStyleDeclarationState == null || localStyleDeclarationState.getLength() == 0) {
			final String style = getAttribute("style");
			if (Strings.isNotBlank(style)) {
				final CSSOMParser parser = new CSSOMParser(new CSS3Parser());
				try {
					styleDeclaration = new CSSStyleDeclarationImpl(this, parser.parseStyleDeclaration(style));
				} catch (final Exception err) {
					final String id = getId();
					final String withId = Strings.isBlank(id) ? "" : " with ID '" + id + "'";
					this.warn("Unable to parse style attribute value for element " + getTagName() + withId + " in " + getDocumentURL() + ".", err);
				}
			}
			this.localStyleDeclarationState = propertyValueProcessed(styleDeclaration);
		}

		return this.localStyleDeclarationState;
	}

	/** {@inheritDoc} */
	@Override
	public void informInvalid() {
		// This is called when an attribute or child changes.
		forgetStyle(false);
		super.informInvalid();
	}

	/**
	 * <p>informInvalidAttibute.</p>
	 *
	 * @param normalName a {@link java.lang.String} object.
	 */
	public void informInvalidAttibute(String normalName) {
		if ("style".equals(normalName)) {
			this.forgetLocalStyle();
		}
		forgetStyle(true);
		informInvalidRecursive();

	}

	private void informInvalidRecursive() {
		super.informInvalid();
		NodeListImpl nodeList = this.getNodeList();
		if (nodeList != null) {
			nodeList.forEach(n -> {
				if (n instanceof HTMLElementImpl) {
					HTMLElementImpl htmlElementImpl = (HTMLElementImpl) n;
					htmlElementImpl.informInvalidRecursive();
				}
			});
		}
	}

	/**
	 * <p>setCharset.</p>
	 *
	 * @param charset a {@link java.lang.String} object.
	 */
	public void setCharset(String charset) {
		setAttribute("charset", charset);
	}
	
	/**
	 * <p>setMouseOver.</p>
	 *
	 * @param mouseOver a boolean.
	 */
	public void setMouseOver(boolean mouseOver) {
		if (hasMouseOver) {
			if (mouseOver) {
				currentStyleDeclarationState = addStyleSheetDeclarations(true, getTagName());
				if (currentStyleDeclarationState != null) {
					informInvalidRecursive();
				}
			} else {
				forgetStyle(true);
				informInvalidRecursive();
			}
		}
	}

	/**
	 * <p>setStyle.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 *
	 */
	public void setStyle(String value) {
		this.setAttribute("style", value);
	}
	
    /** {@inheritDoc} */
    @Override
    public String getContentEditable() {
        String contenteditable = this.getAttribute("contenteditable");
        return Strings.isBlank(contenteditable) ? "true" : contenteditable;
    }

    /** {@inheritDoc} */
    @Override
    public void setContentEditable(String contenteditable) {
        this.setAttribute("contenteditable", contenteditable);
    }
 
	/** {@inheritDoc} */
	@Override
	public void warn(String message) {
		logger.log(Level.WARNING, message);
	}

	/** {@inheritDoc} */
	@Override
	public void warn(String message, Throwable err) {
		logger.log(Level.WARNING, message, err);
	}

	/** {@inheritDoc} */
	@Override
	public String getAccessKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getAccessKeyLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getAutocapitalize() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Element getOffsetParent() {
		Node node = getParentNode();
		if (node instanceof HTMLElement) {
			final HTMLElementImpl element = (HTMLElementImpl) node;
			ComputedCSSStyleDeclaration style = element.getComputedStyle();
			if (!CSSValues.STATIC.isEqual(style.getPosition())) {
				return element;
			}
		}

		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSpellcheck() {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isDraggable() {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isHidden() {
		final String hidden = getAttribute("hidden");
		return hidden != null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTranslate() {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setAccessKey(String accessKey) {
		setAttribute("accessKey", accessKey);
	}

	/** {@inheritDoc} */
	@Override
	public void setAutocapitalize(String autocapitalize) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setDraggable(boolean draggable) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setHidden(boolean hidden) {
		setAttribute("hidden", String.valueOf(hidden));
	}

	/** {@inheritDoc} */
	@Override
	public void setSpellcheck(boolean spellcheck) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setTranslate(boolean translate) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void click() {
		HtmlController.getInstance().onMouseClick(this, null, 0, 0);
	}

	/**
	 * <p>findStyleDeclarations.</p>
	 *
	 * @param elementName a {@link java.lang.String} object.
	 * @param classes an array of {@link java.lang.String} objects.
	 * @param mouseOver a {@link java.lang.Boolean } object.
	 * @return a {@link java.util.List} object.
	 */
	public final List<CSSStyleSheetImpl.SelectorEntry> findStyleDeclarations(String elementName, String[] classes, boolean mouseOver) {
		final HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;

		if (doc == null) {
			return new ArrayList<>();
		}
		final StyleSheetAggregator ssa = doc.getStyleSheetAggregator();
		ssa.setDoc(doc);
		final List<CSSStyleSheetImpl.SelectorEntry> list = ssa.getActiveStyleDeclarations(this, elementName, classes, mouseOver);
		hasMouseOver = ssa.isMouseOver();
		return list;
	}

	/**
	 * Adds style sheet declarations applicable to this element. A properties object
	 * is created if necessary when the one passed is null.
	 * @param mouseOver a {@link java.lang.Boolean } object.
	 * @return a {@link org.loboevolution.html.node.css.CSSStyleDeclaration} object.
	 */
	private CSSStyleDeclaration addStyleSheetDeclarations(boolean mouseOver, String elementName) {

		CSSStyleDeclarationImpl localStyleDeclarationState = new CSSStyleDeclarationImpl(this);
		final String classNames = getClassName();
		final String[] classNameArray = Strings.isNotBlank(classNames) ? Strings.split(classNames) : null;

		final List<CSSStyleSheetImpl.SelectorEntry> matchingRules = findStyleDeclarations(elementName, classNameArray, mouseOver);
		for (CSSStyleSheetImpl.SelectorEntry entry : matchingRules) {
			localStyleDeclarationState.getProperties().addAll(entry.getRule().getStyle().getProperties());
		}
		return propertyValueProcessed(localStyleDeclarationState);
	}

	private CSSStyleDeclarationImpl propertyValueProcessed(final CSSStyleDeclarationImpl localStyleDeclarationState) {
		final List<PropertyCssInfo> properties3 = new ArrayList<>();
		final List<Property> properties = localStyleDeclarationState.getProperties();
		properties.forEach(prop -> {
			CSSValueImpl propertyValue = prop.getValue();
			properties3.add(new PropertyCssInfo(prop.getName(), propertyValue != null ? propertyValue.getCssText() : null, prop.isImportant()));
		});

		properties3.forEach(prop -> localStyleDeclarationState.setPropertyValueProcessed(prop.getName(), prop.getValue(), prop.isImportant()));

		return localStyleDeclarationState;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "[object HTMLElement]";
	}
}
