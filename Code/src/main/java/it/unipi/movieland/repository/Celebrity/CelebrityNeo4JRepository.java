package it.unipi.movieland.repository.Celebrity;

import it.unipi.movieland.model.Celebrity.CelebrityNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;
import java.util.Map;

@Repository
public interface CelebrityNeo4JRepository extends Neo4jRepository<CelebrityNeo4J, String> {

    void deleteById(String id);

    // Metodo per recuperare tutte le celebrità con paginazione
    Page<CelebrityNeo4J> findAll(Pageable pageable);

    //CREATE A CELEBRITY
    @Query("CREATE (c:Celebrity {person_id: $personId, name: $name, Poster: $poster}) RETURN c")
    CelebrityNeo4J createCelebrity(@Param("personId") String personId, @Param("name") String name, @Param("poster") String poster);

    //FIND CELEBRITY BY ID
    @Query("MATCH (c:Celebrity {person_id: $personId}) RETURN c.person_id AS person_id, c.name AS name, c.Poster AS Poster ")
    CelebrityNeo4J findCelebrityByPersonId(@Param("personId") String personId);

    //UPDATE CELEBRITY
    @Query("MATCH (c:Celebrity {person_id: $personId}) " +
            "SET c.name = $name, c.Poster = $poster " +
            "RETURN c")
    Optional<CelebrityNeo4J> updateCelebrity(String personId, String name, String poster);

    //ACTOR RECOMMENDATIONS FROM SECOND DEGREE CONNECTIONS
    @Query("MATCH (user:User {username: $username})-[:FOLLOW]->(friend:User)-[:ACTOR_FOLLOW]->(celebrity:Celebrity) " +
            "WHERE NOT (user)-[:ACTOR_FOLLOW]->(celebrity) " +
            "RETURN celebrity.name AS recommendedCelebrity, COUNT(friend) AS numCommonFollowers " +
            "ORDER BY numCommonFollowers DESC " +
            "LIMIT 10")
    List<Map<String, Object>> recommendSecondDegreeCelebrities(@Param("username") String username);
}


