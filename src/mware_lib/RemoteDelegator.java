package mware_lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class RemoteDelegator {

    public static boolean debug;
    private int port;
    private NameServiceProxy nameService;
    private static final Logger logger = Logger.getLogger(RemoteDelegator.class.getName());


    public static Object invokeMethod(String objectName, String locationHost, int locationPort, String className,
                                      String methodName, Object... params) {
        if (debug)
            logger.info(String.format("called with: %s,%s,%d,%s,%s,%s", objectName, locationHost, locationPort,
                    className, methodName, Arrays.toString(params)));
//        String methodRequest = ApplicationProtocol.requestMethodExecution(objectName, className, methodName,
//                params);
//        if (debug)
//            logger.info(String.format("Sending methodRequest: %s to %s:%d", methodRequest, locationHost,
//                    locationPort));
        try (Socket mySock = new Socket(locationHost, locationPort);
             // I/O-Kanäle der Socket
             BufferedReader in = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
             PrintWriter out = new PrintWriter(mySock.getOutputStream(), true)
        ) {
            // Kommunikation
            String methodRequest = ApplicationProtocol.requestMethodExecution(objectName, className, methodName,
                    params);
            if (debug)
                logger.info(String.format("Sending methodRequest: %s to %s:%d", methodRequest, locationHost,
                        locationPort));
            out.println(methodRequest);
            String reply = in.readLine();
            if (NameServiceProtocol.getType(reply).equals(NameServiceProtocol.SUCCESS)) {
                return String.format("%s,%s,%s", NameServiceProtocol.getObjectName(reply), NameServiceProtocol
                        .getHost(reply), NameServiceProtocol.getPort(reply));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 4.0;
    }


}
