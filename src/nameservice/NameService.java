package nameservice;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class NameService implements Runnable {

    protected ConcurrentHashMap<String, String[]> registry;
    private static NameService instance = null;
    private static boolean running = true;
    private static int port;
    private static final Logger logger = Logger.getLogger(NameService.class.getName());
    private static final boolean debug = true;

    private NameService() {
        this.registry = new ConcurrentHashMap<>();
    }

    public static NameService getInstance() {
        if (instance == null) {
            instance = new NameService();
        }
        return instance;
    }

    public static void shutdown() {
        running = false;
    }

    /**
     * @param args port where the Nameservice should listen for incoming requests
     */
    public static void main(String[] args) {
        port = Integer.parseInt(args[0]);
        NameService nameService = NameService.getInstance();
        new Thread(nameService).start();

    }

    @Override
    public void run() {
        if(debug) {
            try {
                logger.info(String.format("Nameservice up and running at %s:%s",InetAddress.getLocalHost()
                                .getHostAddress(),port));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        try (
                ServerSocket serverSocket = new ServerSocket(port)
        ) {
            while (running) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Received new connection, delegating...");
                //delegate to a Thread so the NameService can keep listening to client requests
                new Thread(new RequestHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
