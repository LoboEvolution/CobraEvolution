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
 * <p>ScrollRestoration class.</p>
 *
 *
 *
 */
public enum ScrollRestoration {
	AUTO("auto"),

	MANUAL("manual");

	private final String value;
	private static final Map<String, ScrollRestoration> ENUM_MAP;

	static {
		Map<String, ScrollRestoration> map = new HashMap<>();
		for (ScrollRestoration instance : ScrollRestoration.values()) {
			map.put(instance.getValue(), instance);
		}
		ENUM_MAP = Collections.unmodifiableMap(map);
	}

	ScrollRestoration(String value) {
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
	public static ScrollRestoration get(String actionName) {
		ScrollRestoration value = ENUM_MAP.get(actionName);
		return value == null ? ScrollRestoration.AUTO : value;
	}

}
