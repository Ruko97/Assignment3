package store;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

public class StoreFunctions {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public PrintWriter printWriter;
    public PrintWriter bankPrintWriter;
    public String message;
    public String method;
    public String fileAddress;
    public String httpVersion;
    public BufferedReader bufferedReader;
    public String first, family, post, credit_card, choice;

    StoreFunctions(String message, PrintWriter printWriter, BufferedReader bufferedReader, PrintWriter bankPrintWriter){
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.message = message;
        this.bankPrintWriter = bankPrintWriter;
    }

    String analyseMessage() throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(message);
        method = stringTokenizer.nextToken();
        fileAddress = stringTokenizer.nextToken();
        httpVersion = stringTokenizer.nextToken();

        if (method.equals("GET") && fileAddress.equals("/index.html")) {
            return HTTPIndexGETResponse();
        }

        else if(method.equals("POST") && fileAddress.equals("/index.html")){
            String parameter;
            while ((parameter=bufferedReader.readLine()).length()!=0){
                System.out.println(ANSI_RED+parameter+ANSI_RESET);
            }
            StringBuilder payload = new StringBuilder();
            while(bufferedReader.ready()){
                payload.append((char) bufferedReader.read());
            }
            System.out.println(ANSI_YELLOW+payload.toString()+ANSI_RESET);
            getInfoFromPayload(payload.toString());
            bankPrintWriter.println( "PAY " + getCostOf(choice) + "  FROM " + first + ' ' + family + ' ' + post + ' ' + credit_card);
            return payload.toString();
        }
        else {
            return "501 Not Implemented";
        }
    }

    private int getCostOf(String choice) {
        if (choice.equals("avengers")) return 100;
        else if (choice.equals("pokemon")) return 70;
        else if (choice.equals("joker")) return 80;
        return 0;
    }

    private void getInfoFromPayload(String payload){
        String[] tokens = payload.split("\\=");

        try {
            first = tokens[1].split("\\&")[0];
            family = tokens[2].split("\\&")[0];
            post = tokens[3].split("\\&")[0];
            credit_card = tokens[4].split("\\&")[0];
            choice = tokens[5];
        }catch (Exception e){
            System.out.println(ANSI_YELLOW+"Exception: Incompatible info"+ANSI_RESET);
        }
        System.out.print(first + " " + family + " " + post + " " + credit_card + " " + choice);
    }


    private String makeHeader(){
        String header = httpVersion+" 200 OK\n";
        header += "Date: "+ new Date().toString() + '\n';
        header += "Server: Custom Java HTTP Server\n";
        header += "Content-Type: text/html\n\r";
        return header;
    }

    private String HTTPIndexGETResponse() throws IOException {
        StringBuilder output = new StringBuilder();

        output.append(makeHeader());
        printWriter.println(output.toString());

        output.setLength(0);

        String actualFileAddress = "src/store/webpages"+fileAddress;
        File HTMLFile = new File(actualFileAddress);
        FileReader fileReader = new FileReader( HTMLFile );
        char[] chars = new char[(int) HTMLFile.length()];
        fileReader.read(chars);
        output.append(chars);
        output.append('\n');

        printWriter.println(output.toString());
        return output.toString();
    }

    String analyseBankResponse(String response) {
        StringBuilder output = new StringBuilder();

        output.append(makeHeader());
        printWriter.println(output.toString());

        output.setLength(0);

        String[] tokens = response.split("\\s", 2);
        int responseCode = Integer.parseInt(tokens[0]);
        output.append( createHTMLBody(responseCode) );

        printWriter.println(output.toString());
        return output.toString();
    }

    private String createHTMLBody(int responseCode) {
        if (responseCode == 200) return "Transaction Successful";
        else if (responseCode == 401) return first + ", you don't have enough credits";
        else if (responseCode == 402) return "No such user exists";
        else return "Not implemented yet";
    }
}