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
 * Created on Nov 19, 2005
 */
package org.loboevolution.html.dom.domimpl;

import org.loboevolution.common.Urls;
import org.loboevolution.gui.HtmlRendererContext;
import org.loboevolution.html.control.ImgSvgControl;
import org.loboevolution.html.dom.HTMLImageElement;
import org.loboevolution.gui.HtmlPanel;
import org.loboevolution.html.dom.nodeimpl.NodeImpl;
import org.loboevolution.html.renderstate.ImageRenderState;
import org.loboevolution.html.renderstate.RenderState;
import org.loboevolution.net.UserAgent;
import org.loboevolution.type.Decoding;
import org.mozilla.javascript.Function;

import java.awt.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

/**
 * <p>HTMLImageElementImpl class.</p>
 */
public class HTMLImageElementImpl extends HTMLElementImpl implements HTMLImageElement {

	/**
	 * <p>Constructor for HTMLImageElementImpl.</p>
	 */
	public HTMLImageElementImpl() {
		super("IMG");
	}

	/**
	 * <p>Constructor for HTMLImageElementImpl.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public HTMLImageElementImpl(final String name) {
		super(name);
	}

	/** {@inheritDoc} */
	@Override
	public void assignAttributeField(String normalName, String value) {
		if ("onload".equals(normalName)) {
			final Function onload = getEventFunction(null, normalName);
			if (onload != null) {
				setOnload(onload);
			}
		} else {
			super.assignAttributeField(normalName, value);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected RenderState createRenderState(RenderState prevRenderState) {
		return new ImageRenderState(prevRenderState, this);
	}

	/** {@inheritDoc} */
	@Override
	public String getAlign() {
		return getAttribute("align");
	}

	/** {@inheritDoc} */
	@Override
	public String getAlt() {
		return getAttribute("alt");
	}

	/** {@inheritDoc} */
	@Override
	public String getBorder() {
		return getAttribute("border");
	}

	/** {@inheritDoc} */
	@Override
	public double getHeight() {
		return getAttributeAsInt("height", -1);
	}

	/** {@inheritDoc} */
	@Override
	public double getHspace() {
		return getAttributeAsInt("hspace", 0);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isIsMap() {
		return getAttributeAsBoolean("isMap");
	}

	/** {@inheritDoc} */
	@Override
	public String getLongDesc() {
		return getAttribute("longDesc");
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		final String name = getAttribute("name");
		return name == null ? "" : name;
	}

	/**
	 * <p>getOnload.</p>
	 *
	 * @return a {@link org.mozilla.javascript.Function} object.
	 */
	public Function getOnload() {
		final Object document = this.document;
		if (document instanceof HTMLDocumentImpl) {
			return ((HTMLDocumentImpl) document).getOnloadHandler();
		} else {
			return null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getSrc() {
		return getAttribute("src");
	}

	/** {@inheritDoc} */
	@Override
	public String getUseMap() {
		return getAttribute("useMap");
	}

	/** {@inheritDoc} */
	@Override
	public double getVspace() {
		return getAttributeAsInt("vspace", 0);
	}

	/** {@inheritDoc} */
	@Override
	public double getWidth() {
		return getAttributeAsInt("width", -1);
	}

	/** {@inheritDoc} */
	@Override
	public void setAlign(String align) {
		setAttribute("align", align);
	}

	/** {@inheritDoc} */
	@Override
	public void setAlt(String alt) {
		setAttribute("alt", alt);
	}

	/** {@inheritDoc} */
	@Override
	public void setBorder(String border) {
		setAttribute("border", border);
	}

	/** {@inheritDoc} */
	@Override
	public void setHeight(double height) {
		setAttribute("height", String.valueOf(height));
	}

	/** {@inheritDoc} */
	@Override
	public void setHspace(double hspace) {
		setAttribute("hspace", "hspace");
	}

	/** {@inheritDoc} */
	@Override
	public void setIsMap(boolean isMap) {
		setAttribute("isMap", isMap ? "isMap" : null);
	}

	/** {@inheritDoc} */
	@Override
	public void setLongDesc(String longDesc) {
		setAttribute("longDesc", longDesc);
	}

	/** {@inheritDoc} */
	@Override
	public void setName(String name) {
		setAttribute("name", name);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>setOnload.</p>
	 */
	public void setOnload(Function onload) {
		final Object document = this.document;
		if (document instanceof HTMLDocumentImpl) {
			((HTMLDocumentImpl) document).setOnloadHandler(onload);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Sets the image URI and starts to load the image. Note that an
	 * HtmlRendererContext should be available to the HTML document for images to be
	 * loaded.
	 */
	@Override
	public void setSrc(String src) {
		setAttribute("src", src);
	}

	/** {@inheritDoc} */
	@Override
	public void setUseMap(String useMap) {
		setAttribute("useMap", useMap);
	}

	/** {@inheritDoc} */
	@Override
	public void setVspace(double vspace) {
		setAttribute("vspace", String.valueOf(vspace));
	}

	/** {@inheritDoc} */
	@Override
	public void setWidth(double width) {
		setAttribute("width", String.valueOf(width));
	}

	/** {@inheritDoc} */
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String getCrossOrigin() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setCrossOrigin(String crossOrigin) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public String getCurrentSrc() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Decoding getDecoding() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setDecoding(Decoding decoding) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public String getLowsrc() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setLowsrc(String lowsrc) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public double getNaturalHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public double getNaturalWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public String getReferrerPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setReferrerPolicy(String referrerPolicy) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public String getSizes() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setSizes(String sizes) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public String getSrcset() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setSrcset(String srcset) {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * <p>draw.</p>
	 *
	 * @param imgSvgControl a {@link org.loboevolution.html.control.ImgSvgControl} object.
	 */
	public void draw(ImgSvgControl imgSvgControl) {
		try {
			final Object document = this.document;
			String uri = null;
			if (document instanceof HTMLDocumentImpl) {
				try {
					HTMLDocumentImpl doc = (HTMLDocumentImpl) document;
					URL baseURL = new URL(doc.getBaseURI());
					String src = getSrc();
					URL scriptURL = Urls.createURL(baseURL, src);
					uri = scriptURL == null ? src : scriptURL.toExternalForm();

				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			} else {
				uri = getSrc();
			}

			final URL url = new URL(uri);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", UserAgent.getUserAgent());
			connection.getHeaderField("Set-Cookie");
			connection.connect();

			NodeImpl mode = (NodeImpl) document;
			HtmlPanel panel = new HtmlPanel();
			panel.setBrowserPanel(null);
			HtmlRendererContext htmlRendererContext = mode.getHtmlRendererContext();
			panel = HtmlPanel.createlocalPanel(connection, panel, mode.getHtmlRendererContext(), mode.getHtmlRendererConfig(), uri);
			final double height = getHeight() == -1 ? htmlRendererContext.getInnerWidth() : getHeight();
			final double width = getWidth() == -1 ? htmlRendererContext.getInnerWidth() : getWidth();
			panel.setPreferredSize(new Dimension((int) width, (int) height));
			imgSvgControl.add(panel);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "[object HTMLImageElement]";
	}
}
