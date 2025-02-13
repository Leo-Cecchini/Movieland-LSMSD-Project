package it.unipi.movieland.service.Comment;

import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import it.unipi.movieland.model.Comment.Comment;
import it.unipi.movieland.repository.Comment.CommentMongoDBRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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

    //METODO PER CREARE UN COMMENTO
    public Comment createComment(String text, String authorId,String postId) {

        if (!userRepository.existsById(authorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id : " + authorId + " not found.");
        }

        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id :" + postId + "not found.");
        }

        Comment comment = new Comment(text,authorId,postId);
        return commentRepository.save(comment);
    }

    //METODO PER RECUPERARE TUTTI I COMMENTI
    public Page<Comment> getAllComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    //METODO PER RECUPERARE UN COMMENTO PER ID
    public Comment getCommentById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with ID " + id + " not found");
        }

        return comment.get();
    }

    //METODO PER ELIMINARE UN COMMENTO PER ID
    public void deleteComment(String id) {

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with ID " + id + " not found"));

        commentRepository.delete(existingComment);
    }

    //METODO PER AGGIORNARE UN COMMENTO ESISTENTE
    public Comment updateComment(String id, String text) {

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with ID " + id + " not found"));

        existingComment.setText(text);
        existingComment.setDatetime(LocalDateTime.now());

        return commentRepository.save(existingComment);
    }

    //METODO PER OTTENERE I COMMENTO PER AUTORE
    public Page<Comment> getCommentsByAuthor(String authorId, int page, int size) {

        boolean userExists = userRepository.existsById(authorId);

        if (!userExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Author with ID %s not found", authorId));
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByAuthor(authorId, pageRequest);

        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No comments found for the author with ID %s", authorId));
        }
        return comments;
    }

    //METODO PER OTTENERE I COMMENTO PER DATA
    public Page<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }

    //METODO PER OTTENERE I COMMENTI TRAMITE ID DEL POST
    public Slice<Comment> getCommentsByPostId(ObjectId postId, int page, int size) {
        if (!postRepository.existsById(String.valueOf(postId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id :" + postId + "not found.");
        }

        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findCommentsByPostId(postId, pageable);
    }
}