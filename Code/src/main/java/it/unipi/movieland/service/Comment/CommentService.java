package it.unipi.movieland.service.Comment;

import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import it.unipi.movieland.model.Comment.Comment;
import it.unipi.movieland.repository.Comment.CommentMongoDBRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autore non trovato nel database.");
        }

        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post non trovato.");
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(authorId);
        comment.setId(postId); // Associa il commento al post
        return commentRepository.save(comment);
    }

    //METODO PER RECUPERARE TUTTI I COMMENTI
    public Page<Comment> getAllComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    //METODO PER RECUPERARE UN COMMENTO PER ID
    public Comment getCommentById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }

    //METODO PER ELIMINARE UN COMMENTO PER ID
    public void deleteComment(String id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento con ID " + id + " non trovato"));
        commentRepository.delete(existingComment);
    }

    //METODO PER AGGIORNARE UN COMMENTO ESISTENTE
    public Comment updateComment(String id, String text) {

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento con ID " + id + " non trovato"));

        existingComment.setText(text);
        existingComment.setDatetime(LocalDateTime.now());

        return commentRepository.save(existingComment);
    }

    //METODO PER OTTENERE I COMMENTO PER AUTORE
    public Page<Comment> getCommentsByAuthor(String author, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByAuthor(author, pageRequest);
    }

    //METODO PER OTTENERE I COMMENTO PER DATA
    public Page<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }
}