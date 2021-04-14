Folder Structure
----------------
The folder src contains two folders, lib, which contains the jar files required to connect to the database and to run JUnit tests, and edu/ucalgary/ensf409 which is where all 
the java files are contained. In the edu/ucalgary/ensf409 folder, there are four java files, three of which are the files for the actual algorithm and one test file called 
TestFile.java.

Instructions to Run (Not JUnit testing)
---------------------------------------
To run the program in a regular manner (ie not for JUnit tests), please download the src folder. Then, change directories into the src folder in the terminal being used to run
and compile the files. Before running commands in the terminal, please be sure to enter the command "SOURCE nameOfPath/databaseUsed.sql" in the MySQL 8.0 Command Line Client. 
Next, please type in the command: "javac -cp .;lib/mysql-connector-java-8.0.23 edu/ucalgary/ensf409/OrderForm.java" followed by the command "java -cp .;lib/mysql-connector-java-8.0.23 edu/ucalgary/ensf409/OrderForm".

After these two commands have been ran, the program should run on the command prompt.

Upon running this program, the user first needs to make a selection of the furniture they would like to order from a displayed list. The user must enter an integer value that 
corresponds to the specific item which is displayed in the list. Once completed, the user must once again enter an integer value corresponding to an item in a new displayed 
list. This second selection is for the type of the furniture they previously selected. After the user makes selections for the furniture and type they would like to order, the 
user will then be asked to enter a positive integer to indicate the quantity of product they would like. When each question is prompted, the user must enter a valid integer 
value. Failure to do so will result in the question being asked again and again until a valid integer is entered. There are also integer values provided by each question that 
allow the user to exit the program if necessary (these exit integers change each selection as indicated in each list). Successfully inputting values for the furniture, type, and 
quantity a user would like to request will result in a text file called “orderform.txt” being created. This text file would be located in the source folder (src) within the 
directory (so the src folder now contains three items, two folders named lib and edu, and the newly created txt file). If the placed order is able to be fulfilled by the current 
inventory in the database, the orderform text file will contain the original request (the furniture, type, and quantity requested) along with the ID codes of the items being 
ordered and the total price of the order. Any requests not able to be processed will be indicated in the orderForm text file as well along with the reommended list of 
manufacturers being printed on the user's terminal. If the program cannot connect to the database, a message will be provided to the user once again on the terminal.

Implementation of Code
----------------------
There are three java files utilized in this project. The first java file is called InputReader, which scans the inputs provided by the user using a Scanner object. Once the 
input values are scanned they are checked to ensure validity (ie if they are a valid integer or not) and stored if they are valid, otherwise the user is re-prompted. The second 
java file is called DataBaseManipulator which extends the InputReader class. In DataBaseManipulator, algorithmic methods for each category of furniture are utlilized based on 
the inputs stored in InputReader. The algorithms then provide the program with the cheapest option that fulfills the request and also ensures that all parts are included with 
the order. If a request is able to be fullfiled, the ID codes from the tables of the requested furniture in the Database are stored in member variables as well as the cheapest 
price, and it creates a text file called "orderform.txt" which writes the output, including the original order information, the lowest price possible, and the IDs associated 
with the lowest price. If a request is unable to be fulfilled, DataBaseManipulator creates an "orderform" text file that states the order could not be placed. The third java 
file is called OrderForm, which extends the DataBaseManipulator class. In the OrderForm java file the cheapest price and ID codes stored in the member variables of 
DataBaseManipulator are used to generate the "orderform" text file created in the OrderForm java file.


*Note: Currently, the url, username, and password for the database are set in the code as: URL: jdbc:mysql://localhost/inventory, USERNAME: scm, PASSWORD: ensf409. If you would 
like to use a different username, password, or URL, please change the lines of code associated with the three in the main method of the OrderForm.java file (lines 38, 39, and 40).

Instructions to Run JUnit Test File
-----------------------------------
To run the JUnit test files from the command line, refresh the database, using the command SOURCE pathToInventory.sql in the MySQL Command Line Client. Then, change directories 
to the src folder. Then, type in your command line: javac -cp .;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/ensf409/OrderForm.java to compile the code initially, then, 
typing in: 

"javac -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/system-rules-1.19.0.jar;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/ensf409/InventoryTest.java"

to compile the InventoryTest.java file, and finally typing in: 

"java -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/system-rules-1.19.0.jar;lib/mysql-connector-java-8.0.23.jar org.junit.runner.JUnitCore edu.ucalgary.ensf409.InventoryTest"

This should cause all tests to run in sequential order as the way we have organized the tests are so that they work if the tests are ran in alphabetical order.

Creators: Curtis Silva, Maheen Hossain, Divyansh Goyal, Liam Parmar.
