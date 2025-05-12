package it.unipi.movieland.repository.User;

import it.unipi.movieland.dto.CelebrityRecommendationsDTO;
import it.unipi.movieland.dto.MovieRecommendationsDTO;
import it.unipi.movieland.dto.UserRecommendationsDTO;
import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.User.UserCelebrity;
import it.unipi.movieland.model.User.UserMovie;
import it.unipi.movieland.model.User.UserNeo4J;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;

public interface UserNeo4JRepository extends Neo4jRepository<UserNeo4J, String> {

    //METHOD TO GET THE MOVIES LIKED BY A USER.
    @Query("MATCH (u:User)-[r:MOVIE_LIKE]->(m:Movie) WHERE u.username = $userId ORDER BY r.date DESC SKIP $skip LIMIT $limit RETURN m.title AS title, m.imdb_id AS movieId, m.Poster_path AS poster ")
    Slice<UserMovie> getLikedMovies(String userId, Pageable pageable);

    //METHOD TO ADD A MOVIE TO A USER'S LIKED MOVIES.
    @Query("MATCH (u:User), (m:Movie) WHERE u.username = $userId AND m.imdb_id = $movieId MERGE (u)-[r:MOVIE_LIKE]->(m) SET r.timestamp = datetime()")
    void addToLikedMovies(String userId, String movieId);

    //METHOD TO REMOVE A MOVIE FROM A USER'S LIKED MOVIES.
    @Query("MATCH (u:User)-[r:MOVIE_LIKE]->(m:Movie) WHERE u.username = $userId AND m.imdb_id = $movieId DELETE r WITH u " +
            "MATCH (u)-[r2:MOVIE_LIKE]->(m2:Movie) ORDER BY r2.likedAt DESC LIMIT 5 RETURN m2.title AS title, m2.imdb_id AS movieId, m2.Poster_path AS poster")
    List<UserMovie> removeFromLikedMovies(String userId, String movieId);

    //METHOD TO GET THE CELEBRITIES FOLLOWED BY A USER.
    @Query("MATCH (u:User)-[r:ACTOR_FOLLOW]->(m:Celebrity) WHERE u.username = $userId ORDER BY r.date DESC SKIP $skip LIMIT $limit RETURN m.name AS name, toInteger(m.person_id) AS celebrityId ")
    Slice<UserCelebrity> getFollowedCelebrities(String userId, Pageable pageable);

    //METHOD TO ADD A CELEBRITY TO A USER'S FOLLOWED CELEBRITIES.
    @Query("MATCH (u:User), (m:Celebrity) WHERE u.username = $userId AND m.person_id = $celebrityId MERGE (u)-[r:ACTOR_FOLLOW]->(m) SET r.Date = datetime()")
    void addToFollowedCelebrities(String userId, String celebrityId);

    //METHOD TO REMOVE A CELEBRITY FROM A USER'S FOLLOWED CELEBRITIES.
    @Query("MATCH (u:User)-[r:ACTOR_FOLLOW]->(m:Celebrity) WHERE u.username = $userId AND m.person_id = $celebrityId DELETE r WITH u " +
            "MATCH (u)-[r2:ACTOR_FOLLOW]->(m2:Celebrity) ORDER BY r2.date DESC LIMIT 5 RETURN m2.name AS name, toInteger(m2.person_id) AS celebrityId ")
    List<UserCelebrity> removeFromFollowedCelebrities(String userId, String celebrityId);

    //METHOD TO GET THE USERS FOLLOWED BY A USER.
    @Query("MATCH (u:User)-[r:FOLLOW]->(m:User) WHERE u.username = $userId SKIP $skip LIMIT $limit RETURN m.username AS username, m.name AS name, m.surname AS surname, m.country AS country, m.favorite_genres AS favorite_genres ")
    Slice<UserNeo4J> getFollowed(String userId, Pageable pageable);

    //METHOD TO GET THE USERS FOLLOWING A USER.
    @Query("MATCH (u:User)<-[r:FOLLOW]-(m:User) WHERE u.username = $userId SKIP $skip LIMIT $limit RETURN m.username AS username, m.name AS name, m.surname AS surname, m.country AS country, m.favorite_genres AS favorite_genres ")
    Slice<UserNeo4J> getFollowers(String userId, Pageable pageable);

    //METHOD TO FOLLOW A USER.
    @Query("MATCH (u:User), (p:User) WHERE u.username = $userId AND p.username = $followedUserId MERGE (u)-[:FOLLOW]->(p) ")
    void followUser(String userId, String followedUserId);

