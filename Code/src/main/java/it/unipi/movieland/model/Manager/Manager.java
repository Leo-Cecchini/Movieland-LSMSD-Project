package it.unipi.movieland.model.Manager;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Managers")
public class Manager {
    private String _id;
    private String email;
    private String password;

    public String get_id() {
        return _id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Manager() {}

    public Manager(String _id, String email, String password) {
        this._id = _id;
        this.email = email;
        this.password = password;
    }
}
