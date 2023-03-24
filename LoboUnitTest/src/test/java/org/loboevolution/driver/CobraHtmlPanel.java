package
        org.loboevolution.driver;

import org.loboevolution.gui.HtmlPanel;
import org.loboevolution.net.UserAgent;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class CobraHtmlPanel {

    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame();
        window.setSize(600, 400);
        window.setVisible(true);

        URL createURL = new URL(Objects.
                requireNonNull(CobraHtmlPanel.class.getResource("/org/lobo/html/htmlsample.html")).toString());
        URLConnection connection = createURL.openConnection();
        connection.setRequestProperty("User-Agent", UserAgent.getUserAgent());
        connection.getHeaderField("Set-Cookie");
        connection.connect();
        final HtmlPanel hpanel = HtmlPanel.createlocalPanel(connection, createURL.toString());
        hpanel.setPreferredSize(new Dimension(500, 800));
        window.getContentPane().add(hpanel);

    }
}

//https://codeberg.org/miurahr/LoboComponent