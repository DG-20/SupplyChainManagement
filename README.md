## Folder Structure

The folder src contains two folders, lib, which contains the jar files required to connect to the database and to run JUnit tests, and edu/ucalgary/ensf409 which is where all the java files are contained. In the edu/ucalgary/ensf409 folder, there are four java files, three of which are the files for the actual algorithm and one test file called TestFile.java.

## Instructions to Run

To run the program in a regular manner (ie not for JUnit tests), please download the src folder. Then, change directories into the src folder in the terminal being used to run and compile the files. Before running commands in the terminal, please be sure to enter the command *SOURCE nameOfPath/databaseUsed.sql* in the MySQL 8.0 Command Line Client. Next, please type in the command: *javac -cp .;lib/mysql-connector-java-8.0.23 edu/ucalgary/ensf409/OrderForm.java* followed by the command *java -cp .;lib/mysql-connector-java-8.0.23 edu/ucalgary/ensf409/OrderForm*.
After these two commands have been ran, the program should run on the command prompt. Please follow the instructions there to proceed with the user-interface.

***Note: Currently, the url, username, and password for the database are set in the code as: URL: jdbc:mysql://localhost/inventory, USERNAME: scm, PASSWORD: ensf409. If you would like to use a different username, password, or URL, please change the lines of code associated with the three in the main method of the OrderForm.java file (lines 37, 38, and 39).**

## Instructions to Run JUnit Test File


