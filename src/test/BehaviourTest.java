package test;

import bank._BankImplBase;
import math_ops._CalculatorImplBase;
import mware_lib.NameServiceProxy;
import mware_lib.ObjectBroker;
import mware_lib.NameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BehaviourTest {

    static String host = "localhost";
    static int port = 8500;
    private static ObjectBroker objBroker;
    private static NameService nameService;

    @BeforeAll
    static void init() {
        nameservice.NameService.main(new String[]{String.valueOf(port)});
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
        assertEquals(6, calculator.add(2.3, 3.7));
    }


    @Test
    public void testNonExistentObject() {
        TestClient client = new TestClient(host, port);
        TestServer server = new TestServer(host, port);
        server.rebindCalculator("myCalculator");
        _CalculatorImplBase calculator = client.resolveCalculator("myCalculator");
        objBroker = ObjectBroker.init(host, port, true);
        nameService = objBroker.getNameService();
        nameService.rebind("", "myCalculator");
        Object result;
        try {
            result = calculator.add(2, 3);
        } catch (RuntimeException re) {
            result = re;
        }
        assert (result instanceof RuntimeException);

    }

    @Test
    public void testValidCashTransactions() {
        TestClient client = new TestClient(host, port);
        TestServer server = new TestServer(host, port);
        server.rebindBankAccount("myBankAccount");
        _BankImplBase account = client.resolveBankAccount("myBankAccount");
        assertEquals("0.0", account.balanceInquiry());
        assertEquals(500, account.deposit(500));
        assertEquals("500.0", account.balanceInquiry());
        System.out.println(account.balanceInquiry());
        assertEquals(225.50, account.withdraw(274.50));
    }

    @Test
    public void testInvalidCashTransactions() {
        TestClient client = new TestClient(host, port);
        TestServer server = new TestServer(host, port);
        server.rebindBankAccount("myBankAccount");
        _BankImplBase account = client.resolveBankAccount("myBankAccount");
        assertEquals("0.0", account.balanceInquiry());
        assertEquals(500, account.deposit(500));
        RuntimeException expectedT = new RuntimeException("Doesn't matter, fails anyway");
        Object result;
        try {
            result = account.withdraw(1000);
            System.out.println("worked " + result);
        } catch (RuntimeException re) {
            result = re;
            System.out.println("Didn't work: " + result.getClass());
        }
        assertEquals(expectedT.getClass(), result.getClass());
        System.out.println(result);
    }


}
