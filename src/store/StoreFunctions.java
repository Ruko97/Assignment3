package store;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

public class StoreFunctions {
    public static String analyseMessage(String message, PrintWriter printWriter) throws IOException {
        if (message.startsWith("GET")) {
            return getHTTPResponse(message, printWriter);
        } else {
            return "501 Not Implemented";
        }
    }

    private static String getHTTPResponse(String message, PrintWriter printWriter) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(message);
        stringTokenizer.nextToken();
        String fileAddress = stringTokenizer.nextToken();
        String HTMLVersion = stringTokenizer.nextToken();

        StringBuilder output = new StringBuilder();
        output.append(HTMLVersion);

        fileAddress = "src/store/webpages" + fileAddress;
        System.out.println(fileAddress);

        File HTMLFile = new File( fileAddress );

        if (HTMLFile.exists()) {
            output.append(" 200 OK\n");
            output.append("Date: " + new Date().toString() + '\n');
            output.append("Server: Custom Java HTTP Server\n");
            output.append("Content-Type: text/html\n\r");

            printWriter.println(output.toString());
            output.setLength(0);

            FileReader fileReader = new FileReader( HTMLFile );
            char[] chars = new char[(int) HTMLFile.length()];
            fileReader.read(chars);
            output.append(chars);
            output.append('\n');

            printWriter.println(output.toString());
            return output.toString();

        } else {
            return "404 Not Found";
        }
    }
}
