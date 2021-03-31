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
            System.out.println("Please make a selection of the furtniture needed.\n" + "Select one from:\n"
                    + "1. Chair\n" + "2. Desk\n" + "3. Filing\n" + "4. Lamp\n"
                    + "5. If you would like to exit the program.");
            userInput = inputCollected.nextInt();
            if (userInput < 0 || userInput > 5)
                System.out.println("\nError: Please enter a number between 1-4!\n");
        } while (userInput < 0 || userInput > 5);

        if (userInput == 5) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        String furnitureChosen = this.typeOfFurniture[userInput - 1];

        int userInput2 = -1;

        if (userInput == 1)
            displayChairs();
        else if (userInput == 2)
            displayDesks();
        else if (userInput == 3)
            displayFilings();
        else if (userInput == 4)
            displayLamps();

        if (userInput2 == -1) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        System.out.println();
        inputCollected.close();
    }

    public void displayChairs() {
        System.out.println(
                "You selected chairs as your furniture.\n" + "Please select from the following types of chairs: \n"
                        + "1. Ergonomic\n" + "2. Executive\n" + "3. Kneeling\n" + "4. Mesh\n" + "5. Task");
    }

    public void displayFilings() {

        System.out.println("You selected filing as your furniture.\n"
                + "Please select from the following types of filing:\n" + "1. Small\n" + "2. Medium\n" + "3. Large");

    }

    public void displayDesks() {
        System.out.println(
                "You selected desks as your furniture.\n" + "Please select from the following types of chairs:\n"
                        + "1. Standing\n" + "2. Traditional\n" + "3. Adjustable");
    }

    public void displayLamps() {
        System.out.println("You selected lamps as your furniture.\n"
                + "Please select from the following types of chairs: \n" + "1. Desk\n" + "2. Swing Arm\n" + "3. Study");
    }

}
