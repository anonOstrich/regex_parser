package ui;

import domain.NFA;
import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.generators.NFAGenerator;

public class TextUI {

    private Scanner scanner;
    private NFAGenerator nfaGenerator;

    public TextUI(Scanner scanner) {
        this.scanner = scanner;
        nfaGenerator = new NFAGenerator();

    }

    public TextUI() {
        // works with scands
        try {
            this.scanner = new Scanner(System.console().reader());
        } catch (Exception e) {
            this.scanner = new Scanner(System.in, "ISO-8859-1");
        }
        nfaGenerator = new NFAGenerator();
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
        } else if (choice == 3) {
            searchLongText();
        }

        System.out.println("Goodbye!");
        return; 
    }

    public void matchExpressionsAndStrings() {
        long start, generated, end;

        while (true) {
            System.out.print("Enter a regular expression:\n> ");
            String regex = scanner.nextLine();

            while (true) {
                System.out.print("Enter a test string: (press enter twice to change the regex)\n> ");
                String test = scanner.nextLine();
                if (test.isEmpty()) {
                    System.out.print(">");
                    if (scanner.nextLine().isEmpty()) {
                        break;
                    }
                }
                String verb = "does not match";
                start = System.nanoTime();
                NFA nfa = nfaGenerator.generateNFA(regex);
                generated = System.nanoTime();
                boolean accepts = nfa.accepts(test);
                end = System.nanoTime();
                if (accepts) {
                    verb = "matches";
                }

                System.out.println("Regular expression '" + regex + "' " + verb + " string '" + test + "'.");
                System.out.println("Generation took " + ((generated - start) / 1000000) + " ms, and matching " + ((end - generated) / 1000000) + " ms.");
            }

            System.out.println("\nEnter new regular expression? (y/n)");
            String input = scanner.nextLine();

            if (input.toLowerCase().equals("n")) {

                break;

            }

        }
        run();
    }

    protected void printInstructions() {
        System.out.println("See README.md or documentation for info on supported operations and symbols)");
    }

    protected int chooseOperation() {
        int choice;
        while (true) {
            System.out.println("Choose by entering the corresponding number: ");
            System.out.println("1: Test performance with possibly tricky expressions");
            System.out.println("2: Match expressions and strings of your choice");
            System.out.println("3: Search text file for phrases");
            System.out.println("4: Exit");
            System.out.print("> ");
            String input = scanner.nextLine();

            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 4) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (Exception e) {
                System.out.println("Input must be an integer between 1-4");
            }
        }

        return choice;
    }

    protected void testTrickyPerformance() {
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

    protected void compareTricky(int n) {
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
        nfa.accepts(test);
        delta = System.nanoTime() - start;
        System.out.println("\tMy implementation: " + (delta / 1000) + " micro s");

        start = System.nanoTime();
        Pattern pattern = Pattern.compile(defaultPattern);
        Matcher m = pattern.matcher(test);
        m.matches();
        delta = System.nanoTime() - start;
        System.out.println("\tDefault implementation: " + (delta / 1000) + " micro s");

        System.out.println("2nd time: ");
        start = System.nanoTime();
        nfa.accepts(test);
        delta = System.nanoTime() - start;
        System.out.println("\tMy implementation: " + (delta / 1000) + " micro s");

        start = System.nanoTime();
        // would seem unfair to call matcher directly, since it knows the test string...
        pattern.matcher(test).matches();
        delta = System.nanoTime() - start;
        System.out.println("\tDefault implementation: " + (delta / 1000) + " micro s");
        System.out.println("-------------------------------");
    }

    protected void searchLongText() {

        String[] fileInfo = buildStringFromUserSelectedFile();
        String filename = fileInfo[0];
        String text = fileInfo[1];

        while (true) {
            System.out.println("Give a search phrase (regular expression that will be searched for):");
            String input = scanner.nextLine();
            NFA nfa = nfaGenerator.generateNFA(".*(" + input + ").*");
            long start = System.nanoTime();
            boolean found = nfa.accepts(text);
            long delta = System.nanoTime() - start;
            String contain = found ? "contains" : "does not contain";
            System.out.println(filename + " " + contain
                    + " a part that matches the given regular expression '" + input + "'.");
            System.out.println("Testing after generating a suitable automaton took " + delta / 1000000 + " milliseconds.");
            System.out.println("Try a new regular expression? (y/n)");
            System.out.print(" >");
            input = scanner.nextLine();
            if (input.equals("n")) {
                break;
            }

        }
        run();

    }

    protected String[] buildStringFromUserSelectedFile() {
        String filename;
        ClassLoader cl = getClass().getClassLoader();
        while (true) {
            System.out.print("Give the name of the file or leave empty for default file (Frankenstein)\n"
                    + ">");
            filename = scanner.nextLine();

            if (filename.isEmpty()) {
                filename = "frankenstein.txt";
            }

            String text = "";
            try (Scanner s = new Scanner(cl.getResourceAsStream(filename), "UTF-8")) {
                System.out.println("Reading...");
                while (s.hasNext()) {
                    text += s.nextLine();
                }
                text = filename + "\n" + text;
                System.out.println("Done!");
                String[] info = new String[]{filename, text};
                return info;
            } catch (Exception e) {
                System.out.println("File resources/" + filename + " does not exist or some other error occured.");
            }
        }
    }

}
