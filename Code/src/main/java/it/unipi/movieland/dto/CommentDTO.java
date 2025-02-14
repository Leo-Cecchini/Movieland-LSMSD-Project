package it.unipi.movieland.dto;

public class CommentDTO {
    private String commentId;

    // Costruttore
    public CommentDTO(String commentId) {
        this.commentId = commentId;
    }

    // Getters e Setters
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
