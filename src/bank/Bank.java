package bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Bank {
    public static void main(String[] args) throws IOException {
        int portno = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portno);
        System.out.println("Created socket to port " + portno);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Successfully connected to " + clientSocket.getInetAddress());
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()) );

            PrintWriter printWriter = new PrintWriter( clientSocket.getOutputStream() );

            String message;
            if ( (message = bufferedReader.readLine()) != null) {
                    printWriter.println(BankFunctions.analyseMessage(message));
            }

            clientSocket.close();
        }

    }
}
