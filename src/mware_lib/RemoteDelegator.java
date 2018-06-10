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

    private int port;
    private NameService nameService;
    private static final Logger logger = Logger.getLogger(RemoteDelegator.class.getName());


    public static Object invokeMethod(String objectName, String locationHost, int locationPort, String className, String methodName, Object... params) {
        System.out.println(Arrays.toString(params));
//        try (Socket mySock = new Socket(locationHost, locationPort);
//             // I/O-Kanäle der Socket
//             BufferedReader in = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
//             PrintWriter out = new PrintWriter(mySock.getOutputStream(), true)
//        ) {
//            // Kommunikation
//            out.write((NameServiceProtocol.createRequest(NameServiceProtocol.RESOLVE, name, InetAddress.getLocalHost
//                    ().getHostAddress(), applicationPort)));
//
//            String reply = in.readLine();
//            if (NameServiceProtocol.getType(reply).equals(NameServiceProtocol.SUCCESS)) {
//                return String.format("%s,%s,%s",NameServiceProtocol.getObjectName(reply),NameServiceProtocol.getHost(reply),NameServiceProtocol.getPort(reply));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
        return null;
    }
}
