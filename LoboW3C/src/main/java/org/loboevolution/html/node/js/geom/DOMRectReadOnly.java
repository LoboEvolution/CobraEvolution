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

package org.loboevolution.html.node.js.geom;

/**
 * <p>DOMRectReadOnly interface.</p>
 */
public interface DOMRectReadOnly {

    /**
     * <p>getBottom.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getBottom();

    /**
     * <p>getHeight.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getHeight();

    /**
     * <p>getLeft.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getLeft();

    /**
     * <p>getRight.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getRight();

    /**
     * <p>getTop.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getTop();

    /**
     * <p>getWidth.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getWidth();

    /**
     * <p>getX.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getX();

    /**
     * <p>getY.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    int getY();


    /**
     * <p>fromRect.</p>
     *
     * @return a DOMRectReadOnly.
     */
    DOMRectReadOnly fromRect();

    /**
     * <p>fromRect.</p>
     *
     * @return a DOMRectReadOnly.
     */
    DOMRectReadOnly fromRect(DOMRectInit other);
}