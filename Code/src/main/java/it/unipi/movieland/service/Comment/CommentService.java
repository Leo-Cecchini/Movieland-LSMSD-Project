package applicationMovieland.service.Comment;

import applicationMovieland.model.Comment.Comment;
import applicationMovieland.repository.Comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // Ottieni tutti i commenti
    public List<Comment> getAllComments() {
        return commentRepository.getAllComments();
    }

    // Ottieni un commento per ID
    public Optional<Comment> getCommentById(String id) {
        return commentRepository.getCommentById(id);
    }

    // Aggiungi un nuovo commento
    public Comment addComment(Comment comment) {
        return commentRepository.addComment(comment);
    }

    // Modifica un commento esistente
    public Comment updateComment(String id, Comment updatedComment) {
        return commentRepository.updateComment(id, updatedComment);
    }

    // Elimina un commento
    public void deleteComment(String id) {
        commentRepository.deleteComment(id);
    }
}