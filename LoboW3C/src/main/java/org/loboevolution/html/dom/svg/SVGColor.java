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
package org.loboevolution.html.dom.svg;

/**
 * <p>SVGColor interface.</p>
 *
 *
 *
 */
public interface SVGColor {
	// Color Types
	/** Constant SVG_COLORTYPE_UNKNOWN=0 */
    short SVG_COLORTYPE_UNKNOWN = 0;
	/** Constant SVG_COLORTYPE_RGBCOLOR=1 */
    short SVG_COLORTYPE_RGBCOLOR = 1;
	/** Constant SVG_COLORTYPE_RGBCOLOR_ICCCOLOR=2 */
    short SVG_COLORTYPE_RGBCOLOR_ICCCOLOR = 2;
	/** Constant SVG_COLORTYPE_CURRENTCOLOR=3 */
    short SVG_COLORTYPE_CURRENTCOLOR = 3;

	/**
	 * <p>getColorType.</p>
	 *
	 * @return a short.
	 */
	short getColorType();

	/**
	 * <p>getICCColor.</p>
	 *
	 * @return a {@link org.loboevolution.html.dom.svg.SVGICCColor} object.
	 */
	SVGICCColor getICCColor();

	/**
	 * <p>setRGBColor.</p>
	 *
	 * @param rgbColor a {@link java.lang.String} object.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 */
	void setRGBColor(String rgbColor) throws SVGException;

	/**
	 * <p>setRGBColorICCColor.</p>
	 *
	 * @param rgbColor a {@link java.lang.String} object.
	 * @param iccColor a {@link java.lang.String} object.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 */
	void setRGBColorICCColor(String rgbColor, String iccColor) throws SVGException;

	/**
	 * <p>setColor.</p>
	 *
	 * @param colorType a short.
	 * @param rgbColor a {@link java.lang.String} object.
	 * @param iccColor a {@link java.lang.String} object.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 * @throws org.loboevolution.html.dom.svg.SVGException if any.
	 */
	void setColor(short colorType, String rgbColor, String iccColor) throws SVGException;
}
