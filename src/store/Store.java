package store;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
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

        ServerSocket storeServer = new ServerSocket(storePort);
        System.out.println("Created store socket to port " + storePort);

        while (true) {
            Socket storeClientSocket = storeServer.accept();
            System.out.println("Successfully connected to " + storeClientSocket.getInetAddress());

            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(
                    storeClientSocket.getInputStream()) );

            PrintWriter printWriter = new PrintWriter( storeClientSocket.getOutputStream(),true );

            String message;
            if ( (message = bufferedReader.readLine()) != null ) {
                String response = StoreFunctions.analyseMessage(message, printWriter);
                System.out.println(response);
                storeClientSocket.close();
            }
        }
    }
}
