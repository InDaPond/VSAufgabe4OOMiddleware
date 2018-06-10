package mware_lib;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class NameServiceProxy extends NameService {

    private String serviceHost;
    private int listenPort;
    private ConcurrentHashMap<String, Object> registry;
    private int applicationPort;
    private boolean debug;
    private static final Logger logger = Logger.getLogger(NameServiceProxy.class.getName());

    private static NameServiceProxy instance = null;

    private NameServiceProxy(String serviceHost, int listenPort, int applicationPort, boolean debug) {
        this.serviceHost = serviceHost;
        this.listenPort = listenPort;
        this.registry = new ConcurrentHashMap<>();
        this.applicationPort = applicationPort;
        this.debug = debug;
    }

    public static NameServiceProxy getInstance(String serviceHost, int listenPort, int applicationPort, boolean debug) {
        if (debug) {
            logger.info("getInstance called");
        }
        if (instance == null) {
            logger.info("new Instance created");
            instance = new NameServiceProxy(serviceHost, listenPort, applicationPort, debug);
        }
        return instance;
    }

    @Override
    public void rebind(Object servant, String name) {
        if (debug)
            logger.info(String.format("rebind called\nObject %s put into local registry with the name %s", servant
                    .getClass().getName(), name));
        this.registry.put(name, servant);
        // establish connection
        if (debug) logger.info(String.format("Writing to %s:%s", serviceHost, listenPort));
        try (Socket mySock = new Socket(serviceHost, listenPort);
             PrintWriter out = new PrintWriter(mySock.getOutputStream(), true)
        ) {
            // communication
            String request = (NameServiceProtocol.createRequest(NameServiceProtocol.REBIND, name, InetAddress
                    .getLocalHost
                    ().getHostAddress(), applicationPort));
            out.println(request);
            if (debug) logger.info("Request sent: " + request);
            if (debug) logger.info("Message sent. Closing Socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object resolve(String name) {
        if (debug) logger.info("resolve called for " + name);
        // establish connection
        if (debug) logger.info(String.format("Writing to %s:%s", serviceHost, listenPort));
        try (Socket mySock = new Socket(serviceHost, listenPort);
             // I/O-Kanäle der Socket
             BufferedReader in = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
             PrintWriter out = new PrintWriter(mySock.getOutputStream(), true)
        ) {
            if (debug) logger.info("Connection established");
            // communcation
            String request = (NameServiceProtocol.createRequest(NameServiceProtocol.RESOLVE, name, InetAddress
                    .getLocalHost
                    ().getHostAddress(), applicationPort));
            out.println(request);
            if (debug) logger.info("Request sent: " + request);
            String reply = in.readLine();
            if (debug) logger.info("Received reply: " + reply);
            if (NameServiceProtocol.getType(reply).equals(NameServiceProtocol.SUCCESS)) {
                return String.format("%s,%s,%s", NameServiceProtocol.getObjectName(reply), NameServiceProtocol
                        .getHost(reply), NameServiceProtocol.getPort(reply));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object resolveLocally(String name) {
        if (debug) logger.info("resolve Locally: " + name);
        return registry.get(name);
    }
}


