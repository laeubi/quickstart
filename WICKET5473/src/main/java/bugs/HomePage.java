package bugs;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class HomePage extends WebPage {
    private static final long       serialVersionUID = 1L;

    private static final AtomicLong instanceCounter  = new AtomicLong();

    public HomePage() {
        add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
        add(new Label("pageId", getPageId()));
        add(new Label("instance", instanceCounter.getAndIncrement()));
        add(new Link("link1") {

            @Override
            public void onClick() {
                System.out.println("onClick Page 1");
                setResponsePage(CopyOfHomePage.class);
            }

        });
        System.out.println("Create Page 1");
    }
}
