package test;

import math_ops._CalculatorImplBase;
import mware_lib.ApplicationProtocol;
import mware_lib.ReflectionUtil;
import mware_lib.RemoteDelegator;
import mware_lib.RequestHandler;
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

    static String host = "localhost";
    static int port = 8500;

    @BeforeAll
    static void init() {
        nameservice.NameService.main(new String[]{String.valueOf(port)});
    }

    @Test
    public void testParameterReflection() throws ClassNotFoundException {


        String request = ApplicationProtocol.requestMethodExecution("wurst", "Wurst", "schneiden", 2.0, 3, "hallo");
        Class[] types = new Class[]{double.class, int.class, String.class};
        Class[] reflectedTypes = ReflectionUtil.getParameterTypes(ApplicationProtocol.getParams(request));
        for (int i = 0; i < types.length; i++) {
            assertEquals(types[i], reflectedTypes[i]);
        }
        Object[] reflectedValues = ReflectionUtil.getParameterValues(ApplicationProtocol.getParams(request));
        Object[] values = new Object[]{2.0, 3, "hallo"};
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], reflectedValues[i]);
        }

    }

    @Test
    public void testReflection() {
        TestClient client = new TestClient(host, port);
        TestServer server = new TestServer(host, port);
        server.rebindCalculator("myCalculator");
        _CalculatorImplBase calculator = client.resolveCalculator("myCalculator");
        assertEquals(5.0, calculator.add(2, 3));

    }

    @Test
    public void testReflectionErrorWrongParameter() {
        TestClient client = new TestClient(host, port);
        TestServer server = new TestServer(host, port);
        server.rebindCalculator("myCalculator");
        Object result = RemoteDelegator.invokeMethod("myCalculator", host, 9999, "Should not matter", "add", 2, "bla");
        NoSuchMethodException expectedT = new NoSuchMethodException("test.TestServer$1Calculator.add(int, java.lang" +
                ".String)");
        assertEquals(expectedT.getClass(), result.getClass());
        NoSuchMethodException typeCast = (NoSuchMethodException) result;
        assertEquals(expectedT.getMessage(), typeCast.getMessage());
    }

    @Test
    public void testReflectionErrorWrongMethodName() {
        TestClient client = new TestClient(host, port);
        TestServer server = new TestServer(host, port);
        server.rebindCalculator("myCalculator");
        Object result = RemoteDelegator.invokeMethod("myCalculator", host, 9999, "Should not matter", "foobar", 2, 3);
        NoSuchMethodException expectedT = new NoSuchMethodException("test.TestServer$1Calculator.foobar(int, int)");
        assertEquals(expectedT.getClass(), result.getClass());
        NoSuchMethodException typeCast = (NoSuchMethodException) result;
        assertEquals(expectedT.getMessage(), typeCast.getMessage());
    }
}
