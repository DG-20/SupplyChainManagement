/**
 * @author Divyansh Goyal <a href="mailto:divyansh.goyal@ucalgary.ca">divyansh.goyal@ucalgary.ca</a>
 * 
 * @author Maheen Hossain <a href="mailto:maheen.hossain@ucalgary.ca">maheen.hossain@ucalgary.ca</a>
 * 
 * @author Liam Parmar <a href="mailto:liam.parmar@ucalgary.ca">liam.parmar@ucalgary.ca</a>
 * 
 * @author Curtis Silva <a href="mailto:curtis.silva@ucalgary.ca">curtis.silva@ucalgary.ca</a>
 * 
 * @version 62.0
 * 
 * @since 1.0
 */
package edu.ucalgary.ensf409;

/*
 * Importing the libraries necessary to read in from the terminal to obtain user choices.
*/
import java.io.IOException;
import java.util.Scanner;

/*
 * InputReader is a class which is the input part for this program. This is where
 * the program takes in inputs from the user to get the details of the 
 * order. It utilizes a Scanner, which reads in from the keyboard, 
 * to store the choices into three protected member fields, furnitureChosen,
 * typeChosen, and quantity. InputReader is a parent class of DataBaseManipulator
 * and thus allows DataBaseManipulator to access its protected field members.
 */
public class InputReader {
    
    // A main is included so as to not cause an error when compiling.
    // Everything will be done from the OrderForm class's main.
    public static void main(String[] args) throws IOException {
    }

    /*
     * Five String arrays to store the various types of each furniture. This is done
     * so that when the user inputs a choice, in the form of an int, the int can be
     * used as the index to set the member variables to the Strings in the same
     * exact way as they appear in the database.
     */
    private String[] typeOfFurniture = { "chair", "desk", "filing", "lamp" };
    private String[] typeOfFiling = { "Small", "Medium", "Large" };
    private String[] typeOfLamps = { "Desk", "Study", "Swing Arm" };
    private String[] typeOfDesks = { "Adjustable", "Standing", "Traditional" };
    private String[] typeOfChairs = { "Ergonomic", "Executive", "Kneeling", "Mesh", "Task" };

    /*
     * Three protected member fields. These fields will later be accessed by the
     * other classes which inherit InputReader and thus the visibility must be set
     * to protected. 
     * furnitureChosen stores the category of furniture chosen by the
     * user.
     * typeChosen stores the type of the furniture chosen by the user.
     * quantity stores the quantity that the user orders for.
     */
    protected String furnitureChosen;
    protected String typeChosen;
    protected int quantity;

    /*
     * The constructor is where everything takes place. This is where
     * the user is asked to make selections based on the options displayed,
     * one at a time, and their responses stored.
     */
    public InputReader() {
        // Creating a new Scanner object to read in from the user's keyboard
        // when prompted for an input.
        Scanner inputCollected = new Scanner(System.in);
        int userInput = -1;

        // This do-while loop keeps prompting the user with 5 options, to choose
        // the type of furniture along with an option to terminate the program. 
        // If the user enters a number which is not one of the options, the user
        // is re-prompted over and over until they enter a valid integer.
        do {
            System.out.println(
                    "\nPlease make a selection of the furniture needed.\n" + "Select one from:\n" + "1. Chair\n"
                            + "2. Desk\n" + "3. Filing\n" + "4. Lamp\n" + "5. If you would like to exit the program.");
            userInput = inputCollected.nextInt();
            if (userInput < 1 || userInput > 5)
                System.out.println("\nError: Please enter a number between 1-4!\n");
        } while (userInput < 1 || userInput > 5);

        // This is a conditional which checks the condition of whether the user inputted "5", which means
        // the user wants to terminate the program. If this is true, print a message and terminate the program.
        if (userInput == 5) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        // Using the user input as an index to the typeOfFurniture array and storing into the
        // member field for later use.
        this.furnitureChosen = this.typeOfFurniture[userInput - 1];

        int userInput2 = 0;

        // Based on the user's first input, this series of if/else-if called different methods
        // which print and take in the user input for the corresponding furniture chosen.
        // The methods return -1 if the user has chosen to terminate and exit the program.
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

        // If the method corresponding to the user's choice returned -1, the user wants to 
        // exit the program. Do so after printing a message.
        if (userInput2 == -1) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        // This do-while loop keeps prompting the user to enter a valid quantity requested.
        // If the user enters a quantity less than 1, except for -1, the program will re-prompt and
        // keep doing so until the user enters a valid int. If the user enters a -1, the program will
        // terminate and print a message, which is also specified in the print statement below.
        int userInput333 = 0;
        do {
            System.out.println("\nWhat quantity of this product do you need? Please enter a positive integer\n"
                    + "Enter -1 to exit this program.");
            userInput333 = inputCollected.nextInt();
            if ((userInput333 < 1) && (userInput333 != -1)) {
                System.out.println("\nPlease enter a positive integer!\n");
            }
        } while ((userInput333 < 1) && (userInput333 != -1));

        // If the user enters -1, the program terminates after printing an error message.
        if (userInput333 == -1) {
            System.out.println("\nYou have successfully exited the program, to run again, compile and run!\n");
            System.exit(1);
        }

        // The quantity entered is stored into the protected member field to be used later.
        this.quantity = userInput333;

        // Printing the user's order.
        System.out.println();
        System.out.println("Your placed order is: \n" + "Quantity: " + this.quantity 
        + "\nType: " + this.typeChosen + "\nFurniture: " + this.furnitureChosen + "\n");
    }

