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

import org.loboevolution.html.dom.svg.SVGDefsElement;
import org.loboevolution.html.dom.svg.SVGRect;

import java.awt.geom.Rectangle2D;

/**
 * <p>SVGDefsElementImpl class.</p>
 */
public class SVGDefsElementImpl extends SVGGraphic implements SVGDefsElement {

	/**
	 * <p>Constructor for SVGDefsElementImpl.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public SVGDefsElementImpl(final String name) {
		super(name);
	}

	@Override
	public SVGRect getBBox() {
		return new SVGRectImpl(new Rectangle2D.Double(0, 0, 0, 0));
	}
}
