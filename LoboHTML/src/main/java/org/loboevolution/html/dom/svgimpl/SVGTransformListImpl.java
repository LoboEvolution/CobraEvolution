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

import org.loboevolution.html.dom.svg.SVGException;
import org.loboevolution.html.dom.svg.SVGMatrix;
import org.loboevolution.html.dom.svg.SVGTransform;
import org.loboevolution.html.dom.svg.SVGTransformList;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <p>SVGTransformListImpl class.</p>
 *
 *
 *
 */
public class SVGTransformListImpl implements SVGTransformList {
	
	private List<SVGTransform> transList;

	/**
	 * <p>Constructor for SVGTransformListImpl.</p>
	 */
	public SVGTransformListImpl() {
		transList = new ArrayList<>();
	}

	/** {@inheritDoc} */
	@Override
	public int getNumberOfItems() {
		return transList.size();
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		transList.clear();
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform initialize(SVGTransform newItem) throws SVGException {
		transList = new ArrayList<>();
		transList.add(newItem);
		return newItem;
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform getItem(int index) {
		return transList.get(index);
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform insertItemBefore(SVGTransform newItem, int index) throws SVGException {

        transList.remove(newItem);

		if (index < 0) {
			transList.add(0, newItem);
		} else if (index > getNumberOfItems() - 1) { // insert at end
			transList.add(newItem);
		} else {
			transList.add(index, newItem);
		}
		return newItem;
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform replaceItem(SVGTransform newItem, int index) throws SVGException {

        transList.remove(newItem);

		if (index < 0 || index > getNumberOfItems() - 1) {
			return null;
		}

		transList.remove(index);
		transList.add(index, newItem);
		return newItem;
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform removeItem(int index) {
		return transList.remove(index);
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform appendItem(SVGTransform newItem) throws SVGException {
		transList.add(newItem);
		return newItem;
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform createSVGTransformFromMatrix(SVGMatrix matrix) {
		SVGTransform transform = new SVGTransformImpl();
		transform.setMatrix(matrix);
		return transform;
	}

	/** {@inheritDoc} */
	@Override
	public SVGTransform consolidate() {
		int numTransforms = getNumberOfItems();
		if (numTransforms == 0) {
			return null;
		}
		SVGTransform transform = getItem(0);
		SVGMatrix result = transform.getMatrix();
		for (SVGTransform svgTransform : transList) {
			result = result.multiply(svgTransform.getMatrix());
		}
		SVGTransform newTransform = new SVGTransformImpl();
		newTransform.setMatrix(result);
		initialize(newTransform);
		return newTransform;
	}

	/**
	 * <p>getAffineTransform.</p>
	 *
	 * @return a {@link java.awt.geom.AffineTransform} object.
	 */
	protected AffineTransform getAffineTransform() {
		int numTransforms = getNumberOfItems();
		if (numTransforms == 0) {
			return new AffineTransform();
		}

		SVGTransform transform = getItem(0);
		SVGMatrix result = transform.getMatrix();
		for (int i = 1; i < numTransforms; i++) {
			transform = getItem(i);
			result = result.multiply(transform.getMatrix());
		}
		return new AffineTransform(result.getA(), result.getB(), result.getC(), result.getD(), result.getE(), result.getF());
	}

	/**
	 * <p>createTransformList.</p>
	 *
	 * @param transformString a {@link java.lang.String} object.
	 * @return a {@link org.loboevolution.html.dom.svg.SVGTransformList} object.
	 */
	public static SVGTransformList createTransformList(String transformString) {
		String SCALE = "scale";
		String TRANSLATE = "translate";
		String MATRIX = "matrix";
		String ROTATE = "rotate";
		String SKEW_X = "skewX";
		String SKEW_Y = "skewY";

		if (transformString == null) {
			return null;
		}

		transformString = transformString.trim();
		SVGTransformListImpl transformList = new SVGTransformListImpl();
		StringTokenizer st = new StringTokenizer(transformString, "()", false);
		while (st.hasMoreTokens()) {
			String transformType = st.nextToken().trim();
			if (!st.hasMoreTokens()) {
				break;
			}
			String transformArgs = st.nextToken().trim();
			if (transformType.contains(MATRIX)) {
				StringTokenizer st1 = new StringTokenizer(transformArgs, ", ", false);
				int numArgs = st1.countTokens();
				if (numArgs == 6) {
					float a = Float.parseFloat(st1.nextToken());
					float b = Float.parseFloat(st1.nextToken());
					float c = Float.parseFloat(st1.nextToken());
					float d = Float.parseFloat(st1.nextToken());
					float e = Float.parseFloat(st1.nextToken());
					float f = Float.parseFloat(st1.nextToken());
					SVGTransformImpl transform = new SVGTransformImpl(SVGTransform.SVG_TRANSFORM_MATRIX);
					SVGMatrixImpl matrix = new SVGMatrixImpl(a, b, c, d, e, f);
					transform.setMatrix(matrix);
					transformList.appendItem(transform);
				}
			} else if (transformType.contains(TRANSLATE)) {
				StringTokenizer st1 = new StringTokenizer(transformArgs, ", ", false);
				int numArgs = st1.countTokens();
				float tx = 0;
				float ty = 0;
				if (numArgs == 1) {
					tx = Float.parseFloat(st1.nextToken());
				} else if (numArgs == 2) {
					tx = Float.parseFloat(st1.nextToken());
					ty = Float.parseFloat(st1.nextToken());
				} else {
					if (numArgs > 2) {
						tx = Float.parseFloat(st1.nextToken());
						ty = Float.parseFloat(st1.nextToken());
					}
				}
				SVGTransformImpl transform = new SVGTransformImpl(SVGTransform.SVG_TRANSFORM_TRANSLATE);
				transform.setTranslate(tx, ty);
				transformList.appendItem(transform);
			} else if (transformType.contains(SCALE)) {
				StringTokenizer st1 = new StringTokenizer(transformArgs, ", ", false);
				int numArgs = st1.countTokens();
				float sx = 0;
				float sy = 0;
				if (numArgs == 1) {
					sx = Float.parseFloat(st1.nextToken());
					sy = sx;
				} else if (numArgs == 2) {
					sx = Float.parseFloat(st1.nextToken());
					sy = Float.parseFloat(st1.nextToken());
				} else {
					if (numArgs > 2) {
						sx = Float.parseFloat(st1.nextToken());
						sy = Float.parseFloat(st1.nextToken());
					}
				}
				SVGTransformImpl transform = new SVGTransformImpl(SVGTransform.SVG_TRANSFORM_SCALE);
				transform.setScale(sx, sy);
				transformList.appendItem(transform);
			} else if (transformType.contains(ROTATE)) {
				StringTokenizer st1 = new StringTokenizer(transformArgs, ", ", false);
				int numArgs = st1.countTokens();
				float angle = 0;
				float cx = 0;
				float cy = 0;
				if (numArgs == 1) {
					angle = Float.parseFloat(st1.nextToken());
				} else if (numArgs == 3) {
					angle = Float.parseFloat(st1.nextToken());
					cx = Float.parseFloat(st1.nextToken());
					cy = Float.parseFloat(st1.nextToken());
				} else {
					if (numArgs == 2) {
						angle = Float.parseFloat(st1.nextToken());
					} else if (numArgs > 3) {
						angle = Float.parseFloat(st1.nextToken());
						cx = Float.parseFloat(st1.nextToken());
						cy = Float.parseFloat(st1.nextToken());
					}
				}
				SVGTransformImpl transform = new SVGTransformImpl(SVGTransform.SVG_TRANSFORM_ROTATE);
				transform.setRotate(angle, cx, cy);
				transformList.appendItem(transform);
			} else if (transformType.contains(SKEW_X)) {
				float skewAngle = Float.parseFloat(transformArgs);
				SVGTransformImpl transform = new SVGTransformImpl(SVGTransform.SVG_TRANSFORM_SKEWX);
				transform.setSkewX(skewAngle);
				transformList.appendItem(transform);
			} else if (transformType.contains(SKEW_Y)) {
				float skewAngle = Float.parseFloat(transformArgs);
				SVGTransformImpl transform = new SVGTransformImpl(SVGTransform.SVG_TRANSFORM_SKEWY);
				transform.setSkewY(skewAngle);
				transformList.appendItem(transform);
			}
		}
		return transformList;
	}
}
