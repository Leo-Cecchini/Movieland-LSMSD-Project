package it.unipi.movieland.repository.Review;

import it.unipi.movieland.dto.ListIdDTO;
import it.unipi.movieland.dto.ReviewRatioDTO;
import it.unipi.movieland.model.Review.ReviewMongoDB;
import it.unipi.movieland.model.User.UserReview;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewMongoDBRepository extends MongoRepository<ReviewMongoDB, String> {

    @Aggregation(pipeline = {
            "{ $match: { movie_id: ?0 } }",
            "{ $sort: { timestamp: -1 } }",
            "{ $skip: ?2 }",
            "{ $limit: ?3 }"
    })
    List<ReviewMongoDB> findByMovieId(String movieId,int offset, int limit);

    @Aggregation(pipeline = {
            "{ $match: { username: ?0 } }",
            "{ $sort: { timestamp: -1 } }",
            "{ $skip: ?2 }",
            "{ $limit: ?3 }"
    })
    List<ReviewMongoDB> findByUsername(String userId,int offset, int limit);

    @Query("{ '_id': ?0 }")
    @Update("{ $set: { review: ?1, timestamp: ?2 } }")
    void updateReview(String id, String txt, LocalDateTime timestamp);

    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { num_likes: 1 }}")
    void likeReview(String id);

    @Query("{ '_id': ?0 }")
    @Update("{ dec: { num_likes: 1 }}")
    void unlikeReview(String id);

    @Aggregation(pipeline = {
            "{ $match: { 'username': ?0 } }",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: 1 }",
            "{ $lookup: { from: 'Movies', localField: 'movie_id', foreignField: '_id', as: 'movie' } }",
            "{ $unwind: '$movie' }",
            "{ $project: { review_id: '$_id', movie_title: '$movie.title', sentiment: '$sentiment', content: '$review'} }"
    })
    Optional<UserReview> findMostRecentReviewWithMovie(String username);

    @Aggregation(pipeline = {
            "{ $project: { _id: 1 } }",    // Seleziona solo il campo _id
            "{ $group: { _id: 1, allIds: { $push: '$_id' } } }" // Raggruppa e crea l'array
    })
    ListIdDTO findAllCommentIds();

    @Aggregation({
            "{ $group: { _id: '$movie_id', positiveReviews: { $sum: { $cond: [ { $eq: ['$sentiment', true] }, 1, 0 ] } }, totalReviews: { $sum: 1 } } }",
            "{ $sort: { totalReviews: -1 } }",
            "{ $limit: 500 }",
            "{ $project: { id: '$_id',positiveReviews: 1, totalReviews: 1, ratio: { $cond: { if: { $eq: ['$totalReviews', 0] }, then: 0, else: { $divide: ['$positiveReviews', '$totalReviews'] } } } } }",
            "{ $sort: { ratio: -1 } }",
            "{ $limit: 10 }"
    })
    List<ReviewRatioDTO> findTopMoviesByReviewRatio();
}