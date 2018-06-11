package complexSyntax;
import mware_lib.RemoteDelegator;
public abstract class _ClassTwoImplBase {
public abstract String stringMethod1 (String s1, String s2, int i, double d);
public abstract String stringMethod2 (double d);
public static _ClassTwoImplBase narrowCast(Object rawObjectRef){
return new _ClassTwoImplBase(){
public String name = rawObjectRef.toString().split(",")[0];
public String host = rawObjectRef.toString().split(",")[1];
public int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);
@Override
public String stringMethod1 (String s1, String s2, int i, double d) throws RuntimeException {
return (String) RemoteDelegator.invokeMethod(name, host, port,"_ClassTwoImplBase","stringMethod1", s1, s2, i, d);}
@Override
public String stringMethod2 (double d) throws RuntimeException {
return (String) RemoteDelegator.invokeMethod(name, host, port,"_ClassTwoImplBase","stringMethod2", d);}
};
}
}