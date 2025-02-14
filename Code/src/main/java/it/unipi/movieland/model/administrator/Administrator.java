package it.unipi.movieland.model.administrator;

//
public class Administrator {

    private String name;
    private String surname;
    private String _id;
    private String password;

    public Administrator() {}

    public Administrator(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this._id = email;
        this.password = password;

    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
