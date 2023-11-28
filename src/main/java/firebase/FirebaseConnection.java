package firebase;

import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import firebase.object.Message;
import firebase.object.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class FirebaseConnection {
    public CountDownLatch latch;
    DatabaseReference ref;

    ArrayList<Message> messages = new ArrayList<>();

    public boolean chatRoomCreator = false;

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
        List<String>  listofString = null;


        DatabaseReference usersRef = ref.child("Users");


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {


                        if (child.getKey().equals(username)) {
                            userExists[0] = true;
                            if (child.getValue().equals(password)){
                                System.out.println("Mot de passe est bon");
                                result[0] = 0;
                            }else {
                                result[0] = 1;
                                System.out.println(password + " != " + child.getValue());
                            }
                            latch.countDown();
                            break;
                        }

                    }
                    latch.countDown();
                } else {
                    result[0] = 2;
                }latch.countDown();
                if (!userExists[0]){
                    System.out.println("Creating a new account");
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
        return result[0];
    }



    public void openChatRoom(User user){
        chatRoomCreator = true;
        DatabaseReference dbRef =  ref.child("ChatRoomParticipent");
        DatabaseReference dbMessage =  ref.child("ChatRoomMessage");
        HashMap<String, Object> h = new HashMap<>();
        h.put(user.getName(), user);
        dbRef.updateChildren(h,(error, ref1) -> {
            if (error != null) {
                System.out.println("Data could not be saved " + error.getMessage());
            } else {
                System.out.println("Data saved successfully. 2 ");
            }
        });

        HashMap<String, Object> hashMessage = new HashMap<>();
        Message message = new Message("ChatRoom Opened", user, LocalDateTime.now().toString());

        hashMessage.put(user.getName() + "|" +  Message.counter, message);
        dbMessage.updateChildren(hashMessage,(error, ref1) -> {
            if (error != null) {
                System.out.println("Data could not be saved " + error.getMessage());
            } else {
                System.out.println("Data saved successfully. 33333 ");
            }
        });

    }

    public void closeChatRoom(){
        DatabaseReference childReferenceToDelete = ref.child("ChatRoomParticipent");
        childReferenceToDelete.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Échec de la suppression du nœud : " + databaseError.getMessage());
                } else {
                    System.out.println("Nœud supprimé avec succès.");
                }
            }
        });
        DatabaseReference childReferenceToDelete2 = ref.child("ChatRoomMessage");
        childReferenceToDelete2.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Échec de la suppression du nœud : " + databaseError.getMessage());
                } else {
                    System.out.println("Nœud supprimé avec succès.");
                }
            }
        });
    }

    public void joinChatRoom(User user){
        DatabaseReference dbRef =  ref.child("ChatRoomParticipent");
        HashMap<String, Object> h = new HashMap<>();
        h.put(user.getName(), user);
        dbRef.updateChildren(h,(error, ref1) -> {
            if (error != null) {
                System.out.println("Data could not be saved " + error.getMessage());
            } else {
                System.out.println("Data saved successfully. 2 ");
            }
        });
    }

    public void sendMessage(Message message){

        DatabaseReference dbMessage =  ref.child("ChatRoomMessage");
        HashMap<String, Object> hashMessage = new HashMap<>();
        Message.counter ++;
        hashMessage.put(message.getUser().getName() + "|" + Message.counter, message);
        dbMessage.updateChildren(hashMessage,(error, ref1) -> {
            if (error != null) {
                System.out.println("Data could not be saved " + error.getMessage());
            } else {
                System.out.println("Data saved successfully. 33333 ");
            }
        });
    }

    public boolean isChatRoomOpen(){
        final boolean[] result = {false};
        latch = new CountDownLatch(1);
        ref.child("ChatRoomMessage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Vérifie si le nœud existe
                if (dataSnapshot.exists()) {
                    result[0] = true;
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Erreur lors de la vérification de l'existence du nœud : " + databaseError.getMessage());
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }

    public  Message[] receiveMessage(){
        HashMap<String, Message> localisationMap = new HashMap<>();
        List<String> listMess = new ArrayList<>();

        latch = new CountDownLatch(1);
        messages.clear();
        DatabaseReference dbMessage =  ref.child("ChatRoomMessage");

        dbMessage.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot child : snapshot.getChildren()) {


                    String name = String.valueOf(child.child("user").child("name").getValue());
                    int r = Integer.valueOf(String.valueOf(child.child("user").child("color").child("0").getValue()));
                    int g = Integer.valueOf(String.valueOf(child.child("user").child("color").child("1").getValue()));
                    int b = Integer.valueOf(String.valueOf(child.child("user").child("color").child("2").getValue()));

                    User user = new User(name, r, g, b);
                    String message = String.valueOf(child.child("message").getValue());
                    String date = String.valueOf(child.child("date").getValue());

                    Message message1 = new Message(message, user, date);
                    messages.add(message1);

                    listMess.add(child.getKey());
                    localisationMap.put(child.getKey(), message1);
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Cancelled");
            }
        });




        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        Collections.sort(listMess, Comparator.comparingInt(s -> Integer.parseInt(s.split("\\|")[1])));
        Message[] arr = new Message[messages.size()];



        for (int i = 0; i< messages.size() ; i++){
            messages.set(i, localisationMap.get(listMess.get(i)));
        }

        latch.countDown();
        return messages.toArray(arr);
    }


}