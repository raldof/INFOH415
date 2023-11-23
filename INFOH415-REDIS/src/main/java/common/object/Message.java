package common.object;

import java.time.LocalDateTime;

public class Message {

    private String message;
    private User user;
    private LocalDateTime date;

    public Message(String message, User user, LocalDateTime date) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
