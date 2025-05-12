package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.MovieNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface MovieNeo4JRepository extends Neo4jRepository<MovieNeo4J, String> {

    //METHOD TO FIND A MOVIE BY ITS IMDB ID
    @Query("MATCH (movie:Movie) WHERE movie.imdb_id = $id RETURN movie LIMIT 1")
    Optional<MovieNeo4J> findByImdbId(String id);

    //METHOD TO FIND ALL IMDB ID OF MOVIES (LIMITED 100 RESULTS)
    @Query("MATCH (n:Movie) RETURN n.imdb_id AS all_ids LIMIT 100")
    List<String> findAllIds();

    void deleteById(String id);

    MovieNeo4J save(MovieNeo4J movie);
}