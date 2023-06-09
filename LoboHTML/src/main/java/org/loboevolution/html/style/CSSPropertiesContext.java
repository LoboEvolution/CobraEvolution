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
package org.loboevolution.html.style;

import org.loboevolution.html.node.css.CSSStyleDeclaration;

/**
 * <p>CSSPropertiesContext interface.</p>
 */
public interface CSSPropertiesContext {
	/**
	 * <p>getDocumentBaseURI.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getDocumentBaseURI();

	/**
	 * <p>getParentStyle.</p>
	 *
	 * @return a {@link org.loboevolution.html.node.css.CSSStyleDeclaration} object.
	 */
	CSSStyleDeclaration getParentStyle();

	/**
	 * <p>informInvalid.</p>
	 */
	void informInvalid();

	/**
	 * <p>informLayoutInvalid.</p>
	 */
	void informLayoutInvalid();

	/**
	 * <p>informLookInvalid.</p>
	 */
	void informLookInvalid();

	/**
	 * <p>informPositionInvalid.</p>
	 */
	void informPositionInvalid();

	/**
	 * <p>informSizeInvalid.</p>
	 */
	void informSizeInvalid();
}
