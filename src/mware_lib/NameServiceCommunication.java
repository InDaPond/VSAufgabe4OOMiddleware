package mware_lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class NameServiceCommunication implements Runnable {
    private int port;
    private String host;
    private Socket mySock;
    private BufferedReader in;
    private OutputStream out;

    public NameServiceCommunication(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

        try {

            // Verbindung aufbauaen
            mySock = new Socket("localhost", 14001);

            // I/O-Kanäle der Socket
            in = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
            out = mySock.getOutputStream();

            // Kommunikation
            out.write(("Knock, knock!\n").getBytes());
            System.out.println(in.readLine());

            // Verbindung schliessen
            in.close();
            out.close();
            mySock.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
