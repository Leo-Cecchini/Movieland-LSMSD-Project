package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.MovieNeo4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class Movie_neo4j_repo {
    @Autowired
    private Movie_neo4j_interface movie_neo4j_interface;

    public void addTitle(MovieNeo4j movie){
        movie_neo4j_interface.save(movie);
    }

    public void updateTitle(MovieNeo4j movie){
        movie_neo4j_interface.save(movie);
    }

    public void deleteTitle(String id){
        movie_neo4j_interface.deleteById(id);
    }
}
