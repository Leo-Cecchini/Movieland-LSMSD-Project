package it.unipi.movieland.repository.Post;

import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Post.PostMongoDB;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostMongoDBRepository extends MongoRepository<PostMongoDB, String> {

    //QUERY TO SEARCH POSTS BASED ON AUTHOR
    Page<PostMongoDB> findByAuthor(String author, Pageable pageable);

    //QUERY TO FIND POSTS BY MOVIE ID
    @Query(value = "{ 'movie_id': ?0 }", fields = "{ 'comment': 0 }")
    List<PostDTO> findByMovieId(String movieId);

    //QUERY TO SEARCH POSTS BASED ON DATE RANGE, EXCLUDING COMMENT FIELD
    @Query(fields = "{ 'comment': 0 }")
    Page<PostDTO> findByDatetimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    //QUERY TO GET POSTS ACTIVITY PER HOUR
    @Aggregation(pipeline = {
            "{ $project: { hour: { $hour: { $dateFromString: { dateString: \"$datetime\" } } } } }",
            "{ $group: { _id: \"$hour\", postCount: { $count: {} } } }",
            "{ $sort: { _id: 1 } }",
            "{ $project: { hour: \"$_id\", postCount: 1 } }"
    })
    List<PostActivityDTO> getPostActivity();

    //QUERY TO GET INFLUENCER REPORT BASED ON AUTHOR ACTIVITY AND COMMENTS
    @Aggregation(pipeline = {
            "{ $group: { _id: \"$author\", totalPosts: { $count: {} }, totalComments: { $sum: { $size: \"$comment\" } } } }",
            "{ $addFields: { commentsPerPost: { $cond: { if: { $eq: [ \"$totalPosts\", 0 ] }," + " then: 0, else: { $divide: [ \"$totalComments\", \"$totalPosts\" ] } } } } }",
            "{ $sort: { totalComments: -1, totalPosts: -1, commentsPerPost: -1 } }",
            "{ $project: { username: \"$_id\", totalPosts: 1, totalComments: 1, commentsPerPost: 1 } }"
    })
    List<UserInfluencerDTO> getInfluencersReport();
}