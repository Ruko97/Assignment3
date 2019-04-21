package store;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Store {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) throws Exception{
        int storePort = Integer.parseInt(args[0]);
        InetAddress bankAddress = InetAddress.getByName(args[1]);
        int bankPort = Integer.parseInt(args[2]);

        Socket bankSocket = new Socket(bankAddress, bankPort);
        System.out.println("Connected to bank server at " +
                bankAddress.toString() + " at port " + bankPort );

        PrintWriter bankPrintWriter = new PrintWriter( bankSocket.getOutputStream(), true );

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

                StoreFunctions storeFunctions = new StoreFunctions(message, printWriter, bufferedReader, bankPrintWriter);

                System.out.println(ANSI_RED+message+ANSI_RESET);
                String response = storeFunctions.analyseMessage();
                System.out.println(ANSI_BLUE+response+ANSI_RESET);

                storeClientSocket.close();
            }
        }
    }
}
