package ui;

import domain.NFA;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.generators.NFAGenerator;

/**
 * Responsible for communicating with the user and providing services based
 * on regular expressions.
 * 
 */
public class TextUI {

    /**
     * Used solely to query the user for input. 
     */
    private Scanner scanner;
    /**
     * Generator for all automata that are used to work with regular expressions.
     * 
     * <p>Only one instance in the course of the program to best utilize 
     * caching.</p>
     * 
     */
    private NFAGenerator nfaGenerator;

    /**
     * For easing testing, not usually needed.
     * 
     * @param scanner 
     */
    protected TextUI(Scanner scanner) {
        this.scanner = scanner;
        nfaGenerator = new NFAGenerator();

    }

    /**
     * Initializes the tools to read user input
     * 
     * <p>Usually a reader can be obtained from the console, but there is no
     * console when executing code in some development environments. If the 
     * former fails, an alternative input is used for the scanner.</p>
     */
    public TextUI() {
        // works with scands
        try {
            this.scanner = new Scanner(System.console().reader());
        } catch (Exception e) {
            this.scanner = new Scanner(System.in, "ISO-8859-1");
        }
        nfaGenerator = new NFAGenerator();
    }

    
    /**
     * Queries the user for preferred task and passes control to the right
     * method. 
     */
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
    }

    /**
     * Queries the user for regex patterns and test strings. 
     * 
     * <p>Has two loops: the outer to decide if the user wants to enter a 
     * new regular expression; the inner to ask for test strings to match 
     * against the given regular expression.</p>
     * 
     */
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

    /**
     * Prints guidance to use the system and to properly construct 
     * regular expressions.
     * 
     */
    protected void printInstructions() {
        System.out.println("See README.md or documentation for info on supported operations and symbols)");
    }

    /** 
     * 
     * Prints available operation numbers and their descriptions.
     * 
     * @return Integer indicating the preferred operation.
     */
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

    /**
     * Queries the user for how many demanding tests are run. 
     * 
     * <p>At around n = 100 won't take much time, at n = 200 will take over 5 
     * minutes.</p>
     * 
     */
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

    /**
     * 
     * Prints performance information of matching a complex-ish regex with a 
     * successful test string. 
     * 
     * <p>First constructs the correct strings for my implementation and Java
     * default implementation. Then the mutual test string is matched against
     * the regular expressions constructed by both methods. The elapsed times
     * are printed.</p>
     * 
     * <p>The test is run the second time. This time caching is utilised, 
     * since the same regular expression has been already used once. The two
     * elapsed times are again printed to the user output.</p>
     * 
     * @param n How long regular expression will be constructed and matched.
     */
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

    /**
     * Queries a regex and searches a text file for portions that match it.
     * 
     * <p>Does not try to match the whole text file's contents with the 
     * given regular expression. It wraps the regex whith .* in the beginning
     * and beginning, so the contents match the expression if any part of the 
     * contents matches the regular expression. Only prints whether there is 
     * a mathching part. </p>
     */
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

    /**
     * 
     * Queries user for file name and tries to read that file into a string.
     * 
     * <p>The file must be in the src/main/resources/ folder for the program
     * to find it. If running the .jar only the files packed into it can 
     * be chosen.</p>
     * <p>If left blank, the choice defaults to the novel Frankenstein. If there
     * is a problem with reading from the specified file, the user is prompted 
     * again for input for as long as it takes to correctly read a text file.</p>
     * 
     * @return An array of two strings: the name of the file and the contents 
     * of the file, respectively.
     */
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
