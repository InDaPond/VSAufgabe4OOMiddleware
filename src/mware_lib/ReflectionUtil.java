package mware_lib;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    public static Class[] getParameterType(String ...params) throws ClassNotFoundException {
            Class[] paramTypes = new Class[params.length];
            for(int i=0; i<params.length;i++){
                Class paramClass = Class.forName(params[i]);
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

    public static Object call(Object object, String method, Object... args) {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i] = toClass(args[i]);
        }
        try {
            return object.getClass().getDeclaredMethod(method, argTypes).invoke(object, args);
        } catch (InvocationTargetException e) {
            return e.getCause();
        } catch (Exception e) {
            return e;
        }
    }

    public static Class<?> toClass(Object o) {
        if (o.getClass() == Integer.class) {
            return int.class;
        } else if (o.getClass() == Double.class) {
            return double.class;
        }
        return o.getClass();
    }



}
