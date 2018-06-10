package mware_lib;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Locale;
import java.util.logging.Logger;

public class RequestHandler implements Runnable {

    private NameServiceProxy nameService;
    private Socket clientSocket;
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());
    private static final boolean debug = true;


    public RequestHandler(Socket clientSocket, NameServiceProxy nameService) {
        this.clientSocket = clientSocket;
        this.nameService = nameService;
    }

    @Override
    public void run() {
//        try (
//                PrintWriter out =
//                        new PrintWriter(clientSocket.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(clientSocket.getInputStream()))
//        ) {
//            if (debug) logger.info("Waiting for incoming requests!");
//            String request = in.readLine();
//            if (debug) logger.info("Request received: " + request);
//            Object reply = invokeMethod();
//            if (debug && reply != null) logger.info("Reply: " + reply);
//            if (NameServiceProtocol.getType(request).equals(NameServiceProtocol.RESOLVE)) {
//                if(debug) logger.info("Sending reply: "+reply);
//                out.println(reply);
//            }
//
//            clientSocket.close();
//            if (debug) logger.info("Job done!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


    /**
     * This uses Java's Reflection mechanism. If you find a way to do without, please do so and write me a message ;)
     * see https://docs.oracle.com/javase/tutorial/reflect/member/methodInvocation.html
     *
     * @param objectName
     * @param className
     * @param methodName
     * @param params
     * @return
     */
    private Object invokeMethod(String objectName, String className, String methodName, String params) {
        Object object = this.nameService.resolveLocally(objectName);
        try {
            Class<?> c = Class.forName(className);
//          Object t = c.newInstance();
            Object t = c.getDeclaredConstructor().newInstance();

//            c.getMethod()
            Method[] allMethods = c.getDeclaredMethods();
            for (Method m : allMethods) {
                String mname = m.getName();
                Type[] pType = m.getGenericParameterTypes();
                if ((pType.length != 1)
                        || Locale.class.isAssignableFrom(pType[0].getClass())) {
                    continue;
                }

                System.out.format("invoking %s()%n", mname);
                try {
                    m.setAccessible(true);
//                    Object o = m.invoke(t, new Locale(args[1], args[2], args[3]));
                    Object o = m.invoke(t, params);
                    System.out.format("%s() returned %b%n", mname, (Boolean) o);

                    // Handle any exceptions thrown by method to be invoked.
                } catch (InvocationTargetException x) {
                    Throwable cause = x.getCause();
                    System.err.format("invocation of %s failed: %s%n",
                            mname, cause.getMessage());
                }
            }

            // production code should handle these exceptions more gracefully
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        } catch (InstantiationException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}