    /*
     * The following are methods which are conditionally called in the constructor as described above.
     * These methods print the choices for the user based on the user's first choice.
     * They then take in the user's choice, and if the choice entered is not one of the presented
     * options, they keep re-prompting the user until the user inputs an acceptable int.
     * If the user has selected to terminate and exit the program, the method returns a -1 which
     * is dealt with in the constructor above.
     */
    
    private int selectChairType() {
        Scanner inputCollected2 = new Scanner(System.in);
        int userInput = -1;

        do {
            System.out.println("\nYou selected chairs as your furniture.\n"
                    + "Please select from the following types of chairs: \n" + "1. Ergonomic\n" + "2. Executive\n"
                    + "3. Kneeling\n" + "4. Mesh\n" + "5. Task\n" + "6. If you would like to exit the program.");

            userInput = inputCollected2.nextInt();
            if (userInput < 0 || userInput > 6) {
                System.out.println("\nPlease enter a number between 1 and 6!\n");
            }
        } while (userInput < 0 || userInput > 6);

        if (userInput == 6) {
            return -1;
        }
        
        return userInput;
    }

    private int selectFilingType() {
        Scanner inputCollected3 = new Scanner(System.in);
        int userInput = -1;

        do {
            System.out.println("\nYou selected filing as your furniture.\n"
                    + "Please select from the following types of filing: \n" + "1. Small\n" + "2. Medium\n"
                    + "3. Large\n" + "4. If you would like to exit the program");

            userInput = inputCollected3.nextInt();
            if (userInput < 0 || userInput > 4) {
                System.out.println("\nPlease enter a number between 1 and 4!\n");
            }
        } while (userInput < 0 || userInput > 4);

        if (userInput == 4) {
            return -1;
        }

        return userInput;
    }

    private int selectDeskType() {
        Scanner inputCollected4 = new Scanner(System.in);
        int userInput = -1;

        do {
            System.out.println("\nYou selected desks as your furniture.\n"
                    + "Please select from the following types of chairs:\n" + "1. Adjustable\n" + "2. Standing\n"
                    + "3. Traditional\n" + "4. If you would like to exit the program.");

            userInput = inputCollected4.nextInt();
            if (userInput < 0 || userInput > 4) {
                System.out.println("\nError: Please enter a number between 1-3!\n");
            }
        } while (userInput < 0 || userInput > 4);

        if (userInput == 4) {
            return (-1);
        }

        return (userInput);
    }

    private int selectLampType() {
        Scanner inputCollected5 = new Scanner(System.in);
        int userInput = -1;

        do {
            System.out.println("\nYou selected lamps as your furniture.\n"
                    + "Please select from the following types of chairs: \n" + "1. Desk\n" + "2. Study\n"
                    + "3. Swing Arm\n" + "4. If you would like to exit the program.");
            userInput = inputCollected5.nextInt();
            if (userInput < 0 || userInput > 4) {
                System.out.println("\nError: Please enter a number between 1-3!\n");
            }
        } while (userInput < 0 || userInput > 4);

        if (userInput == 4) {
            return (-1);
        }
        
        return (userInput);
    }

    /*
     * Constructor used when testing to take in values and set them.
     */
    public InputReader(String furniture, String type, int size)
    {
        boolean validArgsChecker = isValid(furniture, type, size);
        if(validArgsChecker == true)
        {
            this.furnitureChosen = furniture;
            this.typeChosen = type;
            this.quantity = size;
        }
        else
        {
            System.out.println("The inputs given to constructor InputReader are invalid");
            System.exit(1);
        }
    }

    protected static boolean isValid(String furniture, String type, int size)
    {
        furniture = furniture.strip();
        type = type.strip();
        furniture = furniture.toLowerCase();
        type = type.toLowerCase();
        if(size < 1)
        {
            return(false);
        }
        if(furniture.equals("desk"))
        {
            if((type.equals("adjustable") == false) && (type.equals("standing") == false) 
                && (type.equals("traditional") == false))
            {
                return(false);
            }  
        }
        else if(furniture.equals("chair"))
        {
            if((type.equals("ergonomic") == false) && (type.equals("executive") == false) 
            && (type.equals("kneeling") == false) && (type.equals("mesh") == false) && (type.equals("task") == false))
            {
                return(false);
            }  
        }
        else if(furniture.equals("lamp"))
        {
            if((type.equals("desk") == false) && (type.equals("study") == false) 
            && (type.equals("swing arm") == false))
            {
                return(false);
            }  
        }
        else if(furniture.equals("filing"))
        {
            if((type.equals("small") == false) && (type.equals("medium") == false) 
            && (type.equals("large") == false))
            {
                return(false);
            }  
        }
        else
        {
            
            return(false);
        }

       
        return(true);
    }
}
