package redis.object;

import java.time.LocalDateTime;

public class Message {



    private int id;
    private String message;
    private User user;
    private LocalDateTime date;

    public Message(String message, User user, LocalDateTime date,int id) {
        this.message = message;
        this.user = user;
        this.date = date;
        this.id = id;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
