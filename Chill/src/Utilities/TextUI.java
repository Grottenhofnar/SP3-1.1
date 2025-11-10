package Utilities;

import java.util.ArrayList;
import java.util.Scanner;

public class TextUI {

    private static Scanner sc = new Scanner(System.in);

    /* Let the user choose a number of integers bound by limit  */
    public ArrayList<String> promptChoice(ArrayList<String> options, int limit, String msg) {
        displayMsg(msg);
        displayList(options, "");
        ArrayList<String> choices = new ArrayList<>();

        while (choices.size() < limit) {
            int choice = promptNumeric(msg);
            choices.add(options.get(choice - 1));
        }
        return choices;
    }

    /* Shows some lines of text and prefixes a line number */
    public void displayList(ArrayList<String> list, String msg) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + ". " + list.get(i));
        }
    }

    /* A wrapper for String.out.println */
    public void displayMsg(String msg) {
        System.out.println(msg);
    }

    /* Prints a message and fetches a number from the console */
    public int promptNumeric(String msg) {
        displayMsg(msg);
        String input = sc.nextLine();
        int numInput = Integer.parseInt(input); //Konverterer svaret til et tal TODO: Exception handling
        return numInput;
    }

    /* Prints a message and fetches a line of text from the console */
    public String promptText(String msg) {
        displayMsg(msg);
        String input = sc.nextLine();
        return input;
    }

    public int getIntOrZero(String s){
        if (s.trim().length() > 0){
            return Integer.parseInt(s);
        }
        return 0;
    }
}
