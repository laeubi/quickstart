package bugs;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class CopyOfHomePage extends WebPage {
    private static final long       serialVersionUID = 1L;
    private static final AtomicLong instanceCounter  = new AtomicLong();

    public CopyOfHomePage() {
        super();
        add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
        add(new Label("pageId", getPageId()));
        add(new Link("link2") {

            @Override
            public void onClick() {
                System.out.println("onClick Page 2");
                setResponsePage(HomePage.class);
            }

        });
        add(new Label("instance", instanceCounter.getAndIncrement()));
        System.out.println("Create Page 2");
    }
}
