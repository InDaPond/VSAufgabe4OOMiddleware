package test;

import mware_lib.ApplicationProtocol;
import mware_lib.ReflectionUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ReflectionTest {

    @Test
    public void testParameterReflection(){
//        Object[] paramTypes = new Object[]{2.0,Double.valueOf(2),"a",new String(),3,Integer.valueOf(3)};
//        for (Object c : paramTypes){
//            System.out.println(ReflectionUtil.toClass(c));
//        }
//        String[] paramsAsString = new String[paramTypes.length];
//        for (int i =0;i<paramTypes.length;i++){
//            paramsAsString[i] = paramTypes[i].toString();
//            System.out.println("parsed: " + paramsAsString[i]);
//        }
        String request = ApplicationProtocol.requestMethodExecution("wurst","Wurst","schneiden",2.0,3.0);
        System.out.println(request);
        String params = ApplicationProtocol.getParams(request);
        System.out.println(params);
        try {
            Class[] types = ReflectionUtil.getParameterType(new String[]{2.0,3.0});
            System.out.println(types);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //        try {
//            ReflectionUtil.getParameterClassObjects(paramsAsString);
//            System.out.println("!!");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(ReflectionUtil.getParameterType(paramsAsString));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


    }
}
