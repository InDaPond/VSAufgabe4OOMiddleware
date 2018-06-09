package mware_lib;

import java.util.logging.Logger;

/**
 * FrontEnd der Middleware public
 */
public class ObjectBroker {

    private String serviceHost;
    private int listenPort;
    private boolean debug;
    private NameService nameservice;
    private static final Logger logger = Logger.getLogger(ObjectBroker.class.getName());
    private static ObjectBroker instance = null;

    private ObjectBroker(String serviceHost, int listenPort, boolean debug) {
        if (debug) {
            logger.info(String.format("ObjectBroker instantiated with \n servicehost: %s, listenPort: %d, debug: %s",
                    serviceHost, listenPort, debug));
        }
        this.serviceHost = serviceHost;
        this.listenPort = listenPort;
        this.debug = debug;
        this.nameservice = NameServiceProxy.getInstance(serviceHost, listenPort);
    }


    /**
     * Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt der Applikation an die Middleware sein
     *
     * @param serviceHost of NameService
     * @param listenPort  of NameService
     * @param debug       switch on(true) or off (false)
     * @return
     */
    public static ObjectBroker init(String serviceHost,
                                    int listenPort, boolean debug) {
        if (instance == null) {
            instance = new ObjectBroker(serviceHost, listenPort, debug);
        }
        return instance;

    }

    /**
     * Liefert den Namensdienst (Stellvetreterobjekt)
     *
     * @return NameService
     */
    public NameService getNameService() {
        return nameservice;
    }


    /**
     * Beendet die Benutzung der Middleware in dieser Anwendung
     */
    public void shutDown() {
        //ToDO
    }

}
