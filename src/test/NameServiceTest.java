package test;

import math_ops._CalculatorImplBase;
import mware_lib.NameServiceProxy;
import mware_lib.ObjectBroker;
import mware_lib.NameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NameServiceTest {

    static String host = "localhost";
    static int port = 8500;
    private static ObjectBroker objBroker;
    private static NameService nameService;

    @BeforeAll
    static void init() {
        System.out.println("!!!");
        nameservice.NameService.main(new String[]{String.valueOf(port)});
        System.out.println("---");
    }

    @Test
    public void testLocalBinding() {
        class Calculator extends _CalculatorImplBase {

            @Override
            public double add(double a, double b) {
                return a + b;
            }

            @Override
            public String getStr(double a) {
                return Double.toString(a);
            }
        }

        ObjectBroker objBroker = ObjectBroker.init(host, port, true);
        NameServiceProxy nameService = (NameServiceProxy) objBroker.getNameService();
        nameService.rebind((Object) new Calculator(), "myCalculator");
        Object result = nameService.resolveLocally("myCalculator");
        Calculator myCalc = (Calculator) result;
        assertEquals(5.0, myCalc.add(2, 3));
    }

    @Test
    public void testResolve() {
        TestClient client = new TestClient(host, port);
        TestServer server = new TestServer(host, port);
        server.rebindCalculator("myCalculator");
        _CalculatorImplBase calculator = client.resolveCalculator("myCalculator");
        System.out.println(calculator);
    }
}
