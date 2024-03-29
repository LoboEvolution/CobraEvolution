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
package org.loboevolution.html.js;

import org.loboevolution.html.dom.nodeimpl.NodeImpl;
import org.loboevolution.html.node.Document;
import org.loboevolution.html.node.events.Event;
import org.loboevolution.http.UserAgentContext;
import org.loboevolution.js.JavaScript;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Executor class.</p>
 *
 *
 *
 */
public class Executor {
	private static final Logger logger = Logger.getLogger(Executor.class.getName());

	/**
	 * A document UserData key used to map Javascript scope in the HTML
	 * document.
	 */
	public static final String SCOPE_KEY = "cobra.js.scope";

	/**
	 * This method should be invoked instead of Context.enter.
	 *
	 * @param codeSource a {@link java.net.URL} object.
	 * @param ucontext a {@link org.loboevolution.http.UserAgentContext} object.
	 * @return a {@link org.mozilla.javascript.Context} object.
	 */
	public static Context createContext(URL codeSource, UserAgentContext ucontext) {
		final Context ctx = Context.enter();
		ctx.setLanguageVersion(Context.VERSION_1_8);
		ctx.setOptimizationLevel(-1);
		return ctx;
	}

	/**
	 * <p>executeFunction.</p>
	 *
	 * @param element a {@link org.loboevolution.html.dom.nodeimpl.NodeImpl} object.
	 * @param f a {@link org.mozilla.javascript.Function} object.
	 * @param event a {@link org.loboevolution.html.node.events.Event} object.
	 * @param obj an array of {@link java.lang.Object} objects.
	 * @return a boolean.
	 */
	public static boolean executeFunction(NodeImpl element, Function f, Event event, Object[] obj) {
		return Executor.executeFunction(element, element, f, event, obj);
	}

	/**
	 * <p>executeFunction.</p>
	 *
	 * @param element a {@link org.loboevolution.html.dom.nodeimpl.NodeImpl} object.
	 * @param thisObject a {@link java.lang.Object} object.
	 * @param f a {@link org.mozilla.javascript.Function} object.
	 * @param event a {@link org.loboevolution.html.node.events.Event} object.
	 * @param obj an array of {@link java.lang.Object} objects.
	 * @return a boolean.
	 */
	public static boolean executeFunction(NodeImpl element, Object thisObject, Function f, Event event, Object[] obj) {
		final Document doc = element.getOwnerDocument();
		if (doc == null) {
			throw new IllegalStateException("Element does not belong to a document.");
		}
		final Context ctx = createContext(element.getDocumentURL(), element.getUserAgentContext());
		try {
			final Scriptable scope = (Scriptable) doc.getUserData(Executor.SCOPE_KEY);
			if (scope == null) {
				throw new IllegalStateException(
						"Scriptable (scope) instance was expected to be keyed as UserData to document using "
								+ Executor.SCOPE_KEY);
			}
			final JavaScript js = JavaScript.getInstance();
			final Scriptable thisScope = (Scriptable) js.getJavascriptObject(thisObject, scope);
			try {
				final Scriptable eventScriptable = (Scriptable) js.getJavascriptObject(event, thisScope);
				ScriptableObject.defineProperty(thisScope, "event", eventScriptable, ScriptableObject.READONLY);
				final Object result = f.call(ctx, thisScope, thisScope, obj);
				if (!(result instanceof Boolean)) {
					return true;
				}
				return (Boolean) result;
			} catch (java.util.MissingResourceException mre) {
				logger.log(Level.WARNING, mre.getMessage());
				return true;
			} catch (final Throwable thrown) {
				logger.log(Level.WARNING, "executeFunction(): There was an error in Javascript code.", thrown);
				return true;
			}
		} finally {
			Context.exit();
		}
	}

	/**
	 * <p>executeFunction.</p>
	 *
	 * @param thisScope a {@link org.mozilla.javascript.Scriptable} object.
	 * @param f a {@link org.mozilla.javascript.Function} object.
	 * @param codeSource a {@link java.net.URL} object.
	 * @param ucontext a {@link org.loboevolution.http.UserAgentContext} object.
	 * @return a boolean.
	 */
	public static boolean executeFunction(Scriptable thisScope, Function f, URL codeSource,
			UserAgentContext ucontext) {
		final Context ctx = createContext(codeSource, ucontext);
		try {
			try {
				final Object result = f.call(ctx, thisScope, thisScope, new Object[0]);
				if (!(result instanceof Boolean)) {
					return true;
				}
				return (Boolean) result;
			} catch (final Throwable err) {
				logger.log(Level.WARNING,
						"executeFunction(): Unable to execute Javascript function " + f.getClassName() + ".", err);
				return true;
			}
		} finally {
			Context.exit();
		}
	}
}
