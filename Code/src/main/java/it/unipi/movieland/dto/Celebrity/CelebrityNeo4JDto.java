package it.unipi.movieland.dto.Celebrity;

import it.unipi.movieland.model.Celebrity.CelebrityNeo4J;

public class CelebrityNeo4JDto {

    private String personId;
    private String name;
    private String poster;

    public CelebrityNeo4JDto(String personId, String name, String poster) {
        this.personId = personId;
        this.name = name;
        this.poster = poster;
    }

    //GETTER AND SETTER
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }


    public static CelebrityNeo4JDto fromEntity(CelebrityNeo4J entity) {
        return new CelebrityNeo4JDto(entity.getPersonId(), entity.getName(), entity.getPoster());
    }
}