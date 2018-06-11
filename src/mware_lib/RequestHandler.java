package mware_lib;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;
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
        try (
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))
        ) {
            if (debug) logger.info("Waiting for incoming requests!");
            String request = in.readLine();
            if (debug) logger.info("Request received: " + request);
            Object reply = invokeMethod(ApplicationProtocol.getObjectName(request), ApplicationProtocol.getClassName
                    (request), ApplicationProtocol.getMethodName(request),ApplicationProtocol.getParams(request));
            if (debug && reply != null) logger.info("Reply: " + reply);
            out.println(debug);
            clientSocket.close();
            if (debug) logger.info("Job done!");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try (

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
    private Object invokeMethod(String objectName, String className, String methodName, String[] params) {
        if (debug) logger.info(String.format("invoking for: %s,%s,%s,%s", objectName, className, methodName, Arrays
                .toString(params)));
        Object target = this.nameService.resolveLocally(objectName);
        try {


//            String[] parmeters = ApplicationProtocol.getParams(params);
//            String[] parameters = params;
            Class[] parameterTypes = ReflectionUtil.getParameterTypes(params);
            if(debug)logger.info("Parameter Types: "+ Arrays.toString(parameterTypes));
            Object[] parameterValues = ReflectionUtil.getParameterValues(params);
            if(debug)logger.info("Parameter Values: "+ Arrays.toString(parameterValues));
            Class<?> targetClass = target.getClass();
            if(debug)logger.info("target Class: "+ targetClass);
            Method method = targetClass.getMethod(methodName, parameterTypes);
            if(debug)logger.info("targeted Method: "+ method);
            Object methodResult = method.invoke(target, parameterValues);
            if(debug)logger.info("received result: "+ methodResult);
            return methodResult;


            // production code should handle these exceptions more gracefully

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException x) {
            Throwable cause = x.getCause();
            System.err.format("invocation of %s failed: %s%n",
                    methodName, cause.getMessage());
        }
        //Hopefulle won't reach this
        return null;
    }


}