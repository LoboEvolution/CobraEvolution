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

package org.loboevolution.common;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.NoSuchElementException;


/**
 * <p>ArrayUtilities class.</p>
 */
public class ArrayUtilities {

	/**
	 * <p>isBlank.</p>
	 *
	 * @param collection a {@link java.util.Collection} object.
	 * @return a boolean.
	 */
	public static boolean isBlank(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * <p>isNotBlank.</p>
	 *
	 * @param collection a {@link java.util.Collection} object.
	 * @return a boolean.
	 */
	public static boolean isNotBlank(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * <p>iterator.</p>
	 *
	 * @param array an array of {@link java.lang.Object} objects.
	 * @param offset a int.
	 * @param length a int.
	 * @return a {@link java.util.Iterator} object.
	 */
	public static Iterator iterator(Object[] array, int offset, int length) {
		return new ArrayIterator(array, offset, length);
	}

	/**
	 * <p>moveItem.</p>
	 *
	 * @param sourceIndex a int.
	 * @param targetIndex a int.
	 * @param list a {@link java.util.List} object.
	 * @param <T> a T object.
	 */
	public static <T> void moveItem(int sourceIndex, int targetIndex, List<T> list) {
	    if (sourceIndex <= targetIndex) {
	        Collections.rotate(list.subList(sourceIndex, targetIndex + 1), -1);
	    } else {
	        Collections.rotate(list.subList(targetIndex, sourceIndex + 1), 1);
	    }
	}
	
	/**
	 * <p>singletonIterator.</p>
	 *
	 * @param item a {@link java.lang.Object} object.
	 * @return a {@link java.util.Iterator} object.
	 */
	public static Iterator singletonIterator(final Object item) {
		return new Iterator() {
			private boolean gotItem = false;

			@Override
			public boolean hasNext() {
				return !this.gotItem;
			}

			@Override
			public Object next() {
				if (this.gotItem) {
					throw new NoSuchElementException();
				}
				this.gotItem = true;
				return item;
			}

			@Override
			public void remove() {
				if (!this.gotItem) {
					this.gotItem = true;
				} else {
					throw new NoSuchElementException();
				}
			}
		};
	}
	

	/**
	 * <p>removeColor.</p>
	 *
	 * @param arr an array of {@link java.awt.Color} objects.
	 * @param index a {@link java.lang.Integer} object
	 * @return an array of {@link java.awt.Color} objects.
	 */
	public static Color[] removeColor(Color[] arr, int index) {
		if (arr == null || index < 0 || index >= arr.length) {
			return arr;
		}
		Color[] anotherArray = new Color[arr.length - 1];
		System.arraycopy(arr, 0, anotherArray, 0, index);
		System.arraycopy(arr, index + 1, anotherArray, index, arr.length - index - 1);
		return anotherArray;
	}

	/**
	 * <p>removeFloat.</p>
	 *
	 * @param arr an array of {@link java.lang.Float} objects.
	 * @param index a {@link java.lang.Integer} object
	 * @return an array of {@link java.lang.Float} objects.
	 */
	public static float[] removeFloat(float[] arr, int index) {
		if (arr == null || index < 0 || index >= arr.length) {
			return arr;
		}
		float[] anotherArray = new float[arr.length - 1];
		System.arraycopy(arr, 0, anotherArray, 0, index);
		System.arraycopy(arr, index + 1, anotherArray, index, arr.length - index - 1);
		return anotherArray;
	}
	
	/**
	 * <p>contains.</p>
	 *
	 * @param ts an array of T[] objects.
	 * @param t a T object.
	 * @param <T> a T object.
	 * @return a boolean.
	 */
	public static <T> boolean contains(final T[] ts, final T t) {
		for (final T e : ts) {
			if (Objects.equals(e, t)) {
				return true;
			}
		}
		return false;
	}
	
	private static class ArrayIterator<T> implements Iterator<T> {
		private final T[] array;
		private final int top;
		private int offset;

		public ArrayIterator(final T[] array, final int offset, final int length) {
			this.array = array;
			this.offset = offset;
			this.top = offset + length;
		}

		public boolean hasNext() {
			return this.offset < this.top;
		}

		public T next() {
			return this.array[this.offset++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
