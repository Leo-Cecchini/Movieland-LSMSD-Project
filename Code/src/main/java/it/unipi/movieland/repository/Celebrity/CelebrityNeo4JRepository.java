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

    //NEW METHOD TO GET ALL IDs OF CELEBRITIES
    @Query("MATCH (c:Celebrity) RETURN c.person_id")
    List<String> findAllIds();
}
