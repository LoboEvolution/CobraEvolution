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

package org.loboevolution.html.renderer.table;

import org.loboevolution.html.renderer.BoundableRenderable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

class TableMatrixEvents {
	
	private BoundableRenderable armedRenderable;
	
	private List<RTableCell> allCells;
	
	/**
	 * <p>Constructor for TableMatrixEvents.</p>
	 *
	 * @param allCells a {@link java.util.List} object.
	 */
	public TableMatrixEvents(List<RTableCell> allCells) {
		this.allCells = allCells;
	}
	
	/**
	 * <p>onDoubleClick.</p>
	 *
	 * @param event a {@link java.awt.event.MouseEvent} object.
	 * @param x a int.
	 * @param y a int.
	 * @return a boolean.
	 */
	public boolean onDoubleClick(final MouseEvent event, int x, int y) {
		for (RTableCell cell : allCells) {
			final Rectangle bounds = cell.getVisualBounds();
			if (bounds.contains(x, y)) {
				if (!cell.onDoubleClick(event, x - bounds.x, y - bounds.y)) {
					return false;
				}
				break;
			}
		}
		return true;
	}

	/**
	 * <p>onMouseClick.</p>
	 *
	 * @param event a {@link java.awt.event.MouseEvent} object.
	 * @param x a int.
	 * @param y a int.
	 * @return a boolean.
	 */
	public boolean onMouseClick(final MouseEvent event, int x, int y) {
		for (RTableCell cell : allCells) {
			final Rectangle bounds = cell.getVisualBounds();
			if (bounds.contains(x, y)) {
				if (!cell.onMouseClick(event, x - bounds.x, y - bounds.y)) {
					return false;
				}
				break;
			}
		}
		return true;
	}

	/**
	 * <p>onMouseDisarmed.</p>
	 *
	 * @param event a {@link java.awt.event.MouseEvent} object.
	 * @return a boolean.
	 */
	public boolean onMouseDisarmed(final MouseEvent event) {
		final BoundableRenderable ar = this.armedRenderable;
		if (ar != null) {
			this.armedRenderable = null;
			return ar.onMouseDisarmed(event);
		} else {
			return true;
		}
	}

	/**
	 * <p>onMousePressed.</p>
	 *
	 * @param event a {@link java.awt.event.MouseEvent} object.
	 * @param x a int.
	 * @param y a int.
	 * @return a boolean.
	 */
	public boolean onMousePressed(final MouseEvent event, int x, int y) {
		for (RTableCell cell : allCells) {
			final Rectangle bounds = cell.getVisualBounds();
			if (bounds.contains(x, y)) {
				if (!cell.onMousePressed(event, x - bounds.x, y - bounds.y)) {
					this.armedRenderable = cell;
					return false;
				}
				break;
			}
		}
		return true;
	}

	/**
	 * <p>onMouseReleased.</p>
	 *
	 * @param event a {@link java.awt.event.MouseEvent} object.
	 * @param x a int.
	 * @param y a int.
	 * @return a boolean.
	 */
	public boolean onMouseReleased(final MouseEvent event, int x, int y) {
		boolean found = false;
		for (RTableCell cell : allCells) {
			final Rectangle bounds = cell.getVisualBounds();
			if (bounds.contains(x, y)) {
				found = true;
				final BoundableRenderable oldArmedRenderable = this.armedRenderable;
				if (oldArmedRenderable != null && cell != oldArmedRenderable) {
					oldArmedRenderable.onMouseDisarmed(event);
					this.armedRenderable = null;
				}
				if (!cell.onMouseReleased(event, x - bounds.x, y - bounds.y)) {
					return false;
				}
				break;
			}
		}
		if (!found) {
			final BoundableRenderable oldArmedRenderable = this.armedRenderable;
			if (oldArmedRenderable != null) {
				oldArmedRenderable.onMouseDisarmed(event);
				this.armedRenderable = null;
			}
		}
		return true;
	}

}
