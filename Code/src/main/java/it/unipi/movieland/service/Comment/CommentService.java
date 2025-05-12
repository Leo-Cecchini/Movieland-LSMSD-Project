package it.unipi.movieland.service.Comment;

import it.unipi.movieland.dto.Celebrity.CommentDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.exception.CommentNotFoundInMongoException;
import it.unipi.movieland.exception.InvalidDateFormatException;
import it.unipi.movieland.exception.PostNotFoundInMongoException;
import it.unipi.movieland.exception.UserNotFoundInMongoException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.time.LocalDateTime;
import java.util.Optional;

import it.unipi.movieland.model.Comment.CommentMongoDB;
import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import it.unipi.movieland.repository.Comment.CommentMongoDBRepository;

@Service
public class CommentService {

    private final CommentMongoDBRepository commentRepository;
    private final UserMongoDBRepository userRepository;
    private final PostMongoDBRepository postRepository;

    @Autowired
    public CommentService(CommentMongoDBRepository commentRepository,UserMongoDBRepository userRepository, PostMongoDBRepository postRepository, MongoTemplate mongoTemplate) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    //METHOD TO CREATE A COMMENT
    public CommentMongoDB createComment(String text, String authorId, String postId) {
        if (!userRepository.existsById(authorId)) {
            throw new CommentNotFoundInMongoException("AUTHOR WITH ID : " + authorId + " NOT FOUND.");}

        if (!postRepository.existsById(postId)) {
            throw new CommentNotFoundInMongoException("POST WITH ID :" + postId + " NOT FOUND.");}

        CommentMongoDB comment = new CommentMongoDB(text,authorId,postId);
        return commentRepository.save(comment);
    }

    //METHOD TO RETRIEVE A COMMENT BY ID
    public CommentMongoDB getCommentById(String id) {
        Optional<CommentMongoDB> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new CommentNotFoundInMongoException("COMMENT WITH ID " + id + " NOT FOUND");
        }
        return comment.get();
    }

    //METHOD TO RETRIEVE ALL COMMENTS
    public Page<CommentMongoDB> getAllComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    //METHOD TO DELETE A COMMENT BY ID
    public void deleteComment(String id) {

        Optional<CommentMongoDB> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new CommentNotFoundInMongoException("COMMENT WITH ID " + id + " NOT FOUND");
        }
        commentRepository.delete(comment.get());
    }

    //METHOD TO UPDATE AN EXISTING COMMENT
    public CommentMongoDB updateComment(String id, String text) {

        CommentMongoDB comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundInMongoException("COMMENT WITH ID " + id + " NOT FOUND"));

        comment.setText(text);
        comment.setDatetime(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    //METHOD TO GET COMMENTS BY AUTHOR
    public Page<CommentMongoDB> getCommentsByAuthor(String authorId, int page, int size) {

        boolean userExists = userRepository.existsById(authorId);

        if (!userExists) {
            throw new UserNotFoundInMongoException("AUTHOR WITH ID " + authorId + " NOT FOUND.");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CommentMongoDB> comments = commentRepository.findByAuthor(authorId, pageRequest);

        if (comments.isEmpty()) {
            throw new CommentNotFoundInMongoException("COMMENTS FOR AUTHOR WITH ID " + authorId + " NOT FOUND");
        }

        return comments;
    }

    //METHOD TO GET COMMENTS BY DATE
    public Page<CommentDTO> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {

        if (startDate == null || endDate == null) {
            throw new InvalidDateFormatException("THE DATE FORMAT ENTERED IS INVALID. USE ‘yyyy-MM-dd’T’HH:mm:ss’.");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateFormatException("THE START DATE CANNOT BE AFTER THE END DATE.");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }

    //METHOD TO GET COMMENTS BY POST ID
    public Slice<CommentMongoDB> getCommentsByPostId(ObjectId postId, int page, int size) {
        if (!postRepository.existsById(String.valueOf(postId))) {
            throw new PostNotFoundInMongoException("POST WITH ID:" + postId + " NOT FOUND.");
        }

        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findCommentsByPostId(postId, pageable);
    }
}







