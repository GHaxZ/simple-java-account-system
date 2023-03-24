import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;

public class Main {
    private HashMap<String, String> userData = new HashMap<>(); // Create a HashMap that will contain username and password combinations
    private Scanner scanner = new Scanner(System.in); // Initializes the Scanner for the User Input

    public static void main(String[] args){
        Main main = new Main(); // Creates new Main instance
        Path path = Paths.get("data.csv"); // Gets the path for the save file
        main.createFile(path);
        main.readData(path);
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
                            dashboard(path, username, password);
                        } else {
                            System.out.println("Username or password is wrong.");
                        }
                        break;
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

    public void readData(Path path){ // Reads file content and writes it into the HashMap
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

    public int determinAccountIndex(Path path, String username, String password){ // Determines the index of the logged in account
        try{
            List<String> fileContent = Files.readAllLines(path);
            int index = 0;
            for(String line : fileContent){
                String[] userCredentials = line.split(",");

                if(userCredentials[0] == username && userCredentials[1] == password){
                    return index;
                }

                index++;
            }
        } catch(IOException e){}
        return 0;
    }

    public void dashboard(Path path, String username, String password){ // Dashboard that opens when user successfully logs into an account
        boolean inputValid = false;

        while(!inputValid){
            System.out.println("Enter your operation (logout, delete): ");
            String input = stringInput();
            switch(input.toLowerCase()){
                case "logout":
                    inputValid = true;
                    break;
                case "delete":
                    if(deleteAccount(path, username, password)){
                        inputValid = true;
                    }
                    break;
                default:
                    System.out.println(input + " is not a valid operation.");
            }
        }
    }

    public boolean deleteAccount(Path path, String username, String password){ // Deletes the account of the user
        int index = determinAccountIndex(path, username, password);
        boolean deletedAccount = false;

        boolean inputValid = false;
        while(!inputValid){
            System.out.println("Are you sure you want to delete your account? (yes/no): ");
            String input = stringInput();
            switch(input.toLowerCase()){
                case "yes":
                    try{
                        List<String> fileContent = Files.readAllLines(path);

                        Files.writeString(path, "", StandardOpenOption.TRUNCATE_EXISTING);

                        for(int x = 0; x < fileContent.size(); x++){
                            String[] userCredentials = fileContent.get(x).split(",");
                            if(x != index){
                                register(path, userCredentials[0], userCredentials[1]);
                            }
                        }
                    } catch(IOException e){}
                    deletedAccount = true;
                    inputValid = true;
                    break;
                case "no":
                    inputValid = true;
                    break;
                default:
                    System.out.println(input + " is not a valid answer.");
            }
        }
        return deletedAccount;
    }

    public String stringInput(){ // String input handler
        return scanner.nextLine();
    }
}