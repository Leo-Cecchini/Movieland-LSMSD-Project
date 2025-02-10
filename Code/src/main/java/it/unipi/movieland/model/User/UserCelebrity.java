package it.unipi.movieland.model.User;

import org.springframework.data.mongodb.core.mapping.Field;

public class UserCelebrity {
    @Field("person_id")
    private Integer celebrityId;
    private String name;
    private String poster;

    public UserCelebrity(Integer celebrityId, String name, String poster) {
        this.celebrityId = celebrityId;
        this.name = name;
        this.poster = poster;
    }

    public Integer getCelebrityId() {
        return celebrityId;
    }

    public String getPoster() {
        return poster;
    }

    public String getName() {
        return name;
    }

    public void setCelebrityId(Integer celebrityId) {
        this.celebrityId = celebrityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}