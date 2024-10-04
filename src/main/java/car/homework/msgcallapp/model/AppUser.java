package car.homework.msgcallapp.model;

public class AppUser {

    public enum UserStatus {
        CONNECTED,
        AWAY,
        BUSY,
        OFFLINE
    }

    private String username;
    private String password;
    private UserStatus status; // Statut : en ligne, absent, etc.

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = UserStatus.OFFLINE;
    }

    // Getters et Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}