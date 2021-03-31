package edu.ucalgary.ensf409;

import java.util.Scanner;

public class InputReader {
    public static void main(String[] args) {
        InputReader obj = new InputReader();
        
    }

    private String[] typeOfFurniture = { "chair", "desk", "filing", "lamp" };

    public InputReader() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
        do {
        System.out.println("Please make a selection of the furtniture needed.\n" +
                            "Select one from:\n" +
                            "1. Chair\n" +
                            "2. Desk\n" +
                            "3. Filing\n" +
                            "4. Lamp");
        userInput = inputCollected.nextInt();
        } while (userInput < 0 || userInput > 4);

        System.out.println(userInput);

    }

    public void displayChairs()
    {
        System.out.println("You selected chairs as your furniture.\n" +
        "Please select from the following types of chairs: \n" + 
        "1. Ergonomic\n" +
        "2. Executive\n" +
        "3. Kneeling\n" +
        "4. Mesh\n" +
        "5. Task");
    }
}
