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
package org.loboevolution.html.renderstate;

/**
 * Additional state that may be set during rendering to override state
 * determined from elements.
 */
public final class RenderThreadState {
	private static final ThreadLocal<RenderThreadState> stateTL = new ThreadLocal<>();

	/**
	 * <p>getState.</p>
	 *
	 * @return a {@link org.loboevolution.html.renderstate.RenderThreadState} object.
	 */
	public static RenderThreadState getState() {
		RenderThreadState ts = stateTL.get();
		if (ts == null) {
			ts = new RenderThreadState();
			stateTL.set(ts);
		}
		return ts;
	}

	public boolean overrideNoWrap;

	private RenderThreadState() {
	}
}
