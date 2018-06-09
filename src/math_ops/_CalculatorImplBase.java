package math_ops;
import mware_lib.RemoteDelegator;
public abstract class _CalculatorImplBase {
public abstract double add (double a, double b);
public abstract String getStr (double a);
public static _CalculatorImplBase narrowCast(Object rawObjectRef){
return new _CalculatorImplBase(){
@Override
public double add (double a, double b){
return (double) RemoteDelegator.invokeMethod("_CalculatorImplBase","add", a, b);}
@Override
public String getStr (double a){
return (String) RemoteDelegator.invokeMethod("_CalculatorImplBase","getStr", a);}
};
}
}