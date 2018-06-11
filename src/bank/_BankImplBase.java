package bank;

import mware_lib.RemoteDelegator;

public abstract class _BankImplBase {
    public abstract double deposit(double amount);

    public abstract double withdraw(double amount);

    public abstract String balanceInquiry();

    public static _BankImplBase narrowCast(Object rawObjectRef) {
        return new _BankImplBase() {
            public String name = rawObjectRef.toString().split(",")[0];
            public String host = rawObjectRef.toString().split(",")[1];
            public int port = Integer.parseInt(rawObjectRef.toString().split(",")[2]);

            @Override
            public double deposit(double amount) throws RuntimeException {
                Object result = RemoteDelegator.invokeMethod(name, host, port, "_BankImplBase", "deposit", amount);
                if (result instanceof RuntimeException) throw (RuntimeException) result;
                return (double) result;
            }

            @Override
            public double withdraw(double amount) throws RuntimeException {
                Object result = RemoteDelegator.invokeMethod(name, host, port, "_BankImplBase", "withdraw", amount);
                if (result instanceof RuntimeException) throw (RuntimeException) result;
                return (double) result;
            }

            @Override
            public String balanceInquiry() throws RuntimeException {
                Object result = RemoteDelegator.invokeMethod(name, host, port, "_BankImplBase", "balanceInquiry");
                if (result instanceof RuntimeException) throw (RuntimeException) result;
                return String.valueOf(result);
            }
        };
    }
}