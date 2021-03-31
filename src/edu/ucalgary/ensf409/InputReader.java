package edu.ucalgary.ensf409;

import java.util.Scanner;

public class InputReader {
    public static void main(String[] args) {
        InputReader obj = new InputReader();
    }

    private String[] typeOfFurniture = { "chair", "desk", "filing", "lamp" };
    private String [] typeOfChairs = {"Ergonomic", "Executive", "Kneeling", "Mesh", "Task"};

    public InputReader() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
        do {
            System.out.println("Please make a selection of the furtniture needed.\n" + "Select one from:\n"
                    + "1. Chair\n" + "2. Desk\n" + "3. Filing\n" + "4. Lamp");
            userInput = inputCollected.nextInt();
            if (userInput < 0 || userInput > 4)
                System.out.println("\nError: Please enter a number between 1-4!\n");
        } while (userInput < 0 || userInput > 4);

        String furnitureChosen = this.typeOfFurniture[userInput - 1];

        
        if (userInput == 1)
            displayChairs();
        else if (userInput == 2)
            displayDesks();
        else if (userInput == 3)
            displayFilings();
        else if (userInput == 4)
            displayLamps();

        int userInputForType = inputCollected.nextInt();

        System.out.println();
        inputCollected.close();
    }

    public int selectChairType() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
       
        do{

        System.out.println(
                "You selected chairs as your furniture.\n" + "Please select from the following types of chairs: \n"
                        + "1. Ergonomic\n" + "2. Executive\n" + "3. Kneeling\n" + "4. Mesh\n" + "5. Task\n" + 
                        "6. If you would like to exit the program");

                        userInput = inputCollected.nextInt();

                        if(userInput < 0 || userInput > 6)
                        {
                            System.out.println("Please enter a number between 1 and 6");
                        }
            } while(userInput < 0 || userInput > 6);

            if(userInput == 6)
            {
                inputCollected.close();
                return -1;
            }
           inputCollected.close();
           return userInput;
    }

    public void displayFilings() {

        System.out.println("You selected filing as your furniture.\n"
                + "Please select from the following types of filing:\n" + "1. Small\n" + "2. Medium\n" + "3. Large");

    }

    public int selectDeskType() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
        do {
            System.out.println(
                "You selected desks as your furniture.\n" + "Please select from the following types of chairs:\n"
                        + "1. Standing\n" + "2. Traditional\n" + "3. Adjustable");
            userInput = inputCollected.nextInt();
            if (userInput < 0 || userInput > 4)
            {
                System.out.println("\nError: Please enter a number between 1-3!\n");
            }
        } while (userInput < 0 || userInput > 4);
              
        if(userInput == 4)
        {
            return(-1);
        }
        else if((userInput > 0) && (userInput < 4))
        {
            return(userInput);
        }
        else
        {
            return(-1);
        }
    }

    public int displayLamps() {
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;
        do {
            System.out.println("You selected lamps as your furniture.\n"
                + "Please select from the following types of chairs: \n" + "1. Desk\n" + "2. Swing Arm\n" + "3. Study");
            userInput = inputCollected.nextInt();
            if (userInput < 0 || userInput > 4)
            {
                System.out.println("\nError: Please enter a number between 1-3!\n");
            }
        } while (userInput < 0 || userInput > 4);
                      
        if(userInput == 4)
        {
            return(-1);
        }
        else if((userInput > 0) && (userInput < 4))
        {
            return(userInput);
        }
        else
        {
            return(-1);
        }
    }

}
