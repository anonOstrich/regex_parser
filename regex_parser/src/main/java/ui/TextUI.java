/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.Scanner; 

/**
 *
 * @author jesper
 */
public class TextUI {
    private Scanner scanner; 
    
    public TextUI(Scanner scanner){
        this.scanner = scanner; 
    }
    
    public TextUI(){
        this(new Scanner(System.in));
    }
    
    
    public void run(){
        System.out.println("Welcome to Regex parser!");
        
        String input = "something"; 
        while(!input.isEmpty()){
            System.out.println("Program is running: press enter to quit.");
            System.out.print("> ");
            input = scanner.nextLine(); 
            System.out.println(input);
        }
        
        System.out.println("Goodbye!");
    }
    
    private void printInstructions(){
    
    }
}
