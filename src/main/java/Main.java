import org.whywolk.topl.lexic.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File("example.dmb");
        try(Scanner scanner = new Scanner(file)) {
            String doc = scanner.useDelimiter("\\Z").next();

            Parser parser = new Parser(doc);
            parser.run();
//
//            System.out.format("%4s | %10s | %5s | %5s | %s %n", "Line", "Type", "Start", "End", "Value");
//            System.out.println("----------------------------------------------");
//            for(Token token : parser.getTokens()) {
//                token.print();
//            }

            if (parser.getErrors().isEmpty()) {
                IdTableBuilder idTableBuilder = new IdTableBuilder(parser.getAllTokens());
                idTableBuilder.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
