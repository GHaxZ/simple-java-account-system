import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;

public class Main {
    private HashMap<String, String> userData = new HashMap<>(); // Create a HashMap that contains username and password combinations
    private Scanner scanner = new Scanner(System.in); // Initializes the Scanner for the User Input

    public static void main(String[] args){
        Main main = new Main(); // Creates new Main instance
        Path path = Paths.get("data.csv"); // Gets the path for the save file
        main.createFile(path);
        main.readFile(path);
        main.mainConsole(path);
    }

    public Main(){} // Default Constructor

    public void createFile(Path path){ // Creates file if not created already
        try {
            Files.createFile(path);
        } catch(IOException e){}
    }

    public void mainConsole(Path path){ // Main input console where all the inputs are made
        while(true){
            System.out.print("Enter your operation (login/register): ");
            String input = stringInput();
            switch(input.toLowerCase()){
                case "login":
                    while(true){
                        System.out.print("Enter your Username: ");
                        String username = stringInput();
                        System.out.print("Enter your password: ");
                        String password = stringInput();

                        if(login(username, password)){
                            System.out.println("Successful login as " + username);
                            break;
                        } else {
                            System.out.println("Username or password is wrong.");
                        }
                    }
                    break;
                case "register":
                    System.out.print("Enter your new Username: ");
                    String newUsername = stringInput();
                    System.out.print("Enter your new password: ");
                    String newPassword = stringInput();
                    register(path, newUsername, newPassword);
                    break;
                default:
                    System.out.println(input + " is not a valid option.");
            }
        }
    }

    public void readFile(Path path){ // Reads file content and writes it into the HashMap
        try {
            List<String> fileContent = Files.readAllLines(path);
            for(String line : fileContent){
                String[] credentials = line.split(",");
                userData.put(credentials[0], credentials[1]);
            }
        } catch(IOException e){}
    }

    public void register(Path path, String username, String password){ // Adds account data to text file
        try{
            if(Files.size(path) == 0){
                Files.writeString(path, username + "," + password, StandardOpenOption.APPEND);
            } else {
                Files.writeString(path,   "\n" + username + "," + password, StandardOpenOption.APPEND);
            }

            userData.put(username, password);

        } catch(IOException e){}
    }

    public boolean login(String username, String password){ // Checks if the username and password combination is valid
        if(userData.containsKey(username) && userData.get(username).equals(password)){
            return true;
        }
        return false;
    }

    public String stringInput(){ // String input handler
        return scanner.nextLine();
    }
}