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

    //RECOMMEND ACTORS BY USER LIKES
    @Query("MATCH (u:User {username: $username})-[:MOVIE_LIKE]->(m:Movie)<-[:ACTED_IN]-(a:Celebrity) " +
            "WHERE NOT (u)-[:FOLLOW]->(a) " +
            "RETURN a.person_id AS person_id, a.name AS RecommendedActor, a.Poster AS poster, COUNT(m) AS SharedMovies " +
            "ORDER BY SharedMovies DESC " +
            "LIMIT 10")
    List<Map<String, Object>> recommendActorsByUserLikes(@Param("username") String username);

    //ACTOR RECOMMENDATIONS FROM FOLLOWING
    @Query("MATCH (u:User {username: $username})-[:ACTOR_FOLLOW]->(a1:Celebrity)-[:ACTED_IN]->(m:Movie)<-[:ACTED_IN]-(a2:Celebrity) " +
            "WHERE NOT (u)-[:FOLLOW]->(a2) " + // Escludi attori già seguiti
            "RETURN a2.name AS RecommendedActor, COUNT(m) AS SharedMovies, a1.name AS with " +
            "ORDER BY SharedMovies DESC "+
            "LIMIT 10")
    List<Map<String, Object>> recommendActorsByFollowedActors(@Param("username") String username);

    //ACTOR RECOMMENDATIONS FROM SECOND DEGREE CONNECTIONS
    @Query("MATCH (user:User {username: $username})-[:FOLLOW|MOVIE_LIKE]->(intermediate)-[:ACTOR_FOLLOW|DIRECTED_IN|ACTED_IN]-(recommendedCelebrity:Celebrity) " +
            "WHERE NOT (user)-[:ACTOR_FOLLOW]->(recommendedCelebrity) " +
            "WITH recommendedCelebrity, COUNT(DISTINCT intermediate) AS numRelations " +
            "OPTIONAL MATCH (recommendedCelebrity)<-[:ACTOR_FOLLOW]-(follower:User) " +
            "WITH recommendedCelebrity, numRelations, COUNT(follower) AS FollowerCount " +
            "RETURN recommendedCelebrity.name AS recommendedCelebrity, numRelations, FollowerCount " +
            "ORDER BY numRelations DESC, FollowerCount DESC " +
            "LIMIT 10")
    List<Map<String, Object>> recommendSecondDegreeCelebrities(@Param("username") String username);
}


