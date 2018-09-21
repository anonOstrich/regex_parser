/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.Scanner; 
import utils.NFAGenerator;

/**
 *
 * @author jesper
 */
public class TextUI {
    private Scanner scanner; 
    private NFAGenerator nfaGenerator; 
    
    public TextUI(Scanner scanner){
        this.scanner = scanner; 
        nfaGenerator = new NFAGenerator(); 
        
    }
    
    public TextUI(){
        this(new Scanner(System.in));
    }
    
    
    public void run(){
        System.out.println("Welcome to Regex parser!\n");
        printInstructions(); 
        System.out.println("");
        
        while(true){
            System.out.print("Enter a regular expression:\n> ");
            String regex = scanner.nextLine(); 
            
            System.out.print("Enter a test string:\n> ");
            String test = scanner.nextLine(); 
            String verb = "does not match"; 
            if(nfaGenerator.generateNFA(regex).accepts(test)){
                verb = "matches";
            } 
            
            System.out.println("Regular expression " + regex + " " + verb + " string " + test);
            System.out.println("\nContinue? (y/n)");
            String input = scanner.nextLine(); 
            
            if(input.toLowerCase().equals("n")){
                break; 
             
            }
            
        }
        
        System.out.println("Goodbye!");
    }
    
    private void printInstructions(){
        System.out.println("Supported characters in both regexes and input strings are a-z, A-Z and 0-9.");
        System.out.println("In regexes you can also use operations (, ), !, *, +, ?, [min,max] , first-last");
        System.out.println("See README.md or documentation for more explanation (might not be provided yet, though :P)");
    }
    
    
    
    
}
