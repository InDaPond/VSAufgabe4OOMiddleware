package mware_lib;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ApplicationCommunicater implements Runnable {

    private int applicationPort;
    private boolean debug;
    private static final Logger logger = Logger.getLogger(ApplicationCommunicater.class.getName());
    private static ApplicationCommunicater instance = null;
    private NameServiceProxy nameService;
    public static boolean running = true;

    private ApplicationCommunicater(int applicationPort, NameServiceProxy nameService, boolean debug) {
        this.applicationPort = applicationPort;
        this.nameService = nameService;
        this.debug = debug;
    }

    public static ApplicationCommunicater getInstance(int applicationPort, NameServiceProxy nameService, boolean debug) {
        if (instance == null) {
            instance = new ApplicationCommunicater(applicationPort, nameService, debug);
        }
        return instance;
    }

    public void shutDown(){
        this.debug = false;
    }

    @Override
    public void run() {
        try (ServerSocket socket = new ServerSocket(applicationPort)
        ) {
            while (running) {
                Socket clientSocket = socket.accept();
                logger.info("Received new connection, delegating...");
                //delegate to a Thread so the ApplicationCommunicator can keep listening to client requests
                new Thread(new RequestHandler(clientSocket,this.nameService)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
