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

package org.loboevolution.html.dom.svgimpl;

import org.loboevolution.common.Strings;
import org.loboevolution.html.dom.svg.SVGAnimatedTransformList;
import org.loboevolution.html.dom.svg.SVGTransformList;
import org.loboevolution.html.dom.svg.SVGTransformable;
import org.loboevolution.html.node.css.CSSStyleDeclaration;

/**
 * <p>SVGTransformableImpl class.</p>
 */
public abstract class SVGTransformableImpl extends SVGLocatableImpl implements SVGTransformable {

	/**
	 * <p>Constructor for SVGTransformableImpl.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public SVGTransformableImpl(final String name) {
		super(name);
	}

	/** {@inheritDoc} */
	@Override
	public SVGAnimatedTransformList getTransform() {
		CSSStyleDeclaration style = getStyle();
		String transformString = Strings.isNotBlank(style.getTransform()) ? style.getTransform() : this.getAttribute("transform");
		SVGTransformList createTransformList = SVGTransformListImpl.createTransformList(transformString);
		return new SVGAnimatedTransformListImpl((SVGTransformListImpl) createTransformList);
	}
}
