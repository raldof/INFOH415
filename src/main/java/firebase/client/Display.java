package firebase.client;
import firebase.FirebaseConnection;
import firebase.Frame;
import firebase.object.User;
import java.util.Scanner;

public class Display {
    FirebaseConnection firebaseConnection = new FirebaseConnection();

    public void connexion(){
        System.out.println("Connexion");
        Scanner userInput = new Scanner(System.in);
        System.out.println("Username:");
        String username = userInput.next();
        System.out.println("Password:");
        String password = userInput.next();


        if (auth(username, password) == 2){ // case where user does not exist
            User user = new User(username, password);
            accessToApp(user, userInput);
        }
    }

    public void accessToApp(User user, Scanner userInput){
        System.out.println("Access to the app ? [Y/N]");
        String answer = userInput.next();

        while (!(answer.equals("Y") || answer.equals("N"))){
            System.out.println("Access to the app ?[Y/N]");
            answer = userInput.next();
        }

        if (answer.equals("Y")){
            createSession(user);
        }
        else if (answer.equals("N")){
            System.out.println("Exit");
        }
    }

    public int auth(String username, String password){
        // Need to verify if the username already exist or not; if exist: 0, if wrong password: 1, if user does not exist: 2
        int result = 1;
        result = firebaseConnection.authentification(username, password);
        System.out.println(result);

        int returnValue = -1;
        return 2; // default value because not DB
    }

    public void createSession(User user){
        Frame frame= new Frame(user);
        frame.startGui();
    }
}
