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

package org.loboevolution.html.node.js.console;

/**
 * Provides access to the browser's debugging console.
 * The specifics of how it works varies from browser to browser, but there is a de facto set of features that are typically provided.
 */
public interface Console {
   
    /**
     * <p>clear.</p>
     */
    void clear();

    /**
     * <p>count.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    void count(String label);

    /**
     * <p>count.</p>
     */
    void count();

    /**
     * <p>debug.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    void debug(Object message);

    /**
     * <p>dir.</p>
     */
    void dir();

    /**
     * <p>log.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    void log(Object message);

    /**
     * <p>error.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    void error(Object message);

    /**
     * <p>group.</p>
     *
     * @param groupTitle a {@link java.lang.String} object.
     */
    void group(String groupTitle);

    /**
     * <p>group.</p>
     */
    void group();

    /**
     * <p>groupCollapsed.</p>
     *
     * @param groupTitle a {@link java.lang.String} object.
     */
    void groupCollapsed(String groupTitle);

    /**
     * <p>groupCollapsed.</p>
     */
    void groupCollapsed();

    /**
     * <p>groupEnd.</p>
     */
    void groupEnd();

    /**
     * <p>info.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    void info(Object message);

    /**
     * <p>markTimeline.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    void markTimeline(String label);

    /**
     * <p>markTimeline.</p>
     */
    void markTimeline();

    /**
     * <p>profile.</p>
     *
     * @param reportName a {@link java.lang.String} object.
     */
    void profile(String reportName);

    /**
     * <p>profile.</p>
     */
    void profile();

    /**
     * <p>profileEnd.</p>
     *
     * @param reportName a {@link java.lang.String} object.
     */
    void profileEnd(String reportName);

    /**
     * <p>profileEnd.</p>
     */
    void profileEnd();

    /**
     * <p>time.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    void time(String label);

    /**
     * <p>time.</p>
     */
    void time();

    /**
     * <p>timeEnd.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    void timeEnd(String label);

    /**
     * <p>timeEnd.</p>
     */
    void timeEnd();

    /**
     * <p>timeStamp.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    void timeStamp(String label);

    /**
     * <p>timeStamp.</p>
     */
    void timeStamp();

    /**
     * <p>timeline.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    void timeline(String label);

    /**
     * <p>timeline.</p>
     */
    void timeline();

    /**
     * <p>timelineEnd.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    void timelineEnd(String label);

    /**
     * <p>timelineEnd.</p>
     */
    void timelineEnd();

    /**
     * <p>trace.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    void trace(Object message);

    /**
     * <p>warn.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    void warn(Object message);

}
