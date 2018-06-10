package test;

import mware_lib.ApplicationProtocol;
import mware_lib.ReflectionUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionTest {

    @Test
    public void testParameterReflection() throws ClassNotFoundException {

        String request = ApplicationProtocol.requestMethodExecution("wurst","Wurst","schneiden",2.0,3.0,"hallo");
        System.out.println(request);
        String[] values = ApplicationProtocol.getParams(request);
        for(Object s : values){
            System.out.println("Value: "+s);
            System.out.println(String.valueOf(s));
        }
        List<String> resultList = new ArrayList<>(Arrays.asList(values));
        String[] transformedList = resultList.toArray(new String[values.length]);
        System.out.println(resultList);
        System.out.println(Arrays.toString(transformedList));
        for(String o : transformedList){
            System.out.println("o: " + ReflectionUtil.getParameterType(o));
            System.out.println(ReflectionUtil.getParameterType(o));
        }
        Class[] result = ReflectionUtil.getParameterTypes(transformedList);
        for(Class c : result){
            System.out.println("Class: "+c);
                }


    }
}