    //METHOD TO UNFOLLOW A USER.
    @Query("MATCH (u:User)-[r:FOLLOW]->(p:User) WHERE u.username = $userId AND p.username = $followedUserId DELETE r ")
    void unfollowUser(String userId, String followedUserId);

    //METHOD TO COUNT THE NUMBER OF USERS FOLLOWED BY A USER.
    @Query("MATCH (u:User)-[r:FOLLOW]->(p) WHERE u.username = $userId RETURN count(r)")
    int countFollowed(String userId);

    //METHOD TO COUNT THE NUMBER OF USERS FOLLOWING A USER.
    @Query("MATCH (u:User)<-[r:FOLLOW]-(p) WHERE u.username = $userId RETURN count(r)")
    int countFollowers(String userId);

    //METHOD TO UPDATE USER INFORMATION.
    @Query("MATCH (u:User {username: $username}) SET u.email = $email, u.name = $name, u.surname = $surname, u.country = $country, u.favorite_genres = $favoriteGenres")
    void modifyUserDetails(String username, String email, String name, String surname, CountryEnum country, List<GenreEnum> favoriteGenres);

    //METHOD TO CHECK IF A USER HAS LIKED A MOVIE.
    @Query("RETURN EXISTS { MATCH (:User {username: $username})-[:MOVIE_LIKE]->(Movie {imdb_id: $movieId}) }")
    boolean isMovieLiked(String username, String movieId);

    //METHOD TO CHECK IF A USER IS FOLLOWING A CELEBRITY.
    @Query("RETURN EXISTS { MATCH (:User {username: $username})-[:ACTOR_FOLLOW]->(Celebrity {person_id: $celebrityId}) }")
    boolean isCelebrityFollowed(String username, String celebrityId);

    //METHOD TO CHECK IF A USER IS FOLLOWING ANOTHER USER.
    @Query("RETURN EXISTS { MATCH (:User {username: $username})-[:FOLLOW]->(User {username: $userId}) }")
    boolean isUserFollowed(String username, String userId);

    //METHOD TO GET ALL USERNAMES FROM THE DATABASE.
    @Query("MATCH (n:User) RETURN n.username AS all_ids")
    List<String> findAllUsernames();

    //METHOD TO RECOMMEND MOVIES BASED ON A USER'S PREFERENCES AND RELATIONS WITH OTHER USERS.
    @Query("MATCH (user:User {username:$userId})-[:FOLLOW|ACTOR_FOLLOW]->(intermediate)-[:ACTED_IN|DIRECTED_IN|MOVIE_LIKE]->(recommendedMovie:Movie) " +
            "WHERE NOT (user)-[:MOVIE_LIKE]->(recommendedMovie) " +
            "WITH recommendedMovie, COUNT(DISTINCT intermediate) AS numRelations, user " +
            "OPTIONAL MATCH (recommendedMovie)<-[:MOVIE_LIKE]-(likingUser:User) WITH recommendedMovie, numRelations, COUNT(likingUser) AS LikeCount, user " +
            "WITH recommendedMovie, numRelations, LikeCount, size(apoc.coll.intersection(user.favorite_genres, recommendedMovie.genres)) AS commonGenres " +
            "RETURN recommendedMovie.title AS title, recommendedMovie.imdb_id AS id, recommendedMovie.Poster_path as poster, LikeCount AS votes, numRelations, commonGenres " +
            "ORDER BY (0.5*commonGenres + numRelations) DESC, LikeCount DESC " +
            "SKIP $skip LIMIT $limit")
    Slice<MovieRecommendationsDTO> recommendMovies(String userId,Pageable pageable);

    //METHOD TO RECOMMEND CELEBRITIES BASED ON A USER'S PREFERENCES AND RELATIONS WITH OTHER USERS.
    @Query("MATCH (user:User {username: $userId})-[:FOLLOW|MOVIE_LIKE]->(intermediate)-[:ACTOR_FOLLOW|DIRECTED_IN|ACTED_IN]-(recommendedCelebrity:Celebrity) " +
            "WHERE NOT (user)-[:ACTOR_FOLLOW]->(recommendedCelebrity) " +
            "WITH recommendedCelebrity, COUNT(DISTINCT intermediate) AS numRelations " +
            "OPTIONAL MATCH (recommendedCelebrity)<-[:ACTOR_FOLLOW]-(follower:User) " +
            "WITH recommendedCelebrity, numRelations, COUNT(follower) AS FollowerCount " +
            "RETURN recommendedCelebrity.name AS name, recommendedCelebrity.person_id AS id, recommendedCelebrity.Poster AS poster, numRelations, FollowerCount AS followers " +
            "ORDER BY numRelations DESC, FollowerCount DESC " +
            "SKIP $skip LIMIT $limit")
    Slice<CelebrityRecommendationsDTO> recommendCelebrities(String userId, Pageable pageable);

