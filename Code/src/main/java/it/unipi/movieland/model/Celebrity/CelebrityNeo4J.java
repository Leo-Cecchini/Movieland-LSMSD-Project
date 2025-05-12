package it.unipi.movieland.model.Celebrity;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Celebrity")
public class CelebrityNeo4J {

    @Id
    @Property("person_id")
    private String personId;

    private String name;

    @Property("Poster")
    private String poster;

    public CelebrityNeo4J() {}

    public CelebrityNeo4J(String personId, String name, String poster) {
        this.personId = personId;
        this.name = name;
        this.poster = poster;
    }

    //GETTER AND SETTER
    public String getPersonId() {
        return personId;
    }
    public void setPersonId(String personId) { this.personId = personId; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) { this.poster = poster;}

    @Override
    public String toString() {
        return "CelebrityNeo4J{" +
                "personId= " + personId +
                ", name= " + name + '\'' +
                ", poster= " + poster + '\'' +
                '}';
    }
}