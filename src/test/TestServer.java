package test;

import bank._BankImplBase;
import math_ops._CalculatorImplBase;
import mware_lib.ObjectBroker;
import mware_lib.NameService;

public class TestServer {
    private String host;
    private int port;

    public TestServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void rebindCalculator(String name) {
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

        ObjectBroker objBroker = ObjectBroker.init("localhost", 8500, true);
        NameService nameService = objBroker.getNameService();
        nameService.rebind((Object) new Calculator(), name);
    }

    public void rebindBankAccount(String name) {
        class BankAccount extends _BankImplBase {
            private double balance;

            public BankAccount() {
                this.balance = 0;
            }

            @Override
            public double deposit(double amount) {
                this.balance += amount;
                return balance;

            }

            @Override
            public double withdraw(double amount) {
                if (amount < 0) {
                    throw new RuntimeException("You can't withdraw a negative amount, smartass");
                }
                if (amount > balance) {
                    throw new RuntimeException("Sorry to tell you, but you are poor.");
                }
                balance -= amount;
                return balance;
            }

            @Override
            public String balanceInquiry() {
                return Double.toString(balance);
            }
        }
        ObjectBroker objBroker = ObjectBroker.init("localhost", 8500, true);
        NameService nameService = objBroker.getNameService();
        nameService.rebind((Object) new BankAccount(), name);
    }
}
