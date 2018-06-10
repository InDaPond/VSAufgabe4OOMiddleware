package math_ops;
import mware_lib.RemoteDelegator;
public abstract class _CalculatorImplBase {
public abstract double add (double a, double b);
public abstract String getStr (double a);
public static _CalculatorImplBase narrowCast(Object rawObjectRef){
return new _CalculatorImplBase(){
private String name = rawObjectRef.toString().split(",")[0];
private String host = rawObjectRef.toString().split(",")[1];
private int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
@Override
public double add (double a, double b){
return (double) RemoteDelegator.invokeMethod(name, host, port,"_CalculatorImplBase","add", a, b);}
@Override
public String getStr (double a){
return (String) RemoteDelegator.invokeMethod(name, host, port,"_CalculatorImplBase","getStr", a);}
};
}
}