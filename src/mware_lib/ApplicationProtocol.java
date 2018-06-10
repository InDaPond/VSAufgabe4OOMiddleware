package mware_lib;

import java.util.Arrays;

public class ApplicationProtocol {
    public static String requestMethodExecution(String objectName, String className, String methodName, Object...
            params) {
        return String.format("%s,%s,%s,%s", objectName, className, methodName, Arrays.toString(params));
    }

    public static String getObjectName(String message){
        return message.split(",")[0];
    }

    public static String getClassName(String message){
        return message.split(",")[1];
    }

    public static String getEssentialClassName(String message){
        return message.split(",")[1].substring(1).replaceAll("ImplBase","");
    }

    public static String getMethodName(String message){
        return message.split(",")[2];
    }

    // dirty hack to deal with varargs, should def be revamped!
    public static String getParams(String message){
        int indexOfParamStart = message.indexOf("[");
        int indexOfParamEnd = message.indexOf("]")+1;
        System.out.println(message.substring(indexOfParamStart,indexOfParamEnd));
        return message.substring(indexOfParamStart,indexOfParamEnd);
    }
}
