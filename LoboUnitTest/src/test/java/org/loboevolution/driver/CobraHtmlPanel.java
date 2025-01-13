package
        org.loboevolution.driver;

import lombok.extern.slf4j.Slf4j;
import org.loboevolution.config.HtmlRendererConfig;
import org.loboevolution.gui.HtmlPanel;
import org.loboevolution.gui.HtmlRendererContext;
import org.loboevolution.gui.LocalHtmlRendererConfig;
import org.loboevolution.gui.LocalHtmlRendererContext;
import org.loboevolution.http.UserAgentContext;
import org.loboevolution.net.UserAgent;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

@Slf4j
public class CobraHtmlPanel {

    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame();
        window.setSize(800, 400);
        window.setVisible(true);

        URL createURL = new URI(Objects.
                requireNonNull(CobraHtmlPanel.class.getResource("/org/lobo/html/htmlsample.html")).toString()).toURL();
        URLConnection connection = createURL.openConnection();
        connection.setRequestProperty("User-Agent", UserAgent.getUserAgent());
        connection.getHeaderField("Set-Cookie");
        connection.connect();
        HtmlPanel panel = new HtmlPanel();
        panel.setBrowserPanel(null);
        panel.setPreferredSize(new Dimension(800, 400));
        final HtmlRendererConfig config = new LocalHtmlRendererConfig();
        final UserAgentContext ucontext = new UserAgentContext(config);
        final HtmlRendererContext rendererContext = new LocalHtmlRendererContext(panel, ucontext);
        panel = HtmlPanel.createlocalPanel(connection, panel, rendererContext, config, createURL.toString());
        window.getContentPane().add(panel);

    }
}