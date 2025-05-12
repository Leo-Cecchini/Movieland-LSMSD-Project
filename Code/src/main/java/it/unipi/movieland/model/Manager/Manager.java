package it.unipi.movieland.model.Manager;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Managers")
public class Manager {

    @Id
    @Field("_id")
    private String username;

    private String email;
    private String password;

    public Manager() {}

    public Manager(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //GETTERS AND SETTERS
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Manager{" +
                "username ='" + username + '\'' +
                ", email ='" + email + '\'' +
                ", password ='[PROTECTED]'" +
                '}';
    }
}