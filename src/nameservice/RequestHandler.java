package nameservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class RequestHandler implements Runnable {

    private NameService nameService;
    private Socket clientSocket;


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
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String request = in.readLine();
            String reply = processRequest(request);
            if(NameServiceProtocol.getType(request).equals(NameServiceProtocol.RESOLVE)){
                out.write(reply);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String processRequest(String request){
        switch(NameServiceProtocol.getType(request)){
            case NameServiceProtocol.UNKNOWN: return NameServiceProtocol.createUnknownProtocol();
            case NameServiceProtocol.REBIND: return rebind(request);
            case NameServiceProtocol.RESOLVE: return resolve(request);
        }
        //Should never be reached
        return NameServiceProtocol.createUnknownProtocol();
    }

    private String resolve(String request) {
        String objName = NameServiceProtocol.getObjectName(request);
        String[] itemLocation = this.nameService.registry.get(objName);
        if(itemLocation==null){
            return NameServiceProtocol.resolveFailed();
        }
        String objHost = NameServiceProtocol.extractObjectHost(itemLocation);
        String objPort = NameServiceProtocol.extractObjectPort(itemLocation);
        return NameServiceProtocol.createResolveResponse(objName,objHost,objPort);
    }

    private String rebind(String request) {
        String[] newItem = new String[2];
        newItem[0] = NameServiceProtocol.getHost(request);
        newItem[1] = NameServiceProtocol.getPort(request);
        this.nameService.registry.put(NameServiceProtocol.getObjectName(request),newItem);
        return null;
    }
}
