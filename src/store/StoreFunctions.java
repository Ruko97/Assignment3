package store;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

public class StoreFunctions {
    public PrintWriter printWriter;
    public String message;
    public String method;
    public String fileAddress;
    public String httpVersion;


    public StoreFunctions(String message, PrintWriter printWriter){
        this.printWriter = printWriter;
        this.message = message;

    }


    public String analyseMessage() throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(message);
        method = stringTokenizer.nextToken();
        fileAddress = stringTokenizer.nextToken();
        httpVersion = stringTokenizer.nextToken();

        if (method.equals("GET") && fileAddress.equals("/index.html")) {
            return HTTPIndexGETResponse();
        } else {
            return "501 Not Implemented";
        }
    }

    public String makeHeader(){
        String header = httpVersion+" 200 OK\n";
        header += "Date: "+ new Date().toString() + '\n';
        header += "Server: Custom Java HTTP Server\n";
        header += "Content-Type: text/html\n\r";
        return header;
    }

    public String HTTPIndexGETResponse() throws IOException {

        StringBuilder output = new StringBuilder();

        output.append(makeHeader());
        printWriter.println(output.toString());

        String actualFileAddress = "src/store/webpages"+fileAddress;

        File HTMLFile = new File(actualFileAddress);
        if (HTMLFile.exists()) {
            output.setLength(0);
            FileReader fileReader = new FileReader(HTMLFile);
            char[] chars = new char[(int) HTMLFile.length()];
            fileReader.read(chars);
            output.append(chars);
            output.append('\n');

            printWriter.println(output.toString());
            return output.toString();
        } else {
            printWriter.println("404 Not Found");
            return "404 Not Found";
        }
    }
}
