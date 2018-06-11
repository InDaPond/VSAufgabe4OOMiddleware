package mware_lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (debug) logger.info("Received reply: " + reply);
            Object[] response = ReflectionUtil.getParameterValues(ApplicationProtocol.getParams(reply));

//            return response[0];
            if(response[1]==null) {
                return response[0];
            }else {
                return ReflectionUtil.getException(reply);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
