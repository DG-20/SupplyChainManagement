package edu.ucalgary.ensf409;

import java.util.Scanner;

public class InputReader {
    public static void main(String[] args) {
        InputReader obj = new InputReader();
    }

    private String[] typeOfFurniture = { "chair", "desk", "filing", "lamp" };
    private String[] typeOfFiling = { "Small", "Medium", "Large" };
    private String[] typeOfLamps = { "Desk", "Study", "Swing Arm" };
    private String[] typeOfDesks = { "Standing", "Traditional", "Adjustable" };
    private String[] typeOfChairs = { "Ergonomic", "Executive", "Kneeling", "Mesh", "Task" };

    private String furnitureChosen;
    private String typeChosen;
    private int quantity;

    public InputReader() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
        do {
            System.out
                    .println("Please make a selection of the furtniture needed.\n" + "Select one from:\n" + "1. Chair\n"
                            + "2. Desk\n" + "3. Filing\n" + "4. Lamp\n" + "5. If you would like to exit the program.");
            userInput = inputCollected.nextInt();
            if (userInput < 0 || userInput > 5)
                System.out.println("\nError: Please enter a number between 1-4!\n");
        } while (userInput < 0 || userInput > 5);

        if (userInput == 5) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        this.furnitureChosen = this.typeOfFurniture[userInput - 1];

        int userInput2 = 0;

        if (userInput == 1) {
            userInput2 = selectChairType();
            if (userInput2 != -1)
                this.typeChosen = this.typeOfChairs[userInput2 - 1];
        } else if (userInput == 2) {
            userInput2 = selectDeskType();
            if (userInput2 != -1)
                this.typeChosen = this.typeOfDesks[userInput2 - 1];
        } else if (userInput == 3) {
            userInput2 = selectFilingType();
            if (userInput2 != -1)
                this.typeChosen = this.typeOfFiling[userInput2 - 1];
        } else if (userInput == 4) {
            userInput2 = selectLampType();
            if (userInput2 != -1)
                this.typeChosen = this.typeOfLamps[userInput2 - 1];
        }

        if (userInput2 == -1) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        int userInput333 = 0;
        do {
            System.out.println("\nWhat quantity of this product do you need? Please enter a positive integer\n"
                    + "Enter -1 to exit this program.");
            userInput333 = inputCollected.nextInt();
            if ((userInput333 < 1) && (userInput333 != -1))
            {
                System.out.println("\nPlease enter a positive integer!\n");
            }
        } while ((userInput333 < 1) && (userInput333 != -1));

        if (userInput333 == -1) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        this.quantity = userInput333;

        System.out.println();
        inputCollected.close();
    }

    public int selectChairType() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;

        do {

            System.out.println("\nYou selected chairs as your furniture.\n"
                    + "Please select from the following types of chairs: \n" + "1. Ergonomic\n" + "2. Executive\n"
                    + "3. Kneeling\n" + "4. Mesh\n" + "5. Task\n" + "6. If you would like to exit the program.");

            userInput = inputCollected.nextInt();

            if (userInput < 0 || userInput > 6) {
                System.out.println("\nPlease enter a number between 1 and 6!\n");
            }
        } while (userInput < 0 || userInput > 6);

        if (userInput == 6) {
            return -1;
        }
        return userInput;
    }

    public int selectFilingType() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;

        do {
            System.out.println("\nYou selected filing as your furniture.\n"
                    + "Please select from the following types of filing: \n" + "1. Small\n" + "2. Medium\n"
                    + "3. Large\n" + "4. If you would like to exit the program");

            userInput = inputCollected.nextInt();

            if (userInput < 0 || userInput > 4) {
                System.out.println("\nPlease enter a number between 1 and 4!\n");
            }
        } while (userInput < 0 || userInput > 4);

        if (userInput == 4) {
            return -1;
        }
        return userInput;
    }

    public int selectDeskType() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
        do {
            System.out.println("\nYou selected desks as your furniture.\n"
                    + "Please select from the following types of chairs:\n" + "1. Standing\n" + "2. Traditional\n"
                    + "3. Adjustable\n" + "4. If you would like to exit the program.");
            userInput = inputCollected.nextInt();
            if (userInput < 0 || userInput > 4) {
                System.out.println("\nError: Please enter a number between 1-3!\n");
            }
        } while (userInput < 0 || userInput > 4);

        if (userInput == 4) {
            return (-1);
        }
        return (userInput);
    }

    public int selectLampType() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
        do {
            System.out.println("\nYou selected lamps as your furniture.\n"
                    + "Please select from the following types of chairs: \n" + "1. Desk\n" + "2. Swing Arm\n"
                    + "3. Study\n" + "4. If you would like to exit the program.");
            userInput = inputCollected.nextInt();
            if (userInput < 0 || userInput > 4) {
                System.out.println("\nError: Please enter a number between 1-3!\n");
            }
        } while (userInput < 0 || userInput > 4);

        if (userInput == 4) {
            return (-1);
        }
        return (userInput);
    }

}
