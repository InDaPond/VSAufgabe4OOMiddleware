package mware_lib;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helps to retrieve the object and call the method on it.
 * If you know a way without reflection please write me a mail
 * to Philipp.Goemann@haw-hamburg.de  since I have no idea how to do so.
 * B/c of the protocol there also some regex hacks. This module is not pretty, but works.
 */
public class ReflectionUtil {

    public static final Logger logger = Logger.getLogger(ReflectionUtil.class.getName());
    public static boolean debug;

    public static Class[] getParameterTypes(String[] params) {
        int length = params.length;
        Class[] result = new Class[length];
        for (int i = 0; i < length; i++) {
            result[i] = getParameterType(params[i]);
        }
        return result;
    }

    public static Object[] getParameterValues(String[] params) {
        int length = params.length;
        Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            result[i] = getParameterValue(params[i]);
        }
        return result;
    }

    //Bad hack, only works b/c we know which types have to be supported and that number is very limited + all basic
    // types
    public static Class getParameterType(String param) {

        Class paramType;
        Class paramClass = getParameterValue(param).getClass();
        if (paramClass == Double.class) {
            paramType = (double.class);
        } else if (paramClass == Integer.class) {
            paramType = (int.class);
        } else {
            paramType = paramClass;
        }

        return paramType;
    }

    //removing whitespace b/c a String Array has them in them, so instead of
    //3
    // 3  will be passed which does not work for int parsing
    //If you pass "3" and it is meant a String, it goes all down the drain..
    public static Object getParameterValue(String param) {
        try {
            if (param.equals(" null")) {
                return null;
            }
            return Integer.parseInt(param.replaceFirst(" ", ""));
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(param.replaceFirst(" ", ""));
            } catch (NumberFormatException e2) {
                return param.replaceFirst(" ", "");
            }
        }
    }

    //Example input: [null, java.lang.NoSuchMethodException: test.TestServer$1Calculator.add(int, int)]
    public static Object getException(String param) {


        String subStr = param.replaceFirst("\\[null, ", "");

        Pattern exceptionMsg = Pattern.compile(" (.*)");
        Matcher m = exceptionMsg.matcher(subStr);
        String exceptionMessage = null;
        if (m.find()) {
            exceptionMessage = (m.group(1)).replace("]", "");
        }

        if (debug) logger.info("exceptionMessage= " + exceptionMessage);
        Pattern exceptionCls = Pattern.compile("(.*):");
        Matcher n = exceptionCls.matcher(subStr);
        String exceptionClass = null;
        if (n.find()) {
            exceptionClass = (n.group(1));
        }
        if (debug) logger.info("exceptionClass= " + exceptionClass);


        try {
            Class c = Class.forName(exceptionClass);

            Object p = c.getDeclaredConstructor(String.class).newInstance(exceptionMessage);
            if (debug) logger.info("returning: " + p);
            return p;


        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
                | InstantiationException e) {
            e.printStackTrace();
        }
        return param;
    }


}
