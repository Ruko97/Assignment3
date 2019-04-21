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
    public String message;
    public String method;
    public String fileAddress;
    public String httpVersion;
    public BufferedReader bufferedReader;
    public String first, family, post, credit_card, choice;

    StoreFunctions(String message, PrintWriter printWriter, BufferedReader bufferedReader){
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.message = message;

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
            return payload.toString();
        }
        else {
            return "501 Not Implemented";
        }
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

        String actualFileAddress = "src/store/webpages"+fileAddress;

        File HTMLFile = new File(actualFileAddress);
        output.setLength(0);
        FileReader fileReader = new FileReader( HTMLFile );
        char[] chars = new char[(int) HTMLFile.length()];
        fileReader.read(chars);
        output.append(chars);
        output.append('\n');

        printWriter.println(output.toString());
        return output.toString();
    }
}