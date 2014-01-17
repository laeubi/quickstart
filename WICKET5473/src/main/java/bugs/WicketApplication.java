package bugs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.IProvider;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see bugs.Start#main(String[])
 */
public class WicketApplication extends WebApplication {

    private static boolean isPersitent = true;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        System.out.println("init isPersitent=" + isPersitent);
        if (isPersitent) {
            setSessionStoreProvider(new IProvider<ISessionStore>() {

                @Override
                public ISessionStore get() {
                    return new PersistentSessionStore();
                }
            });
        }
    }

    /**
     * This simulates a persitent session that stores entries as seriallized
     * objects somewhere an later restores them (maybe on restart or a regualr
     * basis)
     * 
     * @author Christoph Läubrich
     */
    private final class PersistentSessionStore extends HttpSessionStoreModified {

        @Override
        protected Serializable getValueInternal(String attributeName, HttpSession httpSession) {
            Serializable val = super.getValueInternal(attributeName, httpSession);
            if (val instanceof byte[]) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream((byte[]) val));
                    Object storedVal = ois.readObject();
                    ois.close();
                    System.out.println("getValue(" + attributeName + ") := " + storedVal);
                    return (Serializable) storedVal;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (val == null) {
                    return null;
                }
                throw new IllegalStateException();
            }
        }

        @Override
        protected void setValueInternal(HttpSession httpSession, Serializable value, String attributeName) {
            try {
                System.out.println("setValue(" + attributeName + ") := " + value);
                if (value != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(value);
                    oos.flush();
                    oos.close();
                    super.setValueInternal(httpSession, bos.toByteArray(), attributeName);
                } else {
                    super.setValueInternal(httpSession, null, attributeName);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
