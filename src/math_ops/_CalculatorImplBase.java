package math_ops;
import mware_lib.RemoteDelegator;
public abstract class _CalculatorImplBase {
public abstract double add (double a, double b);
public abstract String getStr (double a);
public static _CalculatorImplBase narrowCast(Object rawObjectRef){
return new _CalculatorImplBase(){
public String name = rawObjectRef.toString().split(",")[0];
public String host = rawObjectRef.toString().split(",")[1];
public int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
@Override
public double add (double a, double b){
    System.out.printf("Name: %s, host:%s, port:%s\n",name,host,port);

return (double) RemoteDelegator.invokeMethod(name, host, port,"_CalculatorImplBase","add", a, b);}
@Override
public String getStr (double a){
return (String) RemoteDelegator.invokeMethod(name, host, port,"_CalculatorImplBase","getStr", a);}

};
}
}