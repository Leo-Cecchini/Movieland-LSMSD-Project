package applicationMovieland.controller.Comment;

import applicationMovieland.model.Comment.Comment;
import applicationMovieland.service.Comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Endpoint per ottenere tutti i commenti
    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    // Endpoint per ottenere un commento per ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere un nuovo commento
    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        Comment addedComment = commentService.addComment(comment);
        return ResponseEntity.ok(addedComment);
    }

    // Endpoint per aggiornare un commento esistente
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody Comment updatedComment) {
        Comment updated = commentService.updateComment(id, updatedComment);
        return ResponseEntity.ok(updated);
    }

    // Endpoint per eliminare un commento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
