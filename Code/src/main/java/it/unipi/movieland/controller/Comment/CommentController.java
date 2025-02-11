package it.unipi.movieland.controller.Comment;

import it.unipi.movieland.model.Comment.Comment;
import it.unipi.movieland.service.Comment.CommentService;
import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserMongoDBRepository userRepository;
    private final PostMongoDBRepository postRepository;

    @Autowired
    public CommentController(CommentService commentService, UserMongoDBRepository userRepository, PostMongoDBRepository postRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    //ENDPOINT PER CREARE UN COMMENTO (MONGODB)
    @PostMapping
    public Comment createComment(@RequestParam String text,
                                 @RequestParam String authorId,
                                 @RequestParam String postId) {
        // Verifica che l'autore esista
        if (!userRepository.existsById(authorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autore non trovato nel database.");
        }

        // Verifica che il post esista
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post non trovato.");
        }

        // Crea il commento e associa l'ID del post
        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(authorId);
        comment.setId(postId); // Associa il commento al post

        // Salva il commento
        return commentService.createComment(comment);
    }

    //ENDPOINT PER RECUPERARE TUTTI I COMMENTI (MONGODB)
    @GetMapping
    public Page<Comment> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        return commentService.getAllComments(page, size);
    }

    //ENDPOINT PER RECUPERARE UN COMMENTO PER ID (MONGODB)
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable String id) {
        return commentService.getCommentById(id);
    }

    //ENDPOINT PER ELIMINARE UN COMMENTO PER ID (MONGODB)
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
    }

    //ENDPOINT PER MODIFICARE UN COMMENTO PER ID (MONGODB)
    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable String id, @RequestParam String text) {
        // Passa solo il testo nel servizio
        return commentService.updateComment(id, text);
    }

    //ENDPOINT PER OTTENERE COMMENTI PER AUTORE CON PAGINAZIONE
    @GetMapping("/author/{author}")
    public Page<Comment> getCommentsByAuthor(
            @PathVariable String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
        ) {
        return commentService.getCommentsByAuthor(author, page, size);
        }

    //ENDPOINT PER CERCARE I COMMENTI IN UN RANGE DI DATE
    @GetMapping("/byDateRange")
    public Page<Comment> getCommentsByDateRange(
            @Parameter(description = "Start data in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String startDate,

            @Parameter(description = "End data in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        return commentService.getCommentsByDateRange(start, end, page, size);
    }
}