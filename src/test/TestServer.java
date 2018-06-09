package test;

import math_ops._CalculatorImplBase;
import mware_lib.ObjectBroker;
import mware_lib.NameService;

public class TestServer {
    private String host;
    private int port;

    public TestServer(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void rebindCalculator(String name){
        class Calculator extends _CalculatorImplBase {

            @Override
            public double add(double a, double b) {
                return a+b;
            }

            @Override
            public String getStr(double a) {
                return Double.toString(a);
            }
        }

        ObjectBroker objBroker = ObjectBroker.init("localhost",8500,true);
        NameService nameService = objBroker.getNameService();
        nameService.rebind((Object) new Calculator(),name);
    }
}
