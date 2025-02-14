package it.unipi.movieland.model.Movie;

import it.unipi.movieland.model.GenreEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Node("Movie")
public class MovieNeo4j {
    @Id
    private String imdb_id;
    private String title;
    private List<GenreEnum> genres;

    public MovieNeo4j(String imdb_id, String title, List<GenreEnum> genres) {
        this.imdb_id = imdb_id;
        this.title = title;
        this.genres = genres;
    }

    public String getImdb_id() {return imdb_id;}
    public void setImdb_id(String imdb_id) {this.imdb_id = imdb_id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public List<GenreEnum> getGenres() {return genres;}
    public void setGenres(List<GenreEnum> genres) {this.genres = genres;}
}
