package it.unipi.movieland.service.Comment;

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

import it.unipi.movieland.model.Comment.Comment;
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
    public Comment createComment(String text, String authorId,String postId) {

        if (!userRepository.existsById(authorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AUTHOR WITH ID : " + authorId + " NOT FOUND.");
        }

        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POST WITH ID :" + postId + "NOT FOUND.");
        }

        Comment comment = new Comment(text,authorId,postId);
        return commentRepository.save(comment);
    }

    //METHOD TO RETRIEVE ALL COMMENTS
    public Page<Comment> getAllComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    //METHOD TO RETRIEVE A COMMENT BY ID
    public Comment getCommentById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "COMMENT WITH ID " + id + " NOT FOUND");
        }
        return comment.get();
    }

    //METHOD TO DELETE A COMMENT BY ID
    public void deleteComment(String id) {

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "COMMENT WITH ID " + id + " NOT FOUND"));

        commentRepository.delete(existingComment);
    }

    //METHOD TO UPDATE AN EXISTING COMMENT
    public Comment updateComment(String id, String text) {

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "COMMENT WITH ID " + id + " NOT FOUND"));

        existingComment.setText(text);
        existingComment.setDatetime(LocalDateTime.now());

        return commentRepository.save(existingComment);
    }

    //METHOD TO GET COMMENTS BY AUTHOR
    public Page<Comment> getCommentsByAuthor(String authorId, int page, int size) {

        boolean userExists = userRepository.existsById(authorId);

        if (!userExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("AUTHOR WITH ID %s NOT FOUND", authorId));
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByAuthor(authorId, pageRequest);

        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("NO COMMENTS FOUND FOR THE AUTHOR WITH ID %s", authorId));
        }
        return comments;
    }

    //METHOD TO GET COMMENTS BY DATE
    public Page<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }

    //METHOD TO GET COMMENTS BY POST ID
    public Slice<Comment> getCommentsByPostId(ObjectId postId, int page, int size) {
        if (!postRepository.existsById(String.valueOf(postId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POST WITH ID:" + postId + "NOT FOUND.");
        }

        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findCommentsByPostId(postId, pageable);
    }
}







