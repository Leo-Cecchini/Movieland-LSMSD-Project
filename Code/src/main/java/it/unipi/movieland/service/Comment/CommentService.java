package it.unipi.movieland.service.Comment;

import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.model.Post.PostComment;
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
import java.security.SecureRandom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    private final CommentMongoDBRepository commentRepository;
    private final MongoTemplate mongoTemplate;

    // Costanti per generazione ID
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final PostMongoDBRepository postMongoDBRepository;
    private final UserMongoDBRepository userMongoDBRepository;

    @Autowired
    public CommentService(CommentMongoDBRepository commentRepository, MongoTemplate mongoTemplate, PostMongoDBRepository postMongoDBRepository, UserMongoDBRepository userMongoDBRepository) {
        this.commentRepository = commentRepository;
        this.mongoTemplate = mongoTemplate;
        this.postMongoDBRepository = postMongoDBRepository;
        this.userMongoDBRepository = userMongoDBRepository;
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

    //METODO PER CREARE UN COMMENTO
    public Comment createComment(String text, String authorId, String postId) {

        if (!userMongoDBRepository.existsById(authorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autore non trovato nel database.");
        }

        Optional<Post> postOptional = postMongoDBRepository.findById(postId);
        if (!postOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post non trovato.");
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(authorId);
        String uniqueCommentId = generateUniqueRandomId(7);  // Metodo per generare ID unico
        comment.setId(uniqueCommentId);

        Comment savedComment = commentRepository.save(comment);

        Post post = postOptional.get();
        PostComment postComment = new PostComment(savedComment.getId(), savedComment.getDatetime());
        post.addComment(postComment);

        postMongoDBRepository.save(post);
        return savedComment;
    }

    //METODO PER RECUPERARE TUTTI I COMMENTI
    public Page<Comment> getAllComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    //METOOD PER RECUPERARE UN COMMENTO PER ID
    public Comment getCommentById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }

    /*
    // Metodo per eliminare un commento per ID
    public void deleteComment(String id) {
        // Trova il commento nella collezione dei commenti
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento con ID " + id + " non trovato"));

        // Elimina il commento dalla collezione dei commenti
        commentRepository.delete(existingComment);

        // Rimuovi il commento da tutti i post che lo contengono
        removeCommentFromPosts(id);
    }



    // Metodo per rimuovere il commento dalla lista dei commenti nei post
    private void removeCommentFromPosts(String commentId) {
        // Trova tutti i post che contengono l'ID del commento
        List<Post> posts = postRepository.findByCommentsContaining(commentId);

        // Per ogni post, rimuovi il commento dalla lista dei commenti
        for (Post post : posts) {
            post.getComments().remove(commentId);
            postRepository.save(post); // Salva il post aggiornato
        }
    }

*/


    /*
    // Metodo per eliminare un commento per ID
    public void deleteComment(String id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento con ID " + id + " non trovato"));
        commentRepository.delete(existingComment);
    }
*/
    //METODO PER AGGIORNARE UN COMMENTO ESISTENTE
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