package nameservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Handles exactly one incoming requests and then shuts down.
 */
public class RequestHandler implements Runnable {

    private NameService nameService;
    private Socket clientSocket;
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());
    private static final boolean debug = true;


    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.nameService = NameService.getInstance();
    }

    @Override
    public void run() {
        try (
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))
        ) {
            if (debug) logger.info("Waiting for incoming requests!");
            String request = in.readLine();
            if (debug) logger.info("Request received: " + request);
            String reply = processRequest(request);
            if (debug && reply != null) logger.info("Reply: " + reply);
            if (NameServiceProtocol.getType(request).equals(NameServiceProtocol.RESOLVE)) {
                if(debug) logger.info("Sending reply: "+reply);
                out.println(reply);
            }

            clientSocket.close();
            if (debug) logger.info("Job done!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String processRequest(String request) {
        switch (NameServiceProtocol.getType(request)) {
            case NameServiceProtocol.UNKNOWN:
                return NameServiceProtocol.createUnknownProtocol();
            case NameServiceProtocol.REBIND:
                return rebind(request);
            case NameServiceProtocol.RESOLVE:
                return resolve(request);
        }
        //Should never be reached
        return NameServiceProtocol.createUnknownProtocol();
    }

    private String resolve(String request) {

        String objName = NameServiceProtocol.getObjectName(request);
        if (debug) logger.info("Retrieving Object...");
        String[] itemLocation = this.nameService.registry.get(objName);
        if (itemLocation == null) {
            if (debug) logger.info("Failed to retrieve item :(");
            return NameServiceProtocol.resolveFailed();
        }
        String objHost = NameServiceProtocol.extractObjectHost(itemLocation);
        String objPort = NameServiceProtocol.extractObjectPort(itemLocation);
        if (debug) logger.info("Item retrieved, Generating response!");
        return NameServiceProtocol.createResolveResponse(objName, objHost, objPort);
    }

    private String rebind(String request) {
        String[] newItem = new String[2];
        newItem[0] = NameServiceProtocol.getHost(request);
        newItem[1] = NameServiceProtocol.getPort(request);
        this.nameService.registry.put(NameServiceProtocol.getObjectName(request), newItem);
        if (debug) logger.info("rebind done!");
        return null;
    }
}
