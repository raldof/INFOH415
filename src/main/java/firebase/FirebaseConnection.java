package firebase;

import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import firebase.object.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FirebaseConnection {
    public CountDownLatch latch;
    DatabaseReference ref;

    public FirebaseConnection() {
        latch = new CountDownLatch(1);
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("info415chat-firebase-adminsdk-6p9o5-db12e555ee.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://info415chat-default-rtdb.europe-west1.firebasedatabase.app")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseApp.initializeApp(options);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
    }





    public int authentification(String username, String password){
        int result[] = {0};
        final boolean[] userExists = {false};

        DatabaseReference usersRef = ref.child("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals(username)) {
                            userExists[0] = true;
                            if (child.getValue() == password){
                                System.out.println("Mot de passe est bon");
                                result[0] = 0;
                            }else {
                                System.out.println("Mot de passe est mauvais");
                                result[0] = 1;
                            }
                        }
                    }
                } else {
                    System.out.println("La base de données est vide.");
                    result[0] = 2;
                }
                if (!userExists[0]){
                    result[0] = 2;
                    Map<String, Object> users = new HashMap<>();
                    users.put(username, password);
                    usersRef.updateChildren(users, (error, ref1) -> {
                        if (error != null) {
                            System.out.println("Data could not be saved " + error.getMessage());
                        } else {
                            System.out.println("Data saved successfully.");
                        }
                    });
                }latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Erreur lors de la vérification de l'utilisateur : " + databaseError.getMessage());
            }
        });

        try {
            latch.await(); // Wait for countdown
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("return");
        return result[0];
    }

}
