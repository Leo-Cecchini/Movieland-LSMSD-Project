package it.unipi.movieland.repository.Comment;

import it.unipi.movieland.dto.Celebrity.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import it.unipi.movieland.model.Comment.CommentMongoDB;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

@Repository
public interface CommentMongoDBRepository extends MongoRepository<CommentMongoDB, String> {

    //METHOD TO SEARCH COMMENTS BASED ON AUTHOR
    Page<CommentMongoDB> findByAuthor(String author, Pageable pageable);

    //METHOD TO SEARCH COMMENTS BASED ON A DATE RANGE
    Page<CommentDTO> findByDatetimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    //METHOD TO FIND COMMENTS USING AGGREGATION
    @Aggregation(pipeline = {
            "{ $match: { post_id: ?0 } }",
            "{ $sort: { datetime: -1 } }",
    })
    Slice<CommentMongoDB> findCommentsByPostId(ObjectId postId, Pageable pageable);
}