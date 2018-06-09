package test;

import math_ops._CalculatorImplBase;
import mware_lib.ObjectBroker;
import nameservice.NameService;

public class TestClient {
    private String host;
    private int port;

    public TestClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public _CalculatorImplBase resolveCalculator(String name) {
        ObjectBroker objectBroker = ObjectBroker.init(host,port,true);
        NameService nameService = objectBroker.getNameService();
        Object rawObjRef = nameService.resolve(name);
        _CalculatorImplBase remoteObj = _CalculatorImplBase.narrowCast(rawObjRef);
        return remoteObj;
    }
}
