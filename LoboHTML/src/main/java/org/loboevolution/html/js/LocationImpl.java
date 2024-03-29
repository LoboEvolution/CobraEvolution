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

package org.loboevolution.html.js;

import org.loboevolution.gui.HtmlRendererContext;
import org.loboevolution.html.dom.domimpl.HTMLDocumentImpl;
import org.loboevolution.html.node.DOMStringList;
import org.loboevolution.html.node.Document;
import org.loboevolution.html.node.js.Location;
import org.loboevolution.js.AbstractScriptableDelegate;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Location class.
 * </p>
 *
 *
 *
 */
public class LocationImpl extends AbstractScriptableDelegate implements Location {
	private static final Logger logger = Logger.getLogger(LocationImpl.class.getName());
	private String target;

	private final WindowImpl window;

	LocationImpl(final WindowImpl window) {
		this.window = window;
	}

	/**
	 * <p>
	 * getHash.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHash() {
		final URL url = getURL();
		return url == null ? null : url.getRef();
	}

	/**
	 * <p>
	 * getHost.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHost() {
		final URL url = getURL();
		if (url == null) {
			return null;
		}
		return url.getHost() + (url.getPort() == -1 ? "" : ":" + url.getPort());
	}

	/**
	 * <p>
	 * getHostname.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHostname() {
		final URL url = getURL();
		if (url == null) {
			return null;
		}
		return url.getHost();
	}

	/**
	 * <p>
	 * getHref.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHref() {
		final Document document = this.window.getDocumentNode();
		return document == null ? null : document.getDocumentURI();
	}

	/**
	 * <p>
	 * getPathname.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPathname() {
		final URL url = getURL();
		return url == null ? null : url.getPath();
	}

	/**
	 * <p>
	 * getPort.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPort() {
		final URL url = getURL();
		if (url == null) {
			return null;
		}
		final int port = url.getPort();
		return port == -1 ? null : String.valueOf(port);
	}

	/**
	 * <p>
	 * getProtocol.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProtocol() {
		final URL url = getURL();
		if (url == null) {
			return null;
		}
		return url.getProtocol() + ":";
	}

	/**
	 * <p>
	 * getSearch.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSearch() {
		final URL url = getURL();
		final String query = url == null ? null : url.getQuery();
		// Javascript requires "?" in its search string.
		return query == null ? "" : "?" + query;
	}

	/**
	 * <p>
	 * Getter for the field target.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTarget() {
		return this.target;
	}

	private URL getURL() {
		URL url;
		try {
			final Document document = this.window.getDocumentNode();
			url = document == null ? null : new URL(document.getDocumentURI());
		} catch (final java.net.MalformedURLException mfu) {
			url = null;
		}
		return url;
	}

	/**
	 * <p>
	 * reload.
	 * </p>
	 */
	public void reload() {
		// TODO: This is not really reload.
		final Document document = this.window.getDocumentNode();
		if (document instanceof HTMLDocumentImpl) {
			final HTMLDocumentImpl docImpl = (HTMLDocumentImpl) document;
			final HtmlRendererContext rcontext = docImpl.getHtmlRendererContext();
			if (rcontext != null) {
				rcontext.reload();
			} else {
				docImpl.warn("reload(): No renderer context in Location's document.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * replace.
	 * </p>
	 */
	public void replace(String href) {
		setHref(href);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * setHref.
	 * </p>
	 */
	public void setHref(String uri) {
		final HtmlRendererContext rcontext = this.window.getHtmlRendererContext();
		if (rcontext != null) {
			try {
				URL url;
				final Document document = this.window.getDocumentNode();
				if (document instanceof HTMLDocumentImpl) {
					final HTMLDocumentImpl docImpl = (HTMLDocumentImpl) document;
					url = docImpl.getFullURL(uri);
				} else {
					url = new URL(uri);
				}
				rcontext.navigate(url, this.target);
			} catch (final java.net.MalformedURLException mfu) {
				logger.log(Level.WARNING, "setHref(): Malformed location: [" + uri + "].", mfu);
			}
		}
	}

	/**
	 * <p>
	 * Setter for the field target.
	 * </p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public void setTarget(String value) {
		this.target = value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		// This needs to be href. Callers
		// rely on that.
		return getHref();
	}

	/** {@inheritDoc} */
	@Override
	public DOMStringList getAncestorOrigins() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setHash(String hash) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setHost(String host) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setHostname(String hostname) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public String getOrigin() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setPathname(String pathname) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setPort(String port) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setProtocol(String protocol) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void setSearch(String search) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void assign(String url) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void reload(boolean forcedReload) {
		// TODO Auto-generated method stub
		
	}
}
