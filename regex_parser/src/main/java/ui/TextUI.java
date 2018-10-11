package ui;

import domain.NFA;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.NFAGenerator;

public class TextUI {

    private Scanner scanner;
    private NFAGenerator nfaGenerator;

    public TextUI(Scanner scanner) {
        this.scanner = scanner;
        nfaGenerator = new NFAGenerator();

    }

    public TextUI() {
        //ääkkösten käsittely, näyttäysi toimivan tällä koodauksella
        this(new Scanner(System.in, "ISO-8859-1"));
    }

    public void run() {
        System.out.println("Welcome to Regex parser!\n");
        printInstructions();
        System.out.println("");

        int choice = chooseOperation();

        if (choice == 1) {
            testTrickyPerformance();
        } else if (choice == 2) {
            matchExpressionsAndStrings();
        }

        System.out.println("Goodbye!");
    }

    public void matchExpressionsAndStrings() {
        long start, generated, end; 

        while (true) {
            System.out.print("Enter a regular expression:\n> ");
            String regex = scanner.nextLine();

            System.out.print("Enter a test string:\n> ");
            String test = scanner.nextLine();
            String verb = "does not match";
            start = System.nanoTime();
            NFA nfa = nfaGenerator.generateNFA(regex);
            generated = System.nanoTime(); 
            boolean accepts = nfa.accepts(test);
            end = System.nanoTime(); 
            if (accepts) {
                verb = "matches";
            }
            

            System.out.println("Regular expression " + regex + " " + verb + " string " + test);
            System.out.println("Generation took " + ((generated - start) / 1000000) +" ms, and matching " + ((end - generated) / 1000000)  + " ms.");
            System.out.println("\nContinue? (y/n)");
            String input = scanner.nextLine();

            if (input.toLowerCase().equals("n")) {
                break;

            }

        }
    }

    private void printInstructions() {
        System.out.println("Supported characters in both regexes and input strings are a-z, A-Z and 0-9.");
        System.out.println("In regexes you can also use operations (, ), !, *, +, ?, [min,max] , first-last");
        System.out.println("See README.md or documentation for more explanation (might not be provided yet, though :P)");
    }

    private int chooseOperation() {
        int choice;
        while (true) {
            System.out.println("Choose by entering the corresponding number: ");
            System.out.println("1: Test performance with possibly tricky expressions");
            System.out.println("2: Match expressions and strings of your choice");
            System.out.println("3: Exit");
            System.out.print("> ");
            String input = scanner.nextLine();

            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 3) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (Exception e) {
                System.out.println("Input must be an integer between 1-3");
            }
        }

        return choice;
    }

    public void testTrickyPerformance() {
        System.out.println("Will compare the performance of two regex parses with patterns of the form '(a?){n}a{n}' when matched with 'a...a' (repeating n times)");

        int max;
        while (true) {
            System.out.print("Choose maximum for n: ");
            try {
                max = Integer.parseInt(scanner.nextLine());
                if (max < 1) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (Exception e) {
                System.out.println("Maximum n must be a positive integer!");
            }
        }

        for (int i = 1; i <= max; i++) {
            compareTricky(i);
        }

        System.out.println("Comparisons finished");

    }

    private void compareTricky(int n) {
        String ownPattern = "(a?)[" + n + "," + n + "]a[" + n + "," + n + "]";
        String defaultPattern = "(a?){" + n + "}a{" + n + "}";
        String test = "";
        for (int i = 0; i < n; i++) {
            test += "a";
        }

        System.out.println("n = " + n + ":");
        System.out.println("1st time:");

        long start, delta;
        start = System.nanoTime();
        NFA nfa = nfaGenerator.generateNFA(ownPattern);
        System.out.println("\t" + nfa.accepts(test));
        delta = System.nanoTime() - start;
        System.out.println("\tMy implementation: " + (delta / 1000) + " micro s");

        start = System.nanoTime();
        Pattern pattern = Pattern.compile(defaultPattern);
        Matcher m = pattern.matcher(test);
        System.out.println("\t" + m.matches());
        delta = System.nanoTime() - start;
        System.out.println("\tDefault implementation: " + (delta / 1000) + " micro s");

        System.out.println("2nd time: ");
        start = System.nanoTime();
        System.out.println("\t" + nfa.accepts(test));
        delta = System.nanoTime() - start;
        System.out.println("\tMy implementation: " + (delta / 1000) + " micro s");

        start = System.nanoTime();
        // would seem unfair to call matcher directly, since it knows the test string...
        System.out.println("\t" + pattern.matcher(test).matches());
        delta = System.nanoTime() - start;
        System.out.println("\tDefault implementation: " + (delta / 1000)  + " micro s");
        System.out.println("-------------------------------");
    }

}
