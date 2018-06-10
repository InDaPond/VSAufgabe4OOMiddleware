package mware_lib;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class NameServiceProxy extends NameService {

    private String serviceHost;
    private int listenPort;
    private ConcurrentHashMap<String, Object> registry;

    private static NameServiceProxy instance = null;

    private NameServiceProxy(String serviceHost, int listenPort) {
        this.serviceHost = serviceHost;
        this.listenPort = listenPort;
        this.registry = new ConcurrentHashMap<>();
    }

    public static NameServiceProxy getInstance(String serviceHost, int listenPort) {
        if (instance == null) {
            instance = new NameServiceProxy(serviceHost, listenPort);
        }
        return instance;
    }

    @Override
    public void rebind(Object servant, String name) {
        registry.put(name, servant);
        String request = NameServiceProtocol.createRequest(NameServiceProtocol.REBIND, serviceHost, listenPort);
    }

    @Override
    public Object resolve(String name) {
        // Verbindung aufbauen
        try (Socket mySock = new Socket(serviceHost, listenPort);
             // I/O-Kanäle der Socket
             BufferedReader in = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
             PrintWriter out = new PrintWriter(mySock.getOutputStream())
        ) {
            // Kommunikation
            out.write((NameServiceProtocol.createRequest(NameServiceProtocol.RESOLVE, serviceHost, listenPort)));

            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object resolveLocally(String name) {
        return registry.get(name);
    }
}


