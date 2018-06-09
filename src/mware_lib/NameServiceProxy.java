package mware_lib;

import nameservice.NameService;

import java.util.concurrent.ConcurrentHashMap;

public class NameServiceProxy extends NameService {

    private String serviceHost;
    private int listenPort;
    private ConcurrentHashMap<String,Object> registry;

    private static NameServiceProxy instance = null;

    private NameServiceProxy(String serviceHost, int listenPort){
        this.serviceHost = serviceHost;
        this.listenPort = listenPort;
        this.registry = new ConcurrentHashMap<>();
    }

    public static NameServiceProxy getInstance(String serviceHost, int listenPort) {
        if(instance == null) {
            instance = new NameServiceProxy(serviceHost, listenPort);
        }
        return instance;
    }

    @Override
    public void rebind(Object servant, String name) {
        registry.put(name,servant);
//        NameServiceCommunication
    }

    @Override
    public Object resolve(String name) {
        return registry.get(name);
    }
}


