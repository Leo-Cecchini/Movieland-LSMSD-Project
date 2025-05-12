package it.unipi.movieland.repository.Review;

import it.unipi.movieland.model.Review.ReviewNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

public interface ReviewNeo4JRepository extends Neo4jRepository<ReviewNeo4J, String> {

    //METHOD TO ASSOCIATE A MOVIE WITH A REVIEW
    @Query("MATCH (u:Movie), (m:Review) WHERE u.imdb_id = $movieId AND m.review_id = $reviewId MERGE (u)<-[r:EVALUATES]-(m)")
    void setMovie(String reviewId, String movieId);

    //METHOD TO LIKE A REVIEW BY A USER
    @Query("MATCH (u:User), (m:Review) WHERE u.username = $userId AND m.review_id = $reviewId MERGE (u)-[r:REVIEW_LIKE]->(m)")
    void likeReview(String reviewId, String userId);

    //METHOD TO UNLIKE A REVIEW BY A USER
    @Query("MATCH (u:User)-[r:REVIEW_LIKE]->(m:Review) WHERE u.username = $userId AND m.review_id = $reviewId DELETE r")
    void unlikeReview(String reviewId, String userId);

    //METHOD TO UPDATE SENTIMENT OF A REVIEW
    @Query("MATCH (r:Review {review_id: $id}) RETURN r")
    void updateReview(String id);

    //METHOD TO GET ALL REVIEW IDS
    @Query("MATCH (n:Review) RETURN n.review_id AS all_ids")
    List<String> findAllIds();

    //METHOD TO CHECK IF A REVIEW IS LIKED BY A USER
    @Query("RETURN EXISTS { MATCH (:User {username: $username})-[:REVIEW_LIKE]->(Review {review_id: $reviewId}) }")
    boolean isReviewLiked(String username, String reviewId);

    //METHOD TO FIND USERS WHO LIKED A SPECIFIC REVIEW
    @Query("MATCH (r:Review {review_id: $reviewId})<-[:REVIEW_LIKE]-(u:User) RETURN properties(u) AS props")
    List<Map<String, Object>> findUserLikeReview(String reviewId);
}