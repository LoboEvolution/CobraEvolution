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
 * Created on Oct 23, 2005
 */
package org.loboevolution.html.parser;

import org.loboevolution.html.node.Element;

class StopException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Element element;

	/**
	 * <p>Constructor for StopException.</p>
	 *
	 * @param element a {@link org.loboevolution.html.node.Element} object.
	 */
	public StopException(Element element) {
		super();
		this.element = element;
	}

	/**
	 * <p>Getter for the field element.</p>
	 *
	 * @return a {@link org.loboevolution.html.node.Element} object.
	 */
	public Element getElement() {
		return this.element;
	}
}
