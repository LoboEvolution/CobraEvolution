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

package org.loboevolution.html.dom.svgimpl;

import org.loboevolution.html.dom.nodeimpl.NodeListImpl;
import org.loboevolution.html.dom.svg.*;
import org.loboevolution.html.node.Node;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>SVGGElementImpl class.</p>
 */
public class SVGGElementImpl extends SVGGraphic implements SVGGElement {

	/**
	 * <p>Constructor for SVGGElementImpl.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public SVGGElementImpl(final String name) {
		super(name);
	}

	/** {@inheritDoc} */
	@Override
	public void draw(final Graphics2D graphics) {
		boolean display = getDisplay();
		float opacity = getOpacity();

        SVGClipPathElementImpl clipPath = getClippingPath();
        Shape clipShape = null;
        if (clipPath != null) {
            clipShape = clipPath.getClippingShape(this);
        }

		if (display && opacity > 0) {
			AffineTransform oldGraphicsTransform = graphics.getTransform();
			Shape oldClip = graphics.getClip();
            final SVGTransformList transform = getTransform().getAnimVal();

            if (transform != null) {
                graphics.transform(((SVGTransformListImpl) transform).getAffineTransform());
            }

            if (clipShape != null) {
                graphics.clip(clipShape);
            }

			if (opacity < 1) {
				SVGSVGElement root = this.getOwnerSVGElement();
				float currentScale = root.getCurrentScale();
				Shape shape = createShape(null);
				AffineTransform screenCTM = getScreenCTM().getAffineTransform();
				Shape transformedShape = screenCTM.createTransformedShape(shape);
				Rectangle2D bounds = transformedShape.getBounds2D();
				double xInc = bounds.getWidth() / 5;
				double yInc = bounds.getHeight() / 5;
				bounds.setRect(bounds.getX() - xInc, bounds.getY() - yInc, bounds.getWidth() + 2 * xInc, bounds.getHeight() + 2 * yInc);
				int imageWidth = (int) (bounds.getWidth() * currentScale);
				int imageHeight = (int) (bounds.getHeight() * currentScale);
				if (imageWidth > 0 && imageHeight > 0) {
					BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
					Graphics2D offGraphics = (Graphics2D) image.getGraphics();
					RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					offGraphics.setRenderingHints(hints);
					if (currentScale != 1) {
						offGraphics.scale(currentScale, currentScale);
					}

					offGraphics.translate(-bounds.getX(), -bounds.getY());
					offGraphics.transform(screenCTM);
					drawChildren(offGraphics);

					Composite oldComposite = graphics.getComposite();
					AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
					graphics.setComposite(ac);
					AffineTransform imageTransform = AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY());
					imageTransform.scale(1 / currentScale, 1 / currentScale);
					try {
						imageTransform.preConcatenate(screenCTM.createInverse());
					} catch (NoninvertibleTransformException e) {
					}
					graphics.drawImage(image, imageTransform, null);
					graphics.setComposite(oldComposite);
					image.flush();
				}
			} else {
				drawChildren(graphics);
			}

			graphics.setTransform(oldGraphicsTransform);
			graphics.setClip(oldClip);
		}
	}

	@Override
	public SVGRect getBBox() {
		Shape shape = createShape(null);
		return new SVGRectImpl(shape.getBounds2D());
	}

	/** {@inheritDoc} */
	@Override
	public Shape createShape(AffineTransform transform) {
		GeneralPath path = new GeneralPath();
		if (hasChildNodes()) {
			NodeListImpl nodeList = (NodeListImpl)getChildNodes();
			nodeList.forEach(child -> {
				Shape childShape = null;
				if (child instanceof SVGGElementImpl) {
					childShape = ((SVGGElementImpl) child).createShape(transform);

				} else if (child instanceof SVGAElementImpl) {
					childShape = ((SVGAElementImpl) child).createShape(transform);

				} else if (child instanceof SVGImageElementImpl) {
					SVGRect bbox = ((SVGImageElement) child).getBBox();
					childShape = new Rectangle2D.Float(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());

				} else if (child instanceof SVGUseElementImpl) {
					SVGRect bbox = ((SVGUseElement) child).getBBox();
					childShape = new Rectangle2D.Float(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());

				} else if (child instanceof SVGSVGElementImpl) {
					SVGSVGElement svg = (SVGSVGElement) child;
					AffineTransform ctm = getCTM().getAffineTransform();
					AffineTransform inverseTransform;
					try {
						inverseTransform = ctm.createInverse();
					} catch (NoninvertibleTransformException e) {
						inverseTransform = null;
					}
					float x = ((SVGLengthImpl) svg.getX()).getTransformedLength(inverseTransform);
					float y = ((SVGLengthImpl) svg.getY()).getTransformedLength(inverseTransform);
					float width = ((SVGLengthImpl) svg.getWidth()).getTransformedLength(inverseTransform);
					float height = ((SVGLengthImpl) svg.getHeight()).getTransformedLength(inverseTransform);

					childShape = new Rectangle2D.Float(x, y, width, height);
				}
				if (child instanceof SVGTransformable) {
					SVGAnimatedTransformList childTransformList = ((SVGTransformable) child).getTransform();
					SVGTransformList list = childTransformList.getAnimVal();
					if (list != null) {
						AffineTransform childTransform = ((SVGTransformListImpl)list).getAffineTransform();
						childShape = childTransform.createTransformedShape(childShape);
					}
				}
				if (childShape != null) {
					path.append(childShape, false);
				}
			});
		}
		return path;
	}
	
	private void drawChildren(Graphics2D graphics) {
		List<Node> drawableChildren = new ArrayList<>();
		if (hasChildNodes()) {
			NodeListImpl childNodes = (NodeListImpl) getChildNodes();
			childNodes.forEach(child -> {
				if (child instanceof Drawable) {
					drawableChildren.add(child);
				}
			});
		}

		for (Node node : drawableChildren) {
			SVGElement selem = (SVGElement)node;
			selem.setOwnerSVGElement(getOwnerSVGElement());
			drawStyle(node);
			Drawable child = (Drawable) node;
			child.draw(graphics);
		}
	}
}
