package firebase.object;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message  {

    public static int counter = 0;

    private String message;

    private User user;

    private String date;

    public Message(String message, User user, String date) {
        this.message = message;
        this.user = user;
        this.date = date;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return message + user.toString() + date;
    }

}
