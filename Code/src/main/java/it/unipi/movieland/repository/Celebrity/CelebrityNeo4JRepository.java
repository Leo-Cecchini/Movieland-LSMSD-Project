package it.unipi.movieland.repository.Celebrity;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.unipi.movieland.model.Celebrity.CelebrityNeo4J;

import java.util.List;
import java.util.Optional;

@Repository
public interface CelebrityNeo4JRepository extends Neo4jRepository<CelebrityNeo4J, String> {

    void deleteById(String id);

    //METHOD TO RETRIEVE ALL CELEBRITIES WITH PAGINATION
    Page<CelebrityNeo4J> findAll(Pageable pageable);

    //UPDATE CELEBRITY
    @Query("MATCH (c:Celebrity {person_id: $personId}) " +
            "SET c.name = $name, c.Poster = $poster " +
            "RETURN c")
    Optional<CelebrityNeo4J> updateCelebrity(String personId, String name, String poster);

    //METHOD TO GET ALL IDs OF CELEBRITIES
    @Query("MATCH (c:Celebrity) RETURN c.person_id")
    List<String> findAllIds();

    //ADD ACTED_IN
    @Query("MATCH (c:Celebrity {person_id: $personId}), (m:Movie {imdb_id: $movieId}) " +
            "MERGE (c)-[:ACTED_IN {character: $character}]->(m)")
    void addActedInRelationship(String personId, String movieId, String character);

    //ADD DIRECTED_IN
    @Query("MATCH (c:Celebrity {person_id: $personId}), (m:Movie {imdb_id: $movieId}) " +
            "MERGE (c)-[:DIRECTED_IN]->(m)")
    void addDirectorInRelationship(String personId, String movieId);

    //REMOVE ACTED_IN
    @Query("MATCH (c:Celebrity {person_id: $personId})-[r:ACTED_IN]->(m:Movie {imdb_id: $movieId}) DELETE r")
    void removeActedInRelationship(String personId, String movieId);

    //REMOVE DIRECTED_IN
    @Query("MATCH (c:Celebrity {person_id: $personId})-[r:DIRECTED_IN]->(m:Movie {imdb_id: $movieId}) " +
            "DELETE r")
    void removeDirectedInRelationship(String personId, String movieId);
}


