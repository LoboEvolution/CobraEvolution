/*
 * MIT License
 *
 * Copyright (c) 2014 - 2023 LoboEvolution
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Contact info: ivan.difrancesco@yahoo.it
 */

package org.loboevolution.html.dom.svgimpl;

import org.loboevolution.common.Strings;
import org.loboevolution.html.dom.svg.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * <p>SVGPathElementImpl class.</p>
 */
public class SVGPathElementImpl extends SVGGraphic implements SVGPathElement {

	private SVGPathSegList pathSegList;

	/**
	 * <p>Constructor for SVGPathElementImpl.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public SVGPathElementImpl(final String name) {
		super(name);
	}

	@Override
	public SVGRect getBBox() {
		Shape shape = createShape(null);
		return new SVGRectImpl(shape.getBounds2D());
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegList getPathSegList() {
		return pathSegList;
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegList getNormalizedPathSegList() {
		return pathSegList;
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegList getAnimatedPathSegList() {
		return pathSegList;
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegList getAnimatedNormalizedPathSegList() {
		return pathSegList;
	}

	/** {@inheritDoc} */
	@Override
	public SVGAnimatedNumber getPathLength() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public float getTotalLength() {
		return pathSegList.getNumberOfItems();
	}

	/** {@inheritDoc} */
	@Override
	public SVGPoint getPointAtLength(float distance) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public int getPathSegAtLength(float distance) {
		// TODO Auto-generated method stub
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegClosePath createSVGPathSegClosePath() {
		return new SVGPathSegClosePathImpl();
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegMovetoAbs createSVGPathSegMovetoAbs(float x, float y) {
		return new SVGPathSegMovetoAbsImpl(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegMovetoRel createSVGPathSegMovetoRel(float x, float y) {
		return new SVGPathSegMovetoRelImpl(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegLinetoAbs createSVGPathSegLinetoAbs(float x, float y) {
		return new SVGPathSegLinetoAbsImpl(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegLinetoRel createSVGPathSegLinetoRel(float x, float y) {
		return new SVGPathSegLinetoRelImpl(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoCubicAbs createSVGPathSegCurvetoCubicAbs(float x, float y, float x1, float y1, float x2, float y2) {
		return new SVGPathSegCurvetoCubicAbsImpl(x, y, x1, y1, x2, y2);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoCubicRel createSVGPathSegCurvetoCubicRel(float x, float y, float x1, float y1, float x2, float y2) {
		return new SVGPathSegCurvetoCubicRelImpl(x, y, x1, y1, x2, y2);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoQuadraticAbs createSVGPathSegCurvetoQuadraticAbs(float x, float y, float x1, float y1) {
		return new SVGPathSegCurvetoQuadraticAbsImpl(x, y, x1, y1);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoQuadraticRel createSVGPathSegCurvetoQuadraticRel(float x, float y, float x1, float y1) {
		return new SVGPathSegCurvetoQuadraticRelImpl(x, y, x1, y1);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegArcAbs createSVGPathSegArcAbs(float x, float y, float r1, float r2, float angle,
												   boolean largeArcFlag, boolean sweepFlag) {
		return new SVGPathSegArcAbsImpl(x, y, r1, r2, angle, largeArcFlag, sweepFlag);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegArcRel createSVGPathSegArcRel(float x, float y, float r1, float r2, float angle,
												   boolean largeArcFlag, boolean sweepFlag) {
		return new SVGPathSegArcRelImpl(x, y, r1, r2, angle, largeArcFlag, sweepFlag);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegLinetoHorizontalAbs createSVGPathSegLinetoHorizontalAbs(float x) {
		return new SVGPathSegLinetoHorizontalAbsImpl(x);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegLinetoHorizontalRel createSVGPathSegLinetoHorizontalRel(float x) {
		return new SVGPathSegLinetoHorizontalRelImpl(x);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegLinetoVerticalAbs createSVGPathSegLinetoVerticalAbs(float y) {
		return new SVGPathSegLinetoVerticalAbsImpl(y);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegLinetoVerticalRel createSVGPathSegLinetoVerticalRel(float y) {
		return new SVGPathSegLinetoVerticalRelImpl(y);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoCubicSmoothAbs createSVGPathSegCurvetoCubicSmoothAbs(float x, float y, float x2, float y2) {
		return new SVGPathSegCurvetoCubicSmoothAbsImpl(x, y, x2, y2);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoCubicSmoothRel createSVGPathSegCurvetoCubicSmoothRel(float x, float y, float x2, float y2) {
		return new SVGPathSegCurvetoCubicSmoothRelImpl(x, y, x2, y2);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoQuadraticSmoothAbs createSVGPathSegCurvetoQuadraticSmoothAbs(float x, float y) {
		return new SVGPathSegCurvetoQuadraticSmoothAbsImpl(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public SVGPathSegCurvetoQuadraticSmoothRel createSVGPathSegCurvetoQuadraticSmoothRel(float x, float y) {
		return new SVGPathSegCurvetoQuadraticSmoothRelImpl(x, y);
	}

	/** {@inheritDoc} */
	@Override
	public void draw(final Graphics2D graphics) {
		String attribute = getAttribute("d");
		if(Strings.isNotBlank(attribute)) {
			constructPathSegList(attribute);
			final Shape shape = createShape(null);
			animate(this);
			drawable(graphics, shape);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Shape createShape(AffineTransform transform) {
		GeneralPath path = new GeneralPath();
		float lastX = 0;
		float lastY = 0;
		Point2D lastControlPoint = null;
		int numPathSegs = pathSegList.getNumberOfItems();
		boolean startOfSubPath = true;
		SVGPoint subPathStartPoint = null;
		SVGPointList points = new SVGPointListImpl();

		for (int i = 0; i < numPathSegs; i++) {
			SVGPathSeg seg = pathSegList.getItem(i);

			if (startOfSubPath) {
				final boolean isMoved  = "M".equalsIgnoreCase(seg.getPathSegTypeAsLetter());
				while (!isMoved && i < numPathSegs) {
					i++;
					seg = pathSegList.getItem(i);
				}

				if (isMoved) {
					if (seg.getPathSegType() == SVGPathSeg.PATHSEG_MOVETO_REL) {
						float x = ((SVGPathSegMovetoRel) seg).getX();
						float y = ((SVGPathSegMovetoRel) seg).getY();
						path.moveTo(x + lastX, y + lastY);
						subPathStartPoint = new SVGPointImpl(x + lastX, y + lastY);
						points.appendItem(subPathStartPoint);
						lastX += x;
						lastY += y;
					} else if (seg.getPathSegType() == SVGPathSeg.PATHSEG_MOVETO_ABS) {
						float x = ((SVGPathSegMovetoAbs) seg).getX();
						float y = ((SVGPathSegMovetoAbs) seg).getY();
						path.moveTo(x, y);
						subPathStartPoint = new SVGPointImpl(x, y);
						points.appendItem(subPathStartPoint);
						lastX = x;
						lastY = y;
					}
					startOfSubPath = false;
				}

			} else {
				switch (seg.getPathSegType()) {
					case SVGPathSeg.PATHSEG_CLOSEPATH: {
						path.closePath();
						lastControlPoint = null;
						startOfSubPath = true;
						if (subPathStartPoint != null) {
							points.appendItem(subPathStartPoint);
							lastX = subPathStartPoint.getX();
							lastY = subPathStartPoint.getY();
							subPathStartPoint = null;
						}
						break;
					}

					case SVGPathSeg.PATHSEG_MOVETO_ABS: {
						float x = ((SVGPathSegMovetoAbs) seg).getX();
						float y = ((SVGPathSegMovetoAbs) seg).getY();
						path.moveTo(x, y);
						lastX = x;
						lastY = y;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_MOVETO_REL: {
						float x = ((SVGPathSegMovetoRel) seg).getX();
						float y = ((SVGPathSegMovetoRel) seg).getY();
						path.moveTo(x + lastX, y + lastY);
						lastX += x;
						lastY += y;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_LINETO_ABS: {
						float x = ((SVGPathSegLinetoAbs) seg).getX();
						float y = ((SVGPathSegLinetoAbs) seg).getY();
						path.lineTo(x, y);
						lastX = x;
						lastY = y;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_LINETO_REL: {
						float x = ((SVGPathSegLinetoRel) seg).getX();
						float y = ((SVGPathSegLinetoRel) seg).getY();
						path.lineTo(x + lastX, y + lastY);
						lastX += x;
						lastY += y;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_ABS: {
						float x = ((SVGPathSegLinetoHorizontalAbs) seg).getX();
						path.lineTo(x, lastY);
						lastX = x;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_REL: {
						float x = ((SVGPathSegLinetoHorizontalRel) seg).getX();
						path.lineTo(x + lastX, lastY);
						lastX += x;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_LINETO_VERTICAL_ABS: {
						float y = ((SVGPathSegLinetoVerticalAbs) seg).getY();
						path.lineTo(lastX, y);
						lastY = y;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_LINETO_VERTICAL_REL: {
						float y = ((SVGPathSegLinetoVerticalRel) seg).getY();
						path.lineTo(lastX, y + lastY);
						lastY += y;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_ABS: {
						float x = ((SVGPathSegCurvetoCubicAbs) seg).getX();
						float y = ((SVGPathSegCurvetoCubicAbs) seg).getY();
						float x1 = ((SVGPathSegCurvetoCubicAbs) seg).getX1();
						float y1 = ((SVGPathSegCurvetoCubicAbs) seg).getY1();
						float x2 = ((SVGPathSegCurvetoCubicAbs) seg).getX2();
						float y2 = ((SVGPathSegCurvetoCubicAbs) seg).getY2();
						path.curveTo(x1, y1, x2, y2, x, y);
						lastControlPoint = new Point2D.Float(x2, y2);
						lastX = x;
						lastY = y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_REL: {
						float x = ((SVGPathSegCurvetoCubicRel) seg).getX();
						float y = ((SVGPathSegCurvetoCubicRel) seg).getY();
						float x1 = ((SVGPathSegCurvetoCubicRel) seg).getX1();
						float y1 = ((SVGPathSegCurvetoCubicRel) seg).getY1();
						float x2 = ((SVGPathSegCurvetoCubicRel) seg).getX2();
						float y2 = ((SVGPathSegCurvetoCubicRel) seg).getY2();
						path.curveTo(lastX + x1, lastY + y1, lastX + x2, lastY + y2, lastX + x, lastY + y);
						lastControlPoint = new Point2D.Float(lastX + x2, lastY + y2);
						lastX += x;
						lastY += y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_ABS: {
						float x = ((SVGPathSegCurvetoCubicSmoothAbs) seg).getX();
						float y = ((SVGPathSegCurvetoCubicSmoothAbs) seg).getY();
						float x2 = ((SVGPathSegCurvetoCubicSmoothAbs) seg).getX2();
						float y2 = ((SVGPathSegCurvetoCubicSmoothAbs) seg).getY2();
						if (lastControlPoint == null) {
							lastControlPoint = new Point2D.Float(lastX, lastY);
						}
						path.curveTo(2 * lastX - (float) lastControlPoint.getX(),
								2 * lastY - (float) lastControlPoint.getY(), x2, y2, x, y);
						lastControlPoint = new Point2D.Float(x2, y2);
						lastX = x;
						lastY = y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_REL: {
						float x = ((SVGPathSegCurvetoCubicSmoothRel) seg).getX();
						float y = ((SVGPathSegCurvetoCubicSmoothRel) seg).getY();
						float x2 = ((SVGPathSegCurvetoCubicSmoothRel) seg).getX2();
						float y2 = ((SVGPathSegCurvetoCubicSmoothRel) seg).getY2();
						if (lastControlPoint == null) {
							lastControlPoint = new Point2D.Float(lastX, lastY);
						}
						path.curveTo(2 * lastX - (float) lastControlPoint.getX(),
								2 * lastY - (float) lastControlPoint.getY(), lastX + x2, lastY + y2, lastX + x, lastY + y);
						lastControlPoint = new Point2D.Float(lastX + x2, lastY + y2);
						lastX += x;
						lastY += y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_ABS: {
						float x = ((SVGPathSegCurvetoQuadraticAbs) seg).getX();
						float y = ((SVGPathSegCurvetoQuadraticAbs) seg).getY();
						float x1 = ((SVGPathSegCurvetoQuadraticAbs) seg).getX1();
						float y1 = ((SVGPathSegCurvetoQuadraticAbs) seg).getY1();
						path.quadTo(x1, y1, x, y);
						lastControlPoint = new Point2D.Float(x1, y1);
						lastX = x;
						lastY = y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_REL: {
						float x = ((SVGPathSegCurvetoQuadraticRel) seg).getX();
						float y = ((SVGPathSegCurvetoQuadraticRel) seg).getY();
						float x1 = ((SVGPathSegCurvetoQuadraticRel) seg).getX1();
						float y1 = ((SVGPathSegCurvetoQuadraticRel) seg).getY1();
						path.quadTo(lastX + x1, lastY + y1, lastX + x, lastY + y);
						lastControlPoint = new Point2D.Float(lastX + x1, lastY + y1);
						lastX += x;
						lastY += y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_ABS: {
						float x = ((SVGPathSegCurvetoQuadraticSmoothAbs) seg).getX();
						float y = ((SVGPathSegCurvetoQuadraticSmoothAbs) seg).getY();
						if (lastControlPoint == null) {
							lastControlPoint = new Point2D.Float(lastX, lastY);
						}
						Point2D nextControlPoint = new Point2D.Float(2 * lastX - (float) lastControlPoint.getX(), 2 * lastY - (float) lastControlPoint.getY());

						path.quadTo((float) nextControlPoint.getX(), (float) nextControlPoint.getY(), x, y);
						lastControlPoint = nextControlPoint;
						lastX = x;
						lastY = y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_REL: {
						float x = ((SVGPathSegCurvetoQuadraticSmoothRel) seg).getX();
						float y = ((SVGPathSegCurvetoQuadraticSmoothRel) seg).getY();
						if (lastControlPoint == null) {
							lastControlPoint = new Point2D.Float(lastX, lastY);
						}
						Point2D nextControlPoint = new Point2D.Float(2 * lastX - (float) lastControlPoint.getX(), 2 * lastY - (float) lastControlPoint.getY());

						path.quadTo((float) nextControlPoint.getX(), (float) nextControlPoint.getY(), lastX + x, lastY + y);
						lastControlPoint = nextControlPoint;
						lastX += x;
						lastY += y;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}

					case SVGPathSeg.PATHSEG_ARC_ABS: {

						float x1 = lastX;
						float y1 = lastY;
						float x2 = ((SVGPathSegArcAbs) seg).getX();
						float y2 = ((SVGPathSegArcAbs) seg).getY();
						float rx = Math.abs(((SVGPathSegArcAbs) seg).getR1());
						float ry = Math.abs(((SVGPathSegArcAbs) seg).getR2());
						float angle = (float) Math.toRadians(((SVGPathSegArcAbs) seg).getAngle());
						boolean fA = ((SVGPathSegArcAbs) seg).getLargeArcFlag();
						boolean fS = ((SVGPathSegArcAbs) seg).getSweepFlag();

						if (rx == 0 || ry == 0) {
							path.lineTo(x2, y2);
						} else {
							Shape arc = createArc(x1, y1, x2, y2, rx, ry, angle, fA, fS);
							path.append(arc, true);
						}
						lastX = x2;
						lastY = y2;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}
					case SVGPathSeg.PATHSEG_ARC_REL: {

						float x1 = lastX;
						float y1 = lastY;
						float x2 = lastX + ((SVGPathSegArcRel) seg).getX();
						float y2 = lastY + ((SVGPathSegArcRel) seg).getY();
						float rx = Math.abs(((SVGPathSegArcRel) seg).getR1());
						float ry = Math.abs(((SVGPathSegArcRel) seg).getR2());
						float angle = (float) Math.toRadians(((SVGPathSegArcRel) seg).getAngle());
						boolean fA = ((SVGPathSegArcRel) seg).getLargeArcFlag();
						boolean fS = ((SVGPathSegArcRel) seg).getSweepFlag();

						if (rx == 0 || ry == 0) {
							path.lineTo(x2, y2);
						} else {
							Shape arc = createArc(x1, y1, x2, y2, rx, ry, angle, fA, fS);
							path.append(arc, true);
						}
						lastX = x2;
						lastY = y2;
						lastControlPoint = null;
						points.appendItem(new SVGPointImpl(lastX, lastY));
						break;
					}
					default:
						break;

				}
			}
		}
		return path;
	}

	private Shape createArc(float x1, float y1, float x2, float y2, float rx, float ry, float angle, boolean fA, boolean fS) {
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		double x1prime = cosAngle * (x1 - x2) / 2 + sinAngle * (y1 - y2) / 2;
		double y1prime = -sinAngle * (x1 - x2) / 2 + cosAngle * (y1 - y2) / 2;
		double rx2 = rx * rx;
		double ry2 = ry * ry;
		double x1prime2 = x1prime * x1prime;
		double y1prime2 = y1prime * y1prime;

		double radiiCheck = x1prime2 / rx2 + y1prime2 / ry2;
		if (radiiCheck > 1) {
			rx = (float) Math.sqrt(radiiCheck) * rx;
			ry = (float) Math.sqrt(radiiCheck) * ry;
			rx2 = rx * rx;
			ry2 = ry * ry;
		}

		double squaredThing = (rx2 * ry2 - rx2 * y1prime2 - ry2 * x1prime2) / (rx2 * y1prime2 + ry2 * x1prime2);
		if (squaredThing < 0) { // this may happen due to lack of precision
			squaredThing = 0;
		}
		squaredThing = Math.sqrt(squaredThing);
		if (fA == fS) {
			squaredThing = -squaredThing;
		}
		double cXprime = squaredThing * rx * y1prime / ry;
		double cYprime = squaredThing * -(ry * x1prime / rx);
		double cx = cosAngle * cXprime - sinAngle * cYprime + (x1 + x2) / 2;
		double cy = sinAngle * cXprime + cosAngle * cYprime + (y1 + y2) / 2;
		double ux = 1;
		double uy = 0;
		double vx = (x1prime - cXprime) / rx;
		double vy = (y1prime - cYprime) / ry;
		double startAngle = Math
				.acos((ux * vx + uy * vy) / (Math.sqrt(ux * ux + uy * uy) * Math.sqrt(vx * vx + vy * vy)));

		if (ux * vy - uy * vx < 0) {
			startAngle = -startAngle;
		}
		ux = (x1prime - cXprime) / rx;
		uy = (y1prime - cYprime) / ry;
		vx = (-x1prime - cXprime) / rx;
		vy = (-y1prime - cYprime) / ry;

		double angleExtent = Math
				.acos((ux * vx + uy * vy) / (Math.sqrt(ux * ux + uy * uy) * Math.sqrt(vx * vx + vy * vy)));

		if (ux * vy - uy * vx < 0) {
			angleExtent = -angleExtent;
		}

		double angleExtentDegrees = Math.toDegrees(angleExtent);
		double numCircles = Math.abs(angleExtentDegrees / 360.0);
		if (numCircles > 1) {
			if (angleExtentDegrees > 0) {
				angleExtentDegrees -= 360 * Math.floor(numCircles);
			} else {
				angleExtentDegrees += 360 * Math.floor(numCircles);
			}
			angleExtent = Math.toRadians(angleExtentDegrees);
		}
		if (fS && angleExtent < 0) {
			angleExtent += Math.toRadians(360.0);
		} else if (!fS && angleExtent > 0) {
			angleExtent -= Math.toRadians(360.0);
		}

		Shape arc = new Arc2D.Double(cx - rx, cy - ry, rx * 2, ry * 2, -Math.toDegrees(startAngle),
				-Math.toDegrees(angleExtent), Arc2D.OPEN);
		arc = AffineTransform.getRotateInstance(angle, cx, cy).createTransformedShape(arc);
		return arc;
	}

	private void constructPathSegList(String d) {
		pathSegList = new SVGPathSegListImpl();
		String commands = "ZzMmLlCcQqAaHhVvSsTt";
		StringTokenizer st = new StringTokenizer(d, commands, true);
		while (st.hasMoreTokens()) {
			String command = st.nextToken();
			while (!commands.contains(command) && st.hasMoreTokens()) {
				command = st.nextToken();
			}
			if (commands.contains(command)) {
				if (command.equals("Z") || command.equals("z")) {
					addCommand(command, null);
				} else {
					if (st.hasMoreTokens()) {
						String parameters = st.nextToken();
						addCommand(command, parameters);
					}
				}
			}
		}
	}

	private void addCommand(String command, String parameters) {
		switch (command) {
			case "Z":
			case "z":
				SVGPathSeg seg = new SVGPathSegClosePathImpl();
				pathSegList.appendItem(seg);
				break;

			case "M":
			case "m":
				addMoveTo(command, parameters);
				break;

			case "L":
			case "l":
				addLineTo(command, parameters);
				break;

			case "C":
			case "c":
				addCurveTo(command, parameters);
				break;

			case "S":
			case "s":
				addSmoothCurveTo(command, parameters);
				break;

			case "H":
			case "h":
				addHorizontalLineTo(command, parameters);
				break;

			case "V":
			case "v":
				addVerticalLineTo(command, parameters);
				break;

			case "Q":
			case "q":
				addQuadraticBezierCurveTo(command, parameters);
				break;

			case "T":
			case "t":
				addTruetypeQuadraticBezierCurveTo(command, parameters);
				break;

			case "A":
			case "a":
				addEllipticArc(command, parameters);
				break;

			default:
				break;
		}
	}

	private void addMoveTo(String command, String parameters) {
		boolean absolute = !command.equals("m");
		boolean firstPoint = true;
		parameters = trimCommas(parameters);
		String delims = " ,-\n\t\r";
		StringTokenizer st = new StringTokenizer(parameters, delims, true);
		while (st.hasMoreTokens()) {
			String token = getNextToken(st, delims);
			float x = Float.parseFloat(token);
			token = getNextToken(st, delims);
			float y = Float.parseFloat(token);
			if (firstPoint) {
				if (absolute) {
					pathSegList.appendItem(new SVGPathSegMovetoAbsImpl(x, y));
				} else {
					pathSegList.appendItem(new SVGPathSegMovetoRelImpl(x, y));
				}
				firstPoint = false;
			} else {
				if (absolute) {
					pathSegList.appendItem(new SVGPathSegLinetoAbsImpl(x, y));
				} else {
					pathSegList.appendItem(new SVGPathSegLinetoRelImpl(x, y));
				}
			}
		}
	}

	private void addLineTo(String command, String parameters) {
		boolean absolute = !command.equals("l");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);
		StringTokenizer st = new StringTokenizer(parameters, delims, true);
		while (st.hasMoreTokens()) {

			// get x coordinate
			String token = getNextToken(st, delims);
			float x = Float.parseFloat(token);

			// get y coordinate
			token = getNextToken(st, delims);
			float y = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegLinetoAbsImpl(x, y));
			} else {
				pathSegList.appendItem(new SVGPathSegLinetoRelImpl(x, y));
			}
		}
	}

	private void addCurveTo(String command, String parameters) {
		boolean absolute = !command.equals("c");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);

		StringTokenizer st = new StringTokenizer(parameters, delims, true);

		while (st.hasMoreTokens()) {

			// get x1 coordinate
			String token = getNextToken(st, delims);
			float x1 = Float.parseFloat(token);

			// get y1 coordinate
			token = getNextToken(st, delims);
			float y1 = Float.parseFloat(token);

			// get x2 coordinate
			token = getNextToken(st, delims);
			float x2 = Float.parseFloat(token);

			// get y2 coordinate
			token = getNextToken(st, delims);
			float y2 = Float.parseFloat(token);

			// get x coordinate
			token = getNextToken(st, delims);
			float x = Float.parseFloat(token);

			// get y coordinate
			token = getNextToken(st, delims);
			float y = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegCurvetoCubicAbsImpl(x, y, x1, y1, x2, y2));
			} else {
				pathSegList.appendItem(new SVGPathSegCurvetoCubicRelImpl(x, y, x1, y1, x2, y2));
			}
		}
	}

	private void addSmoothCurveTo(String command, String parameters) {
		boolean absolute = !command.equals("s");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);
		StringTokenizer st = new StringTokenizer(parameters, delims, true);

		while (st.hasMoreTokens()) {

			// get x2 coordinate
			String token = getNextToken(st, delims);
			float x2 = Float.parseFloat(token);

			// get y2 coordinate
			token = getNextToken(st, delims);
			float y2 = Float.parseFloat(token);

			// get x coordinate
			token = getNextToken(st, delims);
			float x = Float.parseFloat(token);

			// get y coordinate
			token = getNextToken(st, delims);
			float y = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegCurvetoCubicSmoothAbsImpl(x, y, x2, y2));
			} else {
				pathSegList.appendItem(new SVGPathSegCurvetoCubicSmoothRelImpl(x, y, x2, y2));
			}
		}
	}

	private void addHorizontalLineTo(String command, String parameters) {
		boolean absolute = !command.equals("h");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);
		StringTokenizer st = new StringTokenizer(parameters, delims, true);

		while (st.hasMoreTokens()) {

			// get x coordinate
			String token = getNextToken(st, delims);
			float x = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegLinetoHorizontalAbsImpl(x));
			} else {
				pathSegList.appendItem(new SVGPathSegLinetoHorizontalRelImpl(x));
			}
		}
	}

	private void addVerticalLineTo(String command, String parameters) {
		boolean absolute = !command.equals("v");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);
		StringTokenizer st = new StringTokenizer(parameters, delims, true);

		while (st.hasMoreTokens()) {

			// get y coordinate
			String token = getNextToken(st, delims);
			float y = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegLinetoVerticalAbsImpl(y));
			} else {
				pathSegList.appendItem(new SVGPathSegLinetoVerticalRelImpl(y));
			}
		}
	}

	private void addEllipticArc(String command, String parameters) {
		boolean absolute = !command.equals("a");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);
		StringTokenizer st = new StringTokenizer(parameters, delims, true);
		while (st.hasMoreTokens()) {

			// get rx coordinate
			String token = getNextToken(st, delims);
			float r1 = Float.parseFloat(token);

			// get ry coordinate
			token = getNextToken(st, delims);
			float r2 = Float.parseFloat(token);

			// get x-axis-rotation
			token = getNextToken(st, delims);
			float angle = Float.parseFloat(token);

			// get large-arc-flag
			token = getNextToken(st, delims);
			float largeArc = Float.parseFloat(token);

			// get sweep-flag
			token = getNextToken(st, delims);
			float sweepFlag = Float.parseFloat(token);

			// get x coordinate
			token = getNextToken(st, delims);
			float x = Float.parseFloat(token);

			// get y coordinate
			token = getNextToken(st, delims);
			float y = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegArcAbsImpl(x, y, r1, r2, angle, largeArc == 1, sweepFlag == 1));
			} else {
				pathSegList.appendItem(new SVGPathSegArcRelImpl(x, y, r1, r2, angle, largeArc == 1, sweepFlag == 1));
			}
		}
	}

	private void addQuadraticBezierCurveTo(String command, String parameters) {
		boolean absolute = !command.equals("q");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);
		StringTokenizer st = new StringTokenizer(parameters, delims, true);
		while (st.hasMoreTokens()) {

			// get x1 coordinate
			String token = getNextToken(st, delims);
			float x1 = Float.parseFloat(token);

			// get y1 coordinate
			token = getNextToken(st, delims);
			float y1 = Float.parseFloat(token);

			// get x coordinate
			token = getNextToken(st, delims);
			float x = Float.parseFloat(token);

			// get y coordinate
			token = getNextToken(st, delims);
			float y = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegCurvetoQuadraticAbsImpl(x, y, x1, y1));
			} else {
				pathSegList.appendItem(new SVGPathSegCurvetoQuadraticRelImpl(x, y, x1, y1));
			}
		}
	}

	private void addTruetypeQuadraticBezierCurveTo(String command, String parameters) {
		boolean absolute = !command.equals("t");
		String delims = " ,-\n\t\r";
		parameters = trimCommas(parameters);

		StringTokenizer st = new StringTokenizer(parameters, delims, true);

		while (st.hasMoreTokens()) {

			// get x coordinate
			String token = getNextToken(st, delims);
			float x = Float.parseFloat(token);

			// get y coordinate
			token = getNextToken(st, delims);
			float y = Float.parseFloat(token);

			// add new seg to path seg list
			if (absolute) {
				pathSegList.appendItem(new SVGPathSegCurvetoQuadraticSmoothAbsImpl(x, y));
			} else {
				pathSegList.appendItem(new SVGPathSegCurvetoQuadraticSmoothRelImpl(x, y));
			}
		}
	}

	private String getNextToken(StringTokenizer st, String delims) {

		String token;
		boolean neg = false;
		try {
			token = st.nextToken();
			while (st.hasMoreTokens() && delims.contains(token)) {
				neg = token.equals("-");
				token = st.nextToken();
			}
			if (delims.contains(token)) {
				token = "0";
			}
		} catch (NoSuchElementException e) {
			token = "0";
		}

		if (neg) {
			token = "-" + token;
		}

		if (token.endsWith("e") || token.endsWith("E")) {
			// is an exponential number, need to read the exponent too
			try {
				String exponent = st.nextToken(); // get the '-'
				exponent += st.nextToken(); // get the exponent digits
				token += exponent;
			} catch (NoSuchElementException e) {
				token += '0';
			}
		}
		return token;
	}

	private String trimCommas(String params) {
		String result = params;
		while (result.startsWith(",")) {
			result = result.substring(1);
		}
		while (result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}