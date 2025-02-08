package it.unipi.movieland.dto;

import it.unipi.movieland.model.Celebrity.Job;
import java.util.List;

public class CelebrityNeo4JDTO {
    private String person_id;  // ID come String per Neo4j
    private String name;
    private String Poster;

    // Costruttore predefinito
    public CelebrityNeo4JDTO() {}

    // Costruttore con parametri
    public CelebrityNeo4JDTO(String personId, String name, String poster, List<Job> jobs) {
        this.person_id = personId;
        this.name = name;
        this.Poster = poster;
    }

    // Getters e Setters
    public String getPersonId() {
        return person_id;
    }

    public void setPersonId(String personId) {
        this.person_id = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }


}