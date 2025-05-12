package it.unipi.movieland.model.Movie;

import it.unipi.movieland.model.Enum.GenreEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Node("Movie")
public class MovieNeo4J {

    @Id
    @Property("imdb_id")
    private String imdbId;
    private String title;
    private List<GenreEnum> genres;

    public MovieNeo4J(String imdbId, String title, List<GenreEnum> genres) {
        this.imdbId = imdbId;
        this.title = title;
        this.genres = genres;
    }

    //GETTERS AND SETTERS
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<GenreEnum> getGenres() { return genres; }
    public void setGenres(List<GenreEnum> genres) { this.genres = genres; }
}