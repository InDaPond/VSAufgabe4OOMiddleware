package mware_lib;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    public static Class[] getParameterTypes(Object[] params) throws ClassNotFoundException {
        Class[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            Class paramClass = params[i].getClass();
            if (paramClass == Double.class) {
                paramTypes[i] = (double.class);
            } else if (paramClass == Integer.class) {
                paramTypes[i] = (int.class);
            } else {
                paramTypes[i] = paramClass;
            }
        }
        return paramTypes;
    }

    //Bad hack, only works b/c we know which types have to be supported and that number is very limited + all basic
    // types
    public static Class getParameterType(String param) {
        Object type;
        try {
            type = Double.parseDouble(param);
        } catch (NumberFormatException e1) {
            try {
                type = Integer.parseInt(param);
            } catch (NumberFormatException e2) {
                type = param;
            }
        }
        Class paramType;

        Class paramClass = type.getClass();
        if (paramClass == Double.class) {
            paramType = (double.class);
        } else if (paramClass == Integer.class) {
            paramType = (int.class);
        } else {
            paramType = paramClass;
        }

        return paramType;
    }


}
