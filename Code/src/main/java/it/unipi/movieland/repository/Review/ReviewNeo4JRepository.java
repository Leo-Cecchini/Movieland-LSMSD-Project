package it.unipi.movieland.repository.Review;

import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.Review.ReviewNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ReviewNeo4JRepository extends Neo4jRepository<ReviewNeo4J,String> {

    @Query("MATCH (u:Movie), (m:Review) WHERE u.imdb_id = $movieId AND m.review_id = $reviewId MERGE (u)<-[r:EVALUATES]-(m)")
    void setMovie(String reviewId,String movieId);

    @Query("MATCH (u:User), (m:Review) WHERE u.username = $userId AND m.review_id = $reviewId MERGE (u)-[r:REVIEW_LIKE]->(m)")
    void likeReview(String reviewId, String userId);

    @Query("MATCH (u:User)-[r:REVIEW_LIKE]->(m:Review) WHERE u.username = $userId AND m.review_id = $reviewId DELETE r")
    void unlikeReview(String reviewId, String userId);

    @Query("MATCH (u:Review {review_id: $id}) SET u.sentiment = $sentiment")
    void updateReview(String id, boolean sentiment);
}
