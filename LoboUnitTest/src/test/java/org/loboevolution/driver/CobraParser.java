package org.loboevolution.driver;

import org.loboevolution.config.HtmlRendererConfig;
import org.loboevolution.gui.HtmlPanel;
import org.loboevolution.gui.HtmlRendererContext;
import org.loboevolution.gui.LocalHtmlRendererConfig;
import org.loboevolution.gui.LocalHtmlRendererContext;
import org.loboevolution.html.dom.domimpl.HTMLDocumentImpl;
import org.loboevolution.html.io.WritableLineReader;
import org.loboevolution.html.node.Document;
import org.loboevolution.html.node.Element;
import org.loboevolution.html.node.Node;
import org.loboevolution.html.node.NodeList;
import org.loboevolution.http.UserAgentContext;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * CobraParser - This object is a wrapper for the Cobra Toolkit, which is part
 * of the Lobo Project (<a href="https://github.com/LoboEvolution/CobraEvolution">...</a>).
 * CobraEvolution is a pure Java HTML renderer and DOM parser"
 * <p>
 * CobraParser opens a URL, uses Cobra to render that HTML and apply JavaScript.
 * It then does a simple tree traversal of the DOM to print beginning and
 * end tag names.
 * <p>
 * Subclass this class and override the
 * <i>doElement(org.loboevolution.html.node.Element element)</i> and
 * <i>doTagEnd(org.loboevolution.html.node.Element element)</i> methods to do some real
 * work.  In the base class, doElement() prints the tag name and
 * doTagEnd() prints a closing version of the tag.
 */

public class CobraParser {
    String url;

    /**
     * Create a CobraParser object with a target URL.
     */

    public CobraParser(String url) {
        this.url = url;
    }

    /**
     * Load the given URL using Cobra.  When the page is loaded,
     * recurse on the DOM and call doElement()/doTagEnd() for
     * each Element node.  Return false on error.
     */

    private void parsePage() {
        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            InputStream in = connection.getInputStream();
            Document document = loadHtml(in, url);
            Element ex = document.getDocumentElement();
            doTree(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * <p>loadHtml.</p>
     *
     * @param in a {@link java.io.InputStream} object.
     * @param url a {@link java.lang.String} object.
     * @return a {@link org.loboevolution.html.dom.domimpl.HTMLDocumentImpl} object.
     */
    private HTMLDocumentImpl loadHtml(InputStream in, String url) {
        HTMLDocumentImpl doc = null;
        try {
            WritableLineReader wis = new WritableLineReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            final HtmlRendererConfig config = new LocalHtmlRendererConfig();
            final UserAgentContext ucontext = new UserAgentContext(config, true);
            HtmlPanel panel = new HtmlPanel();
            panel.setPreferredSize(new Dimension(800, 400));
            final HtmlRendererContext rendererContext = new LocalHtmlRendererContext(panel, ucontext);
            ucontext.setUserAgentEnabled(true);
            doc = new HTMLDocumentImpl(ucontext, rendererContext, config, wis, url);
            doc.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Recurse the DOM starting with Node.  For each Node of
     * type Element, call doElement() with it and recurse over its
     * children.  The Elements refer to the HTML tags, and the children
     * are tags contained inside the parent tag.
     */

    private void doTree(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            doElement(element);
            NodeList nl = element.getChildNodes();
            if (nl == null) return;
            int num = nl.getLength();
            for (int i = 0; i < num; i++) {
                doTree(nl.item(i));
            }
            doTagEnd(element);
        }
    }

    /**
     * Simple doElement to print the tag name of the Element.  Override
     * to do something real.
     */

    private void doElement(Element element) {
        System.out.println("<" + element.getTagName() + ">");
    }

    /**
     * Simple doTagEnd() to print the closing tag of the Element.
     * Override to do something real.
     */

    private void doTagEnd(Element element) {
        System.out.println("</" + element.getTagName() + ">");
    }

    /**
     * Open CobraParser on www.cnn.com by default.
     * Parse the page
     * and print the beginning and end tags.
     */

    public static void main(String[] args) {
        String url = Objects.requireNonNull(CobraParser.class.getResource("/org/lobo/html/htmlsample.html")).toString();
        if (args.length == 1) url = args[0];
        CobraParser p = new CobraParser(url);
        p.parsePage();
        System.exit(0);
    }
}
