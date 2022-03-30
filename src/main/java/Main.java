import org.whywolk.topl.lexic.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        File file = new File("example.dmb");
        try(Scanner scanner = new Scanner(file)) {
            String doc = scanner.useDelimiter("\\Z").next();
            Solver solver = new Solver(doc);
            solver.start();
            System.out.format("%4s | %10s | %5s | %5s | %s %n", "Line", "Type", "Start", "End", "Value");
            System.out.println("----------------------------------------------");
            for(Token token : solver.getTokens()) {
                token.print();
            }


//            Pattern p = Pattern.compile("\\s+");
//            Matcher m = p.matcher(doc);
//
//            ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(p.split(doc)));
//            while(m.find()) {
//                tokens.add(m.group());
//            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
