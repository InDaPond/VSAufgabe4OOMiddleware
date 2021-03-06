package mware_lib;

import java.util.logging.Logger;

/**
 * FrontEnd der Middleware public
 */
public class ObjectBroker {

    private String serviceHost;
    private int listenPort;
    private boolean debug;
    private NameServiceProxy nameservice;
    private ApplicationCommunicater applicationCommunicater;
    private static final Logger logger = Logger.getLogger(ObjectBroker.class.getName());
    private static ObjectBroker instance = null;
    private static final int applicationPort = 9999;
    private static final String logFileLocation = "../logs";

    private ObjectBroker(String serviceHost, int listenPort, boolean debug) {
        if (debug) {
            logger.info(String.format("ObjectBroker instantiated with \t servicehost: %s, listenPort: %d, debug: %s",
                    serviceHost, listenPort, debug));
            RemoteDelegator.debug = true;
            ReflectionUtil.debug = true;
        }
        this.serviceHost = serviceHost;
        this.listenPort = listenPort;
        this.debug = debug;
        this.nameservice = NameServiceProxy.getInstance(serviceHost, listenPort, applicationPort, debug);
        this.applicationCommunicater = ApplicationCommunicater.getInstance(applicationPort,this.nameservice,debug);
        new Thread(applicationCommunicater).start();
    }


    /**
     * Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt der Applikation an die Middleware sein
     *
     * @param serviceHost of NameService
     * @param listenPort  of NameService
     * @param debug       switch on(true) or off (false)
     * @return ObjectBroker
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
        this.applicationCommunicater.shutDown();
        ObjectBroker.instance = null;
    }

}
