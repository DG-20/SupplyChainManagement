package edu.ucalgary.ensf409;

public class InputReader {
    public static void main(String[] args) {
        InputReader obj = new InputReader();

        displayChairs();
        
    }

    public static void displayChairs()
    {
        System.out.println("You selected chairs as your furniture.\n" +
        "Please select from the following types of chairs: \n" + 
        "1. Ergonomic \n" +
        "2. Executive \n" +
        "3. Kneeling \n" +
        "4. Mesh \n" +
        "5. Task \n");
    }
}
