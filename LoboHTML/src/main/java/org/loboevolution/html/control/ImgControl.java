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
 * Created on Nov 19, 2005
 */
package org.loboevolution.html.control;

import org.loboevolution.common.Strings;
import org.loboevolution.common.WrapperLayout;
import org.loboevolution.gui.HtmlRendererContext;
import org.loboevolution.html.AlignValues;
import org.loboevolution.html.dom.domimpl.HTMLDocumentImpl;
import org.loboevolution.html.dom.domimpl.HTMLElementImpl;
import org.loboevolution.html.dom.domimpl.HTMLImageElementImpl;
import org.loboevolution.gui.HtmlPanel;
import org.loboevolution.html.node.css.CSSStyleDeclaration;
import org.loboevolution.html.renderer.HtmlController;
import org.loboevolution.html.style.HtmlValues;
import org.loboevolution.http.UserAgentContext;
import org.loboevolution.info.TimingInfo;
import org.loboevolution.net.HttpNetwork;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * <p>ImgControl class.</p>
 */
public class ImgControl extends BaseControl {

	/** Constant logger */
	protected static final Logger logger = Logger.getLogger(ImgControl.class.getName());

	private static final long serialVersionUID = 1L;

	private Image image;

	private final String alt;

	private Dimension preferredSize;

	private int valign = AlignValues.BASELINE.getValue();
	
	private boolean mouseBeingPressed;

	/**
	 * <p>Constructor for ImgControl.</p>
	 *
	 * @param modelNode a {@link org.loboevolution.html.dom.domimpl.HTMLImageElementImpl} object.
	 */
	public ImgControl(HTMLImageElementImpl modelNode) {
		super(modelNode);
		setLayout(WrapperLayout.getInstance());
		UserAgentContext bcontext = modelNode.getUserAgentContext();
		alt = modelNode.getAlt() != null ? modelNode.getAlt() : "";
		TimingInfo info = new TimingInfo();
		if (bcontext.isImagesEnabled()) {
			image = HttpNetwork.getImage(modelNode, info, true);
			final HtmlRendererContext htmlRendererContext = modelNode.getHtmlRendererContext();
			final HtmlPanel htmlPanel = htmlRendererContext.getHtmlPanel();
			htmlPanel.getBrowserPanel().getTimingList.add(info);
		}
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				mouseBeingPressed = true;
				repaint();
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				mouseBeingPressed = false;
				repaint();
				HtmlController.getInstance().onPressed(modelNode, e, e.getX(), e.getY());
			}
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					HtmlController.getInstance().onContextMenu(modelNode, e, e.getX(), e.getY());
				}
			}
		});
	}

	/** {@inheritDoc} */
	@Override
	public Dimension getPreferredSize() {
		final Dimension ps = this.preferredSize;
		return ps == null ? new Dimension(0, 0) : ps;
	}

	/** {@inheritDoc} */
	@Override
	public int getVAlign() {
		return this.valign;
	}

	/** {@inheritDoc} */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Dimension size = this.getSize();
		final Insets insets = this.getInsets();
		final Image image = this.image;
		if (image != null) {
			g.drawImage(image, insets.left, insets.top, size.width - insets.left - insets.right,
					size.height - insets.top - insets.bottom, this);
		} else {
			g.drawString(alt, 10, 10);
		}
		
		if (this.mouseBeingPressed) {
			final Color over = new Color(255, 100, 100, 64);
			final Color oldColor = g.getColor();
			try {
				g.setColor(over);
				g.fillRect(0, 0, size.width, size.height);
			} finally {
				g.setColor(oldColor);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void reset(final int availWidth, final int availHeight) {
		super.reset(availWidth, availHeight);
		final HTMLElementImpl element = this.controlElement;
		CSSStyleDeclaration currentStyle = element.getCurrentStyle();
		final int dw = getValueSize(element.getAttribute("width"), currentStyle.getWidth(), availWidth);
		final int dh = getValueSize(element.getAttribute("height"), currentStyle.getHeight(), availHeight);
		this.preferredSize = createPreferredSize(dw, dh);
		this.valign = getValign(element);
	}

	private Dimension createPreferredSize(int dw, int dh) {
		final Image img = this.image;
		if (dw == -1) {
			if (dh != -1) {
				final int iw = img == null ? -1 : img.getWidth(this);
				final int ih = img == null ? -1 : img.getHeight(this);
				if (ih == 0) {
					dw = iw == -1 ? 0 : iw;
				} else if (iw == -1 || ih == -1) {
					dw = 0;
				} else {
					dw = dh * iw / ih;
				}
			} else {
				dw = img == null ? -1 : img.getWidth(this);
				if (dw == -1) {
					dw = 0;
				}
			}
		}
		if (dh == -1) {
			if (dw != -1) {
				final int iw = img == null ? -1 : img.getWidth(this);
				final int ih = img == null ? -1 : img.getHeight(this);
				if (iw == 0) {
					dh = ih == -1 ? 0 : ih;
				} else if (iw == -1 || ih == -1) {
					dh = 0;
				} else {
					dh = dw * ih / iw;
				}
			} else {
				dh = img == null ? -1 : img.getHeight(this);
				if (dh == -1) {
					dh = 0;
				}
			}
		}
		return new Dimension(dw, dh);
	}

	private int getValign(HTMLElementImpl element) {
		String alignText = element.getAttribute("align");

		if (Strings.isNotBlank(alignText)) {
			alignText = alignText.toLowerCase().trim();
		} else{
			CSSStyleDeclaration style = element.getCurrentStyle();
			alignText = Strings.isNotBlank(style.getVerticalAlign()) ? style.getVerticalAlign() : "";
		}

		switch (alignText) {
			case "middle":
				return AlignValues.MIDDLE.getValue();
			case "absmiddle":
				return AlignValues.ABSMIDDLE.getValue();
			case "top":
				return AlignValues.TOP.getValue();
			case "bottom":
				return AlignValues.BOTTOM.getValue();
			case "absbottom":
				return AlignValues.ABSBOTTOM.getValue();
			default:
			case "baseline":
				return AlignValues.BASELINE.getValue();
		}
	}
	
	
	private int getValueSize(String attribute, String styleAttribute, int availSize) {
		String size;
		if (Strings.isNotBlank(attribute)) {
			size = attribute.toLowerCase().trim();
		} else{
			size = Strings.isNotBlank(styleAttribute) ? styleAttribute : "";
		}
		final HTMLElementImpl element = this.controlElement;
		HTMLDocumentImpl doc = (HTMLDocumentImpl)element.getDocumentNode();
		return  HtmlValues.getPixelSize(size, null, doc.getDefaultView(), -1, availSize);
	}
}
