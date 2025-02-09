package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.MovieNeo4j;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface Movie_neo4j_interface extends Neo4jRepository<MovieNeo4j, String> {
    MovieNeo4j save(MovieNeo4j movie);
    void deleteById(String id);
}
