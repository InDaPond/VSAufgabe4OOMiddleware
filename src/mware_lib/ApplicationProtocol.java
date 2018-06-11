package mware_lib;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationProtocol {

    public static final String SUCCESS = "ok";
    public static final String FAILURE = "nok";
    public static String requestMethodExecution(String objectName, String className, String methodName, Object...
            params) {
        return String.format("%s,%s,%s,%s", objectName, className, methodName, Arrays.toString(params));
    }

    public static String createReply(Object result, Throwable throwable){
        Object[] tmp = new Object[]{result, throwable};
        return String.format("%s",Arrays.toString(tmp));
    }

    public static String getObjectName(String message) {
        return message.split(",")[0];
    }

    public static String getClassName(String message) {
        return message.split(",")[1];
    }

    public static String getEssentialClassName(String message) {
        return message.split(",")[1].substring(1).replaceAll("ImplBase", "");
    }

    public static String getMethodName(String message) {
        return message.split(",")[2];
    }

    //     dirty hack to deal with varargs, should def be revamped!
    public static String[] getParams(String message) {

        Pattern p = Pattern.compile("\\[(.*)]");
        Matcher m = p.matcher(message);
        while (m.find()) {
            return (m.group(1)).split(",");
        }
        //in case it does not work as expected
                int indexOfParamStart = message.indexOf("[");
        int indexOfParamEnd = message.indexOf("]")+1;
        String[] splitMessage = message.substring(indexOfParamStart, indexOfParamEnd).replaceAll("\\[", "")
                .replaceAll("]", "").replaceAll(" ","").split(",");
        return splitMessage;

    }

}