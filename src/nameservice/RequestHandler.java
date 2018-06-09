package nameservice;

import java.io.BufferedReader;
import java.io.PrintWriter;


public class RequestHandler implements Runnable {

    private BufferedReader in;
    private PrintWriter out;

    public RequestHandler(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {

    }
}
