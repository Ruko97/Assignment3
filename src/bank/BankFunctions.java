package bank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class BankFunctions {
    static String analyseMessage(String message) throws IOException {
        if (message.startsWith("PAY")) {
            return payMoney(message);
        } else {
            return "101 COMMAND NOT IMPLEMENTED";
        }
    }

    /* The format of the message should be PAY (amount) FROM (firstname) (familyname)
       (postcode) (creditno) */
    private static String payMoney(String message) throws IOException {
        String firstName, familyName, postcode, creditno;
        int amount;
        List<String> newText = new ArrayList<>();

        try {
            String[] tokens = message.split("\\s");

            amount = Integer.parseInt(tokens[1]);
            firstName = tokens[3];
            familyName = tokens[4];
            postcode = tokens[5];
            creditno = tokens[6];

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return "102 COMMAND FORMAT WRONG";
        }

        File file = new File("src/bank/database.txt");
        String output = "402 USER NOT FOUND";

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            newText.add(fileReader.readLine());

            while (fileReader.ready()) {
                String record = fileReader.readLine();
                String[] fields = record.split(",");

                String recordFirstName = fields[0];
                String recordFamilyName = fields[1];
                String recordPostcode = fields[2];
                String recordCreditno = fields[3];

                int recordAmount = Integer.parseInt(fields[5]);

                if (firstName.equals(recordFirstName) && familyName.equals(recordFamilyName)
                        && postcode.equals(recordPostcode) && creditno.equals(recordCreditno)) {
                    if (amount <= recordAmount) {
                        int newAmount = recordAmount - amount;
                        fields[5] = String.valueOf(newAmount);

                        output = "200 TRANSACTION SUCCESSFUL";
                    } else {
                        output = "401 INSUFFICIENT CREDITS";
                    }
                }

                record = String.join(",", fields);

                newText.add(record);
            }

        }

        try (PrintWriter printWriter = new PrintWriter( new FileWriter(file) )) {
            for (String newRecord : newText) {
                printWriter.println(newRecord);
            }
        }

        return output;
    }
}
