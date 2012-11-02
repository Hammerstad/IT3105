/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.Scanner;

/**
 *
 * @author Nicklas
 */
public class TextUI implements IUserInterface {
    private Scanner in;
    
    
    public TextUI() {
        this.in = new Scanner(System.in);
    }

    @Override
    public String requestString(String question) {
        System.out.println(question);
        return in.next();
    }

    @Override
    public int requestInt(String question) {
        System.out.println(question);
        return in.nextInt();
    }

    @Override
    public double requestDouble(String question) {
        System.out.println(question);
        return in.nextDouble();
    }
    
}
