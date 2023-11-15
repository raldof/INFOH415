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
        DatabaseReference userRef = ref.child("User");
        Query query = userRef.orderByChild("name").equalTo(username);
        int[] result = {0};
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Récupération des données de l'utilisateur
                    String motDePasse = snapshot.child("password").getValue(String.class);
                    if (motDePasse.equals(password)){
                        result[0] = 0;
                    }
                    else {
                        result[0] = 1;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Gestion des erreurs
                if (error != null){
                    System.err.println("Erreur : " + error.getMessage());
                } else {
                    result[0] = 2;
                    User user = new User(username, password);
                    userRef.setValue(user, (error1, ref1) -> {
                        if (error1 != null) {
                            System.out.println("Data could not be saved " + error1.getMessage());
                        } else {
                            System.out.println("Data saved successfully.");
                        }
                    });
                }

            }
        });
        return result[0];
    }

}
