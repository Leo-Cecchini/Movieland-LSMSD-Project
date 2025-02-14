package it.unipi.movieland.model.Celebrity;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Id;
//
@Node("Celebrity")
public class CelebrityNeo4J {

    @Id
    private String person_id;
    private String name;
    private String Poster;

    //Costruttore non parametrizzato
    public CelebrityNeo4J() {}

    // Costruttore parametrizzato
    public CelebrityNeo4J(String person_id, String name, String poster) {
        this.person_id = person_id;
        this.name = name;
        this.Poster = poster;
    }

    // Getters e Setters
    public String getPersonId() {
        return person_id;
    }

    public void setPersonId(String person_id) {
        this.person_id = person_id;
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
    @Override
    public String toString() {
        return "CelebrityNeo4J{" +
                "person_id=" + person_id +
                ", name='" + name + '\'' +
                ", poster='" + Poster + '\'' +
                '}';
    }
}