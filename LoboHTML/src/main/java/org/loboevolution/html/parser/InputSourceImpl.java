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
 * Created on Oct 22, 2005
 */
package org.loboevolution.html.parser;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * The InputSourceImpl class implements the
 * InputSource interface.
 *
 * Author J. H. S.
 *
 */
public class InputSourceImpl extends InputSource {

	/**
	 * Constructs an InputSourceImpl.
	 *
	 * @param byteStream The input stream where content can be read.
	 * @param uri        The URI that identifies the content.
	 * @param charset    The character set of the input stream.
	 */
	public InputSourceImpl(InputStream byteStream, String uri, Charset charset) {
		super(byteStream);
		setEncoding(charset.displayName());
		setSystemId(uri);
		setPublicId(uri);
	}
	/**
	 * Constructs an InputSourceImpl.
	 *
	 * @param characterStream The Reader where characters can be read.
	 * @param uri             The URI of the document.
	 */
	public InputSourceImpl(Reader characterStream, String uri) {
		super(characterStream);
		setSystemId(uri);
	}
}
