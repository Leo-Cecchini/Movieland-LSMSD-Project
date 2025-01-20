package it.unipi.movieland.repository.Comment;

import it.unipi.movieland.model.Comment.Comment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepository {

    private List<Comment> commentList = new ArrayList<>();

    // Ottieni tutti i commenti
    public List<Comment> getAllComments() {
        return commentList;
    }

    // Ottieni un commento per ID
    public Optional<Comment> getCommentById(String id) {
        return commentList.stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst();
    }

    // Aggiungi un nuovo commento
    public Comment addComment(Comment comment) {
        commentList.add(comment);
        return comment;
    }

    // Modifica un commento esistente
    public Comment updateComment(String id, Comment updatedComment) {
        Comment existingComment = getCommentById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        existingComment.setText(updatedComment.getText());
        existingComment.setDatetime(updatedComment.getDatetime());
        return existingComment;
    }

    // Elimina un commento
    public void deleteComment(String id) {
        Comment comment = getCommentById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentList.remove(comment);
    }
}