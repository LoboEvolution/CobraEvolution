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

package org.loboevolution.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>DocumentReadyState class.</p>
 *
 *
 *
 */
public enum DocumentReadyState {

	LOADING("loading"),

	INTERACTIVE("interactive"),

	COMPLETE("complete");

	private final String value;
	private static final Map<String, DocumentReadyState> ENUM_MAP;

	static {
		Map<String, DocumentReadyState> map = new HashMap<>();
		for (DocumentReadyState instance : DocumentReadyState.values()) {
			map.put(instance.getValue(), instance);
		}
		ENUM_MAP = Collections.unmodifiableMap(map);
	}

	DocumentReadyState(String value) {
		this.value = value;
	}

	/**
	 * <p>
	 * Getter for the field value.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <p>
	 * isEqual.
	 * </p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isEqual(String value) {
		return this.value.equals(value);
	}

	/**
	 * <p>
	 * get.
	 * </p>
	 *
	 * @param actionName a {@link java.lang.String} object.
	 * @return a {@link org.loboevolution.type} object.
	 */
	public static DocumentReadyState get(String actionName) {
		DocumentReadyState value = ENUM_MAP.get(actionName);
		return value == null ? DocumentReadyState.COMPLETE : value;
	}
}
