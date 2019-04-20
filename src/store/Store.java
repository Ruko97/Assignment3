package store;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Store {
    public static void main(String[] args) throws IOException {
        int storePort = Integer.parseInt(args[0]);
        InetAddress bankAddress = InetAddress.getByName(args[1]);
        int bankPort = Integer.parseInt(args[2]);

        Socket bankSocket = new Socket(bankAddress, bankPort);
        System.out.println("Connected to bank server at " +
                bankAddress.toString() + " at port " + bankPort );

        PrintWriter printWriter = new PrintWriter(
                bankSocket.getOutputStream(), true );
        printWriter.println("PAY 100 FROM Jim Morrison 2052 12345678");

        bankSocket.close();
    }
}
