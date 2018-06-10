package test;

import mware_lib.ApplicationProtocol;
import mware_lib.ReflectionUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {

    @Test
    public void testParameterReflection() throws ClassNotFoundException {


       String request = ApplicationProtocol.requestMethodExecution("wurst", "Wurst", "schneiden", 2.0, 3, "hallo");
                Class[] types = new Class[]{double.class, int.class, String.class};
        Class[] reflectedTypes = ReflectionUtil.getParameterTypes(ApplicationProtocol.getParams(request));
        for (int i = 0; i < types.length; i++) {
            assertEquals(types[i], reflectedTypes[i]);
        }
        Object[] reflectedValues = ReflectionUtil.getParameterValues(ApplicationProtocol.getParams(request));
        Object[] values = new Object[]{2.0,3,"hallo"};
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], reflectedValues[i]);
        }

    }
}
