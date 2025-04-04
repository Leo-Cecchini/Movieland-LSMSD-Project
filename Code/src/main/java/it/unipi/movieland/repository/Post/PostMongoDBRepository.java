package it.unipi.movieland.repository.Post;

import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostMongoDBRepository extends MongoRepository<Post, String> {
    @Query(value = "{ 'movie_id': ?0 }", fields = "{ 'comment': 0 }")
    Page<PostDTO> findByMovieId(String movieId, Pageable pageable);

    @Query(fields = "{ 'comment': 0 }")
    Page<PostDTO> findByDatetimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $project: { hour: { $hour: '$datetime' } } }",
            "{ $group: { _id: '$hour', postCount: { $count: {} } } }",
            "{ $sort: { _id: 1 } }",
            "{ $project: { hour: '$_id', postCount: 1 } }"
    })
    List<PostActivityDTO> getPostActivity();

    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Comments', 'localField': '_id', 'foreignField': 'post_id', 'as': 'postComments' } }",
            "{ '$group': { '_id': '$author', 'totalPosts': { '$sum': 1 }, 'totalComments': { '$sum': { '$size': '$postComments' } } } }",
            "{ '$addFields': { 'commentsPerPost': { '$cond': { 'if': { '$eq': [ '$totalPosts', 0 ] }, 'then': 0, 'else': { '$divide': [ '$totalComments', '$totalPosts' ] } } } } }",
            "{ '$sort': { 'totalComments': -1, 'totalPosts': -1, 'commentsPerPost': -1 } }",
            "{ '$project': { 'username': '$_id', 'totalPosts': 1, 'totalComments': 1, 'commentsPerPost': 1, '_id': 0 } }"
    })
    List<UserInfluencerDTO> getInfluencersReport();

}