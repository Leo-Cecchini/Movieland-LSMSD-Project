package it.unipi.movieland.service.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unipi.movieland.dto.CommentDTO;
import it.unipi.movieland.model.Comment.Comment;
import it.unipi.movieland.repository.Comment.CommentMongoDBRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;

//
@Service
public class CommentService {

    private final CommentMongoDBRepository commentRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CommentService(CommentMongoDBRepository commentRepository,  MongoTemplate mongoTemplate) {
        this.commentRepository = commentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    //METODO PER CREARE UN COMMENTO
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment); // Salva il commento nel database
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
    public Comment updateComment(String id, CommentDTO updateDTO) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento con ID " + id + " non trovato"));

        String originalAuthor = existingComment.getAuthor();
        String originalId = existingComment.getId();

        existingComment.setDatetime(LocalDateTime.now());

        existingComment.setText(updateDTO.getText()); // Usa il testo dal DTO

        existingComment.setAuthor(originalAuthor);
        existingComment.setId(originalId);

        return commentRepository.save(existingComment);
    }

    //METODO PER OTTENERE I COMMENTI IN BASE ALL'AUTORE
    public Page<Comment> getCommentsByAuthor(String author, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return commentRepository.findByAuthor(author, pageRequest);
    }

    //METODO PER OTTENERE I COMMENTI IN BASE ALLA DATA
    public Page<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }
}