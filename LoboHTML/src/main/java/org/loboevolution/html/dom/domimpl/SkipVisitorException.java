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
 * Created on Dec 3, 2005
 */
package org.loboevolution.html.dom.domimpl;

class SkipVisitorException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for SkipVisitorException.</p>
	 */
	public SkipVisitorException() {
		super();
	}

	/**
	 * <p>Constructor for SkipVisitorException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public SkipVisitorException(String message) {
		super(message);
	}

	/**
	 * <p>Constructor for SkipVisitorException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public SkipVisitorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Constructor for SkipVisitorException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public SkipVisitorException(Throwable cause) {
		super(cause);
	}

}
