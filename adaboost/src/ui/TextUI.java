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
        return in.nextLine();
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

    @Override
    public boolean requestBoolean(String question) {
        System.out.println(question);
        return in.nextBoolean();
    }

    @Override
    public int requestChoice(String title, Object[] o) {
        return requestChoice(title, o, new Object[]{});
    }

    @Override
    public int requestChoice(String title, Object[] o, Object[] skip) {
        System.out.println(title);
        int i = 1;
        int choice = Integer.MIN_VALUE;
        do {
            for (Object obj : o) {
                boolean sskip = false;
                for (Object sobj : skip) {
                    if (obj.equals(sobj)){
                        sskip=true;
                    }
                }
                if (sskip) {
                    continue;
                }
                System.out.println((i++) + ") " + obj.toString());
            }
            System.out.println("Please select one of the above...");
            choice = in.nextInt();
            if (choice == -1) {
                break;
            }
        } while (choice < 1 || choice > o.length);
        return choice;
    }
}
