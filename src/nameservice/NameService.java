package nameservice;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class NameService {

    protected ConcurrentHashMap<String, String[]> registry;
    private static NameService instance = null;
    private static boolean running = true;

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
        int portNumber = Integer.parseInt(args[0]);
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber)
        ) {
            while (running) {
                Socket clientSocket = serverSocket.accept();
                //delegate to a Thread so the NameService can keep listening to client requests
                new Thread(new RequestHandler(clientSocket)).start();




            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
