package mware_lib;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

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
    public static Object getParameterValue(String param) {
        try {
            return Integer.parseInt(param.replaceFirst(" ",""));
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(param.replaceFirst(" ",""));
            } catch (NumberFormatException e2) {
                return param.replaceFirst(" ","");
            }
        }
    }


}