    //METHOD TO RECOMMEND USERS BASED ON A USER'S RELATIONSHIPS WITH OTHER USERS AND THEIR PREFERENCES.
    @Query("MATCH (user:User {username: $userId})-[:FOLLOW|ACTOR_FOLLOW|MOVIE_LIKE]->(intermediate)-[:FOLLOW|ACTOR_FOLLOW|MOVIE_LIKE]-(recommendedUser:User) " +
            "WHERE NOT (user)-[:FOLLOW]->(recommendedUser) AND user <> recommendedUser " +
            "WITH recommendedUser, COUNT(DISTINCT intermediate) AS numRelations " +
            "OPTIONAL MATCH (recommendedUser)-[:FOLLOW]->(follower:User) " +
            "WITH recommendedUser, numRelations, COUNT(follower) AS FollowerCount " +
            "RETURN recommendedUser.username AS username, recommendedUser.country AS country, numRelations, FollowerCount AS followers " +
            "ORDER BY numRelations DESC, FollowerCount DESC " +
            "SKIP $skip LIMIT $limit")
    Slice<UserRecommendationsDTO> recommendUsers(String userId, Pageable pageable);

    //METHOD TO RECOMMEND MOVIES BASED ON REVIEWS THAT A USER HAS LIKED.
    @Query("MATCH (user:User {username: $userId})-[:REVIEW_LIKE]->(Review)<-[:POSTED]-(User)-[:POSTED]->(r:Review)-[:EVALUATES]->(recommendedMovie:Movie) " +
            "WHERE r.sentiment=true AND NOT (user)-[:MOVIE_LIKE]->(recommendedMovie) " +
            "WITH recommendedMovie, COUNT(DISTINCT r) AS numRelations, user " +
            "OPTIONAL MATCH (recommendedMovie)<-[:MOVIE_LIKE]-(likingUser:User) WITH recommendedMovie, numRelations, COUNT(likingUser) AS LikeCount, user " +
            "WITH recommendedMovie, numRelations, LikeCount, size(apoc.coll.intersection(user.favorite_genres, recommendedMovie.genres)) AS commonGenres " +
            "RETURN recommendedMovie.title AS title, recommendedMovie.imdb_id AS id, recommendedMovie.Poster_path as poster, LikeCount AS votes, numRelations, commonGenres " +
            "ORDER BY (0.5*commonGenres + numRelations) DESC, LikeCount DESC " +
            "SKIP $skip LIMIT $limit")
    Slice<MovieRecommendationsDTO> recommendByReview(String userId, Pageable pageable);

    //METHOD TO RECOMMEND MOVIES BASED ON ACTORS THAT A USER HAS LIKED MOVIES FROM.
    @Query("MATCH (user:User {username:$userId})-[:MOVIE_LIKE]->(likedMovie:Movie)<-[:ACTED_IN]-(intermediate:Celebrity)-[:ACTED_IN]->(recommendedMovie:Movie) " +
            "WHERE NOT (user)-[:MOVIE_LIKE]->(recommendedMovie) " +
            "WITH recommendedMovie, COUNT(DISTINCT intermediate) AS numRelations, user " +
            "OPTIONAL MATCH (recommendedMovie)<-[:MOVIE_LIKE]-(likingUser:User) WITH recommendedMovie, numRelations, COUNT(likingUser) AS LikeCount, user " +
            "WITH recommendedMovie, numRelations, LikeCount, size(apoc.coll.intersection(user.favorite_genres, recommendedMovie.genres)) AS commonGenres " +
            "RETURN recommendedMovie.title AS title, recommendedMovie.imdb_id AS id, recommendedMovie.Poster_path as poster, LikeCount AS votes, numRelations, commonGenres " +
            "ORDER BY (0.5*commonGenres + numRelations) DESC, LikeCount DESC " +
            "SKIP $skip LIMIT $limit")
    Slice<MovieRecommendationsDTO> recommendByCast(String userId,Pageable pageable);
}
