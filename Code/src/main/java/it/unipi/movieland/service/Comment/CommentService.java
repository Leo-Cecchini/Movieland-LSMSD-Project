package it.unipi.movieland.service.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unipi.movieland.model.Comment.Comment;
import it.unipi.movieland.repository.Comment.CommentMongoDBRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.security.SecureRandom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class CommentService {

    private final CommentMongoDBRepository commentRepository;
    private final MongoTemplate mongoTemplate;

    // Costanti per generazione ID
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    public CommentService(CommentMongoDBRepository commentRepository,  MongoTemplate mongoTemplate) {
        this.commentRepository = commentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    // Metodo per generare un ID unico
    private String generateUniqueRandomId(int length) {
        String generatedId;
        do {
            StringBuilder sb = new StringBuilder("CC");
            for (int i = 0; i < length; i++) {
                sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
            }
            generatedId = sb.toString();
        } while (mongoTemplate.exists(Query.query(Criteria.where("_id").is(generatedId)), Comment.class));

        return generatedId;
    }

    // Metodo per creare un commento
    public Comment createComment(Comment comment) {
        // Genera un ID unico per il commento
        String uniqueId = generateUniqueRandomId(7); // Scegli la lunghezza dell'ID
        comment.setId(uniqueId);

        // Salva il commento nel database
        return commentRepository.save(comment);
    }

    // Metodo per recuperare tutti i commenti
    public Page<Comment> getAllComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    // Metodo per recuperare un commento per ID
    public Comment getCommentById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }

    // Metodo per eliminare un commento per ID
    public void deleteComment(String id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento con ID " + id + " non trovato"));
        commentRepository.delete(existingComment);
    }

    // Metodo per aggiornare un commento esistente
    public Comment updateComment(String id, String text) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento con ID " + id + " non trovato"));

        String originalAuthor = existingComment.getAuthor();
        String originalId = existingComment.getId();

        existingComment.setDatetime(LocalDateTime.now());
        existingComment.setText(text);
        existingComment.setAuthor(originalAuthor);
        existingComment.setId(originalId);

        return commentRepository.save(existingComment);
    }

    // Metodo per ottenere i commenti in base all'autore
    public Page<Comment> getCommentsByAuthor(String author, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByAuthor(author, pageRequest);
    }

    // Metodo per ottenere i commenti in base alla data
    public Page<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }
}