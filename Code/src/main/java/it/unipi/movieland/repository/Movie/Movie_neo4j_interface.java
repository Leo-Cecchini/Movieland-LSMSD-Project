package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.model.Movie.MovieNeo4j;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface Movie_neo4j_interface extends Neo4jRepository<MovieNeo4j, String> {
    @Query("MATCH (movie:Movie) WHERE movie.imdb_id = $id RETURN movie")
    Optional<MovieNeo4j> findByImdbId(String id);
    MovieNeo4j save(MovieNeo4j movie);
    void deleteById(String id);

    @Query("MATCH (n:Movie) RETURN n.imdb_id AS all_ids")
    List<String> findAllIds();
}
