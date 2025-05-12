package it.unipi.movieland.model.User;

import org.springframework.data.mongodb.core.mapping.Field;

public class UserCelebrity {
    @Field("person_id")
    private Integer celebrityId;
    private String name;
    private String poster;

    public UserCelebrity() {}

    public UserCelebrity(Integer celebrityId, String name, String poster) {
        this.celebrityId = celebrityId;
        this.name = name;
        this.poster = poster;
    }

    public UserCelebrity(String celebrityId, String name, String poster) {
        this.celebrityId = Integer.parseInt(celebrityId); // Conversione diretta dalla stringa
        this.name = name;
        this.poster = poster;
    }

    //GETTERS AND SETTERS
    public Integer getCelebrityId() {
        return celebrityId;
    }
    public void setCelebrityId(Integer celebrityId) {
        this.celebrityId = celebrityId;
    }
    public void setCelebrityId(String celebrityId) {
        this.celebrityId = Integer.parseInt(celebrityId); // Conversione diretta dalla stringa
    }

    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}